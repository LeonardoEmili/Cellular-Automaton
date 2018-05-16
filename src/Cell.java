public class Cell {

    int y, x;
    private boolean status, nextStatus = false;

    public Cell(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public boolean isAlive() {
        return this.status;
    }

    public void setToAlive() {
        this.status = this.nextStatus = true;
    }

    public void breath() {
        this.nextStatus = true;
    }

    public void die() {
        this.nextStatus= false;
    }

    public void updateStatus() {
        this.status = this.nextStatus;
    }


    public boolean getStatus () {
        return this.status;
    }

    public boolean getNextStatus() {
        return this.nextStatus;
    }
}
