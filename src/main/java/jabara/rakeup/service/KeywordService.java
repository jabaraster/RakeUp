/**
 * 
 */
package jabara.rakeup.service;

import jabara.general.NotFound;
import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.impl.KeywordServiceImpl;

import java.util.List;
import java.util.Set;

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
    List<EKeyword> findByLabels(Set<String> pLabels);

    /**
     * @param pKeyword
     */
    void insertOrUpdate(EKeyword pKeyword);

    /**
     * @param pLabel ラベル文字列
     * @return INSERTしたエンティティオブジェクト.
     */
    EKeyword insert(String pLabel);

}
