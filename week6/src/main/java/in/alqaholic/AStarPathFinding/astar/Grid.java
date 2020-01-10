package in.alqaholic.AStarPathFinding.astar;

import in.alqaholic.AStarPathFinding.controllers.AStar;

import javax.imageio.ImageIO;
import java.util.*;

public class Grid {

    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;

    private Cell[] grid;
    private int rows,cols;

    public Grid(int width, int height) {
        this.cols = width;
        this.rows = height;
        this.grid = new Cell[rows*cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                grid[y * cols + x] = new Cell(x,y);
            }
        }
    }

    public void setBlocked(int i, int j) {
        grid[j * cols + i] = null;
    }

    public void toggleBlocked(int i, int j) {
        if (grid[j * cols + i] == null) {
            grid[j * cols + i] = new Cell(i, j);
        }
        else {
            grid[j * cols + i] = null;
        }
    }

    public boolean isBlocked(int i, int j) {
        return grid[j * cols + i] == null;
    }

    public int getCost(int i, int j) {
        return grid[j * cols + i].getfCost();
    }


    public List<Cell> astar(int startI, int startJ, int endI, int endJ) {
        PriorityQueue<Cell> open = new PriorityQueue<>(Comparator.comparingInt((Object o) -> ((Cell) o).getfCost()));
        boolean[] closed = new boolean[grid.length];

        open.add(cellAt(startI,startJ));
        Cell current;

        while (true) {
            current = open.poll();

            if (current == null) break;

            closed[current.getI() + cols * current.getJ()] = true;

            if (current.equals(cellAt(endI,endJ))) break;

            Cell t;

            if (current.getJ() - 1 > 0) {
                t = cellAt(current.getI(), current.getJ() - 1); // left neighbour
                checkAndUpdateCost(open, closed, current, t, current.getfCost() + V_H_COST);
            }

            if (current.getJ() + 1 < rows) {
                t = cellAt(current.getI(), current.getJ() + 1); // right neighbour
                checkAndUpdateCost(open, closed, current, t, current.getfCost() + V_H_COST);
            }

            if (current.getI() - 1 > 0) {
                t = cellAt(current.getI() - 1, current.getJ()); // top neighbour
                checkAndUpdateCost(open, closed, current, t, current.getfCost() + V_H_COST);

                if (current.getJ() - 1 > 0) {
                    t = cellAt(current.getI() - 1, current.getJ() - 1); // top left neighbour
                    checkAndUpdateCost(open, closed, current, t, current.getfCost() + DIAGONAL_COST);
                }

                if (current.getJ() + 1 < rows) {
                    t = cellAt(current.getI() - 1, current.getJ() + 1); // top right neighbour
                    checkAndUpdateCost(open, closed, current, t, current.getfCost() + DIAGONAL_COST);
                }
            }
            if (current.getI() + 1 < cols) {
                t = cellAt(current.getI() + 1, current.getJ()); // bottom neighbour
                checkAndUpdateCost(open, closed, current, t, current.getfCost() + V_H_COST);

                if (current.getJ() - 1 > 0) {
                    t = cellAt(current.getI() + 1, current.getJ() - 1); // bottom left neighbour
                    checkAndUpdateCost(open, closed, current, t, current.getfCost() + DIAGONAL_COST);
                }

                if (current.getJ() + 1 < rows) {
                    t = cellAt(current.getI() + 1, current.getJ() + 1); // bottom right neighbour
                    checkAndUpdateCost(open, closed, current, t, current.getfCost() + DIAGONAL_COST);
                }
            }
        }

        Cell end = cellAt(endI, endJ);

        if (end.getParent() == null) return List.of();
        else {
            Cell t = end;
            List<Cell> path = new ArrayList<>();
            while (t.getParent() != null && t.getParent() != cellAt(startI, startJ)) {
                t = t.getParent();
                path.add(t);
            }
            return path;
        }
    }

    private void checkAndUpdateCost(PriorityQueue<Cell> open, boolean[] closed, Cell current, Cell t, int cost) {
        if(t == null || closed[t.getJ() * cols + t.getI()])return;

        boolean inOpen = open.contains(t);
        if(!inOpen || cost<t.getfCost()){
            t.setfCost(cost);
            t.setParent(current);
            if(!inOpen)open.add(t);
        }
    }

    public Cell cellAt(int i, int j) {
        return grid[j * cols + i];
    }

}
