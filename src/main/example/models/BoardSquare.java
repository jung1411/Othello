package main.example.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class BoardSquare extends ImageView {

    private Image emptySquare = new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResource("blank.png")).toExternalForm());
    private Image blackSquare = new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResource("black.png")).toExternalForm());
    private Image whiteSquare = new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResource("white.png")).toExternalForm());
    private Image moveSquare = new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResource("move.png")).toExternalForm());
    private int squareType;

    public BoardSquare(int x, int y, int squareType) {
        super();
        switch(squareType) {
            case 0:
                setImage(emptySquare);
                this.squareType = squareType;
                break;
            case 1:
                setImage(whiteSquare);
                this.squareType = squareType;
                break;
            case 2:
                setImage(blackSquare);
                this.squareType = squareType;
                break;
            case 3:
                setImage(moveSquare);
                this.squareType = squareType;
                break;
            default:
                break;
        }

        // fit squares to board
        setFitHeight(60);
        setPreserveRatio(true);
        setSmooth(true);
        setCache(true);
        this.setX(x * 60);
        this.setY(y * 60);

    }



    public int getSquareType() {
        return squareType;
    }



    public void setTileType(int type) {
        this.squareType = type;
    }
}
