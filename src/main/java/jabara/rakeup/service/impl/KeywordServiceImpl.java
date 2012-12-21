/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.jpa.entity.IEntity;
import jabara.jpa_guice.DaoBase;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.entity.ELabelableEntityBase_;
import jabara.rakeup.service.KeywordService;
import jabara.rakeup.service.NotFound;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author jabaraster
 */
public class KeywordServiceImpl extends DaoBase implements KeywordService {
    private static final long serialVersionUID = -6130802685624628629L;

    /**
     * @see jabara.rakeup.service.KeywordService#findByLabel(java.lang.String)
     */
    @Override
    public EKeyword findByLabel(final String pLabel) throws NotFound {
        if (pLabel == null || pLabel.length() == 0) {
            throw NotFound.INSTANCE;
        }
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EKeyword> query = builder.createQuery(EKeyword.class);
        final Root<EKeyword> root = query.from(EKeyword.class);

        query.where(builder.equal(root.get(ELabelableEntityBase_.label), pLabel));

        return getSingleResult(em.createQuery(query));
    }

    /**
     * @see jabara.rakeup.service.KeywordService#insert(java.lang.String)
     */
    @Override
    public EKeyword insert(final String pLabel) {
        final EKeyword ret = new EKeyword(pLabel);
        getEntityManager().persist(ret);
        return ret;
    }

    private static <E extends IEntity> E getSingleResult(final TypedQuery<E> pQuery) throws NotFound {
        try {
            return pQuery.getSingleResult();
        } catch (final NoResultException e) {
            throw NotFound.INSTANCE;
        }
    }

}
