#!C:\opt\cygwin\bin\perl.exe -w
##*****************************************************************************
## $Id: collect.pl,v 1.3 2005/06/15 21:34:05 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

collect.pl - ...

=head1 SYNOPSIS

collect.pl [-v|--verbose] 

collect.pl [-h|-help]

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
my $verbose = 0;

use Getopt::Long;
GetOptions("h|help"	=> \&usage,
	   "v|verbose"	=> \$verbose,
	  );

my $info = new Info('../../src/java');
$info->dump(\*STDOUT, 'syntax');


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
  my $self     = { 'name'    => $name,
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

my %info = ();

#------------------------------------------------------------------------------
# Function:	new
# Description:	
# Returns:	
#
sub new
{ my ($class, $basedir) = @_;
  collect($basedir);
  my $self     = { 
		 };
  return bless $self,$class;
}

sub dump {
  my ($self, $fd, $type) = @_;

  foreach (sort keys %info) {
    $_ = $info{$_};
    if ($_->getType() eq $type) {
      print $fd $_->getValue()."\n" 

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

sub analyze {

  if ( $_ eq 'CVS' ) {
    $File::Find::prune = 1;
  } elsif (m/\.java$/) {
    analyzeJava($_);
  }
}

sub analyzeJava {
  my ($file) = @_;

#  print STDERR "$file\n" if $verbose;
  my $package = $file;
  my $fd      = new FileHandle($file, 'r') || die "$file: $!\n";
  while(<$fd>) {
    if (m|^package\s+(.*);|) {
      $package = $1;
    } elsif (m/<doc/) {
      my $spec = $_;
      my $val  = '';
      while(<$fd>) {
	last if m|</doc>|;
	s|^\s*\* ?||;
	$val .= $_;
      }
      $_      = new InfoItem ($spec, $package, $val);
      my $key = $_->getKey();
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
