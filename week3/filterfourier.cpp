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

    src = imread(samples::findFile("./../test.jpg"), IMREAD_GRAYSCALE);


    createTrackbar("Mask Threshold", "src", &threshold_value, 255, on_trackbar_change, 0);
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

void visualizeFourier(Mat& src, Mat& dst) {
    // normalize values and switch to  logarithmic scale => log(1 + M);
    dst = src.clone();
    dst += Scalar::all(1.0f);
    log(dst, dst);

    normalize(dst, dst, 0, 1, NORM_MINMAX);
    
}

void flipQuads(Mat& src, Mat& dst) {
    dst = src.clone();
    // switch quadrants for better visualization
    dst = dst(Rect(0, 0, dst.cols & -2, dst.rows & -2)); // now magI is an (even x even) Mat
    
    int cx = dst.cols/2;
    int cy = dst.rows/2;
    
    Mat q0(dst, Rect(0, 0, cx, cy));
    Mat q1(dst, Rect(cx, 0, cx, cy));
    Mat q2(dst, Rect(0, cy, cx, cy));
    Mat q3(dst, Rect(cx, cy, cx, cy));
    
    Mat tmp;

    //first Q to fourth Q
    q0.copyTo(tmp);
    q3.copyTo(q0);
    tmp.copyTo(q3);

    //second Q to third Q
    q1.copyTo(tmp);
    q2.copyTo(q1);
    tmp.copyTo(q2);
}

void on_trackbar_change(int, void*) {

    Mat fourierTransform,fourierTransformNormalized;
    Mat fourierTransformPlanes[] = {Mat_<float>(src), Mat::zeros(src.size(), CV_32F)};
    Mat complexSrc;
    merge(fourierTransformPlanes, 2, complexSrc);
    dft(complexSrc, complexSrc);
    split(complexSrc, fourierTransformPlanes);
    magnitude(fourierTransformPlanes[0], fourierTransformPlanes[1], fourierTransform);

    visualizeFourier(fourierTransform, fourierTransformNormalized);

    
    Mat fourierMask;
    threshold(fourierTransformNormalized, fourierMask, (float)threshold_value/255.0 , 1.0, THRESH_BINARY_INV);
    // fourierMask = fourierTransformNormalized;
    Mat fourierMaskBit;
    fourierMask.convertTo(fourierMaskBit, CV_8UC1);

    Mat fourierMasked;
    complexSrc.copyTo(fourierMasked, fourierMaskBit);
    
    Mat inverseTransform;
    dft(fourierMasked, inverseTransform, DFT_SCALE|DFT_INVERSE|DFT_REAL_OUTPUT);
    cout << inverseTransform(Rect(150,150,10,10)) << endl;

    inverseTransform.convertTo(dst, CV_8U);

    imshow("src",src);

    flipQuads(fourierMask,fourierMask);
    imshow("threshold",fourierMask);
    
    imshow("dst", dst);
}