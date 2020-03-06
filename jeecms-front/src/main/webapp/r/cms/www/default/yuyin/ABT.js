//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) v4.01
//说明：ABT v4 正式版本
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

//----------------------------------------------------------------------
//******************************初始化部分******************************
//----------------------------------------------------------------------
var ABT = new Object();
//加载附属文件--------------------------------------
function loadABTFiles(){
	ABT.info = new Object();
	ABT.info.url = "";
	ABT.info.pageLoaded = false;
	ABT.info.speechState = false;
	var ABTFileUrl = document.getElementById("ABT").getAttribute("src");
	if(ABTFileUrl.indexOf("/")!=-1){ABT.info.url = ABTFileUrl.substring(0,ABTFileUrl.lastIndexOf("/")+1);}
	else{ABT.info.url = "";}
	document.writeln("<script type=\"text/javascript\" src=\""+ABT.info.url+"config.js\"></script>");
	document.writeln("<script type=\"text/javascript\" src=\""+ABT.info.url+"pinyin.js\"></script>");
	document.writeln("<script type=\"text/javascript\" src=\""+ABT.info.url+"swfobject.js\">swfobject.registerObject(\"flexApp\", \"9.0.0\", \"expressInstall.swf\");</script>");
}
loadABTFiles();
//页面加载完毕运行----------------------------------
if(window.addEventListener){window.addEventListener("load",function(){ABT.init();},false);}
else{window.attachEvent("onload",function(){ABT.init();});}
//键盘事件监听--------------------------------------
document.onkeydown = function keyListener(e){
	if(!ABT.info.pageLoaded){return;}
	var currkey=0,e=e||event;
	currkey=e.keyCode||e.which||e.charCode;
	ABT.keyboardListener(e.ctrlKey,e.altKey,e.shiftKey,currkey);
}
//语音插件初始化------------------------------------
/**function flexInit(){
	var allText = new Array();
	for(var a=0;a<ABT.dom.allFlag.length;a++){
		if(ABT.dom.allFlag[a].innerHTML!="undefined"&&ABT.dom.allFlag[a].innerHTML!=""){
			var tempVar = ABT.plugins["addFlagTags"].getText(ABT.dom.allFlag[a]);
			if(tempVar.indexOf("undefined")==-1&&tempVar!=""){
				allText.push(tempVar);
			}
		}
	}
	var obj = swfobject.getObjectById("flexApp");
	obj.dataCollection(allText);
	ABT.info.speechState = true;
}*/

//----------------------------------------------------------------------
//******************************工具条部分******************************
//----------------------------------------------------------------------

//#############################[工具条API]##############################
ABT.API = {
	//getElementById--------------------------
	gi:function(elementId){return document.getElementById(elementId);},
	//getElementsByClassName------------------
	gc:function(className,parentElement){
		if(!className){return false;}
		var allElement = (parentElement||document.body).getElementsByTagName("*");
		var result = new Array();
		for(var a=0;a<allElement.length;a++){
			if(allElement[a].className == className||allElement[a].getAttribute("class")==className){result.push(allElement[a]);}
		}
		return result;
	},
	//节点className修改-----------------------
	cc:function(elementId,className){
		if(ABT.API.gi(elementId)){ABT.API.gi(elementId).className = className;}
	},
	//节点事件操作----------------------------
	eventOperate:{
		add:function(elem,type,fn){
			if(elem.attachEvent){
				var typeRef = "_" + type;
				if(!elem[typeRef]){elem[typeRef] = [];}
				for(var i in elem[typeRef]){if(elem[typeRef][i] == fn){return;}}
				elem[typeRef].push(fn);
				elem["on"+type] = function(){for(var i in this[typeRef]){this[typeRef][i].apply(this,arguments);}}	
			}else{elem.addEventListener(type,fn,false);}
		},
		remove:function(elem,type,fn){
			if(elem.detachEvent){if(elem["_"+type]){for(var i in elem["_"+type]){if(elem["_"+type][i] == fn){elem["_"+type].splice(i,1);break;}}}}
			else{elem.removeEventListener(type,fn,false);}
		}
	},
	//Cookie操作------------------------------
	cookie:{
		set:function(cookieName,cookieValue){
			if(cookieName==undefined||cookieValue==undefined){return;}
			var Days = ABT.config.cookieTime;
			var exp = new Date(); 
			exp.setTime(exp.getTime() + Days*24*60*60*1000);
			document.cookie = cookieName+"="+ escape(cookieValue)+";expires="+exp.toGMTString()+";path=/";
		},
		get:function(cookieName){
			if(cookieName==undefined){return;}
			var arr = document.cookie.match(new RegExp("(^| )"+cookieName+"=([^;]*)(;|$)"));
			if(arr != null) return unescape(arr[2]); return null;
		}
	},
	//跳转节点操作----------------------------
	skipElement:{
		build:function(element,elementText){
			if(!element){return;}
			var shipElement = document.createElement("div");
			shipElement.setAttribute("class","skipElement");
			shipElement.style.cssText = "width:0px;height:0px;font-size:0px;line-height:0px;";
			shipElement.innerHTML = "<a href=\"javascript:\" onblur=\"ABT.API.skipElement.remove(this)\">"+elementText+"</a>";
			element.parentNode.insertBefore(shipElement,element);
			shipElement.firstChild.focus();
		},
		remove:function(skipElement){skipElement.parentNode.parentNode.removeChild(skipElement.parentNode);}
	},
	//键盘编码处理方法------------------------
	keyCodeStringHandle:function(keyString){
		var thisKeyString = "";
		thisKeyString += String(keyString.toUpperCase().indexOf("CTRL")>-1?1:0);
		thisKeyString += String(keyString.toUpperCase().indexOf("ALT")>-1?1:0);
		thisKeyString += String(keyString.toUpperCase().indexOf("SHIFT")>-1?1:0);
		thisKeyString += keyString.toUpperCase().substring(keyString.toUpperCase().lastIndexOf("+")+1);
		return thisKeyString;
	},
	//文本提取--------------------------------
	getText:function(element){
		var elementText;
		if(element.nodeName=="#text"){elementText=element.nodeValue;}
		else if(element.nodeName=="IMG"){
			if(element.getAttribute("alt")){elementText=element.getAttribute("alt");}
			else if(element.getAttribute("title")){elementText=element.getAttribute("title");}
			else{elementText=document.title;}
		}
		else if(element.nodeName=="A"){elementText = ABT.plugins["addFlagTags"].getText(element.firstChild);}
		else if(element.nodeName=="INPUT"||element.nodeName=="SELECT"||element.nodeName=="OBJECT"){
			elementText = ABT.plugins["addFlagTags"].getText(element.parentNode);
		}
		else{elementText=element.innerText||element.textContent;}
		return elementText;
	},
	//插件调用--------------------------------
	plugin:{
		loadedPlugins:new String(),
		call:function(pluginName,parameterString,methodName){
			if(!pluginName){return;}
			if(!parameterString&&parameterString!=0){parameterString = "";}
			if(!methodName){methodName = "mainMethod";}
			if(ABT.API.plugin.loadedPlugins.indexOf(pluginName)!=-1){
				ABT.API.plugin.execute(pluginName,parameterString,methodName);
				return;
			}
			var newPlugin = ABT.API.plugin.include(pluginName);
			if(!/*@cc_on!@*/0){
				newPlugin.onload = newPlugin.onerror = function(){ABT.API.plugin.execute(pluginName,parameterString,methodName);}
			}
			else{
				newPlugin.onreadystatechange = function(){
					if(newPlugin.readyState=="loaded"||newPlugin.readyState=="complete"){ABT.API.plugin.execute(pluginName,parameterString,methodName);}
				}
			}
		},
		include:function(pluginName){
			var pluginNode = document.createElement("script");
			pluginNode.setAttribute("type","text/javascript");
			//pluginNode.setAttribute("src",ABT.info.url+"plugins/"+pluginName+"/plugin.js");
			
			pluginNode.setAttribute("src",ABT.info.url+"plugins/"+pluginName+"/plugin.js");
			document.getElementsByTagName("head")[0].appendChild(pluginNode);
			ABT.API.plugin.loadedPlugins = ABT.API.plugin.loadedPlugins+""+pluginName+"|";
			return pluginNode;
		},
		execute:function(pluginName,parameterString,methodName){
			var markString = parameterString==""&&parameterString!="0"?"":"'";
			try{eval("ABT.plugins[\""+pluginName+"\"]."+methodName+"("+markString+parameterString+markString+")");}
			catch(e){alert("当调用"+pluginName+"插件时，发生如下错误:\n"+e+"\n错误代码："+"ABT.plugins[\""+pluginName+"\"]."+methodName+"("+markString+parameterString+markString+")");}
		}
	},
	//文本朗读--------------------------------
	speech:{
		textArray:new Array(),
		deaconCount:0,
		deaconMode:0,
		deaconText:function(text){
			if(!ABT.info.speechState){return;}
			if(!text){ABT.API.speech.deaconNext();}
			var deaconObj = swfobject.getObjectById("flexApp");
			if(deaconObj){deaconObj.esdStart(text,"ABT.API.speech.deaconNext()");}
		},
		deaconNext:function(){
			if(!ABT.info.speechState||ABT.API.speech.deaconMode==0){return ;}
			if(ABT.API.speech.deaconCount>=ABT.API.speech.textArray.length){return ;}
			ABT.API.speech.deaconText(ABT.API.speech.textArray[ABT.API.speech.deaconCount]);
			ABT.API.speech.deaconCount++;
		},
		deaconStart:function(deaconMode,deaconText){
			if(!ABT.info.speechState){return;}
			ABT.API.speech.deaconMode = deaconMode;
			if(ABT.API.speech.deaconMode==0){
				ABT.API.speech.deaconText(deaconText);
			}
			else if(ABT.API.speech.deaconMode==1){
				ABT.API.speech.textArray = deaconText;
				ABT.API.speech.deaconCount = 0;
				ABT.API.speech.deaconNext();
			}
		},
		deaconStop:function(){
			if(!ABT.info.speechState){return;}
			var deaconObj = swfobject.getObjectById("flexApp");
			deaconObj.esdStop();
		}
	}
}
//############################[工具条初始化]############################
ABT.init = function(){
	//工具条界面状态--------------------------
	ABT.info.UIState = false;
	//配置参数声明----------------------------
	declareABTConfig();
	//皮肤使用定义----------------------------
	ABT.skin = ABT.config.skin[ABT.config.defaultSkin];
	//浏览器类型判断--------------------------
	ABT.info.browser = new Object();
	var ua = navigator.userAgent.toLowerCase();
	var s;
	(s = ua.match(/msie ([\d.]+)/)) ? ABT.info.browser.ie = s[1] :
	(s = ua.match(/firefox\/([\d.]+)/)) ? ABT.info.browser.firefox = s[1] :
	(s = ua.match(/chrome\/([\d.]+)/)) ? ABT.info.browser.chrome = s[1] :
	(s = ua.match(/opera.([\d.]+)/)) ? ABT.info.browser.opera = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? ABT.info.browser.safari = s[1] : 0;
	//公共节点获取----------------------------
	ABT.dom = new Object();
	if(ABT.API.gi(ABT.config.container)){
		ABT.dom.container = ABT.API.gi(ABT.config.container);
		ABT.dom.allElements = ABT.dom.container.getElementsByTagName("*");
		ABT.dom.allImg = ABT.dom.container.getElementsByTagName("img");
		ABT.dom.allFrame = ABT.dom.container.getElementsByTagName("iframe");
		ABT.dom.allStyle = document.getElementsByTagName("head")[0].getElementsByTagName("link");
		ABT.dom.error = false;
	}
	else{ABT.dom.error = true;}
	//iframe键盘监听--------------------------
	if(!ABT.dom.error){
		for(var i=0;i<ABT.dom.allFrame.length;i++){
			var iframeDOM = ABT.dom.allFrame[i].contentWindow;
			try{
				var scriptElement = iframeDOM.document.createElement("script");
				scriptElement.setAttribute("type","text/javascript");
				scriptElement.text = "document.onkeydown = function(e){var e=e||event;document.parentWindow.parent.keyListener(e);}";
				iframeDOM.document.getElementsByTagName("head")[0].appendChild(scriptElement);
			}catch(e){}
		}
	}
	//皮肤预载--------------------------------
	var ABTStyleElement = document.createElement("link");
	ABTStyleElement.setAttribute("rel","stylesheet");
	ABTStyleElement.setAttribute("type","text/css");
	ABTStyleElement.setAttribute("id","ABTStyle");
	//ABTStyleElement.setAttribute("href",ABT.info.url+"css/"+ABT.skin.folder+"/skin.css");
	ABTStyleElement.setAttribute("href",ABT.info.url+"css/skin.css");
	document.getElementsByTagName("head")[0].appendChild(ABTStyleElement);
	//添加识别标签----------------------------
	ABT.API.plugin.call("addFlagTags");
	//添加语音工具----------------------------
	/*
	var ABTSpeecher = document.createElement("div");
	ABTSpeecher.setAttribute("id","ABTSpeecher");
	//ABTSpeecher.innerHTML = "<object id=\"flexApp\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\"0\"height=\"0\" tabindex=\"-1\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\""+ABT.config.speederURL+"\" /><!--[if !IE]>--><object type=\"application/x-shockwave-flash\" data=\""+ABT.config.speederURL+"\" width=\"0\" height=\"0\"><param name=\"allowScriptAccess\" value=\"always\" /><!--<![endif]--><div><h1>Alternative content</h1><p><a href=\"http://www.adobe.com/go/getflashplayer\"><img src=\"http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif\" alt=\"Get Adobe Flash player\" /></a></p></div><!--[if !IE]>--></object><!--<![endif]--></object>";
	ABTSpeecher.innerHTML = "";
	document.body.insertBefore(ABTSpeecher,document.body.firstChild);
	ABT.speecher = ABT.API.gi("flexApp");
	*/
	//初始化完成------------------------------
	ABT.info.pageLoaded = true;
	//读取记忆--------------------------------
	ABT.memory();
}

//###########################[工具条键盘接口]###########################
ABT.keyboardListener = function(ctrlKey,altKey,shiftKey,otherKey){
	var keyNum = String(ctrlKey?1:0)+String(altKey?1:0)+String(shiftKey?1:0)+String.fromCharCode(otherKey).toUpperCase();
	//界面开关识别----------------------------
	for(var a=0;a<ABT.config.UIShortcuts.length;a++){
		if(keyNum==ABT.API.keyCodeStringHandle(ABT.config.UIShortcuts[a])){
			ABT.show();
			break;
		}
	}
	//插件快捷键识别--------------------------
	for(var b in ABT.config.plugins){
		if(ABT.config.plugins[b].shortcuts){
			for(var c=0;c<ABT.config.plugins[b].shortcuts.length;c++){
				var shortcuts = ABT.config.plugins[b].shortcuts[c].split("|")[0];
				var parameterString = ABT.config.plugins[b].shortcuts[c].split("|")[1];
				var methodName = ABT.config.plugins[b].shortcuts[c].split("|")[2];
				if(keyNum==ABT.API.keyCodeStringHandle(shortcuts)){ABT.API.plugin.call(b,parameterString,methodName);}
			}
		}
	}
}
//###########################[工具条记忆接口]###########################
ABT.memory = function(){
	if(ABT.dom.error){return;}
	//alert(document.cookie);
	for(var a in ABT.config.plugins){
		var cookieContent = ABT.API.cookie.get(ABT.config.plugins[a].ms);
		if(Number(cookieContent)&&!isNaN(cookieContent)){ABT.show();break;}
	}
	if(Number(ABT.API.cookie.get("ABTUI"))==1&&!ABT.info.UIState){ABT.show();};
	for(var b in ABT.config.plugins){
		var cookieContent = ABT.API.cookie.get(ABT.config.plugins[b].ms);
		if(Number(cookieContent)&&!isNaN(cookieContent)&&ABT.config.plugins[b].ms!="ms_yy"){/*alert(b+"插件执行:"+cookieContent);*/ABT.API.plugin.call(b,ABT.config.plugins[b].ms,"memoryMethod");}
	}
}
//###########################[工具条界面开关]###########################
ABT.show = function(){
	if(!ABT.info.pageLoaded||ABT.dom.error){return;}
	if(!ABT.info.UIState){
		var ABTHTML = "<h2 id=\"ABTTitle\">无障碍辅助系统</h2>";
		var pluginGroupCount = 0;
		for(var a in ABT.config.pluginGroup){
			pluginGroupCount++;
			ABTHTML = ABTHTML+"<h3 id=\""+a+"Title\"><a href=\"javascript:ABT.API.cc('ABTInterface','ABTTag"+pluginGroupCount+"On')\" title=\""+ABT.config.pluginGroup[a].name+"\">"+ABT.config.pluginGroup[a].name+"</a></h3><div class=\"pluginsBox\" id=\""+a+"Box\">";
			for(var b=0;b<ABT.config.pluginGroup[a].member.length;b++){
				//语音开关控制
				if(ABT.config.plugins[ABT.config.pluginGroup[a].member[b]].ms!="ms_yy"){
					ABTHTML = ABTHTML+ABT.config.plugins[ABT.config.pluginGroup[a].member[b]].showHTML;
				}else{
					if($.cookie('yuyinOpen')=="true"){
						ABTHTML = ABTHTML+ABT.config.plugins[ABT.config.pluginGroup[a].member[b]].showHTML;
					}
				}
			}
			ABTHTML = ABTHTML+"</div>";
		}
		var newABT = document.createElement("div");
		newABT.setAttribute("id","ABTInterface");
		newABT.className = ABT.skin.className;
		newABT.innerHTML = ABTHTML;
		document.body.style.paddingTop = ABT.skin.UIHeight+"px";
		document.body.insertBefore(newABT,document.body.firstChild);
		if(ABT.config.bodyBgPosition){document.body.style.backgroundPosition = "center "+ABT.skin.UIHeight+"px";}
		ABT.info.UIState = true;
		ABT.API.cookie.set("ABTUI",1);
	}
	else{
		document.body.removeChild(document.getElementById("ABTInterface"));
		document.body.style.paddingTop = "0px";
		if(ABT.config.bodyBgPosition){document.body.style.backgroundPosition = "center top";}
		ABT.info.UIState = false;
		ABT.API.cookie.set("ABTUI",0);
	}
	var openMode=$.cookie("ms_yy");
	var base=$.cookie('base');
	//初始化语音开关状态
	if(openMode==1){
		$("#open").val("关闭");
		var closePng=base+'/r/cms/yuyin/images/close.png';
		$("#open").css("background","rgba(0, 0, 0, 0) url("+closePng+") no-repeat scroll 15px top");
		GOV.Openyuyin();
	}else{
		$("#open").val("开启");
		var openPng=base+'/r/cms/yuyin/images/open.png';
		$("#open").css("background","rgba(0, 0, 0, 0) url("+openPng+") no-repeat scroll 15px top");
		GOV.StopYuyin();
	}
}
//###########################[工具条重置功能]###########################
ABT.Rreset = function(){
	ABT.API.cookie.set("ABTUI",1);
	for(var a in ABT.config.plugins){ABT.API.cookie.set(ABT.config.plugins[a].ms,0);}
	window.location.reload();
}
//###########################[工具条插件声明]###########################
ABT.plugins = new Object();