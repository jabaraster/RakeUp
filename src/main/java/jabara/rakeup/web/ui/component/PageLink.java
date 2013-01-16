/**
 * 
 */
package jabara.rakeup.web.ui.component;

import jabara.general.ArgUtil;

import java.io.Serializable;

import org.apache.wicket.Page;

/**
 * @author jabaraster
 */
public class PageLink implements Serializable {
    private static final long           serialVersionUID = 8545832823057240069L;

    private final Class<? extends Page> pageType;
    private final String                rel;
    private final String                classAttributeValue;

    /**
     * @param pPage ページクラス.
     * @param pRel リンクラベル.
     * @param pClassAttributeValue class属性値.
     */
    public PageLink(final Class<? extends Page> pPage, final String pRel, final String pClassAttributeValue) {
        ArgUtil.checkNull(pPage, "pPage"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pRel, "pRel"); //$NON-NLS-1$
        this.rel = pRel;
        this.pageType = pPage;
        this.classAttributeValue = pClassAttributeValue == null ? "" : pClassAttributeValue; //$NON-NLS-1$
    }

    /**
     * @return classAttributeValueを返す.
     */
    public String getClassAttributeValue() {
        return this.classAttributeValue;
    }

    /**
     * @return pageを返す.
     */
    public Class<? extends Page> getPageType() {
        return this.pageType;
    }

    /**
     * @return relを返す.
     */
    public String getRel() {
        return this.rel;
    }
}
