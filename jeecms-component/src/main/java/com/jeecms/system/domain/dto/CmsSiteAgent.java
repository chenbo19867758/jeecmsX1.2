/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import com.jeecms.common.util.HibernateProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.jeecms.auth.domain.vo.SortMenuVO;
import com.jeecms.channel.domain.Channel;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;

/**
 * 站点增强类
 * 
 * @author: tom
 * @date: 2019年8月30日 下午5:36:34
 */
public class CmsSiteAgent implements Serializable {
        static Logger logger = LoggerFactory.getLogger(CmsSiteAgent.class);
        private static final long serialVersionUID = 8359912924427975500L;

        private CmsSite site;

        public CmsSiteAgent(CmsSite site) {
                super();
                this.site = site;
        }

        /**
         * @return the site
         */
        public CmsSite getSite() {
                return site;
        }

        /**
         * @param site
         *                the site to set
         */
        public void setSite(CmsSite site) {
                this.site = site;
        }

        public CmsSiteAgent() {
                super();
        }

        /** 按照站点ID排序 **/
        public static List<CmsSite> sortByIdAndChild(List<CmsSite> sites) {
                if (sites != null && sites.size() > 0) {
                        sites = sites.stream().sorted(Comparator.comparing(CmsSite::getId))
                                        .collect(Collectors.toList());
                        for (CmsSite org : sites) {
                                if (org.getChildren() != null && org.getChildren().size() > 0) {
                                        org.setChildren(sortByIdAndChild(org.getChildren()));
                                }
                        }
                }
                return sites;
        }

        /** 按照站点ID排序 **/
        public static List<CmsSite> sortByIdAndChild(Set<CmsSite> sites) {
                List<CmsSite> sortSites = new ArrayList<CmsSite>();
                if (sites != null && sites.size() > 0) {
                        sortSites = sites.stream().sorted(Comparator.comparing(CmsSite::getId))
                                        .collect(Collectors.toList());
                        for (CmsSite site : sites) {
                                if (site.getChildren() != null && site.getChildren().size() > 0) {
                                        site.setChildren(sortByIdAndChild(site.getChildren()));
                                }
                        }
                }
                return sortSites;
        }

        /** 得到站点ID集合 **/
        public static List<Integer> sortIds(Set<Integer> ids) {
                List<Integer> idList = new ArrayList<Integer>();
                if (ids != null && ids.size() > 0) {
                        idList = ids.stream().sorted().collect(Collectors.toList());
                }
                return idList;
        }

        /** 按照站点排序值，站点创建时间排序 **/
        public static List<CmsSite> sortBySortNumAndChild(List<CmsSite> sites) {
                if (sites != null && sites.size() > 0) {
                        sites = sites.stream()
                                        .sorted(Comparator.comparing(CmsSite::getSortNum)
                                                        .reversed().thenComparing(Comparator
                                                                        .comparing(CmsSite::getCreateTime).reversed()))
                                        .collect(Collectors.toList());
                        for (CmsSite s : sites) {
                                if (s.getChildren() != null && s.getChildren().size() > 0) {
                                        List<CmsSite> childrens = s.getChildren();
                                        childrens = childrens.stream().sorted(Comparator.comparing(CmsSite::getSortNum)
                                                        .reversed().thenComparing(Comparator
                                                                        .comparing(CmsSite::getCreateTime).reversed()))
                                                        .collect(Collectors.toList());
                                        s.setChildren(childrens);
                                }
                        }
                }
                return sites;
        }
        
        public static void initSiteChild(CopyOnWriteArraySet<CmsSite> set ){
                for (CmsSite s : set) {
                        for (CmsSite site : s.getChilds()) {
                                HibernateProxyUtil.loadHibernateProxy(site.getCfg());
                                HibernateProxyUtil.loadHibernateProxy(site);
                                logger.debug("loadSite Children->" + site.getId());
                        }
                }
        }
}
