/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.general.ArgUtil;
import jabara.rakeup.web.ui.RakeUpWicketApplication;
import jabara.rakeup.web.ui.component.JavaScriptUtil;
import jabara.rakeup.web.ui.component.PageLink;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * 
 * @author jabaraster
 */
public abstract class RakeUpWebPageBase extends WebPage {
    private static final long    serialVersionUID     = -4825633194551616360L;

    private final List<PageLink> navigationLinksValue = new ArrayList<PageLink>();

    private ListView<PageLink>   navigationLinks;

    /**
     * 
     */
    public RakeUpWebPageBase() {
        super();
        build();
    }

    /**
     * @param pParameters パラメータ情報.
     */
    public RakeUpWebPageBase(final PageParameters pParameters) {
        super(pParameters);
        build();
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        renderCommonHead(pResponse);
    }

    /**
     * ページに固有のスタイルを記述したスタイルシートをheadタグに追加します. <br>
     * 「ページに固有のページに固有のスタイルを記述したスタイルシート」とは、ページクラス名と同名のcssファイルのことを指します. <br>
     * 
     * @param pResponse ヘッダ描画用オブジェクト.
     */
    protected void addPageCssReference(final IHeaderResponse pResponse) {
        addPageCssReference(pResponse, this.getClass());
    }

    /**
     * @return HTMLのtitleタグの内容
     */
    protected abstract IModel<String> getTitleLabelModel();

    @SuppressWarnings({ "nls", "serial" })
    private void build() {
        this.add(new Label("titleLabel", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return getTitleLabelModel().getObject() + " - RakeUp";
            }
        }));
        this.add(getNavigationLinks());

        this.navigationLinksValue.addAll(RakeUpWicketApplication.getNavigationLinks());
    }

    @SuppressWarnings("serial")
    private ListView<PageLink> getNavigationLinks() {
        if (this.navigationLinks == null) {
            this.navigationLinks = new ListView<PageLink>("navigationLinks", this.navigationLinksValue) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<PageLink> pItem) {
                    final PageLink pageLink = pItem.getModelObject();
                    final StatelessLink<?> link = new StatelessLink<String>("link") { //$NON-NLS-1$
                        @Override
                        public void onClick() {
                            this.setResponsePage(pageLink.getPageType());
                        }
                    };
                    final Label label = new Label("label", new Model<String>(pageLink.getRel()));
                    label.add(AttributeModifier.append("class", pageLink.getClassAttributeValue()));
                    link.add(label);
                    pItem.add(link);
                }
            };

        }
        return this.navigationLinks;
    }

    /**
     * @param pResponse 書き込み用レスポンス.
     * @param pPageType CSSファイルの基準となるページクラス.
     */
    public static void addPageCssReference(final IHeaderResponse pResponse, final Class<? extends Page> pPageType) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pPageType, "pPageType"); //$NON-NLS-1$
        pResponse.renderCSSReference(new CssResourceReference(pPageType, pPageType.getSimpleName() + ".css")); //$NON-NLS-1$
    }

    /**
     * @param pResponse 全ての画面に共通して必要なheadタグ内容を出力します.
     */
    public static void renderCommonHead(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.renderCSSReference(new CssResourceReference(RakeUpWebPageBase.class, "style.css")); //$NON-NLS-1$
        pResponse.renderCSSReference(new CssResourceReference(RakeUpWebPageBase.class, "RakeUp.css")); //$NON-NLS-1$
        pResponse.renderJavaScriptReference(new JavaScriptResourceReference(RakeUpWebPageBase.class, JavaScriptUtil.COMMON_JS_FILE_PATH));
    }
}
