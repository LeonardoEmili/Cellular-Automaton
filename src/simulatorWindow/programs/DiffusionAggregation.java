package simulatorWindow.programs;

import simulatorWindow.utils.iVec2;
import simulatorWindow.utils.iVec3;

import java.util.Random;

public class DiffusionAggregation extends CellularAutomataProgram {
    // using an internal class since CellularAutomataProgram subclasses may need different type of Cells
    class Cell {
        int  state;    // 0 == empty,  1 == moving,  2 == aggregated
        int  movementDirection;     // -1 no movement, 0 top, 1 tr, 2 right, 3 br, 4 bottom, 5 bl, 6 left, 7 tl
        int  stableNumber;
        boolean moved;
        iVec3 color;

        public Cell() {
            state = 0;
            moved = false;
            stableNumber = 0;
            movementDirection = -1;
            color = new iVec3();
        }
    }

    private Cell[][] grid;
    private Random random;
    private int stableCount;

    public DiffusionAggregation() {
        random = new Random();

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

                if (cell.state == 0) {
                    cell.color = new iVec3(0,0,0);
                    continue;
                }
                if (cell.state == 2) {
                    float redComponent = (float)cell.stableNumber / (float)stableCount;
                    cell.color = new iVec3(redComponent,0.35f,0.65f);
                    continue;
                }

                // if it's not empty or stable, it's moving
                iVec3 mc = getMovementColor(cell.movementDirection);
                cell.color = mc;

                if (collidesWithStableCell(j, i)) {
                    cell.state = 2;
                    cell.stableNumber = stableCount++;
                    continue;
                }

                // this cell already moved in this tick, so prevent it from moving again
                if (cell.moved) continue;

                Cell nextCell = getNextCell(j, i, cell.movementDirection);
                if (nextCell.state == 0) {
                    cell.state = 0;
                    nextCell.state = 1;
                    nextCell.movementDirection = cell.movementDirection;
                    nextCell.moved = true;
                    continue;
                }

                // at this point we made contact with another moving cell, so change direction
                // cell.movementDirection = (cell.movementDirection + 1) % 8;
                cell.movementDirection = (int)(random.nextFloat() * 8.0f) % 8;
            }

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++)
                grid[i][j].moved = false;
    }

    public void reset() {
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                iVec3 initialColor = getInitialCellColor(j, i);

                grid[i][j].color = initialColor;
                grid[i][j].state = 0;
                grid[i][j].stableNumber = 0;
                grid[i][j].movementDirection = (int)(random.nextFloat() * 8.0f) % 8;

                // se il colore iniziale non è nero, allora questa cella è considerata viva
                if (initialColor.x > 0) {
                    grid[i][j].state = 1;
                }
            }

        stableCount = 1;

        // central cell will be aggregated
        grid[gridSize.y / 2][gridSize.x / 2].state = 2;
        grid[gridSize.y / 2][gridSize.x / 2].stableNumber = stableCount;
    }

    public iVec3 getColor(int x, int y) {
        return grid[y][x].color;
    }

    public String name() {
        return "Diffusion Aggregation";
    }

    // ritorna il numero totale delle celle vicine "vive"
    private boolean collidesWithStableCell(int x, int y) {

        for (int i = 0; i < 8; i++) {
            iVec2 dm = getMovementDelta(i);

            int xx = x + dm.x;
            int yy = y + dm.y;

            if (xx < 0 || xx >= gridSize.x || yy < 0 || yy >= gridSize.y) continue;
            if (grid[yy][xx].state == 2) return true;
        }

        return false;
    }


    private Cell getNextCell(int x, int y, int movementDir) {
        iVec2 dm = getMovementDelta(movementDir);

        int xx = x + dm.x;
        int yy = y + dm.y;

        if (xx < 0 || xx >= gridSize.x || yy < 0 || yy >= gridSize.y) return grid[y][x];
        return grid[yy][xx];
    }


    private iVec2 getMovementDelta(int movementDirection) {
        switch(movementDirection) {
            case 0:
                return new iVec2(0, 1);
            case 1:
                return new iVec2(1, 1);
            case 2:
                return new iVec2(1, 0);
            case 3:
                return new iVec2(1, -1);
            case 4:
                return new iVec2(0, -1);
            case 5:
                return new iVec2(-1, -1);
            case 6:
                return new iVec2(-1, 0);
            case 7:
                return new iVec2(-1, -1);
        }

        return new iVec2(0, 0);
    }

    private iVec3 getMovementColor(int movementDirection) {
        iVec3 color = new iVec3(0, 0 ,0);

        switch(movementDirection) {
            case 0:
                color = new iVec3(0.5f, 0.5f,0.5f); break;
            case 1:
                color = new iVec3(0.5f, 0.5f, 1); break;
            case 2:
                color = new iVec3(0.5f, 1, 0.5f); break;
            case 3:
                color = new iVec3(0.5f, 1 ,1); break;
            case 4:
                color = new iVec3(1, 0.5f, 0.5f); break;
            case 5:
                color = new iVec3(1, 0.5f, 1f); break;
            case 6:
                color = new iVec3(1, 1, 0.5f); break;
            case 7:
                color = new iVec3(1, 1, 1); break;
        }

        color.x *= 0.5f;
        color.y *= 0.5f;
        color.z *= 0.5f;

        return color;
    }
}
