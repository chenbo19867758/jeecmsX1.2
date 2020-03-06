/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.service;

import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.dto.CoreMenuDto;
import com.jeecms.auth.domain.vo.MenuVO;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;

import java.util.List;

/**
 * @Description:菜单管理
 * @author: ztx
 * @date: 2018年8月10日 上午9:43:45
 */
public interface CoreMenuService extends IBaseService<CoreMenu, Integer> {

	/**
	 * 新增
	 *
	 * @param menuDto 菜单对象
	 * @return 菜单
	 * @throws GlobalException 全局异常
	 */
	CoreMenu save(CoreMenuDto menuDto) throws GlobalException;

	/**
	 * 修改
	 *
	 * @param menuDto 菜单对象
	 * @return 菜单
	 * @throws GlobalException 全局异常
	 */
	CoreMenu update(CoreMenuDto menuDto) throws GlobalException;
	
	/**
         * 查询可分配权限的菜单
         *
         * @param parentId 父菜单ID
         * @return 菜单列表
         */
        List<CoreMenu> findAllWithAuth() ;

	/**
	 * 查询
	 *
	 * @param parentId 父菜单ID 0则查询顶层栏目列表
	 * @return 菜单列表
	 * @throws GlobalException 全局异常
	 */
	List<CoreMenu> findByParams(Integer parentId) throws GlobalException;

	/**
	 * 根据路由标识查询
	 *
	 * @param routing 路由
	 * @return 菜单
	 * @throws GlobalException 全局异常
	 */
	CoreMenu findByRouting(String routing) throws GlobalException;

	/**
	 * 根据路由地址查询
	 *
	 * @param path 路由
	 * @return 菜单
	 * @throws GlobalException 全局异常
	 */
	CoreMenu findByPathAndHasDeleted(String path);

	/**
	 * 删除,级联查询子节点
	 *
	 * @param id 菜单对象ID
	 * @return 菜单集合
	 * @throws GlobalException 全局异常
	 */
	List<CoreMenu> remove(Integer id) throws GlobalException;

	/**
	 * 显示
	 *
	 * @param ids 菜单ID
	 * @return 菜单集合
	 * @throws GlobalException 全局异常
	 */
	List<CoreMenu> show(Integer... ids) throws GlobalException;

	/**
	 * 隐藏
	 *
	 * @param ids 菜单ID
	 * @return 菜单集合
	 * @throws GlobalException 全局异常
	 */
	List<CoreMenu> hide(Integer... ids) throws GlobalException;

	
	/**
         * 关闭参与权限分配
         *
         * @param ids 菜单ID
         * @return 菜单集合
         * @throws GlobalException 全局异常
         */
        List<CoreMenu> openAuth(List<Integer> ids) throws GlobalException;

        /**
         * 开启参与权限分配
         *
         * @param ids 菜单ID
         * @return 菜单集合
         * @throws GlobalException 全局异常
         */
        List<CoreMenu> closeAuth(List<Integer> ids) throws GlobalException;
        
	/**
	 * 分类获取菜单信息
	 *
	 * @param paginable 分页组键
	 * @param user      用户
	 * @return MenuVO
	 * @throws GlobalException 异常
	 */
	MenuVO getMenu(CoreUser user) throws GlobalException;
} 
