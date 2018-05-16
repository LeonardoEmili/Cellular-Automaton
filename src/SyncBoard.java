import java.util.Random;

public class SyncBoard {

    private int h, w;
    private TCell[][] grid;
    private Random generator;
    int percentage = 30;
    boolean randomSeed;
    Tuple[] population;

    public SyncBoard(int h, int w, Tuple[] population, boolean randomSeed, int percentage) {
        this.h = h;
        this.w = w;
        this.grid = new TCell[this.h][this.w];
        this.generator = new Random();
        this.randomSeed = randomSeed;
        this.percentage = percentage;
        this.population = population;
        populate();
    }

    public void populate() {
        for (int i = 0; i < this.h; i++) {
            for (int j = 0; j < this.w; j++)
                grid[i][j] = new TCell(i, j);
        }
        if (this.randomSeed)
            launchRandomTCells();
        else
            launchChosenTCells();
    }

    public void launchRandomTCells() {
        int population = h * w * this.percentage / 100;
        for (int i = 0; i < population; i++) {
            int newY = generator.nextInt(this.h);
            int newX = generator.nextInt(this.w);
            while (grid[newY][newX].isAlive()) {
                newY = generator.nextInt(this.h);
                newX = generator.nextInt(this.w);
            }
            TCell t = grid[newY][newX];
            t.start();
        }
    }

    public void launchChosenTCells() {
        for (Tuple t: this.population)
            this.grid[t.getY()][t.getX()].start();
    }





}
