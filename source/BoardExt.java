public class BoardExt {

    // The following five constants were defined in the starter code (kt54)
    private static final int  DEFAULT_SIZE = 6;
    private static final char FREE    = '.';
    private static final char INVALID = ' ';
    private static final char FOX     = '*';
    private static final char GOOSE   = 'o';
    private static final int  MIN_INDEX = 0;
    private static final int  MAX_INDEX = DEFAULT_SIZE-1;
    private static final int  THIRD_OF_BOARD = DEFAULT_SIZE/3;


    private int boardsize;
    private char[][] board;

    private void invalid2x2Square(int x , int y) {
        board[x][y] = INVALID;
        board[x][y+1] = INVALID;
        board[x+1][y] = INVALID;
        board[x+1][y+1] = INVALID;
    }

    private void invalid3x3Square(int x , int y){
        for(int i = x-1; i <= x+1; i++){
            for (int j = y-1; j <= y+1; j++){
                board[i][j] = INVALID;
            }
        }
    }

    // Default constructor was provided by the starter code. Extend as needed (kt54)
    public BoardExt() {
        this.boardsize = DEFAULT_SIZE;

        board = new char[boardsize][boardsize];

        // Clear all playable fields
        for(int x=0; x<boardsize; x++) {
            for (int y = 0; y < boardsize; y++) {
                board[x][y] = FREE;
                if((x < THIRD_OF_BOARD || x >= boardsize-THIRD_OF_BOARD) && (y < THIRD_OF_BOARD || y >= boardsize-THIRD_OF_BOARD)){
                    board[x][y] = INVALID;
                }
            }
        }
        /*Fill all playable fields with geese to check geese win condition
        for(int x=0; x<boardsize; x++)
            for(int y=0; y<boardsize; y++)
                board[x][y] = GOOSE;*/

        /*Replace corners with invalid 2x2 squares
        invalid2x2Square(MIN_INDEX , MIN_INDEX);
        invalid2x2Square(MIN_INDEX  , MAX_INDEX - 1 );
        invalid2x2Square(MAX_INDEX - 1 , MIN_INDEX);
        invalid2x2Square(MAX_INDEX - 1 , MAX_INDEX - 1);*/

        //Replace corners with invalid 3x3 squares


        // Put a single fox in the starting position
        board[3][4] = FREE;
        board[boardsize/2][(boardsize+(boardsize/2))/2] = FOX;

        //Put geese in starting positions
        for(int i = MIN_INDEX; i <= MAX_INDEX; i++){
            for(int j = MIN_INDEX; j <= MAX_INDEX/2; j++){
                if(board[i][j] == FREE) {
                    board[i][j] = GOOSE;
                    //not sure how to avoid "magic constants" here

                    if(j == MAX_INDEX/2 && i >= THIRD_OF_BOARD && i < boardsize-THIRD_OF_BOARD){
                        board[i][j] = FREE;
                    }

                }
            }
        }
    }



    // Prints the board. This method was provided with the starter code. Better not modify to ensure
    // output consistent with the autochecker (kt54)
    public void printBoard() {

        for(int y=0; y<boardsize; y++)
        {
            for(int x=0; x<boardsize; x++) {
                System.out.print(" ");
                switch(board[x][y]) {
                    case FREE:
                        System.out.print(".");
                        break;
                    case FOX:
                        System.out.print("*");
                        break;
                    case GOOSE:
                        System.out.print("o");
                        break;
                    default:
                        System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    public char[][] getBoard() {
        return this.board;
    }

    public static char getGOOSE() {
        return GOOSE;
    }

    public static char getFOX() {
        return FOX;
    }

    public static char getFREE() {
        return FREE;
    }

    public static char getINVALID() {
        return INVALID;
    }

    public static int getMaxIndex() {
        return MAX_INDEX;
    }

    public static int getMinIndex() {
        return MIN_INDEX;
    }

    public void makeMove(int x1, int y1, int x2, int y2, boolean goose) {
        board[x1][y1] = FREE;
        if(goose){
            board[x2][y2] = GOOSE;
        }
        else{
            board[x2][y2] = FOX;
        }
    }
    public void eatGoose(int x, int y){
        board[x][y] = FREE;
    }
}