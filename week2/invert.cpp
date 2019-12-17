#include <iostream>
#include <opencv2/core/core.hpp>
#include "opencv2/imgproc.hpp"
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

Mat src, dst;

int should_invert = 0;
int threshold_value = 150;

void callback(int state, void* userdata);

int main(int argc, char **argv)
{
    src = imread(samples::findFile("./../test.jpg"), IMREAD_GRAYSCALE);
    namedWindow("Out", WINDOW_AUTOSIZE | WINDOW_GUI_NORMAL | WINDOW_OPENGL);
    createTrackbar("Should invert", "Out", &should_invert, 1, callback, 0);
    createTrackbar("Threshold", "Out", &threshold_value, 255, callback, 0);
    callback(0,0);

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

void callback(int state, void* userdata) {
    threshold(src, dst, threshold_value, 255, THRESH_BINARY);
    if (should_invert) bitwise_not(dst,dst);
    imshow("Out", dst);
}
