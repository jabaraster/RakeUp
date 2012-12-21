/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.service.DI;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * 
 * @author jabaraster
 */
public class RakeUpWicketApplication extends WebApplication {

    private static final String ENC = "UTF-8"; //$NON-NLS-1$

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return IndexPage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());

        // GuiceによるDI機能をWicketに対して有効にする仕込み.
        // これによりDaoBaseクラスを継承したクラスのpublicメソッドはトランザクションが自動ではられるようになる.
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, DI.getGuiceInjector()));
    }
}
