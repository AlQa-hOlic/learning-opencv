#include <iostream>
#include <opencv2/core/core.hpp>
#include "opencv2/imgproc.hpp"
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

Mat fourier, ifourier;

int main(int argc, char **argv)
{
    Mat src = imread(samples::findFile("./../test.jpg"), IMREAD_GRAYSCALE);

    src.convertTo(src,CV_64FC1, 1.0f/255.0f);

    dft(src, fourier);

    imshow("Original", src);
    
    imshow("Fourier Transform", fourier);
    moveWindow("Fourier Transform", 50, 50);

    idft(fourier, ifourier, CV_HAL_DFT_SCALE);

    imshow("Inverse Fourier Transform", ifourier);
    moveWindow("Inverse Fourier Transform", 100, 100);

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
