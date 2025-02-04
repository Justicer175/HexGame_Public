package hr.java.game.hex_projekt_maksrezek.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DocumentationUtil {

    public DocumentationUtil() throws IOException {
    }

    public static void generateDocumentation() {
        StringBuilder generator = new StringBuilder();

        //when changing position(computer) path won't work
        //go to hr.java.game... and do copy / paste path - absolute path
        String path = "C:\\Users\\Maks\\Desktop\\JAVA\\Hex_Projekt_MaksRezek\\src\\main\\java\\hr\\java\\game\\hex_projekt_maksrezek";
        //String path = "C:\\Users\\Goran\\Documents\\Hex_Projekt_MaksRezek\\src\\main\\java\\hr\\java\\game\\hex_projekt_maksrezek";

        try{
            List<Path> classList =  Files.walk(Paths.get(path))
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .filter(p -> !p.getFileName().toString().equals("module-info.java"))
                    .toList();

            for(Path classes : classList){
                int indexOfHr = classes.toString().indexOf("hr");
                String fqcn = classes.toString().substring(indexOfHr);
                fqcn = fqcn.replace('\\', '.');
                fqcn = fqcn.substring(0, fqcn.length() - 5);

                Class<?> documentationClass = Class.forName(fqcn);


                String classModifiers = Modifier.toString(documentationClass.getModifiers());

                generator.append("<h2>"
                        + classModifiers
                        + fqcn
                        + "</h2>\n");

                Field[] classVariables = documentationClass.getFields();

                for(Field field : classVariables) {
                    String modifiers = Modifier.toString(field.getModifiers());
                    generator.append("<h3>"
                            + modifiers + " "
                            + field.getType().getName() + " "
                            + field.getName()
                            + "</h3>\n");
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Documentation</title>
                </head>
                <body>
                <h1>List of classes</h1>
                """
                + generator +
                """
                </body>
                </html>
                """;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("documentation/doc.html"))) {
            writer.write(html);
        } catch(IOException ex) {
            ex.printStackTrace();
        }

    }

}
