/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.general.ArgUtil;
import jabara.rakeup.web.ui.JavaScriptUtil;

import org.apache.wicket.Page;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * 
 * @author jabaraster
 */
public abstract class RakeUpWebPageBase extends WebPage {
    private static final long serialVersionUID = -4825633194551616360L;

    private Panel             headerPanel;

    /**
     * 
     */
    public RakeUpWebPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters パラメータ情報.
     */
    @SuppressWarnings({ "nls", "serial" })
    public RakeUpWebPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(new Label("titleLabel", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return getTitleLabelModel().getObject() + " - RakeUp";
            }
        }));
        this.add(getHeaderPanel());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
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
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        addPageCssReference(pResponse, this.getClass());
    }

    /**
     * ページに固有の処理を記述したJavaScriptファイルを参照するscriptタグをheadタグに追加します. <br>
     * 「ページに固有の処理を記述したJavaScriptファイル」とは、ページクラス名と同名のjsファイルのことを指します. <br>
     * 
     * @param pResponse ヘッダ描画用オブジェクト.
     */
    protected void addPageJavaScriptReference(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        addPageJavaScriptReference(pResponse, this.getClass());
    }

    /**
     * @param pId パネルに与えるwicket:id.
     * @return headerタグの中に入れるパネル.
     */
    protected abstract Panel createHeaderPanel(String pId);

    /**
     * @return ヘッダに入れるパネル.
     */
    protected Panel getHeaderPanel() {
        if (this.headerPanel == null) {
            this.headerPanel = createHeaderPanel("headerPanel"); //$NON-NLS-1$
        }
        return this.headerPanel;
    }

    /**
     * @return HTMLのtitleタグの内容
     */
    protected abstract IModel<String> getTitleLabelModel();

    /**
     * @param pResponse
     */
    public static void addJQueryJavaSriptReference(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(RakeUpWebPageBase.class, "jquery-1.8.3.min.js"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 書き込み用レスポンス.
     * @param pPageType CSSファイルの基準となるページクラス.
     */
    public static void addPageCssReference(final IHeaderResponse pResponse, final Class<? extends Page> pPageType) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pPageType, "pPageType"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(pPageType, pPageType.getSimpleName() + ".css"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 書き込み用レスポンス.
     * @param pPageType jsファイルの基準となるページクラス.
     */
    public static void addPageJavaScriptReference(final IHeaderResponse pResponse, final Class<? extends Page> pPageType) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pPageType, "pPageType"); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(pPageType, pPageType.getSimpleName() + ".js"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 全ての画面に共通して必要なheadタグ内容を出力します.
     */
    public static void renderCommonHead(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(RakeUpWebPageBase.class, "fonts/icomoon/style.css"))); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(RakeUpWebPageBase.class, "bootstrap/css/bootstrap.min.css"))); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(RakeUpWebPageBase.class, "RakeUp.css"))); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(RakeUpWebPageBase.class,
                JavaScriptUtil.COMMON_JS_FILE_PATH)));
    }
}
