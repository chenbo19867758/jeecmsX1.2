package com.jeecms.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.dao.CoreApiDao;
import com.jeecms.auth.domain.CoreApi;
import com.jeecms.auth.service.CoreApiService;
import com.jeecms.common.base.domain.Sort;
import com.jeecms.common.base.domain.SortDto;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;

/**
 * Api管理service实现类
 * 
 * @author: chenming
 * @date: 2019年4月9日 下午3:37:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreApiServiceImpl extends BaseServiceImpl<CoreApi, CoreApiDao, Integer> implements CoreApiService {

        @Override
        public List<CoreApi> sort(SortDto sortDto) throws GlobalException {
                List<CoreApi> apiList = new ArrayList<>();
                for (Sort sort : sortDto.getSorts()) {
                        CoreApi coreApi = super.get(sort.getId());
                        coreApi.setSortNum(sort.getSortNum());
                        apiList.add(super.update(coreApi));
                }
                return apiList;
        }

        @Override
        @Transactional(readOnly = true, rollbackFor = Exception.class)
        public CoreApi findByUrl(String apiUrl, Short requestMethod) {
                CoreApi result = null;
                if (requestMethod != null && requestMethod.intValue() != 0) {
                        result = dao.findByApiUrlAndRequestMethodAndHasDeleted(apiUrl, requestMethod, false);
                        return result;
                }
                result = dao.findByApiUrlAndHasDeleted(apiUrl, false);
                return result;
        }

}
