#!/bin/perl.exe -w
##*****************************************************************************
## $Id: unit-summary.pl,v 1.1 2006/05/08 15:59:13 gene Exp $
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

my %cache = ('' => 1);
my @units;
my @primitives;
my @configurations;
my $targetdir  = dirname($0) . "/../target/www/unit";
mkdir $targetdir if not -e $targetdir;

chdir dirname($0);

foreach $_ (glob 'unit-summary/*') {
  my $f = $targetdir."/".basename($_);
  copy($_,$f) if not m/~$/;
}

foreach $_ (glob '../src/java/config/unit/*.xml') {
  print STDERR "--- $_\n" if $verbose;
  process_unit($_);
}

foreach $_ (glob '../src/java/config/*.xml') {
  print STDERR "--- $_\n" if $verbose;
  process_cfg($_);
}

my $out = HTML_start("$targetdir/overview.html",
		     'Overview',
		     'navi.css', <<__EOF__);
<h1>ExTeX</h1>
<a href="units.html" target="unitFrame">All Units</a>
<p>Units</p>
__EOF__

foreach $_ (sort @units) {
  print $out "<a href=\"unit-$_.html\" target=\"unitFrame\">$_</a><br />";
}

print $out <<__EOF__;
<p>Configurations</p>
__EOF__

foreach $_ (sort {return lc($$a[0]) cmp lc($$b[0])} @configurations) {
  print $out "<a href=\"cfg_$$_[0].html\" target=\"infoFrame\">$$_[0]</a><br />";
}

HTML_end($out);

$out = HTML_start("$targetdir/units.html",
		  'All Units',
		  'navi.css', <<__EOF__);
<h2>All Units</h2>
__EOF__

foreach $_ (sort {return lc($$a[0]) cmp lc($$b[0])} @primitives) {
  my @a = @$_;
  if ($a[3] eq '') {
    print $out "<a href=\"$a[2]\" target=\"infoFrame\">\\$a[0]</a><br />";
  } else {
    print $out "<span class=\"undef\">\\$a[0]</span><br />";
  }
}

HTML_end($out);


#------------------------------------------------------------------------------
#
sub process_cfg {
  my $file   = shift;
  my $cfg    = basename($file, ".xml");
  my $ok     = 0;
  my $banner = '';
  my @u;
  my $fd     = new FileHandle($file, 'r') || die "$file: $!\n";;

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

  $fd = HTML_start("$targetdir/cfg_$cfg.html", "The Configuration $cfg", 'info.css', <<__EOF__);
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

  push @units, $unit;

  my $fd = new FileHandle($file, 'r') || die "$file: $!\n";;

  while (<$fd>) {
    $def = "" if m/<define/;
    $def = "n" if m/<Define/;
    $name = $1 if m/fine name="([a-z]*)"/i;
    $class = $1 if m/class="([^\"]*)"/;

    if (m/>/ and defined $name) {
      my $s  = $class;
      $s     =~ s/.*primitives.//;
      $class = '' if $def ne '';
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
		   "<h2>Unit $unit</h2>\n");

  foreach $_ (sort {return lc($$a[0]) cmp lc($$b[0])} @prim) {
    my @a = @$_;
    if ($a[3] eq '') {
      print $fd "<a href=\"$a[2]\" target=\"infoFrame\">\\$a[0]</a><br />";
    } else {
      print $fd "<span class=\"undef\">\\$a[0]</span><br />";
    }
  }

  HTML_end($fd);

  foreach $_ (@prim) {
    my @a = @$_;
    next if defined $cache{$a[2]};
    $cache{$a[2]} = 1;
    doit(@$_);
  }
}

#------------------------------------------------------------------------------
#
sub doit {
  my ($name,$class,$short,$def,$unit) = @_;
  my $doc  = '';
  if ($class eq '') {
    $doc = "<h2>The Primitive \\$name</h2>";
  } else {
    local $_ = $class;
    s|\.|/|g;
    my $java = "../src/java/$_.java";
    my $fd   = new FileHandle($java, 'r') || die "$java: $!\n";
    while(<$fd>) {
      if (m/<doc/) {
	while(<$fd>) {
	  last if m|</doc|;
	  s/^ *\* ?//;
	  s///;
	  s|TODO (.*)|<p><img src="construct.gif" alt="$1" title="$1"/></p>|;
	  $doc  .= $_;
        }
      }
    }
    $fd->close();
  }

  if ($doc eq '') {
    $doc = <<__EOF__;
<h3>The Primitive \\$name</h3>
<p><img src="construct.gif" alt="missing" title="missing"/></p>
__EOF__
  } else {
    $doc =~ s|\{[@]linkplain\s*\S+\s*([^\}]+)\}|$1|gsm;
    $doc =~ s|\{[@]link\s*\S+\s*([^\}]+)\}|$1|gsm;
  }

  my $fd = HTML_start("$targetdir/$short",
		      "Primitive \\$name",
		      'info.css', <<__EOF__);
<div class="unit">From unit $unit</div>
<hr />
$doc
<hr />
<div class="footer">Submit
 a <a href="https://developer.berlios.de/bugs/?group_id=1915">bug report</a>
 or a <a href="https://developer.berlios.de/feature/?group_id=1915">feature request</a>.
</div>
__EOF__
  HTML_end($fd);
}

#------------------------------------------------------------------------------
#
sub HTML_start {
  my $file  = shift;
  my $title = shift;
  my $style = shift;
  my $fd    = new FileHandle($file, 'w') || die "$file: $!\n";;

  print $fd <<__EOF__;
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML//3.2//EN">
<html>
<head>
  <title>$title</title>
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
