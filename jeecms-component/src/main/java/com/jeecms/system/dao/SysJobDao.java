/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SysJob;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description:定时任务dao接口
 * @author: tom
 * @date: 2018年6月12日 上午9:43:35
 */
public interface SysJobDao extends IBaseDao<SysJob, Integer> {

        /**
         * 根据名称查找
         *
         * @param cronName   名称
         * @param hasDeleted 是否删除
         * @return SysJob
         * @throws GlobalException 程序异常
         * @Title: findByNameAndHasDeleted
         * @return: SysJob
         */
        SysJob findByCronNameAndHasDeleted(String cronName, boolean hasDeleted) throws GlobalException;

        /**
         * 查找分组名称
         *
         * @return SysJob
         * @Title: findGroupName
         * @return: List<String>
         */
        @Query("select t.cronType from SysJob t where t.hasDeleted = 0 and t.siteId = ?1 group by t.cronType ")
        List<Integer> findCronType(Integer siteId);
}
