/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
public class ShowEntryPage extends RestrictedPageBase {
    private static final long  serialVersionUID = 1379075323513756763L;

    @Inject
    EntryService               entryService;

    private final EEntry       entryValue;

    private Label              title;
    private ListView<EKeyword> keywords;
    private MultiLineLabel     body;

    private Label              updated;
    private StatelessLink<?>   goEdit;
    private StatelessLink<?>   deleter;

    /**
     * @param pParameters パラメータ情報.
     */
    public ShowEntryPage(final PageParameters pParameters) {
        super(pParameters);

        final StringValue value = pParameters.get(0);

        try {
            final long entryId = value.toLong();
            this.entryValue = this.entryService.findById(entryId);

            this.add(getGoEdit());
            this.add(getTitle());
            this.add(getKeywords());
            this.add(getBody());

            this.add(getUpdated());
            this.add(getDeleter());

            setStatelessHint(true);

        } catch (final StringValueConversionException _) {
            throw new RestartResponseException(IndexPage.class);

        } catch (final NotFound e) {
            throw new RestartResponseException(IndexPage.class);
        }
    }

    /**
     * @see jabara.rakeup.web.ui.page.RakeUpWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        addPageCssReference(pResponse);
        addJQueryJavaSriptReference(pResponse);
        addPageJavaScriptReference(pResponse);
        pResponse.render(OnDomReadyHeaderItem.forScript("prepareDeleteConfirmation('" + getDeleter().getMarkupId() + "')")); //$NON-NLS-1$ //$NON-NLS-2$ 
    }

    /**
     * @see jabara.rakeup.web.ui.page.RakeUpWebPageBase#getTitleLabelModel()
     */
    @SuppressWarnings("serial")
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new AbstractReadOnlyModel<String>() {
            @SuppressWarnings("synthetic-access")
            @Override
            public String getObject() {
                return ShowEntryPage.this.entryValue.getTitle();
            }
        };
    }

    private MultiLineLabel getBody() {
        if (this.body == null) {
            final String bodyText = getBodyText();
            this.body = new MultiLineLabel("body", bodyText); //$NON-NLS-1$
            this.body.setEscapeModelStrings(false);
        }
        return this.body;
    }

    private String getBodyText() {
        try {
            return this.entryValue.getMarkdownHtmlText();
        } catch (final NotFound e) {
            return this.entryValue.getText();
        }
    }

    @SuppressWarnings({ "serial", "nls" })
    private StatelessLink<?> getDeleter() {
        if (this.deleter == null) {
            this.deleter = new StatelessLink<Object>("deleter") {
                @SuppressWarnings("synthetic-access")
                @Override
                public void onClick() {
                    ShowEntryPage.this.entryService.delete(ShowEntryPage.this.entryValue);
                    this.setResponsePage(IndexPage.class);
                }
            };
        }
        return this.deleter;
    }

    @SuppressWarnings("serial")
    private Link<?> getGoEdit() {
        if (this.goEdit == null) {
            this.goEdit = new StatelessLink<Object>("goEdit") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                public void onClick() {
                    final PageParameters parameters = new PageParameters();
                    parameters.set(0, ShowEntryPage.this.entryValue.getId());
                    this.setResponsePage(EditEntryPage.class, parameters);
                }
            };
        }
        return this.goEdit;
    }

    @SuppressWarnings("serial")
    private ListView<EKeyword> getKeywords() {
        if (this.keywords == null) {
            this.keywords = new ListView<EKeyword>("keywords", this.entryValue.getKeywordsAsList()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<EKeyword> pItem) {
                    pItem.add(new Label("label", pItem.getModelObject().getLabel())); //$NON-NLS-1$
                }
            };
        }
        return this.keywords;
    }

    private Label getTitle() {
        if (this.title == null) {
            this.title = new Label("title", this.entryValue.getTitle()); //$NON-NLS-1$
        }
        return this.title;
    }

    @SuppressWarnings({ "nls", "serial" })
    private Label getUpdated() {
        if (this.updated == null) {
            this.updated = new Label("updated", new AbstractReadOnlyModel<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    return format(ShowEntryPage.this.entryValue.getUpdated());
                }
            });
        }
        return this.updated;
    }

    /**
     * この画面に特定の投稿を表示するためのパラメータを作成します.
     * 
     * @param pEntry この画面に表示する投稿.
     * @return この画面に特定の投稿を表示するためのパラメータ.
     */
    public static PageParameters createShowEntryPageParameter(final EEntry pEntry) {
        ArgUtil.checkNull(pEntry, "pEntry"); //$NON-NLS-1$

        final PageParameters ret = new PageParameters();
        ret.set(0, pEntry.getId());
        return ret;
    }

    private static String format(final Date pUpdated) {
        if (isToday(pUpdated)) {
            return toTimeFormat(pUpdated);
        } else if (isYesterday(pUpdated)) {
            return "昨日"; //$NON-NLS-1$
        } else {
            return toDateFormat(pUpdated);
        }
    }

    private static boolean isSameDay(final Calendar pCalendar1, final Calendar pCalendar2) {
        return pCalendar1.get(Calendar.YEAR) == pCalendar2.get(Calendar.YEAR) //
                && pCalendar1.get(Calendar.DAY_OF_YEAR) == pCalendar2.get(Calendar.DAY_OF_YEAR) //
        ;
    }

    private static boolean isToday(final Date pDate) {
        final Calendar now = Calendar.getInstance();
        final Calendar cal = toCalendar(pDate);
        return isSameDay(now, cal);
    }

    private static boolean isYesterday(final Date pDate) {
        final Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, -1000 * 60/* 秒 */* 60/* 分 */* 24/* 時間 */); // 昨日の日付
        return isSameDay(now, toCalendar(pDate));
    }

    private static Calendar toCalendar(final Date pDate) {
        final Calendar ret = Calendar.getInstance();
        ret.setTime(pDate);
        return ret;
    }

    private static String toDateFormat(final Date pUpdated) {
        return new SimpleDateFormat("MM/dd").format(pUpdated); //$NON-NLS-1$
    }

    private static String toTimeFormat(final Date pDate) {
        return new SimpleDateFormat("HH:mm").format(pDate); //$NON-NLS-1$
    }
}
