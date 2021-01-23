package dynamics.gain.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

public class Dynamics {

    public static boolean isTestEnv(Environment env){
        String[] profilesPre = env.getActiveProfiles();
        List<String> profiles = Arrays.asList(profilesPre);
        return profiles.contains(Constants.MOCK_ENVIRONMENT);
    }

    public static boolean isDevEnv(Environment env){
        String[] profilesPre = env.getActiveProfiles();
        List<String> profiles = Arrays.asList(profilesPre);
        return profiles.contains(Constants.DEV_ENVIRONMENT);
    }
}
