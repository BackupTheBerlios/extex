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
import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.main.MainFontException;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.FileFinder;

/**
 * This class implements a efm-font.
 * It use a fontfile in efm-format.
 * 
 * TODO at the moment only one font per fontgroup
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class EFMFont extends XMLFont implements Font {

	/**
	 * The fontname
	 */
	private String name;

	/**
	 * the external fontfile
	 */
	private File externalfile;

	/**
	 * the em-size for the font
	 */
	private Dimen emsize;

	/**
	 * Creates a new object.
	 */
	public EFMFont(String name, Dimen emsize, FileFinder fileFinder) throws GeneralException, ConfigurationException {
		super();
		// trim name !
		if (name != null) {
			this.name = name.trim();
		}
		this.emsize = emsize;
		em = emsize;
		loadFont(fileFinder);
	}

	/**
	 * fontfile
	 */
	File fontfile;

	/**
	 * load the Font
	 * @throws GeneralException, if a error is thrown.
	 */
	private void loadFont(FileFinder finder) throws GeneralException, ConfigurationException {
		if (name != null) {

			fontfile = finder.findFile(name, "efm");

			if (fontfile.exists()) {

				try {

					// create a document with SAXBuilder (without validate)
					SAXBuilder builder = new SAXBuilder(false);
					Document doc = builder.build(fontfile);

					// get fontgroup
					Element fontgroup = doc.getRootElement();

					if (fontgroup == null) {
						throw new MainFontException("not fontgroup found"); // TODO change
					}

					// empr
					Attribute attr = fontgroup.getAttribute("empr");

					if (attr != null) {
						try {
							empr = attr.getFloatValue();
						} catch (Exception e) {
							empr = 100.0f;
						}
					}
					// calculate em
					em = new Dimen((long) (emsize.getValue() * empr / 100));

					// get units_per_em
					attr = fontgroup.getAttribute("units-per-em");
					if (attr != null) {
						units_per_em = attr.getIntValue();
					}

					// get ex
					attr = fontgroup.getAttribute("x-height");
					if (attr != null) {
						ex = attr.getIntValue();
					}

					// get glyph-list
					Element font = scanForElement(fontgroup, "font");
					if (font != null) {
						List glyphlist = font.getChildren("glyph");
						Element e;

						for (int i = 0; i < glyphlist.size(); i++) {
							e = (Element) glyphlist.get(i);
							String key = e.getAttributeValue("ID");
							if (key != null) {
								GlyphValues gv = new GlyphValues();
								gv.glyph_number = e.getAttributeValue("glyph-number");
								gv.glyph_name = e.getAttributeValue("glyph-name");
								gv.unicode = e.getAttributeValue("unicode");
								gv.width = e.getAttributeValue("width");
								gv.depth = e.getAttributeValue("depth");
								gv.height = e.getAttributeValue("height");
								gv.bllx = e.getAttributeValue("bllx");
								gv.blly = e.getAttributeValue("blly");
								gv.burx = e.getAttributeValue("burx");
								gv.bury = e.getAttributeValue("bury");

								// kerning
								List kerninglist = e.getChildren("kerning");
								for (int k = 0; k < kerninglist.size(); k++) {
									Element kerning = (Element) kerninglist.get(k);
									KerningValues kv = new KerningValues();
									kv.id = kerning.getAttributeValue("glyph-id");
									kv.name = kerning.getAttributeValue("glyph-name");
									kv.size = kerning.getAttributeValue("size");
									gv.kerning.put(kv.id, kv);
								}

								// ligature
								List ligaturelist = e.getChildren("ligature");
								for (int k = 0; k < ligaturelist.size(); k++) {
									Element ligature = (Element) ligaturelist.get(k);
									LigatureValues lv = new LigatureValues();
									lv.letter = ligature.getAttributeValue("letter");
									lv.letterid = ligature.getAttributeValue("letter-id");
									lv.lig = ligature.getAttributeValue("lig");
									lv.ligid = ligature.getAttributeValue("lig-id");
									gv.ligature.put(lv.letterid, lv);
								}
								glyph.put(key, gv);
								glyphname.put(gv.glyph_name, key);
							}
						}
					}

					// exernal fontfile
					String efile = font.getAttribute("filename").getValue();

					if (efile != null) {
						// externalfile = finder.findFile(efile, "pfb"); TODO change if FileFinder is okay
						externalfile = new File("./font/" + efile);
					}

				} catch (JDOMException e) {
					throw new MainFontException(e.getMessage()); // TODO change
				} catch (IOException e) {
					throw new MainFontException(e.getMessage()); // TODO change
				}

			} else {
				throw new MainFontException("font not found"); // TODO change
			}
		} else {
			throw new MainFontException("fontname not valid"); // TODO change
		}
	}

	/**
	 * The hash for the glyphs (ID)
	 */
	private HashMap glyph = new HashMap();

	/**
	 * The hash for the glyphs (name -> ID)
	 */
	private HashMap glyphname = new HashMap();

	/**
	 * Internal container for the glyph-values 
	 */
	private class GlyphValues {
		String glyph_number;
		String glyph_name;
		String unicode;
		String width;
		String bllx;
		String blly;
		String burx;
		String bury;
		String depth;
		String height;
		HashMap kerning = new HashMap();
		HashMap ligature = new HashMap();
	}

	/**
	 * container for kerning-values 
	 */
	private class KerningValues {
		String name;
		String id;
		String size;
	}

	/**
	 * container for ligature-values 
	 */
	private class LigatureValues {
		String letter;
		String letterid;
		String lig;
		String ligid;
	}

	/**
	 * Return the with of a space.
	 * 
	 * @see de.dante.extex.interpreter.type.Font#getSpace()
	 */
	public Glue getSpace() {
		// use em-size for 'space'
		Glue rt = new Glue(em);

		// glyph 'space' exists?
		String key = (String) glyphname.get("space");
		if (key != null) {
			try {
				rt = new Glue(getWidth(new UnicodeChar(Integer.parseInt(key))));
			} catch (Exception e) {
				// do nothing, use default 
			}
		}
		return rt;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getEm()
	 */
	public Dimen getEm() {
		return em;
	}

	/**
	 * ex: the height of 'x'
	 */
	private int ex = 0;

	/**
	 * the em-procent-size 
	 */
	private float empr = 100.0f;

	/**
	 * em: the width 
	 */
	private Dimen em;

	/**
	 * units per em
	 */
	private int units_per_em = 1000;

	/**
	 * @see de.dante.extex.interpreter.type.Font#getEx()
	 */
	public Dimen getEx() {
		return new Dimen(units_per_em, ex, em);
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getFontDimen(long)
	 */
	public Dimen getFontDimen(long index) {
		return null;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getFontName()
	 */
	public String getFontName() {
		return name;
	}

	public String toString() {
		return "<fontname (EFM): "
			+ getFontName()
			+ " filename "
			+ fontfile
			+ (isExternalFont() ? " (" + externalfile + ")" : "")
			+ " with size "
			+ emsize.toPT()
			+ " units_per_em = "
			+ units_per_em
			+ " ex = "
			+ ex
			+ " em = "
			+ em.toPT()
			+ " (with "
			+ empr
			+ "%)"
			+ " number of glyphs = "
			+ glyph.size()
			+ " >";
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getDepth(de.dante.util.UnicodeChar)
	 */
	public Dimen getDepth(UnicodeChar c) {
		Dimen rt = Dimen.ZERO_PT;

		GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c.getCodePoint()));
		if (gv != null) {
			try {
				int d = Integer.parseInt(gv.depth);
				rt = new Dimen(units_per_em, d, em);
			} catch (Exception e) {
				// do nothing, return ZERO_PT
			}
		}
		return rt;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getHeight(de.dante.util.UnicodeChar)
	 */
	public Dimen getHeight(UnicodeChar c) {
		Dimen rt = Dimen.ZERO_PT;

		GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c.getCodePoint()));
		if (gv != null) {
			try {
				int h = Integer.parseInt(gv.height);
				rt = new Dimen(units_per_em, h, em);
			} catch (Exception e) {
				// do nothing, return ZERO_PT
			}
		}
		return rt;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getWidth(de.dante.util.UnicodeChar)
	 */
	public Dimen getWidth(UnicodeChar c) {
		Dimen rt = Dimen.ZERO_PT;

		GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c.getCodePoint()));
		if (gv != null) {
			try {
				int w = Integer.parseInt(gv.width);
				rt = new Dimen(units_per_em, w, em);
			} catch (Exception e) {
				// do nothing, return ZERO_PT
			}
		}
		return rt;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#isDefined(de.dante.util.UnicodeChar)
	 */
	public boolean isDefined(UnicodeChar c) {
		return glyph.containsKey(String.valueOf(c.getCodePoint()));
	}

	/**
	 * Return the kerning between c1 and c2.
	 * @see de.dante.extex.interpreter.type.Font#kern(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
	 */
	public Dimen kern(UnicodeChar c1, UnicodeChar c2) {
		Dimen rt = Dimen.ZERO_PT;

		GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c1.getCodePoint()));
		if (gv != null) {
			KerningValues kv = (KerningValues) gv.kerning.get(String.valueOf(c2.getCodePoint()));
			try {
				int size = Integer.parseInt(kv.size);
				rt = new Dimen(units_per_em, size, em);
			} catch (Exception e) {
				// do nothing, use default
			}
		}
		return rt;
	}

	/**
	 * Return the ligature as <code>UnicodeChar</code>, 
	 * or <code>null</code>, if no ligature exists.
	 * 
	 * If you get a ligature-character, then you MUST call the 
	 * method <code>ligature()</code> twice, if a ligature with 
	 * more then two characters exist.
	 * (e.g. f - ff - ffl)
	 * @see de.dante.extex.interpreter.type.Font#ligature(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
	 */
	public UnicodeChar ligature(UnicodeChar c1, UnicodeChar c2) {
		UnicodeChar rt = null;

		GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c1.getCodePoint()));
		if (gv != null) {
			LigatureValues lv = (LigatureValues) gv.ligature.get(String.valueOf(c2.getCodePoint()));
			try {
				int id = Integer.parseInt(lv.ligid);
				rt = new UnicodeChar(id);
			} catch (Exception e) {
				// do nothing, use default
			}
		}
		return rt;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#isExternalFont()
	 */
	public boolean isExternalFont() {
		return externalfile == null ? false : true;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getExternalFile()
	 */
	public File getExternalFile() {
		return externalfile;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#getExternalID()
	 */
	public String getExternalID(UnicodeChar c) {
		String rt = null;
		if (externalfile != null) {
			GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c.getCodePoint()));
			if (gv != null) {
				rt = gv.glyph_number;
			}
		}
		return rt;
	}
}
