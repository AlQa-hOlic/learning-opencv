#include <stdio.h>

int main(int argc,char** argv) {
    char* input = new char[16];
    scanf("%s", input);
    printf("You entered: %s\n",input);
    return 0;
}