/**
 * 
 */
package jabara.rakeup.service;

import jabara.general.NotFound;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.impl.KeywordServiceImpl;

import java.util.Collection;
import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(KeywordServiceImpl.class)
public interface KeywordService {

    /**
     * @param pLabel
     * @return ラベル文字列がpLabelに一致するオブジェクト.
     * @throws NotFound
     */
    EKeyword findByLabel(String pLabel) throws NotFound;

    /**
     * @param pLabels
     * @return 指定のラベルのエンティティを返します. <br>
     *         指定のラベルがDBにまだ保存されていない場合、エンティティをnewして返します. newしたエンティティはまだ永続化されていません. <br>
     */
    List<EKeyword> findByLabels(Collection<String> pLabels);

    /**
     * @param pLabels
     * @return 指定のラベルを持つエンティティを返します. <br>
     *         該当がないラベルは、無視されます. <br>
     */
    List<EKeyword> findPersistedByLabels(Collection<String> pLabels);

    /**
     * @param pLabel ラベル文字列
     * @return INSERTしたエンティティオブジェクト.
     */
    EKeyword insert(String pLabel);

    /**
     * @param pKeyword
     */
    void insertOrUpdate(EKeyword pKeyword);

}
