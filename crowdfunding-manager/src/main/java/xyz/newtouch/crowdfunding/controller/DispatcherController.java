package xyz.newtouch.crowdfunding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.newtouch.crowdfunding.mapper.TMenuMapper;
import xyz.newtouch.crowdfunding.pojo.TMenu;

import java.util.List;

/**
 * @author weibing
 */
@Deprecated
@Controller
public class DispatcherController {

    /**
     * 这里会提示找不到tMenuMapper，因为tMenuMapper没有直接通过注解配置到ioc而是项目启动时通过web.xml的配置将所有满足san的类加载到IOC，不用管，运行起来后可以找到)
     */
    @Autowired
    private TMenuMapper tMenuMapper;

    @ResponseBody
    @GetMapping("/allMenus")
    public List<TMenu> getAllMenus() {
        // 查询所有菜单数据
        return tMenuMapper.selectByExample(null);
    }
}