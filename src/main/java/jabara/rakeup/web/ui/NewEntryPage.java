/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.service.EntryService;
import jabara.rakeup.web.ui.component.EntryPanel;
import jabara.rakeup.web.ui.component.ErrorClassAppender;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.inject.Inject;

/**
 * 
 * @author jabaraster
 */
public class NewEntryPage extends RakeUpWebPageBase {
    private static final long        serialVersionUID   = -4814540956069264138L;

    @Inject
    EntryService                     entryService;

    private final EEntry             entryValue         = new EEntry();

    private final ErrorClassAppender errorClassAppender = new ErrorClassAppender("error"); //$NON-NLS-1$

    private Form<?>                  entryForm;
    private FeedbackPanel            feedback;
    private EntryPanel               entry;
    private AjaxButton               submitter;

    /**
     * 
     */
    public NewEntryPage() {
        this.add(getEntryForm());
    }

    /**
     * @see jabara.rakeup.web.ui.RakeUpWebPageBase#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.renderOnDomReadyJavaScript("RakeUp.focus('" + getEntry().getTitle().getMarkupId() + "')"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see jabara.rakeup.web.ui.RakeUpWebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new Model<String>(this.getClass().getSimpleName());
    }

    private EntryPanel getEntry() {
        if (this.entry == null) {
            this.entry = new EntryPanel("entry", this.entryValue); //$NON-NLS-1$
        }
        return this.entry;
    }

    private Form<?> getEntryForm() {
        if (this.entryForm == null) {
            this.entryForm = new Form<Object>("entryForm"); //$NON-NLS-1$
            this.entryForm.setOutputMarkupId(true);

            this.entryForm.add(getFeedback());
            this.entryForm.add(getEntry());
            this.entryForm.add(getSubmitter());
        }
        return this.entryForm;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
            this.feedback.setVisible(false);
        }
        return this.feedback;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") {
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    NewEntryPage.this.errorClassAppender.addErrorClass(getEntryForm());
                    pTarget.add(getEntryForm());
                }

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(@SuppressWarnings("unused") final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    onSubmitterClick();
                }
            };

        }
        return this.submitter;
    }

    private void onSubmitterClick() {
        this.entryService.insert(this.entry.getEntry());
        this.setResponsePage(IndexPage.class);
    }

}
