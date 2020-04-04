# travel《黑马旅游网》综合案例
【JavaServlet项目】
1	前言
为了巩固web基础知识，提升综合运用能力，故而讲解此案例。要求，每位同学能够独立完成此案例。

2	项目导入
1 一个空的项目：
 
2 复制到指定的 project 目录里[idea工作空间里]：【不破坏项目】
 

点击绿色＋按钮
 
选择travel项目的pom.xml文件，点击ok，完成项目导入。需要等待一小会，项目初始化完成。
 
  导入maven管理的项目。
 

3	启动项目
3.1		方式一：
 

 
3.2	方式二：配置maven快捷启动
 
 

1 idea右上角
 

 
【这个导入的maven项目如何进行，tomcat热部署。】
 

4	技术选型 【2019-12-1-周日-17.47】
4.1	Web层
a)	Servlet：前端控制器
b)	html：视图     【self：普通用户使用的使用html加载速度快，jsp一般用于OA或财务系统。】
c)	Filter：过滤器
d)	BeanUtils：数据封装
e)	Jackson：json序列化工具
4.2	Service层
f)	Javamail：java发送邮件工具
g)	Redis：nosql内存数据库   【self：缓存动作，提升查询性能。】
h)	Jedis：java的redis客户端  【self：操作redis的】
4.3	Dao层
i)	Mysql：数据库
j)	Druid：数据库连接池
k)	JdbcTemplate：jdbc的工具  【self：】

5	创建数据库
-- 创建数据库
CREATE DATABASE travel;
-- 使用数据库
USE travel;
--创建表
	复制提供好的sql

6	注册功能
6.1	页面效果
 

【2019-12-1-周日-17.51】
  之前出差，两个地方：
1	控制层：servlet的dopost执行了两次 [原因不明]
2	Dao层： 查询没有效果，数据库没有查出数据出来。
6.2	功能分析
 
【2019-11-19-23.03-周二】【现在最重要的是听细节。不要着急，注重细节和思考。以前是很多基础知识不懂，所以很多都不是很理解。现在重在理解，吸收，尝试自己先思考，去写代码，在优化代码。在听老师是如何写代码解决问题的】
1 使用AJAX提交代码作为，可以在后台校验进行数据服务器交互。[这里为html。如果未jsp页面，则可以一次请求，存放入request域中返回错误信息到前端页面，在页面上提示]

2 设置编码：用统一的过滤器 filter 完成。

6.3	代码实现【2019-11-19-23.03】Java项目
6.3.1	前台代码实现
6.3.2	表单校验
	提升用户体验，并减轻服务器压力。
//校验用户名
//单词字符，长度8到20位
function checkUsername() {
             //1.获取用户名值
   var username = $("#username").val();
   //2.定义正则
   var reg_username = /^\w{8,20}$/;
   
   //3.判断，给出提示信息
    var flag = reg_username.test(username);
    if(flag){
        //用户名合法
                 $("#username").css("border","");
   }else{
        //用户名非法,加一个红色边框
      $("#username").css("border","1px solid red");
   }
    
             return flag;
         }

         //校验密码
         function checkPassword() {
             //1.获取密码值
             var password = $("#password").val();
             //2.定义正则
             var reg_password = /^\w{8,20}$/;

             //3.判断，给出提示信息
             var flag = reg_password.test(password);
             if(flag){
                 //密码合法
                 $("#password").css("border","");
             }else{
                 //密码非法,加一个红色边框
                 $("#password").css("border","1px solid red");
             }

             return flag;
         }

         //校验邮箱
function checkEmail(){
    //1.获取邮箱
   var email = $("#email").val();
   //2.定义正则      itcast@163.com
   var reg_email = /^\w+@\w+\.\w+$/;

   //3.判断
   var flag = reg_email.test(email);
   if(flag){
                 $("#email").css("border","");
   }else{
                 $("#email").css("border","1px solid red");
   }

   return flag;
}

$(function () {
             //当表单提交时，调用所有的校验方法
   $("#registerForm").submit(function(){

                 return checkUsername() && checkPassword() && checkEmail();
                 //如果这个方法没有返回值，或者返回为true，则表单提交，如果返回为false，则表单不提交
   });

             //当某一个组件失去焦点是，调用对应的校验方法
   		$("#username").blur(checkUsername);
             $("#password").blur(checkPassword);
             $("#email").blur(checkEmail);


         });


6.3.3	异步(ajax)提交表单【2019-9-22-周日-16.21】
	在此使用异步提交表单是为了获取服务器响应的数据。因为我们前台使用的是html作为视图层，不能够直接从servlet相关的域对象获取值，只能通过ajax获取响应数据
 
【2019-11-20-周三-23.15】【回顾项目】争取五天将这个项目over并吸收。
   同步提交，submit，页面会自动跳转。 Html页面，不能使用EL表达式，那么不能使用域对象。 
【疑问： 请求的地址怎么办？？需不需要加虚拟路径名】
异步提交，可以通过F12,控制台查看，异步请求信息。$(“form”).serialize() 【jquery提供的方法，将表单序列化，若为js对象，需要转化为Jq对象。】服务器响应回来的数据，data，后台代码写完来处理
6.3.4	后台代码实现 【2019-11-20-周三-23.44】
6.3.5	编写RegistUserServlet
@WebServlet("/registUserServlet")

public class RegistUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {





        //验证校验

        String check = request.getParameter("check");

        //从sesion中获取验证码

        HttpSession session = request.getSession();

        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次

        //比较

        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){

            //验证码错误

            ResultInfo info = new ResultInfo();

            //注册失败

            info.setFlag(false);

            info.setErrorMsg("验证码错误");

            //将info对象序列化为json

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(info);

            response.setContentType("application/json;charset=utf-8");

            response.getWriter().write(json);

            return;

        }



        //1.获取数据

        Map<String, String[]> map = request.getParameterMap();



        //2.封装对象

        User user = new User();

        try {

            BeanUtils.populate(user,map);

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        } catch (InvocationTargetException e) {

            e.printStackTrace();

        }



        //3.调用service完成注册

        UserService service = new UserServiceImpl();

        boolean flag = service.regist(user);

        ResultInfo info = new ResultInfo();

        //4.响应结果

        if(flag){

            //注册成功

            info.setFlag(true);

        }else{

            //注册失败

            info.setFlag(false);

            info.setErrorMsg("注册失败!");

        }



        //将info对象序列化为json

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(info);



        //将json数据写回客户端

        //设置content-type

        response.setContentType("application/json;charset=utf-8");

        response.getWriter().write(json);





    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.doPost(request, response);

    }

}

【self：对于为啥定义一个类用来封装返回来的数据不是太理解。】
   答：应该是为了方便，封装数据，  object这个怎么用呢？？
【2019-11-20-周三-23.46】
该老师的书写代码思路：
 1 先编写servlet，在servlet里写好逻辑注释，
     1 接受数据
     2 封装数据
     3 调用service处理数据
     4 相应结果。
【一次写出方法，后将servlet，相关的逻辑代码编写完成。】


6.3.6	编写UserService以及UserServiceImpl
public class UserServiceImpl implements UserService {



    private UserDao userDao = new UserDaoImpl();

    /**

     * 注册用户

     * @param user

     * @return

     */

    @Override

    public boolean regist(User user) {

        //1.根据用户名查询用户对象

        User u = userDao.findByUsername(user.getUsername());

        //判断u是否为null

        if(u != null){

            //用户名存在，注册失败

            return false;

        }

        //2.保存用户信息

        userDao.save(user);

        return true;

    }

}


6.3.7	编写UserDao以及UserDaoImpl
public class UserDaoImpl implements UserDao {



    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());



    @Override

    public User findByUsername(String username) {

        User user = null;

        try {

            //1.定义sql

            String sql = "select * from tab_user where username = ?";

            //2.执行sql

            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);

        } catch (Exception e) {



        }



        return user;

    }



    @Override

    public void save(User user) {

        //1.定义sql

        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email) values(?,?,?,?,?,?,?)";

        //2.执行sql



        template.update(sql,user.getUsername(),

                    user.getPassword(),

                user.getName(),

                user.getBirthday(),

                user.getSex(),

                user.getTelephone(),

                user.getEmail());

    }

}

6.3.8	邮件激活
	为什么要进行邮件激活？为了保证用户填写的邮箱是正确的。将来可以推广一些宣传信息，到用户邮箱中。
6.3.9	 发送邮件
1.	申请邮箱
2.	开启授权码   【self： 163邮箱。 Qq邮箱用密码登录】
3.	在MailUtils中设置自己的邮箱账号和密码(授权码)
 

邮件工具类：MailUtils，调用其中sendMail方法可以完成邮件发送


6.3.10	 用户点击邮件激活
经过分析，发现，用户激活其实就是修改用户表中的status为‘Y’
 

分析：
 

发送邮件代码：
 

修改保存Dao代码，加上存储status和code 的代码逻辑
 

激活代码实现：
ActiveUserServlet
//1.获取激活码

String code = request.getParameter("code");

if(code != null){

    //2.调用service完成激活

    UserService service = new UserServiceImpl();

    boolean flag = service.active(code);



    //3.判断标记

    String msg = null;

    if(flag){

        //激活成功

        msg = "激活成功，请<a href='login.html'>登录</a>";

    }else{

        //激活失败

        msg = "激活失败，请联系管理员!";

    }

    response.setContentType("text/html;charset=utf-8");

    response.getWriter().write(msg);

UserService：active
@Override

public boolean active(String code) {

    //1.根据激活码查询用户对象

    User user = userDao.findByCode(code);

    if(user != null){

        //2.调用dao的修改激活状态的方法

        userDao.updateStatus(user);

        return true;

    }else{

        return false;

    }



}

UserDao：findByCode	updateStatus

/**

 * 根据激活码查询用户对象

 * @param code

 * @return

 */

@Override

public User findByCode(String code) {

    User user = null;

    try {

        String sql = "select * from tab_user where code = ?";



        user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);

    } catch (DataAccessException e) {

        e.printStackTrace();

    }



    return user;

}



/**

 * 修改指定用户激活状态

 * @param user

 */

@Override

public void updateStatus(User user) {

    String sql = " update tab_user set status = 'Y' where uid=?";

    template.update(sql,user.getUid());

}




7	登录
7.1	分析
 

7.2	代码实现
7.2.1	前台代码
 
7.2.2	后台代码
LoginServlet
//1.获取用户名和密码数据

Map<String, String[]> map = request.getParameterMap();

//2.封装User对象

User user = new User();

try {

    BeanUtils.populate(user,map);

} catch (IllegalAccessException e) {

    e.printStackTrace();

} catch (InvocationTargetException e) {

    e.printStackTrace();

}



//3.调用Service查询

UserService service = new UserServiceImpl();

User u  = service.login(user);



ResultInfo info = new ResultInfo();



//4.判断用户对象是否为null

if(u == null){

    //用户名密码或错误

    info.setFlag(false);

    info.setErrorMsg("用户名密码或错误");

}

//5.判断用户是否激活

if(u != null && !"Y".equals(u.getStatus())){

    //用户尚未激活

    info.setFlag(false);

    info.setErrorMsg("您尚未激活，请激活");

}

//6.判断登录成功

if(u != null && "Y".equals(u.getStatus())){

    //登录成功

    info.setFlag(true);

}



//响应数据

ObjectMapper mapper = new ObjectMapper();



response.setContentType("application/json;charset=utf-8");

mapper.writeValue(response.getOutputStream(),info);

UserService
public User login(User user) {

    return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());

}

UserDao
public User findByUsernameAndPassword(String username, String password) {

    User user = null;

    try {

        //1.定义sql

        String sql = "select * from tab_user where username = ? and password = ?";

        //2.执行sql

        user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);

    } catch (Exception e) {



    }



    return user;

}
7.2.3	index页面中用户姓名的提示信息功能
效果：
 
header.html代码
 
Servlet代码
//从session中获取登录用户

Object user = request.getSession().getAttribute("user");

//将user写回客户端



ObjectMapper mapper = new ObjectMapper();

response.setContentType("application/json;charset=utf-8");

mapper.writeValue(response.getOutputStream(),user);


8	退出
什么叫做登录了？session中有user对象。
实现步骤：
1.	访问servlet，将session销毁
2.	跳转到登录页面
代码实现：
	Header.html 
Servlet:
//1.销毁session

request.getSession().invalidate();



//2.跳转登录页面

response.sendRedirect(request.getContextPath()+"/login.html");







————————————————————————
【2019-10-2-周三 12.10】
疑问：1, 在保存数据的时候是怎么操作的

这个项目主要是HTML为view，后台为Java代码。


【也不知道为什么，明明知道有很多事情需要自己去做，但总是静不下心来，去坚持做他，昨天影星和我说，在拼搏的年纪一定要多努力，他说你现在不努力的话，等你到了30岁同学聚会的时候，才发现距离拉的那么开，2020年还剩不到91天就要到来，我说过，这三月我要把我的头发调养好，第二步，把工作做好，明年去杭州，一定拿到7k左右，该学习的时候不多努力学习的话，以后再未来的日子里学习就更缓慢了。少年加油啊！不要太大压力了。】



疑惑点：
1，
<!--注册表单-->
<form id="registerForm" action="user"> 【这个action是随便写的，用的是AJAX提交 submit(function(){})】

2，
  Idea后台在方法里修改了代码，还需要从新启动的吗？？

错误：【无法解决】
translate.google.com/gen204?nca=te_li&client=te_lib&logld=vTE_20190724_00:1 GET http://translate.google.com/gen204?nca=te_li&client=te_lib&logld=vTE_20190724_00 net::ERR_CONNECTION_TIMED_OUT
Image (async)
gr @ VM7205:293
eval @ VM7205:477
eval @ VM7205:479
xhr.onreadystatechange @ VM7201:419
XMLHttpRequest.send (async)
onLoadJavascript @ VM7201:421
eval @ VM7204:8
eval @ VM7204:9
xhr.onreadystatechange @ VM7201:419
XMLHttpRequest.send (async)
onLoadJavascript @ VM7201:421
(anonymous) @ VM7201:427
(anonymous) @ VM7201:427
(anonymous) @ VM7201:427
translate.google.com/gen204?sl=en&tl=zh-CN&textlen=4&sp=nmt&ttt=193&ttl=126&ttf=659&sr=1&nca=te_time&client=te_lib&logld=vTE_20190724_00:1 GET http://translate.google.com/gen204?sl=en&tl=zh-CN&textlen=4&sp=nmt&ttt=193&ttl=126&ttf=659&sr=1&nca=te_time&client=te_lib&logld=vTE_20190724_00 net::ERR_CONNECTION_TIMED_OUT


【2019-11-21-周四-0.08】

【2019-11-22-周五-0.48】
 这个项目还是出错，原因不明，debug调试，居然点击提交显示请求两次，奇了怪。

严重: Servlet.service() for servlet [cn.itcast.travel.web.servlet.RegistUserServlet] in context with path [/travel] threw exception
org.springframework.dao.DuplicateKeyException: PreparedStatementCallback; SQL [insert into tab_user(username,password,name,birthday,sex,telephone,email) value(?,?,?,?,?,?,?);]; Duplicate entry 'zh' for key 'AK_nq_username'; nested exception is com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'zh' for key 'AK_nq_username'
	at org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator.doTranslate(SQLErrorCodeSQLExceptionTranslator.java:239)
	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:73)
	at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:660)
	at org.springframework.jdbc.core.JdbcTemplate.update(JdbcTemplate.java:909)
	at org.springframework.jdbc.core.JdbcTemplate.update(JdbcTemplate.java:970)
	at org.springframework.jdbc.core.JdbcTemplate.update(JdbcTemplate.java:980)
	at cn.itcast.travel.dao.impl.UserDaoImpl.save(UserDaoImpl.java:33)
	at cn.itcast.travel.service.impl.UserServiceImpl.regist(UserServiceImpl.java:24)
	at cn.itcast.travel.web.servlet.RegistUserServlet.doPost(RegistUserServlet.java:52)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:647)


Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'zh' for key 'AK_nq_username'
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:411)
	at com.mysql.jdbc.Util.getInstance(Util.java:386)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:1041)
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4190)
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4122)
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2570)
	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2731)
	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2818)
	at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:2157)
	at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:2460)
	at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:2377)
	at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:2361)
	at com.alibaba.druid.pool.DruidPooledPreparedStatement.executeUpdate(DruidPooledPreparedStatement.java:253)
	at org.springframework.jdbc.core.JdbcTemplate$2.doInPreparedStatement(JdbcTemplate.java:916)
	at org.springframework.jdbc.core.JdbcTemplate$2.doInPreparedStatement(JdbcTemplate.java:909)
	at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:644)
	... 27 more

Failed to load resource: net::ERR_CONNECTION_TIMED_OUT

这里用的是AJAX 发送请求 Servlet运行了两次，怎么会这样呢？？？



【用form表单的action 发送请求，报以下错误】【2019-11-22-周五10.17】
java.lang.IllegalStateException: Cannot call sendError() after the response has been committed
	at org.apache.catalina.connector.ResponseFacade.sendError(ResponseFacade.java:451)
	at org.apache.catalina.servlets.DefaultServlet.serveResource(DefaultServlet.java:781)
原因：请求地址错误 
<form id="registerForm" action = "/registUserServlet" method = post> 错误

<form id="registerForm" action = "registUserServlet" method = post> correct，【为什么HTML这样写就可以了，不需要加虚拟路径名吗？？】


严重: Servlet.service() for servlet [cn.itcast.travel.web.servlet.RegistUserServlet] in context with path [/travel] threw exception
org.springframework.dao.DataIntegrityViolationException: PreparedStatementCallback; SQL [insert into tab_user(username,password,name,birthday,sex,telephone,email) value(?,?,?,?,?,?,?);]; Data truncation: Data too long for column 'sex' at row 1; nested exception is com.mysql.jdbc.MysqlDataTruncation: Data truncation: Data too long for column 'sex' at row 1
	at org.springframework.jdbc.support.SQLStateSQLExceptionTranslator.doTranslate(SQLStateSQLExceptionTranslator.java:102)
原因：由于没有将中文编码，导致数据村数据库报错。

【有个很奇怪的问题，发送一次请求，servlet却执行了两次？？】


【2019-11-22-周五-14.51】
明明数据库里有数据，但是执行这语句查不到语句

//1,定义sql语句
 String sql = "select * from tab_user where username = ?";
// String sql = "select * from tab_user where username = ?";
 //进行查询
 user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),userName);

  思考解决方法，写一个Test类，main方法，单元测试原因：  不涉及，web容器初始化的问题，都可以写一个test类。

【2019-12-1-周日-17.55】
一： 从新新建了一个项目，从新写过这个项目，注意细节

【self：debug启动的时候报错：如下】
Capture agent: unable to read settings
java.io.FileNotFoundException: C:\Users\ÀîÓÀÆô\AppData\Local\Temp\capture2.props (系统找不到指定的路径。)
	at java.io.FileInputStream.open0(Native Method)
	at java.io.FileInputStream.open(FileInputStream.java:195)
	at java.io.FileInputStream.<init>(FileInputStream.java:138)
	at java.io.FileInputStream.<init>(FileInputStream.java:93)
	at java.io.FileReader.<init>(FileReader.java:58)
	at com.intellij.rt.debugger.agent.CaptureAgent.readSettings(CaptureAgent.java:93)
	at com.intellij.rt.debugger.agent.CaptureAgent.premain(CaptureAgent.java:31)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.instrument.InstrumentationImpl.loadClassAndStartAgent(InstrumentationImpl.java:386)
	at sun.instrument.InstrumentationImpl.loadClassAndCallPremain(InstrumentationImpl.java:401)

【self：从新建立一个项目，注册功能OK，之前出现那样的错误是自己的写的代码有问题：】

【2019-12-12-周四-23.46】

   敲代码的思路：
      多看别人写的代码；
      注意局部变量与成员变量。注意一些方法的使用，与使用的技巧。
【*】e.printStackTrace();把异常打印到控制台。
try {
    String sql = "select * from tab_user where username=?";
    user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username);
} catch (DataAccessException e) {
   // e.printStackTrace();
}

【2019-12-13-周五-15.42】
一：邮件激活
一：发送邮件
   由工具类实现，懂得会怎么改。


二： 用户点击邮箱激活

response.setContentType("application/json;charset=utf-8");
response.setContentType("text/html;charset=utf-8");

激活功能有个地址问题，在抽取了 baseservlet 后，访问的时候会有个问题？？暂时没解决
二：登陆功能
一：分析，功能制作的逻辑

 1 编写loginServlet
3	获取登陆信息
4	调用service
5	查询用户是否存在
6	判断用户是否激活

 
  图上为服务器响应问题。
$(function () {
    $.get("header.html",function (data) {
        $("#header").html(data);
    });
    $.get("footer.html",function (data) {
        $("#footer").html(data);
    });
});
 Ajax可以直接请求，页面资源。并且返回整个页面。


【2019-12-14-周六-12.29】
一：注意这里面的请求方式。有个问题
$.post("user/regist",$(this).serialize(),function(data){

$.ajax({
   url:"user/login",
 
请求地址，是没有/user/login 
<span id="span_username"></span>
<a href="myfavorite.html" class="collection">我的收藏</a>
<a href="javascript:location.href='exitServlet';">退出</a>

//3.发送邮件激活码
String content="<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
MailUtils.sendMail(user.getEmail(),content,"激活邮箱");

请求uri: /travel/user/active
请求方法methodName： active


设置idea的编码参数：如图
 

 
请求uri: /travel/user/login.html


二：分类数据展示
编写代码思路
  1 分析
        区分功能块。
        确定代码编写的地方。分层架构。提高代码的维护性与复用性。
  2  可选择从dao –> service --> servlet 【看个人思考习惯】

Dao查询：
  

方法用的最多的进行抽取。

后台代码：

前台代码：

缓存： redis 缓存需要启动 redis服务器
       存入内存中。【关掉redis服务器，缓存没有了。】
       【疑问：咋清空redis的缓存的数据？？】

三：分页查询数据

后台代码：
  编写完后台代码，进行单元测试。确保代码能够运行起来。

JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

Caused by: java.lang.IllegalArgumentException: No DataSource specified
	at org.springframework.util.Assert.notNull(Assert.java:112)
	at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:97)
原因：没有定义DataSource

分页主要逻辑：
 //封装PageBean
PageBean<Route> pb = new PageBean<Route>();
//设置当前页码
pb.setCurrentPage(currentPage);
//设置每页显示条数
pb.setPageSize(pageSize);

//设置总记录数
int totalCount = routeDao.findTotalCount(cid);
pb.setTotalCount(totalCount);
//设置当前页显示的数据集合
int start = (currentPage - 1) * pageSize;//开始的记录数
List<Route> list = routeDao.findByPage(cid,start,pageSize);
pb.setList(list);

//设置总页数 = 总记录数/每页显示条数
int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
pb.setTotalPage(totalPage);

推演公式：
 当前页数     查询出来的数据
 currentPage    limit ? , ?  （index，rows）
  1                0,   5    
  2                5,   5
  3                10,  5
  4                15,  5
查询索引： index = (currentPage – 1 ) * rows


【2019-12-15-周日-13.56】
分页：
【注意：
  用href 点击会发生同步请求，页面跳转

 

【38-旅游线路分页展示_分页数据展示_前台代码_异步加载数据 2：39秒】	
page_lis += ' <li onclick="load('+cid+','+i+')" class="curPage"><a href="javaScript:void(0)">'+i+'</a></li>';


【black board】
View 为 html。用AJAX进行局部刷新。

[ 若 View 为 JSP 用什么方式访问服务端？？ 分页功能]

后台功能： 主要查询分页数据。


前端功能：将查询的数据进行展示。
1 页码进行展示

2 数据进行展示。
 

//创建页码的li
li = '<li onclick="javascipt:load('+cid+','+i+')"><a href="javascript:void(0)">'+i+'</a></li>';

page_lis += ' <li onclick="load('+cid+','+i+')"><a href="javaScript:void(0)">'+i+'</a></li>';



【2019-12-20-周五-17.43】
【思考：
  分类弄好了，在页面上也展示了， 那么如何，点击这个连接而不用跳转页面呢？？
  
思路：
   点击 <a href=””> 获取分类id 到 route_list.html 页面进行 数据便利，需要发送AJAX请求到后台查询数据，返回JSON数据，再遍历到，该页面上 展示。【进入页面，便初始化发送AJAX进行遍历查询。】
  接着是一些，分页的逻辑。

】


疑问： 页面时如何布局的呢？？





【2019-12-22-周日-16.56】
搜索功能设计：
 
一，明知要点：
  1 点击搜索，需要往后台传递的参数
        1 模糊搜索的  名称 name
        2  模糊搜索的   类别 cid
 2 这些数据 前台 该如何传递，后台该如何处理。

二，疑问点
1	点击搜索，怎么 拼接参数。
2	首页是没有cid的，也有可能没有 name 参数。

二， 设计方案：
  1 


验证：F:\IdeaProjects\itcast\travel\src\main\webapp\js\getParameter.js
  这个js方法 是可以获取任意的 链接 穿过来的多个参数吗？？
var li = '<li><a href="route_list.html?cid='+data[i].cid+'&name='+data[i].cname+'">'+data[i].cname+'</a></li>';
var name = getParameter(name);
alert(name);

这个方法好像不能读到 ？后面第二个参数。
 
这个是可以读取到第二个参数，刚刚缓存了。
====================
编码中的问题：
  1 cid 从前台 没有值传到后台是什么？？ 是 null 或 “” 空字符串

编码中出的错误：
  //拼接 sql 模板
String sql = "SELECT COUNT(*) FROM  tab_route WHERE 1=1 ";
sb.append("and cid = ?");
param.add(cid);

sb.append(" and rname like ?");
param.add("%"+rname+"%");

1 拼接字符串的时候，没有将 该空格的地方 空客。
2 


rname = new String(rname.getBytes("utf-8"));

为什么设置 utf-8 不行呢？？？

rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

需要这样写才可以的。

搜索字符串拼接：
var first_page = ' <li onclick="load('+cid+','+1+')"><a href="javaScript:void(0)">首页</a></li>';

var first_page = ' <li onclick="load('+cid+','+1+'+\’西安\’)"><a href="javaScript:void(0)">首页</a></li>';

var first_page = ' <li onclick="load('+cid+','+1+'+\’’+rname+’\’)"><a href="javaScript:void(0)">首页</a></li>';

我去，照着些，都总是有那么多错误出现，恶心。

模糊查询，照着写，都错洞百出，
 加油加油加油miss Lis 最棒。












【2019-12-24-周二-11.24】
路线详情功能实现
1 点击超链接，附带id，跳转到 route_detail.html 单个详情页，


JQ Ajax  $("form").serialize()
$.ajax({
url:"请求路径",
async: "false/true",
data: "请求数据",
type: "post/get",
dataType: "相应的数据类型，json/text",
success:function(result){ $("#div1").html(result); }
         
});

$.get("demo_test.html",function(data,status){ alert("Data: " + data + "nStatus: " + status); });
$.get(URL,data,function(data,status,xhr),dataType)

$.post("demo_test.html",function(data,status){ alert("Data: " + data + "nStatus: " + status); });
$(selector).post(URL,data,function(data,status,xhr),dataType)


2 图片数据，填充。
  填充后，动态轮播没有效果了。
由于轮播功能的js是在页面加载后就执行，而图片数据实在Ajax后才有。


var rid = getParameter("rid");
alert(rid);
$.ajax({
    url:"route/findOne",
    data: rid,
    type:"post",
超链接发送的请求，进入页面初始化，在页面中发送Ajax请求，为什么后台没有获取到参数的值呢？？
Data:rid 写错误，更正为 data:{key:value}

图片轮播效果要放到回调函数中执行。
//2.发送请求请求 route/findOne
 $.get("route/findOne",{rid:rid},function (route) {
        $("#dd").html(ddstr);

     //图片展示和切换代码调用
     goImg();
 });

【2019-12-28-周六-21.27】
一：收藏功能分析
一张表存储人员与路线关系。


【2019-12-29-周日-16.42】
自制力太差了，这么久还没学完做完。

public Favorite findByRidAndUid(String rid, int uid) {
    Favorite favorite = null;
    try {
        String sql = "select * from tab_favorite where rid = ? and uid = ?";
        favorite = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);

Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);
这个是视频写的，将字符串转成 int 类型。

虽然数据库的rid的类型是 int 类型，但这个  JdbcTemplate 好像也可以查。需要认清下这个东西。
 
 .prop() 设置固有属性  与 .attr() 设置一部分固有属性和自定义属性 区别在哪里？？


location.reload();
该方法是将 页面重新加载。

终于学完了。
总结：在敲代码的时候，会有很多疏忽。自主思考能力还不是很强。
