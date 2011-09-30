#!/bin/sh

DESC="libraries-descriptions.txt"
LIBRARY_DIR="../../lib" 
TEXFILE="Libraries.tex"

cat > $TEXFILE << EOF
\begin{center}
\begin{longtable}{|p{0.3\textwidth}|p{0.2\textwidth}|p{0.5\textwidth}|}
\hline 
Filename & License & Description\\\\
\hline
EOF

for I in `cat libraries-descriptions.txt | sort | grep -E '^\w.*\t.*$' | sed 's/\t/%18/' | sed s/\ /%20/g` ; do
	J=`echo $I | sed 's/%20/\ /g'`
	LIBPATTERN=`echo $J | sed 's/^\(\w.*\)%18.*/\1/' | sed 's/\\$/\*/'`
	LIBDESC=`echo $J | sed 's/^\w.*%18\(.*\)/\1/'`
	LIBPATH=`find $LIBRARY_DIR -name "$LIBPATTERN" | head -1`
	if [ "$LIBPATH" != "" ] ; then
		LIBNAME=`basename "$LIBPATH"`
		echo $LIBNAME
		LIBPATH_LICENSE=`echo $LIBPATH | sed 's/jar$/LICENSE/'`
		if [ -f "$LIBPATH_LICENSE" ] ; then
			LICENSE_LINE=`cat "$LIBPATH_LICENSE" | head -1`
			LICENSE_NAME=`echo "$LICENSE_LINE" | sed 's/^\(.*\)\ -\ .*$/\1/'`
			LICENSE_VERSION=`echo "$LICENSE_LINE" | sed 's/^.*\ -\ \(.*\)$/\1/'`
			if [ "$LICENSE_VERSION" = "unknown" ] ; then
				LICENSE="$LICENSE_NAME"
			else
				LICENSE="$LICENSE_NAME - $LICENSE_VERSION"
			fi
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
EOF

# end
