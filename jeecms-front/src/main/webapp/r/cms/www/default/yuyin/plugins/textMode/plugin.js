//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 纯文本模式插件 v1.0
//说明：为工具条提供“纯文本模式”功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------
ABT.plugins["textMode"] = {
	textModeState:false,
	mainMethod:function(){
		if(!this.textModeState){
			ABT.dom.container.style.display = "none";
			for(var c=0;c<ABT.dom.allFrame.length;c++){
				var iframeDOM = ABT.dom.allFrame[c].contentWindow;
				var newFrameContainer = document.createElement("div");
				try{newFrameContainer.innerHTML = iframeDOM.document.body.innerHTML;}catch(z){}
				ABT.dom.allFrame[c].parentNode.insertBefore(newFrameContainer,ABT.dom.allFrame[c]);
			}
			while(ABT.dom.allFrame.length){ABT.dom.allFrame[0].parentNode.removeChild(ABT.dom.allFrame[0]);}
			for(var d=0;d<ABT.dom.allImg.length;d++){
				var newImgContainer = document.createElement("span");
				newImgContainer.innerHTML = ABT.API.getText(ABT.dom.allImg[d]);
				ABT.dom.allImg[d].parentNode.insertBefore(newImgContainer,ABT.dom.allImg[d]);
			}
			while(ABT.dom.allImg.length){ABT.dom.allImg[0].parentNode.removeChild(ABT.dom.allImg[0]);}
			var textModeStyleUrl = ABT.info.url+"plugins/textMode/textMode.css";
			for(var a=0;a<ABT.dom.allStyle.length;a++){
				if(ABT.dom.allStyle[a].getAttribute("id")!="ABTStyle"){
					ABT.dom.allStyle[a].setAttribute("href","#");
				}
			}
			var textModeStyle = document.createElement("link");
			textModeStyle.setAttribute("rel","stylesheet");
			textModeStyle.setAttribute("type","text/css");
			textModeStyle.setAttribute("href",textModeStyleUrl);
			document.getElementsByTagName("head")[0].appendChild(textModeStyle);
			
			for(var b=0;b<ABT.dom.allElements.length;b++){ABT.dom.allElements[b].style.height = null;};
			
			if(document.getElementById("textmodebutton")){document.getElementById("textmodebutton").setAttribute("value","切换回默认模式")};
			ABT.dom.container.style.display = "block";
			this.textModeState = true;
			ABT.API.cookie.set(ABT.config.plugins["textMode"].ms,this.textModeState?1:0);
		}
		else{
			this.textModeState = false;
			ABT.API.cookie.set(ABT.config.plugins["textMode"].ms,this.textModeState?1:0);
			window.location.href=window.location.href;
			window.location.reload();
		}
	},
	memoryMethod:function(){
		if(ABT.API.cookie.get(ABT.config.plugins["textMode"].ms)==1){
			ABT.plugins["textMode"].mainMethod();
		}
	}
}