/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.type.dimen;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the data type Dimen.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DimenTest extends ExTeXLauncher {

    /**
     * The constant <tt>INITIAL</tt> contains the initial value which should be
     * some strange value not related to anything else in some test case.
     */
    private static final int INITIAL = -111;

    /**
     * Constructor for DimenTest.
     *
     * @param arg the argument
     */
    public DimenTest(final String arg) {
        super(arg);
    }

    /**
     * Test case:
     * The constant ONE has a proper value.
     */
    public void testONE() {

        assertEquals(65536L, Dimen.ONE);
    }

    /**
     * Test case:
     * The constant ONE_PT has a proper value.
     */
    public void testONEpt() {

        assertEquals(65536L, Dimen.ONE_PT.getValue());
    }

    /**
     * Test case:
     * The constant ONE_PT has a proper value.
     */
    public void testONEinch() {

        assertEquals(4736286L, Dimen.ONE_INCH.getValue());
    }

    /**
     * Test case:
     * The constructor without parameters delivers a proper result.
     */
    public void test1() {

        assertEquals(0L, new Dimen().getValue());
    }

    /**
     * Test case:
     * The constructor with long parameter 0 delivers a proper result.
     */
    public void test10() {

        assertEquals(0L, new Dimen(0L).getValue());
    }

    /**
     * Test case:
     * The constructor with long parameter 1 delivers a proper result.
     */
    public void test11() {

        assertEquals(1L, new Dimen(1L).getValue());
    }

    /**
     * Test case:
     * The constructor with long parameter 2 delivers a proper result.
     */
    public void test12() {

        assertEquals(2L, new Dimen(2L).getValue());
    }

    /**
     * Test case:
     * The constructor with long parameter -1 delivers a proper result.
     */
    public void test13() {

        assertEquals(-1L, new Dimen(-1L).getValue());
    }

    /**
     * Test case:
     * The constructor with long parameter -2 delivers a proper result.
     */
    public void test14() {

        assertEquals(-2L, new Dimen(-2L).getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter 0 delivers a proper result.
     */
    public void test20() {

        assertEquals(0L, new Dimen(0).getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter 1 delivers a proper result.
     */
    public void test21() {

        assertEquals(1L, new Dimen(1).getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter 2 delivers a proper result.
     */
    public void test22() {

        assertEquals(2L, new Dimen(2).getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -1 delivers a proper result.
     */
    public void test23() {

        assertEquals(-1L, new Dimen(-1).getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -2 delivers a proper result.
     */
    public void test24() {

        assertEquals(-2L, new Dimen(-2).getValue());
    }

    /**
     * Test case:
     * The constructor with Dimen parameter null delivers a proper result.
     */
    public void test3null() {

        Dimen d = null;
        assertEquals(0L, new Dimen(d).getValue());
    }

    /**
     * Test case:
     * The constructor with Dimen parameter 0 delivers a proper result.
     */
    public void test30() {

        Dimen d = new Dimen(0);
        assertEquals(0L, new Dimen(d).getValue());
    }

    /**
     * Test case:
     * The constructor with Dimen parameter 1 delivers a proper result.
     */
    public void test31() {

        Dimen d = new Dimen(1);
        assertEquals(1L, new Dimen(d).getValue());
    }

    /**
     * Test case:
     * The constructor with Dimen parameter 2 delivers a proper result.
     */
    public void test32() {

        Dimen d = new Dimen(2);
        assertEquals(2L, new Dimen(d).getValue());
    }

    /**
     * Test case:
     * The constructor with Dimen parameter -1 delivers a proper result.
     */
    public void test33() {

        Dimen d = new Dimen(-1);
        assertEquals(-1L, new Dimen(d).getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -2 delivers a proper result.
     */
    public void test34() {

        Dimen d = new Dimen(-2);
        assertEquals(-2L, new Dimen(d).getValue());
    }

    /**
     * Test case:
     * The constructor with source parameter 0 delivers a proper result.
     * ...
     */
    //TODO gene: incomplete


    /**
     * Test case:
     * Setting to the long value 0 delivers a proper result.
     */
    public void testSet10() {

        Dimen d = new Dimen(INITIAL);
        d.set(0L);
        assertEquals(0L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the long value 1 delivers a proper result.
     */
    public void testSet11() {

        Dimen d = new Dimen(INITIAL);
        d.set(1L);
        assertEquals(1L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the long value 2 delivers a proper result.
     */
    public void testSet12() {

        Dimen d = new Dimen(INITIAL);
        d.set(2L);
        assertEquals(2L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the long value -1 delivers a proper result.
     */
    public void testSet13() {

        Dimen d = new Dimen(INITIAL);
        d.set(-1L);
        assertEquals(-1L, d.getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -2 delivers a proper result.
     */
    public void testSet14() {

        Dimen d = new Dimen(INITIAL);
        d.set(-2L);
        assertEquals(-2L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the Dimen value 0 delivers a proper result.
     */
    public void testSet20() {

        Dimen d = new Dimen(INITIAL);
        d.set(new Dimen(0L));
        assertEquals(0L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the Dimen value 1 delivers a proper result.
     */
    public void testSet21() {

        Dimen d = new Dimen(INITIAL);
        d.set(new Dimen(1L));
        assertEquals(1L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the Dimen value 2 delivers a proper result.
     */
    public void testSet22() {

        Dimen d = new Dimen(INITIAL);
        d.set(new Dimen(2L));
        assertEquals(2L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the Dimen value -1 delivers a proper result.
     */
    public void testSet23() {

        Dimen d = new Dimen(INITIAL);
        d.set(new Dimen(-1L));
        assertEquals(-1L, d.getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -2 delivers a proper result.
     */
    public void testSet24() {

        Dimen d = new Dimen(INITIAL);
        d.set(new Dimen(-2L));
        assertEquals(-2L, d.getValue());
    }

    /**
     * Test case:
     * Setting to the source parameter 0 delivers a proper result.
     * ...
     */
    //TODO: incomplete


    /**
     * Test case:
     * Adding 0 delivers a proper result.
     */
    public void testAdd00() {

        Dimen d = new Dimen(0);
        d.add(new Dimen(0));
        assertEquals(0L, d.getValue());
    }

    /**
     * Test case:
     * Adding 1 delivers a proper result.
     */
    public void testAdd01() {

        Dimen d = new Dimen(0);
        d.add(new Dimen(1));
        assertEquals(1L, d.getValue());
    }

    /**
     * Test case:
     * Adding 2 delivers a proper result.
     */
    public void testAdd02() {

        Dimen d = new Dimen(0);
        d.add(new Dimen(2));
        assertEquals(2L, d.getValue());
    }

    /**
     * Test case:
     * Adding -1 delivers a proper result.
     */
    public void testAdd03() {

        Dimen d = new Dimen(0);
        d.add(new Dimen(-1));
        assertEquals(-1L, d.getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -2 delivers a proper result.
     */
    public void testAdd04() {

        Dimen d = new Dimen(0);
        d.add(new Dimen(-2));
        assertEquals(-2L, d.getValue());
    }

    /**
     * Test case:
     * Adding 0 delivers a proper result.
     */
    public void testAdd10() {

        Dimen d = new Dimen(1);
        d.add(new Dimen(0));
        assertEquals(1L, d.getValue());
    }

    /**
     * Test case:
     * Adding 1 delivers a proper result.
     */
    public void testAdd11() {

        Dimen d = new Dimen(1);
        d.add(new Dimen(1));
        assertEquals(2L, d.getValue());
    }

    /**
     * Test case:
     * Adding 2 delivers a proper result.
     */
    public void testAdd12() {

        Dimen d = new Dimen(1);
        d.add(new Dimen(2));
        assertEquals(3L, d.getValue());
    }

    /**
     * Test case:
     * Adding -1 delivers a proper result.
     */
    public void testAdd13() {

        Dimen d = new Dimen(1);
        d.add(new Dimen(-1));
        assertEquals(0L, d.getValue());
    }

    /**
     * Test case:
     * The constructor with int parameter -2 delivers a proper result.
     */
    public void testAdd14() {

        Dimen d = new Dimen(1);
        d.add(new Dimen(-2));
        assertEquals(-1L, d.getValue());
    }



    /**
     * The field <tt>STRING</tt> contains the mapping from scaled points to
     * the printed representation in pt as computed by <logo>TeX</logo>.
     */
    private static final String[] STRING = {//
            /* 0 */ "0.0pt",
            /* 1 */ "0.00002pt",
            /* 2 */ "0.00003pt",
            /* 3 */ "0.00005pt",
            /* 4 */ "0.00006pt",
            /* 5 */ "0.00008pt",
            /* 6 */ "0.00009pt",
            /* 7 */ "0.0001pt",
            /* 8 */ "0.00012pt",
            /* 9 */ "0.00014pt",
            /* 10 */ "0.00015pt",
            /* 11 */ "0.00017pt",
            /* 12 */ "0.00018pt",
            /* 13 */ "0.0002pt",
            /* 14 */ "0.00021pt",
            /* 15 */ "0.00023pt",
            /* 16 */ "0.00024pt",
            /* 17 */ "0.00026pt",
            /* 18 */ "0.00027pt",
            /* 19 */ "0.00029pt",
            /* 20 */ "0.0003pt",
            /* 21 */ "0.00032pt",
            /* 22 */ "0.00034pt",
            /* 23 */ "0.00035pt",
            /* 24 */ "0.00037pt",
            /* 25 */ "0.00038pt",
            /* 26 */ "0.0004pt",
            /* 27 */ "0.00041pt",
            /* 28 */ "0.00043pt",
            /* 29 */ "0.00044pt",
            /* 30 */ "0.00046pt",
            /* 31 */ "0.00047pt",
            /* 32 */ "0.00049pt",
            /* 33 */ "0.0005pt",
            /* 34 */ "0.00052pt",
            /* 35 */ "0.00053pt",
            /* 36 */ "0.00055pt",
            /* 37 */ "0.00056pt",
            /* 38 */ "0.00058pt",
            /* 39 */ "0.0006pt",
            /* 40 */ "0.00061pt",
            /* 41 */ "0.00063pt",
            /* 42 */ "0.00064pt",
            /* 43 */ "0.00066pt",
            /* 44 */ "0.00067pt",
            /* 45 */ "0.00069pt",
            /* 46 */ "0.0007pt",
            /* 47 */ "0.00072pt",
            /* 48 */ "0.00073pt",
            /* 49 */ "0.00075pt",
            /* 50 */ "0.00076pt",
            /* 51 */ "0.00078pt",
            /* 52 */ "0.0008pt",
            /* 53 */ "0.00081pt",
            /* 54 */ "0.00082pt",
            /* 55 */ "0.00084pt",
            /* 56 */ "0.00085pt",
            /* 57 */ "0.00087pt",
            /* 58 */ "0.00089pt",
            /* 59 */ "0.0009pt",
            /* 60 */ "0.00092pt",
            /* 61 */ "0.00093pt",
            /* 62 */ "0.00095pt",
            /* 63 */ "0.00096pt",
            /* 64 */ "0.00098pt",
            /* 65 */ "0.00099pt",
            /* 66 */ "0.001pt",
            /* 67 */ "0.00102pt",
            /* 68 */ "0.00104pt",
            /* 69 */ "0.00105pt",
            /* 70 */ "0.00107pt",
            /* 71 */ "0.00108pt",
            /* 72 */ "0.0011pt",
            /* 73 */ "0.00111pt",
            /* 74 */ "0.00113pt",
            /* 75 */ "0.00114pt",
            /* 76 */ "0.00116pt",
            /* 77 */ "0.00117pt",
            /* 78 */ "0.00119pt",
            /* 79 */ "0.0012pt",
            /* 80 */ "0.00122pt",
            /* 81 */ "0.00124pt",
            /* 82 */ "0.00125pt",
            /* 83 */ "0.00127pt",
            /* 84 */ "0.00128pt",
            /* 85 */ "0.0013pt",
            /* 86 */ "0.00131pt",
            /* 87 */ "0.00133pt",
            /* 88 */ "0.00134pt",
            /* 89 */ "0.00136pt",
            /* 90 */ "0.00137pt",
            /* 91 */ "0.00139pt",
            /* 92 */ "0.0014pt",
            /* 93 */ "0.00142pt",
            /* 94 */ "0.00143pt",
            /* 95 */ "0.00145pt",
            /* 96 */ "0.00146pt",
            /* 97 */ "0.00148pt",
            /* 98 */ "0.0015pt",
            /* 99 */ "0.00151pt",
            /* 100 */ "0.00153pt",
            /* 101 */ "0.00154pt",
            /* 102 */ "0.00156pt",
            /* 103 */ "0.00157pt",
            /* 104 */ "0.00159pt",
            /* 105 */ "0.0016pt",
            /* 106 */ "0.00162pt",
            /* 107 */ "0.00163pt",
            /* 108 */ "0.00165pt",
            /* 109 */ "0.00166pt",
            /* 110 */ "0.00168pt",
            /* 111 */ "0.0017pt",
            /* 112 */ "0.00171pt",
            /* 113 */ "0.00172pt",
            /* 114 */ "0.00174pt",
            /* 115 */ "0.00175pt",
            /* 116 */ "0.00177pt",
            /* 117 */ "0.00179pt",
            /* 118 */ "0.0018pt",
            /* 119 */ "0.00182pt",
            /* 120 */ "0.00183pt",
            /* 121 */ "0.00185pt",
            /* 122 */ "0.00186pt",
            /* 123 */ "0.00188pt",
            /* 124 */ "0.00189pt",
            /* 125 */ "0.0019pt",
            /* 126 */ "0.00192pt",
            /* 127 */ "0.00194pt",
            /* 128 */ "0.00195pt",
            /* 129 */ "0.00197pt",
            /* 130 */ "0.00198pt",
            /* 131 */ "0.002pt",
            /* 132 */ "0.00201pt",
            /* 133 */ "0.00203pt",
            /* 134 */ "0.00204pt",
            /* 135 */ "0.00206pt",
            /* 136 */ "0.00208pt",
            /* 137 */ "0.00209pt",
            /* 138 */ "0.0021pt",
            /* 139 */ "0.00212pt",
            /* 140 */ "0.00214pt",
            /* 141 */ "0.00215pt",
            /* 142 */ "0.00217pt",
            /* 143 */ "0.00218pt",
            /* 144 */ "0.0022pt",
            /* 145 */ "0.00221pt",
            /* 146 */ "0.00223pt",
            /* 147 */ "0.00224pt",
            /* 148 */ "0.00226pt",
            /* 149 */ "0.00227pt",
            /* 150 */ "0.00229pt",
            /* 151 */ "0.0023pt",
            /* 152 */ "0.00232pt",
            /* 153 */ "0.00233pt",
            /* 154 */ "0.00235pt",
            /* 155 */ "0.00237pt",
            /* 156 */ "0.00238pt",
            /* 157 */ "0.0024pt",
            /* 158 */ "0.00241pt",
            /* 159 */ "0.00243pt",
            /* 160 */ "0.00244pt",
            /* 161 */ "0.00246pt",
            /* 162 */ "0.00247pt",
            /* 163 */ "0.00249pt",
            /* 164 */ "0.0025pt",
            /* 165 */ "0.00252pt",
            /* 166 */ "0.00253pt",
            /* 167 */ "0.00255pt",
            /* 168 */ "0.00256pt",
            /* 169 */ "0.00258pt",
            /* 170 */ "0.0026pt",
            /* 171 */ "0.00261pt",
            /* 172 */ "0.00262pt",
            /* 173 */ "0.00264pt",
            /* 174 */ "0.00266pt",
            /* 175 */ "0.00267pt",
            /* 176 */ "0.00269pt",
            /* 177 */ "0.0027pt",
            /* 178 */ "0.00272pt",
            /* 179 */ "0.00273pt",
            /* 180 */ "0.00275pt",
            /* 181 */ "0.00276pt",
            /* 182 */ "0.00278pt",
            /* 183 */ "0.00279pt",
            /* 184 */ "0.0028pt",
            /* 185 */ "0.00282pt",
            /* 186 */ "0.00284pt",
            /* 187 */ "0.00285pt",
            /* 188 */ "0.00287pt",
            /* 189 */ "0.00288pt",
            /* 190 */ "0.0029pt",
            /* 191 */ "0.00291pt",
            /* 192 */ "0.00293pt",
            /* 193 */ "0.00294pt",
            /* 194 */ "0.00296pt",
            /* 195 */ "0.00298pt",
            /* 196 */ "0.00299pt",
            /* 197 */ "0.003pt",
            /* 198 */ "0.00302pt",
            /* 199 */ "0.00304pt",
            /* 200 */ "0.00305pt",
            /* 201 */ "0.00307pt",
            /* 202 */ "0.00308pt",
            /* 203 */ "0.0031pt",
            /* 204 */ "0.00311pt",
            /* 205 */ "0.00313pt",
            /* 206 */ "0.00314pt",
            /* 207 */ "0.00316pt",
            /* 208 */ "0.00317pt",
            /* 209 */ "0.00319pt",
            /* 210 */ "0.0032pt",
            /* 211 */ "0.00322pt",
            /* 212 */ "0.00323pt",
            /* 213 */ "0.00325pt",
            /* 214 */ "0.00327pt",
            /* 215 */ "0.00328pt",
            /* 216 */ "0.0033pt",
            /* 217 */ "0.00331pt",
            /* 218 */ "0.00333pt",
            /* 219 */ "0.00334pt",
            /* 220 */ "0.00336pt",
            /* 221 */ "0.00337pt",
            /* 222 */ "0.00339pt",
            /* 223 */ "0.0034pt",
            /* 224 */ "0.00342pt",
            /* 225 */ "0.00343pt",
            /* 226 */ "0.00345pt",
            /* 227 */ "0.00346pt",
            /* 228 */ "0.00348pt",
            /* 229 */ "0.0035pt",
            /* 230 */ "0.00351pt",
            /* 231 */ "0.00352pt",
            /* 232 */ "0.00354pt",
            /* 233 */ "0.00356pt",
            /* 234 */ "0.00357pt",
            /* 235 */ "0.00359pt",
            /* 236 */ "0.0036pt",
            /* 237 */ "0.00362pt",
            /* 238 */ "0.00363pt",
            /* 239 */ "0.00365pt",
            /* 240 */ "0.00366pt",
            /* 241 */ "0.00368pt",
            /* 242 */ "0.0037pt",
            /* 243 */ "0.00371pt",
            /* 244 */ "0.00372pt",
            /* 245 */ "0.00374pt",
            /* 246 */ "0.00375pt",
            /* 247 */ "0.00377pt",
            /* 248 */ "0.00378pt",
            /* 249 */ "0.0038pt",
            /* 250 */ "0.00381pt",
            /* 251 */ "0.00383pt",
            /* 252 */ "0.00385pt",
            /* 253 */ "0.00386pt",
            /* 254 */ "0.00388pt",
            /* 255 */ "0.00389pt",
            /* 256 */ "0.0039pt",
            /* 257 */ "0.00392pt",
            /* 258 */ "0.00394pt",
            /* 259 */ "0.00395pt",
            /* 260 */ "0.00397pt",
            /* 261 */ "0.00398pt",
            /* 262 */ "0.004pt",
            /* 263 */ "0.00401pt",
            /* 264 */ "0.00403pt",
            /* 265 */ "0.00404pt",
            /* 266 */ "0.00406pt",
            /* 267 */ "0.00407pt",
            /* 268 */ "0.00409pt",
            /* 269 */ "0.0041pt",
            /* 270 */ "0.00412pt",
            /* 271 */ "0.00414pt",
            /* 272 */ "0.00415pt",
            /* 273 */ "0.00417pt",
            /* 274 */ "0.00418pt",
            /* 275 */ "0.0042pt",
            /* 276 */ "0.00421pt",
            /* 277 */ "0.00423pt",
            /* 278 */ "0.00424pt",
            /* 279 */ "0.00426pt",
            /* 280 */ "0.00427pt",
            /* 281 */ "0.00429pt",
            /* 282 */ "0.0043pt",
            /* 283 */ "0.00432pt",
            /* 284 */ "0.00433pt",
            /* 285 */ "0.00435pt",
            /* 286 */ "0.00436pt",
            /* 287 */ "0.00438pt",
            /* 288 */ "0.0044pt",
            /* 289 */ "0.00441pt",
            /* 290 */ "0.00443pt",
            /* 291 */ "0.00444pt",
            /* 292 */ "0.00446pt",
            /* 293 */ "0.00447pt",
            /* 294 */ "0.00449pt",
            /* 295 */ "0.0045pt",
            /* 296 */ "0.00452pt",
            /* 297 */ "0.00453pt",
            /* 298 */ "0.00455pt",
            /* 299 */ "0.00456pt",
            /* 300 */ "0.00458pt",
            /* 301 */ "0.0046pt",
            /* 302 */ "0.00461pt",
            /* 303 */ "0.00462pt",
            /* 304 */ "0.00464pt",
            /* 305 */ "0.00465pt",
            /* 306 */ "0.00467pt",
            /* 307 */ "0.00468pt",
            /* 308 */ "0.0047pt",
            /* 309 */ "0.00471pt",
            /* 310 */ "0.00473pt",
            /* 311 */ "0.00475pt",
            /* 312 */ "0.00476pt",
            /* 313 */ "0.00478pt",
            /* 314 */ "0.00479pt",
            /* 315 */ "0.0048pt",
            /* 316 */ "0.00482pt",
            /* 317 */ "0.00484pt",
            /* 318 */ "0.00485pt",
            /* 319 */ "0.00487pt",
            /* 320 */ "0.00488pt",
            /* 321 */ "0.0049pt",
            /* 322 */ "0.00491pt",
            /* 323 */ "0.00493pt",
            /* 324 */ "0.00494pt",
            /* 325 */ "0.00496pt",
            /* 326 */ "0.00497pt",
            /* 327 */ "0.00499pt",
            /* 328 */ "0.005pt",
            /* 329 */ "0.00502pt",
            /* 330 */ "0.00504pt",
            /* 331 */ "0.00505pt",
            /* 332 */ "0.00507pt",
            /* 333 */ "0.00508pt",
            /* 334 */ "0.0051pt",
            /* 335 */ "0.00511pt",
            /* 336 */ "0.00513pt",
            /* 337 */ "0.00514pt",
            /* 338 */ "0.00516pt",
            /* 339 */ "0.00517pt",
            /* 340 */ "0.00519pt",
            /* 341 */ "0.0052pt",
            /* 342 */ "0.00522pt",
            /* 343 */ "0.00523pt",
            /* 344 */ "0.00525pt",
            /* 345 */ "0.00526pt",
            /* 346 */ "0.00528pt",
            /* 347 */ "0.0053pt",
            /* 348 */ "0.00531pt",
            /* 349 */ "0.00533pt",
            /* 350 */ "0.00534pt",
            /* 351 */ "0.00536pt",
            /* 352 */ "0.00537pt",
            /* 353 */ "0.00539pt",
            /* 354 */ "0.0054pt",
            /* 355 */ "0.00542pt",
            /* 356 */ "0.00543pt",
            /* 357 */ "0.00545pt",
            /* 358 */ "0.00546pt",
            /* 359 */ "0.00548pt",
            /* 360 */ "0.0055pt",
            /* 361 */ "0.00551pt",
            /* 362 */ "0.00552pt",
            /* 363 */ "0.00554pt",
            /* 364 */ "0.00555pt",
            /* 365 */ "0.00557pt",
            /* 366 */ "0.00558pt",
            /* 367 */ "0.0056pt",
            /* 368 */ "0.00562pt",
            /* 369 */ "0.00563pt",
            /* 370 */ "0.00565pt",
            /* 371 */ "0.00566pt",
            /* 372 */ "0.00568pt",
            /* 373 */ "0.00569pt",
            /* 374 */ "0.0057pt",
            /* 375 */ "0.00572pt",
            /* 376 */ "0.00574pt",
            /* 377 */ "0.00575pt",
            /* 378 */ "0.00577pt",
            /* 379 */ "0.00578pt",
            /* 380 */ "0.0058pt",
            /* 381 */ "0.00581pt",
            /* 382 */ "0.00583pt",
            /* 383 */ "0.00584pt",
            /* 384 */ "0.00586pt",
            /* 385 */ "0.00587pt",
            /* 386 */ "0.00589pt",
            /* 387 */ "0.0059pt",
            /* 388 */ "0.00592pt",
            /* 389 */ "0.00594pt",
            /* 390 */ "0.00595pt",
            /* 391 */ "0.00597pt",
            /* 392 */ "0.00598pt",
            /* 393 */ "0.006pt",
            /* 394 */ "0.00601pt",
            /* 395 */ "0.00603pt",
            /* 396 */ "0.00604pt",
            /* 397 */ "0.00606pt",
            /* 398 */ "0.00607pt",
            /* 399 */ "0.00609pt",
            /* 400 */ "0.0061pt",
            /* 401 */ "0.00612pt",
            /* 402 */ "0.00613pt",
            /* 403 */ "0.00615pt",
            /* 404 */ "0.00616pt",
            /* 405 */ "0.00618pt",
            /* 406 */ "0.0062pt",
            /* 407 */ "0.00621pt",
            /* 408 */ "0.00623pt",
            /* 409 */ "0.00624pt",
            /* 410 */ "0.00626pt",
            /* 411 */ "0.00627pt",
            /* 412 */ "0.00629pt",
            /* 413 */ "0.0063pt",
            /* 414 */ "0.00632pt",
            /* 415 */ "0.00633pt",
            /* 416 */ "0.00635pt",
            /* 417 */ "0.00636pt",
            /* 418 */ "0.00638pt",
            /* 419 */ "0.0064pt",
            /* 420 */ "0.00641pt",
            /* 421 */ "0.00642pt",
            /* 422 */ "0.00644pt",
            /* 423 */ "0.00645pt",
            /* 424 */ "0.00647pt",
            /* 425 */ "0.00648pt",
            /* 426 */ "0.0065pt",
            /* 427 */ "0.00652pt",
            /* 428 */ "0.00653pt",
            /* 429 */ "0.00655pt",
            /* 430 */ "0.00656pt",
            /* 431 */ "0.00658pt",
            /* 432 */ "0.00659pt",
            /* 433 */ "0.0066pt",
            /* 434 */ "0.00662pt",
            /* 435 */ "0.00664pt",
            /* 436 */ "0.00665pt",
            /* 437 */ "0.00667pt",
            /* 438 */ "0.00668pt",
            /* 439 */ "0.0067pt",
            /* 440 */ "0.00671pt",
            /* 441 */ "0.00673pt",
            /* 442 */ "0.00674pt",
            /* 443 */ "0.00676pt",
            /* 444 */ "0.00677pt",
            /* 445 */ "0.00679pt",
            /* 446 */ "0.0068pt",
            /* 447 */ "0.00682pt",
            /* 448 */ "0.00684pt",
            /* 449 */ "0.00685pt",
            /* 450 */ "0.00687pt",
            /* 451 */ "0.00688pt",
            /* 452 */ "0.0069pt",
            /* 453 */ "0.00691pt",
            /* 454 */ "0.00693pt",
            /* 455 */ "0.00694pt",
            /* 456 */ "0.00696pt",
            /* 457 */ "0.00697pt",
            /* 458 */ "0.00699pt",
            /* 459 */ "0.007pt",
            /* 460 */ "0.00702pt",
            /* 461 */ "0.00703pt",
            /* 462 */ "0.00705pt",
            /* 463 */ "0.00706pt",
            /* 464 */ "0.00708pt",
            /* 465 */ "0.0071pt",
            /* 466 */ "0.00711pt",
            /* 467 */ "0.00713pt",
            /* 468 */ "0.00714pt",
            /* 469 */ "0.00716pt",
            /* 470 */ "0.00717pt",
            /* 471 */ "0.00719pt",
            /* 472 */ "0.0072pt",
            /* 473 */ "0.00722pt",
            /* 474 */ "0.00723pt",
            /* 475 */ "0.00725pt",
            /* 476 */ "0.00726pt",
            /* 477 */ "0.00728pt",
            /* 478 */ "0.0073pt",
            /* 479 */ "0.00731pt",
            /* 480 */ "0.00732pt",
            /* 481 */ "0.00734pt",
            /* 482 */ "0.00735pt",
            /* 483 */ "0.00737pt",
            /* 484 */ "0.00739pt",
            /* 485 */ "0.0074pt",
            /* 486 */ "0.00742pt",
            /* 487 */ "0.00743pt",
            /* 488 */ "0.00745pt",
            /* 489 */ "0.00746pt",
            /* 490 */ "0.00748pt",
            /* 491 */ "0.00749pt",
            /* 492 */ "0.0075pt",
            /* 493 */ "0.00752pt",
            /* 494 */ "0.00754pt",
            /* 495 */ "0.00755pt",
            /* 496 */ "0.00757pt",
            /* 497 */ "0.00758pt",
            /* 498 */ "0.0076pt",
            /* 499 */ "0.00761pt",
            /* 500 */ "0.00763pt",
            /* 501 */ "0.00764pt",
            /* 502 */ "0.00766pt",
            /* 503 */ "0.00768pt",
            /* 504 */ "0.00769pt",
            /* 505 */ "0.0077pt",
            /* 506 */ "0.00772pt",
            /* 507 */ "0.00774pt",
            /* 508 */ "0.00775pt",
            /* 509 */ "0.00777pt",
            /* 510 */ "0.00778pt",
            /* 511 */ "0.0078pt",
            /* 512 */ "0.00781pt",
            /* 513 */ "0.00783pt",
            /* 514 */ "0.00784pt",
            /* 515 */ "0.00786pt",
            /* 516 */ "0.00787pt",
            /* 517 */ "0.00789pt",
            /* 518 */ "0.0079pt",
            /* 519 */ "0.00792pt",
            /* 520 */ "0.00793pt",
            /* 521 */ "0.00795pt",
            /* 522 */ "0.00797pt",
            /* 523 */ "0.00798pt",
            /* 524 */ "0.008pt",
            /* 525 */ "0.00801pt",
            /* 526 */ "0.00803pt",
            /* 527 */ "0.00804pt",
            /* 528 */ "0.00806pt",
            /* 529 */ "0.00807pt",
            /* 530 */ "0.00809pt",
            /* 531 */ "0.0081pt",
            /* 532 */ "0.00812pt",
            /* 533 */ "0.00813pt",
            /* 534 */ "0.00815pt",
            /* 535 */ "0.00816pt",
            /* 536 */ "0.00818pt",
            /* 537 */ "0.0082pt",
            /* 538 */ "0.00821pt",
            /* 539 */ "0.00822pt",
            /* 540 */ "0.00824pt",
            /* 541 */ "0.00826pt",
            /* 542 */ "0.00827pt",
            /* 543 */ "0.00829pt",
            /* 544 */ "0.0083pt",
            /* 545 */ "0.00832pt",
            /* 546 */ "0.00833pt",
            /* 547 */ "0.00835pt",
            /* 548 */ "0.00836pt",
            /* 549 */ "0.00838pt",
            /* 550 */ "0.00839pt",
            /* 551 */ "0.0084pt",
            /* 552 */ "0.00842pt",
            /* 553 */ "0.00844pt",
            /* 554 */ "0.00845pt",
            /* 555 */ "0.00847pt",
            /* 556 */ "0.00848pt",
            /* 557 */ "0.0085pt",
            /* 558 */ "0.00851pt",
            /* 559 */ "0.00853pt",
            /* 560 */ "0.00854pt",
            /* 561 */ "0.00856pt",
            /* 562 */ "0.00858pt",
            /* 563 */ "0.00859pt",
            /* 564 */ "0.0086pt",
            /* 565 */ "0.00862pt",
            /* 566 */ "0.00864pt",
            /* 567 */ "0.00865pt",
            /* 568 */ "0.00867pt",
            /* 569 */ "0.00868pt",
            /* 570 */ "0.0087pt",
            /* 571 */ "0.00871pt",
            /* 572 */ "0.00873pt",
            /* 573 */ "0.00874pt",
            /* 574 */ "0.00876pt",
            /* 575 */ "0.00877pt",
            /* 576 */ "0.00879pt",
            /* 577 */ "0.0088pt",
            /* 578 */ "0.00882pt",
            /* 579 */ "0.00883pt",
            /* 580 */ "0.00885pt",
            /* 581 */ "0.00887pt",
            /* 582 */ "0.00888pt",
            /* 583 */ "0.0089pt",
            /* 584 */ "0.00891pt",
            /* 585 */ "0.00893pt",
            /* 586 */ "0.00894pt",
            /* 587 */ "0.00896pt",
            /* 588 */ "0.00897pt",
            /* 589 */ "0.00899pt",
            /* 590 */ "0.009pt",
            /* 591 */ "0.00902pt",
            /* 592 */ "0.00903pt",
            /* 593 */ "0.00905pt",
            /* 594 */ "0.00906pt",
            /* 595 */ "0.00908pt",
            /* 596 */ "0.0091pt",
            /* 597 */ "0.00911pt",
            /* 598 */ "0.00912pt",
            /* 599 */ "0.00914pt",
            /* 600 */ "0.00916pt",
            /* 601 */ "0.00917pt",
            /* 602 */ "0.00919pt",
            /* 603 */ "0.0092pt",
            /* 604 */ "0.00922pt",
            /* 605 */ "0.00923pt",
            /* 606 */ "0.00925pt",
            /* 607 */ "0.00926pt",
            /* 608 */ "0.00928pt",
            /* 609 */ "0.0093pt",
            /* 610 */ "0.00931pt",
            /* 611 */ "0.00932pt",
            /* 612 */ "0.00934pt",
            /* 613 */ "0.00935pt",
            /* 614 */ "0.00937pt",
            /* 615 */ "0.00938pt",
            /* 616 */ "0.0094pt",
            /* 617 */ "0.00941pt",
            /* 618 */ "0.00943pt",
            /* 619 */ "0.00945pt",
            /* 620 */ "0.00946pt",
            /* 621 */ "0.00948pt",
            /* 622 */ "0.00949pt",
            /* 623 */ "0.0095pt",
            /* 624 */ "0.00952pt",
            /* 625 */ "0.00954pt",
            /* 626 */ "0.00955pt",
            /* 627 */ "0.00957pt",
            /* 628 */ "0.00958pt",
            /* 629 */ "0.0096pt",
            /* 630 */ "0.00961pt",
            /* 631 */ "0.00963pt",
            /* 632 */ "0.00964pt",
            /* 633 */ "0.00966pt",
            /* 634 */ "0.00967pt",
            /* 635 */ "0.00969pt",
            /* 636 */ "0.0097pt",
            /* 637 */ "0.00972pt",
            /* 638 */ "0.00974pt",
            /* 639 */ "0.00975pt",
            /* 640 */ "0.00977pt",
            /* 641 */ "0.00978pt",
            /* 642 */ "0.0098pt",
            /* 643 */ "0.00981pt",
            /* 644 */ "0.00983pt",
            /* 645 */ "0.00984pt",
            /* 646 */ "0.00986pt",
            /* 647 */ "0.00987pt",
            /* 648 */ "0.00989pt",
            /* 649 */ "0.0099pt",
            /* 650 */ "0.00992pt",
            /* 651 */ "0.00993pt",
            /* 652 */ "0.00995pt",
            /* 653 */ "0.00996pt",
            /* 654 */ "0.00998pt",
            /* 655 */ "0.01pt",
            /* 656 */ "0.01001pt",
            /* 657 */ "0.01003pt",
            /* 658 */ "0.01004pt",
            /* 659 */ "0.01006pt",
            /* 660 */ "0.01007pt",
            /* 661 */ "0.01009pt",
            /* 662 */ "0.0101pt",
            /* 663 */ "0.01012pt",
            /* 664 */ "0.01013pt",
            /* 665 */ "0.01015pt",
            /* 666 */ "0.01016pt",
            /* 667 */ "0.01018pt",
            /* 668 */ "0.0102pt",
            /* 669 */ "0.01021pt",
            /* 670 */ "0.01022pt",
            /* 671 */ "0.01024pt",
            /* 672 */ "0.01025pt",
            /* 673 */ "0.01027pt",
            /* 674 */ "0.01028pt",
            /* 675 */ "0.0103pt",
            /* 676 */ "0.01031pt",
            /* 677 */ "0.01033pt",
            /* 678 */ "0.01035pt",
            /* 679 */ "0.01036pt",
            /* 680 */ "0.01038pt",
            /* 681 */ "0.01039pt",
            /* 682 */ "0.0104pt",
            /* 683 */ "0.01042pt",
            /* 684 */ "0.01044pt",
            /* 685 */ "0.01045pt",
            /* 686 */ "0.01047pt",
            /* 687 */ "0.01048pt",
            /* 688 */ "0.0105pt",
            /* 689 */ "0.01051pt",
            /* 690 */ "0.01053pt",
            /* 691 */ "0.01054pt",
            /* 692 */ "0.01056pt",
            /* 693 */ "0.01057pt",
            /* 694 */ "0.01059pt",
            /* 695 */ "0.0106pt",
            /* 696 */ "0.01062pt",
            /* 697 */ "0.01064pt",
            /* 698 */ "0.01065pt",
            /* 699 */ "0.01067pt",
            /* 700 */ "0.01068pt",
            /* 701 */ "0.0107pt",
            /* 702 */ "0.01071pt",
            /* 703 */ "0.01073pt",
            /* 704 */ "0.01074pt",
            /* 705 */ "0.01076pt",
            /* 706 */ "0.01077pt",
            /* 707 */ "0.01079pt",
            /* 708 */ "0.0108pt",
            /* 709 */ "0.01082pt",
            /* 710 */ "0.01083pt",
            /* 711 */ "0.01085pt",
            /* 712 */ "0.01086pt",
            /* 713 */ "0.01088pt",
            /* 714 */ "0.0109pt",
            /* 715 */ "0.01091pt",
            /* 716 */ "0.01093pt",
            /* 717 */ "0.01094pt",
            /* 718 */ "0.01096pt",
            /* 719 */ "0.01097pt",
            /* 720 */ "0.01099pt",
            /* 721 */ "0.011pt",
            /* 722 */ "0.01102pt",
            /* 723 */ "0.01103pt",
            /* 724 */ "0.01105pt",
            /* 725 */ "0.01106pt",
            /* 726 */ "0.01108pt",
            /* 727 */ "0.0111pt",
            /* 728 */ "0.01111pt",
            /* 729 */ "0.01112pt",
            /* 730 */ "0.01114pt",
            /* 731 */ "0.01115pt",
            /* 732 */ "0.01117pt",
            /* 733 */ "0.01118pt",
            /* 734 */ "0.0112pt",
            /* 735 */ "0.01122pt",
            /* 736 */ "0.01123pt",
            /* 737 */ "0.01125pt",
            /* 738 */ "0.01126pt",
            /* 739 */ "0.01128pt",
            /* 740 */ "0.01129pt",
            /* 741 */ "0.0113pt",
            /* 742 */ "0.01132pt",
            /* 743 */ "0.01134pt",
            /* 744 */ "0.01135pt",
            /* 745 */ "0.01137pt",
            /* 746 */ "0.01138pt",
            /* 747 */ "0.0114pt",
            /* 748 */ "0.01141pt",
            /* 749 */ "0.01143pt",
            /* 750 */ "0.01144pt",
            /* 751 */ "0.01146pt",
            /* 752 */ "0.01147pt",
            /* 753 */ "0.01149pt",
            /* 754 */ "0.0115pt",
            /* 755 */ "0.01152pt",
            /* 756 */ "0.01154pt",
            /* 757 */ "0.01155pt",
            /* 758 */ "0.01157pt",
            /* 759 */ "0.01158pt",
            /* 760 */ "0.0116pt",
            /* 761 */ "0.01161pt",
            /* 762 */ "0.01163pt",
            /* 763 */ "0.01164pt",
            /* 764 */ "0.01166pt",
            /* 765 */ "0.01167pt",
            /* 766 */ "0.01169pt",
            /* 767 */ "0.0117pt",
            /* 768 */ "0.01172pt",
            /* 769 */ "0.01173pt",
            /* 770 */ "0.01175pt",
            /* 771 */ "0.01176pt",
            /* 772 */ "0.01178pt",
            /* 773 */ "0.0118pt",
            /* 774 */ "0.01181pt",
            /* 775 */ "0.01183pt",
            /* 776 */ "0.01184pt",
            /* 777 */ "0.01186pt",
            /* 778 */ "0.01187pt",
            /* 779 */ "0.01189pt",
            /* 780 */ "0.0119pt",
            /* 781 */ "0.01192pt",
            /* 782 */ "0.01193pt",
            /* 783 */ "0.01195pt",
            /* 784 */ "0.01196pt",
            /* 785 */ "0.01198pt",
            /* 786 */ "0.012pt",
            /* 787 */ "0.01201pt",
            /* 788 */ "0.01202pt",
            /* 789 */ "0.01204pt",
            /* 790 */ "0.01205pt",
            /* 791 */ "0.01207pt",
            /* 792 */ "0.01208pt",
            /* 793 */ "0.0121pt",
            /* 794 */ "0.01212pt",
            /* 795 */ "0.01213pt",
            /* 796 */ "0.01215pt",
            /* 797 */ "0.01216pt",
            /* 798 */ "0.01218pt",
            /* 799 */ "0.01219pt",
            /* 800 */ "0.0122pt",
            /* 801 */ "0.01222pt",
            /* 802 */ "0.01224pt",
            /* 803 */ "0.01225pt",
            /* 804 */ "0.01227pt",
            /* 805 */ "0.01228pt",
            /* 806 */ "0.0123pt",
            /* 807 */ "0.01231pt",
            /* 808 */ "0.01233pt",
            /* 809 */ "0.01234pt",
            /* 810 */ "0.01236pt",
            /* 811 */ "0.01237pt",
            /* 812 */ "0.01239pt",
            /* 813 */ "0.0124pt",
            /* 814 */ "0.01242pt",
            /* 815 */ "0.01244pt",
            /* 816 */ "0.01245pt",
            /* 817 */ "0.01247pt",
            /* 818 */ "0.01248pt",
            /* 819 */ "0.0125pt",
            /* 820 */ "0.01251pt",
            /* 821 */ "0.01253pt",
            /* 822 */ "0.01254pt",
            /* 823 */ "0.01256pt",
            /* 824 */ "0.01257pt",
            /* 825 */ "0.01259pt",
            /* 826 */ "0.0126pt",
            /* 827 */ "0.01262pt",
            /* 828 */ "0.01263pt",
            /* 829 */ "0.01265pt",
            /* 830 */ "0.01266pt",
            /* 831 */ "0.01268pt",
            /* 832 */ "0.0127pt",
            /* 833 */ "0.01271pt",
            /* 834 */ "0.01273pt",
            /* 835 */ "0.01274pt",
            /* 836 */ "0.01276pt",
            /* 837 */ "0.01277pt",
            /* 838 */ "0.01279pt",
            /* 839 */ "0.0128pt",
            /* 840 */ "0.01282pt",
            /* 841 */ "0.01283pt",
            /* 842 */ "0.01285pt",
            /* 843 */ "0.01286pt",
            /* 844 */ "0.01288pt",
            /* 845 */ "0.0129pt",
            /* 846 */ "0.01291pt",
            /* 847 */ "0.01292pt",
            /* 848 */ "0.01294pt",
            /* 849 */ "0.01295pt",
            /* 850 */ "0.01297pt",
            /* 851 */ "0.01299pt",
            /* 852 */ "0.013pt",
            /* 853 */ "0.01302pt",
            /* 854 */ "0.01303pt",
            /* 855 */ "0.01305pt",
            /* 856 */ "0.01306pt",
            /* 857 */ "0.01308pt",
            /* 858 */ "0.01309pt",
            /* 859 */ "0.0131pt",
            /* 860 */ "0.01312pt",
            /* 861 */ "0.01314pt",
            /* 862 */ "0.01315pt",
            /* 863 */ "0.01317pt",
            /* 864 */ "0.01318pt",
            /* 865 */ "0.0132pt",
            /* 866 */ "0.01321pt",
            /* 867 */ "0.01323pt",
            /* 868 */ "0.01324pt",
            /* 869 */ "0.01326pt",
            /* 870 */ "0.01328pt",
            /* 871 */ "0.01329pt",
            /* 872 */ "0.0133pt",
            /* 873 */ "0.01332pt",
            /* 874 */ "0.01334pt",
            /* 875 */ "0.01335pt",
            /* 876 */ "0.01337pt",
            /* 877 */ "0.01338pt",
            /* 878 */ "0.0134pt",
            /* 879 */ "0.01341pt",
            /* 880 */ "0.01343pt",
            /* 881 */ "0.01344pt",
            /* 882 */ "0.01346pt",
            /* 883 */ "0.01347pt",
            /* 884 */ "0.01349pt",
            /* 885 */ "0.0135pt",
            /* 886 */ "0.01352pt",
            /* 887 */ "0.01353pt",
            /* 888 */ "0.01355pt",
            /* 889 */ "0.01357pt",
            /* 890 */ "0.01358pt",
            /* 891 */ "0.0136pt",
            /* 892 */ "0.01361pt",
            /* 893 */ "0.01363pt",
            /* 894 */ "0.01364pt",
            /* 895 */ "0.01366pt",
            /* 896 */ "0.01367pt",
            /* 897 */ "0.01369pt",
            /* 898 */ "0.0137pt",
            /* 899 */ "0.01372pt",
            /* 900 */ "0.01373pt",
            /* 901 */ "0.01375pt",
            /* 902 */ "0.01376pt",
            /* 903 */ "0.01378pt",
            /* 904 */ "0.0138pt",
            /* 905 */ "0.01381pt",
            /* 906 */ "0.01382pt",
            /* 907 */ "0.01384pt",
            /* 908 */ "0.01385pt",
            /* 909 */ "0.01387pt",
            /* 910 */ "0.01389pt",
            /* 911 */ "0.0139pt",
            /* 912 */ "0.01392pt",
            /* 913 */ "0.01393pt",
            /* 914 */ "0.01395pt",
            /* 915 */ "0.01396pt",
            /* 916 */ "0.01398pt",
            /* 917 */ "0.01399pt",
            /* 918 */ "0.014pt",
            /* 919 */ "0.01402pt",
            /* 920 */ "0.01404pt",
            /* 921 */ "0.01405pt",
            /* 922 */ "0.01407pt",
            /* 923 */ "0.01408pt",
            /* 924 */ "0.0141pt",
            /* 925 */ "0.01411pt",
            /* 926 */ "0.01413pt",
            /* 927 */ "0.01414pt",
            /* 928 */ "0.01416pt",
            /* 929 */ "0.01418pt",
            /* 930 */ "0.01419pt",
            /* 931 */ "0.0142pt",
            /* 932 */ "0.01422pt",
            /* 933 */ "0.01424pt",
            /* 934 */ "0.01425pt",
            /* 935 */ "0.01427pt",
            /* 936 */ "0.01428pt",
            /* 937 */ "0.0143pt",
            /* 938 */ "0.01431pt",
            /* 939 */ "0.01433pt",
            /* 940 */ "0.01434pt",
            /* 941 */ "0.01436pt",
            /* 942 */ "0.01437pt",
            /* 943 */ "0.01439pt",
            /* 944 */ "0.0144pt",
            /* 945 */ "0.01442pt",
            /* 946 */ "0.01443pt",
            /* 947 */ "0.01445pt",
            /* 948 */ "0.01447pt",
            /* 949 */ "0.01448pt",
            /* 950 */ "0.0145pt",
            /* 951 */ "0.01451pt",
            /* 952 */ "0.01453pt",
            /* 953 */ "0.01454pt",
            /* 954 */ "0.01456pt",
            /* 955 */ "0.01457pt",
            /* 956 */ "0.01459pt",
            /* 957 */ "0.0146pt",
            /* 958 */ "0.01462pt",
            /* 959 */ "0.01463pt",
            /* 960 */ "0.01465pt",
            /* 961 */ "0.01466pt",
            /* 962 */ "0.01468pt",
            /* 963 */ "0.0147pt",
            /* 964 */ "0.01471pt",
            /* 965 */ "0.01472pt",
            /* 966 */ "0.01474pt",
            /* 967 */ "0.01476pt",
            /* 968 */ "0.01477pt",
            /* 969 */ "0.01479pt",
            /* 970 */ "0.0148pt",
            /* 971 */ "0.01482pt",
            /* 972 */ "0.01483pt",
            /* 973 */ "0.01485pt",
            /* 974 */ "0.01486pt",
            /* 975 */ "0.01488pt",
            /* 976 */ "0.0149pt",
            /* 977 */ "0.01491pt",
            /* 978 */ "0.01492pt",
            /* 979 */ "0.01494pt",
            /* 980 */ "0.01495pt",
            /* 981 */ "0.01497pt",
            /* 982 */ "0.01498pt",
            /* 983 */ "0.015pt",
            /* 984 */ "0.01501pt",
            /* 985 */ "0.01503pt",
            /* 986 */ "0.01505pt",
            /* 987 */ "0.01506pt",
            /* 988 */ "0.01508pt",
            /* 989 */ "0.01509pt",
            /* 990 */ "0.0151pt",
            /* 991 */ "0.01512pt",
            /* 992 */ "0.01514pt",
            /* 993 */ "0.01515pt",
            /* 994 */ "0.01517pt",
            /* 995 */ "0.01518pt",
            /* 996 */ "0.0152pt",
            /* 997 */ "0.01521pt",
            /* 998 */ "0.01523pt",
            /* 999 */ "0.01524pt"
    };

    /**
     * Test case:
     * The printable representation as produced by toString() has to be
     * identical to the 1000 values in STRING which have been produced by TeX.
     */
    public void testToString() {

        Dimen d = new Dimen();

        for (long i = 0; i < STRING.length; i++) {
            d.set(i);
            assertTrue(STRING[(int) i].equals(d.toString()));
        }
    }

}
