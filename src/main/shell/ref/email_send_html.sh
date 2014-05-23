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
�EDPemail_send_html.sh ՗[o�0���)� SaUJ.��h$��{�&�������,ؔ8����΅
]��j�����l�/����KY+
P="�%�b
�C��)����0�Ry�c����z ���8�74����1I֡"��q&���Ȓ
0И"�tBGQ7�;,�BN�]�A���|�u�=������A֒4t���~����>�^_}�^]���Ah���R�U˹NÀG<���9|�/p��5Oc"Ә�u��i��i��i�MW��y8Z��V����LT/��Ѭ�jZi>*o���2�:�Qb6���-�DS1;	�2f'��/����ŷ2�pm�)�������u_������� ��D3�-�<n��,���0%��<z6Q�3�bn�<��ԉ�X�.F��f�p�ݸ��<Gߜxl�;��]G.�SQ�m�6;�#�d���"����sΔa�����X-oհ	`����g��y���?�-��=W��0f��~��BL�|��I�Yut��t��%8��o��7�aj���\02��ڛ~��c���������
��ss��n�M��Zt�O��Y� \���\r���d/P�����n �bV���q�@=b1{�0m3Zϐ^�ku۽ӮmV,�E���r�z�����ӝ������ !�����y�U#��E�j�9�>�TUS���w-��t���A��9�U�i*�\/~���lN��suԔ��i�k3������f�q��V+�F��� ��� G�ss=��iu��k`�  