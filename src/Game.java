public class Game {

    // The following five constants were defined in the starter code (kt54)
    private static String FOXPLAYS_MSG      = "Fox plays. Enter move:";
    private static String GEESEPLAY_MSG     = "Geese play. Enter move:";
    private static String ILLEGALMOVE_MSG   = "Illegal move!";
    private static String FOXWINS_MSG       = "Fox wins!";
    private static String GEESEWIN_MSG      = "Geese win!";

    private Board gameBoard;

    // Minimal constructor. Expand as needed (kt54)
    public Game() {
        gameBoard = new Board();
    }

    // Build on this method to implement game logic.
    public void play() {

        EasyIn2 reader = new EasyIn2();

        gameBoard = new Board();

        boolean done = false;

        while(!done) {
            gameBoard.printBoard();

            System.out.println(GEESEPLAY_MSG);

            int x1 = reader.getInt();
            int y1 = reader.getInt();
            int x2 = reader.getInt();
            int y2 = reader.getInt();

            // This is just demonstration code, so we immediately let geese win
            // to avoid unnecessary violence.
            System.out.println(GEESEWIN_MSG);
            done = true;
        }
    }
}