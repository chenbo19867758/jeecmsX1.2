package com.jeecms.admin.controller.interact;/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.admin.controller.BaseTreeAdminController;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.local.ThreadPoolService;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.ChastityUtil;
import com.jeecms.common.web.util.HttpClientUtil;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentSaveDto;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.content.service.ContentService;
import com.jeecms.interact.domain.dto.ConsumeDataDto;
import com.jeecms.interact.domain.vo.ClientInitDataVo;
import com.jeecms.interact.domain.vo.CollectContent;
import com.jeecms.interact.domain.vo.CollectResult;
import com.jeecms.resource.service.impl.UploadFileService;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采集
 * @author: tom
 * @date: 2019/11/5 10:16   
 */
@RequestMapping("/collect")
@RestController
public class CollectController extends BaseTreeAdminController<Channel, Integer> {
    static Logger logger = LoggerFactory.getLogger(CollectController.class);
    /**
     * 获取id
     */
    @RequestMapping(value = "/getId", method = RequestMethod.GET)
    public ResponseInfo getId(HttpServletRequest request) throws GlobalException {
        String[] keys = idService.getEncryptId();
        Integer siteId = SystemContextUtils.getSiteId(request);
        String id="";
        if(keys!=null&&keys.length>=2){
            id = keys[0]+","+ keys[1];
        }
        id = idService.getId();
        List<Channel> channels = SystemContextUtils.getUser(request)
                .getContentChannelsByOperator(siteId, CmsDataPerm.OPE_CONTENT_CREATE);
        if (channels.size() > 0) {
            channels = channels.stream()
                    .filter(channel -> !channel.getRecycle() && !channel.getHasDeleted()
                            && channel.getSiteId().equals(siteId))
                    .sorted(Comparator.comparing(Channel::getSortNum)
                            .thenComparing(
                                    Comparator.comparing(Channel::getCreateTime)))
                    .collect(Collectors.toList());
        }
        JSONArray channelArray = super.getChildTree(channels, false, "name", "id","isBottom");
        List<CmsModel>models = cmsModelService.findList(CmsModel.CONTENT_TYPE, siteId);
        JSONArray modelArray = new JSONArray();
        for(CmsModel m:models){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",m.getId());
            jsonObject.put("modelName",m.getModelName());
            JSONArray itemArray = new JSONArray();
            for(CmsModelItem it:m.getItemsForCollect()){
                JSONObject item = new JSONObject();
                item.put("name",it.getItemLabel());
                item.put("field",it.getField());
                item.put("dataType",it.getDataType());
                itemArray.add(item);
            }
            jsonObject.put("modelFields",itemArray);
            modelArray.add(jsonObject);
        }
        ClientInitDataVo vo = new ClientInitDataVo();
        vo.setAppId(id);
        vo.setChannels(channelArray);
        vo.setModels(modelArray);
        return new ResponseInfo(vo);
    }


    @RequestMapping(value = "/consume", method = RequestMethod.GET)
    public ResponseInfo consume(Integer taskId,HttpServletRequest request) throws GlobalException {
        CmsSite site = SystemContextUtils.getSite(request);
        Integer userId = SystemContextUtils.getUserId(request);
        String id= idService.getId();
        String identity = RequestUtils.getHeaderOrParam(request,"identity");
        CollectResult result = getRequestDatas(site,userId,taskId,id,identity);
        /**后台线程消费数据*/
        ThreadPoolService.getInstance().execute(new ContentConsumeThread(result,site,taskId,userId,id,identity));
        return new ResponseInfo();
    }

    @PostMapping(value = "/consumeByIds")
    public ResponseInfo consumeByIds(@RequestBody @Validated ConsumeDataDto idDto, HttpServletRequest request) throws GlobalException {
        CmsSite site = SystemContextUtils.getSite(request);
        Integer userId = SystemContextUtils.getUserId(request);
        String id= idService.getId();
        String identity = RequestUtils.getHeaderOrParam(request,"identity");
        /**后台线程消费数据*/
        ThreadPoolService.getInstance().execute(new ContentConsumeDataThread(site,
                idDto.getTaskId(),idDto.getIds(),userId,id,identity));
        return new ResponseInfo();
    }

    class ContentConsumeThread implements  Runnable{
        CollectResult result ;
        CmsSite site;
        Integer taskId;
        Integer userId;
        String appId;
        String identity;

        public ContentConsumeThread(CollectResult result,CmsSite site,Integer taskId,
                                    Integer userId,String appId,String identity) {
            this.result = result;
            this.site = site;
            this.taskId = taskId;
            this.userId = userId;
            this.appId = appId;
            this.identity = identity;
        }

        @Override
        public void run() {
            while(result.isNext()){
                try {
                    Thread.sleep(2000);
                    result = getRequestDatas(site,userId,taskId,appId,identity);
                }catch (Exception e){
                }
            }
        }
    }


    class ContentConsumeDataThread implements  Runnable{
        CmsSite site;
        Integer taskId;
        Integer[] dataIds;
        Integer userId;
        String appId;
        String identity;

        public ContentConsumeDataThread(CmsSite site, Integer taskId,Integer[] dataIds,
                                    Integer userId,String appId,String identity) {
            this.site = site;
            this.dataIds = dataIds;
            this.userId = userId;
            this.appId = appId;
            this.identity = identity;
            this.taskId = taskId;
        }

        @Override
        public void run() {
                try {
                    doConsumeByIds(site,userId,taskId,dataIds,appId,identity);
                }catch (Exception e){
                }
        }
    }

    private  CollectResult getRequestDatas(CmsSite site,Integer userId,Integer taskId,String appId,String identity){
        String serverUrl = SERVER_URL_BASE + "/collect/data/results/list?";
        String serverTaskGetUrl = SERVER_URL_BASE + "/collectTasks/"+taskId;
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId.toString());
        params.put("page","1");
        params.put("size","20");
        params.put("identity",identity);
        params.put("appId",appId);
        Map<String,String>headers =new HashMap<String,String>();
        headers.put("identity",identity);
        headers.put("appId",appId);
        Integer channelId = null;
        Integer modelId = null;
        JSONArray contentJson = null;
        boolean next = false;
        try {
            /**获取任务详情*/
            String result = HttpClientUtil.get(serverTaskGetUrl,headers);
            JSONObject json = JSONObject.parseObject(result);
            if(200==json.getInteger("code")){
                JSONObject data = json.getJSONObject("data");
                 channelId  = data.getInteger("channelId");
                 modelId  = data.getInteger("modelId");
                result = HttpClientUtil.postParams(serverUrl,params);
                JSONObject serverRes = JSONObject.parseObject(result);
                /**获取任务具体数据*/
                if(serverRes!=null&&serverRes.get("code").equals(200)){
                    /**尚在运行中则 0未开始或已结束 1 运行中 2暂停*/
                    JSONObject dataJson = serverRes.getJSONObject("data");
                    Integer status = dataJson.getInteger("status");
                    contentJson = dataJson.getJSONArray("contents");
                    if(status!=0){
                        next = true;
                    }
                    /**当前还能取到数据，则继续*/
                    if(contentJson!=null&&contentJson.size()>0) {
                        next = true;
                        doSaveContent(identity,appId,contentJson,site,channelId,modelId,userId);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new CollectResult(next,contentJson);
    }

    private void doSaveContent(String identity,String appId,JSONArray contentJson,CmsSite site,
                               Integer channelId,Integer modelId,Integer userId){
        List<Integer>toDelIds =  new ArrayList<Integer>();
        String serverDelUrl = SERVER_URL_BASE + "/collect/data/results";
        Map<String,String>headers =new HashMap<String,String>();
        headers.put("identity",identity);
        headers.put("appId",appId);
        CmsModel model = cmsModelService.findById(modelId);
        String defTxtModelField = null;
        List<CmsModelItem> items = model.getItems().stream().filter(m->CmsModelConstant.CONTENT_TXT.equals(m.getDataType())).sorted(Comparator.comparing(CmsModelItem::getSortNum)).collect(Collectors.toList());
        if (items != null && items.size() > 0) {
            defTxtModelField = items.get(0).getField();
        }
        String defPicModelField = null;
        List<CmsModelItem> picFields = model.getItems().stream().filter(m->CmsModelConstant.MANY_CHART_UPLOAD.equals(m.getDataType())).sorted(Comparator.comparing(CmsModelItem::getSortNum)).collect(Collectors.toList());
        if (picFields != null && picFields.size() > 0) {
            defPicModelField = picFields.get(0).getField();
        }
        if(contentJson!=null&&contentJson.size()>0){
            for (int i=0;i<contentJson.size();i++){
                JSONObject content = contentJson.getJSONObject(i);
                Integer dataId = content.getInteger("id");
                JSONObject dataVal = content.getJSONObject("contentValue");
                JSONObject resJson = content.getJSONObject("resJson");
                CollectContent rs = JSONObject.parseObject(dataVal.toJSONString(),CollectContent.class);
                System.out.println(dataId+" ->"+dataVal);
                /**采集图片资源*/
                Map<String,String>  map = (Map)resJson;
                if(StringUtils.isNotBlank(rs.getTitle())){
                    /**快速采集定义的txt*/
                    String txt = dataVal.getString("contxt");
                    /**正文有图片则下载替换*/
                    if(StringUtils.isNotBlank(txt)&&map!=null&&map.size()>0){
                        for(Map.Entry<String,String> entry : map.entrySet()){
                            try {
                                /**使用全路径替换正文的图片*/
                                String uploadResultFileUrl = uploadFileService.uploadFileByUrl(entry.getValue(),site,false);
                                txt = txt.replaceAll(entry.getKey(),uploadResultFileUrl);
                            } catch (Exception e) {
                            }
                        }
                        dataVal.put("contxt",txt);
                        rs.setContxt(txt);
                    }
                    /**微博图片作为一个多图字段上传*/
                    JSONArray res = dataVal.getJSONArray("resource");
                    List<Integer>resIds =new ArrayList<>();
                    /**图片集合下载*/
                    if(res!=null&&res.size()>0){
                        for(Integer j=0;j<res.size();j++){
                            String rUrl = res.getString(j);
                            resIds.add(uploadFileService.uploadFileByUrlForId(rUrl,site));
                        }
                    }

                    String singleRes = dataVal.getString("singleRes");
                    Integer resId = null;
                    /**单图下载*/
                    if(StringUtils.isNotBlank(singleRes)){
                        resId = uploadFileService.uploadFileByUrlForId(singleRes,site);
                    }
                    /**处理标题过长*/
                    if(rs.getTitle().length()>150){
                        rs.setTitle(rs.getTitle().substring(0,150));
                    }
                    /**处理自定义采集的数据*/
                    Map<String,String>dataMap = JSONObject.parseObject(dataVal.toJSONString(),Map.class);
                    List<String> otherKey = dataMap.keySet().stream()
                            .filter(oldPrivilege ->! CollectContent.getDefKeys().contains(oldPrivilege)).collect(Collectors.toList());
                    Map<String,String>otherDataMap = new HashMap<>();
                    for(String key:otherKey){
                        /**富文本字段前缀*/
                        String txtPrefix = "txt_";
                        String data = dataMap.get(key);
                        if(key.startsWith(txtPrefix)){
                            /**正文有图片则下载替换*/
                            if(StringUtils.isNotBlank(data)&&map!=null&&map.size()>0){
                                for(Map.Entry<String,String> entry : map.entrySet()){
                                    try {
                                        /**使用全路径替换正文的图片*/
                                        String uploadResultFileUrl = uploadFileService.uploadFileByUrl(entry.getValue(),site,false);
                                        data = data.replaceAll(entry.getKey(),uploadResultFileUrl);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            /**去除前缀*/
                            otherDataMap.put(key.substring(txtPrefix.length()),data);
                        }else{
                            otherDataMap.put(key,data);
                        }
                    }
                    ContentSaveDto dto = ContentSaveDto.buildForCollect(rs,channelId,model,userId,
                            defTxtModelField,defPicModelField,resIds,resId,otherDataMap);
                    /**存储入库*/
                    try {
                        Content c =contentService.save(dto,site);
                    }catch (GlobalException e){
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
                toDelIds.add(dataId);
            }
        }
        /**删除无用数据*/
        if(toDelIds!=null&&toDelIds.size()>0){
            JSONObject delParam =new JSONObject();
            Integer[] ids = new Integer[toDelIds.size()];
            toDelIds.toArray(ids);
            delParam.put("ids",ids);
            try {
                HttpClientUtil.sendByDeleteMethod(serverDelUrl,delParam,headers);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private  CollectResult doConsumeByIds(CmsSite site,Integer userId,Integer taskId,Integer[]ids,
                                           String appId,String identity){
        String serverUrl = SERVER_URL_BASE + "/collect/data/results/listIds";
        String serverTaskGetUrl = SERVER_URL_BASE + "/collectTasks/"+taskId;
        Map<String,Object> params = new HashMap<>();
        params.put("ids",ids);
        params.put("taksId",taskId);
        Map<String,String>headers =new HashMap<String,String>();
        headers.put("identity",identity);
        headers.put("appId",appId);
        Integer channelId = null;
        Integer modelId = null;
        JSONArray contentJson = null;
        try {
            /**获取任务详情*/
            String result = HttpClientUtil.get(serverTaskGetUrl,headers);
            JSONObject json = JSONObject.parseObject(result);
            if(200==json.getInteger("code")){
                JSONObject data = json.getJSONObject("data");
                channelId  = data.getInteger("channelId");
                modelId  = data.getInteger("modelId");
                result = HttpClientUtil.postJson(serverUrl,params,headers);
                JSONObject serverRes = JSONObject.parseObject(result);
                /**获取任务具体数据*/
                if(serverRes!=null&&serverRes.get("code").equals(200)){
                    JSONObject dataJson = serverRes.getJSONObject("data");
                    contentJson = dataJson.getJSONArray("contents");
                    List<Integer>toDelIds =  new ArrayList<Integer>();
                    if(contentJson!=null&&contentJson.size()>0){
                        doSaveContent(identity,appId,contentJson,site,channelId,modelId,userId);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new CollectResult(false,contentJson);
    }

    @Autowired
    private ChastityUtil idService;
    @Autowired
    private CmsModelService cmsModelService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private ChannelService channelService;

    // http://192.168.0.173:9000/MODULE-COLLECT  ChastityUtil.SERVER_BASE
    private final static  String SERVER_URL_BASE = ChastityUtil.SERVER_BASE;

}
