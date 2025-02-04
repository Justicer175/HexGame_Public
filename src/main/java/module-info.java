module hr.java.game.hex_projekt_maksrezek {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.rmi;
    requires java.naming;
    requires java.xml;


    opens hr.java.game.hex_projekt_maksrezek to javafx.fxml;
    exports hr.java.game.hex_projekt_maksrezek;
    exports hr.java.game.hex_projekt_maksrezek.chat to java.rmi;
}