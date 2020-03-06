package com.jeecms.resource.service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.resource.domain.dto.TplReSourceDto;
import com.jeecms.resource.domain.dto.TplSaveDirDto;
import com.jeecms.resource.domain.dto.TplUpdateDto;
import com.jeecms.system.domain.CmsSite;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 模板Service
 *
 * @author tom
 * @date 2019年3月5日 下午4:48:37
 */
public interface TplService {
    /**
     * 获得模板列表。根据前缀，用于选择模板。
     *
     * @param prefix 前缀
     * @param site   站点
     * @return list
     * @throws IOException 异常
     */
    List<? extends Tpl> getListByPrefix(String prefix, CmsSite site) throws IOException, GlobalException;

    /**
     * 根据模板前缀获取模板名称列表
     *
     * @param site   站点
     * @param prefix 方案名
     * @return list
     */
    List<String> getIndexBySolutionPath(String prefix, CmsSite site) throws IOException, GlobalException;

    /**
     * 获得下级模板列表。根据路径，用于展现下级目录和文件。
     *
     * @param path 路径
     * @param site 站点
     * @return list
     * @throws GlobalException 异常
     */
    List<? extends Tpl> getChild(String path, CmsSite site) throws GlobalException;

    /**
     * 保存文件夹
     *
     * @param dto  TplSaveDirDto
     * @param site 站点
     * @throws GlobalException 异常
     */
    void saveDir(TplSaveDirDto dto, CmsSite site) throws GlobalException;

    /**
     * 保存模板
     *
     * @param dto  模板Dto
     * @param site 站点
     * @throws GlobalException 异常
     */
    void save(TplReSourceDto dto, CmsSite site) throws GlobalException;

    /**
     * 保存模板
     *
     * @param path    路径
     * @param file    文件
     * @param site    站点
     * @param isCover true 替换 false 保留
     * @throws GlobalException 异常
     */
    void save(String path, MultipartFile file, CmsSite site, Boolean isCover) throws GlobalException;

    /**
     * 获得模板（对应模型页获取）
     *
     * @param name 模板
     * @param site 站点
     * @return Tpl
     * @throws GlobalException 异常
     */
    Tpl get(String name, CmsSite site) throws GlobalException;


    /**
     * 获取模板
     *
     * @param name 模板路径
     * @param site 站点
     * @return Tpl
     */
    Tpl getFile(String name, CmsSite site) throws IOException, GlobalException;

    /**
     * 修改模板
     *
     * @param dto  模板Dto
     * @param site 站点id
     * @throws GlobalException 异常
     */
    void update(TplUpdateDto dto, CmsSite site) throws GlobalException;

    /**
     * 修改模板名称或路径
     *
     * @param orig 原路径
     * @param dist 新路径
     * @param site 站点
     * @throws GlobalException 异常
     */
    void rename(String orig, String dist, CmsSite site) throws GlobalException;

    /**
     * 删除模板
     *
     * @param names 模板名称数组
     * @param site  站点
     * @return 被删除的模板数量
     * @throws GlobalException 异常
     */
    int delete(String[] names, CmsSite site) throws GlobalException;

}
