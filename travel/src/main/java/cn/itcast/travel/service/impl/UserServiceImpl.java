package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

   private  UserDao userDao = new UserDaoImpl();
    @Override
    public boolean regist(User user) {
        boolean flag;

        //1.先查询，名字是否存在
        User u  = userDao.findByUsername(user.getUsername());
        if(null != u){
            //用户名存在，注册失败
            return false;
        }
        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        userDao.save(user);
        //3.发送邮件激活码
        String content="<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮箱");
        return true;

    }

    @Override
    public boolean active(String code) {
       // boolean flag = false;
       // User user = null;
        //根据激活码，查询是否有该用户
        User user = userDao.findUserByCode(code);
        if(user != null){
            //修改用户的激活状态
            userDao.updateUserStatus(user);
            //flag = true;
            return  true;
        }else {
            return false;
        }
       // return flag;
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user);
    }
}
