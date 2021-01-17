package social.okay;

import dynamics.gain.MockUtils;
import dynamics.gain.service.UserService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import dynamics.gain.mocks.MockUser;
import dynamics.gain.model.User;
import dynamics.gain.repository.UserRepo;
import dynamics.gain.service.AuthService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:o-combined-test.xml")
public class UserActionTest {

    private static final Logger log = Logger.getLogger(UserActionTest.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;


    @Test
    public void testBasicSignup(){
        MockUtils.mockRequestCycle();
        RedirectAttributes redirect = new RedirectAttributesModelMap();
        User mockUser = new MockUser(1);
        userService.register("",  mockUser, new MockHttpServletRequest(), redirect);
        assertEquals(3, userRepo.getCount());
    }

}
