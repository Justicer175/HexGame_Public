package hr.java.game.hex_projekt_maksrezek.savingAndLoading;

import hr.java.game.hex_projekt_maksrezek.model.Colors;
import hr.java.game.hex_projekt_maksrezek.model.PolygonSpecifics;
import hr.java.game.hex_projekt_maksrezek.model.Tile;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class ConverterForSaveAndLoad implements Serializable {
    public static final String SAVE_GAME_FILE_NAME = "saveGame/save1.dat";
    public static final Integer NUMBER_OF_ROWS = 13;
    public static final Integer NUMBER_OF_COLUMNS = 13;

    private String[][] gameBoardState;
    private boolean playerOnePlaying;
    private Integer numberOfTurnsPlayed;

    //only for multiplayer
    private boolean resetGame = false;

    public static String[][] convertoToString(PolygonSpecifics[][] polygonSpecifics){
        String[][] gameInString = new String[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];

        for(int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                gameInString[i][j] = polygonSpecifics[i][j].color.toString();
                //System.out.println(polygonSpecifics[i][j].color.toString());
            }
            //System.out.println("\n");
        }

        return gameInString;
    }

    public static void convertToGame(String[][] gameBoardState, PolygonSpecifics[][] gameField) {

        for(int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {

                if(gameBoardState[i][j].equals("BLUE")){
                    gameField[i][j].polygon.setFill(Color.BLUE);
                    gameField[i][j].color = Colors.BLUE;
                }
                else if(gameBoardState[i][j].equals("RED")){
                    gameField[i][j].polygon.setFill(Color.RED);
                    gameField[i][j].color = Colors.RED;
                }
                else if(gameBoardState[i][j].equals("WHITE")){
                    gameField[i][j].polygon.setFill(Color.ANTIQUEWHITE);
                    gameField[i][j].color = Colors.WHITE;
                }
                else {
                    System.out.println("ERROR on this location when loading the game");
                    System.out.println(gameBoardState[i][j]);
                }
                gameField[i][j].deadEnd = false;
            }
        }
    }
}
