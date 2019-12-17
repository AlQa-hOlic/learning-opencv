#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

char window_name[] = "Week2";
char trackbar_name[] = "Rotate";
//0: ROTATE_90_CLOCKWISE 1: ROTATE_180 2: ROTATE_90_COUNTERCLOCKWISE
int trackbar_value = 0;
int const trackbar_max_type = 3;

Mat src, rotated;

void trackbar_on_change( int, void* )
{

    String statusbar_text;
    if(trackbar_value==3) {
        statusbar_text="NONE";
        rotated = src;
    }
    else {
        if(trackbar_value==0) statusbar_text ="ROTATE_90_CLOCKWISE";
        if(trackbar_value==1) statusbar_text="ROTATE_180";
        if(trackbar_value==2) statusbar_text="ROTATE_90_COUNTERCLOCKWISE";
        rotate(src, rotated, trackbar_value);
    }
    
    setWindowTitle("Rotated", statusbar_text);
    imshow("Rotated", rotated);
}

int main(int argc, char **argv)
{
    src = imread(samples::findFile("./../test.jpg"), IMREAD_COLOR);
    namedWindow("Trackbars", WINDOW_AUTOSIZE);
    namedWindow("Rotated", WINDOW_AUTOSIZE);
    
    // imshow("Trackbars", src);

    createTrackbar(trackbar_name, "Trackbars", &trackbar_value, trackbar_max_type, trackbar_on_change, 0);
    trackbar_on_change(0,0);

    moveWindow("Rotated", 100,200);
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