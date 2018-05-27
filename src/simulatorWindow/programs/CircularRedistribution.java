package simulatorWindow.programs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import simulatorWindow.utils.iVec3;
import simulatorWindow.utils.iVec2;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/** follows conventions and general ideas from: http://www.mirekw.com/ca/rullex_cycl.html */
public class CircularRedistribution extends CellularAutomataProgram {
    class Cell {
        int  state;    // 0 = non excited,  1 = excited
        int  newstate;
        int  lastRotationIndex;
        iVec3 color;

        public Cell() {
            state = 0;
            color = new iVec3();
        }
    }

    private Cell[][] grid;
    private int R = 1;
    private boolean rotateIndexes = true;
    private float minThreshold = 2.25f;
    private float maxThreshold = 6.7f;
    private float interpolation = 0.8f;

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

                if (isExcited(cell, j, i)) {
                    cell.newstate = 1;
                } else {
                    cell.newstate = 0;
                }
            }

        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                Cell cell = grid[i][j];
                cell.state = cell.newstate;

                if (cell.state == 1) {
                    setExcitedCellColor(cell, j, i);
                }
            }
    }

    public void reset() {
        for (int i = 0; i < gridSize.y; i++)
            for (int j = 0; j < gridSize.x; j++) {
                // this program is expected to run by using a "random color" initial condition
                iVec3 initialColor = getInitialCellColor(j, i);

                grid[i][j].color = new iVec3(initialColor.x, initialColor.x, initialColor.x);
                grid[i][j].state = 0;
                grid[i][j].lastRotationIndex = 0;
                grid[i][j].newstate = 0;
            }
    }

    public iVec3 stateToColor(int state) {
        return new iVec3(0, 0, 0);
    }

    public iVec3 getColor(int x, int y) {
        return grid[y][x].color;
    }

    public String name() {
        return "Circular Redistribution";
    }

    public boolean isExcited(Cell cell, int x, int y) {
        float value = getAccumulationValue(cell, x, y);
        if (value > minThreshold && value < maxThreshold) return true;

        return false;
    }

    private void setExcitedCellColor(Cell cell, int x, int y) {
        float accum = getAccumulationValue(cell, x, y);
        // int Rarea = (R+2*(R-1)) * (R+2*(R-1));
        int angle = (cell.lastRotationIndex + (int)Math.floor(accum)) % 8;
        iVec3 prevColor = cell.color;
        iVec3 newColor  = new iVec3(0,0,0);

        switch(angle) {
            case 0:
                newColor = getAjacentCellColor(x, y, 1, 0); break;
            case 1:
                newColor = getAjacentCellColor(x, y, 1, 1); break;
            case 2:
                newColor = getAjacentCellColor(x, y, 0, 1); break;
            case 3:
                newColor = getAjacentCellColor(x, y, -1, 1); break;
            case 4:
                newColor = getAjacentCellColor(x, y, -1, 0); break;
            case 5:
                newColor = getAjacentCellColor(x, y, -1, -1); break;
            case 6:
                newColor = getAjacentCellColor(x, y, 0, -1); break;
            case 7:
                newColor = getAjacentCellColor(x, y, 1, -1); break;
        }

        if(rotateIndexes)
            cell.lastRotationIndex = angle;

        cell.color.x = prevColor.x * (1.0f - interpolation) + newColor.x * interpolation;
        cell.color.y = prevColor.y * (1.0f - interpolation) + newColor.y * interpolation;
        cell.color.z = prevColor.z * (1.0f - interpolation) + newColor.z * interpolation;
    }

    private iVec3 getAjacentCellColor(int x, int y, int dx, int dy) {
        int xx = x + dx;
        int yy = y + dy;

        if(xx < 0) xx = gridSize.x - 1;
        if(xx >= gridSize.x) xx = 0;
        if(yy < 0) yy = gridSize.y - 1;
        if(yy >= gridSize.y) yy = 0;

        return grid[yy][xx].color;
    }

    private float getAccumulationValue(Cell cell, int x, int y) {
        float accum = 0;

        for ( int i = -R; i < R+1; i++)
            for ( int j = -R; j < R+1; j++) {
                int xx = x + j;
                int yy = y + i;

                if(xx < 0) xx = gridSize.x - 1;
                if(xx >= gridSize.x) xx = 0;
                if(yy < 0) yy = gridSize.y - 1;
                if(yy >= gridSize.y) yy = 0;

                accum += grid[yy][xx].color.x;
            }

        return accum;
    }










    /* Methods related to the GUI tab */
    public void buildTab(Tab tab) {
        VBox mainLayout = new VBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(7);

        CheckBox rotationCheckbox = new CheckBox("Rotate Indexes");
        rotationCheckbox.setSelected(true);


        Label minTlabel = new Label("Minimum Threshold: ");
        Slider minTslider = new Slider();
        minTslider.setMaxWidth(160);
        minTslider.setMin(-5);
        minTslider.setMax(8);
        minTslider.setValue(2.25f);
        minTslider.setShowTickLabels(true);
        minTslider.setShowTickMarks(true);
        minTslider.setMajorTickUnit(1);
        minTslider.setMinorTickCount(2);
        minTslider.setBlockIncrement(0.05f);

        Label maxTlabel = new Label("Maximum Threshold: ");
        Slider maxTslider = new Slider();
        maxTslider.setMaxWidth(160);
        maxTslider.setMin(0);
        maxTslider.setMax(15);
        maxTslider.setValue(6.7f);
        maxTslider.setShowTickLabels(true);
        maxTslider.setShowTickMarks(true);
        maxTslider.setMajorTickUnit(1);
        maxTslider.setMinorTickCount(2);
        maxTslider.setBlockIncrement(0.05f);

        Label interpolationLabel = new Label("Interpolation: ");
        Slider interpolationSlider = new Slider();
        interpolationSlider.setMaxWidth(160);
        interpolationSlider.setMin(0);
        interpolationSlider.setMax(1);
        interpolationSlider.setValue(0.8f);
        interpolationSlider.setShowTickLabels(true);
        interpolationSlider.setShowTickMarks(true);
        interpolationSlider.setMajorTickUnit(0.25f);
        interpolationSlider.setMinorTickCount(2);
        interpolationSlider.setBlockIncrement(0.015f);


        rotationCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                rotateIndexes = new_val;
            }
        });

        minTslider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                minThreshold = new_val.floatValue();
            }
        });

        maxTslider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                maxThreshold = new_val.floatValue();
            }
        });

        interpolationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                interpolation = new_val.floatValue();
            }
        });

        mainLayout.getChildren().addAll(rotationCheckbox, minTlabel, minTslider, maxTlabel, maxTslider, interpolationLabel,
                interpolationSlider);

        tab.setContent(mainLayout);
    }
}
