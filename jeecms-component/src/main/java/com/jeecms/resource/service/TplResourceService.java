/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.Zipper;
import com.jeecms.system.domain.CmsSite;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 模板资源管理接口
 *
 * @author tom
 * @version 1.0
 * @date 2018年11月5日 下午3:54:35
 */
public interface TplResourceService {

    /**
     * 获得子文件列表
     *
     * @param path           父文件夹路径
     * @param dirAndEditable 是否只获取文件夹和可编辑文件
     * @param site           站点
     * @return List
     */
    List<? extends Tpl> listFile(String path, boolean dirAndEditable, CmsSite site) throws IOException, GlobalException;

    /**
     * 保存文件
     *
     * @param path    保存路径
     * @param file    保存文件
     * @param isCover true 替换 false 保留
     * @throws GlobalException 异常
     */
    void saveFile(String path, MultipartFile file, Boolean isCover) throws GlobalException;

    /**
     * 创建文件夹
     *
     * @param path    当前目录
     * @param dirName 文件夹名
     * @param site    站点
     * @throws IOException     IO异常
     * @throws GlobalException 异常
     */
    void createDir(String path, String dirName, CmsSite site) throws IOException, GlobalException;

    /**
     * 创建文件
     *
     * @param path     当前目录
     * @param filename 文件名
     * @param data     文件内容
     * @param site     站点
     * @throws GlobalException 异常
     */
    void createFile(String path, String filename, String data, CmsSite site) throws GlobalException;

    /**
     * 读取文件
     *
     * @param name 文件名称
     * @return 文件内容
     * @throws GlobalException 异常
     */
    Tpl readFile(String name) throws GlobalException;

    /**
     * 修改文件
     *
     * @param root    路径
     * @param site    站点
     * @param name    文件名称
     * @param newName 文件新名称
     * @param data    文件内容
     * @throws IOException     IO异常
     * @throws GlobalException 异常
     */
    void updateFile(String root, String name, String newName, String data,
                    CmsSite site) throws IOException, GlobalException;

    /**
     * 文件重命名
     *
     * @param origName 原文件名
     * @param site     站点
     * @param destName 目标文件名
     * @throws IOException     IO异常
     * @throws GlobalException 异常
     */
    void rename(String origName, String destName, CmsSite site) throws IOException, GlobalException;

    /**
     * 删除文件
     *
     * @param site  站点
     * @param names 文件名数组
     * @return 成功数量
     * @throws GlobalException 异常
     */
    int delete(String[] names, CmsSite site) throws IOException, GlobalException;

    /**
     * 拷贝模板及资源
     *
     * @param from 源站点
     * @param to   目标站点
     * @throws IOException IO异常
     */
    void copyTplAndRes(CmsSite from, CmsSite to) throws IOException;

    /**
     * 删除模板及资源
     *
     * @param site 站点
     * @throws IOException     IO异常
     * @throws GlobalException 异常
     */
    void delTplAndRes(CmsSite site) throws IOException, GlobalException;

    /**
     * 列出所有模板方案
     *
     * @param path 模板方案路径
     * @return 方案名数组
     */
    String[] getSolutions(String path) throws GlobalException;

    /**
     * 导出模板
     *
     * @param site     站点
     * @param solution 方案名
     * @return List
     * @throws GlobalException 异常
     */
    List<Zipper.FileEntry> export(CmsSite site, String solution) throws GlobalException;

    /**
     * 导入模板
     *
     * @param file    文件
     * @param site    站点
     * @param isCover 是否覆盖 true 覆盖 false 保存
     * @throws IOException     IO异常
     * @throws GlobalException 异常
     */
    void imoport(File file, CmsSite site, Boolean isCover) throws IOException, GlobalException;

    /**
     * 文件重名处理
     *
     * @param tplRoot  路径
     * @param filename 文件名
     * @param i        序号
     * @return 放回结果 文件名(i)
     */
    String filename(String tplRoot, String filename, int i) throws GlobalException;
}
