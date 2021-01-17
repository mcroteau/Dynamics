package dynamics.gain;

import dynamics.gain.service.PhoneService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import dynamics.gain.access.GainAccessor;
import dynamics.gain.repository.*;
import dynamics.gain.model.*;
import dynamics.gain.common.Constants;
import org.springframework.stereotype.Component;
import xyz.strongperched.Parakeet;

import javax.annotation.PostConstruct;

@Component
public class AppRunner {

	private static final Logger log = Logger.getLogger(AppRunner.class);

	@Autowired
    GainAccessor accessor;

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	ProjectRepo projectRepo;

	@Autowired
	PhoneService phoneService;

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {
		Parakeet.perch(accessor);
		createApplicationRoles();
		createApplicationAdministrator();
		createApplicationGuest();
	}


	private void createApplicationRoles(){
		Role adminRole = roleRepo.find(Constants.ROLE_ADMIN);
		Role accountRole = roleRepo.find(Constants.ROLE_ACCOUNT);

		if(adminRole == null){
			adminRole = new Role();
			adminRole.setName(Constants.ROLE_ADMIN);
			roleRepo.save(adminRole);
		}

		if(accountRole == null){
			accountRole = new Role();
			accountRole.setName(Constants.ROLE_ACCOUNT);
			roleRepo.save(accountRole);
		}

		log.info("Roles : " + roleRepo.count());
	}

	
	private void createApplicationAdministrator(){
		
		try{
			User existing = userRepo.getByUsername(Constants.ADMIN_USERNAME);
			String password = Parakeet.dirty(Constants.PASSWORD);

			if(existing == null){
				User admin = new User();
				admin.setUsername(Constants.ADMIN_USERNAME);
				admin.setPassword(password);
				userRepo.saveAdministrator(admin);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		log.info("Users : " + userRepo.getCount());
	}


	private void createApplicationGuest(){
		User existing = userRepo.getByUsername(Constants.GUEST_USERNAME);
		String password = xyz.strongperched.Parakeet.dirty(Constants.GUEST_PASSWORD);

		if(existing == null){
			User user = new User();
			user.setUsername(Constants.GUEST_USERNAME);
			user.setPassword(password);
			userRepo.save(user);
		}
		log.info("Users : " + userRepo.getCount());
	}

}