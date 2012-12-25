/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.general.ArgUtil;
import jabara.general.IProducer2;
import jabara.jpa_guice.ThreadLocalEntityManagerFactoryHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @param <S> テスト対象のサービスクラスの型.
 * @author jabaraster
 */
public class ServiceTestHelper<S> {

    private EntityManagerFactory                      emf;

    private final IProducer2<EntityManagerFactory, S> serviceGenerator;
    private S                                         service;

    /**
     * @param pServiceGenerator テスト対象のサービスクラス.
     */
    public ServiceTestHelper(final IProducer2<EntityManagerFactory, S> pServiceGenerator) {
        ArgUtil.checkNull(pServiceGenerator, "pServiceGenerator"); //$NON-NLS-1$
        this.serviceGenerator = pServiceGenerator;
    }

    /**
     * 
     */
    public void after() {
        if (this.emf != null) {
            final EntityTransaction tx = this.emf.createEntityManager().getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
            this.emf.close();
        }
    }

    /**
     * @return サービスクラス.
     */
    public S before() {
        this.emf = ThreadLocalEntityManagerFactoryHandler.wrap(Persistence.createEntityManagerFactory("mainPersistenceUnit")); //$NON-NLS-1$
        this.service = this.serviceGenerator.produce(this.emf);

        final EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();

        return this.service;
    }
}
