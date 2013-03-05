/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;

import java.lang.reflect.Field;

import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * @author jabaraster
 */
public final class ValidatorUtil {

    private ValidatorUtil() {
        //
    }

    /**
     * @param <T>
     * @param pCheckTargetObjectType
     * @param pPropertyName
     * @return
     */
    public static <T> IValidator<String> createStringValidator(final Class<T> pCheckTargetObjectType, final SingularAttribute<T, String> pPropertyName) {
        ArgUtil.checkNull(pCheckTargetObjectType, "pCheckTargetObjectType"); //$NON-NLS-1$
        ArgUtil.checkNull(pPropertyName, "pPropertyName"); //$NON-NLS-1$

        try {
            final Field field = getField(pCheckTargetObjectType, pPropertyName);
            final Size size = field.getAnnotation(Size.class);
            if (size == null) {
                throw new IllegalStateException("@" + Size.class.getSimpleName() + "がフィールドに付与されていないため、このメソッドを利用出来ません."); //$NON-NLS-1$//$NON-NLS-2$
            }

            return new StringValidator(Integer.valueOf(size.min()), Integer.valueOf(size.max()));

        } catch (final Exception e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @param <T>
     * @param pCheckTargetObjectType
     * @param pPropertyName
     * @return
     */
    public static <T> boolean isRequired(final Class<T> pCheckTargetObjectType, final SingularAttribute<T, ?> pPropertyName) {
        ArgUtil.checkNull(pCheckTargetObjectType, "pCheckTargetObjectType"); //$NON-NLS-1$
        ArgUtil.checkNull(pPropertyName, "pPropertyName"); //$NON-NLS-1$
        final Field field = getField(pCheckTargetObjectType, pPropertyName);
        return field.isAnnotationPresent(NotNull.class);
    }

    private static Field getField(final Class<?> pCheckTargetObjectType, final SingularAttribute<?, ?> pPropertyName) {
        try {
            return pCheckTargetObjectType.getDeclaredField(pPropertyName.getName());
        } catch (final NoSuchFieldException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}
