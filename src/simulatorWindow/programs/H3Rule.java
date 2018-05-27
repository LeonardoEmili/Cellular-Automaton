package simulatorWindow.programs;

import simulatorWindow.utils.iVec2;
import simulatorWindow.utils.iVec3;

public class H3Rule extends CellularAutomataProgram {
    private Cell[][] grid;

    public void setGridDimension(int x, int y) {
        grid = new Cell[y][x];
        gridSize = new iVec2(x, y);

        // initialize cells
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++)
                grid[i][j] = new Cell();
    }
    public void tick() {
        if (gridSize == null) return;   // if grid is not initialized, return

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                int lcell = getNeighborState(j, i, -1);
                int ccell = getNeighborState(j, i, 0);
                int rcell = getNeighborState(j, i, +1);

                if (lcell == 0 && ccell == 0 && rcell == 0)
                    grid[i][j].newstate = 0;
                if (lcell == 0 && ccell == 0 && rcell == 1)
                    grid[i][j].newstate = 1;
                if (lcell == 0 && ccell == 1 && rcell == 0)
                    grid[i][j].newstate = 1;
                if (lcell == 0 && ccell == 1 && rcell == 1)
                    grid[i][j].newstate = 0;
                if (lcell == 1 && ccell == 0 && rcell == 0)
                    grid[i][j].newstate = 1;
                if (lcell == 1 && ccell == 0 && rcell == 1)
                    grid[i][j].newstate = 1;
                if (lcell == 1 && ccell == 1 && rcell == 0)
                    grid[i][j].newstate = 0;
                if (lcell == 1 && ccell == 1 && rcell == 1)
                    grid[i][j].newstate = 1;
            }

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                Cell cell = grid[i][j];
                cell.state = cell.newstate;

                if(cell.state == 1) {
                    cell.color.x = 1;
                    cell.color.y = 1;
                    cell.color.z = 1;
                }
                else {
                    cell.color.y -= 0.003f;
                    cell.color.z -= 0.003f;
                    if (cell.color.y < 0) cell.color.y = 0;
                    if (cell.color.z < 0) cell.color.z = 0;
                }
            }
    }

    // resets based on initial condition
    public void reset() {
        if (gridSize == null) return;   // if grid is not initialized, return

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                iVec3 initialColor = getInitialCellColor(j, i);
                grid[i][j].color = initialColor;
                grid[i][j].state = 0;
                grid[i][j].newstate = 0;

                if (initialColor.x > 0)
                    grid[i][j].state = 1;
            }
    }

    public iVec3 getColor(int x, int y) {
        return grid[y][x].color;
    }

    public String name() {
        return "H3-Rule";
    }



    private int getNeighborState(int x, int y, int dir) {
        if ((x+dir) > 0 && (x+dir) < gridSize.x && (y+1) < gridSize.y) return grid[y+1][x+dir].state;
        return 0;
    }

    // using an internal class since CellularAutomataProgram subclasses may need different type of Cells
    class Cell {
        int  state;    // 0 == dead,  1 == alive
        int  newstate;
        iVec3 color;

        public Cell() {
            state = 0;
            color = new iVec3();
        }
    }
}
