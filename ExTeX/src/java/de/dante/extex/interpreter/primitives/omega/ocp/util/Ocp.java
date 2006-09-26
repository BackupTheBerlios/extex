/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.omega.ocp.util;

import java.io.InputStream;
import java.io.Serializable;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.ResourceFinder;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Ocp implements Code, Serializable {

    /**
     * The field <tt>serialVersionUID</tt> contains the ...
     */
    private static final long serialVersionUID = 2006;

    /**
     * This is a factory method for OCPs.
     *
     * @param resource the name of the resource
     * @param finder the resource finder
     *
     * @return the OCP encountered
     */
    public static Ocp load(final String resource, final ResourceFinder finder)
            throws InterpreterException {

        try {
            InputStream stream = finder.findResource(resource, "ocp");
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
        // TODO gene: load unimplemented
        return new Ocp(resource);
    }

    /**
     * The field <tt>name</tt> contains the name.
     */
    private String name;

    /**
     * Creates a new object.
     *
     * @param resource the name of the resource
     */
    public Ocp(final String resource) {

        super();
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

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#getName()
     */
    public String getName() {

        return name;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#isIf()
     */
    public boolean isIf() {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#isOuter()
     */
    public boolean isOuter() {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#setName(java.lang.String)
     */
    public void setName(final String name) {

        this.name = name;
    }

}
