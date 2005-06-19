%
% 2005 Gerd Neugebauer
%
\NeedsTeXFormat{LaTeX2e}
\LoadClass[12pt,div12,a4paper]{scrbook}

\usepackage[latin1]{inputenc}
\usepackage[T1]{fontenc}
\usepackage{booktabs}
\usepackage{multicol}
\usepackage{listings}
\usepackage{graphicx,pgf,pgfshade}

\definecolor{hellmagenta}{rgb}{1,0.75,0.9}
\definecolor{hellcyan}{rgb}{0.75,1,0.9}
\definecolor{hellgelb}{rgb}{1,1,0.8}
\definecolor{backgroundColor}{rgb}{1,1,0.8}
\definecolor{javaBackgroundColor}{rgb}{0.8,1,0.8}
\definecolor{colKeys}{rgb}{0,0,.8}
\definecolor{colIdentifier}{rgb}{0,0,0}
\definecolor{colComments}{rgb}{.7,.2,.2}
\definecolor{colString}{rgb}{0,0.5,0}
\definecolor{darkyellow}{rgb}{1,0.9,0}
\definecolor{colRule}{rgb}{.8,.8,.64}
%
\usepackage{listings}
\lstset{%
    language=[AlLaTeX]TEX,%
    float=hbp,%
    basicstyle=\ttfamily\small, %
    identifierstyle=\color{colIdentifier}, %
    keywordstyle=\color{colKeys}\bf\tt, %
    stringstyle=\color{colString}, %
    commentstyle=\color{colComments}, %
    columns=flexible, %
    tabsize=3, %
    frame=single, %
    extendedchars=true, %
    showspaces=false, %
    showstringspaces=false, %
    numbers=none,% left, %
    numberstyle=\tiny\sf, %
    breaklines=true, %
    backgroundcolor=\color{backgroundColor}, %
    breakautoindent=true, %
    captionpos=b,%
    xleftmargin=\fboxsep,%
    xrightmargin=\fboxsep%
}

\lstset{%
    language=Java,%
    float=hbp,%
    basicstyle=\ttfamily\small, %
    identifierstyle=\color{colIdentifier}, %
    keywordstyle=\color{colKeys}\bf\tt, %
    stringstyle=\color{colString}, %
    commentstyle=\itshape\color{colComments}, %
    columns=flexible, %
    tabsize=4, %
    frame=single, %
    extendedchars=true, %
    showspaces=false, %
    showstringspaces=false, %
    numbers=none,% =left, %
    numberstyle=\tiny\sf, %
    breaklines=true, %
    backgroundcolor=\color{javaBackgroundColor}, %
    breakautoindent=true, %
    captionpos=b,%
    xleftmargin=\fboxsep,%
    xrightmargin=\fboxsep%
}

\lstset{%
    float=hbp,%
    basicstyle=\ttfamily\small, %
    identifierstyle=\color{colIdentifier}, %
    keywordstyle=\color{colKeys}\bf\tt, %
    stringstyle=\color{colString}, %
    commentstyle=\bfseries\color{colComments}, %
    rulecolor=\color{colRule}, %
    columns=flexible, %
    tabsize=4, %
    frame=single, %
    extendedchars=true, %
    showspaces=false, %
    showstringspaces=false, %
    numbers=none,% =left, %
    numberstyle=\tiny\sf, %
    breaklines=true, %
    backgroundcolor=\color{backgroundColor}, %
    breakautoindent=true, %
    captionpos=b,%
    xleftmargin=\fboxsep,%
    xrightmargin=\fboxsep%
}

\usepackage{hyperref}
\definecolor{linkColor}{rgb}{.2,.2,.7}
\hypersetup{colorlinks, 
            citecolor=linkColor,
            filecolor=linkColor,
            linkcolor=linkColor,
            urlcolor=linkColor}
           
\providecommand*{\ExTeX}{\ifx\texorpdfstring\undefined
  \textrm{% the logo comes always with serifs
    \ensuremath{\textstyle\varepsilon_{\kern-0.15em\mathcal{X}}}%
    \kern-.15em\TeX}%
  \else\texorpdfstring{%
  \textrm{% the logo comes always with serifs
    \ensuremath{\textstyle\varepsilon_{\kern-0.15em\mathcal{X}}}%
    \kern-.15em\TeX
  }}{ExTeX}%
  \fi
}
\newcommand\ExTeXbox{%
  \begin{pgfpicture}{0mm}{0mm}{128mm}{64mm}
    \pgfdeclarehorizontalshading{thisShading}{120mm}%
    {color(0mm)=(white); rgb(160mm)=(.65,.65,.65)}%

    \pgfrect[fill]{\pgfpoint{0.5mm}{-0.5mm}}{\pgfpoint{128.5mm}{63.5mm}}

    \begin{pgfscope}
      \pgfmoveto{\pgforigin}
      \pgflineto{\pgfpoint{0mm}{64mm}}
      \pgflineto{\pgfpoint{128mm}{64mm}}
      \pgflineto{\pgfpoint{128mm}{0mm}}
      \pgfclosepath
      \pgfclip
      \pgfputat{\pgfpoint{64mm}{32mm}}{%
        \begin{pgfrotateby}{\pgfdegree{340}}
          \pgfbox[center,center]{\pgfuseshading{thisShading}}
        \end{pgfrotateby}}
    \end{pgfscope}
    
    \begin{pgfmagnify}{4}{4}
      \pgfputat{\pgfpoint{15.85mm}{8.15mm}}{%
        \pgfbox[center,center]{\color{white}\Huge\ExTeX}}
      \pgfputat{\pgfpoint{16.15mm}{7.85mm}}{%
        \pgfbox[center,center]{\color{white!25!black}\Huge\ExTeX}}
      \pgfputat{\pgfpoint{16mm}{8mm}}{%
        \pgfbox[center,center]{\color{white!80!black}\Huge\ExTeX}}
    \end{pgfmagnify}
    
    %  \pgfrect[stroke]{\pgfpoint{0mm}{0mm}}{\pgfpoint{128mm}{64mm}}
  \end{pgfpicture}%
}

\providecommand\eTeX{\ensuremath{\varepsilon}-\TeX}

\newcommand\INCOMPLETE{\bigskip\par\noindent{\unitlength=1mm
    \framebox(60,20){\color{gray}To be completed.}}\bigskip\par}
\newcommand\Incomplete{\bigskip\par\noindent{\unitlength=1mm
    \framebox(60,8){\color{gray}To be completed.}}\bigskip\par}

\newenvironment{abstract}{\begin{quotation}}{\end{quotation}}

\usepackage{multicol}
\renewenvironment{theindex}{%
  \setchapterpreamble{\index@preamble}%
  \if@openright\cleardoublepage\else\clearpage\fi%
  \chapter*{\indexname}\@mkboth{\indexname}{\indexname}%
  \begin{multicols}3
  \thispagestyle{\indexpagestyle}\parindent\z@
  \setlength{\parskip}{\z@ \@plus .3\p@}
  \setlength{\parfillskip}{\z@ \@plus 1fil}
  \let\item\@idxitem}
{\end{multicols}\clearpage}

\providecommand\indexGroup[1]{\vskip 12pt plus 4pt minus 2pt
  \textbf{\sffamily\normalsize #1}%
  \vskip 6pt plus 3pt minus 1pt
  \nopagebreak
}

\ProvidesClass{extex-doc}

