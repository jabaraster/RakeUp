/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.jpa.entity.EntityBase_;
import jabara.jpa_guice.DaoBase;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.service.EntryService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author jabaraster
 */
public class EntryServiceImpl extends DaoBase implements EntryService {
    private static final long serialVersionUID = 2363543705605502284L;

    /**
     * @see jabara.rakeup.service.EntryService#countAll()
     */
    @Override
    public int countAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EEntry> root = query.from(EEntry.class);

        query.select(builder.count(root.get(EntityBase_.id)));

        return em.createQuery(query).getSingleResult().intValue();
    }

    /**
     * @see jabara.rakeup.service.EntryService#getAll()
     */
    @Override
    public List<EEntry> getAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EEntry> query = builder.createQuery(EEntry.class);
        query.from(EEntry.class);
        return em.createQuery(query).getResultList();
    }

    /**
     * @see jabara.rakeup.service.EntryService#insert(java.lang.String)
     */
    @Override
    public EEntry insert(final String pText) {
        final EEntry newSample = new EEntry();
        newSample.setText(pText);
        getEntityManager().persist(newSample);
        return newSample;
    }

}
