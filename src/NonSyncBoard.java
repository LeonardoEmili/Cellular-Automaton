import java.util.Random;

public class NonSyncBoard {

    private int h, w;
    private Cell[][] grid;
    private Random generator;
    int percentage = 30;
    boolean randomSeed;
    Tuple[] population;

    public NonSyncBoard(int h, int w, Tuple[] population) {
        this.h = h;
        this.w = w;
        this.grid = new Cell[h][w];
        this.generator = new Random();
        this.randomSeed = randomSeed;
        this.percentage = percentage;
        this.population = population;
        populate();
    }

    public void populate() {
        for (int i = 0; i < this.h; i++) {
            for (int j = 0; j < this.w; j++)
                grid[i][j] = new Cell(i, j);
        }
        this.setRandomPopulation();
    }

    public void setRandomPopulation() {
        int population = h * w * percentage / 100;
        for (int i = 0; i < population; i++) {
            int newY = generator.nextInt(this.h);
            int newX = generator.nextInt(this.w);
            while (grid[newY][newX].isAlive()) {
                newY = generator.nextInt(this.h);
                newX = generator.nextInt(this.w);
            }
            grid[newY][newX].setToAlive();
        }
    }



    public static int getNeighborsAlive(int y, int x, Cell[][] grid) {
        int counter = 0;
        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                if (dy == 0 && dx == 0)
                    continue;
                int Y = (y + dy + grid.length) % grid.length;
                int X = (x + dx + grid[0].length) % grid[0].length;
                if (grid[Y][X].isAlive())
                    counter++;
            }
        }
        return counter;
    }

    public void update () {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Cell currentCell = grid[i][j];
                int alive = getNeighborsAlive(i, j, this.grid);

                if (alive < 2 || alive > 3)
                    currentCell.die();
                else
                    currentCell.breath();
            }
        }
        for (int i = 0; i < this.h; i++) {
            for (int j = 0; j < this.w; j++) {
                this.grid[i][j].updateStatus();
            }
        }
    }

    public void display() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j].isAlive())
                    System.out.print("░░");
                else
                    System.out.print("▓▓");
            }
            System.out.println();
        }
        for (int i = 0; i < w; i++) {
            System.out.print("~ ");
        }
        System.out.println();
    }

}
