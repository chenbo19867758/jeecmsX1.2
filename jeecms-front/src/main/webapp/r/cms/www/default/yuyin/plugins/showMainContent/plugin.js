//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 全屏正文插件 v1.0
//说明：为工具条提供"全屏正文"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["showMainContent"] = {
	state:false,
	mainMethod:function(){
		if(!ABT.plugins["showMainContent"].state){
			var mainElementContent;
			if(ABT.API.gi("articlecontent")){mainElementContent=ABT.API.gi("articlecontent").innerHTML;}
			else if(ABT.API.gi("maincontent")){mainElementContent=ABT.API.gi("maincontent").innerHTML;}
			else if(ABT.API.gi("content")){mainElementContent=ABT.API.gi("content").innerHTML;}
			else{mainElementContent="<h2>不能正确抓取正文内容</h2>";}
			
			var newShowBox = document.createElement("div");
			newShowBox.setAttribute("id","mainContentBox");
			newShowBox.className = "publicFullBox";
			newShowBox.style.height = document.documentElement.clientHeight-ABT.skin.UIHeight+"px";
			newShowBox.style.top = ABT.skin.UIHeight+"px";
			newShowBox.innerHTML = "<div id=\"closeMainContent\"><a href=\"javascript:ABT.API.plugin.call('showMainContent')\" title=\"关闭全屏正文\"></a></div><div id=\"ABTMainContent\">"+mainElementContent+"</div>";
			
			document.body.appendChild(newShowBox);
			var allContentNode = ABT.API.gi("ABTMainContent").getElementsByTagName("*");
			for(var a=0;a<allContentNode.length;a++){ABT.plugins["showMainContent"].clearContent(allContentNode[a]);}
			ABT.dom.container.style.display = "none";
			ABT.plugins["showMainContent"].state = true;
		}
		else{
			document.body.removeChild(ABT.API.gi("mainContentBox"));
			ABT.dom.container.style.display = "block";
			ABT.plugins["showMainContent"].state = false;
		}
	},
	clearContent:function(element){
		element.setAttribute("id","");
		element.className = "";
	}
}