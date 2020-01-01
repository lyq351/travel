package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
      /*  //1.从redis中查询
        //获取redis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2可使用sortedset排序查询
       // Set<String> categorys = jedis.zrange("category",0,-1);
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;
        //2.判断是否为空
        if(categorys == null || categorys.size()==0){
            System.out.println("数据库查询。。。。");
            cs = categoryDao.findAll();
            //将数据存入redis
            for(int i=0;i<cs.size();i++){
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
            }
        }else {
            System.out.println("redis查询。。。。");
            //将Set<Stirng> 转成 List<Category>
            cs = new ArrayList<Category>();
            for(Tuple tuple : categorys){
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }*//*

        //3.为空从数据库中查询

        //4.不为空直接返回。
        return cs;*/
        return categoryDao.findAll();
    }
}
