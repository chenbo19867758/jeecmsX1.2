package com.jeecms.auth.service;

import java.util.List;

import com.jeecms.auth.domain.CoreApi;
import com.jeecms.common.base.domain.SortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * Api管理接口
 * 
 * @author: chenming
 * @date: 2019年4月9日 下午3:37:15
 */
public interface CoreApiService extends IBaseService<CoreApi, Integer> {

        /**
         * 排序
         * 
         * @param sortDto
         * @return List
         * @throws GlobalException
         */
        List<CoreApi> sort(SortDto sortDto) throws GlobalException;

        /**
         * 根据apiurl查询
         * 
         * @param apiUrl
         *                apiUrl地址
         * @param requestMethod
         *                request method
         * @return api
         */
        CoreApi findByUrl(String apiUrl, Short requestMethod);

}
