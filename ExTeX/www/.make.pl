#!C:\usr\local\share\cygwin\bin\perl.exe -w
##*****************************************************************************
## $Id: .make.pl,v 1.7 2006/05/01 09:09:21 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

make.pl - ...

=head1 SYNOPSIS

make.pl [-v|--verbose] 

make.pl [-h|-help]

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

#------------------------------------------------------------------------------
# Variable:	$trace
# Description:	
#
my $trace = 0;

#------------------------------------------------------------------------------
# Variable:	$srcdir
# Description:	
#
my $srcdir = "src";

#------------------------------------------------------------------------------
# Variable:	$destdir
# Description:	
#
my $destdir = "www";

#------------------------------------------------------------------------------
# Variable:	...
# Description:	
#
my ($sec,$min,$hour,$day,$month,$year,$wday,$yday,$isdst) = localtime(time);
$year  += 1900;
$month += 1;

my $force = undef;

use Getopt::Long;
GetOptions("h|help"	=> \&usage,
	   "v|verbose"  => \$verbose,
	   "trace"      => \$trace,
	   "force"      => \$force,
	   "src=s"	=> \$srcdir,
	   "destdir=s"  => \$destdir,
	  );
use File::Basename;
use File::Find;
use File::Copy;
use FileHandle;

$srcdir =~ s|/*$||;

die "Missing source dir `$srcdir'" if ! -d $srcdir;

mkdir $destdir if ! -d $destdir;

my $srclen = length($srcdir)+1;

my @dependencies = ();

my $DEPEND = undef;

my $dep;
if ( $DEPEND ) {
  $dep= new FileHandle(".dependencies","w");
  print $dep "my %depend = {\n";
}

find( {wanted   => \&process,
       no_chdir => 1,
      },
      $srcdir );

if ( $DEPEND ) {
  print $dep "};\n";
  $dep->close();
}


#------------------------------------------------------------------------------
# Function:	process
#
sub process
{ 
  return if m/~$/;
  return if m/\.bak$/;
  return if $_ eq $srcdir;
  
  my $name = substr($_,$srclen);

  if ( $name =~ m/^\./ or $name =~ m|/\.| ) {
  } elsif ( $name =~ m:CVS$:  ) {
    $File::Find::prune = 1;
  } elsif ( -d $_ ) {
    if ( ! -e "$destdir/$name" ) {
      print STDERR "Creating $name\n" if $verbose;
      mkdir("$destdir/$name");
    }
  } elsif ( $name =~ m/\.html/ ) {
    print STDERR "Processing $name\n" if $verbose;
    my $top = $name;
    $top    =~ s|[^/]+|..|g;
    $top    =~ s|\.\.$||;
    my $out = new FileHandle("$destdir/$name","w");
    includeFile(dirname($_), basename($_), $out, $top );
    $out->close();
    writeDependency($destdir,$name);
  } elsif ( $force or not uptodate("$destdir/$name",$_) ) {
    print STDERR "Copying $name\n" if $verbose;
    copy($_,"$destdir/$name");
  }
}

#------------------------------------------------------------------------------
# Function:	uptodate
#
sub uptodate
{ my $file1 = shift;
  local $_;
  return undef if not -e $file1;

  my @s1 = stat($file1);

  foreach $_ (@_) {
    my @s = stat($_);
    return undef if ($s1[9] < $s[9]);
  }
  
  return 1;
}

#------------------------------------------------------------------------------
# Function:	writeDependency
#
sub writeDependency
{ my ($dir,$name) = @_;

  return if not $DEPEND;

  local $_ = "$dir/$name";
  s/\\/\\\\/g;
  s/'/\\'/g;
  print $dep "\t'$_' => [\n";
  foreach $_ (@dependencies) {
    s/\\/\\\\/g;
    s/'/\\'/g;
    print $dep "\t\t\t'$_',\n";
  }
  print $dep "\t        ],\n";
  @dependencies = ();
}

#------------------------------------------------------------------------------
# Function:	findFile
#
sub findFile
{ my ($dir,$file) = @_;
  my $d	   = "$dir/$file";
  local $_ = $d;
  do {
    print STDERR "\t$_\n" if $trace;
    if ( -e $_ ) {
      push @dependencies,$_;
      return new FileHandle($_,"r");
    }
    $dir = dirname($dir);
    $_ = "$dir/$file";
  } while ($dir ne ".");

  print STDERR "\t$_\n" if $trace;
  if ( -e $_ ) {
    push @dependencies,$_;
    return new FileHandle($_,"r");
  }
  
  die "$d: No such file\n";
}

#------------------------------------------------------------------------------
# Function:	includeFile
#
sub includeFile 
{ my ($fromdir,$from,$out,$top) = @_;
  local $_;
  print STDERR "Including $fromdir/$from\n" if $trace;
  my $in = findFile($fromdir,$from);

  while ( <$in> ) {
    s|\&top;|$top|g;
    s|\&year;|$year|g;
    s|\&month;|$month|g;
    s|\&day;|$day|g;
#    s|\&eTeX;|&epsilon;-T<span style="vertical-align:-20%;">E</span>X|g;
#    s|\&pdfTeX;|pdfT<span style="vertical-align:-20%;">E</span>X|g;
#    s|\&TeX;|T<span style="vertical-align:-20%;">E</span>X|g;
    s|\&eTeX;|&epsilon;-TeX|g;
    s|\&pdfTeX;|pdfTeX|g;
    s|\&TeX;|TeX|g;
    s|\&LaTeX;|LaTeX|g;
    s|\&ExTeX;|ExTeX|g;
    if ( m|</head>|i ) {
      includeFile($fromdir,".headEnd",$out,$top,$fromdir);
      print $out $_;
    } elsif ( m|<body>|i ) {
      print $out $_;
      includeFile($fromdir,".bodyStart",$out,$top,$fromdir);
    } elsif ( m|</body>|i ) {
      includeFile($fromdir,".bodyEnd",$out,$top,$fromdir);
      print $out $_;
    } elsif ( m|<navigation[ ]*/>|i ) {
      includeFile($fromdir,".navigation",$out,$top,$fromdir);
    } elsif ( m|<info[ ]*/>|i ) {
      includeFile($fromdir,".info",$out,$top,$fromdir);
    } elsif ( m|<tabs[ ]*/>|i ) {
      includeFile($fromdir,".tabs",$out,$top,$fromdir);
    } elsif ( m|<directory[ ]*/>|i ) {

      print $out "  <table>\n";
      my @files = glob("$fromdir/*");
      foreach $_ (@files)
      { next if m|/CVS$|;
	next if m|/index.html$|;
	next if m|/~$|;
	print "$_\n";
#	my ($dev,$ino,$mode,$nlink,$uid,$gid,$rdev,$size,
#            $atime,$mtime,$ctime,$blksize,$blocks)
#           = stat($_);
	s|.*/||;
        print $out "    <tr>\n      <td><a href=\"$_\">$_</a></td>\n    </tr>\n";
      }
      print $out "  </table>\n";

    } else {
      chomp;
      print $out "$_\n";
    }
  }

  $in->close();
}

#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
