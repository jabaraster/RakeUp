/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
public class IndexPage extends RakeUpWebPageBase {
    private static final long  serialVersionUID = -3725581870632049658L;

    @Inject
    EntryService               entryService;

    private final List<EEntry> entriesValue     = new ArrayList<EEntry>();

    private ListView<EEntry>   entries;
    private WebMarkupContainer entriesContainer;

    /**
     * 
     */
    public IndexPage() {
        this.add(getEntriesContainer());
        this.entriesValue.addAll(this.entryService.getAll());
    }

    /**
     * @see jabara.rakeup.web.ui.RakeUpWebPageBase#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        addPageCssReference(pResponse);
    }

    /**
     * @see jabara.rakeup.web.ui.RakeUpWebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new Model<String>(this.getClass().getSimpleName());
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<EEntry> getEntries() {
        if (this.entries == null) {
            this.entries = new ListView<EEntry>("entries", Model.ofList(this.entriesValue)) {
                @Override
                protected void populateItem(final ListItem<EEntry> pItem) {
                    pItem.setModel(new CompoundPropertyModel<EEntry>(pItem.getModelObject()));
                    pItem.add(new Label("title"));

                    final ListView<EKeyword> keywords = new ListView<EKeyword>("keywords") {
                        @Override
                        protected void populateItem(@SuppressWarnings("hiding") final ListItem<EKeyword> pItem) {
                            pItem.add(new Label("label", pItem.getModelObject().getLabel()));
                        }
                    };
                    pItem.add(keywords);

                    final Link<?> goEdit = new Link<String>("goEdit") {
                        @Override
                        public void onClick() {
                            final PageParameters parameters = new PageParameters();
                            parameters.set(0, pItem.getModelObject().getId());
                            this.setResponsePage(EditEntryPage.class, parameters);
                        }
                    };
                    pItem.add(goEdit);

                    final Link<?> goDetail = new Link<String>("goDetail") {
                        @Override
                        public void onClick() {
                            final PageParameters parameters = new PageParameters();
                            parameters.set(0, pItem.getModelObject().getId());
                            this.setResponsePage(ShowEntryPage.class, parameters);
                        }
                    };
                    pItem.add(goDetail);
                }
            };
        }

        return this.entries;
    }

    @SuppressWarnings("nls")
    private WebMarkupContainer getEntriesContainer() {
        if (this.entriesContainer == null) {
            this.entriesContainer = new WebMarkupContainer("entriesContainer");
            this.entriesContainer.setOutputMarkupId(true);
            this.entriesContainer.add(getEntries());
        }

        return this.entriesContainer;
    }
}
