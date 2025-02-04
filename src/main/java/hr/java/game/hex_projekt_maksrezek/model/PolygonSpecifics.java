package hr.java.game.hex_projekt_maksrezek.model;

import javafx.scene.shape.Polygon;

public class PolygonSpecifics {

    public Colors color;
    public Polygon polygon;
    public boolean deadEnd;

    //coordinates
    private int coordinateX;
    private int coordinateY;


    public PolygonSpecifics(Polygon polygon, Colors color, boolean deadEnd, int coordinateX, int coordinateY){
        this.polygon = polygon;
        this.color = color;
        this.deadEnd = deadEnd;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    @Override
    public String toString() {
        return "PolygonSpecifics{" +
                "color=" + color +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                '}';
    }
}
