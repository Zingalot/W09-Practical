public class Game {

    // The following five constants were defined in the starter code (kt54)
    private static boolean GOOSE_TURN       = true;
    private static String FOXPLAYS_MSG      = "Fox plays. Enter move:";
    private static String GEESEPLAY_MSG     = "Geese play. Enter move:";
    private static String ILLEGALMOVE_MSG   = "Illegal move!";
    private static String FOXWINS_MSG       = "Fox wins!";
    private static String GEESEWIN_MSG      = "Geese win!";
    private static boolean validFoxMove     = false;
    private static int xFox                 = 0;
    private static int yFox                 = 0;
    private static int gameStart            = 1;
    private static boolean gooseJustPlayed  = false;

    private Board gameBoard = new Board();

    // Minimal constructor. Expand as needed (kt54)
    public Game() {
        gameBoard = new Board();
    }


    // Build on this method to implement game logic.
    public void play() {

        EasyIn2 reader = new EasyIn2();



        boolean done = false;



        while(!done) {
            gameBoard.printBoard();
            gooseJustPlayed = false;
            /*if (gameStart == 1) {
                System.out.println("Fox Starts, Enter the co-ordinates");
                gameStart++;
            }*/
            if (GOOSE_TURN == true) {
                System.out.println(GEESEPLAY_MSG);
            }
            if (GOOSE_TURN == false) {
                System.out.println(FOXPLAYS_MSG);
            }
            //Need to check that these inputs are inside the board to avoid an out of bounds exception
            int x1 = reader.getInt();
            int y1 = reader.getInt();
            int x2 = reader.getInt();
            int y2 = reader.getInt();
            if (checkInput(x1, x2, y1, y2)) {

                //Goose turn
                if (GOOSE_TURN == true) {
                    if (gameBoard.getBoard()[x1][y1] == gameBoard.getGOOSE()) {
                        gooseMove(x1, x2, y1, y2);
                        gooseJustPlayed = true;
                        findFox();
                        for (int i = Math.max(xFox - 2, gameBoard.getMinIndex()); i <= Math.min(xFox + 2, gameBoard.getMaxIndex()); i++) {
                            for (int j = Math.max(yFox - 2, gameBoard.getMinIndex()); j <= Math.min(yFox + 2, gameBoard.getMaxIndex()); j++) {
                                testFoxMoves(x1, y1, i, j);
                            }
                        }
                        if (!validFoxMove) {
                            gameBoard.printBoard();
                            System.out.println(GEESEWIN_MSG);
                            done = true;
                        }
                    } else {
                        System.out.println(ILLEGALMOVE_MSG);
                    }
                }

                //Fox turn
                if (GOOSE_TURN == false) {
                    if (gameBoard.getBoard()[x1][y1] == gameBoard.getFOX()) {
                        foxMove(x1, x2, y1, y2);
                        if (checkFoxWin()) {
                            gameBoard.printBoard();
                            System.out.println(FOXWINS_MSG);
                            done = true;
                        }
                    } else if (gooseJustPlayed == false) {
                        System.out.println(ILLEGALMOVE_MSG);
                    }
                }

                if (x1 == 9 && x2 == 9 && y1 == 9 && y2 == 9) {
                    done = true;
                }


                // This is just demonstration code, so we immediately let geese win
                // to avoid unnecessary violence.
                //System.out.println(GEESEWIN_MSG);
                //done = true;
            }
            else{
                System.out.println(ILLEGALMOVE_MSG);
            }
        }
    }

    public void foxMove(int x1, int x2, int y1, int y2){
        int validMove = Math.abs(x2-x1)+Math.abs(y2-y1); //A value for the distance between the starting square and move square
        switch (validMove) {
            //The fox is moving normally vertically or horizontally
            case 1:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = true;
                }
                else{
                    System.out.println(ILLEGALMOVE_MSG);
                }
                break;

            case 2:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && Math.abs(x2 - x1) == Math.abs(y2 - y1))/*The fox is moving normally diagonally*/ {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = true;
                }
                else {//The fox is jumping vertically or horizontally over a goose
                    if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && ((x2 - x1) != (y2 - y1))
                            && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
                        gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                        gameBoard.eatGoose(x1+(x2-x1)/2, y1+(y2-y1)/2);
                        GOOSE_TURN = true;
                    }
                    else {
                        System.out.println(ILLEGALMOVE_MSG);
                    }
                }
                break;
            //The fox is jumping over a goose diagonally
            case 4:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (Math.abs(x2 - x1) == Math.abs(y2 - y1)) && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    gameBoard.eatGoose(x1+(x2-x1)/2, y1+(y2-y1)/2);
                    GOOSE_TURN = true;
                }
                else{
                    System.out.println(ILLEGALMOVE_MSG);
                }
                break;
            default:
                System.out.println(ILLEGALMOVE_MSG);
                break;
        }
    }

    public void gooseMove (int x1, int x2, int y1, int y2){
        int validMove = Math.abs(x2-x1)+Math.abs(y2-y1);
        switch (validMove) {
            //The goose is moving vertically or horizontally
            case 1:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = false;
                }
                else{
                    System.out.println(ILLEGALMOVE_MSG);
                }
                break;
                //The goose is moving diagonally
            case 2:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && Math.abs(x2 - x1) == Math.abs(y2 - y1)) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = false;
                }
                else{
                    System.out.println(ILLEGALMOVE_MSG);
                }
                break;
            //The goose is moving illegally
            default:
                System.out.println(ILLEGALMOVE_MSG);
                break;
        }
    }

    public void testFoxMoves(int x1, int y1, int x2, int y2){
        validFoxMove = false;
        int validMove = Math.abs(x2-x1)+Math.abs(y2-y1);
        switch (validMove) {
            //The fox is moving normally vertically or horizontally
            case 1:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {
                    validFoxMove = true;
                    break;
                }

            case 2:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && Math.abs(x2 - x1) == Math.abs(y2 - y1))/*The fox is moving normally diagonally*/ {
                    validFoxMove = true;
                }
                else {//The fox is jumping vertically or horizontally over a goose
                    if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (Math.abs(x2 - x1) != Math.abs(y2 - y1))
                            && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
                        validFoxMove = true;
                    }
                }
                break;
            //The fox is jumping over a goose diagonally
            case 4:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (Math.abs(x2 - x1) == Math.abs(y2 - y1)) && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
                    validFoxMove = true;
                }
                break;
            default:
                break;
        }
    }

    public void findFox(){
        for(int i = gameBoard.getMinIndex(); i <= gameBoard.getMaxIndex(); i++){
            for(int j = gameBoard.getMinIndex(); j <= gameBoard.getMaxIndex(); j++){
                if(gameBoard.getBoard()[i][j] == gameBoard.getFOX()){
                    xFox = i;
                    yFox = j;
                }
            }
        }
    }
    public boolean checkFoxWin(){
        boolean foxWin = true;
        for( int i = gameBoard.getMinIndex(); i <= gameBoard.getMaxIndex(); i++){
            for( int j = gameBoard.getMinIndex(); j <= gameBoard.getMaxIndex(); j++){
                if(gameBoard.getBoard()[i][j] == gameBoard.getGOOSE()){
                    foxWin = false;
                }
            }
        }
        return foxWin;
    }
    public boolean checkInput(int x1,int x2, int y1, int y2){
        boolean validInput = true;
        if(gameBoard.getMinIndex() > x1 || x1 > gameBoard.getMaxIndex()){
            validInput = false;
        }
        if(gameBoard.getMinIndex() > x2 || x2 > gameBoard.getMaxIndex()){
            validInput = false;
        }
        if(gameBoard.getMinIndex() > y1 || y1 > gameBoard.getMaxIndex()){
            validInput = false;
        }
        if(gameBoard.getMinIndex() > y2 || y2 > gameBoard.getMaxIndex()){
            validInput = false;
        }
        return validInput;
    }
}