package hr.java.game.hex_projekt_maksrezek.winnerChecker;

import hr.java.game.hex_projekt_maksrezek.model.Colors;
import hr.java.game.hex_projekt_maksrezek.model.PolygonSpecifics;

import java.util.ArrayList;

public class CheckForWinnerRed {

    private final static int NUMBER_OF_COLUMNS = 13;

    private final static   int NUMBER_OF_TOUCING_POLYGONS = 6;

    //check for win
    private static ArrayList<PolygonSpecifics> path = new ArrayList<>();
    private static ArrayList<PolygonSpecifics> usedPolygons = new ArrayList<>();

    //winning path;
    private static ArrayList<PolygonSpecifics> winningPath = new ArrayList<>();
    public static PolygonSpecifics[][] gameField = new PolygonSpecifics[14][14];

    //winner
    public static boolean winner = false;

    //other stuff:
    private static PolygonSpecifics currentPolygon;
    private static boolean foundToucing = false;


    public static boolean checkForWinner(PolygonSpecifics[][] gField){

        gameField = gField;

        int xStart = 1;
        int yStart = 1;

        //check if touching up wall
        for(yStart = 1; yStart < NUMBER_OF_COLUMNS; yStart++){
            if(gameField[xStart][yStart].color == Colors.RED){
                path.add(gameField[xStart][yStart]);
                break;
            }
        }

        //if no one is touching upside, leave
        if(path.isEmpty()){
            return false;
        }

        currentPolygon = path.getFirst();

        do{
            foundToucing = false;

            for(int i = 0; i < NUMBER_OF_TOUCING_POLYGONS;i++){

                if(foundToucing || winner){break;}
                switch (i){
                    case 0:
                        if(currentPolygon.getCoordinateX()-1 == 0 || checkIfGood( gameField[currentPolygon.getCoordinateX()-1][currentPolygon.getCoordinateY()])
                                || path.contains(gameField[currentPolygon.getCoordinateX()-1][currentPolygon.getCoordinateY()])) {
                            break;
                        }
                        else{
                            addNewPossibleRoute(gameField[currentPolygon.getCoordinateX()-1][currentPolygon.getCoordinateY()]);
                        }
                        break;
                    case 1:
                        if(currentPolygon.getCoordinateX()-1 == 0 || checkIfGood( gameField[currentPolygon.getCoordinateX()-1][currentPolygon.getCoordinateY()+1])
                                || path.contains(gameField[currentPolygon.getCoordinateX()-1][currentPolygon.getCoordinateY()+1])){
                            break;
                        }
                        else{
                            addNewPossibleRoute(gameField[currentPolygon.getCoordinateX()-1][currentPolygon.getCoordinateY()+1]);
                        }
                        break;
                    case 2:
                        if(currentPolygon.getCoordinateX()-1 == 12 || currentPolygon.getCoordinateY()-1 == 0
                                || checkIfGood( gameField[currentPolygon.getCoordinateX()][currentPolygon.getCoordinateY()+1])
                                || path.contains(gameField[currentPolygon.getCoordinateX()][currentPolygon.getCoordinateY()+1])){
                            break;
                        }
                        else {
                            addNewPossibleRoute(gameField[currentPolygon.getCoordinateX()][currentPolygon.getCoordinateY()+1]);
                        }
                        break;
                    case 3:
                        if(currentPolygon.getCoordinateX()+1 == 12){
                            winner = true;
                            break;
                        }
                        else if(checkIfGood(gameField[currentPolygon.getCoordinateX()+1][currentPolygon.getCoordinateY()])
                                || path.contains(gameField[currentPolygon.getCoordinateX()+1][currentPolygon.getCoordinateY()])){
                            break;
                        }
                        else{
                            addNewPossibleRoute(gameField[currentPolygon.getCoordinateX()+1][currentPolygon.getCoordinateY()]);
                        }
                        break;
                    case 4:
                        if(currentPolygon.getCoordinateX()-1 == 12 || currentPolygon.getCoordinateY()-1 == 0
                                || checkIfGood(gameField[currentPolygon.getCoordinateX()+1][currentPolygon.getCoordinateY()-1])
                                || path.contains(gameField[currentPolygon.getCoordinateX()+1][currentPolygon.getCoordinateY()-1] )){
                            break;
                        }
                        else {
                            addNewPossibleRoute(gameField[currentPolygon.getCoordinateX()+1][currentPolygon.getCoordinateY()-1]);
                        }
                        break;
                    case 5:
                        if(currentPolygon.getCoordinateX()-1 == 12 || currentPolygon.getCoordinateY()-1 == 0
                                || checkIfGood(gameField[currentPolygon.getCoordinateX()][currentPolygon.getCoordinateY()-1])
                                || path.contains(gameField[currentPolygon.getCoordinateX()][currentPolygon.getCoordinateY()-1])){
                            break;
                        }
                        else {
                            addNewPossibleRoute(gameField[currentPolygon.getCoordinateX()][currentPolygon.getCoordinateY()-1]);
                        }
                        break;
                    default:
                        break;
                }
            }

            if(!foundToucing){
                currentPolygon.deadEnd = true;
                usedPolygons.add(path.getLast());
                path.removeLast();

                if(path.isEmpty()){
                    for(int yStart2 = yStart+1; yStart2< NUMBER_OF_COLUMNS; yStart2++){
                        if(gameField[xStart][yStart2].color == Colors.RED){
                            yStart = yStart2;
                            resetAllDeadEnds();
                            path.add(gameField[xStart][yStart]);
                            currentPolygon = gameField[xStart][yStart];
                            break;
                        }
                    }
                }
                else {
                    currentPolygon = path.getLast();
                }
            }

            //winner part;
            if(winner){
                winningPath = path;
                path = new ArrayList<>();

            }


        }while(!path.isEmpty());

        if(winner){
            resetAllDeadEnds();
            PrintOutWinner.printOut(Colors.RED);
            if(winningPath != null){
                for(PolygonSpecifics polygonSpecifics : winningPath){
                    //System.out.println(polygonSpecifics);
                }
            }
            return true;
        }
        else{
            resetAllDeadEnds();
            path = new ArrayList<PolygonSpecifics>();
            return false;
        }
    }

    private static boolean checkIfGood(PolygonSpecifics polygonSpecifics){
        return !(polygonSpecifics.color == Colors.RED && !polygonSpecifics.deadEnd);
    }
    private static void addNewPossibleRoute(PolygonSpecifics polygonSpecifics){
        foundToucing = true;
        currentPolygon = polygonSpecifics;
        path.add(polygonSpecifics);
    }
    private static void resetAllDeadEnds(){
        for(PolygonSpecifics polygonSpecifics : usedPolygons){
            polygonSpecifics.deadEnd = false;
        }
    }
}
