/**
 * 
 */
package jabara.rakeup.web.ui.component;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public class CommonMenu extends Panel {
    private static final long  serialVersionUID = 4843777706564354758L;

    private ListView<PageLink> navigationLinks;

    /**
     * @param pId
     * @param pNavigationLinks
     */
    public CommonMenu(final String pId, final List<? extends PageLink> pNavigationLinks) {
        super(pId, Model.ofList(pNavigationLinks));
        this.add(getNavigationLinks());
    }

    /**
     * @return リンク情報一覧.
     */
    @SuppressWarnings("unchecked")
    protected List<? extends PageLink> getNavigationLinksValus() {
        return (List<? extends PageLink>) getDefaultModelObject();
    }

    @SuppressWarnings("serial")
    private ListView<PageLink> getNavigationLinks() {
        if (this.navigationLinks == null) {
            this.navigationLinks = new ListView<PageLink>("navigationLinks", getNavigationLinksValus()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<PageLink> pItem) {
                    final PageLink pageLink = pItem.getModelObject();
                    final StatelessLink<?> link = new StatelessLink<String>("link") { //$NON-NLS-1$
                        @Override
                        public void onClick() {
                            this.setResponsePage(pageLink.getPageType());
                        }
                    };
                    final Label label = new Label("icon", ""); //$NON-NLS-1$ //$NON-NLS-2$
                    label.add(AttributeModifier.append("class", pageLink.getClassAttributeValue())); //$NON-NLS-1$
                    link.add(label);
                    link.add(new Label("label", pageLink.getRel())); //$NON-NLS-1$
                    pItem.add(link);
                }
            };

        }
        return this.navigationLinks;
    }
}
