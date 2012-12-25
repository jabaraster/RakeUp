/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.rakeup.web.ui.FailAuthentication;
import jabara.rakeup.web.ui.RakeUpSession;
import jabara.rakeup.web.ui.component.JavaScriptUtil;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public class LoginPage extends WebPage {
    private static final long serialVersionUID = -8987437226988765476L;

    private StatelessForm<?>  form;
    private FeedbackPanel     feedback;
    private PasswordTextField password;
    private AjaxButton        submitter;

    /**
     * 
     */
    public LoginPage() {
        this.add(getForm());

        setStatelessHint(true);
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        RakeUpWebPageBase.renderCommonHead(pResponse);
        pResponse.renderOnDomReadyJavaScript(JavaScriptUtil.getFocusScript(getPassword()));
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
            this.feedback.setOutputMarkupId(true);
        }
        return this.feedback;
    }

    private StatelessForm<?> getForm() {
        if (this.form == null) {
            this.form = new StatelessForm<Object>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getPassword());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            this.password = new PasswordTextField("password", new Model<String>()); //$NON-NLS-1$
            this.password.setOutputMarkupId(true);
            this.password.setRequired(true);
        }
        return this.password;
    }

    @SuppressWarnings({ "serial", "nls" })
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") {
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    pTarget.add(getFeedback());
                }

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    onSubmitterClick(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private void onSubmitterClick(final AjaxRequestTarget pTarget) {
        final RakeUpSession session = (RakeUpSession) Session.get();
        try {
            session.authenticate(getPassword().getModelObject());
            this.setResponsePage(IndexPage.class);

        } catch (final FailAuthentication e) {
            error(getString("failAuthentication")); //$NON-NLS-1$
            pTarget.add(getFeedback());
            pTarget.appendJavaScript(JavaScriptUtil.getFocusScript(getPassword()));
        }
    }
}
