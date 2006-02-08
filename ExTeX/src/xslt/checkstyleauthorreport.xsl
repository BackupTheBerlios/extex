<?xml version="1.0" encoding="ISO-8859-1"?>

<!--
   Tranformation a checkstyle report with autor to a html file.
   
   @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
   @version $Revision: 1.1 $
-->

<xsl:stylesheet version="1.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <!-- output -->
   <xsl:output method="html" encoding="utf-8" indent="no" />

   <!--  strip all white space -->
   <xsl:strip-space elements="*" />

   <!-- root -->
   <xsl:template match="/">
      <html>
         <head>
            <title>Chechstyle Report</title>
         </head>
         <body>
            <h1>Checkstyle report</h1>
            <xsl:apply-templates select="checkstyle" />
         </body>
      </html>
   </xsl:template>

   <!--  checkstyle -->
   <xsl:template match="checkstyle">
      <table border="1">
         <tr>
            <td>files</td>
            <td>
               <xsl:value-of select="count(file)" />
            </td>
         </tr>
         <tr>
            <td>errors</td>
            <td>
               <xsl:value-of select="count(file/error)" />
            </td>
         </tr>
         <xsl:for-each
            select="/checkstyle/file/author[not(../preceding::file/author=.)]">
            <xsl:sort select="." />
            <xsl:variable name="val" select="text()" />
            <tr>
               <td>maintainer</td>
               <td>
                  <xsl:element name="a">
                     <xsl:attribute name="href">
                        <xsl:text>#</xsl:text>
                        <xsl:value-of select="translate($val,' ','')" />
                     </xsl:attribute>
                     <xsl:value-of select="$val" />
                  </xsl:element>
               </td>
            </tr>
         </xsl:for-each>
      </table>
      <h2>files</h2>
      <table border="1">
         <xsl:for-each select="file">
            <xsl:sort select="author" />
            <tr>
               <td>
                  <xsl:if test="author[not(../preceding::file/author=.)]">
                     <xsl:element name="a">
                        <xsl:attribute name="name">
                           <xsl:value-of select="translate(author,' ','')" />
                        </xsl:attribute>
                     </xsl:element>
                  </xsl:if>
                  <xsl:number value="position()" format="1. " />
               </td>
               <td>
                  <xsl:element name="a">
                     <xsl:attribute name="href">
                        <xsl:text>file:</xsl:text>
                        <xsl:value-of select="@name" />
                     </xsl:attribute>
                     <xsl:value-of select="@name" />
                  </xsl:element>

               </td>
            </tr>
            <tr>
               <td></td>
               <td>
                  <xsl:value-of select="author" />
               </td>
            </tr>
            <xsl:apply-templates select="error" />
         </xsl:for-each>
      </table>
   </xsl:template>

   <!--  error -->
   <xsl:template match="error">
      <tr>
         <td></td>
         <td>
            <xsl:value-of select="@message" />
            <xsl:text>(</xsl:text>
            <xsl:value-of select="@line" />
            <xsl:text>)</xsl:text>
         </td>
      </tr>
   </xsl:template>

</xsl:stylesheet>