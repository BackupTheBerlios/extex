/*
 * Copyright (C) 2004  Gerd Neugebauer
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
package de.dante.test;

import java.util.Properties;

import de.dante.extex.ExTeX;


public class ExTeXLauncher {

    private Properties properties = new Properties();
    
    public ExTeXLauncher(final String profile) {
        super();
        if (profile.indexOf('*') >= 0) {
            properties.setProperty("extex.traceTokenizer", "true");
        }
        properties.setProperty("extex.logger",
                               "de.dante.extex.logging.BufferLogger");
//        BufferLogger.setProfile(profile);
    }

    public String run(final String code) throws Exception {
        ExTeX main = new ExTeX(properties);
        properties.setProperty("extex.code",code);
        main.run();
//        return BufferLogger.close();
        return null; //TODO
    }
}