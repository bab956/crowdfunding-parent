package xyz.newtouch.crowdfunding.service;

import org.springframework.stereotype.Service;
import xyz.newtouch.crowdfunding.pojo.TAdmin;

import java.util.List;

/**
 * @author weibing
 */
public interface TAdminService {
    /**
     * 登录使用
     * @param tAdmin 用户信息
     * @return 如果传入的用户名存在且用户名和密码成功匹配则返回对应的用户名信息，不存在返回null
     */
    TAdmin getTAdmin(TAdmin tAdmin);

    /**
     * 根据查询条件(非必要参数)查询出对应的用户信息
     * @param keyword 查询条件，没有条件传递空字符串
     * @return 对应的用户信息
     */
    List<TAdmin> getUsers(String keyword);

    /**
     * 添加用户信息
     * @param tAdmin 需要添加的用户信息
     */
    void insertUser(TAdmin tAdmin);

    /**
     * 根据用户id查询出对应的用户信息
     * @param id 用户id
     * @return 对应的用户信息
     */
    TAdmin queryUserById(Integer id);

    /**
     * 修改用户信息
     * @param tAdmin 修改后的用户信息
     */
    void updateUser(TAdmin tAdmin);

    /**
     * 删除用户信息
     * @param id 需要删除的用户的id
     */
    void deleteUserById(Integer id);

    /**
     * 删除传递的id对应的所有user
     * @param idList 需要删除的user列表
     */
    void deleteUsers(List<Integer> idList);
}
