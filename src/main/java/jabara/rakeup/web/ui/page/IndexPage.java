/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;
import jabara.rakeup.web.ui.component.JavaScriptUtil;

import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.StringValue;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class IndexPage extends RakeUpWebPageBase {

    @Inject
    EntryService                       entryService;

    private final FilterCondition      filterConditionValue = new FilterCondition();
    private final AtomicInteger        countValue           = new AtomicInteger();

    private Form<?>                    filterForm;
    private TextField<FilterCondition> filterCondition;
    private AjaxButton                 filterExecutor;

    private Label                      count;

    private AjaxPagingNavigator        entriesPager;
    private DataView<EEntry>           entries;
    private WebMarkupContainer         entriesContainer;

    /**
     * @param pParameters パラメータ情報.
     */
    public IndexPage(final PageParameters pParameters) {
        this.add(getFilterForm());
        this.add(getEntriesPager());
        this.add(getCount());
        this.add(getEntriesContainer());

        final StringValue query = pParameters.get("q"); //$NON-NLS-1$
        if (!query.isEmpty()) {
            this.filterConditionValue.setFilterString(query.toString("")); //$NON-NLS-1$
        }
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
        pTarget.add(getEntriesPager());
        pTarget.add(getCount());
        pTarget.add(getEntriesContainer());
        pTarget.add(getFilterCondition());
    }

    @SuppressWarnings("nls")
    private Label getCount() {
        if (this.count == null) {
            this.count = new Label("count", new AbstractReadOnlyModel<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    return String.valueOf(IndexPage.this.countValue.get());
                }
            });
            this.count.setOutputMarkupId(true);
        }
        return this.count;
    }

    @SuppressWarnings({ "nls", "synthetic-access" })
    private DataView<EEntry> getEntries() {
        if (this.entries == null) {
            this.entries = new DataView<EEntry>("entries", new EntryDataProvider(), 20) {
                @Override
                protected void populateItem(final Item<EEntry> pItem) {
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
                            final AjaxLink<?> goFiltered = new IndicatingAjaxLink<Object>("goFiltered") {
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

    private AjaxPagingNavigator getEntriesPager() {
        if (this.entriesPager == null) {
            this.entriesPager = new AjaxPagingNavigator("entriesPager", getEntries()); //$NON-NLS-1$
            this.entriesPager.setOutputMarkupId(true);
        }
        return this.entriesPager;
    }

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

    @SuppressWarnings({ "nls" })
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

    private class EntryDataProvider implements IDataProvider<EEntry> {

        @Override
        public void detach() {
            // 処理なし
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public Iterator<? extends EEntry> iterator(final int pFirst, final int pCount) {
            return IndexPage.this.entryService.find(IndexPage.this.filterConditionValue, pFirst, pCount).iterator();
        }

        @Override
        public IModel<EEntry> model(final EEntry pObject) {
            return Model.of(pObject);
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public int size() {
            final int ret = IndexPage.this.entryService.count(IndexPage.this.filterConditionValue);
            IndexPage.this.countValue.set(ret);
            return ret;
        }

    }

    private static class FilterConditionConverter implements IConverter<FilterCondition> {

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
