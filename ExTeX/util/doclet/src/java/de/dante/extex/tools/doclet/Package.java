/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
 */

package de.dante.tools.doclet;

import java.util.TreeMap;
import com.sun.javadoc.ClassDoc;

/**
 * This class implements a container for javadoc-packages.
 *
 * @author <a href="mailto:Alexander.Kraenzlein@gmx.de">Alexander Kraenzlein</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class Package {

    /**
     * the name of the package
     */
    private String pkg;

    /**
     * list of classes (sorted)
     */
    private TreeMap classes;

    /**
     * list of interface (sorted)
     */
    private TreeMap interfaces;

    /**
     * list of exceptions (sorted)
     */
    private TreeMap exceptions;

    /**
     * list of errors (sorted)
     */
    private TreeMap errors;

    /**
     * list of primitives (sorted)
     */
    private TreeMap primitives;

    /**
     * init
     * @param pkgname the package-name
     */
    public Package(final String pkgname) {

        pkg = pkgname;
        if (pkg == null || pkg.equals("")) {
            this.pkg = "- none - ";
        }
        // init TreeMaps
        classes = new TreeMap();
        interfaces = new TreeMap();
        exceptions = new TreeMap();
        errors = new TreeMap();
        primitives = new TreeMap();
    }

    /**
     * add a element to this package
     *
     * @param cd the ClassDoc-object to add to this package
     */
    public void addElement(final ClassDoc cd) {

        if (cd.isInterface()) {
            interfaces.put(cd.name(), cd);
        } else if (cd.isClass()) {
            if (isException(cd)) {
                exceptions.put(cd.name(), cd);
            } else if (isError(cd)) {
                errors.put(cd.name(), cd);
            } else if (isPrimitive(cd)) {
                primitives.put(cd.name(), cd);
            } else {
                classes.put(cd.name(), cd);
            }
        }
    }

    /**
     * isException ?
     * @param doc ClassDoc to test
     * @return true, if the ClassDoc is an Exception otherwise false
     */
    public boolean isException(final ClassDoc doc) {

        ClassDoc sup = doc.superclass();
        if (sup == null) {
            return false;
        }
        if (sup.name().equals("java.lang.Exception")) {
            return true;
        }
        return isException(sup);
    }

    /**
     * isError ?
     * @param doc ClassDoc to test
     * @return true, it the ClassDoc is an Error otherwise false
     */
    public boolean isError(final ClassDoc doc) {

        ClassDoc sup = doc.superclass();
        if (sup == null) {
            return false;
        }
        if (sup.name().equals("java.lang.Error")) {
            return true;
        }
        return isError(sup);
    }

    /**
     * isPrimitive ?
     * @param doc ClassDoc to test
     * @return true, it the ClassDoc is an Primitive otherwise false
     */
    public boolean isPrimitive(final ClassDoc doc) {

        if (doc.tags().length > 0) {
            for (int k = 0; k < doc.tags().length; k++) {
                if (doc.tags()[k].name().equals("@isPrimitive")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return Returns the classes.
     */
    public TreeMap getClasses() {

        return classes;
    }

    /**
     * @return Returns the errors.
     */
    public TreeMap getErrors() {

        return errors;
    }

    /**
     * @return Returns the exceptions.
     */
    public TreeMap getExceptions() {

        return exceptions;
    }

    /**
     * @return Returns the interfaces.
     */
    public TreeMap getInterfaces() {

        return interfaces;
    }

    /**
     * @return Returns the pkg.
     */
    public String getPkg() {

        return pkg;
    }

    /**
     * @return Returns the primitives.
     */
    public TreeMap getPrimitives() {

        return primitives;
    }
}