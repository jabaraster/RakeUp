/**
 * 
 */
package jabara.rakeup.service.impl;

import jabara.general.NotFound;
import jabara.rakeup.service.DI;
import jabara.rakeup.service.EntryService;

import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class EntryServiceImplTest {

    /**
     * @throws NotFound
     */
    @SuppressWarnings("static-method")
    @Test(expected = NotFound.class)
    public void _findById_01_該当なし() throws NotFound {
        final EntryService service = DI.get(EntryService.class);
        service.findById(-1);
    }

}
