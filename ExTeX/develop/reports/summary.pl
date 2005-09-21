#!C:\opt\cygwin\bin\perl.exe -w
##*****************************************************************************
## $Id: summary.pl,v 1.1 2005/09/21 07:15:02 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

summary.pl - ...

=head1 SYNOPSIS

summary.pl [-v|--verbose] 

summary.pl [-h|-help]

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
my $verbose = 0;

my $outfile = "index.xml";

my $dir = ".";

use Getopt::Long;
GetOptions("h|help"	=> \&usage,
	   "outfile=s"	=> \$outfile,
	   "dir=s"	=> \$dir,
	   "v|verbose"	=> \$verbose,
	  );

chdir $dir;
@_ = glob("*");
print @_;


#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
