#!/bin/sh

DESC="libraries-descriptions.txt"
LIBRARY_DIR="../../lib" 
TEXFILE="Libraries.tex"

cat > $TEXFILE << EOF
\begin{center}
\begin{longtable}{|p{0.4\textwidth}|p{0.5\textwidth}|}
\hline 
Filename & Description\\\\
\hline
EOF

for I in `cat libraries-descriptions.txt | sort | grep -E '^\w.*\t.*$' | sed 's/\t/%18/' | sed s/\ /%20/g` ; do
	J=`echo $I | sed 's/%20/\ /g'`
	LIBPATTERN=`echo $J | sed 's/^\(\w.*\)%18.*/\1/' | sed 's/\\$/\*/'`
	LIBDESC=`echo $J | sed 's/^\w.*%18\(.*\)/\1/'`
	LIBPATH=`find $LIBRARY_DIR -name "$LIBPATTERN" | head -1`
	if [ "$LIBPATH" != "" ] ; then
		LIBNAME=`basename $LIBPATH`
		echo $LIBNAME
		cat >> $TEXFILE << EOF
\hline 
$LIBNAME & $LIBDESC\\\\
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
