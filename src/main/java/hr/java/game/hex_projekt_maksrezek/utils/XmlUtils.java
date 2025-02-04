package hr.java.game.hex_projekt_maksrezek.utils;

import hr.java.game.hex_projekt_maksrezek.model.Colors;
import hr.java.game.hex_projekt_maksrezek.model.GameMove;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

    private static final String GAME_MOVES_XML_FILE_NAME = "xml/gameMoves.xml";

    public static void saveGameMovesToXml(List<GameMove> gameMoves) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("GameMoves");
            doc.appendChild(rootElement);

            for(GameMove gameMove : gameMoves) {
                Element gameMoveElement = doc.createElement("GameMove");

                Element symbolElement = doc.createElement("Color");
                symbolElement.setTextContent(gameMove.getColor().toString());
                gameMoveElement.appendChild(symbolElement);

                Element positionXelement = doc.createElement("PositionX");
                positionXelement.setTextContent(gameMove.getPositionX().toString());
                gameMoveElement.appendChild(positionXelement);

                Element positionYelement = doc.createElement("PositionY");
                positionYelement.setTextContent(gameMove.getPositionY().toString());
                gameMoveElement.appendChild(positionYelement);

                Element localDateTimeElement = doc.createElement("LocalDateTime");
                localDateTimeElement.setTextContent(gameMove.getLocalDateTime());
                gameMoveElement.appendChild(localDateTimeElement);

                rootElement.appendChild(gameMoveElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(new File(GAME_MOVES_XML_FILE_NAME)));
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GameMove> readGameMovesFromXml() {

        List<GameMove> gameMoves = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(GAME_MOVES_XML_FILE_NAME);
            Element gameMoveElement = dom.getDocumentElement();
            NodeList nl = gameMoveElement.getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("GameMove")) {
                        Colors symbol =
                                Colors.valueOf(el.getElementsByTagName("Color").item(0).getTextContent());

                        Integer positionX =
                                Integer.parseInt(el.getElementsByTagName("PositionX").item(0).getTextContent());

                        Integer positionY =
                                Integer.parseInt(el.getElementsByTagName("PositionY").item(0).getTextContent());

                        String localDateTime =
                                String.valueOf(el.getElementsByTagName("LocalDateTime").item(0));

                        GameMove gameMove = new GameMove();
                        gameMove.setColor(symbol);
                        gameMove.setPositionX(positionX);
                        gameMove.setPositionY(positionY);
                        gameMove.setLocalDateTime(localDateTime);

                        gameMoves.add(gameMove);
                    }
                }
            }
        }
        catch(ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return gameMoves;
    }

    public static void resetFile(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            docBuilder.reset();

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }
}
