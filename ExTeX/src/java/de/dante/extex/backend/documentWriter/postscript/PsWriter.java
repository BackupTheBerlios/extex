/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter.postscript;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.backend.documentWriter.postscript.util.FontManager;
import de.dante.extex.backend.documentWriter.postscript.util.HeaderManager;
import de.dante.extex.backend.documentWriter.postscript.util.PsConverter;
import de.dante.extex.backend.documentWriter.postscript.util.PsUnit;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configurable;

/**
 * This document writer produces multi-page PostScript documents.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class PsWriter extends AbstractPostscriptWriter
        implements
            SingleDocumentStream,
            Configurable {

    /**
     * The field <tt>DF</tt> contains the formatter for the date.
     */
    private static final DateFormat DF = new SimpleDateFormat(
            "MM/dd/yyyy HH:mm:ss");

    /**
     * The field <tt>fontManager</tt> contains the font manager.
     */
    private FontManager fontManager = new FontManager();

    /**
     * The field <tt>headerManager</tt> contains the header manager.
     */
    private HeaderManager headerManager = new HeaderManager();

    /**
     * The field <tt>page</tt> contains the byte arrays of the single pages.
     */
    private List page = new ArrayList();

    /**
     * The field <tt>pageHeight</tt> contains the height of the paper.
     */
    private Dimen pageHeight = new Dimen(Dimen.ONE * 297 * 7227 / 2540);

    /**
     * The field <tt>pageWidth</tt> contains the width of the paper.
     */
    private Dimen pageWidth = new Dimen(Dimen.ONE * 210 * 7227 / 2540);

    /**
     * The field <tt>writer</tt> contains the target output stream.
     */
    private OutputStream stream = null;

    /**
     * Creates a new object.
     *
     * @param options the options for the document writer
     */
    public PsWriter(final DocumentWriterOptions options) {

        super();
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() throws DocumentWriterException, IOException {

        int pages = page.size();
        stream.write("%!PS-Adobe-3.0\n".getBytes());
        writeDsc(stream, "Creator", getParameter("Creator"));
        writeDsc(stream, "CreationDate", DF.format(Calendar.getInstance()
                .getTime()));
        writeDsc(stream, "Title", getParameter("Title"));
        writeDsc(stream, "Pages", Integer.toString(pages));
        writeDsc(stream, "PageOrder", getParameter("PageOrder"));

        stream.write("%%BoundingBox: 0 0 ".getBytes());
        StringBuffer sb = new StringBuffer(' ');
        PsUnit.toPoint(pageWidth, sb, true);
        sb.append(' ');
        PsUnit.toPoint(pageHeight, sb, true);
        sb.append('\n');
        stream.write(sb.toString().getBytes());

        writeFonts(stream, fontManager);
        writeDsc(stream, "EndComments");
        headerManager.write(stream);
        fontManager.write(stream);
        fontManager.clear();

        if ("Descend".equals(getParameter("PageOrder"))) {
            for (int i = pages - 1; i >= 0; i--) {
                writePage(i);
            }
        } else {
            for (int i = 0; i < pages; i++) {
                writePage(i);
            }
        }
        writeDsc(stream, "EOF");
        stream.close();
        stream = null;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "ps";
    }

    /**
     * @see de.dante.extex.backend.documentWriter.SingleDocumentStream#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream out) {

        this.stream = out;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
     */
    public int shipout(final Page p) throws GeneralException, IOException {

        PsConverter converter = getConverter(headerManager);
        page.add(converter.toPostScript(p, fontManager, headerManager));
        return 1;
    }

    /**
     * Write a single page to the output.
     *
     * @param no the page number in the array
     *
     * @throws IOException in case of an error during writing
     */
    private void writePage(final int no) throws IOException {

        stream.write("%%Page: ".getBytes());
        byte[] pageno = Integer.toString(no + 1).getBytes();
        stream.write(pageno);
        stream.write(' ');
        stream.write(pageno);
        stream.write('\n');
        stream.write((byte[]) page.get(no));
        stream.write("showpage\n".getBytes());
    }

}
