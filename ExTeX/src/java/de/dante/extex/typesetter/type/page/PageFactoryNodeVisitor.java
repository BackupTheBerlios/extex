
package de.dante.extex.typesetter.type.page;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.NodeVisitor;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface PageFactoryNodeVisitor extends NodeVisitor {

    /**
     * Setter for the context.
     *
     * @param context the context
     */
    void setContext(Context context);

    /**
     * Setter for the page.
     *
     * @param page the page
     */
    void setPage(Page page);

    /**
     * Setter for the typesetter.
     *
     * @param typsetter the typesetter
     */
    void setTypesetter(Typesetter typsetter);

}
