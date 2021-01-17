package social.okay;

import okay.MockUtils;
import okay.service.UserService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import okay.common.Constants;
import okay.mocks.MockUser;
import okay.model.User;
import okay.repository.UserRepo;
import okay.service.AuthService;

import java.util.List;

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
