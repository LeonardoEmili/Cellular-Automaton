package simulatorWindow.utils;

public class Wrapper {

    private volatile int ctrl;

    public void increaseCtrl() { ctrl++; }

    public int getCtrl() { return ctrl; }

    public void setCtrl() { ctrl = 0; }
}
