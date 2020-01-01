package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public Favorite findByRidAndUid(String rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            favorite = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
           // e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findByRidCount(int rid) {
        //这种方法是根据数据库里的所有数据查询。
        String sql = "select count(*) from tab_favorite where rid = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        jdbcTemplate.update(sql,rid,new Date(),uid); //没有返回值，怎么判断是否添加成功呢？？
    }

    @Override
    public void delete(int rid, int uid) {
        String sql = "delete from tab_favorite where rid = ? and uid = ?";
        jdbcTemplate.update(sql,rid,uid);
    }
}
