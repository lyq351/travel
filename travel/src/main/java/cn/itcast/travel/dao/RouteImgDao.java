package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {

    /**
     * 根据外键rid查询图片，为多个。 在tab_route_img 表里有 一个外键字段
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
