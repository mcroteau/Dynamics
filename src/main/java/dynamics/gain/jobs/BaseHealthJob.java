package dynamics.gain.jobs;

import dynamics.gain.common.Constants;
import dynamics.gain.common.JobData;
import dynamics.gain.common.Utils;
import dynamics.gain.model.Project;
import dynamics.gain.model.ProjectPhone;
import dynamics.gain.model.ProjectStatus;
import dynamics.gain.repository.ProjectRepo;
import dynamics.gain.service.PhoneService;
import org.apache.log4j.Logger;
import org.quartz.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class BaseHealthJob implements Job {

    private static final Logger log = Logger.getLogger(BaseHealthJob.class);

    int offset;
    String jobName;
    JobKey jobKey;
    ProjectRepo projectRepo;
    PhoneService phoneService;

    public BaseHealthJob(int offset, String jobName){
        this.offset = offset;
        this.jobName = jobName;
        this.jobKey = new JobKey(jobName, Constants.HEALTH_GROUP);
    }

    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        try {

            JobData.reset();
            Date begin = new Date();

            JobDetail jobDetail = jobContext.getScheduler().getJobDetail(this.jobKey);
            this.projectRepo = (ProjectRepo) jobDetail.getJobDataMap().get(Constants.PROJECT_REPO_KEY);
            this.phoneService = (PhoneService) jobDetail.getJobDataMap().get(Constants.PHONE_SERVICE_KEY);

            List<Project> projects = projectRepo.getHealthList(this.offset);
            for(Project project : projects){
                if(!project.isDisabled()) {

                    ProjectStatus projectStatus = projectRepo.getProjectStatus(project.getId());
                    projectStatus.setValidationDate(Utils.getDate());

                    int statusCode;
                    double respTime = 0;

                    try {
                        Date start = new Date();

                        URL url = new URL(project.getUri());

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("HEAD");
                        connection.setConnectTimeout(6701);
                        statusCode = connection.getResponseCode();
                        connection.disconnect();

                        Date stop = new Date();
                        double diff = stop.getTime() - start.getTime();

                        log.info(start.getTime() + "-" + stop.getTime() + " = " + diff);
                        respTime =  diff/1000;

                    }catch(Exception e){
                        e.printStackTrace();
                        statusCode = 701;
                        JobData.instance.timeouts++;
                    }

                    if(JobData.instance.timeouts == JobData.MAX_TIMEOUTS){
                        if(!JobData.instance.notified){
                            phoneService.support("Okay! Consecutive timeouts.");
                            JobData.instance.notified = true;
                            JobData.instance.timeouts = 0;
                        }
                    }

                    projectStatus.setStatusCode(statusCode);

                    double totalValidations = projectStatus.getTotalHttpValidations() + 1;
                    projectStatus.setTotalHttpValidations(totalValidations);

                    if(!projectStatus.isInitialSaving()) {
                        double responseSum = projectStatus.getResponseSum() + respTime;
                        double avgResp = responseSum / projectStatus.getOperationalHttpValidations();

                        projectStatus.setAvgResponse(avgResp);
                        projectStatus.setResponseSum(responseSum);
                    }

                    List<ProjectPhone> phones = projectRepo.getPhones(project.getId());
                    String numbers = Utils.getPhones(phones);

                    if(statusCode != 200){

                        //send sms if first attempt or every 20 mins while down
                        if(!projectStatus.isNotified() ||
                                projectStatus.getNotifiedCount() % 3 == 0){

                            if(!numbers.equals("")){
                                if(statusCode == 701){
                                    new Thread(() -> phoneService.send(numbers, "Okay! " + project.getName() + " is experiencing slow response time and might be down!")).start();
                                }

                                if(statusCode != 701){
                                    new Thread(() -> phoneService.send(numbers, "Okay! " + project.getName() + " is observed as down!")).start();
                                }
                                projectStatus.setNotified(true);
                            }
                        }
                    }

                    //increment counter if notified
                    if(projectStatus.isNotified()){
                        projectStatus.setNotifiedCount(projectStatus.getNotifiedCount()+1);
                    }

                    if(statusCode == 200){
                        projectStatus.setLatestResponse(respTime);
                        projectStatus.setOperationalHttpValidations(projectStatus.getOperationalHttpValidations() + 1);

                        //reset counter if notified and 200 status code
                        if(projectStatus.isNotified()){
                            projectStatus.setNotified(false);
                            projectStatus.setNotifiedCount(0);
                            projectRepo.updateStatus(projectStatus);
                            new Thread(() -> phoneService.send(numbers, "Okay! " + project.getName() + " is back up and running!")).start();
                        }
                    }

                    if(projectStatus.isInitialSaving())projectStatus.setInitialSaving(false);
                    projectRepo.updateStatus(projectStatus);

                }//end !project.isDisabled

            }//for

            JobData.reset();

            double performanceTime = (new Date().getTime() - begin.getTime())/1000;
            log.info(this.jobName + " complete : " + performanceTime + " milliseconds");

        } catch (SchedulerException sex) {
            sex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void printProject(Project project){
        log.info(this.jobName + " " + project.getName());
    }
}
