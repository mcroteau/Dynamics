package okay.jobs;

import okay.common.Constants;
import org.apache.log4j.Logger;

public class HealthJobTres extends BaseHealthJob {

    private static final Logger log = Logger.getLogger(HealthJobTres.class);

    public HealthJobTres(){
        super(6, Constants.HEALTH_JOB3);
    }
}
