package main.example.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.example.models.Direction;
import main.example.models.BoardSquare;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    /**
     * JavaFX
     **/
    @FXML
    private Pane gamePane;
    @FXML
    private JFXDialog jfxDialog;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private StackPane root;
    @FXML
    private JFXButton button1;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

    // All squares on board
    private final Group squaresGrid = new Group();

    // displayed board
    int[][] boardDisplay;

    // squares between pieces, if white, select all pieces between piece placed and far piece
    private int[][] squaresBetween;
    
    // players one and two
    int playerOne = 1;
    int playerTwo = 2;
    
    //Players score
    int whiteScore;
    int blackScore;
    // Display next possible move
    boolean showHelp;
    
    // size of square on board, usually 60, board is 600
    double squareSize;

    // Set board size to 10x10
    int width = 10;
    int height = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showHelp = true;
        //add squares to board
        gamePane.getChildren().setAll(squaresGrid);
        // set size of square
        squareSize = (double) 600 / (double) width;

        // initialize board
        boardDisplay = new int[width][height];

        // initialize squares between moved piece and color piece
        squaresBetween = new int[width][height];

        // cLear board for a new start
        resetBoard();

        // reset scores and all values to zero
        updatePlayerScore();

        // Mouse click on square
        gamePane.setOnMouseClicked(this::setClickedSquare);

    }

    // Function to get the square clicked
    private void setClickedSquare(MouseEvent mouse) {
        // since board is 600 by 600, mouse value will be for example 432, 420,
        // since square size is 60, you divide 432 b6 60 tjen cast to into to find
        // x position and 420 by 60 to get y position
        int x =(int) (mouse.getX() / squareSize);
        int y =(int) (mouse.getY() / squareSize);
        movePiece(x, y);

    }



// Move piece to square9x,y)
    private void movePiece(int x, int y) {
        // set player turns to play
        playerTurns();

        // If move is invalid. return
        if (boardDisplay[x][y] != 0 && boardDisplay[x][y] != 3) {
            return;
        }

        // Find possible moves to make by current player

        if (findPossibleMoves(x, y, true)) {
            setSquare(x, y, playerOne);
            // find squares between current move and flip them
            flipSquaresBetween();
            alternatePlayers();
            // set player turns
            if (anyMovePossible()) {
                // No move is possible, skip turn
                System.out.println("Player " + playerOne + " has NO LEGAL MOVE.");
                alternatePlayers();
                isGameOver();
            }
            updatePlayerScore();
        } else {
            // Invalid move
            System.out.println("Can't make this move.");
        }

        // Display next possible move
        displayNextMoveMarker();
        // Update board
        updateBoardDisplayAndPieces();
    }

    private boolean findPossibleMoves(int x, int y, boolean squaresSet) {
        //if square value is unset, return
        if (boardDisplay[x][y] != 0 && boardDisplay[x][y] != 3) {
            return false;
        }

        // Create posibl moves
        boolean hasAValidPath = false;
        Direction[] possibleMove = new Direction[8];
        // Like Queen in chess, add 8 movement directions
        possibleMove[0] = new Direction(0, 1);
        possibleMove[1] = new Direction(1, 1);
        possibleMove[2] = new Direction(1, 0);
        possibleMove[3] = new Direction(1, -1);
        possibleMove[4] = new Direction(0, -1);
        possibleMove[5] = new Direction(-1, -1);
        possibleMove[6] = new Direction(-1, 0);
        possibleMove[7] = new Direction(-1, 1);

        for (Direction direction : possibleMove) {
            // find all posible moves on every pice and add marker to it

            // current x and y possition
            int x_pos = x;
            int y_pos = y;
            // increment positon
            x_pos += direction.getDeltaX();
            y_pos += direction.getDeltaY();
            int[][] tempBoard = new int[width][height];


            if (isValidMove(x_pos, y_pos)
                    && boardDisplay[x_pos][y_pos] == playerTwo) {
                tempBoard[x_pos][y_pos] = 1;
                x_pos += direction.getDeltaX();
                y_pos += direction.getDeltaY();

                while (isValidMove(x_pos, y_pos) && boardDisplay[x_pos][y_pos] == playerTwo) {
                    tempBoard[x_pos][y_pos] = 1;
                    x_pos += direction.getDeltaX();
                    y_pos += direction.getDeltaY();
                }

                if (isValidMove(x_pos, y_pos) && boardDisplay[x_pos][y_pos] == playerOne) {
                    hasAValidPath = true;
                    if (squaresSet) {
                        for (int print_x = 0; print_x < width; print_x++) {
                            for (int print_y = 0; print_y < height; print_y++) {
                                if (tempBoard[print_x][print_y] == 1) {
                                    squaresBetween[print_x][print_y] = tempBoard[print_x][print_y];
                                }
                            }
                        }
                    }
                }


            }
        }

        return hasAValidPath;
    }


// No more valid moves
    private void isGameOver() {
        if (anyMovePossible()) {
            System.out.println("\nGAME OVER\n");
            updatePlayerScore();

            String winner;
            if (whiteScore > blackScore) {
                winner = "White";
            } else if (blackScore > whiteScore) {
                winner = "Black";
            } else {
                winner = "TIE";
            }
            System.out.println("Winner: " + winner);

            if (blackScore != whiteScore) {
                label1.setText(winner + " wins!");
            } else {
                label1.setText("TIE!");
            }
            label2.setText("End Score\n\nWhite: " + whiteScore + "\nBlack: " + blackScore);
            label2.setAlignment(Pos.CENTER_LEFT);
            jfxDialog.setTransitionType(JFXDialog.DialogTransition.TOP);
            jfxDialog.setOverlayClose(false);
            jfxDialog.show(root);
            button1.setOnMouseClicked((e) -> {
                resetBoard();
                jfxDialog.close();
            });

        }
    }


    // Move methods
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private boolean anyMovePossible() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (findPossibleMoves(x, y, false)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void displayNextMoveMarker() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (boardDisplay[x][y] == 3) {
                    boardDisplay[x][y] = 0;
                }
                if (showHelp && findPossibleMoves(x, y, false)) {
                    boardDisplay[x][y] = 3;
                }
            }
        }
    }

    // Square methods
    private void flipSquaresBetween() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (squaresBetween[x][y] == 1) {
                    if (boardDisplay[x][y] == 1) {
                        setSquare(x, y, 2);
                    } else if (boardDisplay[x][y] == 2) {
                        setSquare(x, y, 1);
                    }
                }
            }
        }
        squaresBetween = new int[width][height];
    }

    public void setSquare(int x, int y, int player) {
        boardDisplay[x][y] = player;
    }

    // Board methods
    public void updateBoardDisplayAndPieces() {
        squaresGrid.getChildren().clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                BoardSquare boardSquare = new BoardSquare(j, i, boardDisplay[j][i]);
                squaresGrid.getChildren().add(boardSquare);

            }
        }

    }

    private void resetBoard() {
        boardDisplay = new int[width][height];
        squaresBetween = new int[width][height];

        boardDisplay[(int) (width / (float) 2 - 0.5)][(int) (height / (float) 2 - 0.5)] = 1;
        boardDisplay[(int) (width / (float) 2 + 0.5)][(int) (height / (float) 2 - 0.5)] = 2;
        boardDisplay[(int) (width / (float) 2 - 0.5)][(int) (height / (float) 2 + 0.5)] = 2;
        boardDisplay[(int) (width / (float) 2 + 0.5)][(int) (height / (float) 2 + 0.5)] = 1;

        playerOne = 1;
        playerTwo = 2;

        label4.setText(Integer.toString(0));
        label3.setText(Integer.toString(0));

        printCurrentTurn();
        displayNextMoveMarker();
        updateBoardDisplayAndPieces();
    }

    // Player methods
    private void alternatePlayers() {
        if (playerOne == 1) {
            playerOne = 2;
        } else if (playerOne == 2) {
            playerOne = 1;
        }
        playerTurns();
        printCurrentTurn();
    }

    private void printCurrentTurn() {
        System.out.println("\n\n");
        if (playerOne == 1) {
            System.out.println("It's WHITE's turn!");
        }
        else if(playerOne == 2) {
            System.out.println("It's BLACK's turn!");
        }

    }


    private void updatePlayerScore() {
        whiteScore = 0;
        blackScore = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (boardDisplay[x][y] == 1) {
                    whiteScore++;
                } else if (boardDisplay[x][y] == 2) {
                    blackScore++;
                }
            }
        }

        System.out.println("WHITE: " + whiteScore);
        label4.setText(Integer.toString(whiteScore));
        System.out.println("BLACK: " + blackScore);
        label3.setText(Integer.toString(blackScore));
    }

    public void playerTurns() {
        if (playerOne == 1) {
            playerTwo = 2;
        } else if (playerOne == 2) {
            playerTwo = 1;
        }
    }

}
