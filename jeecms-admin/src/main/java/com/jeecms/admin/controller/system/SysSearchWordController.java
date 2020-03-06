/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.SortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.system.domain.SysSearchWord;
import com.jeecms.system.service.SysSearchWordService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 搜索词Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-27
 */
@RequestMapping("/searchWords")
@RestController
public class SysSearchWordController extends BaseAdminController<SysSearchWord, Integer> {

        @Autowired
        private SysSearchWordService service;

        @PostConstruct
        public void init() {
                String[] queryParams = {};
                super.setQueryParams(queryParams);
        }


        /**
         * @Title: 列表分页
         * @Description: TODO
         * @param: @param request
         * @param: @param pageable
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @GetMapping(value = "/page")
        @SerializeField(clazz = SysSearchWord.class, includes = {"id", "word", "isRecommend", "searchCount", "sortNum"})
        public ResponseInfo page(HttpServletRequest request, @PageableDefault(sort = "searchCount", direction =
                Direction.ASC) Pageable pageable) throws GlobalException {
                Map<String, String[]> params = new HashMap<String, String[]>(3);
                String word = request.getParameter("word");
                String isRecommend = request.getParameter("isRecommend");
                Integer siteId = SystemContextUtils.getSiteId(request);
                if (SystemContextUtils.getSiteId(request) != null) {
                        params.put("EQ_siteId_Integer", new String[]{siteId.toString()});
                }
                params.put("EQ_isRecommend_Boolean", new String[]{isRecommend});
                params.put("LIKE_word_String", new String[]{word});
                Sort sort = pageable.getSort().and(new Sort(Sort.Direction.DESC, "id"));
                Pageable page = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
                return new ResponseInfo(service.getPage(params, page, false));
        }

        /**
         * @Title: 获取详情
         * @Description: TODO
         * @param: @param id
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @GetMapping(value = "/{id}")
        @SerializeField(clazz = SysSearchWord.class, includes = {"id", "word", "isRecommend", "searchCount", "sortNum"})
        @Override
        public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
                return super.get(id);
        }

        /**
         * @Title: 添加
         * @Description: TODO
         * @param: @param result
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @PostMapping()
        public ResponseInfo save(@RequestBody @Valid SysSearchWord sysSearchWord,
                                 HttpServletRequest request, BindingResult result) throws GlobalException {
                service.saveBatch(sysSearchWord, SystemContextUtils.getSiteId(request));
                return new ResponseInfo();
        }


        /**
         * @Title: 修改
         * @Description: TODO
         * @param: @param result
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @PutMapping()
        public ResponseInfo update(@RequestBody @Valid SysSearchWord sysSearchWord, BindingResult result,
                                   HttpServletRequest request) throws GlobalException {
                validateId(sysSearchWord.getId());
                if (!service.checkWord(sysSearchWord.getWord(), sysSearchWord.getId(),
                        SystemContextUtils.getSiteId(request))) {
                        return new ResponseInfo(SysOtherErrorCodeEnum.SEARCH_WORD_ALREADY_EXIST.getCode(),
                                SysOtherErrorCodeEnum.SEARCH_WORD_ALREADY_EXIST.getDefaultMessage());
                }
                //获取汉字的首字母
                String letter = ChineseCharToEn.getAllFirstLetter(sysSearchWord.getWord());
                sysSearchWord.setIniChinese(letter);
                return super.update(sysSearchWord, result);
        }

        /**
         * 校验搜索词是否重复
         *
         * @param word    搜索词
         * @param id      搜索词id
         * @param request HttpServletRequest
         * @return ResponseInfo
         */
        @GetMapping("/unique/word")
        public ResponseInfo check(String word, Integer id, HttpServletRequest request) {
                return new ResponseInfo(service.checkWord(word, id, SystemContextUtils.getSiteId(request)));
        }

        /**
         * @Title: 排序
         * @Description: TODO
         * @param: @param sorts
         * @param: @return
         * @return: ResponseInfo
         */
        @PutMapping(value = "/sort")
        @Override
        public ResponseInfo sort(@RequestBody @Valid SortDto sorts, BindingResult result) throws GlobalException {
                super.validateBindingResult(result);
                return super.sort(sorts, result);
        }

        /**
         * @Title: 删除
         * @Description: TODO
         * @param: @param ids
         * @param: @return
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @DeleteMapping()
        public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
                return super.physicalDelete(dels.getIds());
        }
}



