package hr.java.game.hex_projekt_maksrezek.model;

import hr.java.game.hex_projekt_maksrezek.HexGameLogic;
import hr.java.game.hex_projekt_maksrezek.HexGridController;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Tile extends Polygon {
    public final static double r = 20; // the inner radius from hexagon center to outer corner
    public final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    public final static double TILE_HEIGHT = 2 * r;
    public final static double TILE_WIDTH = 2 * n;

    int xStartOffset = 40; // offsets the entire field to the right
    int yStartOffset = 40; // offsets the entire fiels downwards
    public Tile(int coordinateX, int coordinateY, HexGameLogic hGameC, GridPane gameGrid) {

        double x = coordinateY * TILE_WIDTH + (coordinateX % 2) * n + xStartOffset;
        double y = coordinateX * TILE_HEIGHT * 0.75 + yStartOffset;

        //create it
        getPoints().addAll(
                x, y,
                x, y + r,
                x + n, y + r * 1.5,
                x + TILE_WIDTH, y + r,
                x + TILE_WIDTH, y,
                x + n, y - r * 0.5
        );

        //color it
        if(coordinateY == Constants.START_OF_ROWS || coordinateY == Constants.END_OF_ROWS-1){
            setFill(Color.BLUE);
        }
        else if(coordinateX == Constants.START_OF_COLUMNS || coordinateX == Constants.END_OF_COLUMNS-1){
            setFill(Color.RED);
        }
        else {
            setFill(Color.ANTIQUEWHITE);
        }


        setStrokeWidth(1);
        setStroke(Color.BLACK);
        setOnMouseClicked(e -> hGameC.setColor(this, gameGrid));

    }
}
