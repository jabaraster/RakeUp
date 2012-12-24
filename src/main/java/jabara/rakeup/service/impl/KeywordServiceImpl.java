/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa_guice.DaoBase;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.entity.ELabelableEntityBase_;
import jabara.rakeup.service.KeywordService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
            throw new NotFound();
        }
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EKeyword> query = builder.createQuery(EKeyword.class);
        final Root<EKeyword> root = query.from(EKeyword.class);

        query.where(builder.equal(root.get(ELabelableEntityBase_.label), pLabel));

        return getSingleResult(em.createQuery(query));
    }

    /**
     * @see jabara.rakeup.service.KeywordService#findByLabels(java.util.Set)
     */
    @Override
    public List<EKeyword> findByLabels(final Set<String> pLabels) {
        ArgUtil.checkNull(pLabels, "pLabels"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EKeyword> query = builder.createQuery(EKeyword.class);
        final Root<EKeyword> root = query.from(EKeyword.class);

        query.where(root.get(ELabelableEntityBase_.label).in(pLabels));

        final List<EKeyword> dbResults = em.createQuery(query).getResultList();

        // DBにないものはnewする.
        // pLabelsからdbResultの中のラベルを引き算すれば、それはDBにないものなのでnewの対象のラベル.
        // こうすれば２重ループを回す必要がなくなる.
        final Set<String> newTarget = new HashSet<String>(pLabels);
        for (final EKeyword dbResult : dbResults) {
            newTarget.remove(dbResult.getLabel());
        }

        for (final String label : newTarget) {
            dbResults.add(new EKeyword(label));
        }
        return dbResults;
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

    /**
     * @see jabara.rakeup.service.KeywordService#insertOrUpdate(jabara.rakeup.entity.EKeyword)
     */
    @Override
    public void insertOrUpdate(final EKeyword pKeyword) {
        ArgUtil.checkNull(pKeyword, "pKeyword"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        if (pKeyword.getId() == null) {
            em.persist(pKeyword);
            return;
        }

        if (em.contains(pKeyword)) {
            return;
        }
        final EKeyword merged = em.merge(pKeyword);
        merged.setLabel(pKeyword.getLabel());
    }

    void setEntityManagerFactory(final EntityManagerFactory pEntityManagerFactory) {
        this.emf = pEntityManagerFactory;
    }

}
