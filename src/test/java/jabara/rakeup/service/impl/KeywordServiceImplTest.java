/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.DI;
import jabara.rakeup.service.NotFound;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class KeywordServiceImplTest {

    /**
     * @throws NotFound -
     */
    @SuppressWarnings("static-method")
    @Test(expected = NotFound.class)
    public void _findByLabel_01_該当なし() throws NotFound {
        DI.get(KeywordServiceImpl.class).findByLabel("hoge"); //$NON-NLS-1$
    }

    /**
     * 
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test(expected = PersistenceException.class)
    public void _findByLabel_02_該当が複数() {
        final EntityManagerFactory emf = DI.get(EntityManagerFactory.class);
        try {
            final EntityManager em = emf.createEntityManager();
            final EKeyword k0 = new EKeyword("a");
            final EKeyword k1 = new EKeyword("a");
            em.getTransaction().begin();
            em.persist(k0);
            em.persist(k1);
            em.flush(); // ここで一意制約違反が起きるのが正しい.

        } finally {
            emf.close();
        }
    }

}
