
module in.alqaholic.OpenCVJava {
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.fxml;
    requires opencv;
    requires com.jfoenix;
    exports in.alqaholic.OpenCVJava;
    opens in.alqaholic.OpenCVJava.controllers to javafx.fxml;
}