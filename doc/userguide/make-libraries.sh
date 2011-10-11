#!/bin/bash

DESC="libraries-descriptions.txt"
LIBRARY_DIR="../../lib" 
TEXFILE="Libraries.tex"
LICENSE_LIST="license-abbrev.lst"

cat > $TEXFILE << EOF
\begin{center}
\begin{longtable}{|p{0.3\textwidth}|p{0.2\textwidth}|p{0.4\textwidth}|}
\hline 
Filename & License & Description\\\\
\hline
EOF

rm -f $LICENSE_LIST
touch $LICENSE_LIST

for I in `cat libraries-descriptions.txt | sort | grep -E '^\w.*\t.*$' | sed 's/\t/%18/' | sed s/\ /%20/g` ; do
	J=`echo $I | sed 's/%20/\ /g'`
	LIBPATTERN=`echo $J | sed 's/^\(\w.*\)%18.*/\1/' | sed 's/\\$/\*/'`
	LIBDESC=`echo $J | sed 's/^\w.*%18\(.*\)/\1/'`
	LIBPATH=`find $LIBRARY_DIR -maxdepth 1 -name "$LIBPATTERN" | head -1`
	if [ "$LIBPATH" != "" ] ; then
		LIBNAME=`basename "$LIBPATH"`
		echo $LIBNAME
		LIBPATH_LICENSE=`echo $LIBPATH | sed 's/jar$/LICENSE/'`
		if [ -f "$LIBPATH_LICENSE" ] ; then
			LICENSE_LINE=`cat "$LIBPATH_LICENSE" | head -1`
			LICENSE_NAME=`echo "$LICENSE_LINE" | sed 's/^\(.*\)\ -\ \(.*\) - \(.*\)$/\2/'`
			LICENSE_LONGNAME=`echo "$LICENSE_LINE" | sed 's/^\(.*\)\ -\ \(.*\) - \(.*\)$/\1/'`
			LICENSE_VERSION=`echo "$LICENSE_LINE" | sed 's/^\(.*\)\ -\ \(.*\) - \(.*\)$/\3/'`
			if [ "$LICENSE_NAME" == "$LICENSE_LINE" ] ; then
				echo "File $LIBPATH_LICENSE malformed."
			fi
			# LaTeXify
			LICENSE_NAME=`echo "$LICENSE_NAME" | sed 's/\\\\/\\\\textbackslash\{\}/g' | sed 's/\_/\\\_/g'`
			LICENSE_LONGNAME=`echo "$LICENSE_LONGNAME" | sed 's/\\\\/\\\\textbackslash\{\}/g' | sed 's/\_/\\\_/g'`
			LICENSE_VERSION=`echo "$LICENSE_VERSION" | sed 's/\\\\/\\\\textbackslash\{\}/g' | sed 's/\_/\\\_/g'`
			if [ "$LICENSE_VERSION" == "unknown" ] ; then
				LICENSE="$LICENSE_NAME"
			else
				LICENSE="$LICENSE_NAME - $LICENSE_VERSION"
			fi
			echo "\\item[$LICENSE_NAME] $LICENSE_LONGNAME" >> $LICENSE_LIST
		else
			echo "Missing $LIBPATH_LICENSE file."
			LICENSE=" --- "
		fi
		cat >> $TEXFILE << EOF
\hline 
$LIBNAME & $LICENSE & $LIBDESC\\\\
EOF
	fi
done

cat >> $TEXFILE << EOF
\hline 
\end{longtable}
\label{tabular:libraries}
\end{center}
\begin{description}
EOF

cat $LICENSE_LIST | sort | uniq >> $TEXFILE

cat >> $TEXFILE << EOF
\end{description}
EOF

rm $LICENSE_LIST

# end
