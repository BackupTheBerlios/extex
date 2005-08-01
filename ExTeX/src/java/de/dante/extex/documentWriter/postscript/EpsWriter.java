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

package de.dante.extex.documentWriter.postscript;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.MultipleDocumentStream;
import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.extex.documentWriter.postscript.util.FontManager;
import de.dante.extex.documentWriter.postscript.util.HeaderManager;
import de.dante.extex.documentWriter.postscript.util.PsConverter;
import de.dante.extex.documentWriter.postscript.util.Unit;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.framework.configuration.Configurable;

/**
 * This document writer produces Encapsulated Postscript documents.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class EpsWriter extends AbstractPostscriptWriter
        implements
            MultipleDocumentStream,
            Configurable {

    /**
     * The field <tt>converter</tt> contains the converter to use.
     */
    private PsConverter converter;

    /**
     * The field <tt>fontManager</tt> contains the font manager.
     */
    private FontManager fontManager;

    /**
     * The field <tt>headerManager</tt> contains the header manager.
     */
    private HeaderManager headerManager = new HeaderManager();

    /**
     * The field <tt>init</tt> contains the indicator whether the initialization
     * is still required.
     */
    private boolean init = true;

    /**
     * The field <tt>pages</tt> contains the number of pages already processed.
     */
    private int pages = 0;

    /**
     * The field <tt>writerFactory</tt> contains the factory for output streams.
     */
    private OutputStreamFactory writerFactory = null;

    /**
     * Creates a new object.
     *
     * @param options the options for the document writer
     */
    public EpsWriter(final DocumentWriterOptions options) {

        super();
        fontManager = new FontManager();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() {

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "eps";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return pages;
    }

    /**
     * @see de.dante.extex.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory factory) {

        this.writerFactory = factory;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes)
            throws GeneralException,
                IOException {

        if (init) {
            init = false;

            headerManager.reset();
            converter = makeConverter(headerManager);
        }

        OutputStream stream = writerFactory.getOutputStream();

        byte[] bytes = converter.nodesToPostScript(nodes, fontManager,
                headerManager);

        stream.write("%!PS-Adobe-2.0 EPSF-2.0\n".getBytes());
        writeDsc(stream, "Creator", getParameter("Creator"));
        writeDsc(stream, "Title", getParameter("Title"));
        writeBB(stream, "BoundingBox", nodes);
        writeHRBB(stream, "HiResBoundingBox", nodes);
        writeFonts(stream, fontManager);
        writeDsc(stream, "EndComments");
        fontManager.write(stream);
        fontManager.clear();
        headerManager.write(stream);
        stream.write(bytes);
        writeDsc(stream, "EOF");
        stream.close();
        stream = null;
        pages++;
    }

    /**
     * Write a BoundingBox DSC to an output stream.
     *
     * @param stream the target stream to write to
     * @param name the name of the DSC comment
     * @param nodes the nodes to extract the dimensions from
     *
     * @throws IOException in case of an error during writing
     */
    private void writeBB(final OutputStream stream, final String name,
            final NodeList nodes) throws IOException {

        StringBuffer sb = new StringBuffer();
        stream.write('%');
        stream.write('%');
        stream.write(name.getBytes());
        stream.write(':');
        stream.write(' ');
        stream.write("0 0 ".getBytes());
        Unit.toPoint(nodes.getWidth(), sb, true);
        stream.write(sb.toString().getBytes());
        sb.delete(0, sb.length() - 1);
        stream.write(' ');
        Dimen d = new Dimen(nodes.getHeight());
        d.add(nodes.getDepth());
        Unit.toPoint(d, sb, true);
        stream.write(sb.toString().getBytes());
        stream.write('\n');
    }

    /**
     * Write a HiResBoundingBox DSC to an output stream.
     *
     * @param stream the target stream to write to
     * @param name the name of the DSC comment
     * @param nodes the nodes to extract the dimensions from
     *
     * @throws IOException in case of an error during writing
     */
    private void writeHRBB(final OutputStream stream, final String name,
            final NodeList nodes) throws IOException {

        StringBuffer sb = new StringBuffer();
        stream.write('%');
        stream.write('%');
        stream.write(name.getBytes());
        stream.write(':');
        stream.write(' ');
        stream.write("0 0 ".getBytes());
        Unit.toPoint(nodes.getWidth(), sb, false);
        stream.write(sb.toString().getBytes());
        sb.delete(0, sb.length() - 1);
        stream.write(' ');
        Dimen d = new Dimen(nodes.getHeight());
        d.add(nodes.getDepth());
        Unit.toPoint(d, sb, false);
        stream.write(sb.toString().getBytes());
        stream.write('\n');
    }

}
