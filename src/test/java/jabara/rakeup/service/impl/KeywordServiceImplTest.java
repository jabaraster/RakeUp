/**
 * 
 */
package jabara.rakeup.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.NotFound;
import jabara.jpa_guice.ThreadLocalEntityManagerFactoryHandler;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.DI;
import jabara.rakeup.service.KeywordService;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import net.arnx.jsonic.JSON;

import org.apache.wicket.util.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class KeywordServiceImplTest {

    EntityManagerFactory emf;

    KeywordServiceImpl   service;

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
        this.emf = ThreadLocalEntityManagerFactoryHandler.wrap(Persistence.createEntityManagerFactory("mainPersistenceUnit")); //$NON-NLS-1$
        this.service = new KeywordServiceImpl();
        this.service.setEntityManagerFactory(this.emf);

        final EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
    }

    /**
     * 
     */
    @After
    public void zAfter() {
        if (this.emf != null) {
            final EntityTransaction tx = this.emf.createEntityManager().getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
            this.emf.close();
        }
    }

    /**
     * @param pArgs
     * @throws IOException
     */
    @SuppressWarnings({ "nls", "resource" })
    public static void main(final String[] pArgs) throws IOException {
        final URL url = new URL("https://api.github.com/markdown");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        OutputStream httpOut = null;
        InputStream in = null;
        try {
            httpOut = conn.getOutputStream();
            final byte[] code = loadMarkdown("../heroku-web-template/README.md"); //$NON-NLS-1$

            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", new String(code, "UTF-8"));
            // map.put("text", "Hello world github/linguist#1 **cool**, and #1!");
            // map.put("mode", "gfm");
            // map.put("context", "github/gollum");

            final String json = JSON.encode(map);
            System.out.println(json);

            httpOut.write(json.getBytes("UTF-8"));
            httpOut.flush();
            httpOut.close();

            in = conn.getInputStream();
            final ByteArrayOutputStream mem = new ByteArrayOutputStream();
            IOUtils.copy(in, mem);

            for (final Map.Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
                System.out.println(header.getKey() + ":" + header.getValue());
            }

            System.out.println(new String(mem.toByteArray(), "UTF-8"));

        } finally {
            IOUtils.close(in);
            IOUtils.close(httpOut);
        }
    }

    @SuppressWarnings("resource")
    private static byte[] loadMarkdown(final String pPath) throws IOException {
        final File file = new File(pPath);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(new BufferedInputStream(in), out);
            return out.toByteArray();

        } finally {
            IOUtils.close(in);
        }
    }

}
