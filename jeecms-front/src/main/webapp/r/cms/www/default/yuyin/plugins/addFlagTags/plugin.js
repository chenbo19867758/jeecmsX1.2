//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 添加识别标签插件 v1.0
//说明：为工具条提供"添加识别标签"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["addFlagTags"] = {
	mainMethod:function(){
		this.getTextNode(ABT.dom.container);
		this.AddTag();
		if(!ABT.dom.error){ABT.dom.allFlag = ABT.API.gc("abtFlag",ABT.dom.container);}
	},
	allTextNode:new Array(),
	getTextNode:function(element){
		var childNodes = element.childNodes;
		for (var i=0;i<childNodes.length;i++) {
			var thisChild = childNodes[i];
			switch(thisChild.nodeType){
				case 1:
					this.getTextNode(thisChild);
					break;
				case 3:
					if(this.trim(thisChild.nodeValue).length == 0){break;}
					if(thisChild.nodeValue!=""){this.allTextNode.push(thisChild);}
					
					break;
			}
			if(thisChild.nodeName=="IMG"||thisChild.nodeName=="INPUT"||thisChild.nodeName=="OBJECT"||thisChild.nodeName=="SELECT"){this.allTextNode.push(thisChild);}
		}
	},
	AddTag:function(){
		if(this.firstRun){return;}
		for(var a=0;a<this.allTextNode.length;a++){
			var tagNode = document.createElement("em");
			if(this.allTextNode[a].nodeName=="IMG"||this.allTextNode[a].nodeName=="INPUT"||this.allTextNode[a].nodeName=="SELECT"){
				tagNode.setAttribute("class","abtFlag");
				var newNode = this.allTextNode[a].cloneNode(true);
				tagNode.appendChild(newNode);
			}
			else if(this.allTextNode[a].nodeName=="OBJECT"&&this.allTextNode[a].parentNode.nodeName!="OBJECT"){
				tagNode.setAttribute("class","abtFlag");
				var newNode = this.allTextNode[a].cloneNode(true);
				tagNode.appendChild(newNode);
			}
			else{
				var newString = this.allTextNode[a].nodeValue;
				var reg = /[，。！？；、：]/;
				if(reg.exec(newString)==null){
					tagNode.setAttribute("class","abtFlag");
					tagNode.innerHTML = newString;
				}
				else{
					tagNode.setAttribute("class","abtFlagGroup");
					tagNode.innerHTML = this.mySplit(newString,/[，。！？；、：]/);
				}
			}
			if(this.allTextNode[a].parentNode){this.allTextNode[a].parentNode.insertBefore(tagNode,this.allTextNode[a]);}
		}
		for(var b=0;b<this.allTextNode.length;b++){
			this.allTextNode[b].parentNode.removeChild(this.allTextNode[b]);
		}
		var allOption = ABT.dom.container.getElementsByTagName("option");
		for(var c=0;c<allOption.length;c++){
			var thisMessage = allOption[c].firstChild.cloneNode(true);
			allOption[c].innerHTML = "";
			allOption[c].appendChild(thisMessage);
		}
		this.firstRun = true;
	},
	trim:function(str){return str.replace(/(^\s*)|(\s*$)/g,"");},
	mySplit:function(str,reg){
		var result,x=str,y,zzz=true;
		var stringArray = new Array();
		do{
			result = reg.exec(x);
			if(result!=null){
				var stringIndex = result.index;
				stringArray.push(x.substring(0,stringIndex+1));
				x = x.substring(stringIndex+1);
			}
			else{
				stringArray.push(x)
				zzz = false;
			}
		}
		while(zzz)
		var yy = "<em class=\"abtFlag\">";
		for(var a=0;a<stringArray.length;a++){
			yy += (a<stringArray.length-1)?(stringArray[a]+"</em><em class=\"abtFlag\">"):(stringArray[a]);
		}
		yy += "</em>";
		return yy;
	},
	getText:function(flagNode){
		var textMessage = "";
		if(flagNode.firstChild.nodeName=="IMG"){
			if(flagNode.parentNode.parentNode.nodeName=="A"||flagNode.parentNode.nodeName=="A"){
				textMessage = "图片链接："+ABT.API.getText(flagNode.firstChild);
			}
			else{textMessage = "图片："+ABT.API.getText(flagNode.firstChild);}
		}
		else if(flagNode.firstChild.nodeName=="OBJECT"){textMessage = "媒体："+flagNode.firstChild.getAttribute("title");}
		else if(flagNode.firstChild.nodeName=="SELECT"){textMessage = "下拉菜单";}
		else if(flagNode.firstChild.nodeName=="INPUT"){
			var inputType = flagNode.firstChild.getAttribute("type");
			switch(inputType){
				case "button":
					textMessage = "表单按钮："+flagNode.firstChild.getAttribute("value");
					break;
				case "image":
					textMessage = "图形按钮："+flagNode.firstChild.getAttribute("alt");
					break;
				case "submit":
					textMessage = "提交按钮："+flagNode.firstChild.getAttribute("value");
					break;
				case "reset":
					textMessage = "重置按钮："+flagNode.firstChild.getAttribute("value");
					break;
				case "file":
					textMessage = "文件域："+flagNode.firstChild.getAttribute("title");
					break;
				case "password":
					textMessage = "密码域："+flagNode.firstChild.getAttribute("title");
					break;
				case "radio":
					textMessage = "单选框："+flagNode.firstChild.getAttribute("title");
					break;
				case "checkbox":
					textMessage = "复选框："+flagNode.firstChild.getAttribute("title");
					break;
				case "text":
					textMessage = "文本域："+flagNode.firstChild.getAttribute("title");
					break;
			}
		}
		else if(flagNode.parentNode.parentNode.nodeName=="A"||flagNode.parentNode.nodeName=="A"){
			var flagNodeContent;
			if(flagNode.parentNode.parentNode.nodeName=="A"){
				if(flagNode.parentNode.parentNode.getAttribute("title")){flagNodeContent = flagNode.parentNode.parentNode.getAttribute("title");}
				else{flagNodeContent = flagNode.innerText||flagNode.textContent;}
			}
			else if(flagNode.parentNode.nodeName=="A"){
				if(flagNode.parentNode.getAttribute("title")){thisContent = flagNode.parentNode.getAttribute("title");}
				else{thisContent = flagNode.innerText||flagNode.textContent;}
			}
			textMessage = "链接："+thisContent;
		}
		else if(flagNode.parentNode.nodeName=="H1"||flagNode.parentNode.nodeName=="H2"||flagNode.parentNode.nodeName=="H3"||flagNode.parentNode.nodeName=="H4"||flagNode.parentNode.nodeName=="H5"||flagNode.parentNode.nodeName=="H6"){
			var thisContent = flagNode.innerText||flagNode.textContent;
			textMessage = "标题："+thisContent;
		}
		else{
			var thisContent = flagNode.innerText||flagNode.textContent;
			textMessage = ""+thisContent;
		}
		return textMessage;
	}
}