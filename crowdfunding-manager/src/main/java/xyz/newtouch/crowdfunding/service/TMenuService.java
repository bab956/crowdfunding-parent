package xyz.newtouch.crowdfunding.service;

import xyz.newtouch.crowdfunding.pojo.TMenu;

import java.util.List;

/**
 * @author weibing
 */
public interface TMenuService {

    /**
     * 获取所有菜单
     * @return 所有菜单信息
     */
    List<TMenu> getTMenus();
}
