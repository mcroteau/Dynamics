package dynamics.gain.repository;

import dynamics.gain.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProjectRepo {

    private static final Logger log = Logger.getLogger(ProjectRepo.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from projects";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from projects";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Project get(long id){
        String sql = "select * from projects where id = ?";
        Project project = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(Project.class));
        return project;
    }

    public ProjectStatus getProjectStatus(long id){
        String sql = "select * from project_status where project_id = ?";
        ProjectStatus project = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(ProjectStatus.class));
        return project;
    }


    public List<Project> getList(){
        String sql = "select * from projects";
        List<Project> projects = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class));
        return projects;
    }

    public List<Project> getHealthList(int offset){
        String sql = "select p.id, p.name, p.uri, a.disabled from " +
                "projects p inner join accounts a on p.user_id = a.id " +
                "limit 3 offset ?";

        List<Project> projects = jdbcTemplate.query(sql, new Object[]{offset}, new BeanPropertyRowMapper<>(Project.class));
        return projects;
    }

    public List<Project> getOverview(long userId){

        String sql = "select p.id, p.name, p.uri, " +
                "ps.status_code, ps.avg_response as avg_resp from " +
                "projects p inner join project_status ps on p.id = ps.project_id " +
                "where p.user_id = ? order by p.name asc";

        List<Project> projects = jdbcTemplate.query(sql, new Object[]{ userId }, new BeanPropertyRowMapper<>(Project.class));
        return projects;
    }

    public Project save(Project project){
        String sql = "insert into projects (uri, name, user_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                project.getUri(), project.getName(), project.getUserId()
        });

        Long id = getId();

        ProjectStatus projectStatus = new ProjectStatus();
        projectStatus.setProjectId(id);
        projectStatus.setStatusCode(200);
        initStatus(projectStatus);

        User user = userRepo.get(project.getUserId());
        ProjectEmail projectEmail = new ProjectEmail();
        projectEmail.setEmail(user.getUsername());
        projectEmail.setProjectId(id);
        addEmail(projectEmail);

        Project savedProject = get(id);
        return savedProject;
    }

    public boolean initStatus(ProjectStatus projectStatus){
        String sql = "insert into project_status (project_id, status_code) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                projectStatus.getProjectId(), projectStatus.getStatusCode()
        });
        return true;
    }

    public boolean update(Project project){
        String sql = "update projects set uri = ?, name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                project.getUri(), project.getName(), project.getId()
        });
        return true;
    }

    public boolean updateStatus(ProjectStatus projectStatus){
        String sql = "update project_status set status_code = ?, avg_response = ?, response_sum = ?, latest_response = ?, notified = ?, notified_count = ?, total_http_validations = ?, operational_http_validations = ?, validation_date = ?, initial_saving = ? where project_id = ?";
        jdbcTemplate.update(sql, new Object[] {
                projectStatus.getStatusCode(), projectStatus.getAvgResponse(), projectStatus.getResponseSum(), projectStatus.getLatestResponse(), projectStatus.isNotified(), projectStatus.getNotifiedCount(), projectStatus.getTotalHttpValidations(), projectStatus.getOperationalHttpValidations(), projectStatus.getValidationDate(), projectStatus.isInitialSaving(), projectStatus.getProjectId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from projects where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteProjectStatus(long id){
        String sql = "delete from project_status where project_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteProjectPhones(long id){
        String sql = "delete from project_phones where project_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteProjectEmails(long id){
        String sql = "delete from project_emails where project_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean addPhone(ProjectPhone projectPhone){
        String sql = "insert into project_phones (phone, project_id) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                projectPhone.getPhone(), projectPhone.getProjectId()
        });
        return true;
    }

    public boolean addEmail(ProjectEmail projectEmail){
        String sql = "insert into project_emails (email, project_id) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                projectEmail.getEmail(), projectEmail.getProjectId()
        });
        return true;
    }

    public List<ProjectPhone> getPhones(long projectId){
        String sql = "select * from project_phones where project_id = ?";
        List<ProjectPhone> projectPhones = jdbcTemplate.query(sql, new Object[]{ projectId },new BeanPropertyRowMapper<>(ProjectPhone.class));
        return projectPhones;
    }

    public List<ProjectEmail> getEmails(long projectId){
        String sql = "select * from project_email where project_id = ?";
        List<ProjectEmail> projectEmails = jdbcTemplate.query(sql, new Object[]{ projectId },new BeanPropertyRowMapper<>(ProjectEmail.class));
        return projectEmails;
    }

    public boolean deletePhone(long id){
        String sql = "delete from project_phones where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteEmail(long id){
        String sql = "delete from project_emails where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

}
