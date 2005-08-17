; author: Sebastian Waschik
; created: 2004-07-26
; RCS-ID: $Id: .prj.el,v 1.3 2005/08/17 22:03:54 plaicy Exp $
(jde-project-file-version "1.0")
(jde-set-variables
 '(jde-run-application-class "")
 '(jde-run-working-directory "../..")
 '(jde-run-read-app-args nil)
 ;; TODO: do the following better (TE)
 '(jde-sourcepath
   (quote
    ("./../../src/java"
     "./../../src/test")))
 '(jde-global-classpath
   (quote
    ("./../../target/classes"
    "./../../lib"
    "./../../develop/lib")))
 '(jde-checkstyle-classpath
   (quote
    ("./../../develop/lib/checkstyle-all-3.4.jar"
     "./../../develop/lib/checkstyle-optional-3.4.jar")))
 '(jde-checkstyle-style "./../../.checkstyle.cfg")

)
