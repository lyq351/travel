package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    /**
     * 根据类别分页查询
     * @param cid 类别id
     * @param currentPage 当前页
     * @param pageSize 当前页显示的数据量
     * @param rname 名称
     * @return
     */
    public PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String rname);

    /**
     * 查询路线详情
     * @param rid
     * @return
     */
    public Route findOne(int rid);
}
