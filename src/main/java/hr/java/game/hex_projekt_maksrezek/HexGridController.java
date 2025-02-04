package hr.java.game.hex_projekt_maksrezek;

import hr.java.game.hex_projekt_maksrezek.chat.ChatConnection;
import hr.java.game.hex_projekt_maksrezek.chat.ChatServer;
import hr.java.game.hex_projekt_maksrezek.chat.ChatService;
import hr.java.game.hex_projekt_maksrezek.gameMoveThreads.GetLastGameMoveThread;
import hr.java.game.hex_projekt_maksrezek.jndi.ConfigurationReader;
import hr.java.game.hex_projekt_maksrezek.model.*;
import hr.java.game.hex_projekt_maksrezek.replayLogic.ReplayGame;
import hr.java.game.hex_projekt_maksrezek.savingAndLoading.ConverterForSaveAndLoad;
import hr.java.game.hex_projekt_maksrezek.utils.DocumentationUtil;
import hr.java.game.hex_projekt_maksrezek.utils.GameMoveUtils;
import hr.java.game.hex_projekt_maksrezek.utils.XmlUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//THIS IS USED TO CONNECT FRONTEND WITH BACKEND
public class HexGridController{
    @FXML
    public GridPane gameGrid;
    @FXML
    private TextField chatField;
    @FXML
    private TextArea messageArea;
    @FXML
    private Button messageButton;
    @FXML
    private Label lastGameMoveLabel;

    //Class initialization
    private final HexGameLogic hGameC = new HexGameLogic();
    private static ChatService stub;

    public void initialize(){
        CreateHexes();
    }

    public void CreateHexes(){

        for (int x = Constants.START_OF_ROWS; x < Constants.END_OF_ROWS; x++) {
            for (int y = Constants.START_OF_COLUMNS; y < Constants.END_OF_COLUMNS; y++) {
                Polygon tile = new Tile(x,y,hGameC,gameGrid);
                //x and y are swapped because java gridpane puts column than row...
                gameGrid.add(tile,y,x);

                Colors colorOfTile = Colors.WHITE;

                if(tile.getFill() == Color.BLUE){
                    colorOfTile = Colors.BLUE;
                }
                else if(tile.getFill() == Color.RED){
                    colorOfTile = Colors.RED;
                }

                HexGameLogic.gameField[x-1][y-1] = new PolygonSpecifics(tile, colorOfTile,false, x-1, y-1);
            }
        }

        ObservableList<Node> childrens = gameGrid.getChildren();

        for (Node node : childrens) {

            GridPane.setMargin(node, new Insets(Constants.MARGINE_SIZE_TOP,0,GridPane.getRowIndex(node) * Constants.MAGRINE_SIZE_BOTTOM,
                    GridPane.getRowIndex(node) * Constants.MARGINE_SIZE));
        }

        if(!HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            stub = ChatConnection.connectToChat(messageArea);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Platform.runLater(new GetLastGameMoveThread(lastGameMoveLabel));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    public void resetGame(){
        resetPolygons(HexGameLogic.gameField);

        HexGameLogic.resetGame = true;

        if(!HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            HexGameLogic.sendRequest();
        }
    }

    public void saveGame(){
        if(!HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            System.out.println("You must be in singleplayer mode");
            return;
        }
        if(HexGameLogic.gameOver){
            System.out.println("Someone already won, cant save");
            return;
        }

        System.out.println("Saving the game state");

        String[][] stateOfGameInString = ConverterForSaveAndLoad.convertoToString(HexGameLogic.gameField);
        ConverterForSaveAndLoad converter = new ConverterForSaveAndLoad(stateOfGameInString, HexGameLogic.firstPlayerPlaying, HexGameLogic.numberOfTurnsPlayed,false);

        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(ConverterForSaveAndLoad.SAVE_GAME_FILE_NAME))){
            objectOutputStream.writeObject(converter);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void loadGame(){
        if(!HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            System.out.println("You must be in singleplayer mode");
            return;
        }

        System.out.println("Loading the game");

        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(ConverterForSaveAndLoad.SAVE_GAME_FILE_NAME))) {

            resetPolygons(HexGameLogic.gameField);
            HexGameLogic.gameOver = false;
            HexGameLogic.resetGame = true;

            ConverterForSaveAndLoad converter = (ConverterForSaveAndLoad) objectInputStream.readObject();
            ConverterForSaveAndLoad.convertToGame(converter.getGameBoardState(), HexGameLogic.gameField);

            HexGameLogic.numberOfTurnsPlayed = converter.getNumberOfTurnsPlayed();
            HexGameLogic.firstPlayerPlaying = converter.isPlayerOnePlaying();
        }
        catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void generateHTMLDocumentation(){
        System.out.println("Creating Documentation");
        DocumentationUtil.generateDocumentation();
    }

    public void resetPolygons(PolygonSpecifics[][] polygonSpecifics){
        for(int i = 0; i < ConverterForSaveAndLoad.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < ConverterForSaveAndLoad.NUMBER_OF_COLUMNS; j++) {
                if(polygonSpecifics[i][j].getCoordinateY() == 0 || polygonSpecifics[i][j].getCoordinateY() == 12){
                    polygonSpecifics[i][j].polygon.setFill(Color.BLUE);
                    polygonSpecifics[i][j].color = Colors.BLUE;
                }
                else if (polygonSpecifics[i][j].getCoordinateX() == 0 || polygonSpecifics[i][j].getCoordinateX() == 12) {
                    polygonSpecifics[i][j].polygon.setFill(Color.RED);
                    polygonSpecifics[i][j].color = Colors.RED;
                }
                else {
                    polygonSpecifics[i][j].polygon.setFill(Color.ANTIQUEWHITE);
                    polygonSpecifics[i][j].color = Colors.WHITE;
                }
                polygonSpecifics[i][j].deadEnd = false;

            }
        }
    }
    public void sendChatMessage() {
        ChatConnection.sendChatMessage(chatField,messageArea,stub);
    }

    public void replayGame() {
        if(HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            resetPolygons(HexGameLogic.gameField);
            ReplayGame.replayGame(HexGameLogic.gameField);
        }
        else {
            System.out.println("Needs to be singleplayer");
        }
    }
}