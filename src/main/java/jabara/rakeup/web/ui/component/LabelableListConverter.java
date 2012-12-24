/**
 * 
 */
package jabara.rakeup.web.ui.component;

import jabara.general.ArgUtil;
import jabara.general.IProducer2;
import jabara.rakeup.model.ILabelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.util.convert.IConverter;

/**
 * 
 * @author jabaraster
 * @param <E> 変換対象の型.
 */
public class LabelableListConverter<E extends ILabelable> implements IConverter<List<E>> {
    private static final long                      serialVersionUID = 3167579504225939678L;

    private final IProducer2<Set<String>, List<E>> entityGenerator;

    /**
     * @param pEntityGenerator
     */
    public LabelableListConverter(final IProducer2<Set<String>, List<E>> pEntityGenerator) {
        ArgUtil.checkNull(pEntityGenerator, "pEntityGenerator"); //$NON-NLS-1$
        this.entityGenerator = pEntityGenerator;
    }

    /**
     * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
     */
    @Override
    public List<E> convertToObject(final String pValue, @SuppressWarnings("unused") final Locale pLocale) {
        if (pValue == null) {
            return new ArrayList<E>();
        }

        final Set<String> labels = parseLabels(pValue);
        final List<E> list = new ArrayList<E>();
        list.addAll(this.entityGenerator.produce(labels));
        return list;
    }

    /**
     * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
     */
    @Override
    public String convertToString(final List<E> pValue, @SuppressWarnings("unused") final Locale pLocale) {
        if (pValue.isEmpty()) {
            return ""; //$NON-NLS-1$
        }

        final String SEPARATOR = ", "; //$NON-NLS-1$
        final StringBuilder sb = new StringBuilder();
        for (final E entity : pValue) {
            if (entity == null) {
                continue;
            }
            sb.append(entity.getLabel()).append(SEPARATOR);
        }
        sb.delete(sb.length() - SEPARATOR.length(), sb.length());
        return new String(sb);
    }

    private static Set<String> parseLabels(final String pValue) {
        final Set<String> ret = new HashSet<String>();
        for (final String label : pValue.split(",")) { //$NON-NLS-1$
            if (label.length() == 0) {
                continue;
            }
            ret.add(label.trim());
        }
        return ret;
    }

}
