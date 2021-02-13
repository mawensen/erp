package com.yufeng.repository;

import com.yufeng.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 权限菜单Repository接口
 *
 * @author Wensen Ma
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    /**
     * 根据id获取权限菜单集合
     *
     * @param userId
     * @return
     */
    @Query(value = "SELECT m.* FROM t_role r,t_menu m,t_role_menu rm WHERE rm.`role_id`=r.`id` AND rm.`menu_id`=m.`id` AND r.`id`=?1", nativeQuery = true)
	List<Menu> findByRoleId(int roleId);

    /**
     * 根据父节点以及用户角色id集合查询子节点
     *
     * @param parentId
     * @param roleId
     * @return
     */
    @Query(value = "select * from t_menu where p_id=?1 and id in (SELECT menu_id FROM t_role_menu WHERE role_id=?2)", nativeQuery = true)
	List<Menu> findByParentIdAndRoleId(int parentId, int roleId);

    /**
     * 根据父节点查找所有子节点
     *
     * @param parentId
     * @return
     */
    @Query(value = "select * from t_menu where p_id=?1", nativeQuery = true)
	List<Menu> findByParentId(int parentId);
}
