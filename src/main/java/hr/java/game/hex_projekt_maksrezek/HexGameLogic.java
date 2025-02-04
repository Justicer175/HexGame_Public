package hr.java.game.hex_projekt_maksrezek;

import hr.java.game.hex_projekt_maksrezek.gameMoveThreads.SaveNewGameMoveThread;
import hr.java.game.hex_projekt_maksrezek.model.Colors;
import hr.java.game.hex_projekt_maksrezek.model.GameMove;
import hr.java.game.hex_projekt_maksrezek.model.Player;
import hr.java.game.hex_projekt_maksrezek.model.PolygonSpecifics;
import hr.java.game.hex_projekt_maksrezek.savingAndLoading.ConverterForSaveAndLoad;
import hr.java.game.hex_projekt_maksrezek.utils.GameMoveUtils;
import hr.java.game.hex_projekt_maksrezek.utils.XmlUtils;
import hr.java.game.hex_projekt_maksrezek.winnerChecker.CheckForWinnerBlue;
import hr.java.game.hex_projekt_maksrezek.winnerChecker.CheckForWinnerRed;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HexGameLogic {

    //contains the whole field for the game (14x14) which contains blue and red corners
    public static PolygonSpecifics[][] gameField = new PolygonSpecifics[14][14];
    //reset the game
    public static boolean resetGame = false;
    //whose turn is it
    public static boolean firstPlayerPlaying = true;
    //number of turns counter
    public static int numberOfTurnsPlayed = 0;
    //winner
    public static boolean gameOver = false;
    //disable buttons in multiplayer
    public static boolean disableButtons = false;
    public  static boolean disableDuringReplay = false;
    List<GameMove> gameMoves = new ArrayList<>();
    public void setColor(Polygon polygon, GridPane gridPane){

        if(resetGame){
            disableDuringReplay = false;
            gameMoves = new ArrayList<>();
            CheckForWinnerBlue.winner = false;
            CheckForWinnerRed.winner = false;
            gameOver = false;
            numberOfTurnsPlayed = 0;
            firstPlayerPlaying = true;

            resetGame = false;

            if(HexApplication.player.name().equals(Player.PLAYER_ONE.name())) {
                disableButtons = false;
            }
            else if(HexApplication.player.name().equals(Player.PLAYER_TWO.name())) {
                disableButtons = true;
            }
        }

        if(disableDuringReplay){
            return;
        }

        if(gameOver){
            System.out.println("The game is over, you cant");
            return;
        }

        if(!HexApplication.player.name().equals(Player.PLAYER_SINGLE.name()) && disableButtons){
            System.out.println("YOU mus wait for the other player to play");
            return;
        }

        //don't fill if color is diffferent than white
        if(polygon.getFill() != Color.ANTIQUEWHITE){
            System.out.println("Trying to fill someone who is already filled");
            return;
        }

        numberOfTurnsPlayed++;

        GameMove newGameMove = new GameMove();

        LocalDateTime now = LocalDateTime.now();
        String timeStamp = LocalDate.now() + " " + now.getHour() + "H :" + now.getMinute() + "M :" + now.getSecond() + "S";

        if(firstPlayerPlaying){
            newGameMove = new GameMove(Colors.BLUE,(gridPane.getRowIndex(polygon)),(gridPane.getColumnIndex(polygon)),timeStamp);
            gameField[(gridPane.getRowIndex(polygon) - 1)][(gridPane.getColumnIndex(polygon) - 1)].color = Colors.BLUE;
            polygon.setFill(Color.BLUE);
            firstPlayerPlaying = false;
        }
        else {
            newGameMove = new GameMove(Colors.RED,(gridPane.getRowIndex(polygon)),(gridPane.getColumnIndex(polygon)),timeStamp);
            polygon.setFill(Color.RED);
            gameField[(gridPane.getRowIndex(polygon) - 1)][(gridPane.getColumnIndex(polygon) - 1)].color = Colors.RED;
            firstPlayerPlaying = true;
        }

        if(!HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            sendRequest();
            disableButtons = true;
        }

        SaveNewGameMoveThread saveNewGameMoveThread = new SaveNewGameMoveThread(newGameMove);
        Thread starter = new Thread(saveNewGameMoveThread);
        starter.start();

        checkFowWinners();

        if(HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            gameMoves.add(newGameMove);
            if(gameOver){

                XmlUtils.saveGameMovesToXml(gameMoves);
            }
        }
    }

    private static void playerOneSendRequest(ConverterForSaveAndLoad gameState) {
        // Closing socket will also close the socket's InputStream and OutputStream.
        try (Socket clientSocket = new Socket(HexApplication.HOST, HexApplication.PLAYER_TWO_SERVER_PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            sendSerializableRequestToPlayerTwo(clientSocket, gameState);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequestToPlayerTwo(Socket client, ConverterForSaveAndLoad gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        System.out.println(gameState.getGameBoardState());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player two!");
    }

    private static void playerTwoSendRequest(ConverterForSaveAndLoad gameState) {
        // Closing socket will also close the socket's InputStream and OutputStream.
        try (Socket clientSocket = new Socket(HexApplication.HOST, HexApplication.PLAYER_ONE_SERVER_PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            sendSerializableRequestToPlayerOne(clientSocket, gameState);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequestToPlayerOne(Socket client, ConverterForSaveAndLoad gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        System.out.println(gameState.getGameBoardState());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player one!");
    }

    public static void sendRequest(){
        ConverterForSaveAndLoad cfsal = new ConverterForSaveAndLoad();

        cfsal.setGameBoardState(new String[ConverterForSaveAndLoad.NUMBER_OF_ROWS][ConverterForSaveAndLoad.NUMBER_OF_COLUMNS]);

        cfsal.setNumberOfTurnsPlayed(numberOfTurnsPlayed);

        cfsal.setPlayerOnePlaying(firstPlayerPlaying);

        cfsal.setResetGame(resetGame);

        for(int i = 0; i < ConverterForSaveAndLoad.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < ConverterForSaveAndLoad.NUMBER_OF_COLUMNS; j++) {
                cfsal.getGameBoardState()[i][j] = gameField[i][j].color.toString();
            }
        }

        if(HexApplication.player.name().equals(Player.PLAYER_ONE.name())) {
            playerOneSendRequest(cfsal);
        }
        else if(HexApplication.player.name().equals(Player.PLAYER_TWO.name())) {
            playerTwoSendRequest(cfsal);
        }
    }

    public static void checkFowWinners(){
        if(numberOfTurnsPlayed > 20){
            gameOver = CheckForWinnerBlue.checkForWinner(gameField);
            if(!gameOver){
                gameOver = CheckForWinnerRed.checkForWinner(gameField);
            }
        }
    }

}
