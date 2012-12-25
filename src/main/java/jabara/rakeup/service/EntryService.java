/**
 * 
 */
package jabara.rakeup.service;

import jabara.general.NotFound;
import jabara.rakeup.entity.EEntry;
import jabara.rakeup.service.impl.EntryServiceImpl;
import jabara.rakeup.web.ui.page.FilterCondition;

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
     * @param pMarkdownText
     * @return HTML
     */
    String encodeMarkdown(String pMarkdownText);

    /**
     * @param pFilterCondition
     * @return -
     */
    List<EEntry> find(FilterCondition pFilterCondition);

    /**
     * @param pId
     * @return IDがpIdのエンティティオブジェクト.
     * @throws NotFound 該当オブジェクトがない場合.
     */
    EEntry findById(long pId) throws NotFound;

    /**
     * @return 全ての永続化された{@link EEntry}オブジェクト.
     */
    List<EEntry> getAll();

    /**
     * @param pEntry
     */
    void insert(EEntry pEntry);

    /**
     * @param pEntry
     */
    void update(EEntry pEntry);
}
