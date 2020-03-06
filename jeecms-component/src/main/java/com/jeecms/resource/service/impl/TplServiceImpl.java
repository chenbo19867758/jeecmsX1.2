/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.service.impl;

import com.jeecms.common.constants.TplConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.service.CmsModelTplService;
import com.jeecms.resource.domain.dto.TplReSourceDto;
import com.jeecms.resource.domain.dto.TplSaveDirDto;
import com.jeecms.resource.domain.dto.TplUpdateDto;
import com.jeecms.resource.service.Tpl;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.resource.service.TplService;
import com.jeecms.system.domain.CmsSite;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jeecms.common.constants.WebConstants.SPT;
import static com.jeecms.common.constants.WebConstants.UTF8;

/**
 * 模板Service实现类
 *
 * @author tom
 * @date 2019年3月5日 下午4:48:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "freemarker.resources.type", havingValue = "local", matchIfMissing = true)
public class TplServiceImpl implements TplService {
    private static Logger log = LoggerFactory.getLogger(TplServiceImpl.class);

    @Override
    public int delete(String[] names, CmsSite site) throws GlobalException {
        File f;
        int count = 0;
        for (String name : names) {
            f = new File(realPathResolver.get(name));
            if (f.isDirectory()) {
                if (FileUtils.deleteQuietly(f)) {
                    count++;
                }
            } else {
                if (f.delete()) {
                    // 模板方案名
                    name = name.replace(site.getTplPath() + SPT, "");
                    int num = name.indexOf(SPT);
                    String solution = "";
                    if (num != -1) {
                        solution = name.substring(0, num);
                    }
                    //模板方案名不为空
                    if (StringUtils.isNotBlank(solution)) {
                        name = name.replace(solution + SPT, "");
                    }

                    List<CmsModelTpl> list = modelTplService.findByTplPath(site.getId(), name, solution);
                    modelTplService.physicalDeleteInBatch(list);
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Tpl get(String name, CmsSite site) {
        File f = new File(realPathResolver.get(name));
        String tplPath = site.getTplPath();

        if (f.exists() && !f.isDirectory()) {
            // 模板方案名
            name = name.replace(tplPath + SPT, "");
            int num = name.indexOf(SPT);
            String solution = "";
            if (num != -1) {
                solution = name.substring(0, num);
            }
            //模板方案名不为空
            if (StringUtils.isNotBlank(solution)) {
                name = name.replace(solution + SPT, "");
            }

            List<CmsModelTpl> modelTpls = modelTplService.findByTplPath(site.getId(), name, solution);
            Integer[] models = new Integer[modelTpls.size()];
            for (int i = 0; i < modelTpls.size(); i++) {
                models[i] = modelTpls.get(i).getModelId();
            }
            FileTpl fileTpl = new FileTpl(f, root);
            fileTpl.setModels(models);
            return fileTpl;
        } else {
            return null;
        }
    }

    @Override
    public Tpl getFile(String name, CmsSite site) {
        File f = new File(realPathResolver.get(name));
        if (f.exists()) {
            return new FileTpl(f, root);
        } else {
            return null;
        }
    }

    @Override
    public List<Tpl> getListByPrefix(String prefix, CmsSite site) {
        File f = new File(realPathResolver.get(prefix));
        if (f.exists()) {
            File[] files = f.listFiles();
            if (files != null) {
                List<Tpl> list = new ArrayList<Tpl>();
                for (File file : files) {
                    list.add(new FileTpl(file, root));
                }
                return list;
            } else {
                return new ArrayList<Tpl>(0);
            }
        } else {
            return new ArrayList<Tpl>(0);
        }
    }

    @Override
    public List<String> getIndexBySolutionPath(String solutionPath, CmsSite site) {
        String url = site.getTplIndexPrefix(solutionPath);
        List<Tpl> list = getListByPrefix(url, site);
        List<String> result = new ArrayList<String>(list.size());
        for (Tpl tpl : list) {
            result.add(tpl.getName());
        }
        return result;
    }

    @Override
    public List<Tpl> getChild(String path, CmsSite site) {
        File file = new File(realPathResolver.get(path));
        File[] child = file.listFiles();
        if (child != null) {
            List<Tpl> list = new ArrayList<Tpl>(child.length);
            for (File f : child) {
                FileTpl fileTpl = new FileTpl(f, root);
                list.add(fileTpl);
            }
            return list;
        } else {
            return new ArrayList<Tpl>(0);
        }
    }

    @Override
    public void rename(String orig, String dist, CmsSite site) throws GlobalException {
        String os = realPathResolver.get(orig);
        File of = new File(os);
        String ds = realPathResolver.get(dist);
        File df = new File(ds);
        try {
            if (of.isDirectory()) {
                FileUtils.moveDirectory(of, df);
            } else {
                FileUtils.moveFile(of, df);
                //模板删除成功删除模型关联
                // 模板方案名
                String name = orig.replace(site.getTplPath() + SPT, "");
                String distRep = dist.replace(site.getTplPath() + SPT, "");
                int nameSpt = name.indexOf(SPT);
                String solution = "";
                if (nameSpt != -1) {
                    solution = name.substring(0, nameSpt);
                }
                int distSpt = distRep.indexOf(SPT);
                String distSolution = "";
                if (distSpt != -1) {
                    distSolution = distSolution.substring(0, distSpt);
                }
                //模板方案名不为空
                if (StringUtils.isNotBlank(solution)) {
                    name = name.replace(solution + SPT, "");
                }
                //模板方案名不为空
                if (StringUtils.isNotBlank(distSolution)) {
                    distRep = dist.replace(distSolution + SPT, "");
                }
                List<CmsModelTpl> list = modelTplService.findByTplPath(site.getId(), name, solution);
                for (CmsModelTpl modelTpl : list) {
                    modelTpl.setTplPath(dist);
                    modelTplService.update(modelTpl);
                }
            }
        } catch (IOException e) {
            log.error("Move template error: " + orig + " to " + dist, e);
        }
    }

    @Override
    public void saveDir(TplSaveDirDto dto, CmsSite site) {
        String real = realPathResolver.get(dto.getRoot() + WebConstants.SPT + dto.getDirName());
        File f = new File(real);
        f.mkdirs();
    }

    @Override
    public void save(TplReSourceDto dto, CmsSite site) throws GlobalException {
        String name = getRoot(dto.getRoot(), site) + "/" + dto.getFilename() + TplConstants.TPL_SUFFIX;
        String real = realPathResolver.get(name);
        File f = new File(real);
        try {
            FileUtils.writeStringToFile(f, dto.getSource(), UTF8);
            Integer[] modelIds = dto.getModels();
            //添加模板模型关联
            modelTplService.saveBatch(dto.getRoot(), site, name, modelIds);
        } catch (IOException e) {
            log.error("Save template error: " + name, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String path, MultipartFile file, CmsSite site, Boolean isCover) throws GlobalException {
        File directory = new File(realPathResolver.get(path));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            String fileName = file.getOriginalFilename();
            //isCover为空或者isCover为false贼保留原文件
            if (isCover == null || !isCover) {
                fileName = resourceService.filename(path, fileName, 0);
            }
            File f = new File(realPathResolver.get(path), fileName);
            file.transferTo(f);
        } catch (IllegalStateException e) {
            log.error("upload template error!", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("upload template error!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(TplUpdateDto dto, CmsSite site) throws GlobalException {
        String name = dto.getRoot();
        String newName = dto.getFilename();
        String real = realPathResolver.get(name);
        File f = new File(real);
        try {
            FileUtils.writeStringToFile(f, dto.getSource(), UTF8);
            //如果原名称和旧名称不一样，做修改名称操作
            if (!name.equals(newName)) {
                //修改文件名称
                rename(name, newName, site);
            }
            Integer[] modelIds = dto.getModels();
            //先删除原来的数据再添加
            String fname = name.replace(site.getTplPath() + WebConstants.SPT, "");
            fname = fname.substring(fname.indexOf(WebConstants.SPT) + 1);
            // 模板方案名
            name = name.replace(site.getTplPath() + SPT, "");
            String solution = "";
            int num = name.indexOf(SPT);
            if (num != -1) {
                solution = name.substring(0, num);
            }
            //模板方案名不为空
            if (StringUtils.isNotBlank(solution)) {
                name = name.replace(solution + SPT, "");
            }

            List<CmsModelTpl> list = modelTplService.findByTplPath(site.getId(), name, solution);
            modelTplService.physicalDeleteInBatch(list);
            //添加模板模型关联
            modelTplService.saveBatch(dto.getRoot(), site, newName, modelIds);
        } catch (IOException e) {
            log.error("Update template error: " + name, e);
            throw new RuntimeException(e);
        }
    }

    private RealPathResolver realPathResolver;

    private String root;

    /**
     * 获取站点路径
     *
     * @param root 路径
     * @param site 站点
     * @return String
     */
    private String getRoot(String root, CmsSite site) {
        if (StringUtils.isBlank(root)) {
            return site.getTplPath();
        }
        return root;
    }

    @Autowired
    public void setRealPathResolver(RealPathResolver realPathResolver) {
        this.realPathResolver = realPathResolver;
        root = realPathResolver.get("");
    }

    private static class PrefixFilter implements FileFilter {
        private String prefix;

        public PrefixFilter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean accept(File file) {
            String name = file.getName();
            return file.isFile() && name.startsWith(prefix);
        }
    }

    @Autowired
    private CmsModelTplService modelTplService;
    @Autowired
    private TplResourceService resourceService;

}
