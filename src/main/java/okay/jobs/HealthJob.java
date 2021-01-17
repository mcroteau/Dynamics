package okay.jobs;

import okay.common.Constants;
import org.apache.log4j.Logger;

public class HealthJob extends BaseHealthJob {

    private static final Logger log = Logger.getLogger(HealthJob.class);

    public HealthJob(){
        super(0, Constants.HEALTH_JOB1);
    }
}
