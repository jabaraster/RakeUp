/**
 * 
 */
package jabara.rakeup.entity;

import jabara.jpa.entity.EntityBase;
import jabara.rakeup.model.ILabelable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author jabaraster
 * @param <E> このクラスを継承する具象クラスの型.
 */
@MappedSuperclass
public abstract class ELabelableEntityBase<E extends ELabelableEntityBase<E>> extends EntityBase<E> implements ILabelable {
    private static final long serialVersionUID     = -4842690979880163206L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_LABEL = 50;

    /**
     * 
     */
    @Column(nullable = false, unique = true, length = MAX_CHAR_COUNT_LABEL * 3)
    protected String          label;

    /**
     * @return the label
     */
    @Override
    public String getLabel() {
        return this.label;
    }

    /**
     * @param pLabel the label to set
     */
    public void setLabel(final String pLabel) {
        this.label = pLabel;
    }

}
