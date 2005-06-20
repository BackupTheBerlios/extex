#!/bin/perl -w
##*****************************************************************************
## $Id: primitives.pl,v 1.4 2005/06/20 10:31:02 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

primitives.pl - ...

=head1 SYNOPSIS

primitives.pl [-v|--verbose] 

primitives.pl [-h|-help]

=head1 DESCRIPTION

=head1 OPTIONS

=head1 AUTHOR

Gerd Neugebauer

=head1 BUGS

=over 4

=item *

...

=back

=cut

use strict;
use FileHandle;

use lib ".";
use lib "./bin";
use xml2tex;

#------------------------------------------------------------------------------
# Function:	usage
# Arguments:	
# Returns:	
# Description:	
#
sub usage
{ use Pod::Text;
  Pod::Text->new()->parse_from_filehandle(new FileHandle($0,'r'),\*STDERR);
}

#------------------------------------------------------------------------------
# Variable:	$verbose
# Description:	
#
my $verbose   = 0;
my $sfile   = undef;
my $pfile     = undef;
my $classpath = '.';

use Getopt::Long;
GetOptions("cp=s"		=> \$classpath,
	   "h|help"		=> \&usage,
	   "sets=s"		=> \$sfile,
	   "primitives=s"	=> \$pfile,
	   "v|verbose"		=> \$verbose,
	  );

my %cfg	      = ();
my %primSets  = ();
my %primClass = ();
my %primitive = ();
my %primUse   = ();
my %cache     = ();

foreach my $file (@ARGV) {
  local $_ = $file;
  s|.*/(.*)\.xml|$1|;
  $cfg{$_} = $file;
  print STDERR "--- Got configuration $_ -> $file" if $verbose;
}

my $out = ($sfile ? new FileHandle($sfile, 'w'): \*STDOUT);

print $out "\n\\subsection{Predefined Configurations}\n";

foreach (sort keys %cfg) {
  processConfig($out, $_, $cfg{$_});
}

print $out "\n\\subsection{Primitive Sets}\n";

foreach (sort keys %primSets) {
  processPrimitives($out, $_, $primSets{$_});
}

$out = ($pfile ? new FileHandle($pfile, 'w'): \*STDOUT);

foreach (sort ignorecase keys %primitive) {
  processPrimitive($out, $_, $primitive{$_});
}



#------------------------------------------------------------------------------
# Function:	ignorecase
#
sub ignorecase {
  lc($a) cmp lc($b)
}

#------------------------------------------------------------------------------
# Function:	processPrimitive
#
sub processPrimitive {
  my ($out, $name, $class) = @_;
  local $_ = ($name eq ' '? '\\[': $name);

  local $_ = $class;
  s|\.|/|g;
  $_ = $classpath . '/' . $_ . '.java';
  if ($class eq 'de.dante.extex.interpreter.primitives.register.count.IntegerParameter') {
    print $out "\n\\subsection*{The Count Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a count register.\n";
  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.skip.SkipParameter') {
    print $out "\n\\subsection*{The Glue Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a skip register.\n";

  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.dimen.DimenParameter') {
    print $out "\n\\subsection*{The Dimen Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a dimen register.\n";

  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.toks.ToksParameter') {
    print $out "\n\\subsection*{The Toks Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a toks register.\n";

  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.font.NumberedFont') {
    print $out "\n\\subsection*{The Font Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a numbered font register.\n";

  } elsif ($cache{$class}) {

  } elsif (-e $_) {
    processClass($out, $name, $_, '');
    $cache{$class} = 1;
  } else {
    print $out "\n\\subsection*{The Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is not implemented yet.\n\n";
  }
  $_ = ($name eq ' '? '\\[': $name);
  print $out "The primitive \\macro{$_} is defined in the set \\texttt{$primUse{$name}}.\n";
}

#------------------------------------------------------------------------------
# Function:	processClass
#
sub processClass {
  my ($out, $name, $file, $s) = @_;
  my $fd = new FileHandle($file, 'r');
  local $_;

  while (<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if (m|<doc.*name="([^\"]*)"|) {
      $s	     = processDocTag($name, $fd, $s);
    }
  }

  $fd->close();
  $_ = $s;

  s|^ ?Examples:\s*$|\\subsubsection*{Examples}\n|g;
  s|\\Macro{ }|\\Macro{\\[}|g;
  s|\\Macro{\\+}|\\Macro{\\char\`\\\\}|g;
  print $out $_;
}


#------------------------------------------------------------------------------
# Function:	processPrimitives
#
sub processPrimitives {
  my ($out, $name, $file) = @_;
  my %prim = ();
  local $_;
  print $out <<__EOF__;
\\subsubsection{The Primitive Set \\texttt{$name}}

The primitive set \\texttt{$name} defines the following primitives:
\\begin{primitives}
__EOF__
  my $fd = new FileHandle($file,'r') || die "$file:$!\n";
  while (<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if (not m/>/) {
      $_ .= <$fd>;
      chomp;
      $_ = $_ . "\n";
    }
    if (m|<[dD]efine name="([^\"]*)"\s+class="([^\"]*)"|m) {
      $prim{$1}	     = $2;
      $primitive{$1} = $2;
      $primClass{$2} = $1;
      $primUse{$1}   = $name;
    }
  }
  $fd->close();
  foreach (sort ignorecase keys %prim) {
    s|\\\\|\\char`\\\\|g; #`
    #s| |\\ {}|g; #`
    $_ = "\\[" if $_ eq ' ';
    print $out "   \\macro{$_}\n";
  }
  print $out "\\end{primitives}\n";
}

#------------------------------------------------------------------------------
# Function:	processConfig
#
sub processConfig {
  my ($out, $name, $file) = @_;
  my $banner;
  local $_;
  my $extReg = undef;
  my @prim   = ();
  my $dir    = $file;
  $dir	     =~ s|/[^/]*$||;
  my $fd   = new FileHandle($file,'r');
  while (<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if (m|<banner>(.*)</banner>|) {
      $banner = $1;
    } elsif (m|<ExtendedRegisterNames>(.*)</ExtendedRegisterNames>|) {
      $extReg = ($1 eq "true");
    } elsif (m|<primitives src="(.*)"|) {
      my $p = $1;
      $p =~ m|.*/(.*)\.xml|;
      $primSets{$1} = "$dir/$p";
      push @prim, $1;
    }
  }
  $fd->close();

  print $out <<__EOF__;

\\subsubsection{The Configuration \\texttt{$name}}\\index{$name}

The configuration \\texttt{$name} identifies itself as
``$banner''.
__EOF__

  $_ = @prim;
  if ($_ == 0) {
    print $out "The configuration contains no primitive sets.";
  } elsif ($_ == 1) {
    print $out "The configuration contains the primitive set \\texttt{$prim[0]}.\n";
  } elsif ($_ == 2) {
    print $out "The configuration contains the primitive sets \\texttt{$prim[0]} and \\texttt{$prim[1]}.\n";
  } else {
    print $out "The configuration contains the primitive sets ";
    my $plast = pop @prim;
    foreach (@prim) {
      print $out "\\texttt{$_}, ";
    }
    print $out " and \\texttt{$plast}.\n";
  }
  if ($extReg) {
    print $out "The configuration allows extended register names.\n";
  }
}

#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
