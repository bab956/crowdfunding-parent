package xyz.newtouch.crowdfunding.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.newtouch.crowdfunding.mapper.TAdminMapper;
import xyz.newtouch.crowdfunding.pojo.TAdmin;
import xyz.newtouch.crowdfunding.pojo.TAdminExample;
import xyz.newtouch.crowdfunding.service.TAdminService;
import xyz.newtouch.crowdfunding.utils.Const;
import xyz.newtouch.crowdfunding.utils.DateUtil;
import xyz.newtouch.crowdfunding.utils.MD5Util;
import xyz.newtouch.crowdfunding.utils.StringUtil;

import java.util.List;

/**
 * @author weibing
 */
@Service
public class TAdminServiceImpl implements TAdminService {

    /**
     * 运行前会提示错误不用管，已经在xml中加载了这些mapper，运行起来可以找到
     */
    @Autowired
    private TAdminMapper tAdminMapper;

    /**
     * 以变更为权限框架默认提供的login接口，如果需要继续使用则也需要和insertUser方法一样变更为BCryptPasswordEncoder加密
     * @param tAdmin 用户信息
     */
    @Deprecated
    @Override
    public TAdmin getTAdmin(TAdmin tAdmin) {
        TAdminExample example = new TAdminExample();
        // 添加查询条件
        TAdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(tAdmin.getLoginacct());
        // 先使用MD5加密再查询是否成功匹配
        criteria.andUserpswdEqualTo(MD5Util.digest(tAdmin.getUserpswd()));
        List<TAdmin> admins = tAdminMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(admins) || admins.size() > 1 ) {
            return null;
        }
        return admins.get(0);
    }

    @Override
    public List<TAdmin> getUsers(String keyword) {
        TAdminExample example = new TAdminExample();
        if (StringUtil.isNotEmpty(keyword)) {
            // 第一个条件
            TAdminExample.Criteria criteria1 = example.createCriteria();
            criteria1.andLoginacctLike("%" + keyword + "%");
            // 第二个条件
            TAdminExample.Criteria criteria2 = example.createCriteria();
            criteria2.andUsernameLike("%" + keyword + "%");
            // 第三个条件
            TAdminExample.Criteria criteria3 = example.createCriteria();
            criteria3.andEmailLike("%" + keyword + "%");
            // 默认只是取第一个条件，所以还需要自己指定后面的条件是和第一个菜单为or的关系
            example.or(criteria2);
            example.or(criteria3);
        }
        // 返回按条件查询后的结果，如果keyword是一个空字符串则不会被拼接条件直接查询全部即可(example会为null进行查询)
        return tAdminMapper.selectByExample(example);
    }

    @Override
    public void insertUser(TAdmin tAdmin) {
        // 添加创建时间
        tAdmin.setCreatetime(DateUtil.getFormatTime());
        // 添加默认密码
        tAdmin.setUserpswd(new BCryptPasswordEncoder().encode(Const.DEFAULT_PASSWORD));
        // 有两个insert方法，insertSelective方法支持动态SQL(也就是不需要传全字段会自动根据字段拼接SQL语句)
        tAdminMapper.insertSelective(tAdmin);
    }

    @Override
    public TAdmin queryUserById(Integer id) {
        return tAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateUser(TAdmin tAdmin) {
        // updateByPrimaryKeySelective支持动态sql
        tAdminMapper.updateByPrimaryKeySelective(tAdmin);
    }

    @Override
    public void deleteUserById(Integer id) {
        tAdminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteUsers(List<Integer> idList) {
        // 创建条件
        TAdminExample example = new TAdminExample();
        TAdminExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(idList);
        // 使用删除来执行这个条件
        tAdminMapper.deleteByExample(example);
    }
}
