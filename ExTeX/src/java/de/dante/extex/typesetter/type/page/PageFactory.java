/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.typesetter.type.page;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.max.StringSource;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.MarkNode;
import de.dante.extex.typesetter.type.node.SpecialNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides a factory for page instances.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class PageFactory implements LogEnabled {

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private Logger logger = null;

    /**
     * The field <tt>sizePattern</tt> contains the pattern for matching the
     * <tt>papersize</tt> special.
     */
    private Pattern sizePattern;

    /**
     * Creates a new object.
     *
     */
    public PageFactory() {

        super();
        sizePattern = Pattern
                .compile("papersize=([0-9.]+[a-z][a-z]),([0-9.]+[a-z][a-z])");
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger log) {

        logger = log;
    }

    /**
     * Get a new instance of a page.
     *
     * @param nodes the nodes contained
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @return the new instance
     *
     * @throws GeneralException in case of an error
     */
    public Page newInstance(final NodeList nodes, final Context context,
            final Typesetter typesetter) throws GeneralException {

        PageImpl page = new PageImpl(nodes);

        page.setMediaWidth(context.getDimen("mediawidth"));
        page.setMediaHeight(context.getDimen("mediaheight"));
        //page.setMediaHOffset(context.getDimen("mediawidth"));
        //page.setMediaVOffset(context.getDimen("mediaheight"));

        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            Node node = nodes.get(i);
            if (node instanceof MarkNode) {
                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");

            } else if (node instanceof NodeList) {
                process(page, (NodeList) node, context, typesetter);
            } else if (node instanceof WhatsItNode) {
                node.atShipping(context, typesetter);

                if (node instanceof SpecialNode) {
                    processSpecialNode((SpecialNode) node, page, context,
                            typesetter);
                }
            }
        }
        return page;
    }

    /**
     * Traverse a page and process special nodes.
     *
     * @param page the page to process
     * @param nodes the nodes to traverse
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an error
     */
    private void process(final Page page, final NodeList nodes,
            final Context context, final Typesetter typesetter)
            throws GeneralException {

        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            Node node = nodes.get(i);
            if (node instanceof NodeList) {
                process(page, (NodeList) node, context, typesetter);
            } else if (node instanceof WhatsItNode) {
                node.atShipping(context, typesetter);

                if (node instanceof SpecialNode) {
                    processSpecialNode((SpecialNode) node, page, context,
                            typesetter);
                }
            }
        }
    }

    /**
     * Have a closer look at a special node.
     *
     * @param node the node encountered
     * @param page the page under consideration
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    protected void processSpecialNode(final SpecialNode node, final Page page,
            final Context context, final Typesetter typesetter)
            throws InterpreterException {

        String text = node.getText();

        if (text.startsWith("papersize=")) {
            Matcher m = sizePattern.matcher(text);
            if (m.matches()) {
                try {
                    Dimen width = new Dimen(context, //
                            new StringSource(m.group(1)), typesetter);
                    Dimen height = new Dimen(context, //
                            new StringSource(m.group(2)), typesetter);
                    page.setMediaWidth(width);
                    page.setMediaHeight(height);
                } catch (ConfigurationException e) {
                    logger.log(Level.SEVERE, "", e);
                }
            } else {
                logger.warning("...");
            }

        } else if (text.equals("landscape")) {

            Dimen h = page.getMediaHeight();
            page.setMediaHeight(page.getMediaWidth());
            page.setMediaWidth(h);
        }
    }

}
