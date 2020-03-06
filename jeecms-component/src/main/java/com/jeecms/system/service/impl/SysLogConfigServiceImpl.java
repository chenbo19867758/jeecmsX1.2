/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.system.dao.SysLogConfigDao;
import com.jeecms.system.domain.SysLogConfig;
import com.jeecms.system.service.SysLogConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志策略Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 10:54:02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogConfigServiceImpl extends BaseServiceImpl<SysLogConfig, SysLogConfigDao, Integer> implements SysLogConfigService {

        @Value(value = "${spring.datasource.url}")
        private String url;

        @Override
        public SysLogConfig getDefault() {
                List<SysLogConfig> list = super.findAll(true);
                if (list != null && list.size() > 0) {
                        SysLogConfig logConfig = list.get(0);
                        logConfig.setSize(getTableSize());
                        return logConfig;
                }
                return null;
        }

        @Override
        public String getTableSize() {
                if (url.contains("jdbc:mysql")) {
                        String[] sf = url.split("//");
                        String sg = sf[1];
                        String[] sdString = sg.split("/");
                        String sgString = sdString[1];
                        int aInteger = sgString.indexOf("?");
                        String database = sgString.substring(0, aInteger);
                        return dao.getMySqlTables(database);
                } else if (url.contains("jdbc:oracle")) {
                        //String[] sdString = url.split(":");
                        //String  database = sdString[sdString.length - 1];
                        return dao.getOracleTables();
                } else if (url.contains("jdbc:sqlserver")) {
                        String[] sdString = url.split("DatabaseName");
                        String database = sdString[sdString.length - 1].substring(1);
                        Object obj = dao.getSqlServerTables(database);
                        String json = JSONObject.toJSONString(obj).replaceAll("\"", "");
                        String[] arr = json.substring(1, json.length() - 1).split(",");
                        String size = arr[3].replace("kb","").trim();
                        size = size.replace("KB", "").trim();
                        return (Integer.valueOf(size) / 1024) + "M";
                } else if (url.contains("jdbc:dm")) {
                        String[] sf = url.split("//");
                        String sg = sf[1];
                        String[] sdString = sg.split("/");
                        String agString = sdString[1];
                        return dao.getDmTables(agString) + "M";
                } else {
                        return "0M";
                }
        }

}