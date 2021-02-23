package dynamics.gain.service;

import dynamics.gain.common.Dynamics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
@PropertySource("classpath:application.properties")
public class PersistenceService {

    private static final String ORGANIZATIONS = "org.properties";

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.dev.api.key}")
    private String devApiKey;

    @Autowired
    Environment env;

    public static String get(String key){
        try {
            Properties props = read();
            return props.get(key).toString();
        }catch (Exception ex){ ex.printStackTrace(); }
        return "";
    }

    private static Properties read() throws IOException {
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

    public String getApiKey(Long locationId){
        if(locationId != null && !locationId.equals("")) {
            if (!Dynamics.isDevEnv(env)) {
                return get("live." + locationId);
            }
            return get("dev." + locationId);
        }
        if(!Dynamics.isDevEnv(env)){
            return apiKey;
        }
        return devApiKey;
    }

    public String getApiKey(){
        if(!Dynamics.isDevEnv(env)){
            return apiKey;
        }
        return devApiKey;
    }
}
