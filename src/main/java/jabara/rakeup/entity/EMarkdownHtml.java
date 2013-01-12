/**
 * 
 */
package jabara.rakeup.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author jabaraster
 */
@Entity
public class EMarkdownHtml extends EntityBase<EMarkdownHtml> {
    private static final long serialVersionUID         = -990452339627817329L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_SOURCE    = EEntry.MAX_CHAR_COUNT_TEXT;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_CONVERTED = MAX_CHAR_COUNT_SOURCE * 5;

    /**
     * 
     */
    @NotNull
    @Length(min = 1, max = MAX_CHAR_COUNT_SOURCE)
    @Column(nullable = false, length = MAX_CHAR_COUNT_SOURCE * 3)
    protected String          source;

    /**
     * 
     */
    @NotNull
    @Length(min = 1, max = MAX_CHAR_COUNT_CONVERTED)
    @Column(nullable = false, length = MAX_CHAR_COUNT_CONVERTED * 3)
    protected String          converted;

    /**
     * @return convertedを返す.
     */
    public String getConverted() {
        return this.converted;
    }

    /**
     * @return sourceを返す.
     */
    public String getSource() {
        return this.source;
    }

    /**
     * @param pConverted convertedを設定.
     */
    public void setConverted(final String pConverted) {
        this.converted = pConverted;
    }

    /**
     * @param pSource sourceを設定.
     */
    public void setSource(final String pSource) {
        this.source = pSource;
    }
}
