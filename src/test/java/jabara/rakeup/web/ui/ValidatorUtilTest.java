/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.entity.EEntry_;
import jabara.rakeup.model.DI;

import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class ValidatorUtilTest {

    /**
     * 
     */
    @SuppressWarnings("static-method")
    @Test
    public void _createStringValidator() {
        System.out.println(EEntry_.title);
        System.out.println(ValidatorUtil.createStringValidator(EEntry.class, EEntry_.title));
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        // JPAメタデータを初期化するために次のコードが必要.
        DI.get(EntityManagerFactory.class).createEntityManager();
    }
}
