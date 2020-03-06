//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 字体放大插件 v1.0
//说明：为工具条提供"字体放大缩小"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["fontZoom"] = {
	memoryMethod:function(){
		var cookieValue = ABT.API.cookie.get(ABT.config.plugins["fontZoom"].ms);
		if(cookieValue!=0){
			this.fontZoomState = cookieValue-this.onceZoom;
			this.mainMethod(1);
		}
	},
	onceZoom:2,
	zoomMax:50,
	zoomMin:12,
	fontZoomState:0,
	mainMethod:function(zoomMode){
		if(this.fontZoomState==0){this.fontZoomState=this.zoomMin;}
		if(zoomMode==1){
			this.fontZoomState += this.onceZoom;
			if(this.fontZoomState>this.zoomMax){this.fontZoomState=this.zoomMax;}
		}
		else if(zoomMode==0){
			this.fontZoomState -= this.onceZoom;
			if(this.fontZoomState<this.zoomMin){this.fontZoomState=0;}
		}
		for(var a=0;a<ABT.dom.allElements.length;a++){
			if(this.fontZoomState>this.zoomMin){
				ABT.dom.allElements[a].style.fontSize = this.fontZoomState+"px";
				ABT.dom.allElements[a].style.lineHeight = (this.fontZoomState+10)+"px";
			}
			else{
				ABT.dom.allElements[a].style.fontSize = null;
				ABT.dom.allElements[a].style.lineHeight = null;
			}
		}
		ABT.API.cookie.set(ABT.config.plugins["fontZoom"].ms,this.fontZoomState==0||this.fontZoomState==this.zoomMin?0:this.fontZoomState);
	}
}