package simulatorWindow.programs;

import simulatorWindow.utils.iVec3;
import simulatorWindow.utils.iVec2;

public class GameOfLife extends CellularAutomataProgram {
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

    private Cell[][] grid;

    // quando il programma viene selezionato dalla GUI va inizializzato in base alle
    // dimensioni della finestra dove verrà disegnato, che sono passate come argomento (x, y)
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
                int cellNeightbors = getCellNeightbors(j, i);

                if (cellNeightbors == 3 && grid[i][j].state == 0) {
                    grid[i][j].newstate = 1;
                }

                if (cellNeightbors < 2 && grid[i][j].state == 1) {
                    grid[i][j].newstate = 0;
                }

                if (cellNeightbors > 3 && grid[i][j].state == 1) {
                    grid[i][j].newstate = 0;
                }
            }

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                Cell cell = grid[i][j];
                cell.state = cell.newstate;

                // da qui in poi sti calcoli servono solo a fare i colori belli su schermo
                if(cell.state == 1) {
                    cell.color.x = 1;
                    cell.color.y = 1;
                    cell.color.z = 1;
                }
                else {
                    if(cell.color.x > 0.5f) {
                        cell.color.x = 0.5f;
                        cell.color.y = 0;
                        cell.color.z = 0;
                    } else {
                        cell.color.x -= 0.005f;
                        if (cell.color.x < 0) cell.color.x = 0;
                        cell.color.y = 0;
                        cell.color.z = 0;
                    }
                }
            }
    }

    // resetta la griglia basandosi sulla condizione iniziale
    public void reset() {
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                // chiamiamo la classe madre che ritornerà il colore associato alla cella in posizione (x, y)
                // in base alle condizioni iniziali che sono state scelte nella GUI
                iVec3 initialColor = getInitialCellColor(j, i);

                grid[i][j].color = initialColor;
                grid[i][j].state = 0;
                grid[i][j].newstate = 0;

                // se il colore iniziale non è nero, allora questa cella è considerata viva
                if (initialColor.x > 0)
                    grid[i][j].state = 1;
            }
    }

    // ritorna il colore associato allo stato della cella in posizione (x, y)
    // verrà poi usato dal Simulator per disegnare la cella su schermo
    public iVec3 getColor(int x, int y) {
        return grid[y][x].color;
    }

    public String name() {
        return "Game Of Life";
    }

    // ritorna il numero totale delle celle vicine "vive"
    private int getCellNeightbors(int x, int y) {
        int total = 0;

        if (x-1 >= 0 && y-1 >= 0 && grid[y-1][x-1].state > 0) total += 1;
        if (x-1 >= 0 && grid[y][x-1].state > 0) total += 1;
        if (x-1 >= 0 && y+1 < gridSize.y && grid[y+1][x-1].state > 0) total += 1;

        if (y-1 > 0 && grid[y-1][x].state > 0) total += 1;
        if (y+1 < gridSize.y && grid[y+1][x].state > 0) total += 1;

        if (x+1 < gridSize.x && y-1 >= 0 && grid[y-1][x+1].state > 0) total += 1;
        if (x+1 < gridSize.x && grid[y][x+1].state > 0) total += 1;
        if (x+1 < gridSize.x && y+1 < gridSize.y && grid[y+1][x+1].state > 0) total += 1;

        return total;
    }
}
