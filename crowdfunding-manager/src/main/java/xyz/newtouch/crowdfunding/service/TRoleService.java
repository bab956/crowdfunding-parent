package xyz.newtouch.crowdfunding.service;

import org.springframework.stereotype.Service;
import xyz.newtouch.crowdfunding.pojo.TRole;

import java.util.List;

/**
 * @author weibing
 */
@Service
public interface TRoleService {

    /**
     * 根据条件查询对应的角色信息
     * @param keyword 需要查询的条件
     * @return 对应的角色信息
     */
    List<TRole> getRoles(String keyword);

    /**
     * 保存角色信息
     * @param tRole 需要保存的角色信息(角色只有一个名称和id(自动生成))
     */
    void insertRole(TRole tRole);

    /**
     * 根据角色id查询角色信息
     * @param id 角色id
     * @return 对应的角色信息
     */
    TRole queryRoleById(Integer id);

    /**
     * 更新角色信息
     * @param role 需要更新的角色
     */
    void updateRole(TRole role);

    /**
     * 批量删除角色
     * @param idList 角色
     */
    void deleteRole(List<Integer> idList);
}
