#include <iostream>
#include <opencv2/core/core.hpp>
#include "opencv2/imgproc.hpp"
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

Mat fourier, ifourier;

int main(int argc, char **argv)
{
    Mat I = imread( samples::findFile( "./../lena.tiff" ), IMREAD_GRAYSCALE);
    Mat padded;
    int m = getOptimalDFTSize( I.rows );
    int n = getOptimalDFTSize( I.cols );
    copyMakeBorder(I, padded, 0, m - I.rows, 0, n - I.cols, BORDER_CONSTANT, Scalar::all(0));

    Mat planes[] = {Mat_<float>(padded), Mat::zeros(padded.size(), CV_32F)}; // [real, complex]
    Mat complexI;
    merge(planes, 2, complexI); // [real, complex] ==> Mat

    dft(complexI, complexI);

    split(complexI, planes); // Mat ==> [real, complex]
    magnitude(planes[0], planes[1], planes[0]); 

    // switch to logarithmic scale [log(1+M)]
    Mat magI = planes[0];
    magI += Scalar::all(1);
    log(magI, magI);

    // //SWAP QUADRANTS
    magI = magI(Rect(0, 0, magI.cols & -2, magI.rows & -2)); // now magI is an (even x even) Mat
    
    int cx = magI.cols/2;
    int cy = magI.rows/2;
    
    Mat q0(magI, Rect(0, 0, cx, cy));
    Mat q1(magI, Rect(cx, 0, cx, cy));
    Mat q2(magI, Rect(0, cy, cx, cy));
    Mat q3(magI, Rect(cx, cy, cx, cy));
    
    Mat tmp;

    //first Q to fourth Q
    q0.copyTo(tmp);
    q3.copyTo(q0);
    tmp.copyTo(q3);

    //second Q to third Q
    q1.copyTo(tmp);
    q2.copyTo(q1);
    tmp.copyTo(q2);

    normalize(magI, magI, 0, 1, NORM_MINMAX);

    imshow("input", I   );
    imshow("fourier", magI);

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
