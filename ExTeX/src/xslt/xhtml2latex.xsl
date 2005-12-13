<?xml version="1.0" encoding="iso-8859-1"?>
<!--
   Tranformation from xhtml to latex.
   
   @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
   @version $Revision: 1.5 $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:html="http://www.w3.org/1999/xhtml">
    
    <!-- output -->
    <xsl:output method="text" encoding="iso-8859-1" indent="no"/>
    
    <!-- root -->
    <xsl:template match="/">
        <xsl:apply-templates select="html:html"/>
    </xsl:template>
    
    <!-- html -->
    <xsl:template match="html:html">
        <xsl:apply-templates select="html:head"/>
        <xsl:apply-templates select="html:body"/>
    </xsl:template>
    
    <!-- head -->
    <xsl:template match="html:head">
        <!-- do nothing -->
    </xsl:template>
    
    <!-- body -->
    <xsl:template match="html:body">
        <xsl:text>\documentclass{scrartcl}&#10;</xsl:text>
        <xsl:text>\usepackage[ngerman]{babel}&#10;</xsl:text>
        <xsl:text>\usepackage[latin1]{inputenc}&#10;</xsl:text>
        <xsl:text>\usepackage[TS1,T1]{fontenc}&#10;</xsl:text>
        <xsl:text>\usepackage[almostfull,force]{textcomp}&#10;</xsl:text>
        <xsl:text>\usepackage{alltt}&#10;</xsl:text>
        <xsl:text>\usepackage[expert]{lucidabr}&#10;</xsl:text>
        <xsl:text>\usepackage{hyperref}&#10;</xsl:text>
        <xsl:text>\begin{document}&#10;&#10;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{document}&#10;</xsl:text>
    </xsl:template>
    
    <!-- h1 -->
    <xsl:template match="html:h1">
        <xsl:text>\section{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- h2 -->
    <xsl:template match="html:h2">
        <xsl:text>\subsection{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- h3 -->
    <xsl:template match="html:h3">
        <xsl:text>\subsubsection{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- h4 -->
    <xsl:template match="html:h4">
        <xsl:text>\paragraph{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- h5 -->
    <xsl:template match="html:h5">
        <xsl:text>\subparagraph{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- h6 -->
    <xsl:template match="html:h6">
        <xsl:text>\textbf{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}\newline&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- ul -->
    <xsl:template match="html:ul">
        <xsl:text>\begin{itemize}&#10;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{itemize}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- ol -->
    <xsl:template match="html:ol">
        <xsl:text>\begin{enumerate}&#10;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{enumerate}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- dl -->
    <xsl:template match="html:dl">
        <xsl:text>\begin{description}&#10;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{description}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- pre -->
    <xsl:template match="html:pre">
        <xsl:text>\begin{alltt}\ttfamily\footnotesize</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{alltt}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- dd -->
    <xsl:template match="html:dd">
        <xsl:apply-templates/>
    </xsl:template>
    
    <!-- li -->
    <xsl:template match="html:li | html:dt">
        <xsl:text>\item </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&#10;
        </xsl:text>
    </xsl:template>
    
    <!-- p -->
    <xsl:template match="html:p">
        <xsl:apply-templates/>
        <xsl:text>&#10;&#10;
        </xsl:text>
    </xsl:template>
    
    <!-- quote -->
    <xsl:template match="html:quote">
        <xsl:text>``</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>''</xsl:text>
    </xsl:template>
    
    <!-- code -->
    <xsl:template match="html:code">
        <xsl:text>\texttt{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- b -->
    <xsl:template match="html:b">
        <xsl:text>\textbf{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- strong -->
    <xsl:template match="html:strong">
        <xsl:text>\textbf{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- it -->
    <xsl:template match="html:it">
        <xsl:text>\emph{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- em -->
    <xsl:template match="html:em">
        <xsl:text>\emph{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- tt -->
    <xsl:template match="html:tt">
        <xsl:text>\texttt{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- big -->
    <xsl:template match="html:big">
        <xsl:text>{\Large </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- small -->
    <xsl:template match="html:small">
        <xsl:text>{\footnotesize </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- table -->
    <xsl:template match="html:table">
        <xsl:text>\begin{table}&#10;</xsl:text>
        <xsl:text>\begin{tabular}{</xsl:text>
<!--        <xsl:choose>
            <xsl:when test="count(col)>0 or count(colgroup/col)>0">
                <xsl:for-each select="col | colgroup/col">
                    <xsl:text>|</xsl:text>
                    <xsl:choose>
                        <xsl:when test="@align='right'">
                            <xsl:text>r</xsl:text>
                        </xsl:when>
                        <xsl:when test="@align='center'">
                            <xsl:text>c</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>l</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise> -->
                <xsl:for-each select="descendant::html:tr[position()=1]/html:td">
                    <xsl:text>|</xsl:text>
                    <xsl:choose>
                        <xsl:when test="@align='right'">
                            <xsl:text>r</xsl:text>
                        </xsl:when>
                        <xsl:when test="@align='center'">
                            <xsl:text>c</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>l</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
 <!--           </xsl:otherwise>
        </xsl:choose> -->
        <xsl:text>|}&#10;</xsl:text>
        <xsl:text>\hline&#10;</xsl:text>
        <xsl:apply-templates select="html:thead | html:tfoot | html:tbody | html:tr"/>
        <xsl:text>\end{tabular}&#10;</xsl:text>
        <xsl:apply-templates select="html:caption"/>
        <xsl:text>\end{table}&#10;</xsl:text>
    </xsl:template>
    
    <!-- caption -->
    <xsl:template match="html:caption">
        <xsl:text>\caption{</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}&#10;</xsl:text>
    </xsl:template>
    
    <!-- tr -->
    <xsl:template match="html:tr">
        <xsl:apply-templates/>
        <xsl:text>\\ \hline&#10;</xsl:text>
    </xsl:template>
    
    <!-- th -->
    <xsl:template match="html:th">
        <xsl:choose>
            <xsl:when test="following-sibling::html:th">
                <xsl:text>\multicolumn{1}{|c}{\textbf{</xsl:text>
                <xsl:apply-templates/>
                <xsl:text>}}&amp;</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>\multicolumn{1}{|c|}{\textbf{</xsl:text>
                <xsl:apply-templates/>
                <xsl:text>}} </xsl:text>
                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- td -->
    <xsl:template match="html:td">
        <xsl:apply-templates/>
        <xsl:if test="local-name(following-sibling::*[1])='td'">
            <xsl:text>&amp;
            </xsl:text>
        </xsl:if>
    </xsl:template>
    
    <!--    <!-#- h1/a -#->
    <xsl:template match="h1/a">
    <!-#- ignore at the moment -#->
    </xsl:template>
    
    <!-#- h2/a -#->
    <xsl:template match="h2/a">
    <!-#- ignore at the moment -#->
    </xsl:template>
    
    <!-#- h3/a -#->
    <xsl:template match="h3/a">
    <!-#- ignore at the moment -#->
    </xsl:template>
    
    <!-#- h4/a -#->
    <xsl:template match="h4/a">
    <!-#- ignore at the moment -#->
    </xsl:template>
    
    <!-#- h5/a -#->
    <xsl:template match="h5/a">
    <!-#- ignore at the moment -#->
    </xsl:template>
    
    <!-#- h6/a -#->
    <xsl:template match="h6/a">
    <!-#- ignore at the moment -#->
    </xsl:template>
    -->
    <!-- a -->
    <xsl:template match="html:a">
        <xsl:choose>
            <xsl:when test='@name'>
                <xsl:choose>
                    <xsl:when test='@href'>
                        <xsl:text>\protect\hypertarget{</xsl:text>
                        <xsl:value-of select="./@name"/>
                        <xsl:text>}{}\href{</xsl:text>
                        <xsl:value-of select="translate(./@href,'#','')"/>
                        <xsl:text>}{</xsl:text>
                        <xsl:apply-templates/>
                        <xsl:text>}</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>\protect\hypertarget{</xsl:text>
                        <xsl:value-of select="./@name"/>
                        <xsl:text>}{</xsl:text>
                        <xsl:apply-templates/>
                        <xsl:text>}</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test='@href'>
                        <xsl:text>\protect\href{</xsl:text>
                        <xsl:value-of select="translate(./@href,'#','')"/>
                        <xsl:text>}{</xsl:text>
                        <xsl:apply-templates/>
                        <xsl:text>}</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- br -->
    <xsl:template match="html:br">
        <xsl:apply-templates/>
        <xsl:text>&#10;&#10;
        </xsl:text>
    </xsl:template>
    
    <!-- hr -->
    <xsl:template match="html:hr">
        <xsl:text>\par\noindent\rule{425pt}{2pt}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- img -->
    <xsl:template match="html:img">
        <xsl:text>\includegraphics{</xsl:text>
        <xsl:value-of select="@src"/>
        <xsl:text>}&#10;</xsl:text>
    </xsl:template>
    
    <!-- div[ align="center" ] -->
    <xsl:template match='html:div[@align="center"]'>
        <xsl:text>{\centering </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- center -->
    <xsl:template match="html:center">
        <xsl:text>{\centering </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <!-- div[ align="left" ] -->
    <xsl:template match='html:div[@align="left"]'>
        <xsl:text>\begin{flushleft}&#10;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{flushleft}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- div[ align0"right" ] -->
    <xsl:template match='html:div[@align="right"]'>
        <xsl:text>\begin{flushright}&#10;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>\end{flushright}&#10;&#10;</xsl:text>
    </xsl:template>
    
    <!-- Text -->
    <xsl:template match="text()">
        
        <!-- remove space -->
        <xsl:variable name="S">
            <!--            <xsl:value-of select="normalize-space()"/> -->
            <xsl:value-of select="."/>
        </xsl:variable>
        
        <xsl:if test="string-length($S)>0">
            <!-- replace some character -->
            <xsl:call-template name="LaTeXChar">
                <xsl:with-param name="i" select="string-length($S)"/>
                <xsl:with-param name="l" select="string-length($S)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    
    <!-- LaTeXChar: A recursif function that generates LaTeX special characters -->
    <xsl:template name="LaTeXChar">
        <xsl:param name="i"/>
        <xsl:param name="l"/>
        
        <!-- generate substring -->
        <xsl:variable name="SS">
            
            
            <!--            <xsl:value-of select="substring(normalize-space(),$l - $i + 1,1)"/> -->
            <xsl:value-of select="substring(.,$l - $i + 1,1)"/>
        </xsl:variable>
        
        <xsl:if test="$i > 0">
            <xsl:choose>
                <xsl:when test="$SS = 'é'">
                    <xsl:text>\'{e}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'ê'">
                    <xsl:text>\^{e}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'è'">
                    <xsl:text>\`{e}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'ï'">
                    <xsl:text>\"{\i}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'î'">
                    <xsl:text>\^{i}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'à'">
                    <xsl:text>\`{a}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'á'">
                    <xsl:text>\'{a}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'â'">
                    <xsl:text>\^{a}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'ç'">
                    <xsl:text>\c{c}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'ô'">
                    <xsl:text>\^{o}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'ù'">
                    <xsl:text>\`{u}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = 'û'">
                    <xsl:text>\^{u}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '|'">
                    <xsl:text>\symbol{124}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '_'">
                    <xsl:text>\_</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '#'">
                    <xsl:text>\#</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '{'">
                    <xsl:text>\{</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '}'">
                    <xsl:text>\}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '^'">
                    <xsl:text>\textasciicaron{}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '$'">
                    <xsl:text>\$</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '%'">
                    <xsl:text>\%</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '&amp;'">
                    <xsl:text>\&amp;</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '\'">
                    <xsl:text>\textbackslash{}</xsl:text>
                </xsl:when>
                <xsl:when test="$SS = '~'">
                    <xsl:text>\textasciitilde{}</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$SS"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text></xsl:text>
            
            <!-- call recursiv -->
            <xsl:call-template name="LaTeXChar">
                <xsl:with-param name="i" select="$i - 1"/>
                <xsl:with-param name="l" select="$l"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>