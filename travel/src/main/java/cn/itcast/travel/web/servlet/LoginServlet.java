package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         //获取用户名和密码数据 【序列化为 json 的数据 可以用 getParamete()获取数据？？】
        System.out.println("username: "+request.getParameter("username"));
        System.out.println("password: "+request.getParameter("password"));
        Map<String,String[]> map = request.getParameterMap(); //底层是如何将参数封装进 Bean里的呢？？

        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service
        UserService userService = new UserServiceImpl();
        User u =  userService.login(user);

        ResultInfo info = new ResultInfo();
        //判断用户是否存在
        if(u == null){
            //用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
        }
        if(u != null && !"Y".equals(u.getStatus())){
            //邮箱未激活
            info.setFlag(false);
            info.setErrorMsg("账号未激活");
        }
        if(u != null && "Y".equals(u.getStatus())){ //user.getStatus().equals("Y")
            request.getSession().setAttribute("user",u);
            info.setFlag(true);
        }
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(info);
        response.setContentType("application/json;charset=utf-8");
       // response.getWriter().write(result);
        mapper.writeValue(response.getOutputStream(),info);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         this.doPost(request,response);
    }
}
