package dynamics.gain.mocks;

import dynamics.gain.common.Constants;
import dynamics.gain.model.User;

public class MockUser extends User {

    public MockUser(int instance){
        this.setUsername("marisa+"+instance + "@gmail.com");
        this.setPassword(Constants.PASSWORD);
    }
}
