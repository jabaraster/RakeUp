/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.DI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class EntryServiceImplTest {
    /**
     * 
     */
    @SuppressWarnings("static-method")
    @Test
    public void _test() {
        try {
            final EntityManager em = DI.get(EntityManagerFactory.class).createEntityManager();
            final EKeyword keyword = new EKeyword();
            keyword.setLabel("aaaaaa"); //$NON-NLS-1$

            em.getTransaction().begin();
            em.persist(keyword);
            em.getTransaction().commit();
        } catch (final ConstraintViolationException e) {
            System.out.println(e.getMessage());
            for (final ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println("  " + violation); //$NON-NLS-1$
            }
        }
    }

    /**
     * @param pArgs 起動引数.
     */
    public static void main(final String[] pArgs) {
        new EntryServiceImplTest()._test();
    }

}
