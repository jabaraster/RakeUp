/**
 * 
 */
package jabara.rakeup.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author jabaraster
 */
@Entity
public class EEntry extends EntityBase<EEntry> {
    private static final long serialVersionUID     = 5059894538997252116L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_TEXT  = 10000;
    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_TITLE = 100;

    /**
     * 
     */
    @NotNull
    @Length(min = 1, max = MAX_CHAR_COUNT_TITLE)
    @Column(nullable = false, length = MAX_CHAR_COUNT_TITLE * 3)
    protected String          title;

    /**
     * 
     */
    @NotNull
    @Length(min = 1, max = MAX_CHAR_COUNT_TEXT)
    @Column(nullable = false, length = MAX_CHAR_COUNT_TEXT * 3)
    protected String          text;

    /**
     * 
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("label")
    protected Set<EKeyword>   keywords             = new HashSet<EKeyword>();

    /**
     * @return the keywords
     */
    public Set<EKeyword> getKeywords() {
        return this.keywords;
    }

    /**
     * @return キーワード一覧をListにし、辞書順にソートして返します.
     */
    public List<EKeyword> getKeywordsAsList() {
        final List<EKeyword> ret = new ArrayList<EKeyword>(this.keywords);
        Collections.sort(ret);
        return ret;
    }

    /**
     * @return １行表示に適した長さのテキスト.
     */
    public String getOneLineText() {
        if (this.text == null) {
            return ""; //$NON-NLS-1$
        }

        final int LINE_CHAR_COUNT = 30;
        if (this.text.length() < LINE_CHAR_COUNT) {
            return this.text;
        }
        return this.text.substring(0, LINE_CHAR_COUNT);
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return titleを返す.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param pText the text to set
     */
    public void setText(final String pText) {
        this.text = pText;
    }

    /**
     * @param pTitle titleを設定.
     */
    public void setTitle(final String pTitle) {
        this.title = pTitle;
    }
}
