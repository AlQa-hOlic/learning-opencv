#include <iostream>
#include <string>
#include <stdio.h>
#include <time.h>
#include <opencv2/core/core.hpp>  
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/video/video.hpp>

using namespace cv;
using namespace std;

const std::string currentDateTime();

int main(int argc,char** argv) {
    VideoCapture capture;

    capture.open(0);

    if(!capture.isOpened()) {
        cout << "Error opening video capture" << endl;
    }

    Mat frame;
    while(capture.read(frame)) {
        if(frame.empty()) {
            cout << "No captured frame!" << endl;
            break;
        }

        Point p = { 20, 50 };

        putText(frame, currentDateTime(), p, FONT_HERSHEY_PLAIN, 1, 5, 1);
        imshow("Text Overlay", frame);

        char k = waitKey(1);
        if(k == 27 || k == ' ') {
            break;
        }
    }
    return 0;
}

const std::string currentDateTime() {
    time_t     now = time(0);
    struct tm  tstruct;
    char       buf[80];
    tstruct = *localtime(&now);
    // Visit http://en.cppreference.com/w/cpp/chrono/c/strftime
    // for more information about date/time format
    strftime(buf, sizeof(buf), "%Y-%m-%d.%X", &tstruct);

    return buf;
}