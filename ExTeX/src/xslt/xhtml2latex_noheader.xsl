<?xml version="1.0" encoding="iso-8859-1"?>
<!--
   Tranformation from xhtml to latex.
   
   @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
   @version $Revision: 1.3 $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:html="http://www.w3.org/1999/xhtml">
    
    <xsl:import href="xhtml2latex.xsl"/>
    
    <!-- body -->
    <xsl:template match="html:body">
        <xsl:apply-templates/>
    </xsl:template>
    
</xsl:stylesheet>