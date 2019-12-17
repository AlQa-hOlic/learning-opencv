#include <iostream>
#include <opencv2/core/core.hpp>
#include "opencv2/imgproc.hpp"
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

int main(int argc, char **argv)
{
    float data[] = {4.0,7.0,
                    2.0,6.0};

    /*
    =================
    [4, 7
     2, 6]
     => inverse =>
    [.6, -.7
     -.2, .4]
    =================
    */
    
    Mat matrix = Mat(Size(2,2), CV_32F, data);

    // for (int i = 0; i < matrix.rows; i++)
    // {
    //     for (int j = 0; j < matrix.cols; j++)
    //     {
    //         matrix.at<float>(i,j) = ((float)random() / (float)random());
    //     }
    // }

    cout << "Input:" << endl << matrix << endl;
    
    invert(matrix, matrix, DECOMP_LU);

    cout << "Output:" << endl << matrix << endl;

    return 0;
}
