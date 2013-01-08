import jabara.jetty_memcached.MemcachedSessionServerStarter;

/**
 * 
 */

/**
 * 
 * @author jabaraster
 */
public class RakeUpWebStarter {
    /**
     * @param pArgs 起動引数.
     */
    public static void main(final String[] pArgs) {
        new MemcachedSessionServerStarter().start();
    }
}
