package xyz.newtouch.crowdfunding.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.newtouch.crowdfunding.mapper.TRoleMapper;
import xyz.newtouch.crowdfunding.pojo.TRole;
import xyz.newtouch.crowdfunding.pojo.TRoleExample;
import xyz.newtouch.crowdfunding.service.TRoleService;
import xyz.newtouch.crowdfunding.utils.StringUtil;

import java.util.List;

/**
 * @author weibing
 */
@Service
public class TRoleServiceImpl implements TRoleService {
    @Autowired
    private TRoleMapper tRoleMapper;

    @Override
    public List<TRole> getRoles(String keyword) {
        TRoleExample example = new TRoleExample();
        if (StringUtil.isNotEmpty(keyword)) {
            TRoleExample.Criteria criteria = example.createCriteria();
            // 根据name模糊查询
            criteria.andNameLike("%" + keyword + "%");
        }
        // 如果没有条件就是查询全部
        return tRoleMapper.selectByExample(example);
    }

    @Override
    public void insertRole(TRole tRole) {
        tRoleMapper.insertSelective(tRole);
    }

    @Override
    public TRole queryRoleById(Integer id) {
        return tRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateRole(TRole role) {
        tRoleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public void deleteRole(List<Integer> idList) {
        TRoleExample example = new TRoleExample();
        TRoleExample.Criteria criteria = example.createCriteria();
        // 使用in来删除所有满足条件的角色
        criteria.andIdIn(idList);
        // 按条件删除角色
        tRoleMapper.deleteByExample(example);
    }
}
