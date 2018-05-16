public class Main {

    public static void main(String[] args) {

        // Wrapper w = new Wrapper();
        NonSyncBoard board1 = new NonSyncBoard(10, 10);
        board1.display();
        int refresh = 5;
        while (refresh != 0) {
            try { Thread.currentThread().sleep(500); } catch (InterruptedException e) {}
            board1.update();
            board1.display();
            refresh--;
        }
    }
}