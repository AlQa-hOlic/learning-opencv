
module in.alqaholic.AStarPathFinding {
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.fxml;
//    requires opencv;
    requires com.jfoenix;
    exports in.alqaholic.AStarPathFinding;
    opens in.alqaholic.AStarPathFinding.controllers to javafx.fxml;
}