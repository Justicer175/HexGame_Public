package hr.java.game.hex_projekt_maksrezek.replayLogic;

import hr.java.game.hex_projekt_maksrezek.HexGameLogic;
import hr.java.game.hex_projekt_maksrezek.model.Colors;
import hr.java.game.hex_projekt_maksrezek.model.GameMove;
import hr.java.game.hex_projekt_maksrezek.model.PolygonSpecifics;
import hr.java.game.hex_projekt_maksrezek.utils.XmlUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReplayGame {

    public static void replayGame(PolygonSpecifics[][] polygonSpecifics) {
        HexGameLogic.gameOver = true;
        HexGameLogic.disableDuringReplay = true;

        List<GameMove> gameMovesList = XmlUtils.readGameMovesFromXml();
        AtomicInteger i = new AtomicInteger(0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            GameMove gameMove = gameMovesList.get(i.get());

            if(gameMove.getColor() == Colors.RED){
                polygonSpecifics[gameMove.getPositionX()-1][gameMove.getPositionY()-1].polygon.setFill(Color.RED);
                polygonSpecifics[gameMove.getPositionX()-1][gameMove.getPositionY()-1].color = Colors.RED;
            }
            else if(gameMove.getColor() == Colors.BLUE){
                polygonSpecifics[gameMove.getPositionX()-1][gameMove.getPositionY()-1].polygon.setFill(Color.BLUE);
                polygonSpecifics[gameMove.getPositionX()-1][gameMove.getPositionY()-1].color = Colors.BLUE;
            }
            else{
                polygonSpecifics[gameMove.getPositionX()-1][gameMove.getPositionY()-1].polygon.setFill(Color.WHITE);
                polygonSpecifics[gameMove.getPositionX()-1][gameMove.getPositionY()-1].color = Colors.WHITE;
            }
            i.set(i.get() + 1);

            if(gameMovesList.size() == i.get()){
                HexGameLogic.numberOfTurnsPlayed = i.get();
                HexGameLogic.checkFowWinners();
            }
        }));
        timeline.setCycleCount(gameMovesList.size());
        timeline.playFromStart();
    }

}
