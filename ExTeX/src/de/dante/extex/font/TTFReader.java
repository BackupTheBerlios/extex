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
package de.dante.extex.font;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.batik.svggen.font.table.CmapFormat;
import org.apache.batik.svggen.font.table.CmapTable;
import org.apache.batik.svggen.font.table.FeatureTags;
import org.apache.batik.svggen.font.table.GlyfTable;
import org.apache.batik.svggen.font.table.GlyphDescription;
import org.apache.batik.svggen.font.table.HeadTable;
import org.apache.batik.svggen.font.table.HheaTable;
import org.apache.batik.svggen.font.table.HmtxTable;
import org.apache.batik.svggen.font.table.KernSubtable;
import org.apache.batik.svggen.font.table.KernTable;
import org.apache.batik.svggen.font.table.LocaTable;
import org.apache.batik.svggen.font.table.MaxpTable;
import org.apache.batik.svggen.font.table.NameTable;
import org.apache.batik.svggen.font.table.Os2Table;
import org.apache.batik.svggen.font.table.PostTable;
import org.apache.batik.svggen.font.table.ScriptTags;
import org.apache.batik.svggen.font.table.Table;
import org.apache.batik.svggen.font.table.TableDirectory;
import org.apache.batik.svggen.font.table.TableFactory;
import org.jdom.Element;

/**
 * This class read a TTF-file.
 * <p>
 * It use the ttf-font-code from the batik-project
 * <p>
 * For more information use <tt>TrueType 1.0 Font Files</tt>
 * Technical Specification, Revision 1.66, November 1995
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TTFReader implements FontMetric, ScriptTags, FeatureTags {

	/**
	 * init
	 * @param file		<code>File</code> for reading
	 */
	public TTFReader(File file) throws TTFException, IOException {

		fontid = file.getName().replaceAll(".ttf", "");

		in = new RandomAccessFile(file, "r");
		readTTF();
		in.close();

		createEfmElement();
	}

	/**
	 * fontid
	 */
	private String fontid;

	/**
	 * Create the efm-element
	 */
	private void createEfmElement() throws TTFException {

		efmelement = new Element("fontgroup");

		String fontFamily = name.getRecord(Table.nameFontFamilyName);
		short unitsPerEm = head.getUnitsPerEm();
		//String panose = os2.getPanose().toString();
		//short ascent = hhea.getAscender();
		//short descent = hhea.getDescender();
		//int baseline = 0; // bit 0 of head.flags will indicate if this is true

		efmelement.setAttribute("name", fontFamily);
		efmelement.setAttribute("id", fontid);
		efmelement.setAttribute("default-size", "12");
		efmelement.setAttribute("empr", "100");
		efmelement.setAttribute("units-per-em", String.valueOf(unitsPerEm));

		//		int horiz_advance_x = font.getOS2Table().getAvgCharWidth();

		Element font = new Element("font");
		efmelement.addContent(font);

		font.setAttribute("font-name", fontid);
		font.setAttribute("font-family", fontFamily);
		font.setAttribute("type", "ttf");

		// Decide upon a cmap table to use for our character to glyph look-up
		CmapFormat cmapFmt = null;
		// The default behaviour is to use the Unicode cmap encoding
		cmapFmt = cmap.getCmapFormat(Table.platformMicrosoft, Table.encodingUGL);
		if (cmapFmt == null) {
			// This might be a symbol font, so we'll look for an "undefined" encoding
			cmapFmt = cmap.getCmapFormat(Table.platformMicrosoft, Table.encodingUndefined);
		}
		if (cmapFmt == null) {
			throw new TTFException("Cannot find a suitable cmap table");
		}

		// Output kerning pairs from the requested range
		KernTable kern = (KernTable) getTable(Table.kern);
		ArrayList kernlist = new ArrayList();
		if (kern != null) {
			KernSubtable kst = kern.getSubtable(0);
			PostTable post = (PostTable) getTable(Table.post);
			for (int i = 0; i < kst.getKerningPairCount(); i++) {

				KernPairs kp = new KernPairs();
				kp.idpre = kst.getKerningPair(i).getLeft();
				kp.charpre = post.getGlyphName(kst.getKerningPair(i).getLeft());
				kp.idpost = kst.getKerningPair(i).getRight();
				kp.charpost = post.getGlyphName(kst.getKerningPair(i).getRight());
				// SVG kerning values are inverted from TrueType's.
				kp.size = -kst.getKerningPair(i).getValue();
				kernlist.add(kp);
			}
		}

		// hash   glyph-number <-> id
		HashMap glyphnumber = new HashMap();
		for (int i = 0; i <= 0xffff; i++) { // TODO range okay???
			int glyphIndex = cmapFmt.mapCharCode(i);
			if (glyphIndex > 0) {
				if (!glyphnumber.containsKey(String.valueOf(glyphIndex))) {
					glyphnumber.put(String.valueOf(glyphIndex), new Integer(i));
				}
			}
		}

		// Include our requested range
		for (int i = 0; i <= 0xffff; i++) { // TODO range okay???
			int glyphIndex = cmapFmt.mapCharCode(i);
			//        ps.println(String.valueOf(i) + " -> " + String.valueOf(glyphIndex));
			//      if (font.getGlyphs()[glyphIndex] != null)
			//        sb.append(font.getGlyphs()[glyphIndex].toString() + "\n");

			if (glyphIndex > 0) {

				Element glyph = new Element("glyph");
				font.addContent(glyph);

				glyph.setAttribute("ID", String.valueOf(i));
				glyph.setAttribute("glyph-number", String.valueOf(glyphIndex));
				glyph.setAttribute("glyph-name", post.getGlyphName(glyphIndex));

				GlyphDescription gd = glyf.getDescription(glyphIndex);

				if (gd != null) {
					glyph.setAttribute("width", String.valueOf(gd.getXMinimum() + gd.getXMaximum()));
					if (gd.getYMinimum() < 0) {
						glyph.setAttribute("depth", String.valueOf(-gd.getYMinimum()));
					} else {
						glyph.setAttribute("depth", "0");
					}
					if (gd.getYMaximum() > 0) {
						glyph.setAttribute("height", String.valueOf(gd.getYMaximum()));
					} else {
						glyph.setAttribute("height", "0");
					}
				}

				// kerning
				for (int k = 0; k < kernlist.size(); k++) {
					KernPairs kp = (KernPairs) kernlist.get(k);
					if (kp.idpre == glyphIndex) {
						Element kerning = new Element("kerning");
						glyph.addContent(kerning);
						int id = ((Integer) glyphnumber.get(String.valueOf(kp.idpost))).intValue();
						kerning.setAttribute("glyph-id", String.valueOf(id));
						//kerning.setAttribute("glyph-number", String.valueOf(kp.idpost));
						kerning.setAttribute("glyph-name", kp.charpost);
						kerning.setAttribute("size", String.valueOf(kp.size));
					}
				}
			}

		}

	}

	/**
	 * container for KerningPair
	 */
	private class KernPairs {
		int idpre;
		String charpre;
		int idpost;
		String charpost;
		int size;
	}

	/**
	 * RandomAccessFile for reading
	 */
	private RandomAccessFile in;

	/**
	 * all tables in the ttf-file
	 */
	private Table[] tables;

	/**
	 * TableDirectory
	 */
	private TableDirectory tableDirectory = null;

	/**
	 * OS/2 and Windows specific metrics
	 */
	private Os2Table os2;

	/**
	 * character tp glyph mapping
	 */
	private CmapTable cmap;

	/**
	 * glyph data
	 */
	private GlyfTable glyf;

	/**
	 * font header 
	 */
	private HeadTable head;

	/**
	 * horizontal header
	 */
	private HheaTable hhea;

	/**
	 * horizonzal metrics
	 */
	private HmtxTable hmtx;

	/**
	 * index to location
	 */
	private LocaTable loca;

	/**
	 * maximum profile
	 */
	private MaxpTable maxp;

	/**
	 * naming table
	 */
	private NameTable name;

	/**
	 * PostScript information
	 * <p>
	 * The <code>PostTable</code> do not return the italicangle.
	 */
	private PostTable post;

	/**
	 * read the ttf-file
	 */
	private void readTTF() throws TTFException, IOException {

		tableDirectory = new TableDirectory(in);
		tables = new Table[tableDirectory.getNumTables()];

		// load each of the tables
		for (int i = 0; i < tableDirectory.getNumTables(); i++) {
			tables[i] = TableFactory.create(tableDirectory.getEntry(i), in);
		}

		// Get references to commonly used tables
		os2 = (Os2Table) getTable(Table.OS_2);
		cmap = (CmapTable) getTable(Table.cmap);
		glyf = (GlyfTable) getTable(Table.glyf);
		head = (HeadTable) getTable(Table.head);
		hhea = (HheaTable) getTable(Table.hhea);
		hmtx = (HmtxTable) getTable(Table.hmtx);
		loca = (LocaTable) getTable(Table.loca);
		maxp = (MaxpTable) getTable(Table.maxp);
		name = (NameTable) getTable(Table.name);
		post = (PostTable) getTable(Table.post);

		
		
		
		// Initialize the tables that require it
		hmtx.init(hhea.getNumberOfHMetrics(), maxp.getNumGlyphs() - hhea.getNumberOfHMetrics());
		loca.init(maxp.getNumGlyphs(), head.getIndexToLocFormat() == 0);
		glyf.init(maxp.getNumGlyphs(), loca);

	}

	/**
	 * Return the table with the spezial tabletype
	 */
	private Table getTable(int tableType) {
		for (int i = 0; i < tables.length; i++) {
			if ((tables[i] != null) && (tables[i].getType() == tableType)) {
				return tables[i];
			}
		}
		return null;
	}

	/**
	 * efm-element
	 */
	Element efmelement;

	/**
	 * @see de.dante.extex.font.FontMetric#getFontMetric()
	 */
	public Element getFontMetric() {
		return efmelement;
	}
}
