public void revice(char[] str){
	int i=0;
	int j=str.length-1;

	while(i<j){
		swap(str,i,j);
		i++;
		j--;
	}

}

private void swap(char[] str,int i,int j)
{
	char temp = str[i];
	str[i]=str[j];
	str[j]=temp;
}

