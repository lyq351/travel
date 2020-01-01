package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据cid查询总记录数
     * @return
     */
    public int findTotalCount(int cid,String rname);

    /**
     * 根据cid，start,pageSize查询当前页的数据集合 start开始页，pageSize,一页展示多少条数据
     */
    public List<Route> findByPage(int cid,int start,int pageSize,String rname);

    /**
     * 根据rid查询单个路线详情
     * @param rid
     * @return
     */
    public Route findOneRoute(int rid);
}
