#include <stdio.h>
#include <stdlib.h>
typedef union
{
    char c;
    int a;
    int b;
}Demo;
int main()
{
    Demo d;
    d.c = 'H';
    d.a = 10;
    d.b = 12;
    printf("最开始时变量所占的字节长度为: %d\n", sizeof(d)/4);
    printf("赋值后的三个值分别为：\n");
    printf("%c\t%d\t%d\n", d.c, d.a, d.b);
    return 0;
     
}
