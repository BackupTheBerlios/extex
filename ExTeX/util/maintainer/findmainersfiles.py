#!/usr/bin/env python
# author: Sebastian Waschik
# created: 2004-07-12
# RCS-ID: $Id: findmainersfiles.py,v 1.1 2004/08/11 20:49:22 plaicy Exp $

# TODO: .cvsignore beachten (TE)

import os
import re
import sys

IGNOREDIRS=["CVS", "target"]

def dirvisitor(data, dirname, filesindir):
    global IGNOREDIRS
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



def search(searchText):
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

    data['outputstring'] = "%(fileName)s (%(author)s)"

    data['searchRE'] = re.compile("")
    if searchText:
        data['outputstring'] = "%(fileName)s"
        data['searchRE'] = re.compile(searchText, re.IGNORECASE)
        
    os.path.walk(os.curdir, dirvisitor, data)



def printUsage():
    print "usage:"
    print "%s [searchText]" % sys.argv[0]



def main():
    if len(sys.argv)==1:
        search(None)
    else:
        if sys.argv[1]=="-h" or len(sys.argv) > 2:
            printUsage()
        else:
            search(sys.argv[1])


if __name__=="__main__":
    main()
