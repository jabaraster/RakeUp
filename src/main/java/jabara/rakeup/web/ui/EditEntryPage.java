/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.general.NotFound;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.service.EntryService;
import jabara.rakeup.web.ui.component.EntryPanel;
import jabara.rakeup.web.ui.component.ErrorClassAppender;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;

import com.google.inject.Inject;

/**
 * 
 * @author jabaraster
 */
public class EditEntryPage extends RakeUpWebPageBase {
    private static final long        serialVersionUID   = -71314295479777894L;

    @Inject
    EntryService                     entryService;

    private EEntry                   entryValue;

    private final ErrorClassAppender errorClassAppender = new ErrorClassAppender("error"); //$NON-NLS-1$

    private Form<?>                  entryForm;
    private FeedbackPanel            feedback;
    private EntryPanel               entry;
    private AjaxButton               submitter;

    /**
     * @param pParameters
     */
    public EditEntryPage(final PageParameters pParameters) {
        super(pParameters);

        final StringValue value = pParameters.get(0);

        try {
            final long entryId = value.toLong();
            this.entryValue = this.entryService.findById(entryId);

        } catch (final StringValueConversionException _) {
            throw new RestartResponseException(IndexPage.class);

        } catch (final NotFound e) {
            throw new RestartResponseException(IndexPage.class);
        }

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

    @SuppressWarnings("serial")
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    EditEntryPage.this.errorClassAppender.addErrorClass(getEntryForm());
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
        this.entryService.update(this.entryValue);
        this.setResponsePage(IndexPage.class);
    }
}
