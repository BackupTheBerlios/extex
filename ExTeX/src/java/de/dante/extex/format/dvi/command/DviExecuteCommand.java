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

package de.dante.extex.format.dvi.command;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.format.dvi.exception.DviException;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Interface for a DVI command to execute.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public interface DviExecuteCommand {

    /**
     * execute DviChar
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviChar command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviBOP
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviBOP command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviDown
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviDown command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviEOP
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviEOP command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviFntDef
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviFntDef command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviFntNum
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviFntNum command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviPOP
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviPOP command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviNOP
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviNOP command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviPost
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviPost command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviPostPost
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviPostPost command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviPre
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviPre command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviPUSH
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviPush command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviRight
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviRight command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviRule
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviRule command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviW
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviW command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviX
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviX command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviXXX
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviXXX command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviY
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviY command) throws DviException, FontException,
            ConfigurationException;

    /**
     * execute DviZ
     * @param command       the command
     * @throws DviException if a dvi-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    void execute(DviZ command) throws DviException, FontException,
            ConfigurationException;
}
