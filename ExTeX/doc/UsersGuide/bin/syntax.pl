#!C:\opt\cygwin\bin\perl.exe -w
##*****************************************************************************
## $Id: syntax.pl,v 1.1 2005/06/19 13:11:44 gene Exp $
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
$info->dump($fd, 'syntax');
if (defined $outfile) {
  $fd->close();
}


#------------------------------------------------------------------------------
package InfoItem;


#------------------------------------------------------------------------------
# Function:	new
# Description:	
# Returns:	
#
sub new
{ my ($class, $spec, $package, $val) = @_;
  my $name = ($spec =~ m|name="([^\"]*)"| ? $1: '');
  my $type = ($spec =~ m|type="([^\"]*)"| ? $1: '');
  $val 	   =~ s|\@linkplain\s+\S+\s+||smg;
  $val 	   =~ s|\@link\s+\S+\s+||smg;
  my $self = { 'name'    => $name,
	       'type'    => $type,
	       'package' => $package,
	       'value'   => $val,
		 };
  return bless $self,$class;
}

sub getKey {
  my $self = shift;
  return $self->{'type'} . ':' . $self->{'name'};
}

sub getName {
  my $self = shift;
  return $self->{'name'};
}

sub getType {
  my $self = shift;
  return $self->{'type'};
}

sub getPackage {
  my $self = shift;
  return $self->{'package'};
}

sub getValue {
  my $self = shift;
  return $self->{'value'};
}

#------------------------------------------------------------------------------
package Info;

use File::Find;
use Cwd;

use lib ".";
use lib "./bin";
use xml2tex;

my %info = ();

#------------------------------------------------------------------------------
# Function:	new
#
sub new
{ my ($class, $basedir) = @_;
  collect($basedir);
  my $self     = { 
		 };
  return bless $self,$class;
}

#------------------------------------------------------------------------------
# Method:	dump
#
sub dump {
  my ($self, $fd, $type) = @_;
  local $_;

  foreach my $in (sort keys %info) {
    $in = $info{$in};
    if ($in->getType() eq $type) {
      print $fd "\n\\subsection*{The Syntactic Entity \\protect\\Tag{",
      $in->getName(), "}}\n\n";
      print $fd $in->getValue(),"\n";
    }
  }
}

#------------------------------------------------------------------------------
# Function:	collect
# Description:	
# Returns:	
#
sub collect {
  my ($basedir) = @_;
  my $cwd	= getcwd();

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
  } elsif (m/\.java$/) {
    analyzeJava($_);
  }
}

#------------------------------------------------------------------------------
# Method:	analyzeJava
#
sub analyzeJava {
  my ($file) = @_;

#  print STDERR "$file\n" if $verbose;
  my $package = $file;
  my $fd      = new FileHandle($file, 'r') || die "$file: $!\n";
  while (<$fd>) {
    if (m|^package\s+(.*);|) {
      $package = $1;
    } elsif (m/<doc/) {
      $_ = new InfoItem ($_,
			 $package,
			 processDocTag((m/name="([^\"])*"/ ? $1 : ''),
				       $fd,
				       ''));
      my $key  = $_->getKey();
      print STDERR "--- Double key $key\n" if defined $info{$key};
      $info{$key} = $_;

#      print STDERR $file.':'.$_->getName()."\n" if $verbose;
    }
  }
  $fd->close();
}



#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
