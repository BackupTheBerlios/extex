/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import java.lang.reflect.InvocationTargetException;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;


/**
 * This primitive provides a binding of a macro or active character to
 * Java code. This code implements the primitive <tt>\javadef</tt>.
 *
 * <doc name="javadef">
 * <h3>The Primitive <tt>\javadef</tt></h3>
 * <p>
 * The primitive <tt>\javadef</tt> attaches a definition to a macro or
 * active character. This is done in a similar way as <tt>\def</tt>
 * works. The difference is that the definition has to be provided in
 * form of a Java class.
 * </p>
 * <p>
 * The general form of this primitive is
 * <pre class="syntax">
 *   &lang;javaload&rang;
 *       &rarr; <tt>\javadef</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *       &lang;control sequence&rang;} &lang;<i>tokens</i>&rang; </pre>
 * </p>
 * <p>
 * The &lang;<i>control sequence</i>&rang; is any macro or active
 * character. If this token is missing or of the wrong type then an
 * error is raised.
 * </p>
 * <p>
 * The &lang;<i>tokens</i>&rang; is any specification of a list of
 * tokens like a constant list enclosed in braces or a toks register.
 * The value of these tokens are taken and interpreted as the name of
 * a Java class. This class is loaded if needed and instantiated. The
 * instance is bound as code to the &lang;<i>control
 * sequence</i>&rang;.
 * </p>
 * <p>
 * The following example illustrates the use of this primitive:
 * <pre class="TeXSample">
 *   \javadef\abc{de.dante.extex.interpreter.primitive.Relax} </pre>
 * </p>
 * <p>
 * The primitive <tt>\javadef</tt> is local to the enclosing group as
 * is <tt>\def</tt>. And similar to <tt>\def</tt> the modifier
 * <tt>\global</tt> can be used to make the definition in all groups
 * instead of the current group only. This is shown in the following
 * example:
 * <pre class="TeXSample">
 *   \global\javadef\abc{de.dante.extex.interpreter.primitive.Relax}
 * </pre>
 * </p>
 * <p>
 * Now we come to the Java side of the definition. The class given as
 * &lang;<i>tokens</i>&rang; must implement the interface {@link
 * de.dante.extex.interpreter.type.Code Code}. The easiest way to achieve
 * this is by declaring a class derived from {@link
 * de.dante.extex.interpreter.type.AbstractCode AbstractCode}.
 * <pre class="JavaSample">
 *   <b>package</b> my.package;
 *
 *   <b>import</b> de.dante.extex.interpreter.AbstractCode;
 *   <b>import</b> de.dante.extex.interpreter.contect.Context;
 *   <b>import</b> de.dante.extex.interpreter.Flags;
 *   <b>import</b> de.dante.extex.interpreter.TokenSource;
 *   <b>import</b> de.dante.extex.typesetter.Typesetter;
 *   <b>import</b> de.dante.util.GeneralException;
 *
 *   <b>class</b> MyPrimitive <b>extends</b> AbstractCode {
 *
 *     <b>public</b> MyPrimitive(<b>final</b> String name) {
 *       super(name);
 *       <i>// initialization code -- if required</i>
 *     }
 *
 *     <b>public boolean</b> execute(<b>final</b> Flags prefix,
 *                            <b>final</b> Context context,
 *                            <b>final</b> TokenSource source,
 *                            <b>final</b> Typesetter typesetter
 *                           ) {
 *       <i>// implement the execution behaviour here</i>
 *       <b>return</b> <b>true</b>;
 *     }
 *   } </pre>
 * </p>
 * <p>
 * There is more to say about primitives like how to write expandable
 * primitives or ifs. Those details can be found in section
 * {@linkplain de.dante.extex.interpreter.primitives Primitives}.
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class JavaDef extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param codeName the name for debugging
     */
    public JavaDef(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        CodeToken cs = source.getControlSequence(context);
        Tokens name = source.getTokens(context);
        String classname = name.toText();
        if ("".equals(classname)) {
            throw new HelpingException(getLocalizer(), "JavaDef.ClassNotFound",
                    classname);
        }
        Code code;

        try {
            code = (Code) (Class.forName(classname)
                    .getConstructor(new Class[]{String.class})
                    .newInstance(new Object[]{cs.getName()}));
        } catch (IllegalArgumentException e) {
            throw new GeneralException(e);
        } catch (SecurityException e) {
            throw new GeneralException(e);
        } catch (InstantiationException e) {
            throw new GeneralException(e);
        } catch (IllegalAccessException e) {
            throw new GeneralException(e);
        } catch (InvocationTargetException e) {
            throw new GeneralException(e);
        } catch (NoSuchMethodException e) {
            throw new GeneralException(e);
        } catch (ClassNotFoundException e) {
            throw new HelpingException(getLocalizer(), "JavaDef.ClassNotFound",
                    classname);
        }
        context.setCode(cs, code, prefix.isGlobal());

        return true;
    }
}
