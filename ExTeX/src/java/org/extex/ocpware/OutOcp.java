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

package org.extex.ocpware;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import org.extex.ocpware.type.OcpProgram;

/**
 * This class provides a main program to print the contents of an ocp file.
 *
 * <p>
 *  The program takes as one argument the name of the ocp file:
 * </p>
 * <pre class="CLI">
 *   java org.extex.ocpware.Outocp &lang;<i>file</i>&rang;
 * </pre>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class OutOcp {

    /**
     * Creates a new object.
     */
    private OutOcp() {

    }

    /**
     * This is the command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        PrintStream err = System.err;
        String file = null;
        boolean orig = true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) {
                orig = false;
            } else if (file != null) {
                err.println("outocp: Too many arguments");
                System.exit(-1);
            } else {
                file = args[i];
            }
        }

        if (file == null) {
            err.println("outocp: No file given");
            System.exit(-1);
        }

        try {
            InputStream stream = new FileInputStream(new File(file));

            OcpProgram.load(stream).dump(System.err, orig);

            stream.close();

        } catch (FileNotFoundException e) {
            err.println("outocp: " + file + " not found");
            System.exit(-1);
        } catch (Exception e) {
            err.println(file + ": " + e.getMessage());
            System.exit(-1);
        }
    }

}
