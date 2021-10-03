#include <iostream>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <string>
#include <cstdlib>

using namespace std;


int main(int argc,char** argv){
    int a,b;
    a = argv[1][0] - '0';
    b = argv[2][0] - '0';
    printf("%d",a+b);
    return 0;
}
