package dynamics.gain.jobs;

import dynamics.gain.common.Constants;
import org.apache.log4j.Logger;

public class HealthJobDos extends BaseHealthJob {

    private static final Logger log = Logger.getLogger(HealthJobDos.class);

    public HealthJobDos(){
        super(3, Constants.HEALTH_JOB2);
    }
}
