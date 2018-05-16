import java.util.Random;

public class NonSyncBoard {

    private int h, w;
    private Cell[][] grid;
    private int population;
    private Random generator;

    public NonSyncBoard(int h, int w) {
        this.h = h;
        this.w = w;
        this.population = h * w * 100 / 30;
        this.grid = new Cell[h][w];
        this.generator = new Random();
        setPopulation();
    }

    public void setPopulation() {
        for (int i = 0; i < this.population; i++) {
            int newY = generator.nextInt(this.h);
            int newX = generator.nextInt(this.w);
            while (this.grid[newY][newX] != null) {
                newY = generator.nextInt(this.h);
                newX = generator.nextInt(this.w);
            }
            grid[newY][newX] = new Cell(newY, newX);
            ;
        }
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }
}
