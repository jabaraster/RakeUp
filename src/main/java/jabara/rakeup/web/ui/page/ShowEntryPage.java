/**
 * 
 */
package jabara.rakeup.web.ui.page;

import jabara.general.NotFound;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.head.IHeaderResponse;
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
public class ShowEntryPage extends RakeUpWebPageBase {
    private static final long  serialVersionUID = 1379075323513756763L;

    @Inject
    EntryService               entryService;

    private final EEntry       entryValue;

    private StatelessLink<?>   goEdit;

    private Label              title;
    private ListView<EKeyword> keywords;
    private MultiLineLabel     body;

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
}
