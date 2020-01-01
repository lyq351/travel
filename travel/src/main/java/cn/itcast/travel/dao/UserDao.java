package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     *注册用户信息
     * @param user
     */
    public  void save(User user);


    /**
     * t通过激活码查询用户
     * @param code
     * @return
     */
    public User findUserByCode(String code);

    /**
     * 修改用户激活状态
     * @param user
     */
    public void updateUserStatus(User user);

    /**
     * 根据用户名密码判断是否有该用户
     * @param user
     * @return
     */
    public User findByUsernameAndPassword(User user);
}
