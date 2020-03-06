//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 辅助线插件 v1.0
//说明：为工具条提供"辅助线"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["guides"] = {
	memoryMethod:function(){
		ABT.plugins["guides"].mainMethod();
	},
	guidesState:false,
	guidesWidth:4,
	guidesColor:"#F00",
	guidesSkew:5,
	mainMethod:function(){
		if(!this.guidesState){
			var newGuidesBox = document.createElement("div");
			newGuidesBox.setAttribute("id","guidesbox")
			newGuidesBox.innerHTML = "<div id=\"guidesXLine\"></div><div id=\"guidesYLine\"></div>";
			document.body.insertBefore(newGuidesBox,ABT.dom.container);
			ABT.API.gi("guidesYLine").style.cssText = "position:absolute;top:0px;left:0px;z-index:8999;font-size:0px;line-height:0px;width:"+this.guidesWidth+"px;height:"+document.documentElement.clientHeight+"px;background-color:"+this.guidesColor;
			ABT.API.gi("guidesXLine").style.cssText = "position:absolute;top:0px;left:0px;z-index:8998;font-size:0px;line-height:0px;width:100%;height:"+this.guidesWidth+"px;background-color:"+this.guidesColor;
			document.onmousemove = this.moveGuides;
			window.onscroll = this.moveGuides;
			this.guidesState = true;
		}
		else{
			ABT.API.gi("guidesbox").parentNode.removeChild(ABT.API.gi("guidesbox"));
			this.guidesState = false;
		}
		ABT.API.cookie.set(ABT.config.plugins["guides"].ms,ABT.plugins["guides"].guidesState?1:0);
	},
	moveGuides:function(e){
		if(!ABT.API.gi("guidesbox")){return;}
		e = window.event?window.event:e;
		var guidesX,guidesY,skew=Number(ABT.plugins["guides"].guidesSkew);
		if(ABT.info.browser.ie){
			guidesX = e.clientX+skew;
			guidesY = e.clientY+(document.documentElement.scrollTop||document.body.scrollTop)+skew;
		}
		else{
			ABT.API.gi("guidesXLine").style.position = "fixed";
			guidesX = e.pageX+skew;
			guidesY = e.pageY-(document.documentElement.scrollTop||document.body.scrollTop)+skew;
		}
		ABT.plugins["guides"].correctGuides();
		ABT.API.gi("guidesYLine").style.left = guidesX+"px";
		ABT.API.gi("guidesXLine").style.top = guidesY+"px";
	},
	correctGuides:function(){
		var pageHeight = document.body.offsetHeight<document.documentElement.clientHeight?document.documentElement.clientHeight:document.body.offsetHeight;
		ABT.API.gi("guidesYLine").style.height = pageHeight+"px";
	}
}