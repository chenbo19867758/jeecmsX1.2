/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  
 package com.jeecms.auth.domain.dto;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeecms.auth.domain.vo.SortMenuVO;
import com.jeecms.system.domain.CmsOrg;

/**   
 * 组织增强类
 * @author: tom
 * @date:   2019年8月30日 下午5:36:34     
 */
public class CmsOrgAgent implements Serializable{

        private static final long serialVersionUID = 8359912924427975500L;
        
        private CmsOrg org;
        
        public CmsOrgAgent(CmsOrg org) {
                super();
                this.org = org;
        }

        public CmsOrgAgent() {
                super();
        }

        public static List<CmsOrg> sortListBySortAndChild(List<CmsOrg> orgs) {
                sortBySortAndChild(orgs);
                return orgs;
        }

        public static List<CmsOrg> sortBySortAndChild(List<CmsOrg> orgs) {
                if (orgs != null && orgs.size() > 0) {
                        orgs = orgs.stream().sorted(Comparator.comparing(CmsOrg::getId).reversed()
                                        ).collect(Collectors.toList());
                        for (CmsOrg org : orgs) {
                                if (org.getChildren() != null && org.getChildren().size() > 0) {
                                        org.setChildren(sortBySortAndChild(org.getChildren()));
                                }
                        }
                }
                return orgs;
        }

}
