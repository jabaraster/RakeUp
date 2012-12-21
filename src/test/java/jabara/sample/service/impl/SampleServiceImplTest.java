/**
 * 
 */
package jabara.sample.service.impl;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.service.DI;
import jabara.rakeup.service.EntryService;

import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class SampleServiceImplTest {

    /**
     * 
     */
    @SuppressWarnings("static-method")
    @Test
    public void _getAll() {
        for (final EEntry sample : DI.get(EntryService.class).getAll()) {
            System.out.println(sample);
        }
    }
}
