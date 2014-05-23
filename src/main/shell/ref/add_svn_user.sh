#!/bin/bash
#set -x
svn_rep_root="/svnroot/repository/"
svn_user_file="/svnroot/repository/userfile"
svn_authz_file="/svnroot/repository/authz.conf"
htpwd_tool="/usr/local/apache/bin/htpasswd"
svn_admin="/usr/local/subversion/bin/svnadmin"
user=$1
rep=$2

cl_group="_group"
rep_group=$rep$cl_group
usage(){
        echo "useage: $0 svnuser repository_name"
        echo "such as $0 user1 repository1"
        echo "such as $0 user2 repository2"
        list_rep;
        exit 1
}
yes=0
no=1
choose()
{
    while :
    do
      prefix=${1:-Do you want to continue?}
      echo -e -n $prefix' [Y/N]: '
      read answer </dev/tty
      case $answer in
          Y|y|YES|yes|Yes)
              return $yes
              ;;
          N|n|No|NO|no)
             # return $no
             exit;
              ;;
          *)
              echo "Please input again."
              ;;
      esac
    done
}

list_rep(){
echo "#######################################"
echo "all repositorys list below:"
echo "#######################################"
ls -l $svn_rep_root | grep ^d | awk '{print $9}'
echo "#######################################"
}

check_user(){
num_user=$(cat $svn_user_file | grep -c "$user")
if [ $num_user -gt 0 ];then
flag_user=0
echo "this user:$user already exist in userfile"
else
flag_user=1

fi

}


check_group(){
num_group=$(cat $svn_authz_file | grep -c "^$rep_group")
if [ $num_group -gt 0 ];then
flag_group=0
echo "$rep group already exist in authz.conf"
else
flag_group=1
fi

}

check_rep(){
num_rep=$(cat $svn_authz_file | grep -c "$rep:")
if [ $num_rep -gt 0 ];then
flag_rep=0
echo " repository:$rep already exist"
else
flag_rep=1
fi

}

check_group_user(){
num_group_user=$(cat $svn_authz_file | grep  "^$rep_group" | grep -c "$user" )
if [ $num_group_user -gt 0 ];then
flag_group_user=0
echo "this user:$user already exist in $rep_group(authz.conf)"
else
flag_group_user=1
echo "this user:$user dont exist in the group of repository:$rep"
fi

}

add_user(){

choose "do you want to add user:$user"
$htpwd_tool $svn_user_file $user
echo "add user :$user to repository :$rep into svn userfile success"
}

add_group(){
choose "do you want to add user_group:$rep_group"
sed -i "1a \\$rep_group=$user" $svn_authz_file
echo "add user :$user to brand new $rep_group success"

}
add_group_user(){
choose "do you want to add user:$user to user_group:$rep_group"
sed -i "s/^$rep_group=/$rep_group=$user,/g" $svn_authz_file
echo "add user :$user to exist user_group: $rep_group success"
}

add_rep(){
choose "start to add repository:$rep ?"
mkdir -p $svn_rep_root/$rep
$svn_admin create  $svn_rep_root/$rep 
echo "[$rep:/]" >> $svn_authz_file
echo "@$rep_group=rw" >> $svn_authz_file
echo "add brand repository:$rep success"
}
#usage;
if [ "$#" != 2 ] ; then
        echo "useage: $0 svnuser repository_name"
        echo "such as $0 user1 repository1"
        echo "such as $0 user2 repository2"
        list_rep;
        exit 1
fi
}
check_user;
check_group;
check_rep;
check_group_user;
list_rep;
all_flag="$flag_user$flag_group$flag_rep$flag_group_user"
#echo "$all_flag"

case $all_flag in
1111)
add_user;
add_rep;
add_group;
;;
0111)
add_rep;
add_group;
;;
0011)
add_rep;
add_group_user;
;;
0001)
add_group_user;
;;
1110)
echo "no such way 1110"
;;
1100)
echo "no such way 1100"
;;
1000)
echo "no such way 1000"
;;
0110)
echo "no such way 0110"
;;
0100)
echo "no such way 0100"
;;
0010)
echo "no such way 0010"
;;
0000)
echo "this user:$user and this repository:$rep aleardy  exist!"
;;
0011)
echo "no such way 0011"
;;
0101)
add_group;
;;
1001)
add_user;
add_group_user;
;;
1010)
echo "no such way 1010"
;;
1011)
echo "no such way 1011"
;;
esac
