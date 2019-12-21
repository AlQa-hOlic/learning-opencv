#include <iostream>
#include <opencv2/core/core.hpp>
#include "opencv2/imgproc.hpp"
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

int mat_inverse_test () {
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

int main(int argc, char **argv)
{
    // mat_inverse_test();
    
    Mat src = imread("./../test.jpg",IMREAD_GRAYSCALE);
    // cvtColor(src,src,COLOR_BGR2GRAY);

    src = src(Rect(0,0,src.cols,src.cols));

    src.convertTo(src, CV_64F, 1.0f/255.0f);

    imshow("square mat", src);

    Mat inverted = src.inv(cv::DECOMP_SVD);

    cout << inverted(Rect(100,100,10,10)) << endl;

    imshow("inverted", inverted);


    while (1)
    {
        char k = waitKey(1);
        if (k == 27 || k == ' ')
        {
            break;
        } 
    }
    return 0;
}