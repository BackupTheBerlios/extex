/*
 * Copyright (C) 2003-2004  Gerd Neugebauer
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
package de.dante.util.configuration;

import de.dante.extex.i18n.Messages;

/**
 * This exception is thrown when a problem in the configuration is detected.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public abstract class ConfigurationException extends Exception {
    /** The message string for this exception */
    public String message = null;

    /** The location for this exception */
    public String source = null;

    /**
     * Creates a new object.
     *
     * @param message the message string
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Creates a new object.
     *
     * @param message the message string
     * @param source the name of the file causing this error
     */
    public ConfigurationException(final String message, final String source) {
        super(message);
        this.message = message;
        this.source  = source;
    }

    /**
     * Creates a new object.
     * 
     * @param message message the message string
     * @param cause the next Throwable in the list
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * Getter for the message of this Exception
     *
     * @return the message
     */
    public String getMessage() {
        if (getCause() != null) {
            for (Throwable t = getCause(); t != null;
                     t = t.getCause()) {
                String msg = t.getMessage();

                if (msg != null) {
                    if (source != null) {
                        if (message != null) {
                            return Messages.format("ConfigException.format_text_cause_message_location",
                                                   getText(), msg, message, source);
                        } else {
                            return Messages.format("ConfigException.format_text_cause_location",
                                                   getText(), msg, source);
                        }
                    } else if (message != null) {
                        return Messages.format("ConfigException.format_text_cause_message",
                                               getText(), msg, message);
                    } else {
                        return Messages.format("ConfigException.format_text_cause",
                                               getText(),msg);
                    }
                }
            }
        }

        if (source != null) {
            if (message != null) {
                return Messages.format("ConfigException.format_text_message_location",
                                       getText(), message, source);
            } else {
                return Messages.format("ConfigException.format_text_location",
                                       getText(), source);
            }
        } else if (message != null) {
            return Messages.format("ConfigException.format_text_message",
                                   getText(), message);
        } else {
            return Messages.format("ConfigException.format_text",
                                   getText());
        }
    }

    /**
     * Getter for the text prefix of this ConfigException.
     *
     * @return the text
     */
    protected String getText() {
        return Messages.format("ConfigException.Configuration_error");
    }
}
