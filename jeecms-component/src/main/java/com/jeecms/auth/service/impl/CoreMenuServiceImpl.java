package com.jeecms.auth.service.impl;

import com.jeecms.auth.constants.AuthConstant;
import com.jeecms.auth.dao.CoreApiDao;
import com.jeecms.auth.dao.CoreMenuDao;
import com.jeecms.auth.domain.CoreApi;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.dto.CoreMenuDto;
import com.jeecms.auth.domain.vo.CoreApiVo;
import com.jeecms.auth.domain.vo.CoreUserAgent;
import com.jeecms.auth.domain.vo.MenuVO;
import com.jeecms.auth.service.CoreMenuService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.component.listener.MenuListener;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author: chenming
 * @date: 2019年4月9日 下午3:06:23
 */
@Service
@Transactional(rollbackFor = { Exception.class })
public class CoreMenuServiceImpl extends BaseServiceImpl<CoreMenu, CoreMenuDao, Integer> implements CoreMenuService {

        @Override
        public List<CoreMenu> findAllWithAuth() {
                return dao.findByIsAuthAndHasDeleted(true, false);
        }

        /**
         * 新增菜单
         */
        @Override
        @Transactional(rollbackFor = { Exception.class })
        public CoreMenu save(CoreMenuDto menuDto) throws GlobalException {
                CoreMenu coreMenu = new CoreMenu();
                MyBeanUtils.copyProperties(menuDto, coreMenu, "id");
                List<CoreApi> apiList = null;
                if (menuDto.getApiIds() != null && menuDto.getApiIds().length != 0) {
                        apiList = coreApiDao.findAllById(Arrays.asList(menuDto.getApiIds()));
                }
                if (menuDto.getMenuType() == null) {
                        coreMenu.setMenuType(CoreMenu.MENUTYPE_MENU);
                }
                coreMenu.setApis(apiList);
                super.save(coreMenu);
                /** 新增菜单权限处理 */
                for (MenuListener listener : listeners) {
                        listener.afterMenuSave(coreMenu);
                }
                return coreMenu;
        }

        /**
         * 更新菜单
         */
        @Override
        public CoreMenu update(CoreMenuDto menuDto) throws GlobalException {
                CoreMenu menuInfo = dao.getOne(menuDto.getId());
                List<CoreApi> apiList = new ArrayList<>();
                if (menuInfo.getApis() != null) {
                        menuInfo.getApis().clear();
                }
                if (menuDto.getApiIds() != null && menuDto.getApiIds().length != 0) {
                        apiList = coreApiDao.findAllById(Arrays.asList(menuDto.getApiIds()));
                }
                menuInfo.setApis(apiList);
                menuInfo.setParentId(menuDto.getParentId());
                menuInfo.setMenuName(menuDto.getMenuName());
                menuInfo.setComponent(menuDto.getComponent());
                menuInfo.setPath(menuDto.getPath());
                menuInfo.setName(menuDto.getName());
                menuInfo.setIcon(menuDto.getIcon());
                menuInfo.setRedirect(menuDto.getRedirect());
                menuInfo.setSortNum(menuDto.getSortNum());
                menuInfo.setMenuType(menuDto.getMenuType());
                menuInfo.setHidden(menuDto.getHidden());
                menuInfo.setIsAuth(menuDto.getIsAuth());
                return super.updateAll(menuInfo);
        }

        /**
         * 根据父id找到子菜单
         *
         * @Detail 父类的查询当parentId为NULL的时候不会作条件查询
         */
        @Transactional(readOnly = true, rollbackFor = Exception.class)
        @Override
        public List<CoreMenu> findByParams(Integer parentId) throws GlobalException {
                if (parentId != null && parentId == 0) {
                        parentId = null;
                }
                List<CoreMenu> menuList = dao.findByParams(parentId, false);
                return menuList;
        }

        /**
         * 根据路由标识查询菜单信息
         */
        @Transactional(readOnly = true, rollbackFor = Exception.class)
        @Override
        public CoreMenu findByRouting(String routing) {
                CoreMenu coreMenu = dao.findByNameAndHasDeleted(routing, false);
                return coreMenu;
        }

        /**
         * 根据路由标识查询菜单信息
         */
        @Transactional(readOnly = true, rollbackFor = Exception.class)
        @Override
        public CoreMenu findByPathAndHasDeleted(String path) {
                return dao.findByPathAndHasDeleted(path, false);
        }

        /**
         * 删除
         */
        @Override
        public List<CoreMenu> remove(Integer id) throws GlobalException {
                CoreMenu menu = super.findById(id);
                List<Integer> removeIdList = new ArrayList<>();
                if (!menu.getIsChild()) {
                        Map<String, String[]> params = new HashMap<>(20);
                        params.put("GTE_lft_Integer", new String[] { String.valueOf(menu.getLft()) });
                        params.put("LTE_rgt_Integer", new String[] { String.valueOf(menu.getRgt()) });
                        List<CoreMenu> sonList = super.getList(params, null, false);
                        for (CoreMenu coreMenu : sonList) {
                                removeIdList.add(coreMenu.getId());
                        }
                } else {
                        removeIdList.add(menu.getId());
                }
                Integer[] removeIdsArray = removeIdList.toArray(new Integer[removeIdList.size()]);
                return super.physicalDelete(removeIdsArray);
        }

        @Override
        public List<CoreMenu> show(Integer... ids) throws GlobalException {
                List<CoreMenu> coreMenus = super.findAllById(Arrays.asList(ids));
                for (CoreMenu it : coreMenus) {
                        it.setHidden(false);
                        super.update(it);
                }
                return coreMenus;
        }

        @Override
        public List<CoreMenu> hide(Integer... ids) throws GlobalException {
                List<CoreMenu> coreMenus = super.findAllById(Arrays.asList(ids));
                for (CoreMenu it : coreMenus) {
                        it.setHidden(true);
                        super.update(it);
                }
                return coreMenus;
        }

        @Override
        public List<CoreMenu> openAuth(List<Integer> ids) throws GlobalException {
                List<CoreMenu> coreMenus = super.findAllById(ids);
                for (CoreMenu it : coreMenus) {
                        it.setIsAuth(true);
                        super.update(it);
                }
                return coreMenus;
        }

        @Override
        public List<CoreMenu> closeAuth(List<Integer> ids) throws GlobalException {
                List<CoreMenu> coreMenus = super.findAllById(ids);
                for (CoreMenu it : coreMenus) {
                        it.setIsAuth(false);
                        super.update(it);
                }
                return coreMenus;
        }

        @Override
        public MenuVO getMenu(CoreUser user) throws GlobalException {
                List<CoreApiVo> permList = new ArrayList<CoreApiVo>();
                CoreUserAgent userAgent = new CoreUserAgent(user);
                permList.addAll(userAgent.getUserApis());
                Integer loginErrorCount = user.getLoginErrorCount();
                GlobalConfig config = configService.getGlobalConfig();
                Integer loginErrorMax = config.getLoginErrorCount();
                AuthConstant.LoginFailProcessMode processingMode = config.getProcessingMode();
                boolean nextNeedCaptcha = false;
                if (loginErrorCount > loginErrorMax
                                && processingMode.equals(AuthConstant.LoginFailProcessMode.needCaptcha)) {
                        nextNeedCaptcha = true;
                }
                return new MenuVO(nextNeedCaptcha, user.getNeedUpdatePassword(),
                                MyDateUtils.formatDate(user.getLastLoginTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN),
                                user.getUsername(), user.getLastLoginIp(),
                                userAgent.getUserMenuVos(CoreMenu.MENUTYPE_MENU), permList, user.getRoleIds(),
                                user.getOrgId());
        }

        /**
         * 递归获取树形结构
         *
         * @param lists
         *                菜单数据
         * @param pid
         *                父id
         * @return List tree list
         */
        private List<CoreMenu> getTree(List<CoreMenu> lists, Integer pid) {
                List<CoreMenu> arrys = new ArrayList<CoreMenu>();
                for (CoreMenu t : lists) {
                        boolean check = false;
                        if (t.getParentId() == pid) {
                                check = true;
                        }
                        if (!check) {
                                if (t.getParentId() != null && pid != null) {
                                        if (t.getParentId().intValue() == pid.intValue()) {
                                                check = true;
                                        }
                                }
                        }
                        if (check) {
                                t.setChildren(getTree(lists, t.getId()));
                                arrys.add(t);
                        }
                }
                return arrys;

        }

        @Autowired
        private CoreApiDao coreApiDao;
        @Autowired
        private GlobalConfigService configService;
        @Autowired
        private List<MenuListener> listeners;
}
