package simulatorWindow.programs;

import javafx.scene.paint.Color;
import simulatorWindow.utils.iVec2;
import simulatorWindow.utils.iVec3;

/** follows conventions and general ideas from: http://www.mirekw.com/ca/rullex_cycl.html */
public class CyclicCA extends CellularAutomataProgram {
    class Cell {
        int  state;    // 0 == dead,  1 == alive
        int  newstate;
        iVec3 color;

        public Cell() {
            state = 0;
            color = new iVec3();
        }
    }

    private Cell[][] grid;
    private int R = 2;
    private int T = 4;
    private int C = 9;
    private iVec3[] colors;

    public CyclicCA() {
        colors = new iVec3[C];
        for(int i = 0; i < C; i++) {
            Color c = Color.hsb(((float)i / (float)C) * 360.0f,0.6f,1);
            colors[i] = new iVec3((float)c.getRed(), (float)c.getGreen(), (float)c.getBlue());
        }
    }


    public void setGridDimension(int x, int y) {
        grid = new Cell[y][x];
        gridSize = new iVec2(x, y);

        // initializza le celle
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++)
                grid[i][j] = new Cell();
    }
    public void tick() {
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                Cell cell = grid[i][j];
                int count = getAdvancedNeighbors(cell, j, i);

                if (count >= T)
                    cell.newstate = (cell.state + 1) % C;

                cell.color = stateToColor(cell.state);
            }

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                Cell cell = grid[i][j];
                cell.state = cell.newstate;
            }
    }

    public void reset() {
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                // this program is expected to run by using a "random color" initial condition
                iVec3 initialColor = getInitialCellColor(j, i);

                int initialState = (int)Math.floor(initialColor.x * C) % C;
                grid[i][j].color = stateToColor(initialState);
                grid[i][j].state = initialState;
                grid[i][j].newstate = initialState;
            }
    }

    public iVec3 stateToColor(int state) {
        if (C > 4) {
            return colors[state];
        }

        switch (state) {
            case 0:
                return new iVec3(0, 0.451f, 0.549f);
            case 1:
                return new iVec3(0.505f, 0.69f, 0.698f);
            case 2:
                return new iVec3(0.839f, 0.917f, 0.831f);
            case 3:
                return new iVec3(0.839f, 0.505f, 0.831f);
        }

        return new iVec3(0, 0, 0);
    }

    public iVec3 getColor(int x, int y) {
        return grid[y][x].color;
    }

    public String name() {
        return "Cyclic CA";
    }

    private int getAdvancedNeighbors(Cell cell, int x, int y) {
        int nextState = (cell.state + 1) % C;
        int count = 0;

        for ( int i = -R; i < R+1; i++)
            for ( int j = -R; j < R+1; j++) {
                int xx = x + j;
                int yy = y + i;

                if(xx < 0) xx = gridSize.x - 1;
                if(xx >= gridSize.x) xx = 0;
                if(yy < 0) yy = gridSize.y - 1;
                if(yy >= gridSize.y) yy = 0;

                if (grid[yy][xx].state == nextState)
                    count++;
            }

        return count;
    }
}
