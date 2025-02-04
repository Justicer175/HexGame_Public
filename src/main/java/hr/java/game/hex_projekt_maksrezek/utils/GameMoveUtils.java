package hr.java.game.hex_projekt_maksrezek.utils;

import hr.java.game.hex_projekt_maksrezek.HexGameLogic;
import hr.java.game.hex_projekt_maksrezek.model.GameMove;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameMoveUtils {

    private static final String GAME_MOVE_HISTORY_FILE_NAME = "gameMoves/gameMoves.dat";
    private static List<GameMove> gameMoveList = new ArrayList<>();

    public static void saveNewGameMove(GameMove newGameMove) {
        gameMoveList.add(newGameMove);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(GAME_MOVE_HISTORY_FILE_NAME)))
        {
            oos.writeObject(gameMoveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameMove getLastGameMove() {

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(GAME_MOVE_HISTORY_FILE_NAME)))
        {
            gameMoveList = (List<GameMove>) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return gameMoveList.get(gameMoveList.size() - 1);
    }

    public static void resetFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(GAME_MOVE_HISTORY_FILE_NAME)))
        {
            oos.close();
            //oos.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
