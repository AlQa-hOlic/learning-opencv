#include <iostream>

struct Size {
    int rows;
    int cols;
};

int* identityMatrix(Size s);
int* zeroMatrix(Size s);
void printMatrix(int* arr, Size s);
void setValue(int* arr, int i, int j, Size s, int value);

int* matrixAddition(int* a, int* b, Size s);
int* matrixSubtraction(int* a, int* b, Size s);
int* matrixMultiplication(int* a, int* b, Size sa, Size sb);
int* matrixDivision(int* a, int* b, Size sa, Size sb);

int main(int argc,char** argv) {
    std::cout << "Matrix Math!" << std::endl;

    Size twoBytwo;
    twoBytwo.rows = 2;
    twoBytwo.cols = 2;

    int *arr1 = identityMatrix(twoBytwo), *arr2 = identityMatrix(twoBytwo);

    setValue(arr2, 0,0,twoBytwo,1);
    setValue(arr2, 0,1,twoBytwo,2);
    setValue(arr2, 1,0,twoBytwo,3);
    setValue(arr2, 1,1,twoBytwo,4);

    setValue(arr1, 0,0,twoBytwo,1);
    setValue(arr1, 0,1,twoBytwo,2);
    setValue(arr1, 1,0,twoBytwo,3);
    setValue(arr1, 1,1,twoBytwo,4);

    std::cout << std::endl << std::endl;
    std::cout << "arr1:" << std::endl;
    printMatrix(arr1,twoBytwo);
    std::cout << std::endl << std::endl;
    
    std::cout << "arr2:" << std::endl;
    printMatrix(arr2,twoBytwo);
    std::cout << std::endl << std::endl;

    std::cout << "addition:" << std::endl;
    printMatrix(matrixAddition(arr1,arr2,twoBytwo),twoBytwo);
    std::cout << std::endl << std::endl;

    std::cout << "subtraction:" << std::endl;
    printMatrix(matrixSubtraction(arr1,arr2,twoBytwo),twoBytwo);
    std::cout << std::endl << std::endl;

    std::cout << "multiplication:" << std::endl;
    printMatrix(matrixMultiplication(arr1,arr2,twoBytwo, twoBytwo),twoBytwo);
    std::cout << std::endl << std::endl;

    std::cout << "division:" << std::endl;
    printMatrix(matrixDivision(arr1,arr2,twoBytwo, twoBytwo),twoBytwo);

    delete []arr1;
    delete []arr2;
    return 0;
}

void setValue(int* arr, int i, int j, Size s, int value) {
    arr[i * s.cols + j] = value;
}


int* matrixAddition(int* a, int* b, Size s) {
    int *arr = new int[s.rows * s.cols];
    for(int i = 0; i < s.rows; i++) {
        for(int j = 0; j < s.cols; j++) {
            int index = i * s.cols + j;
            arr[index] = a[index] + b[index];
        }
    }
    return arr;
}
int* matrixSubtraction(int* a, int* b, Size s) {
    int *arr = new int[s.rows * s.cols];
    for(int i = 0; i < s.rows; i++) {
        for(int j = 0; j < s.cols; j++) {
            int index = i * s.cols + j;
            arr[index] = a[index] - b[index];
        }
    }
    return arr;
}

int* matrixMultiplication(int* a, int* b, Size sa, Size sb) {
    if(sa.cols != sb.rows) {
        return NULL;
    }
    else {
        int *arr = new int[sa.rows * sb.cols];
        for(int i = 0; i < sa.rows; i++) {
            for(int j = 0; j < sb.cols; j++) {
                int index = i * sb.cols + j;
                int sum = 0;
                for(int k = 0; k < sb.cols; k++) {
                    sum += a[i * sb.cols + k] * b[k * sb.cols + j];
                }
                arr[index] = sum;
            }
        }
        return arr;
    }
}
int* matrixDivision(int* a, int* b, Size sa, Size sb) {
    if(sa.cols != sb.rows) {
        return NULL;
    }
    else {
        int *arr = new int[sa.rows * sb.cols];
        for(int i = 0; i < sa.rows; i++) {
            for(int j = 0; j < sb.cols; j++) {
                int index = i * sb.cols + j;
                int sum = 0;
                for(int k = 0; k < sb.cols; k++) {
                    sum += a[i * sb.cols + k] / b[k * sb.cols + j];
                }
                arr[index] = sum;
            }
        }
        return arr;
    }
}

int* identityMatrix(Size s) {
    int* arr = new int[s.rows*s.cols];

    for(int i = 0; i < s.rows; i++) {
        for(int j = 0; j < s.cols; j++) {
            arr[i * s.cols + j] = i == j ? 1 : 0;
        }
    }

    return arr;
}
int* zeroMatrix(Size s) {
    int* arr = new int[s.rows*s.cols];

    for(int i = 0; i < s.rows; i++) {
        for(int j = 0; j < s.cols; j++) {
            arr[i * s.cols + j] = 0;
        }
    }
    
    return arr;
}

void printMatrix(int* arr, Size s) {
    if(arr == NULL) {
        std::cout << "error: null matrix!" << std::endl;
        return;
    }
    for(int i = 0; i < s.rows; i++) {
        for(int j = 0; j < s.cols; j++) {
            std::cout << arr[i * s.cols + j] << ((j == s.cols - 1) ? "" : "\t");
        }
        std::cout << std::endl;
    }
}
