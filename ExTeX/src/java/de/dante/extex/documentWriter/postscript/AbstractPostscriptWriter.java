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
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.color.ColorAware;
import de.dante.extex.color.ColorConverter;
import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.postscript.util.PsBasicConverter;
import de.dante.extex.documentWriter.postscript.util.PsConverter;
import de.dante.extex.documentWriter.postscript.util.FontManager;
import de.dante.extex.documentWriter.postscript.util.PsBoxConverter;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

/**
 * This is the abstract base class for document writers producing PostScript
 * code. Here some utility methods of general nature are collected.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractPostscriptWriter
        implements
            DocumentWriter,
            Configurable,
            ResourceConsumer,
            ColorAware {

    /**
     * The field <tt>boxed</tt> contains the indicator whether the box-only
     * converter should be used.
     */
    private boolean boxed;

    /**
     * The field <tt>colorConverter</tt> contains the ...
     */
    private ColorConverter colorConverter = null;

    /**
     * The field <tt>finder</tt> contains the ...
     */
    private ResourceFinder finder = null;

    /**
     * The field <tt>parameter</tt> contains the map for parameters.
     */
    private Map parameter = new HashMap();

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
     * Getter for a named parameter.
     *
     * @param name the name of the parameter
     *
     * @return the value of the parameter or <code>null</code> if none exists
     */
    protected String getParameter(final String name) {

        return (String) parameter.get(name);
    }

    /**
     * Create a PostScript converter.
     *
     * @return the new converter
     */
    protected PsConverter makeConverter() {

        Dimen y = new Dimen(Dimen.ONE_INCH);
        y.multiply(2970, 254); // A4 paper

        y.subtract(Dimen.ONE_INCH);

        PsConverter converter;
        if (boxed) {
            converter = new PsBoxConverter(Dimen.ONE_INCH, y);
        } else {
            converter = new PsBasicConverter(Dimen.ONE_INCH, y);
        }
        if (converter instanceof ColorAware) {
            ((ColorAware) converter).setColorConverter(colorConverter);
        }
        if (converter instanceof ResourceConsumer) {
            ((ResourceConsumer) converter).setResourceFinder(finder);
        }
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
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        parameter.put(name, value);
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
