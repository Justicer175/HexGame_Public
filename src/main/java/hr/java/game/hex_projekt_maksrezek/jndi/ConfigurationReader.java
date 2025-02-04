package hr.java.game.hex_projekt_maksrezek.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class ConfigurationReader {
    private static final String PROVIDER_URL = "file:D:/JavaConf";

    private static Hashtable<?, ?> configureEnvironment() {
        return new Hashtable<>() {
            {
                put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.fscontext.RefFSContextFactory");
                put(Context.PROVIDER_URL, PROVIDER_URL);
            }
        };
    }

    public static String getValue(String key) {
        try (InitialDirContextCloseable context = new InitialDirContextCloseable(configureEnvironment())){

            String fileName = "conf.properties";
            Object object = context.lookup(fileName);

            Properties props = new Properties();
            props.load(new FileReader(object.toString()));

            return props.getProperty(key);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}