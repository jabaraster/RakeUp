/**
 * 
 */
package jabara.rakeup.service;

/**
 * @author jabaraster
 */
public final class NotFound extends Exception {
    private static final long    serialVersionUID = 1059856358839734808L;

    /**
     * 
     */
    public static final NotFound INSTANCE         = new NotFound();

    private NotFound() {
        // 処理なし
    }
}
