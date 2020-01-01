package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    /**
     * 通过rid与uid查询是否收藏
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(String rid,int uid);

    /**
     * 查询路线收藏次数
     * @param rid
     * @return
     */
    public int findByRidCount(int rid);

    /**
     * 添加收藏
     * @param i
     * @param uid
     */
    public void add(int i, int uid);

    /**
     * 取消收藏
     * @param i
     * @param uid
     */
    public void delete(int i, int uid);
}
