#!C:\opt\cygwin\bin\perl.exe -w
##*****************************************************************************
## $Id: howto.pl,v 1.1 2005/08/29 07:08:46 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

howto.pl - ...

=head1 SYNOPSIS

howto.pl [-v|--verbose] 

howto.pl [-h|-help]

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
use File::Find;
use Cwd;
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
my $verbose  = 0;

my $classpath = '.';

my $outfile = undef;

use Getopt::Long;
GetOptions("h|help"    => \&usage,
           "cp=s"      => \$classpath,
	   "out=s"	=> \$outfile,
	   "v|verbose" => \$verbose,
	  );

my $out = (defined $outfile ? new FileHandle($outfile, 'w') : \*STDOUT);

collect($classpath);

$out->close() if defined $outfile;


#------------------------------------------------------------------------------
# Function:	collect
# Description:	
# Returns:	
#
sub collect {
  my ($basedir) = @_;
  my $cwd = getcwd();
  chdir $basedir;
  find({no_chdir=>1,
	wanted=>\&analyze}, '.');
  chdir $cwd;
}

#------------------------------------------------------------------------------
# Method:	analyze
#
sub analyze {

  if ( $_ eq 'CVS' ) {
    $File::Find::prune = 1;
  } elsif (m/package\.html$/) {
    analyzeHTML($_);
  } elsif (m/\.java$/) {
#    analyzeHTML($_);
  }
}

sub analyzeHTML {
  my $file = shift;
  local $_;

  my $fd = new FileHandle($file);

  while(<$fd>) {
    if (m/<doc .*type="howto"/) {
      $_ = processDocTag ($file, $fd, '');

      print $out "$_\n";
    }
  }
  $fd->close();
}


#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
