package com.model.persist.daoImpl;
import com.model.persist.dao.AdminDao;
import com.model.persist.domain.Admin;
import com.model.persist.domain.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2016/5/20.
 * 管理员登录操作
 */
public class AdminDaoImpl implements AdminDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Admin getAdminById(int id) {
        List<Admin> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_ADMIN_BY_ID, new Object[]{id}, new AdminRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public Admin getAdminInfoById(int id) {
        List<Admin> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_ADMIN_BY_ID, new Object[]{id}, new AdminRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public Admin getAdminByName(String username) {
        List<Admin> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_ADMIN_BY_USERNMAE, new Object[]{username}, new AdminRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public boolean insertAdmin(UserInfo info) {
        return false;
    }

    @Override
    public boolean updateAdmin(String head, String username) {
        return false;
    }

    private class AdminRowMapper implements RowMapper<Admin> {

        @Override
        public Admin mapRow(ResultSet resultSet, int i) throws SQLException {
            String username = resultSet.getString("admin_name");
            String password = resultSet.getString("admin_pwd");
            return new Admin(username, password);
        }
    }
}
