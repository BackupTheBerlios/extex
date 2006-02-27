/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
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

package de.dante.extex.typesetter;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.hyphenator.Hyphenator;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.extex.typesetter.type.page.PageFactory;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is the factory for instances of
 * {@link de.dante.extex.typesetter.Typesetter Typesetter}.
 *
 * @TODO documentation incomplete
 *
 * <pre>
 *  &lt;Typesetter default="someType"&gt;
 *    &lt;someType class="the.package.TheClass"&gt;
 *      &lt;LigatureBuilder class="some.package.SomeClass"/&gt;
 *      &lt;PageBuilder class="someOther.package.SomeOtherClass"/&gt;
 *      &lt;ParagraphBuilder class="another.package.AnotherClass"&gt;
 *        &lt;Hyphenator class="an.other.package.AnOtherClass"/&gt;
 *      &lt;/ParagraphBuilder&gt;
 *    &lt;/someType&gt;
 *  &lt;/Typesetter&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.22 $
 */
public class TypesetterFactory extends AbstractFactory {

    /**
     * Creates a new object.
     *
     * @param configuration the configuration for this factory
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TypesetterFactory(final Configuration configuration)
            throws ConfigurationException {

        super();
        configure(configuration);
    }

    /**
     * Make the node factory according to the specification
     *
     * @param config the configuration to use
     *
     * @return the node factory
     *
     * @throws ConfigurationException in case of an configuration error
     */
    private NodeFactory makeNodeFactory(final Configuration config)
            throws ConfigurationException {

        Configuration cfg = config.getConfiguration("NodeFactory");
        NodeFactory nodeFactory = (NodeFactory) createInstanceForConfiguration(
                cfg, NodeFactory.class);
        if (nodeFactory instanceof LogEnabled) {
            ((LogEnabled) nodeFactory).enableLogging(getLogger());
        }
        if (nodeFactory instanceof Configurable) {
            ((Configurable) nodeFactory).configure(cfg);
        }
        return nodeFactory;
    }

    /**
     * Make a new page builder according to the specification in the
     * configuration. The sub-configuration <code>PageBuilder</code> is used
     * to determine the requested properties.
     *
     * @param config the configuration to use
     * @param context the interpreter context
     * @param typesetter the typesetter associated with it
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     * @throws TypesetterException in case of an error
     */
    private PageBuilder makePageBuilder(final Configuration config,
            final Context context, final Typesetter typesetter)
            throws ConfigurationException,
                TypesetterException {

        Configuration cfg = config.getConfiguration("PageBuilder");
        PageBuilder pageBuilder = (PageBuilder) createInstanceForConfiguration(
                cfg, PageBuilder.class);
        pageBuilder.setContext(context);
        PageFactory pageFactory = new PageFactory();
        if (pageFactory instanceof LogEnabled) {
            ((LogEnabled) pageFactory).enableLogging(getLogger());
        }
        pageBuilder.setPageFactory(pageFactory);
        return pageBuilder;
    }

    /**
     * Make a new paragraph builder according to the specification in the
     * configuration. The sub-configuration <code>ParagraphBuilder</code> is used
     * to determine the requested properties.
     *
     * @param config the configuration to use
     *
     * @return the new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    private ParagraphBuilder makeParagraphBuilder(final Configuration config,
            final TypesetterOptions options) throws ConfigurationException {

        Configuration cfg = config.getConfiguration("ParagraphBuilder");
        ParagraphBuilder builder = (ParagraphBuilder) createInstanceForConfiguration(
                cfg, ParagraphBuilder.class);
        if (builder instanceof HyphenationEnabled) {
            cfg = cfg.findConfiguration("Hyphenator");
            if (cfg != null) {
                Hyphenator hyphenator = (Hyphenator) createInstanceForConfiguration(
                        cfg, Hyphenator.class);
                ((HyphenationEnabled) builder).enableHyphenation(hyphenator);
            }
        }
        builder.setOptions(options);
        return builder;
    }

    /**
     * Get an instance of a typesetter.
     *
     * @param type the symbolic name of the configuration to use
     * @param context the interpreter context
     *
     * @return a new typesetter
     *
     * @throws ConfigurationException in case of an configuration error
     * @throws TypesetterException in case of another error
     */
    public Typesetter newInstance(final String type, final Context context)
            throws TypesetterException,
                ConfigurationException {

        Configuration cfg = selectConfiguration(type);

        Typesetter typesetter = (Typesetter) createInstance(type,
                Typesetter.class);
        typesetter.setNodeFactory(makeNodeFactory(cfg));
        typesetter.setParagraphBuilder(makeParagraphBuilder(cfg,
                (TypesetterOptions) context));
        typesetter.setPageBuilder(makePageBuilder(cfg, context, typesetter));
        typesetter.setOptions((TypesetterOptions) context);

        return typesetter;
    }

}
