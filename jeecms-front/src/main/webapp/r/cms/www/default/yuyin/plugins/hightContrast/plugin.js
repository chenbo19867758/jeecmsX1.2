//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 区域跳转插件 v1.0
//说明：为工具条提供"区域跳转"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["hightContrast"] = {
	delimit:new Array(
		"#FFF|#000|hcbw",
		"#FFF|#93F|hcwp",
		"#000|#09F|hcbb",
		"#F00|#FFF|hcrw"
	),
	memoryMethod:function(){ABT.plugins["hightContrast"].mainMethod(Number(ABT.API.cookie.get(ABT.config.plugins["hightContrast"].ms)));},
	mainMethod:function(colorNum){
		colorNum = Number(colorNum);
		if(!colorNum){colorNum=0;}
		if(colorNum!=0){
			var qcolor = ABT.plugins["hightContrast"].delimit[colorNum-1].split("|")[0];
			var bcolor = ABT.plugins["hightContrast"].delimit[colorNum-1].split("|")[1];
			ABT.plugins["hightContrast"].contrastControl(ABT.dom.container,1,qcolor,bcolor);
			for(var b=0;b<ABT.dom.allElements.length;b++){ABT.plugins["hightContrast"].contrastControl(ABT.dom.allElements[b],1,qcolor,bcolor);}
			for(var c=0;c<ABT.dom.allFrame.length;c++){
				var iframeDOM = ABT.dom.allFrame[c].contentWindow;
				try{
					var iframeBody = iframeDOM.document.getElementsByTagName("body")[0];
					ABT.plugins["hightContrast"].contrastControl(iframeBody,1,qcolor,bcolor);
					for(var d=0;d<iframeBody.getElementsByTagName("*").length;d++){
						ABT.plugins["hightContrast"].contrastControl(iframeBody.getElementsByTagName("*")[d],1,qcolor,bcolor);
					}
				}catch(e){}
			}
		}
		else{
			ABT.plugins["hightContrast"].contrastControl(ABT.dom.container,0);
			for(var b=0;b<ABT.dom.allElements.length;b++){ABT.plugins["hightContrast"].contrastControl(ABT.dom.allElements[b],0);}
			for(var c=0;c<ABT.dom.allFrame.length;c++){
				var iframeDOM = ABT.dom.allFrame[c].contentWindow;
				try{
					var iframeBody = iframeDOM.document.getElementsByTagName("body")[0];
					ABT.plugins["hightContrast"].contrastControl(iframeBody,0);
					for(var d=0;d<iframeBody.getElementsByTagName("*").length;d++){
						ABT.plugins["hightContrast"].contrastControl(iframeBody.getElementsByTagName("*")[d],0);
					}
				}catch(e){}
			}
		}
		var buttonNodeId = "";
		if(colorNum==0){buttonNodeId = "hcdefault";}
		else{buttonNodeId = this.delimit[colorNum-1].split("|")[2];}
		for(var y=0;y<this.delimit.length;y++){ABT.API.cc(ABT.plugins["hightContrast"].delimit[y].split("|")[2],"");}
		ABT.API.cc("hcdefault","");
		ABT.API.cc(buttonNodeId,"hconchange");
		ABT.API.cookie.set(ABT.config.plugins["hightContrast"].ms,colorNum);
	},
	contrastControl:function(element,mode,q,b){
		if(mode==0){
			element.style.backgroundColor = "";
			element.style.backgroundImage = "";
			element.style.color = "";
		}
		else if(mode==1){
			element.style.backgroundColor = b;
			element.style.backgroundImage = "none";
			element.style.color = q;
		}
	}
}