package dynamics.gain.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Properties;

@Service
public class LightService {

    private static final String ORGANIZATIONS = "org.properties";

    public String get(String key){
        try {
            Properties props = read();
            return props.get(key).toString();
        }catch (Exception ex){}
        return "";
    }

    public boolean write(String key, String value){
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream(ORGANIZATIONS);
            props.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(ORGANIZATIONS);
            props.setProperty(key, value);
            props.store(out, null);
            out.close();

        }catch(Exception ex){}
        return true;
    }

    private Properties read() throws IOException {
        FileInputStream fis = null;
        Properties prop = null;

        try {
            fis = new FileInputStream(ORGANIZATIONS);
            prop = new Properties();
            prop.load(fis);
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            fis.close();
        }
        return prop;
    }
}
