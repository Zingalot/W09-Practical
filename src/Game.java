public class Game {

    // The following five constants were defined in the starter code (kt54)
    private static boolean GOOSE_TURN       = false;
    private static String FOXPLAYS_MSG      = "Fox plays. Enter move:";
    private static String GEESEPLAY_MSG     = "Geese play. Enter move:";
    private static String ILLEGALMOVE_MSG   = "Illegal move!";
    private static String FOXWINS_MSG       = "Fox wins!";
    private static String GEESEWIN_MSG      = "Geese win!";
    private static boolean validFoxMove     = false;
    private static int xFox                 = 0;
    private static int yFox                 = 0;
    private static int gameStart            = 1;

    private Board gameBoard;

    // Minimal constructor. Expand as needed (kt54)
    public Game() {
        gameBoard = new Board();
    }


    // Build on this method to implement game logic.
    public void play() {

        EasyIn2 reader = new EasyIn2();

        Board gameBoard = new Board();

        boolean done = false;



        while(!done) {
            gameBoard.printBoard();
            if(gameStart == 1){
                System.out.println("Fox Starts, Enter the co-ordinates");
                gameStart++;
            }
            if(GOOSE_TURN == true){
                System.out.println(GEESEPLAY_MSG);
            }
            if(GOOSE_TURN == false){
                System.out.println(FOXPLAYS_MSG);
            }
            int x1 = reader.getInt();
            int y1 = reader.getInt();
            int x2 = reader.getInt();
            int y2 = reader.getInt();


            //Goose turn
            if(GOOSE_TURN == true && gameBoard.getBoard()[x1][y1] == gameBoard.getGOOSE()) {
                gooseMove(x1,x2,y1,y2);
            }

            //Fox turn
            if(GOOSE_TURN == false && gameBoard.getBoard()[x1][y1] == gameBoard.getFOX()) {
                findFox();
                for(int i = xFox-2; i <= Math.min(xFox+2, gameBoard.getMaxIndex()-1); i++){
                    for(int j = yFox-2; j <= Math.min(yFox+2, gameBoard.getMaxIndex()-1); j++){
                        testFoxMoves(x1, x2, i, j);
                    }
                }
                if(!validFoxMove){
                    System.out.println(GEESEWIN_MSG);
                    done = true;
                }
                else{
                    foxMove(x1,x2,y1,y2);
                }
            }






            // This is just demonstration code, so we immediately let geese win
            // to avoid unnecessary violence.
            //System.out.println(GEESEWIN_MSG);
            //done = true;
        }
    }

    public void foxMove(int x1, int x2, int y1, int y2){
        int validMove = Math.abs((x2-x1)+(y2-y1)); //A value for the distance between the starting square and move square
        switch (validMove) {
            //The fox is moving normally vertically or horizontally
            case 1:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = true;
                    System.out.println("Moving Vertically Or Horizontally");
                    System.out.println("x1: " + x1 + ", x2: " + x2 + ", y1: " + y1 + ", y2: " + y2);
                    break;
                }
                else{System.out.println(ILLEGALMOVE_MSG);}

            case 2:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (x2 - x1) == (y2 - y1))/*The fox is moving normally diagonally*/ {
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
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && ((x2 - x1) == (y2 - y1)) && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    gameBoard.eatGoose(x1+(x2-x1)/2, y1+(y2-y1)/2);
                    GOOSE_TURN = true;
                }
                else{System.out.println(ILLEGALMOVE_MSG);}
                break;
            default:
                System.out.print(ILLEGALMOVE_MSG);
                break;
        }
    }

    public void gooseMove (int x1, int x2, int y1, int y2){
        int validMove = Math.abs((x2-x1)+(y2-y1));
        switch (validMove) {
            //The goose is moving vertically or horizontally
            case 1:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = false;
                    break;
                }
                else{System.out.println(ILLEGALMOVE_MSG);}
                //The goose is moving diagonally
            case 2:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (x2 - x1) == (y2 - y1)) {
                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = false;
                }
                else{System.out.println(ILLEGALMOVE_MSG);}
                break;
            //The goose is moving illegally
            default:
                System.out.print(ILLEGALMOVE_MSG);
                break;
        }
    }

    public void testFoxMoves(int x1, int y1, int x2, int y2){
        int validMove = Math.abs((x2-x1)+(y2-y1));
        switch (validMove) {
            //The fox is moving normally vertically or horizontally
            case 1:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {
                    validFoxMove = true;
                    break;
                }

            case 2:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (x2 - x1) == (y2 - y1))/*The fox is moving normally diagonally*/ {
                    validFoxMove = true;
                }
                else {//The fox is jumping vertically or horizontally over a goose
                    if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && ((x2 - x1) != (y2 - y1))
                            && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
                        validFoxMove = true;
                    }
                }
                break;
            //The fox is jumping over a goose diagonally
            case 4:
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && ((x2 - x1) == (y2 - y1)) && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {
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

}