import jabara.jetty_memcached.MemcachedSessionServerStarter;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.Resource;

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
        new Resource("", null);
        new MemcachedSessionServerStarter().start();
    }
}
