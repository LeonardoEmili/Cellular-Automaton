package simulatorWindow;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simulatorWindow.programs.CellularAutomataProgram;
import simulatorWindow.programs.CyclicCA;
import simulatorWindow.programs.Default;
import simulatorWindow.utils.InitialStateCondition;
import simulatorWindow.utils.iVec2;
import simulatorWindow.utils.iVec3;

public class Simulator {

    public CellularAutomataProgram currentProgram;
    private Canvas canvas;
    private GraphicsContext gc;
    private InitialStateCondition initialCondition = InitialStateCondition.random;
    private AnimationTimer painter;
    public iVec2 size;
    public int generationCount = 0;

    public Simulator() {
        painter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tickAndPaint();
            }
        };
            size = new iVec2(325, 250);
    }

    public void start() {
        painter.start();
    }

    public void tickAndPaint() {
        currentProgram.tick();
        generationCount++;
        paintResult();
    }

    public void pause() {
        painter.stop();
    }

    public void tick() {
        pause();
        tickAndPaint();
    }

    public void stop() {
        painter.stop();
        generationCount = 0;
        currentProgram.reset();
        paintResult();
    }

    public void setInitialStateCondition(InitialStateCondition condition) {
        currentProgram.condition = condition;
        initialCondition         = condition;
    }

    public void setProgram(CellularAutomataProgram newprogram) {
        currentProgram = newprogram;
        currentProgram.condition = initialCondition;
        currentProgram.setGridDimension(size.x, size.y);
        currentProgram.reset();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, size.x * 2, size.y * 2);
    }

    public void paintResult() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, size.x * 2, size.y * 2);

        for (int i = 0; i < currentProgram.gridSize.y; i++)
            for (int j = 0; j < currentProgram.gridSize.x; j++) {
                // swapping y value since bottom is top for javaFX
                int y = currentProgram.gridSize.y - i - 1;

                iVec3 fillColor = currentProgram.getColor(j, i);
                gc.setFill(new Color(fillColor.x, fillColor.y, fillColor.z, 1));
                gc.fillRect(j * 2, y * 2, 2, 2);
            }
    }

}
