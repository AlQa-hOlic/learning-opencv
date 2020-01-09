package in.alqaholic.AStarPathFinding.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class MainController implements Initializable {



    private Stage parent;

    @FXML
    private Canvas grid;
    private GraphicsContext ctx;

    @FXML
    private JFXButton load_bitmap;
    @FXML
    private JFXButton save_bitmap;
    @FXML
    private JFXButton find_path;
    @FXML
    private JFXButton mark_obstacles;

    private boolean isMarkingObstacles;

    public void setStage(Stage parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ctx = grid.getGraphicsContext2D();
        ctx.setFill(Color.BLUE);
        ctx.fillRect(0,0, grid.getWidth(),grid.getHeight());
        isMarkingObstacles = false;

        AStar.grid = new AStar.Cell[48][48];
        AStar.closed = new boolean[48][48];
        AStar.open = new PriorityQueue<>((Object o1, Object o2) -> {
            AStar.Cell c1 = (AStar.Cell)o1;
            AStar.Cell c2 = (AStar.Cell)o2;

            return Integer.compare(c1.finalCost, c2.finalCost);
        });

        for (int y = 0; y < 48; y++) {
            for (int x = 0; x < 48; x++) {
                AStar.grid[x][y] = new AStar.Cell(x,y);
                ctx.setFill(Color.GRAY);
                ctx.fillRect(x*10,y*10,10,10);
            }
        }


    }


    public void onMarkObstacles(ActionEvent actionEvent) {
        if (isMarkingObstacles) {
            load_bitmap.setDisable(false);
            save_bitmap.setDisable(false);
            find_path.setDisable(false);
            mark_obstacles.setText("Mark Obstacles");
            isMarkingObstacles = false;
        }
        else {
            load_bitmap.setDisable(true);
            save_bitmap.setDisable(true);
            find_path.setDisable(true);
            mark_obstacles.setText("Confirm");
            isMarkingObstacles = true;
        }
    }

    public void canvasMousePressed(MouseEvent mouseEvent) {
        int x = (int)mouseEvent.getX() / 10;
        int y = (int)mouseEvent.getY() / 10;
        if (isMarkingObstacles) {
            if(AStar.grid[x][y] == null) {
                AStar.grid[x][y] = new AStar.Cell(x,y);
                ctx.setFill(Color.GRAY);
                ctx.fillRect(x*10,y*10, 10,10);
            }
            else {
                AStar.setBlocked(x,y);
                ctx.setFill(Color.WHITE);
                ctx.fillRect(x*10,y*10, 10,10);
            }
        }
        else {
            if (mouseEvent.isPrimaryButtonDown()) {
                AStar.setStartCell(x,y);
                ctx.setFill(Color.GREENYELLOW);
                ctx.fillRect(x*10,y*10, 10,10);
            }
            else if (mouseEvent.isSecondaryButtonDown()) {
                AStar.setEndCell(x,y);
                ctx.setFill(Color.CRIMSON);
                ctx.fillRect(x*10,y*10, 10,10);
            }
        }
    }

    public void findPath(ActionEvent actionEvent) {
        AStar.AStar();
        if (AStar.grid[AStar.endI][AStar.endJ].parent == null) {
            new Alert(Alert.AlertType.ERROR, "Couldn't find path to the target!", ButtonType.OK).showAndWait();
        }
        else {
            markPathToTarget(AStar.grid[AStar.endI][AStar.endJ].parent);
        }
    }

    private void markPathToTarget(AStar.Cell parent) {
        if (parent.i == AStar.startI && parent.j == AStar.startJ) {
            new Alert(Alert.AlertType.INFORMATION, "Found path to the target!", ButtonType.OK).showAndWait();
            return;
        }
        else {
            ctx.setFill(Color.DARKGREEN);
            ctx.fillRect(parent.i*10,parent.j*10, 10,10);
            markPathToTarget(parent.parent);
        }
    }
}
