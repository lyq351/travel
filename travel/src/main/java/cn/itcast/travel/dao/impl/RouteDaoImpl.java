package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findTotalCount(int cid,String rname) {
        /*String sql2 = "select * from tab_route where cid = ?";
        List<Route> list = jdbcTemplate.query(sql2 ,new BeanPropertyRowMapper<Route>(Route.class),cid);
        System.out.println("size; "+list.size());*/

        //String sql = "select count(*) from tab_route where cid = ?";
        //拼接 sql 模板
        String sql = "SELECT COUNT(*) FROM  tab_route WHERE 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List param = new ArrayList();
        if(cid != 0){
            sb.append("and cid = ?");
            param.add(cid);
        }
        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ?");
            param.add("%"+rname+"%");
        }
        sql = sb.toString();
        return jdbcTemplate.queryForObject(sql,Integer.class,param.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
       // String sql = "select * from tab_route where cid = ? limit ?,?";
        String sql = "SELECT * FROM tab_route WHERE 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List param = new ArrayList();

        if(cid != 0){
            sb.append("and cid = ?");
            param.add(cid);
        }
        if(rname != null && rname.length() > 0 ){
            sb.append(" and rname like ?");
            param.add("%"+rname+"%");
        }
        sb.append(" limit ?,?");
        param.add(start);
        param.add(pageSize);
        sql = sb.toString();

        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Route>(Route.class),param.toArray());
    }

    @Override
    public Route findOneRoute(int rid) {
        String sql = "select * from tab_route where rid = ?";
        Route route = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
        return route;
    }
}
