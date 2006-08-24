#!/bin/perl.exe -w
##*****************************************************************************
## $Id: make-test-summary.pl,v 1.2 2006/08/24 07:34:06 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

make-test-summary.pl - ...

=head1 SYNOPSIS

make-test-summary.pl [-v|--verbose] 

make-test-summary.pl [-h|-help]

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
use File::Basename;
use FileHandle;

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

my $target = dirname($0) . "/../target/www/reports/test-summary.html";
#my $target = '';

#------------------------------------------------------------------------------
# Variable:	$verbose
# Description:	
#
my $verbose = 0;

#------------------------------------------------------------------------------
# Width of the bar graph
my $w 	 = 512;

use Getopt::Long;
GetOptions("h|help"	=> \&usage,
	   "output=s"   => \$target,
	   "v|verbose"	=> \$verbose,
	  );

local $_;
my $no	 = 'undefined';
my $err	 = 'undefined';
my $fail = 'undefined';
my $succ = 'undefined';
my $g	 = 0;
my $o	 = 0;
my $r 	 = $w;

{ local $/=undef;
  my $file = dirname($0) . "/../target/www/reports/tests/overview-summary.html";
  my $fd = new FileHandle($file, 'r');
  if (defined $fd) {
    $_ = <$fd>;
    $fd->close();

    m|<td><a href=\"all-tests.html\"[^>]*\">([0-9]+)</a></td><td><a href=\"alltests-fails.html\"[^>]*>([0-9]+)</a></td><td><a href=\"alltests-errors.html\"[^>]*>([0-9]+)</a></td><td>([0-9.]+)%</td><td>([0-9.]+)</td>|;
    $no   = $1;
    $err  = $3;
    $fail = $2;
    $succ = $4;
    $g    = int($w*($no-$err-$fail)/$no);
    $o    = int($w*$fail/$no);
    $r    = int($w*$err/$no);
  } else {
    print STDERR <<__EOF__
*** $file not found.
*** Run
***	../build junitreport
*** to generate it.
__EOF__
  }
}

my $out = ($target eq '' ? \*STDERR : new FileHandle($target, 'w'));

print $out <<__EOF__;
<div>Number of tests: $no</div>
<div>Success rate: $succ\%</div>
<div>
__EOF__

print $out "<img src=\"../image/s-gray.png\" width=\"1\" height=\"24\"\n/>";
print $out "<img src=\"../image/s-green.png\" width=\"$g\" height=\"24\"\n/>"
    if $g > 0;
print $out "<img src=\"../image/s-orange.png\" width=\"$o\" height=\"24\"\n/>"
    if $o > 0;
print $out "<img src=\"../image/s-red.png\" width=\"$r\" height=\"24\"\n/>"
    if $r > 0;
print $out "<img src=\"../image/s-gray.png\" width=\"1\" height=\"24\"\n/>";

print $out <<__EOF__;
</div>
__EOF__

close $out if $out != \*STDOUT;

#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
