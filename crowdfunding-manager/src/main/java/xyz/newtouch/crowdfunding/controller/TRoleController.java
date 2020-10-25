package xyz.newtouch.crowdfunding.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.newtouch.crowdfunding.pojo.TRole;
import xyz.newtouch.crowdfunding.service.TRoleService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weibing
 */
@RequestMapping("/role")
@Controller
public class TRoleController {
    @Autowired
    TRoleService tRoleService;

    @RequestMapping("/index")
    public String gotoRolePage() {
        // WEB-INF/pages/role/index.jsp
        return "role/index";
    }

    /**
     * 查询角色信息并提供分页支持
     */
    @PreAuthorize("hasRole('PM - 项目经理')")
    @ResponseBody
    @RequestMapping("/loadData")
    public PageInfo<TRole> loadData(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            HttpSession session) {
        // 初始化一个分页工具
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<TRole> pageInfo = null;
        try {
            List<TRole> roles = tRoleService.getRoles(keyword);
            // 封装数据并制定默认展示逻辑页数
            pageInfo = new PageInfo<>(roles, 5);
            // 保存到session域中以供其他地方后续使用
            session.setAttribute("rolePageInfo", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }

    /**
     * 保存角色信息
     */
    @ResponseBody
    @RequestMapping("/saveRole")
    public Map<String, Object> saveRole(TRole tRole, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        try {
            tRoleService.insertRole(tRole);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果失败则返回fail和对应的错误信息
            map.put("status", "fail");
            map.put("errorMsg", e.getMessage());
        }
        PageInfo<TRole> rolePageInfo = (PageInfo<TRole>) session.getAttribute("rolePageInfo");
        // 反之返回成功和最大页数用户跳转到最后一页
        map.put("status", "success");
        // 最大逻辑页数，因为此处每次拿到的数都是新增成功之前的为了避免新增后刚好放到了下一页不能成功跳转到最后一页，所以每次都访问拿到旧页数+1的页(超出最大数时只会跳转到最后一页)
        map.put("pages", rolePageInfo.getPages() + 1);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getRoleInfo")
    public TRole getRoleInfo(Integer id) {
        return tRoleService.queryRoleById(id);
    }

    @ResponseBody
    @RequestMapping("/updateRole")
    public Map<String, Object> updateRole(TRole role, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        try {
            tRoleService.updateRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果失败则返回fail和对应的错误信息
            map.put("status", "fail");
            map.put("errorMsg", e.getMessage());
        }
        PageInfo<TRole> rolePageInfo = (PageInfo<TRole>) session.getAttribute("rolePageInfo");
        // 反之返回成功
        map.put("status", "success");
        // 返回当前页码
        map.put("pageNum", rolePageInfo.getPageNum());
        return map;
    }

    @ResponseBody
    @RequestMapping("/deleteRole")
    public Map<String, Object> deleteRole(String ids, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        String[] idList = ids.split(",");
        List<Integer> allId = new ArrayList<>();
        for (String id : idList) {
            allId.add(Integer.parseInt(id));
        }
        try {
            tRoleService.deleteRole(allId);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果失败则返回fail和对应的错误信息
            map.put("status", "fail");
            map.put("errorMsg", e.getMessage());
        }
        PageInfo<TRole> rolePageInfo = (PageInfo<TRole>) session.getAttribute("rolePageInfo");
        // 反之返回成功
        map.put("status", "success");
        // 返回当前页码
        map.put("pageNum", rolePageInfo.getPageNum());
        return map;
    }
}
