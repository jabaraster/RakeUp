/**
 * 
 */
package jabara.rakeup.model;

import jabara.general.ArgUtil;
import jabara.jpa.util.SystemPropertyToPostgreJpaPropertiesParser;
import jabara.jpa_guice.SinglePersistenceUnitJpaModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author jabaraster
 */
public final class DI {

    /**
     * 
     */
    public static final String    PERSISTENCE_UNIT_NAME = "mainPersistenceUnit"; //$NON-NLS-1$

    private static final Injector _injector             = createInjector();

    private DI() {
        // 　処理なし
    }

    /**
     * 指定のオブジェクトを取得します. <br>
     * このメソッドを使っていいのは次のクラスだけです. <br>
     * <ul>
     * <li>JAX-RSのリソースクラス</li>
     * </ul>
     * 
     * @param pType 取得するオブジェクトの型.
     * @return オブジェクト.
     * @param <T> 取得するオブジェクトの型.
     */
    public static <T> T get(final Class<T> pType) {
        ArgUtil.checkNull(pType, "pType"); //$NON-NLS-1$
        return _injector.getInstance(pType);
    }

    /**
     * @return Google Guiceの{@link Injector}を返します.
     */
    public static Injector getGuiceInjector() {
        return _injector;
    }

    private static Injector createInjector() {
        final SinglePersistenceUnitJpaModule jpaModule = new SinglePersistenceUnitJpaModule(PERSISTENCE_UNIT_NAME,
                new SystemPropertyToPostgreJpaPropertiesParser());
        return Guice.createInjector(jpaModule);
    }
}
