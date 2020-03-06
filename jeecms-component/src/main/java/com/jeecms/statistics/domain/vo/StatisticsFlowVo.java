package com.jeecms.statistics.domain.vo;

import java.util.List;

public class StatisticsFlowVo {
	
	private List<StatisticsFlowListVo> vos;
	
	private StatisticsFlowImageVo imageVo;

	public StatisticsFlowVo(List<StatisticsFlowListVo> vos, StatisticsFlowImageVo imageVo) {
		super();
		this.vos = vos;
		this.imageVo = imageVo;
	}

	public List<StatisticsFlowListVo> getVos() {
		return vos;
	}

	public void setVos(List<StatisticsFlowListVo> vos) {
		this.vos = vos;
	}

	public StatisticsFlowImageVo getImageVo() {
		return imageVo;
	}

	public void setImageVo(StatisticsFlowImageVo imageVo) {
		this.imageVo = imageVo;
	}
	
}
