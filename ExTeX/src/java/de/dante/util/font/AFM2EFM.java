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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * This class implements a converter from AFM to EFM.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
// TODO incomplete
public class AFM2EFM {

	private String pfbname;
	private String efmname;
	private String defaultsize;
	
	/**
	 * init
	 * @param afmin		Stream for Reading the afm-file
	 * @param pfbname	the name of the pfbfile
	 * @param efmname	the name of the efmfile
	 */
	public AFM2EFM(BufferedInputStream afmin, String pfbname, String efmname, String defaultsize) {

		this.pfbname = pfbname;
		this.efmname = efmname;
		this.defaultsize =defaultsize;
		
		try {

			// create a Reader (AFM use US_ASCII)
			BufferedReader reader = new BufferedReader(new InputStreamReader(afmin, "US-ASCII"));

			// read the File
			readAFMFile(reader);

			// close the reader
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * The Postscript font name.
	 */
	private String afmFontName = "";

	/**
	 * The full name of the font.
	 */
	private String afmFullName = "";

	/**
	 * The family name of the font.
	 */
	private String afmFamilyName = "";

	/**
	 * The weight of the font: normal, bold, etc.
	 */
	private String afmWeight = "";

	/**
	 * The italic angle of the font, usually 0.0 or negative.
	 */
	private float afmItalicAngle = 0.0f;

	/**
	 * \code{true} if all the characters have the same width.
	 */
	private boolean afmIsFixedPitch = false;

	/**
	 * The character set of the font.
	 */
	private String afmCharacterSet = "";

	/**
	 * The llx of the FontBox.
	 */
	private int afmllx = -9999;

	/**
	 * The lly of the FontBox.
	 */
	private int afmlly = -9999;

	/**
	 * The lurx of the FontBox.
	 */
	private int afmurx = -9999;

	/**
	 * The ury of the FontBox.
	 */
	private int afmury = -9999;

	/**
	 * The underline position.
	 */
	private int afmUnderlinePosition = 0;

	/**
	 * The underline thickness.
	 */
	private int afmUnderlineThickness = 0;

	/**
	 * The font's encoding name. 
	 * This encoding is
	 * - StandardEncoding
	 * - AdobeStandardEncoding
	 * - For all other names the font is treated as symbolic.
	?	 */
	private String afmEncodingScheme = "FontSpecific";

	/**
	 * CapHeight
	 */
	private int afmCapHeight = 700;

	/**
	 * XHeight
	 */
	private int afmXHeight = 480;

	/**
	 * Ascender
	 */
	private int afmAscender = 0;

	/**
	 * Descender
	 */
	private int afmDescender = 0;

	/**
	 * StdHW
	 */
	private int afmStdHW;

	/**
	 * StdVW
	 */
	private int afmStdVW = 0;

	/**
	 * Represents the section CharMetrics in the AFM file. 
	 */
	private ArrayList afmCharMetrics = new ArrayList(256);

	/**
	 * Represents the section KerningPairs in the AFM file.
	 */
	private ArrayList afmKerningPairs = new ArrayList(256);

	/**
	 * Char-Name - Char-Number
	 */
	private HashMap afmCharNameNumber = new HashMap(256);

	/**
	 * Read the AFM-File 
	 * @param reader	the Reader fore the fileinput
	 * @throws IOException	if an io-error are throws
	 */
	private void readAFMFile(BufferedReader reader) throws IOException {

		// line
		String line;

		//read first the AFM-header and then the metrics
		boolean isMetrics = false;

		while ((line = reader.readLine()) != null) {

			// get the token from the line
			StringTokenizer tok = new StringTokenizer(line);

			// no more tokens
			if (!tok.hasMoreTokens()) {
				continue;
			}

			// read the command
			String command = tok.nextToken();

			// check the command
			if (command.equals("Comment")) {
				continue;
			} else if (command.equals("Notice")) {
				continue;
			} else if (command.equals("FontName")) {
				afmFontName = tok.nextToken("\u00ff").substring(1);
			} else if (command.equals("FullName")) {
				afmFullName = tok.nextToken("\u00ff").substring(1);
			} else if (command.equals("FamilyName")) {
				afmFamilyName = tok.nextToken("\u00ff").substring(1);
			} else if (command.equals("Weight")) {
				afmWeight = tok.nextToken("\u00ff").substring(1);
			} else if (command.equals("ItalicAngle")) {
				afmItalicAngle = Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("IsFixedPitch")) {
				afmIsFixedPitch = tok.nextToken().equals("true");
			} else if (command.equals("CharacterSet")) {
				afmCharacterSet = tok.nextToken("\u00ff").substring(1);
			} else if (command.equals("FontBBox")) {
				afmllx = (int) Float.valueOf(tok.nextToken()).floatValue();
				afmlly = (int) Float.valueOf(tok.nextToken()).floatValue();
				afmurx = (int) Float.valueOf(tok.nextToken()).floatValue();
				afmury = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("UnderlinePosition")) {
				afmUnderlinePosition = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("UnderlineThickness")) {
				afmUnderlineThickness = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("EncodingScheme")) {
				afmEncodingScheme = tok.nextToken("\u00ff").substring(1);
			} else if (command.equals("CapHeight")) {
				afmCapHeight = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("XHeight")) {
				afmXHeight = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("Ascender")) {
				afmAscender = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("Descender")) {
				afmDescender = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("StdHW")) {
				afmStdHW = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("StdVW")) {
				afmStdVW = (int) Float.valueOf(tok.nextToken()).floatValue();
			} else if (command.equals("StartCharMetrics")) {
				isMetrics = true;
				break;
			}
		}
		// metric not found
		if (!isMetrics) {
			System.err.println("Missing StartCharMetrics");
			System.exit(1);
		}
		// read the metric
		while ((line = reader.readLine()) != null) {

			// get the token from the line
			StringTokenizer tok = new StringTokenizer(line);

			// no more tokens
			if (!tok.hasMoreTokens()) {
				continue;
			}

			// get the command
			String command = tok.nextToken();
			if (command.equals("EndCharMetrics")) {
				isMetrics = false;
				break;
			}

			// default values
			AFMCharMetric cm = new AFMCharMetric();

			// get the token separate bei ';'
			tok = new StringTokenizer(line, ";");
			while (tok.hasMoreTokens()) {
				StringTokenizer tokc = new StringTokenizer(tok.nextToken());
				if (!tokc.hasMoreTokens()) {
					continue;
				}
				command = tokc.nextToken();

				// command ?
				if (command.equals("C")) {
					cm.C = Integer.parseInt(tokc.nextToken());
				} else if (command.equals("CH")) {
					cm.C = Integer.parseInt(tokc.nextToken(), 16);
				} else if (command.equals("WX")) {
					cm.WX = Float.valueOf(tokc.nextToken()).intValue();
				} else if (command.equals("N")) {
					cm.N = tokc.nextToken();
				} else if (command.equals("B")) {
					cm.Bllx = (int) Float.valueOf(tokc.nextToken()).floatValue();
					cm.Blly = (int) Float.valueOf(tokc.nextToken()).floatValue();
					cm.Burx = (int) Float.valueOf(tokc.nextToken()).floatValue();
					cm.Bury = (int) Float.valueOf(tokc.nextToken()).floatValue();
				} else if (command.equals("L")) {
					cm.addL(tokc.nextToken().trim(), tokc.nextToken().trim());
				}
			}
			afmCharMetrics.add(cm);

			// store name and number 
			if (afmCharNameNumber.containsKey(cm.N)) {
				if (cm.C != -1) {
					afmCharNameNumber.put(cm.N, new Integer(cm.C));
				}
			} else {
				afmCharNameNumber.put(cm.N, new Integer(cm.C));
			}
		}

		// metric close?
		if (isMetrics) {
			System.err.println("Missing EndCharMetrics");
			System.exit(1);
		}

		// read  next command
		while ((line = reader.readLine()) != null) {
			StringTokenizer tok = new StringTokenizer(line);
			if (!tok.hasMoreTokens())
				continue;
			String ident = tok.nextToken();
			if (ident.equals("EndFontMetrics")) {
				// end
				return;
			}
			if (ident.equals("StartKernPairs")) {
				isMetrics = true;
				break;
			}
		}
		if (!isMetrics) {
			System.err.println("Missing EndFontMetrics");
			System.exit(1);
		}

		// read KernPairs
		while ((line = reader.readLine()) != null) {
			StringTokenizer tok = new StringTokenizer(line);
			if (!tok.hasMoreTokens())
				continue;
			String ident = tok.nextToken();
			if (ident.equals("KPX")) {
				KernPairs kp = new KernPairs();
				kp.charpre = tok.nextToken();
				kp.charpost = tok.nextToken();
				kp.kerningsize = tok.nextToken();
				afmKerningPairs.add(kp);

			} else if (ident.equals("EndKernPairs")) {
				isMetrics = false;
				break;
			}
		}
		if (isMetrics) {
			System.err.println("Missing EndKernPairs");
			System.exit(1);
		}
	}

	/**
	 * container for KerningPair
	 */
	private class KernPairs {
		String charpre;
		String charpost;
		String kerningsize;
	}

	/**
	 * Container for the AFM-CharMetrik
	 */
	private class AFMCharMetric {

		/**
		 * C
		 */
		int C = -1;

		/**
		 * WX
		 */
		int WX = -9999;

		/**
		 * Name
		 */
		String N = "";

		/**
		 * B
		 */
		int Bllx = -9999;
		int Blly = -9999;
		int Burx = -9999;
		int Bury = -9999;

		/**
		 * Ligatur
		 */
		HashMap L = null;

		/**
		 * Add a ligature
		 * @param letter	the basic letter
		 * @param lig		the ligature
		 */
		public void addL(String letter, String lig) {
			if (L == null) {
				L = new HashMap();
			}
			L.put(letter, lig);
		}

		/**
		 * print
		 */
		public String toString() {
			StringBuffer buf = new StringBuffer(100);

			buf.append("   C          : " + C + LF);
			buf.append("   WX         : " + WX + LF);
			buf.append("   N          : " + N + LF);
			buf.append("   B          : " + Bllx + " " + Blly + " " + Burx + " " + Bury + LF);

			if (L != null) {
				Iterator iterator = L.keySet().iterator();

				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					buf.append("   L          : " + key + "  " + L.get(key) + LF);
				}
			}
			return buf.toString();
		}
	}

	/**
	 * LineFeed
	 */
	private char LF = '\n';

	/**
	 * The \code{String} for the class.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer(1024);

		buf.append("FontName            : " + afmFontName + LF);
		buf.append("Fullname            : " + afmFullName + LF);
		buf.append("FamilyName          : " + afmFamilyName + LF);
		buf.append("CharacterSet        : " + afmCharacterSet + LF);
		buf.append("Weight              : " + afmWeight + LF);
		buf.append("ItalicAngle         : " + afmItalicAngle + LF);
		buf.append("IsFixedPitch        : " + afmIsFixedPitch + LF);
		buf.append("llx                 : " + afmllx + LF);
		buf.append("lly                 : " + afmlly + LF);
		buf.append("urx                 : " + afmurx + LF);
		buf.append("ury                 : " + afmury + LF);
		buf.append("UnderlinePosition   : " + afmUnderlinePosition + LF);
		buf.append("UnderlineThickness  : " + afmUnderlineThickness + LF);
		buf.append("EncodingScheme      : " + afmEncodingScheme + LF);
		buf.append("CapHeight           : " + afmCapHeight + LF);
		buf.append("XHeight             : " + afmXHeight + LF);
		buf.append("Ascender            : " + afmAscender + LF);
		buf.append("Descender           : " + afmDescender + LF);
		buf.append("StdHW               : " + afmStdHW + LF);
		buf.append("StdVW               : " + afmStdVW + LF);

		for (int i = 0; i < afmCharMetrics.size(); i++) {
			buf.append("CharMetric" + LF + afmCharMetrics.get(i));
		}

		return buf.toString();
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
	 * Create a EFM-File
	 */
	public void createEFMFile() {

		try {

			// create efm-file
			Element root = new Element("fontgroup");
			root.setAttribute("name", filenameWithoutExtensionAndPath(efmname));
			root.setAttribute("id", filenameWithoutExtensionAndPath(efmname));
			root.setAttribute("default-size", defaultsize);
			root.setAttribute("empr", "100");

			Document doc = new Document();
			doc.addContent(new Comment(" ExTeX-Font-Metric-File (EFM) "));
			// TODO add DTD
			doc.setRootElement(root);

			Element font = new Element("font");
			font.setAttribute("type", "type1");
			font.setAttribute("filename", filenameWithoutPath(pfbname));
			root.addContent(font);

			font.addContent(new Comment(" type: the source-type of the metric-file e.g. Type1 AFM "));
			font.addContent(new Comment(" filename : the filename of the pfb-file "));

			font.setAttribute("font-name", afmFontName);
			font.setAttribute("font-fullname", afmFullName);
			font.setAttribute("font-family", afmFamilyName);
			font.setAttribute("font-weight", afmWeight);

			root.setAttribute("units-per-em", "1000");
			root.setAttribute(
				"bbox",
				String.valueOf(afmllx) + ' ' + String.valueOf(afmlly) + ' ' + String.valueOf(afmurx) + ' ' + String.valueOf(afmury));
			if (afmUnderlineThickness != 0) {
				font.setAttribute("underline-position", String.valueOf(afmUnderlinePosition));
				font.setAttribute("underline-thickness", String.valueOf(afmUnderlineThickness));
			}
			root.setAttribute("x-height", String.valueOf(afmXHeight));
			root.setAttribute("cap-height", String.valueOf(afmCapHeight));

			// ???
			//fontface.setAttribute("characterset", afmCharacterSet);
			//fontface.setAttribute("italicangle", String.valueOf(afmItalicAngle));
			//fontface.setAttribute("isfixedpitch", String.valueOf(afmIsFixedPitch));
			//fontface.setAttribute("encodingscheme", String.valueOf(afmEncodingScheme));

			for (int i = 0; i < afmCharMetrics.size(); i++) {

				// create  glyph
				Element glyph = new Element("glyph");

				// get the AFMCharMertix-object
				AFMCharMetric cm = (AFMCharMetric) afmCharMetrics.get(i);

				// create attributes
				if (cm.C >= 0) {
					glyph.setAttribute("ID", String.valueOf(cm.C));
				} else {
					glyph.setAttribute("ID", "notdef_" + cm.N);
				}
				glyph.setAttribute("glyph-number", String.valueOf(cm.C));
				glyph.setAttribute("glyph-name", cm.N);
				glyph.setAttribute("unicode", cm.N);

				if (cm.WX != -9999) {
					glyph.setAttribute("width", String.valueOf(cm.WX));
				} else {
					// calculate with from bbox
					if (cm.Bllx != -9999) {
						glyph.setAttribute("width", String.valueOf(cm.Bllx + cm.Burx));
					}
				}

				if (cm.Bllx != -9999) {
					glyph.setAttribute("bllx", String.valueOf(cm.Bllx));
					glyph.setAttribute("blly", String.valueOf(cm.Blly));
					glyph.setAttribute("burx", String.valueOf(cm.Burx));
					glyph.setAttribute("bury", String.valueOf(cm.Bury));

					if (cm.Blly < 0) {
						glyph.setAttribute("depth", String.valueOf(-cm.Blly));
					} else {
						glyph.setAttribute("depth", "0");
					}
					if (cm.Bury > 0) {
						glyph.setAttribute("height", String.valueOf(cm.Bury));
					} else {
						glyph.setAttribute("height", "0");
					}
					
					// kerning
					addKerning(glyph);
				}

				if (cm.L != null) {
	
					Iterator iterator = cm.L.keySet().iterator();

					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						Element lig = new Element("ligature");
						lig.setAttribute("letter", key);
						lig.setAttribute("letter-id", getIDforName(key));
						String value = (String) cm.L.get(key);
						lig.setAttribute("lig", value);
						lig.setAttribute("lig-id", getIDforName(value));
						glyph.addContent(lig);
					}
				}

				// add to fontseg
				font.addContent(glyph);

			}

			// write to efm-file
			XMLOutputter xmlout = new XMLOutputter("   ", true);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(efmname), 0x8000);
			xmlout.output(doc, out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Add the kerning-pais for the glyph
	 */
	private void addKerning(Element glyph) {

		String glyphname = glyph.getAttributeValue("glyph-name");
		KernPairs kp;
		
		for (int i=0; i<afmKerningPairs.size(); i++) {
			kp = (KernPairs)afmKerningPairs.get(i);
			if (kp.charpre.equals(glyphname)) {
				Element kerning = new Element("kerning");

				kerning.setAttribute("glyph-name", kp.charpost);
				kerning.setAttribute("glyph-id", getIDforName(kp.charpost));
				kerning.setAttribute("size", kp.kerningsize);
				
				glyph.addContent(kerning);
			}
		}
	}
	
	/**
	 * Return the id for a charname 
	 */
	private String getIDforName(String name) {
		int id = -1;

		if (name != null) {
			Integer i =(Integer) afmCharNameNumber.get(name);
			if (i != null) {
				id = i.intValue();
			}
		} else {
			name = "null";
		}

		if (id >= 0) {
			return String.valueOf(id);
		} else {
			return "notdef_" + name;
		}
	}

	/**
	 * only for test
	 */
	public static void main(String[] args) {

		if (args.length != 4) {
			System.err.println("java de.dante.util.font.afm.AFM2EFM <afm-file> <pfb-file> <efm-file> <default-size>");
			System.exit(1);
		}

		try {

			System.err.println("open " + args[0]);

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(args[0]));

			AFM2EFM afm = new AFM2EFM(in, args[1], args[2], args[3]);

			in.close();

			// System.err.println(afm);

			afm.createEFMFile();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
}