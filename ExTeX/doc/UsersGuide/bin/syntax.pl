#!C:\opt\cygwin\bin\perl.exe -w
##*****************************************************************************
## $Id: syntax.pl,v 1.3 2005/06/20 11:18:11 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

syntax.pl - ...

=head1 SYNOPSIS

syntax.pl [-v|--verbose] 

syntax.pl [-h|-help]

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

use lib ".";
use lib "bin";
use Info;

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
my $classpath = '.';
my $outfile   = undef;

use Getopt::Long;
GetOptions("cp=s"	=> \$classpath,
	   "h|help"	=> \&usage,
	   "out=s"	=> \$outfile,
	   "v|verbose"	=> \$verbose,
	  );

my $info = new Info($classpath);

my $fd 	 = (defined $outfile ? new FileHandle($outfile, 'w') : \*STDOUT);
$info->dump($fd, 'syntax',  "\n\\subsection*{The Syntactic Entity \\protect\\Tag{%name%}}\n\n");
if (defined $outfile) {
  $fd->close();
}




#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
