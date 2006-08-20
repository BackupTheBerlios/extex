/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.pageFilter.selector;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.backend.exception.BackendException;
import de.dante.extex.backend.exception.BackendMissingTargetException;
import de.dante.extex.backend.pageFilter.PagePipe;
import de.dante.extex.typesetter.type.page.Page;

/**
 * This page filter selects some pages to be shipped out.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class PageSelector implements PagePipe {

    /**
     * The field <tt>out</tt> contains the output target.
     */
    private PagePipe out = null;

    /**
     * The field <tt>pageNo</tt> contains the current page number.
     */
    private int pageNo = 0;

    /**
     * The field <tt>ranges</tt> contains the list of ranges to check.
     */
    private List ranges = null;

    /**
     * Creates a new object.
     *
     */
    public PageSelector() {

        super();
        pageNo = 0;
    }

    /**
     * Creates a new object.
     *
     * @param spec the specification of the pages to select
     */
    public PageSelector(final String spec) {

        super();
        addSelector(spec);
    }

    /**
     * Add some specifications for pages to be selected.
     *
     * @param spec the specification of the pages to select
     */
    public void addSelector(final String spec) {

        ranges = new ArrayList();
        String[] r = spec.split(",");

        for (int i = 0; i < r.length; i++) {
            String s = r[i].trim();
            int j = s.indexOf('-');
            if (j < 0) {
                int from = Integer.parseInt(s);
                addRule(new IntervalRule(from, from));
            } else if (j == 0) {
                addRule(new IntervalRule(Integer.MAX_VALUE, Integer.parseInt(s
                        .substring(1))));
            } else if (j == s.length() - 1) {
                addRule(new IntervalRule(Integer.parseInt(s.substring(0, s
                        .length() - 2)), Integer.MAX_VALUE));
            } else {
                addRule(new IntervalRule(Integer
                        .parseInt(s.substring(0, j - 1)), Integer.parseInt(s
                        .substring(j))));
            }
            //TODO gene: extend the syntax for mod
        }
    }

    /**
     * Add a rule to the set of rules.
     *
     * @param rule the rule to add
     */
    protected void addRule(final IntervalRule rule) {

        ranges.add(rule);
    }

    /**
     * @see de.dante.extex.backend.pageFilter.PagePipe#close()
     */
    public void close() throws BackendException {

        if (out == null) {
            throw new BackendMissingTargetException();
        }

        out.close();
    }

    /**
     * @see de.dante.extex.backend.nodeFilter.PagePipe#setOutput(
     *      de.dante.extex.backend.nodeFilter.PagePipe)
     */
    public void setOutput(final PagePipe pipe) {

        this.out = pipe;
    }

    /**
     * @see de.dante.extex.backend.nodeFilter.PagePipe#setParameter(
     *      java.lang.String, java.lang.String)
     */
    public void setParameter(final String name, final String value) {

    }

    /**
     * @see de.dante.extex.backend.nodeFilter.PagePipe#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
     */
    public void shipout(final Page page) throws BackendException {

        if (out == null) {
            throw new BackendMissingTargetException();
        }

        pageNo++;

        if (ranges == null) {
            out.shipout(page);
        }

        int size = ranges.size();
        for (int i = 0; i < size; i++) {
            if (((IntervalRule) ranges.get(i)).check(pageNo)) {
                out.shipout(page);
                return;
            }
        }
    }

}
