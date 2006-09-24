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

package de.dante.extex.scanner.stream;

import java.io.InputStream;

/**
 * This interface describes the ability to manipulate an
 * {@link java.io.InputStream InputStream} by attaching additional processing
 * units in a pipe mannor.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface StreamDecorator {

    /**
     * Attach a processor to an input stream.
     * If the decorator decides that no additional pipe element is required it
     * shoudl simply return the input stream.
     *
     * @param stream the stream to add some processing unit to
     *
     * @return the new input stream. This value should never be
     *   <code>null</code>.
     */
    InputStream pipe(InputStream stream);

}
