#include <iostream>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <string>
#include <cstdlib>
#include <regex>

using namespace std;

string token = "";
string reserve[3];
int num = 0;
char filter[4] = {' ','\t','\r','\n'};
//各非终结符类型，1表示int,2表示main,3表示return,4表示数字
//5表示(,6表示),7表示{,8表示},9表示;
int sym[1000];
int sym_p = 0;
int src = 0;
string word[1000];
int word_p = 0;


void setReserve(){
    reserve[0] = "int";
    reserve[1] = "main";
    reserve[2] = "return";
    // reserve[3] = "(";
    // reserve[4] = ")";
    // reserve[5] = "{";
    // reserve[6] = "}";
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

bool isHexadecimal(char ch){
    if(isDigit(ch)||(ch>='a'&&ch<='f')||(ch>='A'&&ch<='F')){
        return true;
    }
    return false;
}

bool isOctal(char ch){
    if(ch >= '0'&& ch <= '7'){
        return true;
    }
    return false;
}


void lexerAnalyse(FILE *fpin){
    setReserve();
    char ch = ' ';
    while((ch = fgetc(fpin)) != EOF){
        token = "";
        if(isFilter(ch)) {
            token += ch;
            word[word_p++] = token;
        }
        else if(isLowLetter(ch)){
            while(isLowLetter(ch)||isUpLetter(ch)){
                token += ch;
                ch = fgetc(fpin);
            }
            fseek(fpin,-1L,SEEK_CUR);
            // while(isUpLetter(ch)||isLowLetter(ch)||ch=='_'||isDigit(ch)){
            //     token += ch;
            //     ch = fgetc(fpin);
            // }
            // fseek(fpin,-1L,SEEK_CUR);
            if(isReserve(token)){
                word[word_p++] = token;
                if(token == "int"){
                    sym[sym_p++] = 1;
                }
                else if(token == "main"){
                    sym[sym_p++] = 2;
                }
                else if(token == "return"){
                    sym[sym_p++] = 3;
                }
            }
            else{
                // printf("lexer error\n");
                exit(-1);
            }
        }
        else if(isDigit(ch)){
            if(ch=='0'){
                token += ch;
                ch = fgetc(fpin);
                if(ch=='x'||ch=='X'){
                    token += ch;
                    ch = fgetc(fpin);
                    if(!isDigit(ch)||!isHexadecimal(ch)){
                        // printf("hexademical error\n");
                        exit(-1);
                    }
                    else{
                        while(isDigit(ch)||isHexadecimal(ch)){
                            token += ch;
                            ch = fgetc(fpin);
                        }
                        int x;
                        stringstream ss1;
                        ss1 << hex << token;
                        ss1 >> x;
                        sym[sym_p++] = 4;
                        word[word_p++] = to_string(x);
                        fseek(fpin,-1L,SEEK_CUR);
                    }
                }
                else if(isOctal(ch)){
                    while(isOctal(ch)){
                        token += ch;
                        ch = fgetc(fpin);
                    }
                    int x;
                    stringstream ss2;
                    ss2 << oct << token;
                    ss2 >> x;
                    sym[sym_p++] = 4;
                    word[word_p++] = to_string(x);
                    fseek(fpin,-1L,SEEK_CUR);
                }
                // else if(!isDigit(ch)){
                //     sym[sym_p++] = 4;
                //     word[word_p++] = token;
                //     fseek(fpin,-1L,SEEK_CUR);
                // }
                else{
//                     sym[sym_p++] = 4;
//                     word[word_p++] = token;
//                     fseek(fpin,-1L,SEEK_CUR);
                    // printf("numtype error\n");
//                     exit(-1);
                }
            }
            else{
                while(isDigit(ch)){
                    token += ch;
                    ch = fgetc(fpin);
                }
                sym[sym_p++] = 4;
                word[word_p++] = token;
                fseek(fpin,-1L,SEEK_CUR);
            }
        }
        else if(ch=='/'){
            ch = fgetc(fpin);
            if(ch == '/'){
                while(ch != '\n'){
                    ch = fgetc(fpin);
                }
            }
            else if(ch == '*'){
                ch = fgetc(fpin);
                while(1){
                    if(ch == '*'){
                        ch = fgetc(fpin);
                        if(ch == '/'){
                            break;
                        }
                        else if(ch == EOF){
                            // printf("last\n");
                            exit(-1);
                        }
                        ch = fgetc(fpin);
                    }
                    else if(ch == EOF){
                        // printf("last\n");
                        exit(-1);
                    }
                    else{
                        ch = fgetc(fpin);
                    }
                }
            }
            else{
                // printf("lexer error\n");
                exit(-1);
            }
        }
        else switch(ch){
            case '(':
                {
                    sym[sym_p++] = 5;
                    word[word_p++] = "(";
                    break;
                }
            case ')':
                {
                    sym[sym_p++] = 6;
                    word[word_p++] = ")";
                    break;
                }
            case '{':
                {
                   sym[sym_p++] = 7;
                   word[word_p++] = "{";
                   break;
                }
            case '}':
                {
                   sym[sym_p++] = 8;
                   word[word_p++] = "}";
                   break;
                }
            case ';':
                {
                   sym[sym_p++] = 9;
                   word[word_p++] = ";";
                   break; 
                }
            default: 
                {
                    exit(-1);
                }
        }
    }
}

void match(int x){
    // printf("%d %d\n",sym[src],x);
    if(sym[src++] != x){
        // printf("not match\n");
        exit(-1);
    }
    // else{
    //     printf("true\n");
    // }
}

void Number(){
    match(4);
}

void Stmt(){
    match(3);
    Number();
    match(9);
}

void Block(){
    match(7);
    Stmt();
    match(8);
}

void Ident(){
    match(2);
}

void FuncType(){
    match(1);
}

void FuncDef(){
    FuncType();
    Ident();
    match(5);
    match(6);
    Block();
}

void CompUnit(){
    FuncDef();
};

void Parse(){
    CompUnit();
}
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
    lexerAnalyse(fpin);
    // for(int i = 0; i < sym_p; i++){
    //     printf("%d\n",sym[i]);
    // }
    Parse();
    ofstream out(argv[2]);
    for(int i = 0; i < word_p; i++){
        if(word[i] == "int"){
            out << "define dso_local i32";
        }
        else if(word[i] == "main"){
            out << "@main";
        }
        else if(word[i] == "return"){
            out << "ret i32";
        }
        else if(word[i] == ";"){
            ;
        }
        else{
            out << word[i];
        }
    }
    out.close();
    // for(int i = 0; i < word_p; i++){
    //     if(word[i] == "int"){
    //         cout << "define dso_local i32";
    //     }
    //     else if(word[i] == "main"){
    //         cout << "@main";
    //     }
    //     else if(word[i] == "return"){
    //         cout << "ret i32";
    //     }
    //     else{
    //         cout << word[i];
    //     }
    // }
    return 0;
}

















