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

import de.dante.extex.color.ColorAware;
import de.dante.extex.color.ColorConverter;
import de.dante.extex.documentWriter.AbstractDocumentWriter;
import de.dante.extex.documentWriter.postscript.util.FontManager;
import de.dante.extex.documentWriter.postscript.util.HeaderManager;
import de.dante.extex.documentWriter.postscript.util.PsBasicConverter;
import de.dante.extex.documentWriter.postscript.util.PsBoxConverter;
import de.dante.extex.documentWriter.postscript.util.PsConverter;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

/**
 * This is the abstract base class for document writers producing PostScript
 * code. Here some utility methods of general nature are collected.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public abstract class AbstractPostscriptWriter extends AbstractDocumentWriter
        implements
            Configurable,
            ResourceConsumer,
            ColorAware {

    /**
     * The field <tt>boxed</tt> contains the indicator whether the box-only
     * converter should be used.
     */
    private boolean boxed;

    /**
     * The field <tt>colorConverter</tt> contains the color converter as set
     * from the managing instance.
     */
    private ColorConverter colorConverter = null;

    /**
     * The field <tt>finder</tt> contains the resource finder as set from the
     * managing instance.
     */
    private ResourceFinder finder = null;

    /**
     * Creates a new object.
     */
    public AbstractPostscriptWriter() {

        super();
        parameter.put("Creator", "ExTeX.psWriter");
        parameter.put("Title", "<title>");
        parameter.put("PageOrder", "Ascend");
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        String b = config.getAttribute("boxed");
        boxed = (b == null ? false : Boolean.valueOf(b).booleanValue());
    }

    /**
     * Create a PostScript converter.
     *
     * @param headerManager the header manager
     *
     * @return the new converter
     *
     * @throws IOException in case of an IO error
     */
    protected PsConverter makeConverter(final HeaderManager headerManager)
            throws IOException {

        Dimen width = new Dimen(Dimen.ONE_INCH);
        width.multiply(2100, 254); // A4 paper
        Dimen height = new Dimen(Dimen.ONE_INCH);
        height.multiply(2970, 254); // A4 paper

        PsConverter converter;
        if (boxed) {
            converter = new PsBoxConverter(width, height);
        } else {
            converter = new PsBasicConverter(width, height);
        }
        if (converter instanceof ColorAware) {
            ((ColorAware) converter).setColorConverter(colorConverter);
        }
        if (converter instanceof ResourceConsumer) {
            ((ResourceConsumer) converter).setResourceFinder(finder);
        }
        converter.init(headerManager);
        return converter;
    }

    /**
     * @see de.dante.extex.color.ColorAware#setColorConverter(
     *      de.dante.extex.color.ColorConverter)
     */
    public void setColorConverter(final ColorConverter converter) {

        this.colorConverter = converter;
    }

    /**
     * @see de.dante.util.resource.ResourceConsumer#setResourceFinder(
     *      de.dante.util.resource.ResourceFinder)
     */
    public void setResourceFinder(final ResourceFinder resourceFinder) {

        this.finder = resourceFinder;
    }

    /**
     * Write a meta comment according to the Document Structuring Conventions.
     *
     * @param stream the target stream to write to
     * @param name the name of the DSC comment
     *
     * @throws IOException in case of an error during writing
     */
    protected void writeDsc(final OutputStream stream, final String name)
            throws IOException {

        stream.write('%');
        stream.write('%');
        stream.write(name.getBytes());
        stream.write('\n');
    }

    /**
     * Write a meta comment according to the Document Structuring Conventions.
     *
     * @param stream the target stream to write to
     * @param name the name of the DSC comment
     * @param value the value of the DSC comment
     *
     * @throws IOException in case of an error during writing
     */
    protected void writeDsc(final OutputStream stream, final String name,
            final String value) throws IOException {

        stream.write('%');
        stream.write('%');
        stream.write(name.getBytes());
        stream.write(':');
        stream.write(' ');
        stream.write(value.getBytes());
        stream.write('\n');

    }

    /**
     * Write a meta comment according to the Document Structuring Conventions
     * containing the <tt>DocumentFonts</tt>.
     *
     * @param stream the target stream to write to
     * @param fontManager the font manager to ask for the fonts
     *
     * @throws IOException in case of an error during writing
     */
    protected void writeFonts(final OutputStream stream,
            final FontManager fontManager) throws IOException {

        stream.write("%%DocumentFonts:".getBytes());
        Font[] fonts = fontManager.listFonts();
        for (int i = 0; i < fonts.length; i++) {
            stream.write(' ');
            stream.write(fonts[i].getFontName().getBytes());
        }
        stream.write('\n');
    }
}
