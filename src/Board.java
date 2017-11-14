public class Board {

    // The following five constants were defined in the starter code (kt54)
    private static final int  DEFAULT_SIZE = 7;
    private static final char FREE    = '.';
    private static final char INVALID = ' ';
    private static final char FOX     = '*';
    private static final char GOOSE   = 'o';
    private static final int  MIN_INDEX = 0;
    private static final int  MAX_INDEX = DEFAULT_SIZE-1;


    private int boardsize;
    private char[][] board;

    private void invalid2x2Square(int x , int y) {
        board[x][y] = INVALID;
        board[x][y+1] = INVALID;
        board[x+1][y] = INVALID;
        board[x+1][y+1] = INVALID;

    }

    // Default constructor was provided by the starter code. Extend as needed (kt54)
    public Board() {
        this.boardsize = DEFAULT_SIZE;

        board = new char[boardsize][boardsize];

        // Clear all playable fields
        for(int x=0; x<boardsize; x++)
            for(int y=0; y<boardsize; y++)
                board[x][y] = FREE;

        //Replace corners with invalid squares
        invalid2x2Square(MIN_INDEX , MIN_INDEX);
        invalid2x2Square(MIN_INDEX  , MAX_INDEX - 1 );
        invalid2x2Square(MAX_INDEX - 1 , MIN_INDEX);
        invalid2x2Square(MAX_INDEX - 1 , MAX_INDEX - 1);

        // Put a single fox in the starting position
        board[3][4] = FREE;
        board[boardsize/2][(boardsize+(boardsize/2))/2] = FOX;

        //Put geese in starting positions
        for(int i = MIN_INDEX; i <= MAX_INDEX; i++){
            for(int j = MIN_INDEX; j <= MAX_INDEX/2; j++){
                if(board[i][j] == FREE) {
                    board[i][j] = GOOSE;
                    //not sure how to avoid "magic constants" here
                    if(j == MAX_INDEX/2 && (i == 2 || i == 3 || i == 4)){
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
        return board;
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
        System.out.println(board[x1][y1]);
        System.out.println("HELLO");
        if(goose){
            board[x2][y2] = GOOSE;
        }
        else{
            board[x2][y2] = FOX;
            printBoard();
        }
    }
    public void eatGoose(int x, int y){
        board[x][y] = FREE;
    }
}