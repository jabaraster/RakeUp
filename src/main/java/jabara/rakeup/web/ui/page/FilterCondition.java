package jabara.rakeup.web.ui.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jabaraster
 */
public class FilterCondition implements Serializable {
    private static final long  serialVersionUID    = -2360391718081328748L;

    /**
     * 
     */
    public static final String DIRECTIVE_KEYWORD   = "kw:";                  //$NON-NLS-1$
    /**
     * 
     */
    public static final String DIRECTIVE_TITLE     = "title:";               //$NON-NLS-1$
    /**
     * 
     */
    public static final String CONDITION_SEPARATOR = " ";                    //$NON-NLS-1$

    private final List<String> keywords            = new ArrayList<String>();
    private final List<String> titleWords          = new ArrayList<String>();

    /**
     * 
     */
    public void clear() {
        clearCore();
    }

    /**
     * @return フィルタの文字列表現.
     */
    public String getFilterString() {
        final StringBuilder sb = new StringBuilder();
        for (final String keyword : this.keywords) {
            sb.append(DIRECTIVE_KEYWORD).append(keyword).append(CONDITION_SEPARATOR);
        }
        for (final String word : this.titleWords) {
            sb.append(DIRECTIVE_TITLE).append(word).append(CONDITION_SEPARATOR);
        }
        return new String(sb);
    }

    /**
     * @return the keywords
     */
    public List<String> getKeywords() {
        return this.keywords;
    }

    /**
     * @return the titleWords
     */
    public List<String> getTitleWords() {
        return this.titleWords;
    }

    /**
     * @param pString フィルタ文字列.
     */
    public void setFilterString(final String pString) {
        if (pString == null || pString.trim().length() == 0) {
            clearCore();
            return;
        }

        final List<String> keywords_ = new ArrayList<String>();
        final List<String> words_ = new ArrayList<String>();

        for (final String word : pString.split("[ 　]")) { //$NON-NLS-1$
            final String w = word.trim();
            if (w.indexOf(DIRECTIVE_KEYWORD) == 0) {
                keywords_.add(w.substring(DIRECTIVE_KEYWORD.length()));
            } else if (w.indexOf(DIRECTIVE_TITLE) == 0) {
                words_.add(w.substring(DIRECTIVE_TITLE.length()));
            } else {
                keywords_.add(w);
                words_.add(w);
            }
        }

        clearCore();
        this.keywords.addAll(keywords_);
        this.titleWords.addAll(words_);
    }

    private void clearCore() {
        this.keywords.clear();
        this.titleWords.clear();
    }
}