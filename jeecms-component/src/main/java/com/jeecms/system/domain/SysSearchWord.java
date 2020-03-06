/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractSortDomain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
@Entity
@Table(name = "jc_sys_search_word")
@NamedQuery(name = "SysSearchWord.findAll", query = "SELECT s FROM SysSearchWord s")
public class SysSearchWord extends AbstractSortDomain<Integer> implements Serializable {
	public static final int ODER_BY_SORT_NUM_DESC = 1;
	public static final int ODER_BY_SORT_NUM_ASC = 3;
	public static final int ODER_BY_SEARCH_COUNT_DESC = 2;
	public static final int ODER_BY_SEARCH_COUNT_ASC = 4;
	public static final int SORT_DEF = 10;
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 搜索词
	 */
	private String word;
	/**
	 * 搜索词首字母
	 */
	private String iniChinese;
	/**
	 * 是否推荐
	 */
	private Boolean isRecommend;
	/**
	 * 搜索次数
	 */
	private Integer searchCount;

	/**
	 * 站点id
	 */
	private Integer siteId;

	/**
	 * 站点
	 */
	private CmsSite site;

	public SysSearchWord() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_search_word", pkColumnValue = "jc_sys_search_word", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_search_word")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotBlank
	@Column(name = "word", nullable = false, length = 50)
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Column(name = "ini_chinese", nullable = false, length = 50)
	public String getIniChinese() {
		return iniChinese;
	}

	public void setIniChinese(String iniChinese) {
		this.iniChinese = iniChinese;
	}

	@Column(name = "is_recommend", nullable = false, length = 1)
	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	@Column(name = "search_count", nullable = false, length = 11)
	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id", insertable = false, updatable = false)
	public CmsSite getSite() {
		return site;
	}

	public void setSite(CmsSite site) {
		this.site = site;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((iniChinese == null) ? 0 : iniChinese.hashCode());
		result = prime * result + ((isRecommend == null) ? 0 : isRecommend.hashCode());
		result = prime * result + ((searchCount == null) ? 0 : searchCount.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SysSearchWord other = (SysSearchWord) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (iniChinese == null) {
			if (other.iniChinese != null) {
				return false;
			}
		} else if (!iniChinese.equals(other.iniChinese)) {
			return false;
		}
		if (isRecommend == null) {
			if (other.isRecommend != null) {
				return false;
			}
		} else if (!isRecommend.equals(other.isRecommend)) {
			return false;
		}
		if (searchCount == null) {
			if (other.searchCount != null) {
				return false;
			}
		} else if (!searchCount.equals(other.searchCount)) {
			return false;
		}
		if (siteId == null) {
			if (other.siteId != null) {
				return false;
			}
		} else if (!siteId.equals(other.siteId)) {
			return false;
		}
		if (word == null) {
			if (other.word != null) {
				return false;
			}
		} else if (!word.equals(other.word)) {
			return false;
		}
		return true;
	}

}