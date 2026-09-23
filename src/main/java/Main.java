import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String GREY = "\u001B[39m";

    private static final int ROW_COUNT = 3;
    private static final int COL_COUNT = 3;

    private static final String CELL_STATE_EMPTY = "■";
    private static final String CELL_STATE_X = GREEN+"X"+RESET;
    private static final String CELL_STATE_O = RED+"O"+RESET;

    private static final String GAME_STATE_X_WIN = GREEN+"Выиграли: вы"+RESET;
    private static final String GAME_STATE_O_WIN = RED+"Выиграли: бот"+RESET;
    private static final String GAME_STATE_DRAW = GREY+"Ничья"+RESET;
    private static final String GAME_STATE_NOT_FINISHED = "Игра не окончена";

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();


    static void main() {
        System.out.println(GREEN+"TicTakToe"+RESET);
        do {
            System.out.print("\nХотите сыграть? [Д]а [Н]ет: ");
            String isPlay = scanner.nextLine();
            if (isPlay.equalsIgnoreCase("Д")){
                String[][] board = createBoard();
                showBoard(board);
                startLoopGame(board);
            }else if(isPlay.equalsIgnoreCase("Н")) {
                System.out.print("До встеричи");
                return;
            }else {
                System.out.println(RED+"Введите коректное значение!"+RESET);
            }
        } while (true);
    }

    private static void startLoopGame(String[][] board) {

        boolean playerTurn = true;

        do {
            if (playerTurn){
                makePlayerTurn(board);
                showBoard(board);
            }else {
                makeBotTurn(board);
                showBoard(board);
            }
            playerTurn = !playerTurn;


            if (!chekGameState(board).equals(GAME_STATE_NOT_FINISHED)) {
                System.out.print(chekGameState(board));
                return;
            }
        } while (true);


    }

    private static String[][] createBoard() {
        String[][] board = new String[ROW_COUNT][COL_COUNT];

        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                board[row][col] = CELL_STATE_EMPTY;
            }
        }
        return board;
    }

    private static void makePlayerTurn(String[][] board) {
        int[] coordinates = inputCellCoordinates(board);
        board[coordinates[0]][coordinates[1]] = CELL_STATE_X;
        System.out.println("Ваш ход: ");
    }

    private static void makeBotTurn(String[][] board) {
        System.out.println("Ход бота: ");
        do {
            int row = random.nextInt(ROW_COUNT);
            int col = random.nextInt(COL_COUNT);

            if (board[row][col].equals(CELL_STATE_EMPTY)) {
                board[row][col] = CELL_STATE_O;
                return;
            }
        } while (true);

    }


    private static int[] inputCellCoordinates(String[][] board) {
        System.out.print("Введите два числа(ряд и калонку) от 0 до 2 через пробел (0-2): ");

        do {
            String[] input = scanner.nextLine().split(" ");
            int row = Integer.parseInt(input[0]);
            int col = Integer.parseInt(input[1]);

            if ((row < 0) || (row >= ROW_COUNT) || (col < 0) || (col >= COL_COUNT)) {
                System.out.print("Некоректное значение! Введите два числа(ряд и калонку) от 0 до 2 через пробел (0-2): ");
            } else if (!Objects.equals(board[row][col], CELL_STATE_EMPTY)) {
                System.out.print("Данная ячейка уже занята! Введите другие кординаты:");
            } else {
                return new int[]{row, col};
            }
        } while (true);

    }

    private static void showBoard(String[][] board) {
        System.out.println("-----");
        for (int row = 0; row < ROW_COUNT; row++) {
            System.out.print("|");
            for (int col = 0; col < COL_COUNT; col++) {
                System.out.print(board[row][col]);
            }
            System.out.println("|");

        }
        System.out.println("-----");
    }


    public static boolean areAllCellTaken(String[][] board) {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                if (board[row][col].equals(CELL_STATE_EMPTY)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static String chekGameState(String[][] board) {

        ArrayList<Integer> sums = new ArrayList<>();

        for (int row = 0; row < ROW_COUNT; row++) {
            int rowSum = 0;
            for (int col = 0; col < COL_COUNT; col++) {
                rowSum += calculateNumValue(board[row][col]);
            }
            sums.add(rowSum);
        }

        for (int col = 0; col < COL_COUNT; col++) {
            int columSum = 0;
            for (int row = 0; row < ROW_COUNT; row++) {
                columSum += calculateNumValue(board[row][col]);
            }
            sums.add(columSum);
        }

        int leftDiagonalSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            leftDiagonalSum += calculateNumValue(board[i][i]);
        }
        sums.add(leftDiagonalSum);

        int rightDiagonalSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            rightDiagonalSum += calculateNumValue(board[i][(ROW_COUNT - 1) - i]);
        }
        sums.add(rightDiagonalSum);

        if (sums.contains(3)) {
            return GAME_STATE_X_WIN;
        } else if (sums.contains(-3)) {
            return GAME_STATE_O_WIN;
        } else if (areAllCellTaken(board)) {
            return GAME_STATE_DRAW;
        } else {
            return GAME_STATE_NOT_FINISHED;
        }

    }

    private static int calculateNumValue(String cellState) {
        if (cellState.equals(CELL_STATE_X)) {
            return 1;
        } else if (cellState.equals(CELL_STATE_O)) {
            return -1;
        } else {
            return 0;
        }
    }


}
