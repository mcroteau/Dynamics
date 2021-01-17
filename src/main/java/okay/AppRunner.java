package okay;

import okay.jobs.HealthJob;
import okay.jobs.HealthJobDos;
import okay.jobs.HealthJobTres;
import okay.service.PhoneService;
import org.apache.log4j.Logger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import okay.access.GainAccessor;
import okay.repository.*;
import okay.model.*;
import okay.common.Constants;
import okay.common.Utils;
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

		createMockProjects();
		
		if(!Utils.isTestEnvironment(env)) {
			startupBackgroundJobs();
		}
	}


	private void createMockProjects() {
		String[] uris = {"http://goioc.xyz",
						 "http://opengreenfield.org",
						 "http://104.156.252.167/poopoo/14years.pdf"};

		User user = userRepo.getByUsername(Constants.ADMIN_USERNAME);
		for(int n = 0; n < uris.length; n++){
			Project project = new Project();
			project.setName(Utils.getRandomString(4) + " " + Utils.getRandomString(6));
			project.setUri(uris[n]);
			project.setUserId(user.getId());
			Project savedProject = projectRepo.save(project);

			ProjectPhone projectPhone = new ProjectPhone();
			projectPhone.setPhone("9076879557");
			projectPhone.setProjectId(savedProject.getId());
			projectRepo.addPhone(projectPhone);
		}
		log.info(projectRepo.getCount() + " projects");
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


	private void startupBackgroundJobs() {
		try {

			Class[] jobs = {HealthJob.class, HealthJobDos.class, HealthJobTres.class};
			String[] jobNames = {Constants.HEALTH_JOB1, Constants.HEALTH_JOB2, Constants.HEALTH_JOB3};
			String[] triggers = { Constants.HEALTH1_TRIGGER, Constants.HEALTH2_TRIGGER, Constants.HEALTH3_TRIGGER};


			for(int n = 0; n < jobs.length; n++){

				JobDetail job = JobBuilder.newJob(jobs[n])
						.withIdentity(jobNames[n], Constants.HEALTH_GROUP).build();

				job.getJobDataMap().put(Constants.PROJECT_REPO_KEY, projectRepo);
				job.getJobDataMap().put(Constants.PHONE_SERVICE_KEY, phoneService);

				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(triggers[n], Constants.HEALTH_GROUP)
						.withSchedule(
								SimpleScheduleBuilder.simpleSchedule()
										.withIntervalInSeconds(Constants.HEALTH_JOBS_DURATION).repeatForever())
						.build();

				Scheduler scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.startDelayed(60 );
				JobKey key = new JobKey(jobNames[n], Constants.HEALTH_GROUP);
				if(!scheduler.checkExists(key)) {
					scheduler.scheduleJob(job, trigger);
					log.info(jobs[n] + " repeated " + Constants.HEALTH_JOBS_DURATION + " seconds");
				}
			}



		}catch(Exception e){
			log.info("issue initializing job" + e.getMessage());
		}
	}

}