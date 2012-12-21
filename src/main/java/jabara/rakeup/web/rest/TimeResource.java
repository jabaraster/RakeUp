/**
 * 
 */
package jabara.rakeup.web.rest;

import java.io.Serializable;
import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author jabaraster
 */
@Path("time")
public class TimeResource {

    /**
     * @return 現在時刻.
     */
    @SuppressWarnings("static-method")
    @Path("now")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Time getNow() {
        return new Time(Calendar.getInstance().getTime().toString());
    }

    /**
     * 
     * @author jabaraster
     */
    public static class Time implements Serializable {
        private static final long serialVersionUID = -3531751856339387124L;

        private String            now;

        /**
         * 
         */
        public Time() {
            //
        }

        /**
         * @param pNow 現在時刻
         */
        public Time(final String pNow) {
            this.now = pNow;
        }

        /**
         * @return the now
         */
        public String getNow() {
            return this.now;
        }

        /**
         * @param pNow the now to set
         */
        public void setNow(final String pNow) {
            this.now = pNow;
        }
    }
}
