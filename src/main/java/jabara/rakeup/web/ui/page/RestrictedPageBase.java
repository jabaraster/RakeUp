/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.rakeup.web.ui.RakeUpWicketApplication;
import jabara.rakeup.web.ui.component.CommonMenu;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public abstract class RestrictedPageBase extends RakeUpWebPageBase {
    private static final long serialVersionUID = -307839716915900637L;

    /**
     * 
     */
    public RestrictedPageBase() {
        //
    }

    /**
     * @param pParameters パラメータ.
     */
    public RestrictedPageBase(final PageParameters pParameters) {
        super(pParameters);
    }

    /**
     * @see jabara.rakeup.web.ui.page.RakeUpWebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pId) {
        return new CommonMenu(pId, RakeUpWicketApplication.getNavigationLinks());
    }
}
