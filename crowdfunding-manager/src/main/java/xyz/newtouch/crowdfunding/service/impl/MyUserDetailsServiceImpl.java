package xyz.newtouch.crowdfunding.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.newtouch.crowdfunding.mapper.TAdminMapper;
import xyz.newtouch.crowdfunding.mapper.TPermissionMapper;
import xyz.newtouch.crowdfunding.mapper.TRoleMapper;
import xyz.newtouch.crowdfunding.pojo.TAdmin;
import xyz.newtouch.crowdfunding.pojo.TAdminExample;
import xyz.newtouch.crowdfunding.pojo.TPermission;
import xyz.newtouch.crowdfunding.pojo.TRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author weibing
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TAdminMapper tAdminMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TPermissionMapper tPermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        TAdminExample example = new TAdminExample();
        TAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TAdmin> admins = tAdminMapper.selectByExample(example);
        TAdmin admin = admins.get(0);

        // 所有的权限和角色
        Set<GrantedAuthority> authorities = new HashSet<>();

        // 当前用户拥有的所有角色
        List<TRole> roles = tRoleMapper.getRolesByAdminId(admin.getId());

        // 当前用户拥有的所有权限
        List<TPermission> permissions = tPermissionMapper.getPermissionsByAdminId(admin.getId());

        // 组装角色
        for (TRole role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        // 组装权限
        for (TPermission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return new User(admin.getLoginacct(), admin.getUserpswd(), authorities);
    }
}
