<?xml version="1.0" encoding="iso-8859-1"?>
<!--
   Remove the namespace from all elements and attributes.
   
   @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
   @version $Revision: 1.1 $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!-- output -->
    <xsl:output method="xml" indent="no"/>
    
    <!-- copy -->
    <xsl:template match="/|comment()|processing-instruction()">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!-- remove namespace (element) -->
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    
    <!-- remove namespace (attribute) -->
    <xsl:template match="@*">
        <xsl:attribute name="{local-name()}">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>