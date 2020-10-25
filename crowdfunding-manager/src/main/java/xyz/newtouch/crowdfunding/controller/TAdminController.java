package xyz.newtouch.crowdfunding.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.newtouch.crowdfunding.pojo.TAdmin;
import xyz.newtouch.crowdfunding.pojo.TMenu;
import xyz.newtouch.crowdfunding.service.TAdminService;
import xyz.newtouch.crowdfunding.service.TMenuService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weibing
 */
@RequestMapping("/admin")
@Controller
public class TAdminController {

    @Autowired
    private TAdminService tAdminService;
    @Autowired
    private TMenuService tMenuService;

    @RequestMapping("/main")
    public String goToMain(HttpSession session) {
        List<TMenu> tMenus = tMenuService.getTMenus();
        List<TMenu> parentMenus = new ArrayList<>();
        // 遍历得到所有顶级父节点
        for (TMenu tMenu : tMenus) {
            // 如果其父id是0则证明其没有父节点，也就是说其自身是一个父节点
            if (tMenu.getPid() == 0) {
                parentMenus.add(tMenu);
            }
        }
        // 遍历得到所有子菜单(注意：此写法只能兼容2个层级的菜单组合)
        for (TMenu childMenu : tMenus) {
            // 只要子菜单
            if (childMenu.getPid() != 0) {
                // 遍历所有父菜单
                for (TMenu parentMenu : parentMenus) {
                    // 判断当前父菜单的id是否与当前子菜单的pid相等
                    if (parentMenu.getId().equals(childMenu.getPid())) {
                        // 如果当前父菜单的id与子菜单的id相等，则将子菜单添加到当前父菜单的子菜单列表
                        parentMenu.getSubmenus().add(childMenu);
                        // 正常情况下一个子菜单只会有一个父菜单，故找到后直接结束内层循环继续下次外层循环
                        break;
                    }
                }
            }
        }
        // 将组装好的菜单放到session域中
        session.setAttribute("parentMenus", parentMenus);
        // 跳转到src/main/webapp/WEB-INF/pages/main.jsp(登录成功后展示的页面)
        return "main";
    }

    /**
     * 已变更为调用权限框架默认提供的login接口
     */
    @Deprecated
    @RequestMapping("/login")
    public String login(TAdmin tAdmin, HttpSession session, Model model) {
        TAdmin logAdmin = tAdminService.getTAdmin(tAdmin);

        if (logAdmin != null) {
            session.setAttribute("logAdmin", logAdmin);
            // 指定重定向到本类的main接口
            return "redirect:/admin/main";
        } else {
            // 登录提示放到request域就行，否则在关闭浏览器之前在有错误信息的情况下进入login就会一直显示上一次失败的信息
            model.addAttribute("loginErrorMsg", "用户名或密码不正确");
            // 不需要系统的视图解析器所以使用显示请求转发(这个不能改因为，loginErrorMsg是放在request域中的改成重定向是拿不到上一个页面的内容的)
            return "forward:/login.jsp";
        }
    }

    /**
     * 已变更为调用权限框架默认提供的logout接口
     */
    @Deprecated
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        // 如果session已经不再了(自动失效了)则直接跳转不再调用invalidate
        if (session != null) {
            // 将session置为无效
            session.invalidate();
        }
        // 重定向到当前工程启动时默认打开的页面，所谓的默认首页(比如当前项目就是login.jsp)
        return "redirect:/";
    }

    /**
     * 为user页面提供支持
     * @param keyword 查询关键字
     * @param currentPageNum 当前页
     * @param pageSize 每页展示的数量
     * @param session 用于存放用户信息到session域
     * @return “用户维护”页面
     */
    @PreAuthorize("hasAnyRole('PM - 项目经理', 'SA - 软件架构师')")
    @RequestMapping("/user")
    public String user(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "currentPageNum",required = false, defaultValue = "1") Integer currentPageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            HttpSession session) {
        // 初始化一个PageHelper(前面已经在mybatis配置文件中引入的分页工具(插件)PageInterceptor)，指定当前页和每页展示的条数，达到SQL中的limit currentPageNum,pageSize
        PageHelper.startPage(currentPageNum, pageSize);
        // 根据条件获取对应的用户，where loginacct like %?% or username like %?% or email like %?%
        List<TAdmin> users =  tAdminService.getUsers(keyword);
        // navigatePages：指定展示的逻辑页数(就是底部的那个第n ~ x的选项，大于5页也默认只展示5个选项，小于5页展示对应的页数，切换后自动切换对应数值)
        PageInfo<TAdmin> pageInfo = new PageInfo<>(users, 5);
        // 直接把处理后的对象放到session，不仅可以使用其中的参数还可以使用分页插件提供的一些方法
        session.setAttribute("pageInfo", pageInfo);
        // 跳转到WEB-INF/pages/admin/user.jsp
        return "admin/user";
    }

    /**
     * 跳转到用户新增页面
     */
    @RequestMapping("/addPage")
    public String goToAddPage() {
        // WEB-INF/pages/admin/add.jsp
        return "admin/add";
    }

    /**
     * 新增用户
     * @param tAdmin 新增的用户信息
     * @param model 用于存放insert时出现的异常信息到request域
     */
    @RequestMapping("/addUser")
    public String addUser(TAdmin tAdmin, Model model, HttpSession session) {
        try {
            tAdminService.insertUser(tAdmin);
        } catch (Exception e) {
            model.addAttribute("insertUserErrorMsg", e.getMessage());
            // 新增失败
            return "admin/add";
        }
        // 从session域中获取pageInfo并强转回PageInfo
        PageInfo<TAdmin> pageInfo = (PageInfo<TAdmin>) session.getAttribute("pageInfo");
        // 跳转到当前类中的user接口，通过pageInfo.getPages()获取最后一页的页码
        return "redirect:/admin/user?currentPageNum=" + pageInfo.getPages();
    }

    /**
     * 跳转到用户修改页面
     * @param id 需要修改的用户id用户查询用户信息回显
     * @param model 用于存储查询到的用户信息或错误信息
     */
    @RequestMapping("/editPage")
    public String goToEditPage(Integer id, Model model) {
        try{
            TAdmin updateUserInfo = tAdminService.queryUserById(id);
            // 将查询到的用户信息放到request域中
            model.addAttribute("updateUserInfo", updateUserInfo);
        } catch (Exception e) {
            model.addAttribute("serverErrorMsg", e.getMessage());
            // 修改失败跳转到用户维护页面
            return "forward:/admin/user";
        }
        // WEB-INF/pages/admin/edit.jsp
        return "admin/edit";
    }

    /**
     * 修改用户信息
     * @param tAdmin 需要修改的用户信息
     * @param model 修改失败时存放错误信息
     * @param session 用于获取页面页码
     */
    @RequestMapping("/updateUser")
    public String updateUser(TAdmin tAdmin, Model model, HttpSession session) {
        try{
            tAdminService.updateUser(tAdmin);
        } catch (Exception e) {
            model.addAttribute("updateUserErrorMsg", e.getMessage());
            // 修改失败回到编辑页
            return "admin/edit";
        }
        // 从session域中获取pageInfo并强转回PageInfo
        PageInfo<TAdmin> pageInfo = (PageInfo<TAdmin>) session.getAttribute("pageInfo");
        // 跳转到当前类中的user接口，通过pageInfo.getPageNum()获取请求时的当前页码
        return "redirect:/admin/user?currentPageNum=" + pageInfo.getPageNum();
    }

    /**
     * 根据id删除用户信息
     */
    @RequestMapping("/delUserItem")
    public String delUserItem(Integer id, Model model, HttpSession session) {
        try{
            tAdminService.deleteUserById(id);
        } catch (Exception e) {
            model.addAttribute("serverErrorMsg", e.getMessage());
            // 修改失败回到用户维护页面
            return "admin/user";
        }
        // 从session域中获取pageInfo并强转回PageInfo
        PageInfo<TAdmin> pageInfo = (PageInfo<TAdmin>) session.getAttribute("pageInfo");
        // 跳转到当前类中的user接口，通过pageInfo.getPageNum()获取请求时的当前页码
        return "redirect:/admin/user?currentPageNum=" + pageInfo.getPageNum();
    }

    /**
     * 批量删除传入的用户id
     */
    @RequestMapping("/batchDeletion")
    public String batchDeletion(String ids, Model model, HttpSession session) {
        String[] userIdList = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : userIdList) {
            idList.add(Integer.parseInt(id));
        }

        try{
            tAdminService.deleteUsers(idList);
        } catch (Exception e) {
            model.addAttribute("serverErrorMsg", e.getMessage());
            // 修改失败回到用户维护页面
            return "admin/user";
        }
        // 从session域中获取pageInfo并强转回PageInfo
        PageInfo<TAdmin> pageInfo = (PageInfo<TAdmin>) session.getAttribute("pageInfo");
        // 跳转到当前类中的user接口，通过pageInfo.getPageNum()获取请求时的当前页码
        return "redirect:/admin/user?currentPageNum=" + pageInfo.getPageNum();
    }
}
