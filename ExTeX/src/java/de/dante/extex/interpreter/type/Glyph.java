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
package de.dante.extex.interpreter.type;

/**
 * Glyph
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class Glyph {

	/**
	 * the width of the glyph
	 */
	private Dimen width = Dimen.ZERO_PT;

	/**
	 * the height of the glyph
	 */
	private Dimen height = Dimen.ZERO_PT;

	/**
	 * the depth of the glyph
	 */
	private Dimen depth = Dimen.ZERO_PT;

	/**
	 * the italic of the glyph
	 */
	private float italic = 0.0f;

	/**
	 * Create a new object.
	 */
	public Glyph() {

	}

	/**
	 * Create a new object.
	 */
	public Glyph(Dimen height, Dimen depth, Dimen width, float italic) {
		this.height = height;
		this.depth = depth;
		this.width = width;
		this.italic = italic;
	}

	/**
	 * @return Returns the depth.
	 */
	public Dimen getDepth() {
		return depth;
	}

	/**
	 * @param depth The depth to set.
	 */
	public void setDepth(Dimen depth) {
		this.depth = depth;
	}

	/**
	 * @return Returns the height.
	 */
	public Dimen getHeight() {
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(Dimen height) {
		this.height = height;
	}

	/**
	 * @return Returns the italic.
	 */
	public float getItalic() {
		return italic;
	}

	/**
	 * @param italic The italic to set.
	 */
	public void setItalic(float italic) {
		this.italic = italic;
	}

	/**
	 * @return Returns the width.
	 */
	public Dimen getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(Dimen width) {
		this.width = width;
	}
}