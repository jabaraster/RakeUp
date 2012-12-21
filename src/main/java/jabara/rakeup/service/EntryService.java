/**
 * 
 */
package jabara.rakeup.service;

import jabara.rakeup.entity.EEntry;
import jabara.rakeup.service.impl.EntryServiceImpl;

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(EntryServiceImpl.class)
public interface EntryService {

    /**
     * @return 全レコード数を返します.
     */
    int countAll();

    /**
     * @return 全ての永続化された{@link EEntry}オブジェクト.
     */
    List<EEntry> getAll();

    /**
     * @param pText
     * @return 永続化されたエンティティ.
     */
    EEntry insert(String pText);
}
