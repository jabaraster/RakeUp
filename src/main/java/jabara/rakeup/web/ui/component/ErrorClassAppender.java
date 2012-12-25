/**
 * 
 */
package jabara.rakeup.web.ui.component;

import jabara.general.ArgUtil;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * 
 * @author jabaraster
 */
public final class ErrorClassAppender extends AttributeAppender {
    private static final long serialVersionUID = -2276179876220020173L;

    /**
     * @param pErrorClassModel エラー時にclass属性に追加する値.
     */
    public ErrorClassAppender(final IModel<?> pErrorClassModel) {
        super("class", pErrorClassModel); //$NON-NLS-1$
    }

    /**
     * @param pErrorClass エラー時にclass属性に追加する値.
     */
    public ErrorClassAppender(final Serializable pErrorClass) {
        super("class", pErrorClass); //$NON-NLS-1$
    }

    /**
     * エラーのある入力項目にclass属性値を追加します. <br>
     * 
     * @param pForm このフォーム以下の入力項目が処理対象になります. <br>
     */
    public void addErrorClass(final Form<?> pForm) {
        ArgUtil.checkNull(pForm, "pForm"); //$NON-NLS-1$

        pForm.visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, Object>() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void component(final FormComponent<?> pComponent, @SuppressWarnings("unused") final IVisit<Object> pVisit) {
                if (pComponent.isValid()) {
                    removeErrorClassAppender(pComponent);
                } else {
                    addErrorClassAppenderIfNot(pComponent);
                }
            }
        });
    }

    @SuppressWarnings("static-method" /* 本来はこれは不要なのだが付けないとeclipseコンパイラが黙らない. バグっぽい */)
    private void addErrorClassAppenderIfNot(final FormComponent<?> pComponent) {
        final List<ErrorClassAppender> behaviors = pComponent.getBehaviors(ErrorClassAppender.class);
        if (behaviors.isEmpty()) {
            pComponent.add(ErrorClassAppender.this);
        }
    }

    private static void removeErrorClassAppender(final FormComponent<?> pComponent) {
        final List<ErrorClassAppender> behaviors = pComponent.getBehaviors(ErrorClassAppender.class);
        pComponent.remove(behaviors.toArray(new Behavior[behaviors.size()]));
    }

}
