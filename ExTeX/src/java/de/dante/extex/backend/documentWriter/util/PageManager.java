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

package de.dante.extex.backend.documentWriter.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a validator for page numbers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class PageManager {

    /**
     * This interface describes a validator.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private interface Validator {

        /**
         * Check that the number is contained a valid page.
         *
         * @param page the page number to check
         *
         * @return <code>true</code> iff the number is a valid page number.
         */
        boolean valid(int page);
    }

    /**
     * This class provides a container for a pair of integers.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class Range implements Validator {

        /**
         * The field <tt>max</tt> contains the highest value.
         */
        private int max;

        /**
         * The field <tt>min</tt> contains the lowest value.
         */
        private int min;

        /**
         * Creates a new object.
         *
         * @param min the lowest value
         * @param max the highest value
         */
        public Range(final int min, final int max) {

            this.min = min;
            this.max = max;
        }

        /**
         * Check that the number is contained in the range.
         *
         * @param page the page number to check
         *
         * @return <code>true</code> iff the number is contained in the range.
         */
        public boolean valid(final int page) {

            return page >= min && page <= max;
        }
    }

    /**
     * The field <tt>pages</tt> contains the list of validators. If the value is
     * <code>null</code> then all pages are selected.
     */
    private List pages = null;

    /**
     * Creates a new object.
     */
    public PageManager() {

        super();
    }

    /**
     * Add some pages to be accepted.
     * The specification is a comma separated list of single pages or ranges of
     * pages.
     *
     * <p>
     *  Examples
     * </p>
     * <pre>
     *  2
     *  2-5
     *  -12
     *  12-
     *  2-4,7-12
     *  2-4,12,28-
     * </pre>
     *
     * @param spec the comma separated list of page ranges
     *
     * @throws IllegalArgumentException in case of a parse error
     */
    public void addPages(final CharSequence spec)
            throws IllegalArgumentException {

        if (pages == null) {
            pages = new ArrayList();
        }

        int len = spec.length();
        int start;

        for (int i = 0; i < len; i++) {
            start = i;
            while (i < len && spec.charAt(i) != ',') {
                i++;
            }
            addPages(spec, start, i);
        }
    }

    /**
     * Add a set of pages to be acceptable.
     *
     * @param spec the specification
     * @param start the start index
     * @param end the index of the first character not to be considered
     */
    private void addPages(final CharSequence spec, final int start,
            final int end) {

        int i = skipSpace(spec, start, end);
        if (i >= end) {
            return;
        }
        int min;
        int c = spec.charAt(i);
        if (c == '-') {
            min = Integer.MIN_VALUE;
        } else if (c >= '0' && c <= '9') {
            min = c - '0';
            while (++i < end) {
                c = spec.charAt(i);
                if (c < '0' || c > '9') {
                    break;
                }
                min = 10 * min + c - '0';
            }
        } else {
            throw new IllegalArgumentException();
        }

        i = skipSpace(spec, i, end);

        int max;
        if (i >= end) {
            max = min;
        } else if (c == '-') {
            i = skipSpace(spec, i + 1, end);

            if (i >= end) {
                max = Integer.MAX_VALUE;
            } else {
                c = spec.charAt(i);
                if (c >= '0' && c <= '9') {
                    max = c - '0';
                    while (++i < end) {
                        c = spec.charAt(i);
                        if (c < '0' || c > '9') {
                            break;
                        }
                        max = 10 * max + c - '0';
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }

        if (skipSpace(spec, i, end) != end) {
            throw new IllegalArgumentException();
        }

        pages.add(new Range(min, max));
    }

    /**
     * Find the index of the first character which is not a space character.
     *
     * @param spec the specification
     * @param start the start index
     * @param end the index of the first character not to be considered
     *
     * @return the index of the first character after the spaces
     */
    private int skipSpace(final CharSequence spec, final int start,
            final int end) {

        int i = start;
        while (i < end) {
            if (!Character.isSpaceChar(spec.charAt(i))) {
                return i;
            }
            i++;
        }
        return i;
    }

    /**
     * Check whether the given page number is in a selected range.
     *
     * @param page the page number to check
     *
     * @return <code>true</code> iff the page is in a selected range
     */
    public boolean isSelected(final int page) {

        if (pages == null) {
            return true;
        }

        for (int i = pages.size() - 1; i >= 0; i--) {
            if (((Validator) pages.get(i)).valid(page)) {
                return true;
            }
        }
        return false;
    }
}
