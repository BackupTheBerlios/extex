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

import de.dante.util.StringList;

import java.util.Iterator;

/**
 * This is the interface for the configuration engine.
 * <p>
 * The configuration of a piece of software is in general independent of the
 * way the configuration information is stored. Only a few methods are required
 * to describe all features needed.
 * </p>
 * <p>
 * This interface is independent from the external representation of the
 * configuration information. This means that the configuration information
 * can be stored in an XML file, a properties file, a database, or even be
 * hard-coded in the source of an implementation.
 * </p>
 * <p>
 * For the illustration the external representation as a special form of XML is
 * used. The following example illustrates an XML configuration file.
 * </p>
 * <pre>
 *   &lt;?xml version="1.0" encoding="ISO-8859-1"?&gt;
 *   &lt;cfg&gt;
 *     . . .
 *     &lt;abc&gt;
 *     . . .
 *     &lt;/abc&gt;
 *     . . .
 *   &lt;/cfg&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface Configuration {

    /**
     * Getter for an attribute with a given name.
     *
     * @param name the tag name of the attribute
     *
     * @return the value of the attribute or <code>null</code> if such an
     * attribute is not present
     *
     * @throws ConfigurationException in case of any kind of problem
     */
    String getAttribute(String name) throws ConfigurationException;

    /**
     * Extract a sub-configuration with a given name.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;abc&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getConfig("abc")</tt> returns a new XMLConfig rooted at abc.
     * </p>
     * <p>
     * If there are more than one tags with the same name then the first one is
     * used.
     * </p>
     * <p>
     * If there are no tags with the given name then an exception is thrown.
     * </p>
     *
     * @param key the tag name of the sub-configuration
     *
     * @return the sub-configuration
     *
     * @throws ConfigurationNotFoundException in case that the given name does
     * not correspond to one of the tags in the current configuration
     */
    Configuration getConfiguration(String key)
        throws ConfigurationNotFoundException, ConfigurationException;

    /**
     * Extract a sub-configuration with a given name.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;abc&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getConfig("abc")</tt> returns a new XMLConfig rooted at abc.
     * </p>
     * <p>
     * If there are more than one tags with the same name then the first one is
     * used.
     * </p>
     * <p>
     * If there are no tags with the given name then an exception is thrown.
     * </p>
     *
     * @param key the tag name of the sub-configuration
     *
     * @return the sub-configuration or <code>null</code> if none is found
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    Configuration findConfiguration(String key)
        throws ConfigurationException;

    /**
     * Extract a sub-configuration with a given name and a given attribute.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;abc name="one"&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     &lt;abc name="two"&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getConfig("abc","two")</tt> returns a new XMLConfig rooted at
     * the abc with the name attribute "two".
     * </p>
     * <p>
     * If there are more than one tags with the same name then the first one is
     * used.
     * </p>
     * <p>
     * If there are no tags with the given name then an exception is thrown.
     * </p>
     *
     * @param key the tag name of the sub-configuration
     * @param attribute the value of the attribute name
     *
     * @return the sub-configuration
     *
     * @throws ConfigurationNotFoundException in case that the given name does
     * not correspond to one of the tags in the current configuration
     */
    Configuration getConfiguration(String key, String attribute)
        throws ConfigurationNotFoundException, ConfigurationException;

    /**
     * Retrieve a value from the configuration as <i>String</i>.
     * If the value could not be determined then <code>null</code> is
     * returned.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;one&gt;the first value&lt;/one&gt;
     *     &lt;two&gt;the second value&lt;/two&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getValue("two")</tt> returns the String "the second value".
     * </p>
     *
     * @param key the name of the desired value
     *
     * @return the value of key or <code>null</code>
     *
     * @throws ConfigurationException in case that something went wrong
     */
    String getValue(String key) throws ConfigurationException;

    /**
     * Getter for the textual value of this configuration.
     *
     * @return the text stored directly in this configuration
     *
     * @throws ConfigurationException in case that something went wrong
     */
    String getValue() throws ConfigurationException;

    /**
     * Retrieve a value from the configuration as <i>int</i>. If the value
     * could not be determined then a given default value is returned.
     *
     * @param key the name of the desired value
     * @param defaultValue the default value
     *
     * @return the value of key or the default value
     *
     * @throws ConfigurationException in case that something went wrong
     */
    int getValueAsInteger(String key, int defaultValue)
        throws ConfigurationException;

    /**
     * Get the list of all values with the given tag name in the current
     * configuration.
     *
     * @param key the name of the tags
     *
     * @return the list of values
     *
     * @throws ConfigurationException in case that something went wrong
     */
    StringList getValues(String key) throws ConfigurationException;

    /**
     * Retrieve an iterator over all items of a sub-configuration.
     *
     * @param key the name of the sub-configuration
     *
     * @return the iterator
     *
     * @throws ConfigurationException in case that something went wrong
     */
    Iterator iterator(String key) throws ConfigurationException;

}
