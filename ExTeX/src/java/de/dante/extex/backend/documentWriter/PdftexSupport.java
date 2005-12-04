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

package de.dante.extex.backend.documentWriter;

import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.pdftex.PdfAnnotation;
import de.dante.extex.typesetter.type.node.pdftex.PdfObject;
import de.dante.extex.typesetter.type.node.pdftex.PdfRefXImage;
import de.dante.extex.typesetter.type.node.pdftex.PdfXForm;

/**
 * This interface describes the methods needed for <logo>pdfTeX</logo> support
 * to make use f the special features of PDF.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface PdftexSupport {

    /**
     * Factory method to construct a PdfAnnotation node.
     *
     * @param rule the rule specification. Only the width height and depth are
     *  relevant. Either of them can be <code>null</code>.
     * @param annotation the annotation text
     *
     * @return a PdfAnnotation node
     *
     * @throws InterpreterException in case of an error
     */
    PdfAnnotation getAnnotation(RuleNode node, String annotation)
            throws InterpreterException;

    /**
     * Factory method to construct a PdfObject node.
     *
     * @param attr the attribute text. This can be <code>null</code>
     * @param isStream boolean indicator to signal that a stream object or
     *  file object is requested
     * @param text the object
     *
     * @return a PdfObject node
     *
     * @throws InterpreterException in case of an error
     */
    PdfObject getObject(String attr, boolean isStream, String text)
            throws InterpreterException;

    /**
     * Factory method to construct a PdfXForm node.
     *
     * @param attr the attribute text. This can be <code>null</code>
     * @param resources the resources specification
     * @param box the content
     *
     * @return a PdfXForm node
     *
     * @throws InterpreterException in case of an error
     */
    PdfXForm getXForm(String attr, String resources, Box box)
            throws InterpreterException;

    /**
     * Factory method to construct a PdfXImage node.
     *
     * @param resource the resource specification
     * @param rule the rule specification. Only the width height and depth are
     *  relevant. Either of them can be <code>null</code>.
     * @param attr the attribute text. This can be <code>null</code>
     * @param page the page number
     * @param immediate the indicator that the image should be put into the
     *  PDF output without waiting for a reference
     *
     * @return a PdfXImage node
     *
     * @throws InterpreterException in case of an error
     */
    PdfRefXImage getXImage(String resource, RuleNode rule, String attr,
            long page, boolean immediate) throws InterpreterException;

    /**
     * Add some material to the PDF catalog.
     *
     * @param text the text for the catalog
     * @param action the action specification.
     *  This parameter can be <code>null</code>
     */
    void pdfcatalog(String text, ActionSpec action);

    /**
     * Retrieve the font name for a given font.
     * For instance if a font is addressed as <tt>/F12</tt> then this method
     * returns <tt>12</tt>.
     *
     * @param font the font to query
     *
     * @return the name used by PDF for the font
     */
    String pdffontname(Font font);

    /**
     * Retrieve the font object number for a given font.
     *
     * @param font the font to query
     *
     * @return the object number
     */
    long pdffontobjnum(Font font);

    /**
     * Include a set of characters from a font into the output regardless of
     * whether they are used or not.
     *
     * @param font the font
     * @param text the set of characters to include
     */
    void pdfincludechars(Font font, String text);

    /**
     * This method inserts the text to te info section.
     *
     * @param text the text to add
     */
    void pdfinfo(String text);

    /**
     * Retrieve the object index of the last annotation.
     *
     * @return the last annotation index or -1 if none
     */
    long pdflastannot();

    /**
     * Retrieve the object index of the last object.
     *
     * @return the last object index or -1 if none
     */
    long pdflastobj();

    /**
     * Retrieve the object index of the last XForm.
     *
     * @return the last XForm index or -1 if none
     */
    long pdflastxform();

    /**
     * Retrieve the object index of the last XImage.
     *
     * @return the last XImage index or -1 if none
     */
    long pdflastximage();

    /**
     * This method inserts the text to <tt>/Names</tt> array.
     *
     * @param text the text to add
     */
    void pdfnames(String text);

    /**
     * Insert some outline into the PDF output.
     *
     * @param action the action specification
     * @param count the count
     * @param text the text
     */
    void pdfoutline(ActionSpec action, long count, String text);

}
