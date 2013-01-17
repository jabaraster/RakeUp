/**
 * 
 */
package jabara.rakeup.web.ui.page;

import org.apache.wicket.Session;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;

/**
 * 何もやることがないこのクラスは排除したいのだが・・・
 * 
 * @author jabaraster
 */
public class LogoutPage extends WebPage {
    private static final long serialVersionUID         = -3810270407936165942L;

    private static final int  REFRESH_INTERVAL_MINUTES = 5;

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        RakeUpWebPageBase.renderCommonHead(pResponse);
        RakeUpWebPageBase.addPageCssReference(pResponse, this.getClass());

        pResponse.render(OnDomReadyHeaderItem.forScript("countDown(" + REFRESH_INTERVAL_MINUTES + ")")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see org.apache.wicket.markup.html.WebPage#onAfterRender()
     */
    @Override
    protected void onAfterRender() {
        super.onAfterRender();
        Session.get().invalidate();
    }
}
