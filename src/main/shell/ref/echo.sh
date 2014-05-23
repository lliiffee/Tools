#!/bin/sh
skip=14
tmpdir=`/bin/mktemp -d ${TMPDIR:-/tmp}/gzexe.XXXXXXXXXX` || exit 1
prog="${tmpdir}/`echo \"$0\" | sed 's|^.*/||'`"
if /usr/bin/tail -n +$skip "$0" | "/bin"/gzip -cd > "$prog"; then
  /bin/chmod 700 "$prog"
  trap '/bin/rm -rf $tmpdir; exit $res' EXIT
  "$prog" ${1+"$@"}; res=$?
else
  echo "Cannot decompress $0"
  /bin/rm -rf $tmpdir
  exit 1
fi; exit $res
�I�5Pecho.sh SV�O���/��JM��W0 . o�i   