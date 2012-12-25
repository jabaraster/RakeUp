/**
 * 
 */
package jabara.rakeup.web.ui.component;

import jabara.general.ArgUtil;

import org.apache.wicket.Component;

/**
 * 
 * @author jabaraster
 */
public final class JavaScriptUtil {

    /**
     * 
     */
    public static final String COMMON_JS_FILE_PATH = "RakeUp.js"; //$NON-NLS-1$

    private JavaScriptUtil() {
        // 処理なし
    }

    /**
     * @param pTag フォーカスを当てる対象のタグ. <br>
     *            {@link Component#setOutputMarkupId(boolean)}にtrueをセットしていることが前提.
     * @return pTagにフォーカスを当てるJavaScriptコード.
     */
    public static String getFocusScript(final Component pTag) {
        ArgUtil.checkNull(pTag, "pTag"); //$NON-NLS-1$
        return "RakeUp.focus('" + pTag.getMarkupId() + "');"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
