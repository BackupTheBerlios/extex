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

package de.dante.extex.backend.documentWriter.rtf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.type.font.Font;

/**
 * This class provides a container for an RTF document.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class RtfDocument {

    /**
     * The field <tt>author</tt> contains the name of the author.
     */
    private String author = ".";

    /**
     * The field <tt>buffer</tt> contains the buffer of the contents.
     */
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    /**
     * The field <tt>colorMap</tt> contains the mapping from colors to an index.
     */
    private Map colorMap = new HashMap();

    /**
     * The field <tt>colorTable</tt> contains the list of colors used.
     */
    private List colorTable = new ArrayList();

    /**
     * The field <tt>creator</tt> contains the name of the creating program.
     */
    private String creator = ".";

    /**
     * The field <tt>fontMap</tt> contains the mapping from Fonts to an index.
     */
    private Map fontMap = new HashMap();

    /**
     * The field <tt>fontTable</tt> contains the table of fonts used.
     */
    private List fontTable = new ArrayList();

    /**
     * The field <tt>pages</tt> contains the number of pages.
     */
    private int pages = 0;

    /**
     * The field <tt>title</tt> contains the title of the document.
     */
    private String title = ".";

    /**
     * Creates a new object.
     */
    public RtfDocument() {

        super();
    }

    /**
     * Getter for author.
     *
     * @return the author
     */
    public String getAuthor() {

        return this.author;
    }

    /**
     * Getter for creator.
     *
     * @return the creator
     */
    public String getCreator() {

        return this.creator;
    }

    /**
     * Getter for title.
     *
     * @return the title
     */
    public String getTitle() {

        return this.title;
    }

    /**
     * Add a color to the color table if necessary and return a unique index.
     *
     * @param col the color
     *
     * @return the unique index in the color table
     */
    protected int mapColor(final RgbColor col) {

        Rgb8Color c = new Rgb8Color(col.getRed() >> 16, col.getGreen() >> 16,
                col.getGreen() >> 16);
        Integer index = (Integer) colorMap.get(c);
        if (index == null) {
            colorTable.add(c);
            index = new Integer(colorTable.size() - 1);
            colorMap.put(c, index);
        }
        return index.intValue();
    }

    /**
     * Add a font to the font table if necessary and return a unique index.
     *
     * @param font the font register
     *
     * @return the unique index in the font table
     */
    protected int mapFont(final Font font) {

        Integer index = (Integer) fontMap.get(font.getFontName());
        if (index == null) {
            fontTable.add(font);
            index = new Integer(fontTable.size() - 1);
            fontMap.put(font.getFontName(), index);
        }
        return index.intValue();
    }

    /**
     * Start a new page.
     *
     * @throws IOException in case of an error
     */
    public void newPage(final Color color) throws IOException {

        if (pages == 0) {
            put("{\\page}");
        }
        //TODO gene: use page color
        pages++;
    }

    /**
     * Write a single byte to the target stream.
     *
     * @param b the byte to write
     *
     * @throws IOException in case of an error
     */
    protected void put(final byte b) throws IOException {

        buffer.write(b);
    }

    /**
     * Write some bytes from a string to the target stream.
     *
     * @param string the source string
     *
     * @throws IOException in case of an error
     */
    protected void put(final String string) throws IOException {

        buffer.write(string.getBytes());
    }

    /**
     * Setter for author.
     *
     * @param author the author to set
     */
    public void setAuthor(final String author) {

        this.author = author.replaceAll("[\\{}]", "");
    }

    /**
     * Setter for creator.
     *
     * @param creator the creator to set
     */
    public void setCreator(final String creator) {

        this.creator = creator.replaceAll("[\\{}]", "");
    }

    /**
     * Setter for title.
     *
     * @param title the title to set
     */
    public void setTitle(final String title) {

        this.title = title.replaceAll("[\\{}]", "");
    }

    /**
     * Write the contents of the RTF file to a target stream.
     *
     * @param stream the target stream
     *
     * @throws IOException in case of an error
     */
    public void write(final OutputStream stream) throws IOException {

        stream.write("{\\rtf1\\ansi\n".getBytes());
        stream.write("\\deff0\\kerning0\n".getBytes());
        // 1 in = 72.72 pt = 1454.4 twips
        stream.write("\\margt1454\\margl1454\n".getBytes());
        writeColorTable(stream);
        writeFontTable(stream);
        stream.write("{\\*\\generator ".getBytes());
        stream.write(creator.getBytes());
        stream.write(";}\n".getBytes());
        writeInfo(stream);

        buffer.writeTo(stream);

        stream.write("}\n".getBytes());
    }

    /**
     * Write the color table to the target stream.
     *
     * @param stream the target stream for writing
     *
     * @throws IOException in case of an error
     */
    private void writeColorTable(final OutputStream stream) throws IOException {

        int size = colorTable.size();
        if (size == 0) {
            return;
        }
        stream.write("{\\colortbl;".getBytes());
        for (int i = 0; i < size; i++) {
            Rgb8Color x = (Rgb8Color) colorTable.get(i);
            stream.write("\\red".getBytes());
            stream.write(Integer.toString(x.getRed()).getBytes());
            stream.write("\\green".getBytes());
            stream.write(Integer.toString(x.getGreen()).getBytes());
            stream.write("\\blue".getBytes());
            stream.write(Integer.toString(x.getBlue()).getBytes());
            stream.write(";".getBytes());
        }
        stream.write("}".getBytes());
    }

    /**
     * Write the font table to the target stream.
     *
     * @param stream the target stream for writing
     *
     * @throws IOException in case of an error
     */
    private void writeFontTable(final OutputStream stream) throws IOException {

        int size = fontTable.size();
        if (size == 0) {
            return;
        }
        stream.write("{\\fonttbl".getBytes());
        for (int i = 0; i < size; i++) {
            Font f = (Font) fontTable.get(i);
            stream.write("\n{\\f".getBytes());
            stream.write(Integer.toString(i).getBytes());
            stream.write("\\froman ".getBytes());
            stream.write(f.getFontName().getBytes());
            stream.write(";}".getBytes());
        }
        stream.write("}".getBytes());
    }

    /**
     * Write the info section to the target stream.
     *
     * @param stream the target stream for writing
     *
     * @throws IOException in case of an error
     */
    private void writeInfo(final OutputStream stream) throws IOException {

        stream.write("{\\info".getBytes());
        writeInfoItem(stream, "title ", ("".equals(title) ? "." : title));
        writeInfoItem(stream, "author ", ("".equals(author) ? "." : author));
        writeInfoTime(stream, "creatim");
        //infoItem(stream, "version", "1");
        //stream.write("{\\edmins0}".getBytes());
        writeInfoItem(stream, "nofpages", Integer.toString(pages));
        //stream.write("{\\nofwords0}".getBytes());
        //stream.write("{\\nofchars3}".getBytes());
        stream.write("{\\*\\company .}".getBytes());
        //stream.write("{\\nofcharsws3}".getBytes());
        //stream.write("{\\vern16391}".getBytes());
        stream.write("}\n".getBytes());
    }

    /**
     * Write an info item to the target stream.
     *
     * @param stream the target stream for writing
     * @param tag the name of the item
     * @param value the value of the item
     *
     * @throws IOException in case of an error
     */
    private void writeInfoItem(final OutputStream stream, final String tag,
            final String value) throws IOException {

        stream.write("{\\".getBytes());
        stream.write(tag.getBytes());
        stream.write(value.getBytes());
        stream.write("}".getBytes());
    }

    /**
     * Write the current date in a format suitable for an info item.
     *
     * @param stream the target stream for writing
     *
     * @throws IOException in case of an error
     */
    private void writeInfoTime(final OutputStream stream, final String tag)
            throws IOException {

        String now = new SimpleDateFormat(
                "'\\yr'y'\\mo'm'\\dy'd'\\hr'h'\\min'm").format(new Date());
        stream.write("{\\".getBytes());
        stream.write(tag.getBytes());
        stream.write(now.getBytes());
        stream.write("}".getBytes());
    }

}
