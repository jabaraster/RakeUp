/**
 * 
 */
package jabara.rakeup;

/**
 * アプリケーションの基本情報.
 * 
 * @author jabaraster
 */
public final class RakeUpBasic {

    private static final String APPLICATION_NAME                              = "RakeUp"; //$NON-NLS-1$
    private static final String APPLICATION_NAME_IN_JAVA_CONSTANT_NAME_FORMAT = "RAKE_UP"; //$NON-NLS-1$

    private RakeUpBasic() {
        //
    }

    /**
     * @return アプリケーション名.
     */
    public static String getApplicationName() {
        return APPLICATION_NAME;
    }

    /**
     * @return Javaの定数名形式のアプリケーション名.
     */
    public static String getApplicationNameInJavaConstantNameFormat() {
        return APPLICATION_NAME_IN_JAVA_CONSTANT_NAME_FORMAT;
    }
}
