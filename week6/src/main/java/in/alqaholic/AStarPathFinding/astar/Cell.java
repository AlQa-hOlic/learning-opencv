package in.alqaholic.AStarPathFinding.astar;

public class Cell {

    private int fCost; // least possible final cost

    private int i,j; // position of the cell

    private Cell parent;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
        this.fCost = 0;
        this.parent = null;
    }

    public int getfCost() {
        return fCost;
    }

    public void setfCost(int fCost) {
        this.fCost = fCost;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Cell[ " + i + ", " + j + " ] = " + fCost;
    }
}
