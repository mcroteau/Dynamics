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


    public List<Project> getList(){
        String sql = "select * from projects";
        List<Project> projects = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class));
        return projects;
    }


    public List<Project> getOverview(long userId){

        String sql = "select * from projects where user_id = ?";

        List<Project> projects = jdbcTemplate.query(sql, new Object[]{ userId }, new BeanPropertyRowMapper<>(Project.class));
        return projects;
    }

    public Project save(Project project){
        String sql = "insert into projects (name, user_id, location_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                project.getName(), project.getUserId(), project.getLocationId()
        });

        Long id = getId();
        Project savedProject = get(id);
        return savedProject;
    }

    public boolean update(Project project){
        String sql = "update projects set name = ?, needs = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                project.getName(), project.getNeeds(), project.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from projects where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

}
