package hr.java.game.hex_projekt_maksrezek.multiplayerThread;

import hr.java.game.hex_projekt_maksrezek.HexApplication;
import hr.java.game.hex_projekt_maksrezek.HexGameLogic;
import hr.java.game.hex_projekt_maksrezek.savingAndLoading.ConverterForSaveAndLoad;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerOneServerThread implements Runnable {
    @Override
    public void run() {
        playerOneAcceptRequests();
    }

    private static void playerOneAcceptRequests() {
        try (ServerSocket serverSocket = new ServerSocket(HexApplication.PLAYER_ONE_SERVER_PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                Platform.runLater(() ->  processSerializableClient(clientSocket));
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){

            ConverterForSaveAndLoad gameState = (ConverterForSaveAndLoad)ois.readObject();


            ConverterForSaveAndLoad.convertToGame(gameState.getGameBoardState(),HexGameLogic.gameField);


            HexGameLogic.numberOfTurnsPlayed = gameState.getNumberOfTurnsPlayed();
            HexGameLogic.firstPlayerPlaying = gameState.isPlayerOnePlaying();
            HexGameLogic.disableButtons = false;

            //only for rest
            HexGameLogic.resetGame = gameState.isResetGame();

            //needs to be last
            HexGameLogic.checkFowWinners();

            System.out.println("Player ONE received the game state!");
            oos.writeObject("Player ONE received the game state - confirmation!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
