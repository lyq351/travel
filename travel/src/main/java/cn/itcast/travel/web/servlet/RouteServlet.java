package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService  = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       // request.setCharacterEncoding("utf-8");
        String currentPageStr = request.getParameter("currentPage"); //当前页码
        String pageSizeStr = request.getParameter("pageSize");  //当前页显示的数据量
        String cidStr = request.getParameter("cid"); //类别id
        String rname = request.getParameter("rname");

       // if(rname != null && rname.length() > 0){
            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
       // }


        //初始化参数
        int cid = 0;//类别id
        //2.处理参数
        if(cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 0; //当前页码，如果不传递，则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
             currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5;
        }


        //调用service
        PageBean<Route> pb  =routeService.pageQuery(cid,currentPage,pageSize,rname);
        //写到客户端
        writeValue(pb,response);
    }

    /**
     * 查询单个线路的详情
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ridStr  = request.getParameter("rid");
        //测试下，数据库是整数型，可以直接传字符串进去吗？？
        int rid = 1;
        if(ridStr != null && ridStr.length() > 0){
           rid = Integer.parseInt(ridStr);
        }
        Route route = routeService.findOne(rid);
        writeValue(route,response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;
        if(user != null){
            uid = user.getUid();
        }
        //调用service进行查询
        boolean flag = favoriteService.isFavorite(rid,uid);
        writeValue(flag,response);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
    if(user == null){//用户没有登录，回写前台进行处理，提示登录
       return; //可以在前台判断用户是否登陆。
        }else {
        //用户登录了
         uid = user.getUid();
    }
    //调用service
        favoriteService.addFavorite(rid,uid);
    }

    /**
     * 取消收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void deleteFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();
        favoriteService.delete(rid,uid);
    }
}
