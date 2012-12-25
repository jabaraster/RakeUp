/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;
import jabara.rakeup.web.ui.component.JavaScriptUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.StringValue;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
public class IndexPage extends RakeUpWebPageBase {
    private static final long          serialVersionUID     = -3725581870632049658L;

    @Inject
    EntryService                       entryService;

    private final List<EEntry>         filteredEntriesValue = new ArrayList<EEntry>();

    private final FilterCondition      filterConditionValue = new FilterCondition();

    private Form<?>                    filterForm;
    private TextField<FilterCondition> filterCondition;
    private AjaxButton                 filterExecutor;

    private ListView<EEntry>           entries;
    private WebMarkupContainer         entriesContainer;

    /**
     * @param pParameters パラメータ情報.
     */
    public IndexPage(final PageParameters pParameters) {
        this.add(getFilterForm());
        this.add(getEntriesContainer());

        final StringValue query = pParameters.get("q"); //$NON-NLS-1$
        if (!query.isEmpty()) {
            this.filterConditionValue.setFilterString(query.toString("")); //$NON-NLS-1$
        }

        this.filteredEntriesValue.addAll(this.entryService.find(this.filterConditionValue));
    }

    /**
     * @see jabara.rakeup.web.ui.page.RakeUpWebPageBase#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse);
        pResponse.renderOnDomReadyJavaScript(JavaScriptUtil.getFocusScript(getFilterCondition()));
    }

    /**
     * @see jabara.rakeup.web.ui.page.RakeUpWebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new Model<String>(this.getString("exampleList")); //$NON-NLS-1$
    }

    private void doFilter(final AjaxRequestTarget pTarget) {
        final List<EEntry> filtered = this.entryService.find(this.filterConditionValue);
        this.filteredEntriesValue.clear();
        this.filteredEntriesValue.addAll(filtered);

        pTarget.add(getEntriesContainer());
        pTarget.add(getFilterCondition());
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

                    final ListView<EKeyword> keywords = new ListView<EKeyword>("keywords", entry.getKeywordsAsList()) {
                        @Override
                        protected void populateItem(@SuppressWarnings("hiding") final ListItem<EKeyword> pItem) {
                            final AjaxLink<?> goFiltered = new AjaxLink<Object>("goFiltered") {
                                @SuppressWarnings("synthetic-access")
                                @Override
                                public void onClick(final AjaxRequestTarget pTarget) {
                                    IndexPage.this.filterConditionValue.clear();
                                    IndexPage.this.filterConditionValue.getKeywords().add(pItem.getModelObject().getLabel());
                                    doFilter(pTarget);
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
    private TextField<FilterCondition> getFilterCondition() {
        if (this.filterCondition == null) {
            final IModel<FilterCondition> m = new IModel<FilterCondition>() {

                @Override
                public void detach() {
                    // 処理なし
                }

                @SuppressWarnings("synthetic-access")
                @Override
                public FilterCondition getObject() {
                    return IndexPage.this.filterConditionValue;
                }

                @SuppressWarnings("synthetic-access")
                @Override
                public void setObject(final FilterCondition pObject) {
                    IndexPage.this.filterConditionValue.clear();

                    if (pObject != null) {
                        IndexPage.this.filterConditionValue.getKeywords().addAll(pObject.getKeywords());
                        IndexPage.this.filterConditionValue.getTitleWords().addAll(pObject.getTitleWords());
                    }
                }
            };
            final Class<FilterCondition> t = FilterCondition.class;
            this.filterCondition = new TextField<FilterCondition>("filterCondition", m, t) { //$NON-NLS-1$
                @SuppressWarnings({ "unchecked", "synthetic-access" })
                @Override
                public <C> IConverter<C> getConverter(final Class<C> pType) {
                    if (FilterCondition.class.isAssignableFrom(pType)) {
                        return (IConverter<C>) new FilterConditionConverter();
                    }
                    return super.getConverter(pType);
                }
            };
            this.filterCondition.setOutputMarkupId(true);
        }
        return this.filterCondition;
    }

    @SuppressWarnings({ "serial", "nls" })
    private AjaxButton getFilterExecutor() {
        if (this.filterExecutor == null) {
            this.filterExecutor = new IndicatingAjaxButton("filterExecutor") {
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    doFilter(pTarget);
                }
            };
        }
        return this.filterExecutor;
    }

    private Form<?> getFilterForm() {
        if (this.filterForm == null) {
            this.filterForm = new Form<Object>("filterForm"); //$NON-NLS-1$
            this.filterForm.add(getFilterCondition());
            this.filterForm.add(getFilterExecutor());
        }
        return this.filterForm;
    }

    private static class FilterConditionConverter implements IConverter<FilterCondition> {
        private static final long serialVersionUID = -6178056927957879045L;

        /**
         * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
         */
        @Override
        public FilterCondition convertToObject(final String pValue, @SuppressWarnings("unused") final Locale pLocale) {
            final FilterCondition ret = new FilterCondition();
            if (pValue != null) {
                ret.setFilterString(pValue);
            }
            return ret;
        }

        /**
         * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
         */
        @Override
        public String convertToString(final FilterCondition pValue, @SuppressWarnings("unused") final Locale pLocale) {
            if (pValue == null) {
                return null;
            }
            return pValue.getFilterString();
        }

    }
}
