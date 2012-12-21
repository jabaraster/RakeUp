/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.jpa.entity.EntityBase_;
import jabara.jpa_guice.DaoBase;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.EntryService;
import jabara.rakeup.service.KeywordService;
import jabara.rakeup.service.NotFound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
     * @see jabara.rakeup.service.EntryService#insert(java.lang.String, java.lang.String)
     */
    @Override
    public EEntry insert(final String pText, final String pKeywords) {
        final List<EKeyword> keywords = parseKeyword(pKeywords);

        final EEntry newSample = new EEntry();
        newSample.setText(pText);
        newSample.getKeywords().addAll(keywords);

        getEntityManager().persist(newSample);

        return newSample;

    }

    private EKeyword findOrInsert(final String pLabel) {
        try {
            return this.keywordService.findByLabel(pLabel);
        } catch (final NotFound e) {
            return this.keywordService.insert(pLabel);
        }
    }

    private List<EKeyword> parseKeyword(final String pKeywords) {
        if (pKeywords == null) {
            return Collections.emptyList();
        }
        if (pKeywords.trim().equals("")) { //$NON-NLS-1$
            return Collections.emptyList();
        }

        final String[] labels = pKeywords.trim().split(","); //$NON-NLS-1$
        final List<EKeyword> ret = new ArrayList<EKeyword>();
        for (final String label : labels) {
            if (label.trim().length() == 0) {
                continue;
            }
            ret.add(findOrInsert(label.trim()));
        }

        return ret;
    }

}
