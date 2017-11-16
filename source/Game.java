public class Game {

    // The following five constants were defined in the starter code (kt54)
    private static String FOXPLAYS_MSG      = "Fox plays. Enter move:";
    private static String GEESEPLAY_MSG     = "Geese play. Enter move:";
    private static String ILLEGALMOVE_MSG   = "Illegal move!";
    private static String FOXWINS_MSG       = "Fox wins!";
    private static String GEESEWIN_MSG      = "Geese win!";

    //To keep track of which pieces move next
    private static boolean GOOSE_TURN       = true;

    //To check if there is a valid fox move on the board - the win condition for the geese
    private static boolean validFoxMove     = false;

    //The position of the fox
    private static int xFox                 = 0;
    private static int yFox                 = 0;

    //To avoid printing illegal move twice on the goose turn
    private static boolean gooseJustPlayed  = false;

    //Defining the associated board
    private Board gameBoard = new Board();

    // Minimal constructor. Expand as needed (kt54)
    public Game() {
        gameBoard = new Board();
    }


    // Build on this method to implement game logic.
    public void play() {

        //Creating an instance of EasyIn2 for user input
        EasyIn2 reader = new EasyIn2();

        //Starting the while loop
        boolean done = false;



        while(!done) {
            //Printing the board at the start of each turn
            gameBoard.printBoard();

            //The goose hasn't played in this loop, so the fox's "invalid move" may need to be called
            gooseJustPlayed = false;

            //Printing messages to signal the start of a turn
            if (GOOSE_TURN == true) {
                System.out.println(GEESEPLAY_MSG);
            }
            if (GOOSE_TURN == false) {
                System.out.println(FOXPLAYS_MSG);
            }

            //Takes the input of the current (%1) and proposed new (%2) squares for the move
            int x1 = reader.getInt();
            int y1 = reader.getInt();
            int x2 = reader.getInt();
            int y2 = reader.getInt();

            //Validating that the input is within the board limits
            if (checkInput(x1, x2, y1, y2)) {

                //Goose turn
                if (GOOSE_TURN == true) {

                    //Checking that the current square is a goose
                    if (gameBoard.getBoard()[x1][y1] == gameBoard.getGOOSE()) {

                        //Moving the goose
                        gooseMove(x1, x2, y1, y2);

                        //The goose has just played (or tried to play), so the fox's illegal move message should not fire
                        gooseJustPlayed = true;

                        //Finds the fox's current position to check the geese win condition
                        findFox();

                        //Resets the boolean. If the fox has valid moves, testFoxMove will update it.
                        validFoxMove = false;

                        //Loops through a 5x5 square around the fox to test all input values for testFoxMoves
                        for (int i = Math.max(xFox - 2, gameBoard.getMinIndex()); i <= Math.min(xFox + 2, gameBoard.getMaxIndex()); i++) {
                            for (int j = Math.max(yFox - 2, gameBoard.getMinIndex()); j <= Math.min(yFox + 2, gameBoard.getMaxIndex()); j++) {

                                //Tests to see if an input move is a legal move for the fox
                                testFoxMoves(xFox, yFox, i, j);

                            }
                        }

                        //If the fox has no valid moves, print the final board and break the game loop
                        if (!validFoxMove) {

                            gameBoard.printBoard();
                            System.out.println(GEESEWIN_MSG);
                            done = true;

                        }
                    }

                    else {

                        System.out.println(ILLEGALMOVE_MSG);

                    }
                }

                //Fox turn
                if (GOOSE_TURN == false) {

                    //Checks that the current square is a fox
                    if (gameBoard.getBoard()[x1][y1] == gameBoard.getFOX()) {

                        //Moving the fox
                        foxMove(x1, x2, y1, y2);

                        //If there are no geese on the board, print the final board and break the game loop
                        if (checkFoxWin()) {

                            gameBoard.printBoard();
                            System.out.println(FOXWINS_MSG);
                            done = true;

                        }
                    }

                    //Only prints illegal move if the goose loop above did not fire
                    else if (gooseJustPlayed == false) {

                        System.out.println(ILLEGALMOVE_MSG);

                    }
                }


                //A debugging method to stop the loop if there is a problem.
                /*if (x1 == 9 && x2 == 9 && y1 == 9 && y2 == 9) {
                    done = true;
                }*/

            }

            //Prints the illegal move message without firing any moves if the user input is outside the board
            else{
                System.out.println(ILLEGALMOVE_MSG);
            }
        }
    }

    public void foxMove(int x1, int x2, int y1, int y2){

        //Creates a value for the total distance from the current square to proposed new square
        int validMove = Math.abs(x2-x1)+Math.abs(y2-y1);

        //Move the fox differently depending on the distance between current and proposed new squares
        switch (validMove) {

            case 1: //The fox is moving normally vertically or horizontally

                //If the proposed new square is free, move the fox, and make the next turn the goose's turn
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE()) {

                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = true;

                }

                //The distance is legal, but the new square is not. Print illegal message without changing turn
                else{

                    System.out.println(ILLEGALMOVE_MSG);

                }

                break;

            case 2: //The fox is moving either diagonally normally, jumping vertically, or jumping horizontally

                //If the fox is moving normally diagonally and the square is free, move the fox
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && Math.abs(x2 - x1) == Math.abs(y2 - y1)) {

                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    GOOSE_TURN = true;

                }

                //The fox is jumping vertically or horizontally
                else {

                    //If the square is free, and a goose is in the middle to jump over: move the fox, eat the goose, and change turn
                    if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && ((x2 - x1) != (y2 - y1))
                            && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {

                        gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                        gameBoard.eatGoose(x1+(x2-x1)/2, y1+(y2-y1)/2);
                        GOOSE_TURN = true;

                    }

                    //The distance is legal, but the other factors are not. Print illegal message without changing turn
                    else {

                        System.out.println(ILLEGALMOVE_MSG);

                    }
                }

                break;


            case 4: //The fox is jumping over a goose diagonally

                //If the new square is free, the movement is diagonal, and there is a goose to jump over: Move the fox, eat the goose and change turn
                if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (Math.abs(x2 - x1) == Math.abs(y2 - y1)) && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE()) {

                    gameBoard.makeMove(x1, y1, x2, y2, GOOSE_TURN);
                    gameBoard.eatGoose(x1+(x2-x1)/2, y1+(y2-y1)/2);
                    GOOSE_TURN = true;

                }

                //The distance is legal but other factors are not. Print illegal message without changing turn
                else{

                    System.out.println(ILLEGALMOVE_MSG);

                }
                break;

            default: //The distance is either 0, or too far for a legal move

                //Print illegal move without changing turn
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
                    if (gameBoard.getBoard()[x2][y2] == gameBoard.getFREE() && (Math.abs(x2 - x1) != Math.abs(y2 - y1) && gameBoard.getBoard()[x1+(x2-x1)/2][y1+(y2-y1)/2] == gameBoard.getGOOSE())
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