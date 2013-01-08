/**
 * 
 */
package jabara.rakeup.web.ui.component;

import jabara.general.ArgUtil;
import jabara.general.IProducer2;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EEntry_;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.KeywordService;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import com.google.inject.Inject;

/**
 * 
 * @author jabaraster
 */
public class EntryPanel extends Panel {
    private static final long                            serialVersionUID = -8970586401384139213L;

    @Inject
    KeywordService                                       keywordService;

    @SuppressWarnings("synthetic-access")
    private final IProducer2<Set<String>, Set<EKeyword>> keywordProducer  = new KeywordProcuder();

    private final EEntry                                 entry;

    private TextField<String>                            title;
    private FeedbackPanel                                titleFeedback;

    private TextField<Set<EKeyword>>                     keywords;

    private TextArea<String>                             body;
    private FeedbackPanel                                bodyFeedback;

    /**
     * @param pId id値.
     * @param pEntry 操作対象のエントリ.
     */
    public EntryPanel(final String pId, final EEntry pEntry) {
        super(pId);
        ArgUtil.checkNullOrEmpty(pId, "pId"); //$NON-NLS-1$
        ArgUtil.checkNull(pEntry, "pEntry"); //$NON-NLS-1$

        this.entry = pEntry;

        this.add(getTitle());
        this.add(getTitleFeedback());

        this.add(getKeywords());

        this.add(getBody());
        this.add(getBodyFeedback());
    }

    /**
     * @return 本文入力欄.
     */
    public TextArea<String> getBody() {
        if (this.body == null) {
            this.body = new TextArea<String>("body", new PropertyModel<String>(this.entry, EEntry_.text.getName())); //$NON-NLS-1$
            this.body.setOutputMarkupId(true);
            this.body.setRequired(true);
        }
        return this.body;
    }

    /**
     * @return 本文入力欄に起因するメッセージ表示エリア.
     */
    public FeedbackPanel getBodyFeedback() {
        if (this.bodyFeedback == null) {
            this.bodyFeedback = new ComponentFeedbackPanel("bodyFeedback", getBody()); //$NON-NLS-1$
            this.bodyFeedback.setOutputMarkupId(true);
        }
        return this.bodyFeedback;
    }

    /**
     * @return このパネルで操作するエントリオブジェクト.
     */
    public EEntry getEntry() {
        return this.entry;
    }

    /**
     * @return キーワード入力欄.
     */
    @SuppressWarnings({ "serial", "unchecked" })
    public TextField<Set<EKeyword>> getKeywords() {
        if (this.keywords == null) {

            final IModel<Set<EKeyword>> m = new KeywordListModel(this.entry);
            final Class<Set<EKeyword>> t = (Class<Set<EKeyword>>) this.entry.getKeywords().getClass();

            this.keywords = new TextField<Set<EKeyword>>("keywords", m, t) { //$NON-NLS-1$
                @SuppressWarnings({ "synthetic-access" })
                @Override
                public <C> IConverter<C> getConverter(final Class<C> pType) {
                    if (Set.class.isAssignableFrom(pType)) {
                        return (IConverter<C>) new LabelableSetConverter<EKeyword>(EntryPanel.this.keywordProducer);
                    }
                    return super.getConverter(pType);
                }
            };
            this.keywords.setOutputMarkupId(true);
        }
        return this.keywords;
    }

    /**
     * @return タイトル入力欄.
     */
    public TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<String>("title", new PropertyModel<String>(this.entry, EEntry_.title.getName())); //$NON-NLS-1$
            this.title.setOutputMarkupId(true);
            this.title.setRequired(true);
        }
        return this.title;
    }

    /**
     * @return タイトル入力欄に起因するメッセージを表示するエリア.
     */
    public FeedbackPanel getTitleFeedback() {
        if (this.titleFeedback == null) {
            this.titleFeedback = new ComponentFeedbackPanel("titleFeedback", getTitle()); //$NON-NLS-1$
            this.titleFeedback.setOutputMarkupId(true);
        }
        return this.titleFeedback;
    }

    private Set<EKeyword> findKeywordByLabels(final Set<String> pLabels) {
        return new HashSet<EKeyword>(this.keywordService.findByLabels(pLabels));
    }

    private static class KeywordListModel implements IModel<Set<EKeyword>> {
        private static final long serialVersionUID = -3015768162652905768L;

        private final EEntry      entry;

        /**
         * @param pEntry
         */
        KeywordListModel(final EEntry pEntry) {
            this.entry = pEntry;
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            // 処理なし
        }

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public Set<EKeyword> getObject() {
            return this.entry.getKeywords();
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(final Set<EKeyword> pObject) {
            this.entry.getKeywords().clear();
            if (pObject != null) {
                this.entry.getKeywords().addAll(pObject);
            }
        }

    }

    private class KeywordProcuder implements IProducer2<Set<String>, Set<EKeyword>>, Serializable {
        private static final long serialVersionUID = 8522095809221201898L;

        @SuppressWarnings("synthetic-access")
        @Override
        public Set<EKeyword> produce(final Set<String> pLabels) {
            return findKeywordByLabels(pLabels);
        }
    }

}
