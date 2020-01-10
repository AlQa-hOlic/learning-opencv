package in.alqaholic.AStarPathFinding.controllers;

import com.jfoenix.controls.JFXButton;
import in.alqaholic.AStarPathFinding.astar.Cell;
import in.alqaholic.AStarPathFinding.astar.Grid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {



    private Stage parent;

    @FXML
    private Canvas grid;
    private GraphicsContext ctx;

    private Grid astarGrid;

    @FXML
    private JFXButton load_bitmap;
    @FXML
    private JFXButton save_bitmap;
    @FXML
    private JFXButton find_path;
    @FXML
    private JFXButton mark_obstacles;

    private boolean isMarkingObstacles;

    private int startI = -1, startJ = -1, endI = -1, endJ = -1;

    public void setStage(Stage parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ctx = grid.getGraphicsContext2D();
        ctx.setFill(Color.BLUE);
        ctx.fillRect(0,0, grid.getWidth(),grid.getHeight());
        isMarkingObstacles = false;

        astarGrid = new Grid(48,48);

        for (int y = 0; y < 48; y++) {
            for (int x = 0; x < 48; x++) {
                Cell cell = astarGrid.cellAt(x, y);
            }
        }
        redrawCanvas();

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
            astarGrid.toggleBlocked(x, y);
            redrawCanvas();
        }
        else {
            if (mouseEvent.isPrimaryButtonDown()) {
                startI = x;
                startJ = y;
            }
            else if (mouseEvent.isSecondaryButtonDown()) {
                endI = x;
                endJ = y;
            }
            redrawCanvas();
        }
    }

    private void redrawCanvas() {
        for (int y = 0; y < 48; y++) {
            for (int x = 0; x < 48; x++) {
                if(!astarGrid.isBlocked(x, y)) {
                    ctx.setFill(Color.GRAY);
                    ctx.fillRect(x*10,y*10, 10,10);
                }
                else {
                    ctx.setFill(Color.WHITE);
                    ctx.fillRect(x*10,y*10, 10,10);
                }
            }
        }
        if (startI != -1 && startJ != -1) {
            ctx.setFill(Color.GREENYELLOW);
            ctx.fillRect(startI*10,startJ*10, 10,10);
        }
        if (endI != -1 && endJ != -1) {
            ctx.setFill(Color.CRIMSON);
            ctx.fillRect(endI*10,endJ*10, 10,10);
        }
    }

    public void findPath(ActionEvent actionEvent) {
        if (startI == -1 || startJ == -1 || endI == -1 || endJ == -1) {
            new Alert(Alert.AlertType.ERROR, "Please define start and end points!", ButtonType.OK).showAndWait();
            return;
        }

        List<Cell> path = astarGrid.astar(startI,startJ,endI,endJ);
        if (path.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Couldn't find path to the target!", ButtonType.OK).showAndWait();
        }
        else {
            markPathToTarget(path);
        }
    }

    private void markPathToTarget(List<Cell> path) {
        path.forEach(cell -> {
            ctx.setFill(Color.GREENYELLOW);
            ctx.fillRect(cell.getI()*10,cell.getJ()*10, 10,10);
        });
    }

    public void loadBitmap(ActionEvent actionEvent) {
        try {
            BufferedImage image = ImageIO.read(new File("untitled.bmp"));
            if (image.getWidth() != 48 || image.getHeight() != 48) {
                new Alert(Alert.AlertType.ERROR, "Invalid image format!", ButtonType.OK).showAndWait();
                return;
            }
            astarGrid = new Grid(48,48);
            startI = startJ = endI = endJ = -1;
            for (int y = 0; y < 48; y++) {
                for (int x = 0; x < 48; x++) {
                    int rgb = image.getRGB(x,y);
                    if ((byte)(rgb >> 8) != 0 && (byte)(rgb >> 16) != 0 && (byte)(rgb) != 0) {
                        astarGrid.setBlocked(x, y);
                    }
                }
            }
            redrawCanvas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBitmap(ActionEvent actionEvent) {
//        FileChooser chooser = new FileChooser();
//        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap image file", "*.bmp"));
//        File file = chooser.showSaveDialog(parent);
//
//        if (file == null || !file.exists()) {
//            return;
//        }
//        else {
        BufferedImage image = new BufferedImage(48,48,BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < 48; y++) {
            for (int x = 0; x < 48; x++) {
                image.setRGB(x, y, astarGrid.isBlocked(x,y)?0xffffffff:0xff000000);
            }
        }
        try {
            ImageIO.write(image, "bmp", new File("untitled.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
    }
}
