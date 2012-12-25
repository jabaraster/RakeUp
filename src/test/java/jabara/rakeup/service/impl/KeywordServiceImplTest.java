/**
 * 
 */
package jabara.rakeup.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.IProducer2;
import jabara.general.NotFound;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.DI;
import jabara.rakeup.service.KeywordService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class KeywordServiceImplTest {

    private final ServiceTestHelper<KeywordServiceImpl> helper = new ServiceTestHelper<KeywordServiceImpl>(
                                                                       new IProducer2<EntityManagerFactory, KeywordServiceImpl>() {
                                                                           @Override
                                                                           public KeywordServiceImpl produce(final EntityManagerFactory pArgument) {
                                                                               final KeywordServiceImpl ret = new KeywordServiceImpl();
                                                                               ret.setEntityManagerFactory(pArgument);
                                                                               return ret;
                                                                           }
                                                                       });
    private KeywordServiceImpl                          service;

    /**
     * @throws NotFound -
     */
    @Test(expected = NotFound.class)
    public void _findByLabel_01_該当なし() throws NotFound {
        this.service.findByLabel("hoge"); //$NON-NLS-1$
    }

    /**
     * 
     */
    @SuppressWarnings({ "nls" })
    @Test(expected = PersistenceException.class)
    public void _findByLabel_02_該当が複数() {
        this.service.insert("a");
        this.service.insert("a");
    }

    /**
     * 
     */
    @SuppressWarnings({ "static-method", "nls", "boxing" })
    @Test
    public void _findByLabels_01_該当なし() {
        final String[] labels = { "Hoge", "Foo" }; // あえて辞書順の逆.
        final List<EKeyword> list = DI.get(KeywordService.class).findByLabels(new HashSet<String>(Arrays.asList(labels)));

        assertThat(list.size(), is(labels.length));

        Arrays.sort(labels);
        Collections.sort(list);

        assertThat(list.get(0).getLabel(), is(labels[0]));
        assertThat(list.get(1).getLabel(), is(labels[1]));
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
