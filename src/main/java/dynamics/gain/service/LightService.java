package dynamics.gain.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Properties;

@Service
public class LightService {

    public String get(String name, String key){
        try {
            Properties props = read(name);
            return props.get(key).toString();
        }catch (Exception ex){}
        return "";
    }

    public boolean write(String name, String[] keys, String[] values){
        Properties props = new Properties();
        try(OutputStream outputStream = new FileOutputStream(name)){
            int idx = 0;
            for(String key : keys) {
                System.out.println(key + ":" + values[idx]);
                props.setProperty(key, values[idx]);
                idx++;
            }
            props.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Properties read(String name) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;

        try {
            fis = new FileInputStream(name);
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
