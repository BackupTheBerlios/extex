/*
 * Copyright (C) 2004 The ExTeX Group
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.tools.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Create a new Configfile from extex.xml and a extension-config-file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class CreateConfig {

    /**
     * filebuffe
     */
    private static final int FILEBUFFER = 0x2000;

    /**
     * Create a new object.
     */
    public CreateConfig() {

        super();
    }

    /**
     * Create a new config-file
     * @param orgconfig     the original  (extex.xml)
     * @param extconfig     the extension (extex-ext.xml)
     * @param newconfig     the new       (extex32.xml)
     * @throws JDOMException ...
     * @throws IOException ...
     */
    public void create(final File orgconfig, final File extconfig,
            final File newconfig) throws JDOMException, IOException {

        // create a document with SAXBuilder (without validate)
        SAXBuilder builder = new SAXBuilder(false);

        Document docorg = builder.build(new BufferedInputStream(
                new FileInputStream(orgconfig), FILEBUFFER));

        Document docext = builder.build(new BufferedInputStream(
                new FileInputStream(extconfig), FILEBUFFER));

        // 'ExTeX'
        Element extexroot = docext.getRootElement();

        List list = extexroot.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element element = (Element) list.get(i);
            boolean extappend = Boolean.valueOf(
                    element.getAttributeValue("extappend")).booleanValue();
            append(docorg, element, extappend);
        }

        // write to xml-file
        XMLOutputter xmlout = new XMLOutputter("   ", true);
        xmlout.setTrimAllWhite(true);
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(newconfig), FILEBUFFER);
        xmlout.output(docorg, out);
        out.close();
    }

    /**
     * Append a Element
     * @param docorg    the originla
     * @param add       the <code>Elelement</code> to add
     * @param extappend append or replace
     * @throws JDOMException ...
     */
    private void append(final Document docorg, final Element add,
            final boolean extappend) throws JDOMException {

        LinkedList list = new LinkedList();
        getParentList(list, add);
        if (extappend) {
            // childs?
            List childs = add.getChildren();
            if (childs.size() > 0) {
                for (int i = 0; i < childs.size(); i++) {
                    Element element = (Element) childs.get(i);
                    boolean extapp = Boolean.valueOf(
                            element.getAttributeValue("extappend"))
                            .booleanValue();
                    append(docorg, element, extapp);
                }
            }
        } else {
            // replace
            Element orgparent = getParent(docorg, list);
            if (orgparent == null) {
                throw new JDOMException("parent not found");
            }
            boolean extkill = Boolean.valueOf(add.getAttributeValue("extkill"))
                    .booleanValue();
            Element copy = (Element) add.clone();
            if (extkill) {
                orgparent.removeChildren(copy.getName());
                copy.removeAttribute("extkill");
            }
            orgparent.addContent(copy);
        }
    }

    /**
     * Return the parent of an element in the original-document
     * @param docorg    the original
     * @param list      the list
     * @return  the parent elment or <code>null</code>, if not found
     */
    private Element getParent(final Document docorg, final List list) {

        Element parent = docorg.getRootElement();
        for (int i = 1; i < list.size(); i++) {
            String name = (String) list.get(i);
            parent = parent.getChild(name);
        }
        return parent;
    }

    /**
     * Return a list of the parents
     * @param list      the list
     * @param element   the element
     * @throws JDOMException ...
     */
    private void getParentList(final List list, final Element element)
            throws JDOMException {

        Element parent = element.getParent();
        if (parent != null) {
            list.add(0, parent.getName());
            getParentList(list, parent);
        }
    }

    /**
     * parameter
     */
    private static final int PARAMETER = 3;

    /**
     * main
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        if (args.length != PARAMETER) {
            System.err.println("java de.dante.extex.plugin.CreateConfig "
                    + "<org-file> <ext-file> <new-file>");
            System.exit(1);
        }

        try {

            File orgfile = new File(args[0]);
            File extfile = new File(args[1]);
            File newfile = new File(args[2]);

            (new CreateConfig()).create(orgfile, extfile, newfile);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}