/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.dynamic.java;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.dynamic.Loader;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This primitive initiates the loading of Java code and implements the
 * primitive <tt>\javaload</tt>
 *
 * <doc name="javaload">
 * <h3>The Primitive <tt>\javaload</tt></h3>
 * <p>
 *  The primitive <tt>\javaload</tt> loads a java class and invokes its
 *  <tt>init()</tt> method. With this method it is possible to load
 *  larger extensions of <logo>ExTeX</logo> in one junk. There is no need to
 *  declare each single macro with <tt>\javadef</tt>.
 * </p>
 * <p>
 *  The general form of this primitive is
 * <pre class="syntax">
 *   &lang;javaload&rang;
 *       &rarr; <tt>\javaload</tt> &lang;tokens&rang; </pre>
 * </p>
 * <p>
 *  The <i>&lang;tokens&rang;</i> is any specification of a list of
 *  tokens like a constant list enclosed in braces or a toks register.
 *  The value of these tokens are taken and interpreted as the name of
 *  a Java class. This class is loaded if needed, instantiated, and its
 *  method
 *  {@link de.dante.extex.interpreter.primitives.dynamic.java.Loadable#init(
 *    de.dante.extex.interpreter.context.Context,
 *    de.dante.extex.typesetter.Typesetter) init()}
 *  is invoked. The instantiation requires the empty contructor to be visible.
 * </p>
 * <p>
 *  The following example illustrates the use of this primitive:
 * <pre class="TeXSample">
 *   \javaload{de.dante.extex.extensions.Basic} </pre>
 * </p>
 * <p>
 *  For the loading of the Java class it is necessary that this Java
 *  class implements the interface
 *  {@link de.dante.extex.interpreter.primitives.dynamic.java.Loadable Loadable}.
 * <pre class="JavaSample">
 *   <b>package</b> my.package;
 *
 *   <b>import</b> de.dante.extex.interpreter.contect.Context;
 *   <b>import</b> de.dante.extex.interpreter.primitives.dynamic.java.Loadable;
 *   <b>import</b> de.dante.extex.typesetter.Typesetter;
 *   <b>import</b> de.dante.util.GeneralException;
 *
 *   <b>class</b> MyModule <b>implements</b> Loadable {
 *
 *     <b>public</b> MyModule() {
 *       super();
 *       <i>// initialization code &ndash; if required</i>
 *     }
 *
 *     <b>public void</b> init(<b>final</b> Context context,
 *                      <b>final</b> Typesetter typesetter
 *                     ) <b>throws</b> GeneralException {
 *       <i>// implement the initialization code here</i>
 *     }
 *   } </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public class JavaLoad extends AbstractCode implements Loader {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     * This method is needed for the nativeload wrapper.
     *
     */
    public JavaLoad() {

        super("");
    }

    /**
     * Creates a new object.
     *
     * @param codeName the name for debugging
     */
    public JavaLoad(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        load(context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.dynamic.Loader#load(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void load(final Context context, final TokenSource source,
            final Typesetter typesetter)
            throws InterpreterException {

        Tokens name = source.getTokens(context, source, typesetter);
        String classname = name.toText();
        if ("".equals(classname)) {
            throw new HelpingException(getLocalizer(), "ClassNotFound",
                    classname);
        }
        Loadable component;

        try {
            component = (Loadable) (Class.forName(classname).newInstance());
            component.init(context, typesetter);
        } catch (ClassNotFoundException e) {
            throw new HelpingException(getLocalizer(), "ClassNotFound",
                    classname);
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }
}