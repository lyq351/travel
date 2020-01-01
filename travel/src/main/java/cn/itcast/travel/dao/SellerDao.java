package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据 tab_route 里的外键 sid 可以查询单个商家。
     * @param sid
     * @return
     */
    public Seller findById(int sid);
}
