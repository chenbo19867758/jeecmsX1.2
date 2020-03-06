//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 文字提示插件 v1.0
//说明：为工具条提供"文字提示"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["textTips"] = {
	textTipsState:false,
	pinyinState:false,
	textbgState:false,
	mainMethod:function(){
		if(!this.textTipsState){
			var newMessageBox = document.createElement("div");
			newMessageBox.setAttribute("id","gettextmessagebox");
			newMessageBox.innerHTML = "<div id=\"closetextmessagebox\"><a href=\"javascript:\" title=\"关闭提示栏\" onclick=\"ABT.API.plugin.call('textTips')\"></a></div><div id=\"gettextmessagecontent\"></div>";
			document.body.insertBefore(newMessageBox,ABT.dom.container);
			document.body.style.paddingBottom = 140+"px";
			ABT.API.gi("textTips1").disabled = false;
			ABT.API.gi("textTips3").disabled = false;
			
			for(var a=0;a<ABT.dom.allFlag.length;a++){
				ABT.API.eventOperate.add(ABT.dom.allFlag[a],"mouseover",ABT.plugins["textTips"].mouseEvent);
				ABT.API.eventOperate.add(ABT.dom.allFlag[a],"mouseout",ABT.plugins["textTips"].clearTextbg);
			}
			for(var b=0;b<ABT.dom.allElements.length;b++){
				var nodeName = ABT.dom.allElements[b].nodeName;
				if(nodeName=="A"||nodeName=="INPUT"||nodeName=="SELECT"||nodeName=="OBJECT"){
					ABT.API.eventOperate.add(ABT.dom.allElements[b],"focus",ABT.plugins["textTips"].mouseEvent);
					ABT.API.eventOperate.add(ABT.dom.allElements[b],"blur",ABT.plugins["textTips"].clearTextbg);
				}
			}
			
			this.textTipsState = true;
		}
		else{
			ABT.API.gi("textTips1").disabled = true;
			ABT.API.gi("textTips3").disabled = true;
			document.body.removeChild(document.getElementById("gettextmessagebox"));
			document.body.style.paddingBottom = 0+"px";
			this.textTipsState = false;
		}
		ABT.API.cookie.set(ABT.config.plugins["textTips"].ms,ABT.plugins["textTips"].textTipsState?1:0);
	},
	mouseEvent:function(){
		if(!ABT.API.gi("gettextmessagecontent")){return;}
		var newText = ""
		if(this.className=="abtFlag"||this.getAttribute("class")=="abtFlag"){newText = ABT.plugins["addFlagTags"].getText(this);}
		else{newText = ABT.API.getText(this);}
		
		if(ABT.plugins["textTips"].pinyinState){newText = ABT.plugins["textTips"].pinyinText(newText);}
		ABT.API.gi("gettextmessagecontent").innerHTML = newText;
		if(ABT.plugins["textTips"].textbgState){
			this.style.backgroundColor = "#F00";
			this.style.color = "#FFF";
		}
	},
	pinyinText:function(text){
		var messayArray = text.split("");
		var newString = "";
		for(var a=0;a<messayArray.length;a++){
			var testVar = "";
			if(pinyin[messayArray[a]]){testVar = pinyin[messayArray[a]];}
			else{testVar = "&nbsp;";}
			if(messayArray[a]==" "){messayArray[a]="&nbsp;";}
			newString += "<span>"+messayArray[a]+"<sup>"+testVar+"</sup></span>";
		}
		return newString;
	},
	clearTextbg:function(){
		this.style.backgroundColor = "";
		this.style.color = "";
	},
	subMethod:function(branchNum){
		if(branchNum==1){
			ABT.plugins["textTips"].pinyinState = ABT.plugins["textTips"].pinyinState?false:true;
		}
		else if(branchNum==2){
			ABT.plugins["textTips"].textbgState = ABT.plugins["textTips"].textbgState?false:true;
		}
	},
	memoryMethod:function(){
		ABT.plugins["textTips"].mainMethod();
	}
}