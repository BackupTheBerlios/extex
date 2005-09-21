<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- ====================================================================== -->
<!--  $Id: junit.xsl,v 1.1 2005/09/21 07:15:02 gene Exp $                -->
<!-- ====================================================================== -->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xalan="http://xml.apache.org/xslt">
 <xsl:output encoding="iso-8859-1" 
	     method="html"
 	     indent="yes"
	     xalan:indent-amount="1"/>

  <xsl:variable name="top">../..</xsl:variable>

 <!-- ===================================================================== -->
 <!-- Template: testsuite                                                   -->
 <!--                                                                       -->
  <xsl:template match="testsuite">
   <html>
    <head>
     <title>ExTeX Test Class <xsl:value-of select="@name"/></title>
     <link rel="stylesheet" href="{$top}/report.css"/>
    </head>
    <body>
     <h1><xsl:value-of select="@name"/></h1>
     <table>
      <tr>
       <td>Tests:</td><td><xsl:value-of select="@tests"/></td>
      </tr>
      <tr>
       <td>Failures:</td><td><xsl:value-of select="@failures"/></td>
      </tr>
      <tr>
       <td>Errors:</td><td><xsl:value-of select="@errors"/></td>
      </tr>
      <tr>
       <td>Time:</td><td><xsl:value-of select="@time"/></td>
      </tr>
     </table>
     <xsl:apply-templates select="testcase"/>
     <address>
     © 2005 <a href="{$top}/imprint.html">The ExTeX Group</a>
     </address>
    </body>
   </html>
  </xsl:template>

 <!-- ===================================================================== -->
 <!-- Template: testcase                                                    -->
 <!--                                                                       -->

  <xsl:template match="testcase">
   <a name="{@name}"/>
   <xsl:choose>
    <xsl:when test="count(error) > 0">
    <h2><img src="{$top}/image/crossed.gif"/> <xsl:value-of select="@name"/></h2>
    <pre><xsl:copy-of select="error/text()"/></pre>
    </xsl:when>
    <xsl:when test="count(failure) > 0">
    <h2><img src="{$top}/image/lightning.gif"/> <xsl:value-of select="@name"/></h2>
    <pre><xsl:copy-of select="failure/text()"/></pre>
    </xsl:when>
    <xsl:otherwise>
    <h2><img src="{$top}/image/checked.gif"/> <xsl:value-of select="@name"/></h2>
    </xsl:otherwise>
   </xsl:choose>
   Time: <xsl:value-of select="@time"/>
  </xsl:template>


  <xsl:template match="summary">
   <html>
    <head>
     <title>ExTeX Test Overview</title>
     <link rel="stylesheet" href="{$top}/report.css"/>
    </head>
    <body>
     <h1>Test Summary</h1>
     <xsl:value-of select="@date"/>
     <table>
      <xsl:apply-templates select="file"/>
     </table>
     <address>
     © 2005 <a href="{$top}/t.html">The ExTeX Group</a>
     </address>
    </body>
   </html>
  </xsl:template>

  <xsl:template match="file">
   <tr>
     <xsl:apply-templates select="document(@name)/testsuite" mode="index"/>
   </tr>
  </xsl:template>

  <xsl:template match="testsuite" mode="index">
   <td style="font-size:66%">
    <a href="TEST-{@name}.html"><xsl:value-of select="@name"/></a>
   </td>
   <td>
     <xsl:apply-templates select="testcase" mode="index"/>
   </td>
  </xsl:template>

  <xsl:template match="testcase" mode="index">
   <xsl:choose>
    <xsl:when test="count(error) > 0">
     <img src="{$top}/image/crossed.gif" title="{@name}"/>
    </xsl:when>
    <xsl:when test="count(failure) > 0">
     <img src="{$top}/image/lightning.gif" title="{@name}"/>
    </xsl:when>
    <xsl:otherwise>
     <img src="{$top}/image/checked.gif" title="{@name}"/>
    </xsl:otherwise>
   </xsl:choose>
  </xsl:template>

</xsl:stylesheet>

