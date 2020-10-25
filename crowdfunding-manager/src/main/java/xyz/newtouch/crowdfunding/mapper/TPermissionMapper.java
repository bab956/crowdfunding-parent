package xyz.newtouch.crowdfunding.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import xyz.newtouch.crowdfunding.pojo.TPermission;
import xyz.newtouch.crowdfunding.pojo.TPermissionExample;

public interface TPermissionMapper {
    long countByExample(TPermissionExample example);

    int deleteByExample(TPermissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    int insertSelective(TPermission record);

    List<TPermission> selectByExample(TPermissionExample example);

    TPermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TPermission record, @Param("example") TPermissionExample example);

    int updateByExample(@Param("record") TPermission record, @Param("example") TPermissionExample example);

    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    /**
     * 根据用户id查询出该id对应的所有角色信息
     * @param id 用户id
     * @return 满足条件的所有权限信息
     */
    List<TPermission> getPermissionsByAdminId(Integer id);
}