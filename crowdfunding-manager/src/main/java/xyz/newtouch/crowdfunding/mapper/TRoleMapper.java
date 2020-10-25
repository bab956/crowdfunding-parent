package xyz.newtouch.crowdfunding.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import xyz.newtouch.crowdfunding.pojo.TRole;
import xyz.newtouch.crowdfunding.pojo.TRoleExample;

public interface TRoleMapper {
    long countByExample(TRoleExample example);

    int deleteByExample(TRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TRole record);

    int insertSelective(TRole record);

    List<TRole> selectByExample(TRoleExample example);

    TRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TRole record, @Param("example") TRoleExample example);

    int updateByExample(@Param("record") TRole record, @Param("example") TRoleExample example);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);

    /**
     * 根据用户id查询出该id对应的所有角色信息
     * @param id 用户id
     * @return 满足条件的所有角色信息
     */
    List<TRole> getRolesByAdminId(Integer id);
}