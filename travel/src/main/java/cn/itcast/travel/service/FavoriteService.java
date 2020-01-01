package cn.itcast.travel.service;

public interface FavoriteService {

    public boolean isFavorite(String rid,int uid);

    public void addFavorite(String rid, int uid);

    /**
     * 取消收藏
     * @param rid
     * @param uid
     */
    public void delete(String rid, int uid);
}
