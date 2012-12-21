/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EEntry_;
import jabara.rakeup.service.EntryService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
public class IndexPage extends WebPage {
    private static final long  serialVersionUID = -3725581870632049658L;

    @Inject
    EntryService               sampleService;

    private final AtomicLong   counterValue     = new AtomicLong(0);

    private final List<EEntry> samplesValue     = new ArrayList<EEntry>();

    private Label              label;

    private Form<?>            form;
    private AjaxButton         updater;

    private Form<?>            insertForm;
    private Label              sampleCount;

    private FeedbackPanel      feedback;

    private TextField<String>  text;
    private AjaxButton         inserter;
    private ListView<EEntry>   samples;
    private WebMarkupContainer samplesContainer;

    /**
     * 
     */
    public IndexPage() {
        this.add(getLabel());
        this.add(getForm());
        this.add(getSampleCount());
        this.add(getInsertForm());
        this.add(getSamplesContainer());

        this.samplesValue.addAll(this.sampleService.getAll());
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
            this.feedback.setOutputMarkupId(true); // Ajaxで扱うコンポーネントはこのoutputMarkupIdをtrueにする必要があります. <br>
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<Object>("form"); //$NON-NLS-1$
            this.form.add(getUpdater());
        }
        return this.form;
    }

    @SuppressWarnings({ "serial", "nls" })
    private AjaxButton getInserter() {
        if (this.inserter == null) {
            this.inserter = new IndicatingAjaxButton("inserter") { // IndicatingAjaxButtonはAjax処理中にぐるぐるアニメが表示されるボタン.
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    pTarget.add(getFeedback());
                }

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    onInserterClick(pTarget);
                }
            };
        }
        return this.inserter;
    }

    private Form<?> getInsertForm() {
        if (this.insertForm == null) {
            this.insertForm = new Form<Object>("insertForm"); //$NON-NLS-1$
            this.insertForm.add(getFeedback());
            this.insertForm.add(getText());
            this.insertForm.add(getInserter());
        }
        return this.insertForm;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Label getLabel() {
        if (this.label == null) {

            this.label = new Label("label", new AbstractReadOnlyModel<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    return String.valueOf(IndexPage.this.counterValue.incrementAndGet());
                }
            });
            this.label.setOutputMarkupId(true); // Ajaxで利用.
        }
        return this.label;
    }

    @SuppressWarnings({ "nls", "serial" })
    private Label getSampleCount() {
        if (this.sampleCount == null) {
            this.sampleCount = new Label("sampleCount", new AbstractReadOnlyModel<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    return String.valueOf(getSampleCountByDb());
                }
            });
            this.sampleCount.setOutputMarkupId(true); // Ajaxで利用.
        }
        return this.sampleCount;
    }

    private int getSampleCountByDb() {
        return this.sampleService.countAll();
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<EEntry> getSamples() {
        if (this.samples == null) {
            this.samples = new ListView<EEntry>("samples", Model.ofList(this.samplesValue)) {
                @Override
                protected void populateItem(final ListItem<EEntry> pItem) {
                    pItem.setModel(new CompoundPropertyModel<EEntry>(pItem.getModelObject()));
                    pItem.add(new Label(EEntry_.text.getName()));
                }
            };
        }

        return this.samples;
    }

    @SuppressWarnings("nls")
    private WebMarkupContainer getSamplesContainer() {
        if (this.samplesContainer == null) {
            this.samplesContainer = new WebMarkupContainer("samplesContainer");
            this.samplesContainer.setOutputMarkupId(true);
            this.samplesContainer.add(getSamples());
        }

        return this.samplesContainer;
    }

    private TextField<String> getText() {
        if (this.text == null) {
            this.text = new TextField<String>("text", new Model<String>()); //$NON-NLS-1$
            this.text.setRequired(true);
        }
        return this.text;
    }

    @SuppressWarnings("serial")
    private AjaxButton getUpdater() {
        if (this.updater == null) {
            this.updater = new IndicatingAjaxButton("updater") { //$NON-NLS-1$

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    pTarget.add(IndexPage.this.getLabel());
                    pTarget.focusComponent(getUpdater()); // レスポンス完了後にフォーカスを当てるコンポーネントを指定.
                }
            };
        }
        return this.updater;
    }

    private void onInserterClick(final AjaxRequestTarget pTarget) {
        this.sampleService.insert(getText().getModelObject());

        pTarget.add(getSampleCount());

        final List<EEntry> all = this.sampleService.getAll();
        this.samplesValue.clear();
        this.samplesValue.addAll(all);
        pTarget.add(getSamplesContainer());
    }
}
