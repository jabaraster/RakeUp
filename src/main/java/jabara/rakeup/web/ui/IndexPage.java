/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
public class IndexPage extends RakeUpWebPageBase {
    private static final long  serialVersionUID     = -3725581870632049658L;

    @Inject
    EntryService               entryService;

    private final List<EEntry> entriesValue         = new ArrayList<EEntry>();
    private final List<EEntry> filteredEntriesValue = new ArrayList<EEntry>();

    private Label              currentFilter;
    private AjaxLink<?>        filterClearer;

    private ListView<EEntry>   entries;
    private WebMarkupContainer entriesContainer;

    /**
     * 
     */
    public IndexPage() {
        this.add(getCurrentFilter());
        this.add(getFilterClearer());
        this.add(getEntriesContainer());
        this.entriesValue.addAll(this.entryService.getAll());
        this.filteredEntriesValue.addAll(this.entriesValue);
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
        return new Model<String>(this.getString("exampleList")); //$NON-NLS-1$
    }

    private void clearFilter() {
        this.filteredEntriesValue.clear();
        this.filteredEntriesValue.addAll(this.entriesValue);
        getFilterClearer().setVisible(false);
        getCurrentFilter().setDefaultModelObject(getFilterNothingText());
    }

    private void filterEntries(final EKeyword pKeyword) {
        this.filteredEntriesValue.clear();
        for (final EEntry entry : this.entriesValue) {
            for (final EKeyword keyword : entry.getKeywords()) {
                if (keyword.getLabel().equals(pKeyword.getLabel())) {
                    this.filteredEntriesValue.add(entry);
                    break;
                }
            }
        }

        getFilterClearer().setVisible(true);
        getCurrentFilter().setDefaultModelObject(pKeyword.getLabel());
    }

    private Label getCurrentFilter() {
        if (this.currentFilter == null) {
            this.currentFilter = new Label("currentFilter", new Model<String>(getFilterNothingText())); //$NON-NLS-1$
            this.currentFilter.setOutputMarkupId(true);
        }
        return this.currentFilter;
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<EEntry> getEntries() {
        if (this.entries == null) {
            this.entries = new ListView<EEntry>("entries", Model.ofList(this.filteredEntriesValue)) {
                @Override
                protected void populateItem(final ListItem<EEntry> pItem) {
                    final EEntry entry = pItem.getModelObject();

                    final Link<?> goDetail = new Link<String>("goDetail") {
                        @Override
                        public void onClick() {
                            final PageParameters parameters = new PageParameters();
                            parameters.set(0, entry.getId());
                            this.setResponsePage(ShowEntryPage.class, parameters);
                        }
                    };
                    goDetail.add(new Label("title", entry.getTitle()));
                    pItem.add(goDetail);

                    final ListView<EKeyword> keywords = new ListView<EKeyword>("keywords", entry.getKeywords()) {
                        @Override
                        protected void populateItem(@SuppressWarnings("hiding") final ListItem<EKeyword> pItem) {
                            final AjaxLink<?> goFiltered = new AjaxLink<Object>("goFiltered") {
                                @SuppressWarnings("synthetic-access")
                                @Override
                                public void onClick(final AjaxRequestTarget pTarget) {
                                    filterEntries(pItem.getModelObject());
                                    pTarget.add(getCurrentFilter());
                                    pTarget.add(getFilterClearer());
                                    pTarget.add(getEntriesContainer());
                                }

                            };
                            goFiltered.add(new Label("label", pItem.getModelObject().getLabel()));
                            pItem.add(goFiltered);
                        }
                    };
                    pItem.add(keywords);

                    final Link<?> goEdit = new Link<String>("goEdit") {
                        @Override
                        public void onClick() {
                            final PageParameters parameters = new PageParameters();
                            parameters.set(0, entry.getId());
                            this.setResponsePage(EditEntryPage.class, parameters);
                        }
                    };
                    pItem.add(goEdit);
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

    @SuppressWarnings("serial")
    private AjaxLink<?> getFilterClearer() {
        if (this.filterClearer == null) {
            this.filterClearer = new AjaxLink<Object>("filterClearer") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                public void onClick(final AjaxRequestTarget pTarget) {
                    clearFilter();
                    pTarget.add(getCurrentFilter());
                    pTarget.add(getFilterClearer());
                    pTarget.add(getEntriesContainer());
                }
            };
            this.filterClearer.setOutputMarkupId(true);
            this.filterClearer.setOutputMarkupPlaceholderTag(true);
            this.filterClearer.setVisible(false);
        }
        return this.filterClearer;
    }

    private String getFilterNothingText() {
        return this.getString("filterNothing"); //$NON-NLS-1$
    }
}
