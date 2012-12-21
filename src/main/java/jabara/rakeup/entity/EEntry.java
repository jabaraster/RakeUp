/**
 * 
 */
package jabara.rakeup.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author jabaraster
 */
@Entity
public class EEntry extends EntityBase<EEntry> {
    private static final long serialVersionUID    = 5059894538997252116L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_TEXT = 10000;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_TEXT * 3)
    protected String          text;

    /**
     * 
     */
    @OneToMany(fetch = FetchType.LAZY)
    protected List<EKeyword>  keywords            = new ArrayList<EKeyword>();

    /**
     * 
     */
    @ManyToOne(fetch = FetchType.LAZY)
    protected ESource         source;

    /**
     * @return the keywords
     */
    public List<EKeyword> getKeywords() {
        return this.keywords;
    }

    /**
     * @return the source
     */
    public ESource getSource() {
        return this.source;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @param pSource the source to set
     */
    public void setSource(final ESource pSource) {
        this.source = pSource;
    }

    /**
     * @param pText the text to set
     */
    public void setText(final String pText) {
        this.text = pText;
    }
}
