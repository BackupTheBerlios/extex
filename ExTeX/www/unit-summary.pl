#!/bin/perl.exe -w
##*****************************************************************************
## $Id: unit-summary.pl,v 1.2 2006/05/19 15:17:25 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

unit-summary.pl - ...

=head1 SYNOPSIS

unit-summary.pl [-v|--verbose] 

unit-summary.pl [-h|-help]

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
use File::Copy;
use File::Find
;
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

#------------------------------------------------------------------------------
# Variable:	$verbose
# Description:	
#
my $verbose = 0;

use Getopt::Long;
GetOptions("h|help"	=> \&usage,
	   "v|verbose"	=> \$verbose,
	  );

my $BASEDIR = dirname($0) . '/../';

my %info;
my %infoAuthor;
my %infoIncomplete = ( '' => 1 );

my @units;
my @primitives;
my @configurations;
my $targetdir  = $BASEDIR . "target/www/unit";

mkdir $targetdir if not -e $targetdir;

chdir dirname($0);

find({no_chdir => 1, wanted => \&analyze}, $BASEDIR . 'src/java');
print "\n" if $verbose;

foreach $_ (glob 'unit-summary/*') {
  next if m/CVS/;
  my $f = $targetdir . "/" . basename($_);
  copy($_, $f) if not m/~$/;
}

foreach $_ (glob $BASEDIR . 'src/java/config/unit/*.xml') {
  process_unit($_);
}

foreach $_ (glob $BASEDIR . 'src/java/config/*.xml') {
  process_cfg($_);
}

make_overview();
make_units();

#------------------------------------------------------------------------------
#
sub make_overview {

  my $out = HTML_start("$targetdir/overview.html",
		       'Overview',
		       'navi.css',
		       '', <<__EOF__);
<h1>ExTeX</h1>
<a href="units.html" target="unitFrame">All Units</a>
<p>Units</p>
__EOF__

  foreach $_ (sort @units) {
    print $out "<a href=\"unit-$_.html\" target=\"unitFrame\">$_</a><br />\n";
  }

  print $out <<__EOF__;
<p>Configurations</p>
__EOF__

  foreach $_ (sort {return lc($$a[0]) cmp lc($$b[0])} @configurations) {
    print $out "<a href=\"cfg_$$_[0].html\" target=\"infoFrame\">$$_[0]</a><br />\n";
  }

  HTML_end($out);
}

#------------------------------------------------------------------------------
#
sub make_units {
  my $out = HTML_start("$targetdir/units.html",
		       'All Units',
		       'navi.css',
		       '', <<__EOF__);
<h2>All Units</h2>
__EOF__

  foreach $_ (sort {return lc($$a[0]) cmp lc($$b[0])} @primitives) {
    my @a = @$_;
    if ($a[3] eq '') {
      print $out "<a href=\"$a[2]\" target=\"infoFrame\">\\$a[0]</a> ($a[4])<br />\n";
    } else {
      print $out "<span class=\"undef\">\\$a[0] ($a[4])</span><br />\n";
    }
  }
  
  HTML_end($out);
}

#------------------------------------------------------------------------------
# Function:     analyze
#
sub analyze {
  my $file = $_;
  if ( $_ eq 'CVS' ) {
    $File::Find::prune = 1;
    return
  } 
  return if not $_ =~ m/\.(java|html)$/;
#  $noFiles++;
#  print STDERR '.' if $verbose;

  my $class = basename($_,'.java');
  my $fd    = new FileHandle($_, 'r') || die "$_: $!\n";
  s|^./||;
  s:\.(java|html)$::;
  s|/|.|g;
  my $base     = $_;
  my $inDoc    = undef;
  my $type     = "";
  my $name     = "";
  my $contents = "";
  my $package  = "";
  my $author   = '';
  local $_;

  while(<$fd>) {
    if (m/package (.*);/ ) { $package = $1; }
    elsif ( $inDoc ) {
      if (m/\<\/doc\>/) {
	$info{"$type:$name"} = (exists $info{"$type:$name"} ? '?' : $contents);
	$info{"$name"} = (exists $info{"$name"} ? '?' : $contents);
        $info{"$package.$class:$type:$name"} = $contents;
        $info{"$package.$class:$type"} = $contents;
        $info{"$package.$class:$name"} = $contents;
        $inDoc                = undef;
        $type		      = "";
        $name		      = "";
        $contents	      = "";
      } else {
        s/^\s*\* ?//;
        $contents  .= $_;
      }
    } elsif (m/\<doc(.*)\>/) {
      $_        = $1;
 #     $noDocs++;
      if (m/name="([^"]*)"/) { $name = $1; }
      if (m/type="([^"]*)"/) { $type = $1; }
      if ( $name eq '' ) { print STDERR "\n$file: Missing name\n"; }
      $inDoc = 1;
    } elsif (m/\@author\s+(.*)/) {
      $_ = $1;
      s/^[^>]*>//;
      s/<.*//;
      $author .= '; ' if $author ne '';
      $author .= $_;
    } elsif (m/TODO.*unimplemented/) {
      $infoIncomplete{"$package.$class"} = 1;
    }
  }

  $infoAuthor{"$package.$class"} = $author;
  
  $fd->close();
}

#------------------------------------------------------------------------------
#
sub process_cfg {
  my $file   = shift;
  my $cfg    = basename($file, ".xml");
  my $ok     = 0;
  my $banner = '';
  my @u;
  print STDERR "--- Processing configuration $cfg\n" if $verbose;
  my $fd = new FileHandle($file, 'r') || die "$file: $!\n";;

  while (<$fd>) {
    if (m/<ExTeX/) {
      $ok = 1;
    } elsif (m|<banner>(.*)</banner>|) {
      $banner		= $1;
    } elsif (m|<unit src="unit/(.*)\.xml"|) {
      push @u, $1;
    }
  }
  $fd->close();

  return if not $ok;

  push @configurations, [$cfg, $banner, @u];

  $fd = HTML_start("$targetdir/cfg_$cfg.html",
		   "The Configuration $cfg",
		   'info.css',
		   '', <<__EOF__);
<h1>The Configuration <tt>$cfg</tt></h1>
<p>$banner</p>
<h2>Units</h2>
<ul>
__EOF__

  foreach $_ (@u) {
    print $fd "<li><a href=\"unit-$_.html\" target=\"unitFrame\">$_</a></li>\n";
  }
  print $fd "</ul>\n";
  HTML_end($fd);
}

#------------------------------------------------------------------------------
#
sub process_unit {
  my $file = shift;
  my $unit = basename($file, ".xml");
  local $_;
  my $name;
  my $class = '';
  my $def;
  my @prim;
  my $see = undef;

  print STDERR "--- Processing unit $unit\n" if $verbose;

  push @units, $unit;

  my $fd = new FileHandle($file, 'r') || die "$file: $!\n";;

  while (<$fd>) {
    $see   = $1 if m/<unit.*src=\"([^"]*)\"/; #"
    $def   = "" if m/<define/;
    $def   = "n" if m/<Define/;
    $name  = $1 if m/fine name="([a-z]*)"/i;
    $class = $1 if m/class="([^\"]*)"/;

    if (m/>/ and defined $name) {
      $class = '' if $def ne '';
      my $s  = $class;
      $s     =~ s/.*primitives.//;
      if (not defined $info{"$class:$name"}) {
	$s .= "_$name";
      }
      push @prim, [$name, $class, $s, $def, $unit];
      push @primitives, [$name, $class, $s, $def, $unit];
      undef $name;
      $class = '';
    }
  }
  $fd->close();
  $fd = HTML_start("$targetdir/unit-$unit.html",
		   "Unit $unit",
		   'navi.css',
		   '',
		   "<h2>Unit $unit</h2>\n");

  foreach $_ (sort {return lc($$a[0]) cmp lc($$b[0])} @prim) {
    my @a = @$_;
    if ($a[3] eq '') {
      print $fd "<a href=\"$a[2]\" target=\"infoFrame\">\\$a[0]</a><br />\n";
    } else {
      print $fd "<span class=\"undef\">\\$a[0]</span><br />\n";
    }
  }

  if (defined $see) {
    print $fd <<__EOF__
<p>
 Further primitives are imported from
 <a href="unit-$see.html" target="unitFrame">$see</a>.
</p>
__EOF__
  }

  HTML_end($fd);

  foreach $_ (@prim) {
    doit(@$_);
  }
}

#------------------------------------------------------------------------------
#
sub doit {
  my ($name,$class,$short,$def,$unit) = @_;
  my $doc    = '';
  my $author = '';
  my $warn   = '';
  local $_;

  return if $class eq '';

  $doc = $info{"$class:$name"};
  foreach $_ ("$class:", "register:", "parameter:", ":", "") {
    $doc = $info{"$_$name"};
    last if defined $doc and $doc ne '?';
  }
  if (not defined $doc) {
    $doc = '';
    print STDERR "*** $name undocumented\n" if $verbose;
  } elsif($doc eq '?') {
    $doc = '';
    print STDERR "*** $name ambiguous\n" if $verbose;
  }

  $warn = "<p><img src=\"unimplemented.png\"></p>\n"
      if $infoIncomplete{$class};

  if ($doc eq '') {
    $warn = "<p><img src=\"unimplemented.png\"></p>\n";
    $doc     = <<__EOF__;
<h3>The Primitive \\$name</h3>
<p><img src="construct.gif" alt="missing" title="missing"/></p>
__EOF__
  } else {
    $doc  =~ s|TODO (.*)|<p><img src="construct.gif" alt="$1" title="$1"/></p>|g;
    $doc =~ s|\{[@]linkplain\s*[^{}]+\)\s+([^\}]+)\}|$1|gsm;
    $doc =~ s|\{[@]link\s*[^{}]+\)\s+([^\}]+)\}|$1|gsm;
  }

  my @d    = localtime;
  my $date = $d[5] + 1900;
  my $fd   = HTML_start("$targetdir/$short",
			"Primitive \\$name",
			'info.css',
			$author, <<__EOF__);
<div class="unit">From unit $unit</div>
<hr />
$warn$doc
<hr />
<div class="footer">Submit
 a <a href="https://developer.berlios.de/bugs/?group_id=1915">bug report</a>
 or a <a href="https://developer.berlios.de/feature/?group_id=1915">feature request</a>.
</div>
<div class="copyright">Copyright &copy; $date The ExTeX Group;
 Use is subject to <a href="fdl.html">license terms</a>.
</div>
__EOF__
  HTML_end($fd);
}

#------------------------------------------------------------------------------
#
sub HTML_start {
  my $file   = shift;
  my $title  = shift;
  my $style  = shift;
  my $author = shift;
  my $fd     = new FileHandle($file, 'w') || die "$file: $!\n";;

  $author = "\n  <meta name=\"Author\" content=\"$author\">" if $author ne '';

  print $fd <<__EOF__;
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML//3.2//EN">
<html>
<head>
  <title>$title</title>$author
  <link rel="stylesheet" href="$style">
</head>
<body>
__EOF__
  local $_;
  while (defined($_=shift)) {
    print $fd $_;
  }
  return $fd;
}

#------------------------------------------------------------------------------
#
sub HTML_end {
  my $fd = shift;
  local $_;
  while (defined($_=shift)) {
    print $fd $_;
  }

  print $fd <<__EOF__;
</body>
</html>
__EOF__

  $fd->close();
}

#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
