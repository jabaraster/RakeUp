/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.model.DI;

import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;

/**
 * @author jabaraster
 */
public class ValidatorUtilTest {

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        // JPAメタデータを初期化するために次のコードが必要.
        DI.get(EntityManagerFactory.class).createEntityManager();
    }
}
