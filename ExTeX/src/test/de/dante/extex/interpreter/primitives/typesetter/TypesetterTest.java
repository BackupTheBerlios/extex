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

package de.dante.extex.interpreter.primitives.typesetter;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the typesetter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TypesetterTest extends ExTeXLauncher {

    /**
     * Constructor for MathaccentTest.
     *
     * @param arg the name
     */
    public TypesetterTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \accent does not work in math mode.
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        Properties properties = getProps();
        properties.setProperty("extex.typesetter", "devel");
        properties.setProperty("extex.output", "xml");

        runCode(properties,
                //--- input code ---
                "\\catcode`{=1\n" //
                + "\\catcode`}=2\n" //
                + "\\font\\fnt=cmtt12\\fnt\n" //
                + "\\hsize=1000pt\n" //
                + "\\vsize=600pt\n" //
                + "\n" //
                + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam\n" //
                + "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat\n" //
                + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation\n" //
                + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n" //
                + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse\n" //
                + "molestie consequat, vel illum dolore eu feugiat nulla facilisis at\n" //
                + "vero eros et accumsan et iusto odio dignissim qui blandit praesent\n" //
                + "luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n" //
                + "Nam liber tempor cum soluta nobis eleifend option congue nihil\n" //
                + "imperdiet doming id quod mazim placerat facer possim assum. Typi non\n" //
                + "habent claritatem insitam; est usus legentis in iis qui facit eorum\n" //
                + "claritatem. Investigationes demonstraverunt lectores legere me lius\n" //
                + "quod ii legunt saepius. Claritas est etiam processus dynamicus, qui\n" //
                + "sequitur mutationem consuetudium lectorum. Mirum est notare quam\n" //
                + "littera gothica, quam nunc putamus parum claram, anteposuerit\n" //
                + "litterarum formas humanitatis per seacula quarta decima et quinta\n" //
                + "decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant\n" //
                + "sollemnes in futurum. \n" //
                + "\n" //
                + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam\n" //
                + "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat\n" //
                + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation\n" //
                + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n" //
                + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse\n" //
                + "molestie consequat, vel illum dolore eu feugiat nulla facilisis at\n" //
                + "vero eros et accumsan et iusto odio dignissim qui blandit praesent\n" //
                + "luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n" //
                + "Nam liber tempor cum soluta nobis eleifend option congue nihil\n" //
                + "imperdiet doming id quod mazim placerat facer possim assum. Typi non\n" //
                + "habent claritatem insitam; est usus legentis in iis qui facit eorum\n" //
                + "claritatem. Investigationes demonstraverunt lectores legere me lius\n" //
                + "quod ii legunt saepius. Claritas est etiam processus dynamicus, qui\n" //
                + "sequitur mutationem consuetudium lectorum. Mirum est notare quam\n" //
                + "littera gothica, quam nunc putamus parum claram, anteposuerit\n" //
                + "litterarum formas humanitatis per seacula quarta decima et quinta\n" //
                + "decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant\n" //
                + "sollemnes in futurum. \n" //
                + "\n" //
                + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam\n" //
                + "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat\n" //
                + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation\n" //
                + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n" //
                + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse\n" //
                + "molestie consequat, vel illum dolore eu feugiat nulla facilisis at\n" //
                + "vero eros et accumsan et iusto odio dignissim qui blandit praesent\n" //
                + "luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n" //
                + "Nam liber tempor cum soluta nobis eleifend option congue nihil\n" //
                + "imperdiet doming id quod mazim placerat facer possim assum. Typi non\n" //
                + "habent claritatem insitam; est usus legentis in iis qui facit eorum\n" //
                + "claritatem. Investigationes demonstraverunt lectores legere me lius\n" //
                + "quod ii legunt saepius. Claritas est etiam processus dynamicus, qui\n" //
                + "sequitur mutationem consuetudium lectorum. Mirum est notare quam\n" //
                + "littera gothica, quam nunc putamus parum claram, anteposuerit\n" //
                + "litterarum formas humanitatis per seacula quarta decima et quinta\n" //
                + "decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant\n" //
                + "sollemnes in futurum. \n" //
                + "\n" //
                + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam\n" //
                + "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat\n" //
                + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation\n" //
                + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n" //
                + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse\n" //
                + "molestie consequat, vel illum dolore eu feugiat nulla facilisis at\n" //
                + "vero eros et accumsan et iusto odio dignissim qui blandit praesent\n" //
                + "luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n" //
                + "Nam liber tempor cum soluta nobis eleifend option congue nihil\n" //
                + "imperdiet doming id quod mazim placerat facer possim assum. Typi non\n" //
                + "habent claritatem insitam; est usus legentis in iis qui facit eorum\n" //
                + "claritatem. Investigationes demonstraverunt lectores legere me lius\n" //
                + "quod ii legunt saepius. Claritas est etiam processus dynamicus, qui\n" //
                + "sequitur mutationem consuetudium lectorum. Mirum est notare quam\n" //
                + "littera gothica, quam nunc putamus parum claram, anteposuerit\n" //
                + "litterarum formas humanitatis per seacula quarta decima et quinta\n" //
                + "decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant\n" //
                + "sollemnes in futurum. \n" //
                + "\n" //
                + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam\n" //
                + "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat\n" //
                + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation\n" //
                + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n" //
                + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse\n" //
                + "molestie consequat, vel illum dolore eu feugiat nulla facilisis at\n" //
                + "vero eros et accumsan et iusto odio dignissim qui blandit praesent\n" //
                + "luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n" //
                + "Nam liber tempor cum soluta nobis eleifend option congue nihil\n" //
                + "imperdiet doming id quod mazim placerat facer possim assum. Typi non\n" //
                + "habent claritatem insitam; est usus legentis in iis qui facit eorum\n" //
                + "claritatem. Investigationes demonstraverunt lectores legere me lius\n" //
                + "quod ii legunt saepius. Claritas est etiam processus dynamicus, qui\n" //
                + "sequitur mutationem consuetudium lectorum. Mirum est notare quam\n" //
                + "littera gothica, quam nunc putamus parum claram, anteposuerit\n" //
                + "litterarum formas humanitatis per seacula quarta decima et quinta\n" //
                + "decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant\n" //
                + "sollemnes in futurum. \n" //
                + "\n" //
                + "\\end\n",
                //--- log message ---
                "",
                //--- output channel ---
                "?");
    }

}
