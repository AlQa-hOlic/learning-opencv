cmake_minimum_required(VERSION 2.8 FATAL_ERROR)

project("Matrix_Operations")



# find_package( OpenCV REQUIRED )
include( /usr/local/lib/cmake/opencv4/OpenCVConfig.cmake )

message( STATUS "OpenCV library status:" )
message( STATUS "    version: ${OpenCV_VERSION}" )
message( STATUS "    libraries: ${OpenCV_LIBS}" )
message( STATUS "    include path: ${OpenCV_INCLUDE_DIRS}" )

include_directories( ${OpenCV_INCLUDE_DIRS} )

set(CMAKE_CXX_STANDARD 11) # C++11...
set(CMAKE_CXX_STANDARD_REQUIRED ON) #...is required...
set(CMAKE_CXX_EXTENSIONS OFF) #...without compiler extensions like gnu++11


add_executable("inverse" inverse.cpp)
target_link_libraries( "inverse" ${OpenCV_LIBS} )

add_executable("threshold" threshold.cpp)
target_link_libraries( "threshold" ${OpenCV_LIBS} )

add_executable("rotate" rotate.cpp)
target_link_libraries( "rotate" ${OpenCV_LIBS} )

add_executable("text_overlay" text_overlay.cpp)
target_link_libraries( "text_overlay" ${OpenCV_LIBS} )