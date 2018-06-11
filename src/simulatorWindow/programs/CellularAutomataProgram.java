package simulatorWindow.programs;

import javafx.scene.control.Tab;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import simulatorWindow.utils.InitialStateCondition;
import simulatorWindow.utils.iVec2;
import simulatorWindow.utils.iVec3;

import java.util.Random;

public abstract class CellularAutomataProgram {

    public iVec2 gridSize;                      // Size of the grid
    public volatile InitialStateCondition condition;     // We used an enum to identify the initial condition
    private Random random = new Random();       // Just a random initial condition
    public static PixelReader pixelReader;
    public static iVec2 imageSize;

    protected iVec3 getInitialCellColor(int x, int y) {
        switch(this.condition) {
            case onlyEven:
                if (x % 2 == 0 && y % 2 == 0)
                    return new iVec3(1,1,1);
                else {
                    return new iVec3(0,0,0);
                }
            case dottedTop:
                if (x == gridSize.x / 2 && y == gridSize.y - 1)
                    return new iVec3(1,1,1);
                return new iVec3(0,0,0);
            case random:
                if (random.nextBoolean())
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case random2:
                if (random.nextFloat() > 0.7)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case random3:
                if (random.nextFloat() > 0.85)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case evenRows:
                if (y % 2 == 0)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case topRow:
                if (y == gridSize.y - 1)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case xySin:
                if (Math.sin(x+y) > 0)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case xySin2:
                if (Math.sin(x * 0.1f + Math.cos(y * 0.1f)) > 0)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case xyXOR:
                if ((x ^ y) > 0)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case mod3:
                if ((x + y) % 3 == 0)
                    return new iVec3(1,1,1);
                else
                    return new iVec3(0,0,0);
            case randomGrayScale:
                float color = random.nextFloat();
                return new iVec3(color, color, color);
            case randomColor:
                return new iVec3(random.nextFloat(),random.nextFloat(),random.nextFloat());
            case fromImage:
                if(pixelReader == null) break;

                float tx = (float)x / (float)gridSize.x;
                float ty = (float)y / (float)gridSize.y;
                int pixelx = (int)(tx * (float)imageSize.x);
                int pixely = (imageSize.y) - ((int)(ty * (float)imageSize.y) + 1);

                Color pixelread = pixelReader.getColor(pixelx, pixely);
                return new iVec3((float)pixelread.getRed(), (float)pixelread.getGreen(), (float)pixelread.getBlue());
        }

        // If not assigned return all black
        return new iVec3(0,0,0);
    }


    /* To-implement methods - START */
    public abstract void setGridDimension(int x, int y);        // Reset grid dimensions
    public abstract void tick();                                // Simulation step-by-step, just one increase per time
    public abstract void reset();                               // Reset grid dimensions based on the initial condition
    public abstract String name();                              // Program's name that will appear in the GUI
    public void buildTab(Tab tab) { }                           // todo, a che ci serve ?
    public abstract iVec3 getColor(int x, int y);               /* This method returns the color associated to the cell's state[x][y],
                                                                   which is used by the Simulator to draw the cell on the screen/*
    /* To-implement methods - END */
}
