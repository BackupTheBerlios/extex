/*
 * Copyright (C) 2004 Michael Niedermair
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
package de.dante.util.font;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * This class implements a converter from SVG-font to EFM-font.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
// TODO incomplete
public class SVG2EFM {

	/**
	 * only for test
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("java de.dante.util.font.SVG2EFM <svg-file> <efm-file>");
			System.exit(1);
		}

		try {

			(new SVG2EFM()).convertSVG2EFM(args[0], args[1]);

		} catch (JDOMException e) {
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	
	/**
	 * Convert a SVG-font to a EFM-font.
	 * 
	 * @param svgfile	the svg-filename
	 * @param efmfile	the efm-filename
	 * @throws IOException	...
	 */
	public void convertSVG2EFM(String svgfile, String efmfile) throws IOException, JDOMException {

		File svg = new File(svgfile);
		File efm = new File(efmfile);

		// create a document with SAXBuilder (without validate)
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(svg);

		Element svgfont = scanForElement(doc.getRootElement(), "font");

		if (svgfont == null) {
			throw new IOException("element 'font' not found!"); //TODO change
		}

		// create efm-document
		Element efmroot = new Element("fontgroup");
		efmroot.setAttribute("name", filenameWithoutExtensionAndPath(svgfile));
		efmroot.setAttribute("id", filenameWithoutExtensionAndPath(svgfile));
		Document efmdoc = new Document(efmroot);
		Element efmfont = new Element("font");
		efmroot.addContent(efmfont);

		// copy font-attributes
		List list = svgfont.getAttributes();
		for (int i = 0; i < list.size(); i++) {
			Attribute at = (Attribute) list.get(i);
			at.detach();
			efmfont.setAttribute(at);
		}
		efmfont.setAttribute("type","svg");
		efmfont.setAttribute("filename", filenameWithoutPath(svgfile));

		// copy element font-face
		Element svgfontface = scanForElement(svgfont, "font-face");
		addElement(svgfontface,"font-face", efmfont);

		// copy element missing-glyph
		Element svgmissingglyph = scanForElement(svgfont, "missing-glyph");
		addElement(svgmissingglyph,"missing-glyph", efmfont);
		
		// copy each element glyph
		List glyphlist = svgfont.getChildren();
		for (int gnr=0; gnr<glyphlist.size();gnr++) {
			Element glyph = (Element)glyphlist.get(gnr);
			if (!glyph.getName().equals("glyph")) {
				continue;
			}
			Attribute at = glyph.getAttribute("glyph-name");
			if (at == null) {
				glyph.setAttribute("ID", "notdef_xxx");
			} else {
				glyph.setAttribute("ID", "notdef_" + at.getValue());
			}
			if (!copyDattribute) {
				glyph.removeAttribute("d");
			}
			addElement(glyph,"glyph",efmfont);
		}
		
		// write to efm-file
		XMLOutputter xmlout = new XMLOutputter("   ", true,"ISO8859-1");// TODO change
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(efm), 0x8000);
		xmlout.output(efmdoc, out);
		out.close();
	}

	/**
	 * Return the SVG-Element or <code>null</code>, if not found.
	 * @param e	the Element
	 * @return	the SVG-element or <code>null</code>, if not found
	 */
	private Element scanForElement(Element e, String name) {

		if (e.getName().equals(name)) {
			return e;
		} else {
			Element svg = e.getChild(name);
			if (svg != null) {
				return svg;
			} else {
				List liste = e.getChildren();
				for (int i = 0; i < liste.size(); i++) {
					Element rt = scanForElement((Element) liste.get(i), name);
					if (rt != null) {
						return rt;
					}
				}
			}
		}
		return null;
	}

	/**
	 * remove the fileextension and path, if exists
	 */
	private String filenameWithoutExtensionAndPath(String file) {
		String rt = file;
		int i = file.lastIndexOf(".");
		if (i > 0) {
			rt = file.substring(0, i);
		}
		i = rt.lastIndexOf(File.separator);
		if (i > 0) {
			rt = rt.substring(i + 1);
		}
		return rt;
	}

	/**
	 * remove the path, if exists
	 */
	private String filenameWithoutPath(String file) {
		String rt = file;
		int i = rt.lastIndexOf(File.separator);
		if (i > 0) {
			rt = rt.substring(i + 1);
		}
		return rt;
	}
	
	/**
	 * Add a <code>Element</code> to another <code>Element</code> and 
	 * kill the parent und the namesapce.
	 *  
	 * @param e		the <code>Element</code> to add
	 * @param dest	the destination
	 * @throws IOException ... TODO change
	 */
	private void addElement(Element e, String name, Element dest) throws IOException {

		if (e == null) {
			throw new IOException("element " + name + " not found!"); //TODO change
		}
		Element copy = (Element)e.clone();
		copy.detach();
		copy.setNamespace(null);
		dest.addContent(copy);
	}
	
	/**
	 * copy the attribute 'd' from the element 'glyph'
	 */
	private boolean copyDattribute = true;
	
	
	/**
	 * @return Returns the copyDattribute.
	 */
	public boolean isCopyDattribute() {
		return copyDattribute;
	}

	/**
	 * @param copyDattribute The copyDattribute to set.
	 */
	public void setCopyDattribute(boolean copyDattribute) {
		this.copyDattribute = copyDattribute;
	}

}