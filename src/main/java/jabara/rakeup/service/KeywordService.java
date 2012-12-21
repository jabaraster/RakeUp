/**
 * 
 */
package jabara.rakeup.service;

import jabara.rakeup.entity.EKeyword;
import jabara.rakeup.service.impl.KeywordServiceImpl;

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
     * @param pLabel ラベル文字列
     * @return INSERTしたエンティティオブジェクト.
     */
    EKeyword insert(String pLabel);

}
