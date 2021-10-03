#include <iostream>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <string>
#include <cstdlib>

using namespace std;

string token = "";
string reserve[11];
int num = 0;
char filter[4] = {' ','\t','\r','\n'};

void setReserve(){
    reserve[0] = "if";
    reserve[1] = "else";
    reserve[2] = "while";
    reserve[3] = "break";
    reserve[4] = "continue";
    reserve[5] = "return";
    // reserve[6] = "=";
    // reserve[7] = ";";
    // reserve[8] = "(";
    // reserve[9] = ")";
    // reserve[10] = "{";
    // reserve[11] = "}";
    // reserve[12] = "+";
    // reserve[13] = "*";
    // reserve[14] = "/";
    // reserve[15] = "<";
    // reserve[16] = ">";
    // reserve[17] = "==";
}

bool isUpLetter(char ch){
    if(ch >= 'A' && ch <= 'Z'){
        return true;
    }
    else return false;
}

bool isLowLetter(char ch){
    if(ch >= 'a' && ch <= 'z'){
        return true;
    }
    else return false;
}

bool isDigit(char x){
    if(x >= '0' && x <= '9'){
        return true;
    }
    else return false;
}

bool isReserve(string token){
    for(int i = 0; i < 6; i++){
        if(token == reserve[i]){
            return true;
        }
    }
    return false;
}

bool isFilter(char ch){
    for(int i = 0; i < 4; i++){
        if(filter[i] == ch){
            return true;
        }
    }
    return false;
}

bool isKeyword(string token){
    for(int i = 0; i < 6; i++){
        if(token == reserve[i]){
            return true;
        }
    }
    return false;
}

void cat(char x){
    token.push_back(x);
}

void analyse(FILE *fpin){
    setReserve();
    char ch = ' ';
    while((ch = fgetc(fpin)) != EOF){
        token = "";
        if(isFilter(ch)) {}
        else if(isLowLetter(ch)){
            while(isLowLetter(ch)){
                token += ch;
                ch = fgetc(fpin);
            }
            fseek(fpin,-1L,SEEK_CUR);
            if(isKeyword(token)){
                if(token == "if"){
                    printf("If\n");
                }
                else if(token == "else"){
                    printf("Else\n");
                }
                else if(token == "while"){
                    printf("While\n");
                }
                else if(token == "break"){
                    printf("Break\n");
                }
                else if(token == "continue"){
                    printf("Continue\n");
                }
                else{
                    printf("Return\n");
                }
            }
            else{
                cout << "Ident(" << token << ")" << endl;
            }
        }
        else if(isDigit(ch)){
            while(isDigit(ch)){
                token += ch;
                ch = fgetc(fpin);
            }
            fseek(fpin,-1L,SEEK_CUR);
            cout << "Number(" << token << ")" << endl;
        }
        else if(isUpLetter(ch)||isLowLetter(ch)){
            while(isUpLetter(ch)||isLowLetter(ch)||ch=='-'||isDigit(ch)){
                token += ch;
                ch = fgetc(fpin);
            }
            fseek(fpin,-1L,SEEK_CUR);
            cout << "Ident(" << token << ")" << endl;
        }
        else if(ch == '='){
            ch = fgetc(fpin);
            if(ch == '='){
                printf("Eq\n");
            }
            else{
                printf("Assign\n");
                fseek(fpin,-1L,SEEK_CUR);
            }
        }
        else switch(ch){
            case '=':
                {
                    printf("Assign\n");
                    break;
                }
            case ';':
                {
                   printf("Semicolon\n");
                   break;
                }
            case '(':
                {
                   printf("LPar\n");
                   break;
                }
            case ')':
                {
                   printf("RPar\n");
                   break;
                }
            case '{':
                {
                   printf("LBrace\n");
                   break; 
                }
            case '}':
                {
                   printf("RBrace\n");
                   break; 
                }
            case '+':
                {
                   printf("Plus\n");
                   break;
                }
            case '*':
                {
                   printf("Mult\n");
                   break;
                }
            case '/':
                {
                   printf("Div\n");
                   break; 
                }
            case '<':
                {
                   printf("Lt\n");
                   break; 
                }
            case '>':
                {
                   printf("Gt\n");
                   break; 
                }
            default: 
                {
                    printf("Err\n");
                    return ;
                }
        }
    }
}



// int main(){
//     char inFile[40];
//     FILE *fpin;
//     while(true){
//         scanf("%s",inFile);
//         if((fpin = fopen(inFile,"r")) != NULL){
//             break;
//         }
//         else{
//             printf("文件名错误，请重新输入源文件名（包括路径和后缀）");
//         }
//     }
//     analyse(fpin);
//     return 0;
// }

int main(int argc,char** argv){
    char inFile[40];
    FILE *fpin;
    while(true){
        strcpy(inFile,argv[1]);
        if((fpin = fopen(inFile,"r")) != NULL){
            break;
        }
        else{
            printf("文件名错误，请重新输入源文件名（包括路径和后缀）");
        }
    }
    analyse(fpin);
    return 0;
}
