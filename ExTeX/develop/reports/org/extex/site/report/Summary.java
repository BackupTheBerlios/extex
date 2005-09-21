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

package org.extex.site.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.Date;

import sun.misc.Compare;
import sun.misc.Sort;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Summary {

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) throws FileNotFoundException {

        String directory = ".";

        int i = 0;

        for (; i < args.length; i++) {
            String a = args[i];
            if ("-directory".startsWith(a)) {
                if (++i < args.length) {
                    directory = args[i];
                }
            } else {

            }
        }

        File dir = new File(directory);
        String[] list = dir.list(new FilenameFilter() {

            /**
             * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
             */
            public boolean accept(final File dir, final String name) {

                return name.endsWith(".xml");
            }
        });

        Sort.quicksort(list, new Compare() {

            /**
             * @see sun.misc.Compare#doCompare(java.lang.Object, java.lang.Object)
             */
            public int doCompare(final Object arg0, final Object arg1) {

                return ((String) arg0).compareTo((String) arg1);
            }
        });

        PrintStream stream = System.out;
        stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        stream.print("<summary name=\"");
        stream.print(dir.getAbsolutePath());
        stream.print("\" date=\"");
        stream.print(new Date().toString());
        stream.println("\">");

        for (int j = 0; j < list.length; j++) {
            stream.print("  <file name=\"");
            stream.print(list[j]);
            stream.println("\"/>");
        }
        stream.println("</summary>");
    }

    /**
     * Creates a new object.
     */
    private Summary() {

        super();
    }

}
