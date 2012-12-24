/**
 * 
 */
package jabara.rakeup.entity;

import jabara.general.ArgUtil;
import jabara.jpa.entity.EntityBase;
import jabara.rakeup.model.ILabelable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author jabaraster
 * @param <E> このクラスを継承する具象クラスの型.
 */
@MappedSuperclass
public abstract class ELabelableEntityBase<E extends ELabelableEntityBase<E>> extends EntityBase<E> implements ILabelable, Comparable<E> {
    private static final long serialVersionUID     = -4842690979880163206L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_LABEL = 50;

    /**
     * 
     */
    @NotNull
    @Length(min = 1, max = MAX_CHAR_COUNT_LABEL)
    @Column(nullable = false, unique = true, length = MAX_CHAR_COUNT_LABEL * 3)
    protected String          label;

    /**
     * 
     */
    public ELabelableEntityBase() {
        // 処理なし
    }

    /**
     * @param pLabel ラベル文字列.
     */
    public ELabelableEntityBase(final String pLabel) {
        this.label = pLabel;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final E pOther) {
        ArgUtil.checkNull(pOther, "pOther"); //$NON-NLS-1$
        final String my = this.label == null ? "" : this.label; //$NON-NLS-1$
        final String ot = pOther.label == null ? "" : pOther.label; //$NON-NLS-1$
        return my.compareTo(ot);
    }

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

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [label=" + this.label + ", id=" + this.id + "]";
    }

}
