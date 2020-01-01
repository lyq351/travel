package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public boolean isFavorite(String rid, int uid) {
        //调用Dao
        Favorite favorite = favoriteDao.findByRidAndUid(rid,uid);

        return favorite != null; //返回true，为收藏，返回false为未收藏
    }

    @Override
    public void addFavorite(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }

    @Override
    public void delete(String rid, int uid) {
        favoriteDao.delete(Integer.parseInt(rid),uid);
    }
}
