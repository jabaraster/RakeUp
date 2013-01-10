import jabara.jetty_memcached.MemcachedSessionServerStarter;

import javax.naming.NamingException;

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
     * @throws NamingException
     */
    public static void main(final String[] pArgs) throws NamingException {
        new MemcachedSessionServerStarter().start();
    }
}
