<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- ====================================================================== -->
<!--  $Id: junit.xsl,v 1.5 2005/10/06 10:49:59 gene Exp $                   -->
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
  <xsl:template name="header">
<table width="100%" class="head" cellspacing="0" cellpadding="0">
 <tr>

  <td colspan="10" class="head">
   <a href="index.html"><img src="{$top}/image/ExTeX-small.gif" alt="ExTeX" border="0"/></a>
  </td>
 </tr>
 <tr>
  <td class="tabOff"><img src="{$top}/image/tab.gif" align="top" /></td>
  <td class="tabOFF"><a class="nav" href="{$top}/development/index.html">Development</a></td>
  <td> </td>

  <td class="tabOff"><img src="{$top}/image/tabOff.gif" align="top" /></td>
  <td class="tabOFF"><a class="nav" href="{$top}/documentation/index.html">Documentation</a></td>
  <td> </td>
  <td class="tabOff"><img src="{$top}/image/tabOff.gif" align="top" /></td>
  <td class="tabOFF"><a class="nav" href="{$top}/sources/index.html">Download</a></td>
  <td width="90%"> </td>
 </tr>

</table>
<div class="tabSep"><br/></div>
  </xsl:template>

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
     <xsl:call-template name="header"/>
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
     <xsl:apply-templates select="testcase" mode="details"/>
     <address>
     © 2005 <a href="{$top}/imprint.html">The ExTeX Group</a>
     </address>
    </body>
   </html>
  </xsl:template>

 <!-- ===================================================================== -->
 <!-- Template: testcase                                                    -->
 <!--                                                                       -->
  <xsl:template match="testcase" mode="details">
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

 <!-- ===================================================================== -->
 <!-- Template: summary                                                     -->
 <!--                                                                       -->
  <xsl:template match="summary">
   <html>
    <head>
     <title>ExTeX Test Overview</title>
     <link rel="stylesheet" href="{$top}/report.css"/>
    </head>
    <body>
     <xsl:call-template name="header"/>
     <h1>Test Summary</h1>
     <small><xsl:value-of select="@date"/></small>

     <xsl:variable name="all" select="file"/>
     <xsl:copy-of select="$all"/>

<!--
     <xsl:variable name="ok"><xsl:call-template name="sel">
      <xsl:with-param name="from"><xsl:copy-of select="$all"/></xsl:with-param>
      <xsl:with-param name="c">A</xsl:with-param>
     </xsl:call-template></xsl:variable>
     : <xsl:value-of select="$ok"/>
-->

     <p>
      Test cases: <xsl:value-of select="count($all)"/>
     </p>

     <table>
      <xsl:apply-templates select="file" mode="ok"/>
     </table>

     <address>
      © 2005 <a href="{$top}/t.html">The ExTeX Group</a>
     </address>
    </body>
   </html>
  </xsl:template>


  <xsl:template name="sel">
   <xsl:param name="from"/>
   <xsl:param name="c"/>
_<xsl:value-of select="$from"/>_

   <xsl:if test="contains($from, $c)">
    <xsl:call-template name="sel">
     <xsl:with-param name="from" select="substring-after($from,$c)"/>
     <xsl:with-param name="char" select="$c"/>
    </xsl:call-template>
   </xsl:if>
  </xsl:template>


 <!-- ===================================================================== -->
 <!-- Template: file                                                        -->
 <!--                                                                       -->
  <xsl:template match="file" mode="s">
    <xsl:apply-templates select="document(@name)/testsuite/testcase" mode="s"/>
  </xsl:template>

 <!-- ===================================================================== -->
 <!-- Template: testcase                                                    -->
 <!--                                                                       -->
  <xsl:template match="testcase" mode="s">
   <xsl:choose>
    <xsl:when test="count(error) > 0">A</xsl:when>
    <xsl:when test="count(failure) > 0">B</xsl:when>
    <xsl:otherwise>C</xsl:otherwise>
   </xsl:choose>
  </xsl:template>


 <!-- ===================================================================== -->
 <!-- Template: file                                                        -->
 <!--                                                                       -->
  <xsl:template match="file" mode="ok">
   <tr>
     <xsl:apply-templates select="document(@name)/testsuite" mode="ok"/>
   </tr>
  </xsl:template>

 <!-- ===================================================================== -->
 <!-- Template: testsuite                                                   -->
 <!--                                                                       -->
  <xsl:template match="testsuite" mode="ok">
   <td style="font-size:66%">
    <a href="TEST-{@name}.html"><xsl:value-of select="@name"/></a>
   </td>
   <td>
     <xsl:apply-templates select="testcase" mode="ok"/>
   </td>
  </xsl:template>

 <!-- ===================================================================== -->
 <!-- Template: testcase                                                    -->
 <!--                                                                       -->
  <xsl:template match="testcase" mode="ok">
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
