<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
   Umwandlung der tfm-xml-Ausgabe nach HTML
   
   @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
   @version $Revision: 1.2 $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" encoding="ISO-8859-1" doctype-public 
        = "-//W3C//DTD HTML 4.01 Transitional//EN" doctype-system = 
        "http://www.w3.org/TR/html4/loose.dtd"/>
    <xsl:param name="cols" select="16" />
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>TFM</title>
                <xsl:element name="link">
                    <xsl:attribute name="rel">stylesheet</xsl:attribute>
                    <xsl:attribute name="type">text/css</xsl:attribute>
                    <xsl:attribute name="href">tfm.css</xsl:attribute>
                </xsl:element>
            </head>
            <body>
                <h1>Font</h1>
                <xsl:apply-templates select="//header"/>
                <xsl:apply-templates select="//charinfo"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="header">
        <ol>
            <li>Fontfamily <xsl:value-of select="@fontfamily"/> </li>
            <li>Designsize <xsl:value-of select="@desingsize"/> </li>
            <li>Checksum <xsl:value-of select="@checksum"/> </li>
            <li>Codingscheme <xsl:value-of select="@codingscheme"/> </li>
            <li>Fonttype <xsl:value-of select="@fonttype"/> </li>
            <li>Sevenbitsafe <xsl:value-of select="@sevenbitsafe"/> </li>
            <li>Xeroxfacecode <xsl:value-of select="@xeroxfacecode"/> </li>
        </ol>
    </xsl:template>
    <xsl:template match="charinfo"> <h2>Übersicht</h2> <table border="1" class="tabel_ueb"> 
        <tbody> <xsl:for-each select="char[position() mod $cols = 1]"> 
        <tr><xsl:variable name="rowy" 
        select=".|following-sibling::char[position() &lt; $cols]"/><xsl:variable 
        name="cellcount" select="count($rowy)"/><xsl:for-each 
        select="$rowy"><td align="center"><xsl:element name="a"><xsl:attribute 
        name="href">#<xsl:value-of select="@id"/></xsl:attribute><xsl:value-of 
        select="@id"/> </xsl:element></td></xsl:for-each><xsl:call-template 
        name="makeemptycells"><xsl:with-param name="cellcount" 
        select="$cellcount"/></xsl:call-template></tr>         </xsl:for-each> 
        </tbody> </table> 
        <h2>Zeichen</h2><table border="1" width="100%" 
        class="tab_char"> <xsl:apply-templates select="char" mode="full"/> 
        </table> </xsl:template>
    <xsl:template match="char" mode="full">
        <tr>
            <td width="5%" class="td_char_lh">
                <xsl:element name="a">
                    <xsl:attribute name="name">
                        <xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <xsl:value-of select="@id"/>
                </xsl:element>
            </td>
            <td width="95%" class="td_char_rh">
                <xsl:value-of select="@char"/>
            </td>
        </tr>
        <tr>
            <td class="td_char_r"></td>
            <td class="td_char_l">
                <table width="100%" class="tab_width">
                    <tr>
                        <th width="25%">width</th>
                        <th width="25%">height</th>
                        <th width="25%">depth</th>
                        <th width="25%">italic</th>
                    </tr>
                    <tr>
                        <td>
                            <xsl:value-of select="@width"/>
                        </td>
                        <td>
                            <xsl:value-of select="@height"/>
                        </td>
                        <td>
                            <xsl:value-of select="@depth"/>
                        </td>
                        <td>
                            <xsl:value-of select="@italic"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <xsl:if test="count(ligature)>0">
            <tr>
                <td></td>
                <td>
                    <table width="100%" class="tab_lig">
                        <tr>
                            <th width="25%"> letter-id </th>
                            <th width="25%"> letter </th>
                            <th width="25%"> lig-id </th>
                            <th width="25%"> lig </th>
                        </tr>
                        <xsl:apply-templates select="ligature"/>
                    </table>
                </td>
            </tr>
        </xsl:if>
        <xsl:if test="count(kerning)>0">
            <tr>
                <td></td>
                <td>
                    <table width="100%" class="tab_kern">
                        <tr>
                            <th width="33%"> id </th>
                            <th width="33%"> char </th>
                            <th width="33%"> size </th>
                        </tr>
                        <xsl:apply-templates select="kerning"/>
                    </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xsl:template match="ligature">
        <tr>
            <td width="25%">
                <xsl:value-of select="@letter-id"/>
            </td>
            <td width="25%">
                <xsl:value-of select="@letter"/>
            </td>
            <td width="25%">
                <xsl:value-of select="@lig-id"/>
            </td>
            <td width="25%">
                <xsl:value-of select="@lig"/>
            </td>
        </tr>
    </xsl:template>
    <xsl:template match="kerning">
        <tr>
            <td width="33%">
                <xsl:value-of select="@id"/>
            </td>
            <td width="33%">
                <xsl:value-of select="@char"/>
            </td>
            <td width="33%">
                <xsl:value-of select="@size"/>
            </td>
        </tr>
    </xsl:template>
    <xsl:template name="makeemptycells">
        <xsl:param name="cellcount"/>
        <xsl:variable name="ncc" select="number($cellcount)"/>
        <xsl:if test="$ncc &lt; $cols">
            <td style="emptycells: show;"></td>
            <xsl:call-template name="makeemptycells">
                <xsl:with-param name="cellcount" select="$ncc + 1" />
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>