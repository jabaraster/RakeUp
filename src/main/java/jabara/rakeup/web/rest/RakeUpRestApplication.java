/**
 * 
 */
package jabara.rakeup.web.rest;

import jabara.jax_rs.JsonMessageBodyReaderWriter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * 
 * @author jabaraster
 */
public class RakeUpRestApplication extends Application {

    /**
     * @see javax.ws.rs.core.Application#getClasses()
     */
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { //
                JsonMessageBodyReaderWriter.class // JSONをきれいに返すにはこのクラスが必要.
                }));
    }

    /**
     * @see javax.ws.rs.core.Application#getSingletons()
     */
    @Override
    public Set<Object> getSingletons() {
        return new HashSet<Object>(Arrays.asList(new Object[] { //
                new TimeResource() //
                }));
    }
}
