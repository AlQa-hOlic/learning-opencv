#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

char window_name[] = "Week2";
char trackbar_name[] = "Rotate";
//0: ROTATE_90_CLOCKWISE 1: ROTATE_180 2: ROTATE_90_COUNTERCLOCKWISE
int trackbar_value = 0;
int const trackbar_max_type = 4;

Mat src, rotated;

void trackbar_on_change( int, void* )
{
    rotate(src, rotated, trackbar_value);

    String statusbar_text;
    if(trackbar_value==0) statusbar_text ="ROTATE_90_CLOCKWISE";
    if(trackbar_value==1) statusbar_text="ROTATE_180";
    if(trackbar_value==2) statusbar_text="ROTATE_90_COUNTERCLOCKWISE";
    
    setWindowTitle("Rotated", statusbar_text);
    imshow("Rotated", rotated);
}

int main(int argc, char **argv)
{
    src = imread(samples::findFile("./../test.jpg"), IMREAD_COLOR);
    namedWindow("Original", WINDOW_AUTOSIZE);
    imshow("Original", src);

    createTrackbar(trackbar_name, "Original", &trackbar_value, trackbar_max_type, trackbar_on_change, 0);
    trackbar_on_change(0,0);

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