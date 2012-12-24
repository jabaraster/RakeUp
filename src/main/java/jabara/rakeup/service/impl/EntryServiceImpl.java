/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.entity.EntityBase_;
import jabara.jpa_guice.DaoBase;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EEntry_;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;
import jabara.rakeup.service.KeywordService;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;

/**
 * @author jabaraster
 */
public class EntryServiceImpl extends DaoBase implements EntryService {
    private static final long serialVersionUID = 2363543705605502284L;

    @Inject
    KeywordService            keywordService;

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
     * @see jabara.rakeup.service.EntryService#findById(long)
     */
    @Override
    public EEntry findById(final long pId) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EEntry> query = builder.createQuery(EEntry.class);
        final Root<EEntry> root = query.from(EEntry.class);

        query.distinct(true);
        root.fetch(EEntry_.keywords, JoinType.LEFT);

        query.where(builder.equal(root.get(EntityBase_.id), Long.valueOf(pId)));

        final EEntry ret = getSingleResult(em.createQuery(query));
        Collections.sort(ret.getKeywords());
        return ret;
    }

    /**
     * @see jabara.rakeup.service.EntryService#getAll()
     */
    @Override
    public List<EEntry> getAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EEntry> query = builder.createQuery(EEntry.class);
        final Root<EEntry> root = query.from(EEntry.class);

        query.distinct(true);
        root.fetch(EEntry_.keywords, JoinType.LEFT);

        return em.createQuery(query).getResultList();
    }

    /**
     * @param pEntry
     */
    @Override
    public void insert(final EEntry pEntry) {
        ArgUtil.checkNull(pEntry, "pEntry"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        for (final EKeyword keyword : pEntry.getKeywords()) {
            this.keywordService.insertOrUpdate(keyword);
        }
        em.persist(pEntry);
    }

    /**
     * @see jabara.rakeup.service.EntryService#update(jabara.rakeup.entity.EEntry)
     */
    @Override
    public void update(final EEntry pEntry) {
        ArgUtil.checkNull(pEntry, "pEntry"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        if (em.contains(pEntry)) {
            return;
        }
        final EEntry merged = em.merge(pEntry);
        merged.setSource(pEntry.getSource());
        merged.setText(pEntry.getText());
        merged.setTitle(pEntry.getTitle());

        merged.getKeywords().clear();
        merged.getKeywords().addAll(pEntry.getKeywords());
        for (final EKeyword keyword : merged.getKeywords()) {
            this.keywordService.insertOrUpdate(keyword);
        }
    }

}
