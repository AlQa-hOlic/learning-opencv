#include <iostream>
#include <opencv2/core/core.hpp>
#include "opencv2/imgproc.hpp"
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

void on_trackbar_change(int, void*);

Mat src,dst;
int threshold_value = 0;


int main(int argc, char **argv)
{

    namedWindow("src");
    namedWindow("threshold");
    namedWindow("dst");

    createTrackbar("Mask Threshold", "src", &threshold_value, 255, on_trackbar_change, 0);
    
    src = imread(samples::findFile("./../test.jpg"), IMREAD_GRAYSCALE);

    on_trackbar_change(0,0);
    
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


void on_trackbar_change(int, void*) {

    Mat srcFloat;
    src.convertTo(srcFloat, CV_32F);

    Mat fourierTransform,fourierTransformNormalized;
    dft(srcFloat, fourierTransform, DFT_REAL_OUTPUT);

    fourierTransform.copyTo(fourierTransformNormalized);
    fourierTransformNormalized += Scalar::all(1);
    log(fourierTransformNormalized, fourierTransformNormalized);
    normalize(fourierTransformNormalized, fourierTransformNormalized, 0.0f, 1.0f, NORM_MINMAX);    

    cout << fourierTransformNormalized(Rect(150,150,10,10)) << endl;
    
    Mat fourierMask;
    threshold(fourierTransformNormalized, fourierMask, (float)threshold_value/255.0f , 1.0f, THRESH_BINARY_INV);
    
    Mat fourierMaskBit;
    fourierMask.convertTo(fourierMaskBit, CV_8UC1);

    Mat fourierMasked;
    fourierTransform.copyTo(fourierMasked, fourierMaskBit);
    
    Mat inverseTransform;
    dft(fourierMasked, inverseTransform, DFT_SCALE|DFT_INVERSE|DFT_REAL_OUTPUT);

    inverseTransform.convertTo(dst, CV_8U);

    imshow("src",src);
    imshow("threshold",fourierMask);
    imshow("dst", dst);
}