package hr.java.game.hex_projekt_maksrezek;

import hr.java.game.hex_projekt_maksrezek.exception.WrongPlayerName;
import hr.java.game.hex_projekt_maksrezek.model.Player;
import hr.java.game.hex_projekt_maksrezek.multiplayerThread.PlayerOneServerThread;
import hr.java.game.hex_projekt_maksrezek.multiplayerThread.PlayerTwoServerThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;

public class HexApplication extends Application {

    private final static int WINDOW_WIDTH = 850;
    private final static int WINDOW_HEIGHT = 700;

    public  static Stage mainstage;

    public static Player player;

    public static final int PLAYER_TWO_SERVER_PORT = 1989;
    public static final int PLAYER_ONE_SERVER_PORT = 1990;

    public static final String HOST = "localhost";

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainstage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(HexApplication.class.getResource("HexGameScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle(player.name());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        String firstArgument = args[0];

        if(Player.valueOf(firstArgument).equals(Player.PLAYER_ONE)){
            player = Player.PLAYER_ONE;
            Thread serverStater = new Thread(new PlayerOneServerThread());
            serverStater.start();
            HexGameLogic.disableButtons = false;
        }
        else if (Player.valueOf(firstArgument).equals(Player.PLAYER_TWO)) {
            player = Player.PLAYER_TWO;
            Thread serverStater = new Thread(new PlayerTwoServerThread());
            serverStater.start();
            HexGameLogic.disableButtons = true;
        }
        else if(Player.valueOf(firstArgument).equals(Player.PLAYER_SINGLE)){
            player = Player.PLAYER_SINGLE;
        }
        else {
            throw new WrongPlayerName("Wrong player name, the player name must be PLAYER_ONE or PLAYER_TWO.");
        }

        launch();
    }
}