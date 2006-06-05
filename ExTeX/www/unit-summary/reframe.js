//
//
// 2006 Gerd Neugebauer

if (parent.frames.length == 0) {

  document.close();
  document.open();
  document.writeln('<!DOCUMENTTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">');
  document.writeln('<html>');
  document.writeln('<head>');
  document.writeln('  <title>Units</title>');
  document.writeln('</head>');
  document.writeln('<frameset rows="100,*">');
  document.writeln(' <frame src="header.html" title="ExTeX" scrolling="no">');
  document.writeln(' <frameset cols="20%,80%">');
  document.writeln('  <frameset rows="30%,70%">');
  document.writeln('   <frame src="overview.html" name="overviewFrame" title="All units">');
  document.writeln('   <frame src="units.html" name="unitFrame" title="All primitives">');
  document.writeln('  </frameset>');
  document.writeln('  <frame src="' + location.pathname + '" name="infoFrame" title="Primitive descriptions" scrolling="yes">');
  document.writeln(' </frameset>');
  document.writeln(' <noframes>');
  document.writeln('  <h2>Frame Alert</h2>');
  document.writeln('  This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client.');
  document.writeln(' </noframes>');
  document.writeln('</frameset>');
  document.writeln('</html>');
  document.close();
}

//
