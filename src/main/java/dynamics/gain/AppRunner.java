package dynamics.gain;

import dynamics.gain.common.Utils;
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
	StateRepo stateRepo;

	@Autowired
	TownRepo townRepo;

	@Autowired
	LocationRepo locationRepo;

	@Autowired
	PhoneService phoneService;

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {
		Parakeet.perch(accessor);
		createRoles();
		createAdministrator();
		createGuest();
		createBaseLocations();
	}

	public void createBaseLocations(){

		if(stateRepo.getCount() == 0){
			User user = userRepo.getByUsername(Constants.ADMIN_USERNAME);
			String BASE_STATE = "Nevada";
			String[][] townData = {
					{ "Fallon", "fallon"},
					{ "Reno", "reno"},
					{ "Las Vegas", "lasvegas"},
			};

			State state = new State();
			state.setName(BASE_STATE);
			State savedState = stateRepo.save(state);

			int index = 0;

			for(String[] data : townData){
				Town town = new Town();
				town.setName(data[0]);
				town.setTownUri(data[1]);
				town.setStateId(savedState.getId());
				Town savedTown = townRepo.save(town);

				String[][] locations = {
						{"Rescue Mission", "rescuemission"},
						{"Camp Mino", "mino"},
				};

				for(String[] shelter: locations){
					Location location = new Location();
					location.setName(shelter[0]);
					location.setLocationUri(shelter[1]+ "" + index);
					location.setDescription("Helping at-risk and homeless families in Clark County achieve sustainable housing and independence through a compassionate, community-based response.");
					location.setNeeds("Shoes, Socks, Jackets, Laptops, Prepaid Phones");
					location.setUserId(user.getId());
					location.setTownId(savedTown.getId());
					location.setCount(Utils.getRandomNumber(231));
					locationRepo.save(location);
				}
				index++;
			}
		}
		log.info(townRepo.getCount() + " Towns");
		log.info(locationRepo.getCount() + " Locations");
	}

	private void createRoles(){
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

	
	private void createAdministrator(){
		
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

	}


	private void createGuest(){
		User existing = userRepo.getByUsername(Constants.GUEST_USERNAME);
		String password = Parakeet.dirty(Constants.GUEST_PASSWORD);

		if(existing == null){
			User user = new User();
			user.setUsername(Constants.GUEST_USERNAME);
			user.setPassword(password);
			userRepo.save(user);
		}
		log.info("Users : " + userRepo.getCount());
	}

}