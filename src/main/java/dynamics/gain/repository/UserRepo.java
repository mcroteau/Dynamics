package dynamics.gain.repository;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import dynamics.gain.common.Constants;
import dynamics.gain.model.*;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

	private static final Logger log = Logger.getLogger(UserRepo.class);

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public long getId() {
		String sql = "select max(id) from accounts";
		long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
		return id;
	}

	public long getCount() {
		String sql = "select count(*) from accounts";
		long count = jdbcTemplate.queryForObject(sql, new Object[] { }, Long.class);
	 	return count; 
	}

	public User get(long id) {
		String sql = "select * from accounts where id = ?";
		
		User user = jdbcTemplate.queryForObject(sql, new Object[] { id },
				new BeanPropertyRowMapper<>(User.class));
		
		if(user == null) user = new User();

		return user;
	}

	public User getByUsername(String username) {
		User user = null;
		try{
			String sql = "select * from accounts where username = ?";
			user = jdbcTemplate.queryForObject(sql, new Object[] { username },
				new BeanPropertyRowMapper<User>(User.class));

		}catch(EmptyResultDataAccessException e){
			//TODO:
		}
		return user;
	}
	
	public List<User> findAll() {
		String sql = "select * from accounts";
		List<User> people = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<User>(User.class));
		return people;
	}
	
	public List<User> findAllOffset(int max, int offset) {
		String sql = "select * from accounts limit " + max + " offset " + offset;
		List<User> people = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
		return people;
	}

	public User save(User user) {
		String sql = "insert into accounts (username, password, disabled) values (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {
				user.getUsername(), user.getPassword(), false
		});

		long id = getId();
		User savedUser = get(id);

		checkSaveDefaultUserRole(id);
		checkSaveDefaultUserPermission(id);

		return savedUser;
	}

	public User saveAdministrator(User user) {
		String sql = "insert into accounts (username, password, disabled) values (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {
				user.getUsername(), user.getPassword(), false
		});

		long id = getId();
		User savedUser = get(id);

		checkSaveAdministratorRole(id);
		checkSaveDefaultUserPermission(id);

		return savedUser;
	}

	public boolean updateUuid(User user) {
		String sql = "update accounts set uuid = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] {
				user.getUuid(), user.getId()
		});
		return true;
	}
	
	public boolean updatePassword(User user) {
		String sql = "update accounts set password = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] { 
			user.getPassword(), user.getId()
		});
		return true;
	}

	public User getByUsernameAndUuid(String username, String uuid){
		User user = null;
		try{
			String sql = "select * from accounts where username = '" + username + "' and uuid = '" + uuid + "'";
			user = jdbcTemplate.queryForObject(sql, new Object[] {},
					new BeanPropertyRowMapper<User>(User.class));

		}catch(EmptyResultDataAccessException e){}

		return user;
	}
	
	public boolean delete(long id) {
		String sql = "delete from accounts where id = ?";
		jdbcTemplate.update(sql, new Object[] {id });
		return true;
	}

	public String getUserPassword(String username) {
		User user = getByUsername(username);
		return user.getPassword();
	}

	public boolean checkSaveAdministratorRole(long accountId){
		Role role = roleRepo.find(Constants.ROLE_ADMIN);
		UserRole existing = getUserRole(accountId, role.getId());
		if(existing == null){
			saveUserRole(accountId, role.getId());
		}
		return true;
	}

	public boolean checkSaveDefaultUserRole(long accountId){
		Role role = roleRepo.find(Constants.ROLE_ACCOUNT);
		UserRole existing = getUserRole(accountId, role.getId());
		if(existing == null){
			saveUserRole(accountId, role.getId());
		}
		return true;
	}

	public UserRole getUserRole(long accountId, long roleId){
		String sql = "select * from account_roles where user_id = ? and role_id = ?";
		try {
			UserRole  accountRole = jdbcTemplate.queryForObject(sql, new Object[]{accountId, roleId},
					new BeanPropertyRowMapper<UserRole>(UserRole.class));
			return accountRole;
		}catch(Exception e){
			return null;
		}
	}

	public boolean checkSaveDefaultUserPermission(long accountId){
		String permission = Constants.ACCOUNT_MAINTENANCE + accountId;
		UserPermission existing = getUserPermission(accountId, permission);
		if(existing == null){
			savePermission(accountId, permission);
		}
		return true;
	}

	public UserPermission getUserPermission(long accountId, String permission){
		String sql = "select * from account_permissions where user_id = ? and permission = ?";
		try {
			UserPermission  accountPermission = jdbcTemplate.queryForObject(sql, new Object[]{accountId, permission},
					new BeanPropertyRowMapper<UserPermission>(UserPermission.class));
			return accountPermission;
		}catch(Exception e){
			return null;
		}
	}

	public boolean saveUserRole(long accountId, long roleId){
		String sql = "insert into account_roles (role_id, user_id) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { 
			roleId, accountId
		});
		return true;
	}
	
	public boolean savePermission(long accountId, String permission){
		String sql = "insert into account_permissions (user_id, permission) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { 
			accountId, permission
		});
		return true;
	}
	
	public boolean deleteUserRoles(long accountId){
		String sql = "delete from account_roles where user_id = ?";
		jdbcTemplate.update(sql, new Object[] { accountId });
		return true;
	}
	
	public boolean deleteUserPermissions(long accountId){
		String sql = "delete from account_permissions where user_id = ?";
		jdbcTemplate.update(sql, new Object[] { accountId });
		return true;
	}

	public Set<String> getUserRoles(long id) {
		String sql = "select r.name from account_roles ur, roles r where ur.role_id = r.id and ur.user_id = " + id;
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}
	
	public Set<String> getUserRoles(String username) {
		User user = getByUsername(username);
		String sql = "select r.name from account_roles ur, roles r where ur.role_id = r.id and ur.user_id = " + user.getId();
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}

	public Set<String> getUserPermissions(long id) {
		String sql = "select permission from account_permissions where user_id = " + id;
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}

	public Set<String> getUserPermissions(String username) {
		User user = getByUsername(username);
		String sql = "select permission from account_permissions where user_id = " + user.getId();
		List<String> permissionsList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> permissions = new HashSet<String>(permissionsList);
		return permissions;
	}

	public long countQuery(String query){
		String sql = "select count(*) from accounts where upper(name) like upper('%" + query + "%')";
		long count = jdbcTemplate.queryForObject(sql, new Object[] {}, Long.class);
		return count;
	}

	public List<User> search(String uncleanedQuery, int offset) {
		String query = uncleanedQuery.replaceAll("([-+.^:,])","");
		String sql = "select distinct * from accounts where upper(name) like upper(:query) and disabled = false order by name";

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("query", "%" + query + "%")
				.addValue("offset", offset);

		List<User> accountsSearched = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<User>(User.class));
		return accountsSearched;
	}

	public boolean disable(User user) {
		String sql = "update accounts set disabled = ?, date_disabled = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] {
				true, user.getDateDisabled(), user.getId()
		});
		return true;
	}

	public boolean renew(User user) {
		String sql = "update accounts set disabled = false where id = ?";
		jdbcTemplate.update(sql, new Object[] { user.getId() });
		return true;
	}

}