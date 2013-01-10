/**
 * 
 */
package jabara.rakeup.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.IProducer2;
import jabara.general.NotFound;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.web.ui.page.FilterCondition;

import java.util.List;

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
                                                                             final KeywordServiceImpl keywordService = new KeywordServiceImpl(
                                                                                     pArgument);
                                                                             final EntryServiceImpl ret = new EntryServiceImpl(pArgument,
                                                                                     keywordService);
                                                                             return ret;
                                                                         }
                                                                     });

    /**
     * 
     */
    @SuppressWarnings("boxing")
    @Test
    public void _find_01_結果は全件() {
        final FilterCondition condition = new FilterCondition();
        final List<EEntry> list = this.service.find(condition);

        assertThat(list.size(), is(this.service.countAll()));

        for (final EEntry entry : list) {
            jabara.Debug.write(entry);
        }
    }

    /**
     * 
     */
    @SuppressWarnings("boxing")
    @Test
    public void _find_02_キーワードで限定() {
        final FilterCondition condition = new FilterCondition();
        condition.setFilterString("kw:iPhone kw:iPad"); //$NON-NLS-1$

        final List<EEntry> list = this.service.find(condition);

        assertThat(list.size() > 0, is(true));

        for (final EEntry entry : list) {
            jabara.Debug.write(entry.getKeywords());
        }
    }

    /**
     * 
     */
    @SuppressWarnings("boxing")
    @Test
    public void _find_03_タイトルで限定() {
        final FilterCondition condition = new FilterCondition();
        condition.setFilterString("title:iPad"); //$NON-NLS-1$

        final List<EEntry> list = this.service.find(condition);

        assertThat(list.size() > 0, is(true));

        for (final EEntry entry : list) {
            jabara.Debug.write(entry.getTitle());
        }
    }

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
