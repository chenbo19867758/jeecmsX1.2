//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 区域跳转插件 v1.0
//说明：为工具条提供"区域跳转"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["pageZoom"] = {
	pageZoomState:1,
	onceZoom:0.2,
	zoomMax:2,
	zoomMin:1,
	mainMethod:function(zoomMode){
		if(zoomMode==1){
			this.pageZoomState = (this.pageZoomState*10+this.onceZoom*10)/10;
			if(this.pageZoomState>this.zoomMax){this.pageZoomState = this.zoomMax;}
		}
		else if(zoomMode==0){
			this.pageZoomState = (this.pageZoomState*10-this.onceZoom*10)/10;
			if(this.pageZoomState<this.zoomMin){this.pageZoomState = this.zoomMin;}
		}
		var containerWidth = ABT.dom.container.offsetWidth;
		var containerHeight = ABT.dom.offsetHeight;
		ABT.dom.container.style.display = "none";
		if(ABT.info.browser.ie){
			if(document.body.offsetWidth>containerWidth*this.pageZoomState){
				ABT.dom.container.style.cssText = "position:absolute;left:50%;margin:0px;";
				ABT.dom.container.style.marginLeft = 0-Math.round(containerWidth*this.pageZoomState/2)+"px";
			}
			else{ABT.dom.container.style.cssText = "position:absolute;left:0px;margin:0px;";}
			ABT.dom.container.style.zoom = this.pageZoomState;
		}
		else{
			var containerX = Math.round(((containerWidth*this.pageZoomState-containerWidth)/this.pageZoomState)/2);
			var containerY = Math.round(((containerHeight*this.pageZoomState-containerHeight)/this.pageZoomState)/2);
			var boxTranslate;
			if(containerWidth*this.pageZoomState>document.body.offsetWidth){
				var fuck = (containerWidth*this.pageZoomState-document.body.offsetWidth)/2;
				boxTranslate=Math.round(fuck/this.pageZoomState);
			}
			else{boxTranslate=0;}
			var hackCSS = "";
			if(ABT.info.browser.firefox){hackCSS = "-moz-";};
			if(ABT.info.browser.chrome||ABT.info.browser.safari){
				hackCSS = "-webkit-";
				document.body.style.height = Math.round(containerHeight*this.pageZoomState)+"px";
				var bodyWidth;
				var oldBodyWidth = document.body.offsetWidth;
				if(oldBodyWidth<containerWidth*this.pageZoomState&&zoomMode==1){
					bodyWidth = Math.round((containerWidth*this.pageZoomState-oldBodyWidth)/this.pageZoomState);
					document.body.style.width = Math.round(containerWidth*this.pageZoomState)+"px";
					boxTranslate -= bodyWidth/2;
				}
				if(zoomMode==0){
					if(screen.width>containerWidth*this.pageZoomState){document.body.style.width = null;}
					else{document.body.style.width = Math.round(containerWidth*this.pageZoomState)+"px";}
				}
			}
			if(ABT.info.browser.opera){hackCSS = "-o-";};
			ABT.dom.container.style.cssText = hackCSS+"transform:scale("+this.pageZoomState+") translate("+boxTranslate+"px,"+containerY+"px);";
		}
		if(this.pageZoomState==1){
			ABT.dom.container.style.cssText = null;
			document.body.style.width = null;
		}
		ABT.dom.container.style.display = "block";
		var cookieValue = this.pageZoomState==1?0:this.pageZoomState;
		ABT.API.cookie.set(ABT.config.plugins["pageZoom"].ms,cookieValue);
	},
	memoryMethod:function(){
		var cookieValue = ABT.API.cookie.get(ABT.config.plugins["pageZoom"].ms);
		if(cookieValue>1){
			cookieValue -= this.onceZoom;
			this.pageZoomState = cookieValue;
			this.mainMethod(1);
		}
	}
}