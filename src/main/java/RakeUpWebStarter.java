import jabara.rakeup.memcached.MemcachedClient;

import java.io.IOException;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.nosql.kvs.AbstractKeyValueStoreClient;
import org.eclipse.jetty.nosql.memcached.AbstractMemcachedClientFactory;
import org.eclipse.jetty.nosql.memcached.MemcachedSessionIdManager;
import org.eclipse.jetty.nosql.memcached.MemcachedSessionManager;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 * @author jabaraster
 */
public class RakeUpWebStarter {
    /**
     * 
     */
    public static final String KEY_WEB_PORT           = "web.port";          //$NON-NLS-1$
    /**
     * 
     */
    public static final String KEY_MEMCACHED_SERVERS  = "memcached.servers"; //$NON-NLS-1$
    /**
     * 
     */
    public static final String KEY_MEMCACHED_USERNAME = "memcached.username"; //$NON-NLS-1$
    /**
     * 
     */
    public static final String KEY_MEMCACHED_PASSWORD = "memcached.password"; //$NON-NLS-1$

    /**
     * 
     */
    public static final int    DEFAULT_WEB_PORT       = 8081;

    /**
     * @param pArgs -
     * @throws Exception -
     */
    public static void main(final String[] pArgs) throws Exception {
        final Server server = createServer();
        server.start();
        server.join();
    }

    private static WebAppContext createContext(final Server pServer, final String pWebAppDirectory) throws IOException {
        final WebAppContext context = new WebAppContext();
        context.setConfigurations(new Configuration[] { //
        new AnnotationConfiguration() //
                , new WebXmlConfiguration() //
                , new WebInfConfiguration() //
                , new TagLibConfiguration() //
                , new PlusConfiguration() //
                , new MetaInfConfiguration() //
                , new FragmentConfiguration() //
                , new EnvConfiguration() //
        });
        context.setContextPath("/"); //$NON-NLS-1$
        context.setDescriptor(pWebAppDirectory + "/WEB-INF/web.xml"); //$NON-NLS-1$
        context.setResourceBase(pWebAppDirectory);
        context.setParentLoaderPriority(true);

        if (hasMemcachedServersDirective()) {
            context.setSessionHandler(createSessionHandler(pServer));
        }

        return context;
    }

    private static MemcachedSessionIdManager createMemcachedSessionIdManager(final Server pServer) throws IOException {
        final String serverString = System.getProperty(KEY_MEMCACHED_SERVERS);
        final MemcachedSessionIdManager memcachedSessionIdManager = new MemcachedSessionIdManager(pServer, serverString,
                new AbstractMemcachedClientFactory() {
                    @Override
                    public AbstractKeyValueStoreClient create(final String pServerString) {
                        final String username = System.getProperty(KEY_MEMCACHED_USERNAME);
                        if (username == null) {
                            return new MemcachedClient(pServerString);
                        }
                        final String password = System.getProperty(KEY_MEMCACHED_PASSWORD);
                        return new MemcachedClient(pServerString, username, password);
                    }
                });
        memcachedSessionIdManager.setKeyPrefix("session:"); //$NON-NLS-1$

        pServer.setSessionIdManager(memcachedSessionIdManager);

        return memcachedSessionIdManager;
    }

    private static Server createServer() throws IOException {
        final String webAppDirectory = "src/main/webapp/"; //$NON-NLS-1$
        final Server server = new Server(getWebPort());
        server.setHandler(createContext(server, webAppDirectory));
        return server;
    }

    private static SessionHandler createSessionHandler(final Server pServer) throws IOException {
        final MemcachedSessionManager memcachedSessionManager = new MemcachedSessionManager();
        memcachedSessionManager.setSessionIdManager(createMemcachedSessionIdManager(pServer));
        return new SessionHandler(memcachedSessionManager);
    }

    private static int getWebPort() {
        final String webPort = System.getProperty(KEY_WEB_PORT);
        if (webPort == null) {
            return DEFAULT_WEB_PORT;
        }
        return Integer.parseInt(webPort);
    }

    private static boolean hasMemcachedServersDirective() {
        return System.getProperty(KEY_MEMCACHED_SERVERS) != null;
    }
}