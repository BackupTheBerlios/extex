/*
 * Copyright (C) 2004  Michael Niedermair
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
package de.dante.tex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

import de.dante.extex.ExTeX;

/*
 * Test for ExTeX.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TestTeX {

	/**
	 * Run ExTeX with a special File and compare the output with a output-test-file.
	 * @param texfile		the tx-file
	 * @param outfile		the output-test-file
	 * @return <code>true</code>, if the two files are equals, otherwise <code>false</code>.
	 */
	public static boolean test(String texfile, String outfile) {

		boolean test = true;

		try {

			// run ExTeX
			Properties pro = System.getProperties();
			ExTeX extex = new ExTeX(pro);
			pro.setProperty("extex.output", "txt");
			pro.setProperty("extex.file", texfile);
			pro.setProperty("extex.jobname", texfile);
			extex.run();

			// compare
			BufferedReader intxt = new BufferedReader(new FileReader(texfile + ".txt"));
			BufferedReader intesttxt = new BufferedReader(new FileReader(outfile));

			String linetxt, linetesttxt;
			while ((linetxt = intxt.readLine()) != null) {
				linetesttxt = intesttxt.readLine();
				if (!linetxt.equals(linetesttxt)) {
					test = false;
					break;
				}
			}
			intxt.close();
			intesttxt.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			// e.printStackTrace();
			test = false;
		}
		return test;
	}
}
