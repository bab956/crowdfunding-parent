package xyz.newtouch.crowdfunding.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.newtouch.crowdfunding.mapper.TMenuMapper;
import xyz.newtouch.crowdfunding.pojo.TMenu;
import xyz.newtouch.crowdfunding.service.TMenuService;

import java.util.List;

/**
 * @author weibing
 */
@Service
public class TMenuServiceImpl implements TMenuService {

    @Autowired
    private TMenuMapper tMenuMapper;

    @Override
    public List<TMenu> getTMenus() {
        return tMenuMapper.selectByExample(null);
    }
}
