/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
 * @version $Revision: 1.10 $
 */
public abstract class ConfigurationException extends Exception {

    /**
     * The field <tt>message</tt> contains the message string for this
     * exception.
     */
    private String message = null;

    /**
     * The field <tt>source</tt> contains the location for this exception.
     */
    private String source = null;

    /**
     * Creates a new object.
     *
     * @param theMessage the message string
     */
    public ConfigurationException(final String theMessage) {
        super(theMessage);
        this.message = theMessage;
    }

    /**
     * Creates a new object.
     *
     * @param theMessage the message string
     * @param theSource the name of the file causing this error
     */
    public ConfigurationException(final String theMessage,
            final String theSource) {

        super(theMessage);
        this.message = theMessage;
        this.source = theSource;
    }

    /**
     * Creates a new object.
     *
     * @param aMessage message the message string
     * @param cause the next Throwable in the list
     */
    public ConfigurationException(final String aMessage, final Throwable cause) {

        super(aMessage, cause);
        this.message = aMessage;
    }

    /**
     * Getter for the message of this Exception.
     * The text is taken from the {@link de.dante.extex.i18n.Messages Messages}.
     * The key depends on the further information present:
     *
     * <p>
     * <tt>ConfigurationException.FormatCauseMessageLocation</tt>
     * is used when a cause, a message, and a location are present.
     * The arguments {1}, {2}, and {3} are set respectively.
     * </p>
     * <p>
     * <tt>ConfigurationException.FormatCauseLocation</tt>
     * is used when a cause and a location are present.
     * The arguments {1} and {2} are set respectively.
     * </p>
     * <p>
     * <tt>ConfigurationException.FormatCauseMessage</tt>
     * is used when a cause and a message are present.
     * The arguments {1} and {2} are set respectively.
     * </p>
     * <p>
     * <tt>ConfigurationException.FormatCause</tt>
     * is used when a cause is present.
     * The argument {1} is set respectively.
     * </p>
     * <p>
     * <tt>ConfigurationException.FormatMessageLocation</tt>
     * is used when a message and a location are present.
     * The arguments {1} and {2} are set respectively.
     * </p>
     * <p>
     * <tt>ConfigurationException.FormatMessage</tt>
     * is used when a message is present.
     * The argument {1} is set respectively.
     * </p>
     * <p>
     * <tt>ConfigurationException.FormatLocation</tt>
     * is used when a location is present.
     * The argument {1} is set respectively.
     * </p>
     * <p>
     * The arguments {0} is always replaced by the text from
     * <tt>{@link #getText() getText()}</tt>.
     * </p>
     *
     * @return the message
     */
    public String getMessage() {

        if (getCause() != null) {
            for (Throwable t = getCause(); t != null; t = t.getCause()) {
                String msg = t.getMessage();

                if (msg != null) {
                    if (source != null) {
                        if (message != null) {
                            return Messages
                                    .format(
                                            "ConfigurationException.FormatCauseMessageLocation",
                                            getText(), msg, message, source);
                        } else {
                            return Messages
                                    .format(
                                            "ConfigurationException.FormatCauseLocation",
                                            getText(), msg, source);
                        }
                    } else if (message != null) {
                        return Messages
                                .format(
                                        "ConfigurationException.FormatCauseMessage",
                                        getText(), msg, message);
                    } else {
                        return Messages
                                .format("ConfigurationException.FormatCause",
                                        getText(), msg);
                    }
                }
            }
        }

        if (source != null) {
            if (message != null) {
                return Messages
                        .format("ConfigurationException.FormatMessageLocation",
                                getText(), message, source);
            } else {
                return Messages.format("ConfigurationException.FormatLocation",
                                       getText(), source);
            }
        } else if (message != null) {
            return Messages.format("ConfigurationException.FormatMessage",
                                   getText(), message);
        } else {
            return Messages.format("ConfigurationException.Text",
                                   getText());
        }
    }

    /**
     * Getter for the text prefix of this ConfigurationException.
     * The text is taken from the {@link de.dante.extex.i18n.Messages Messages}
     * under the key <tt>ConfigurationException.Text</tt>.
     *
     * @return the text
     */
    protected String getText() {
        return Messages.format("ConfigurationException.Text");
    }

    /**
     * Getter for the unformatted message.
     *
     * @return the unformatted message
     */
    protected String getMessageUnformatted() {

        return super.getMessage();
    }
}
