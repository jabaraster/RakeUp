/**
 * 
 */
package jabara.rakeup;

import org.apache.log4j.Logger;

/**
 * 
 * @author jabaraster
 */
public final class RakeUpEnv {

    /**
     * 
     */
    public static final String ENV_PASSWORD = "RAKE_UP_PASSWORD"; //$NON-NLS-1$

    private RakeUpEnv() {
        // 処理なし
    }

    /**
     * @return ログインパスワード.
     */
    public static String getPassword() {
        final String p = System.getenv(ENV_PASSWORD);
        Logger.getLogger(RakeUpEnv.class).warn(p);
        return p == null ? "password" : p; //$NON-NLS-1$
    }
}
