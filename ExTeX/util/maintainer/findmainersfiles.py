#!/usr/bin/env python
# author: Sebastian Waschik
# created: 2004-07-12
# RCS-ID: $Id: findmainersfiles.py,v 1.2 2004/08/11 21:56:08 plaicy Exp $

# TODO: .cvsignore beachten (TE)

import getopt
import os
import re
import sys

IGNOREDIRS=["CVS", "target"]

def dirvisitor(data, dirname, filesindir):
    global IGNOREDIRS

    # remove ignored directories
    for i in IGNOREDIRS:
        try:
            filesindir.remove(i)
        except ValueError:
            pass

    output={}
    for fileName in filesindir:
        output['author'] = ""
        output['fileName'] = dirname+os.sep+fileName

        authorRE = None
        for fileType in data['fileTypes']:
            if fileType['fileName'].search(fileName):
                authorRE = fileType['author']
                break

        try:

            # test if there is a regular expression for this filetype
            if authorRE is not None:

                file=open(output['fileName'], "rt")
                fileContent = file.read()
                file.close()

                matchObj = authorRE.search(fileContent)
                if matchObj is not None:
                    output['author'] = matchObj.group(1)

                    if data['searchRE'].search(output['author']):
                        print data['outputstring'] % output
        except IOError:
            pass



def search(searchText, printMaintainer):
    data={}

    data['fileTypes']=[]
    data['fileTypes'].append({
        "fileName":re.compile("\\.java$"),
        "author":re.compile('@author <a[^>]*>([^>]*)(?=<)')
        })
    data['fileTypes'].append({
        "fileName":re.compile("\\.html$"),
        "author":re.compile('<meta name="author"[^>]*content="([^"]*)"')
        })
    # TODO: the folloging is not correct (TE)
    data['fileTypes'].append({
        "fileName":re.compile("\\.tex$"),
        "author":re.compile("\\author\\{([^}]*)\\}")
        })

    data['outputstring'] = "%(fileName)s"
    if printMaintainer:
        data['outputstring'] += " (%(author)s)"

    data['searchRE'] = re.compile("")
    if searchText:
        data['searchRE'] = re.compile(searchText, re.IGNORECASE)

    os.path.walk(os.curdir, dirvisitor, data)



def printUsage():
    print "usage:"
    print "%s [--printauthor] [searchText]" % sys.argv[0]
    print "  --printauthor: Always print author"



def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "h", ["help", "printauthor"])
    except getopt.GetoptError, e:
        print "unknown option \"%s\"" % e.opt
        printUsage()
        sys.exit(1)

    searchText = None
    printMaintainer = 1

    if len(args) == 1:
        searchText = args[0]
        printMaintainer = 0
    elif len(args) > 1:
        printUsage()

    # TODO: put searchText in Option (TE)
    for opt, arg in opts:
        if opt in ("-h", "--help"):
            printUsage()
            sys.exit()
        elif opt == "--printauthor":
            printMaintainer = 1


    search(searchText, printMaintainer)


if __name__=="__main__":
    main()
