package okay.mocks;

import okay.common.Constants;
import okay.model.User;

public class MockUser extends User {

    public MockUser(int instance){
        this.setUsername("marisa+"+instance + "@gmail.com");
        this.setPassword(Constants.PASSWORD);
    }
}
