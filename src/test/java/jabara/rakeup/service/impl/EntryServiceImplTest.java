/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.general.IProducer2;
import jabara.general.NotFound;
import jabara.rakeup.entity.EEntry;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class EntryServiceImplTest {

    EntityManagerFactory                              emf;

    EntryServiceImpl                                  service;

    private final ServiceTestHelper<EntryServiceImpl> helper = new ServiceTestHelper<EntryServiceImpl>(
                                                                     new IProducer2<EntityManagerFactory, EntryServiceImpl>() {
                                                                         @Override
                                                                         public EntryServiceImpl produce(final EntityManagerFactory pArgument) {
                                                                             return new EntryServiceImpl().setEntityManagerFactory(pArgument);
                                                                         }
                                                                     });

    /**
     * @throws NotFound -
     */
    @Test(expected = NotFound.class)
    public void _findById_01_該当なし() throws NotFound {
        this.service.findById(-1);
    }

    /**
     * 
     */
    @Test
    public void _getAll_01() {
        for (final EEntry entry : this.service.getAll()) {
            System.out.println(entry);
        }
    }

    /**
     * 
     */
    @Before
    public void yBefore() {
        this.service = this.helper.before();
    }

    /**
     * 
     */
    @After
    public void zAfter() {
        this.helper.after();
    }

}
