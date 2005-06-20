#!/usr/local/bin/perl
##*****************************************************************************
## $Id: Info.pm,v 1.1 2005/06/20 10:31:02 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

Info.pm - ...

=head1 SYNOPSIS

Info.pm

=head1 DESCRIPTION

=head1 Attributes

=head1 Methods

=head1 AUTHOR

Gerd Neugebauer

=head1 BUGS

=over 4

=item *

...

=back

=cut


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

use strict;
use Exporter;
our @ISA       = qw(Exporter);
our @EXPORT    = qw();
our @EXPORT_OK = qw();

#------------------------------------------------------------------------------
# Variable:	$VERSION
# Description:	
#
our $VERSION = ('$Revision: 1.1 $ ' =~ m/[0-9.]+/ ? $& : '0.0' );

use FileHandle;
use File::Find;
use Cwd;

use xml2tex;

my %info = ();

#------------------------------------------------------------------------------
# Function:	new
#
sub new
{ my ($class, $basedir) = @_;
  my $self     = collect($basedir);
  return bless $self,$class;
}

#------------------------------------------------------------------------------
# Method:	dump
#
sub dump {
  my ($self, $fd, $type, $title) = @_;
  local $_;

  foreach my $in (sort keys %$self) {
    $in = $self->{$in};
    if ($in->getType() eq $type) {
      my $n = $in->getName();
      $_    = $title;
      s/%name%/$n/;
      print $fd $_,$in->getValue(),"\n";
    }
  }
}

#------------------------------------------------------------------------------
# Method:	getValue
#
sub getValue {
  my ($self, $name) = @_;
  local $_ = $self->{$name};
  
  return (defined $_ ? $_->getValue() : undef);
}

#------------------------------------------------------------------------------
# Function:	collect
# Description:	
# Returns:	
#
sub collect {
  my ($basedir) = @_;
  my $cwd = getcwd();
  %info   = ();
  chdir $basedir;
  find({no_chdir=>1,
	wanted=>\&analyze}, '.');
  chdir $cwd;
  return \%info;
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


1;
##-----------------------------------------------------------------------------
## Local Variables: 
## mode: perl
## End: 
