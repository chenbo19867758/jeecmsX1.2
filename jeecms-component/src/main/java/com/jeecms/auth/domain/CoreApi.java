package com.jeecms.auth.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.ImmutableMap;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * Api主体信息
 * 
 * @author: chenming
 * @date: 2019年4月9日 下午3:39:14
 */
@Entity
@Table(name = "jc_sys_api")
public class CoreApi extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;

        public static final Map<String, Short> METHODS = ImmutableMap.of("GET", (short)1, "POST", (short)2, "DELETE",(short)3,
                        "UPDATE",(short) 4, "PUT", (short)5);

        private Integer id;
        /** API名称 */
        private String apiName;
        /** api权限标识 */
        private String perms;
        /** api地址 */
        private String apiUrl;
        /** 请求方式 1- get 2-post 3-update 4-put 5-delete */
        private Short requestMethod;
        /** 使用场景 */
        private String useScene;
        /** 排序值 */
        private Integer sortNum;

        /** 菜单列表 */
        private List<CoreMenu> menus;

        public CoreApi() {
        }

        /**
         * 对应的http方法字符串
         * @Title: getMethod
         * @param method api method字段值
         * @return: String
         */
        public static String getMethod(Short method) {
                for (Entry<String, Short> entry : METHODS.entrySet()) {
                        if (entry.getValue().equals(method)) {
                                return entry.getKey();
                        }
                }
                return METHODS.keySet().iterator().next();
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_api", pkColumnValue = "jc_sys_api", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_api")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        @Length(max = 150)
        @Column(name = "api_name", nullable = false, length = 150)
        public String getApiName() {
                return this.apiName;
        }

        public void setApiName(String apiName) {
                this.apiName = apiName;
        }

        @Column(name = "api_per_mark")
        @Length(max = 150)
        public String getPerms() {
                return this.perms;
        }

        public void setPerms(String perms) {
                this.perms = perms;
        }

        @NotBlank
        @Length(max = 150)
        @Column(name = "api_url", nullable = false, length = 150)
        public String getApiUrl() {
                return this.apiUrl;
        }

        public void setApiUrl(String apiUrl) {
                this.apiUrl = apiUrl;
        }

        @NotNull
        @Digits(integer = 6, fraction = 0)
        @Column(name = "request_method", nullable = false)
        public Short getRequestMethod() {
                return this.requestMethod;
        }

        public void setRequestMethod(Short requestMethod) {
                this.requestMethod = requestMethod;
        }

        @Column(name = "use_scene")
        @Length(max = 255)
        public String getUseScene() {
                return this.useScene;
        }

        public void setUseScene(String useScene) {
                this.useScene = useScene;
        }

        @Digits(integer = 11, fraction = 0)
        @Column(name = "sort_num", length = 11)
        public Integer getSortNum() {
                return sortNum;
        }

        public void setSortNum(Integer sortNum) {
                this.sortNum = sortNum;
        }

        @ManyToMany
        @JoinTable(name = "jc_tr_menu_api", joinColumns = @JoinColumn(name = "api_body_information_id") , inverseJoinColumns = @JoinColumn(name = "menu_management_id") )
        public List<CoreMenu> getMenus() {
                return menus;
        }

        public void setMenus(List<CoreMenu> menus) {
                this.menus = menus;
        }

}