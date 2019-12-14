#include <iostream>


int** identityMatrix(int rows, int cols) {
    int** arr = new int*[rows];

    for(int i = 0; i < rows; i++) {
        arr[i] = new int[cols];

        for(int j = 0; j < cols; j++) {
            arr[i][j] = i == j ? 1 : 0;
        }
    }

    return arr;
}
int** zeroMatrix(int rows, int cols) {
    int** arr = new int*[rows];

    for(int i = 0; i < rows; i++) {
        arr[i] = new int[cols];

        for(int j = 0; j < cols; j++) {
            arr[i][j] = 0;
        }
    }
    
    return arr;
}

void printMatrix(int** arr, int rows, int cols) {
    for(int i = 0; i < rows; i++) {
        for(int j = 0; j < cols; j++) {
            std::cout << arr[i][j] << ((j == cols - 1) ? "" : "\t");
        }
        std::cout << std::endl;
    }
}

int main(int argc,char** argv) {
    std::cout << "Hello, World!" << std::endl;

    printMatrix(zeroMatrix(2,2),2,2);
    return 0;
}