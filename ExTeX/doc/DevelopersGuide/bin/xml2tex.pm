#!/usr/local/bin/perl
##*****************************************************************************
## $Id: xml2tex.pm,v 1.1 2005/08/29 07:08:46 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

xml2tex.pm - ...

=head1 SYNOPSIS

xml2tex.pm

=head1 DESCRIPTION

=head1 Attributes

=head1 Methods

=head1 AUTHOR

Gerd Neugebauer

=head1 BUGS

=over 4

=item *

...

=back

=cut

#------------------------------------------------------------------------------
package xml2tex;

use strict;
use Exporter;
our @ISA       = qw(Exporter);
our @EXPORT    = qw(translate processDocTag);
our @EXPORT_OK = qw();

#------------------------------------------------------------------------------
# Variable:	$VERSION
# Description:	
#
our $VERSION = ('$Revision: 1.1 $ ' =~ m/[0-9.]+/ ? $& : '0.0' );

#------------------------------------------------------------------------------
# Function:	translate
#
sub translate {
  local $_ = shift;
  s|^ *\* ?||;
#      s|\\|\\|g;
  s|([~_\%\$])|\\$1|g;
  s|\s*\&nbsp;\s*|~|g;
  s|\&ndash;|--|g;
  s|\&lang;|\\tag{|g;
  s|\&rang;|}|g;
  s|\&\#x0*5c;|\\|g;
  s|\&rarr;|\\rightarrow{}|g;

  s|<logo>TeX</logo>|\\TeX{}|;
  s|<logo>LaTeX</logo>|\\LaTeX{}|;
  s|<logo>ExTeX</logo>|\\ExTeX{}|;
  s|<logo>eTeX</logo>|\\eTeX{}|;
  s|<logo>Omega</logo>|Omega|;
  s|<tt>\\ </tt>|\\Macro{\\[}|g;
  s|<tt>\\([^<]*)</tt>|\\Macro{$1}|g;
  s|<tt>{</tt>|\\texttt{\\char\`\\{}|g;
  s|<tt>}</tt>|\\texttt{\\char\`\\}}|g;
  s|<tt>([^<]*)</tt>|\\texttt{$1}|g;
  s|<i>([^<]*)</i>|\\textit{$1}|g;
  s|<b>([^<]*)</b>|\\emph{$1}|g;
  s|<strong>|\\emph{|g;
  s|</strong>|}|g;
  s|<emph>([^<]*)</emph>|\\emph{$1}|g;
  s|<em>([^<]*)</em>|\\emph{$1}|g;
  s|<sub>([^<]*)</sub>|\\ensuremath{_{$1}}|g;
  s|<sup>([^<]*)</sup>|\\ensuremath{^{$1}}|g;

  return $_;
}

#------------------------------------------------------------------------------
# Function:	processDocTag
#
sub processDocTag {
  my ($name, $fd, $s) = @_;
  local $_;

  while (<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if(m|</doc>|) {
      last;
    }

    if ( m/ TODO / ) {
      $s .= "\n\n\\Incomplete\n\n";
      next;
    }

    $_ = translate($_);

    s|<h3>(.*)</h3>\s*|\\section{$1}|;
    s|<h4>(.*)</h4>|\\subsection{$1}|;
    s|<p>\s*||g;
    s|<dl>|\\begin{description}|g;
    s|</dl>|\\end{description}|g;
    s|<dt>|\\item[|g;
    s|</dt>|]|g;
    s|<dd>||g;
    s|</dd>||g;
    s|<tt>([^<]*)</tt>|\\texttt{$1}|g;
    s|<table format="(.*)">|\\par\\begin{tabular}{$1}\\toprule|g;
    s|</table>|\\bottomrule\\end{tabular}\\par|g;
    s|<tr>||g;
    s|</td></tr>|\\\\|g;
    s|</tr>|\\\\|g;
    s|<td>||g;
    s|</td>|\&|g;
    s|<ul>|\\begin{itemize}|g;
    s|</ul>|\\end{itemize}|g;
    s|<li>|\\item |g;
    s|</li>||g;
    s|<p class="TeXbook">\s*|\\|g;
    s|</p>\s*|\\par |;
    s|<br[ /]*>|\\ |;
    if (m/\s*<pre\s+class="syntax">/) {
      $s .= $`;
      my $spec = '\\begin{syntax}';
      while(<$fd>) {
	$_ = translate($_) ;
	next if m/^\s*$/;
	
	s|^(\s*)\\rightarrow{}|$1\\SyntaxDef|;
	s/^(\s*)\|/$1\\SyntaxOr/;
	s/\[([a-z \<=\>\&;]*)\]/[\\texttt{$1}]/;
	
	if (m|</pre>|) {
	    $spec .= $`;
	    last;
	  }
	$spec .= $_;
      }
      $_ = $spec;
#	s|\@linkplain\s+\\([^)]+\\)\s+||sg;
#	s|\@linkplain\s+[^()]+\s+||sg;
      s|\@linkplain\s+\S+\s+||sg;
      s|\@link\s+\w+\\([^)]+\\)\s+||sg;
      s|\@link\s+[^()]+\s+||sg;
      s/\n/\t\\\\\n/mg;
      $_ .= "\n\\end{syntax}\n";

    } elsif (m/\s*<pre\s+class="JavaSample">/) {
      $s .= $`;
      my $spec = '\\begin{lstlisting}{language=Java}' . $';
      while(<$fd>) {
	chomp;
	$_ = $_ . "\n";
	s|^ \* ?||;
#	  s|([~_\%\$])|\\$1|g;
	s|\&\#x0*5c;|\\|g;
	s|\&ndash;\s*|--|g;
	s|</?[bi]>||g;
	
	if (m|</pre>|) {
	  $spec .= $`;
	  last;
	}
	$spec .= $_;
      }
      $_ = $spec;
      $_ .= "\\end{lstlisting}\n";
      
    } elsif (m/\s*<pre\s+class="Sample">/) {
      $s .= $`;
      my $spec = '\\begin{lstlisting}{}' . $';
      while(<$fd>) {
	chomp;
	$_ = $_ . "\n";
	s|^ \* ?||;
#	  s|([~_\%\$])|\\$1|g;
	s|\&\#x0*5c;|\\|g;
	s|\&ndash;\s*|--|g;
	s|</?[bi]>||g;
	
	if (m|</pre>|) {
	  $spec .= $`;
	  last;
	}
	$spec .= $_;
      }
      $_ = $spec;
      $_ .= "\\end{lstlisting}\n";

    } elsif (m/\s*<pre\s+class="CLISample">/) {
      $s .= $`;
      my $spec = '\\begin{lstlisting}{}' . $';
      while(<$fd>) {
	chomp;
	$_ = $_ . "\n";
	s|^ \* ?||;
#	  s|([~_\%\$])|\\$1|g;
	s|\&\#x0*5c;|\\|g;
	s|\&ndash;\s*|--|g;
	s|</?[bi]>||g;
	
	if (m|</pre>|) {
	  $spec .= $`;
	  last;
	}
	$spec .= $_;
      }
      $_ = $spec;
      $_ .= "\\end{lstlisting}\n";

    } elsif (m/\s*<pre\s+class="TeXSample">/) {
      $s .= $`;
      my $spec = '\\begin{lstlisting}{language=TeX}' . $';
      while(<$fd>) {
	chomp;
	$_ = $_ . "\n";
	s|^ \* ?||;
#	  s|([~_\%\$])|\\$1|g;
	s|\&\#x0*5c;|\\|g;
	s|\&ndash;\s*|--|g;
	s|</?[bi]>||g;
	
	if (m|</pre>|) {
	  $spec .= $`;
	  last;
	}
	$spec .= $_;
      }
      $_ = $spec;
      $_ .= "\n\\end{lstlisting}\n";
      
    }

    print STDERR "$name: unprocessed: $& $_\n" if(m|</?[a-z][a-z0-9]*|i);
    s|\&lt;|<|g;
    s|\&gt;|>|g;
    s|\@linkplain\s+\S+\s+||smg;
    s|\@link\s+\S+\s+||smg;
    $s .= $_;
  }

  $_ = $s;
  s|\@linkplain\s+\S+\s+||smg;
  s|\@link\s+\S+\s+||smg;
  s/\\par\s+/\n\n/g;
  s/\n\n\n+/\n\n/g;
  s|\&lt;|<|g;
  s|\&gt;|>|g;

  return $_;
}

1;
##-----------------------------------------------------------------------------
## Local Variables: 
## mode: perl
## End: 
