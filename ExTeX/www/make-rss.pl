#!/bin/perl.exe -w
##*****************************************************************************
## $Id: make-rss.pl,v 1.2 2006/06/15 19:51:39 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

make-rss.pl - ...

=head1 SYNOPSIS

make-rss.pl [-v|--verbose] 

make-rss.pl [-h|-help]

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

my $target = dirname($0) . "/../target/www/rss/2.0/ExTeX.rss";
my $MAX    = 8;

#------------------------------------------------------------------------------
# Variable:	$verbose
# Description:	
#
my $verbose = 0;

use Getopt::Long;
GetOptions("h|help"	=> \&usage,
	   "max=n"      => \$MAX,
	   "output=s"   => \$target,
	   "v|verbose"	=> \$verbose,
	  );

mkdir dirname($target);

my $out     = ($target eq '' ? \*STDERR : new FileHandle($target, 'w'));
my $gendate = formatDate(localtime);
@_ = localtime;
my $year = $_[] + 1900;

print $out <<__EOF__;
<?xml version="1.0"?>
<rss version="2.0">
  <channel>
    <title>ExTeX News</title>
    <link>http://www.extex.org/</link>
    <description>ExTeX - Typesetting for the 21st Century</description>
    <language>en-us</language>
    <copyright>(c) $year The ExTeX Group</copyright>
    <pubDate>$gendate</pubDate>
    <lastBuildDate>$gendate</lastBuildDate>
    <docs>http://blogs.law.harvard.edu/tech/rss</docs>
    <generator>ExTeX Site Builder</generator>
    <managingEditor>gene\@gerd-neugebauer.de</managingEditor>
    <webMaster>gene\@gerd-neugebauer.de</webMaster>
    <ttl>1440</ttl>
    <image>
      <url>http://www.extex.org/rss/ExTeX.png</url>
      <title>ExTeX News</title>
      <link>http://www.extex.org/</link>
    </image>
__EOF__

  my $no = 0;
  foreach my $f (reverse sort glob(dirname($0)."/src/news/*.xml") ) {
    last if $no++ >= $MAX;
    include($no, $f);
  }

print $out <<__EOF__;
  </channel>
</rss>
__EOF__

close $out if $out != \*STDOUT;


#------------------------------------------------------------------------------
#
sub formatDate {
  
  return sprintf("%s, %s %s %s %02d:%02d:%02d GMT",
                 ('Sun','Mon','Tue','Wed','Thu','Fri','Sat')[$_[6]],
                 $_[3],
                 ('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec')[$_[4]],
                 $_[5] + 1900,
                 $_[2],
                 $_[1],
                 $_[0]
                );
}

#------------------------------------------------------------------------------
#
sub include {
  my $no   = shift;
  my $file = shift;
  local $/ = undef;
  my $fd   = new FileHandle($file, 'r') || die $!;
  local $_ = <$fd>;
  s|.*||;
  s|\015||g;
  s|<!\[CDATA\[||g;
  s|\]\]>||g;
  s|<description>|$&<![CDATA[|g;
  s|</description>|]]>$&|g;
  print $out $_;
}

#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
