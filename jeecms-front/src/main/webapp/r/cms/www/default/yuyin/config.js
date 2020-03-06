//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) v4.01 配置文件
//说明：用于ABT的功能设置
//开发：scorpio
//日期：2011/08/29
// http://www.oddpi.com
//----------------------------------------------------------------------

function declareABTConfig(){
	ABT.config = new Object();
	//-------------------------------------------------------
	//*****基础功能设置**************************************
	//-------------------------------------------------------
	
	//工具条主开关---------------------------------
	ABT.config.mainSwitch = true;
	
	//工具条界面开关快捷键-------------------------
	ABT.config.UIShortcuts = ["alt+j"];
	//工具条界面显示效果开关-----------------------
	//ABT.config.UIEffect = true;
	//body标签背景位移开关-------------------------
	ABT.config.bodyBgPosition = true;
	
	//主容器名称-----------------------------------
	ABT.config.container = "container";
	
	//帮助页面地址---------------------------------
	ABT.config.helpPageURL = "http://192.168.170.140:8700/toolbarhelp/helpContent.html";
	
	//语音朗读Flash地址----------------------------
	ABT.config.speederURL = "http://221.208.141.145:8080/ESDOA/test.swf";
	
	//cookie功能开关-------------------------------
	ABT.config.cookieSwitch = true;
	//cookie保存时间-------------------------------
	ABT.config.cookieTime = 30;
	
	//皮肤选择及声明-------------------------------
	ABT.config.defaultSkin = 0;
	ABT.config.skin = new Array();
	ABT.config.skin[0] = {
		name:"默认样式",
		className:"ABTTag1On",
		UIHeight:133,
		folder:"default"
	}
	//-------------------------------------------------------
	//*****插件声明部分**************************************
	//-------------------------------------------------------
	//插件组声明-----------------------------------
	ABT.config.pluginGroup = {
		"amblyopiaAide":{
			name:"无障碍阅读辅助工具",
			member:["yuyin","textMode","textTips","pageZoom","fontZoom","showMainContent","hightContrast","guides","reset","close"]
		}
		/*
		"speech":{
			name:"语音朗读工具",
			member:["speech"]
		}
		*/
	}
	//插件声明-------------------------------------
	ABT.config.plugins = {
		//区域跳转-----------------------
		"areaSkip":{
			shortcuts:[
				"Alt+K|header:已跳转到头部内容区域",
				"Alt+C|content:已跳转到主要内容区域",
				"Alt+Ctrl+C|content:已跳转到主要内容区域",
				"Alt+M|main:已跳转到中间内容区域",
				"Ctrl+Alt+Shift+M|main:已跳转到中间内容区域",
				"Alt+L|leftsidebar:已跳转到左侧内容区域",
				"Alt+L|leftcolumn:已跳转到左侧内容区域",
				"Ctrl+Alt+Shift+L|leftsidebar:已跳转到左侧内容区域",
				"Ctrl+Alt+Shift+L|leftcolumn:已跳转到左侧内容区域",
				"Alt+R|rightsidebar:已跳转到右侧内容区域",
				"Alt+R|rightcolumn:已跳转到右侧内容区域",
				"Ctrl+Alt+Shift+R|rightsidebar:已跳转到右侧内容区域",
				"Ctrl+Alt+Shift+R|rightcolumn:已跳转到右侧内容区域",
				"Alt+B|footer:已跳转到底部内容区域",
				"Ctrl+Alt+Shift+B|footer:已跳转到底部内容区域"
			]
		},
		//栏目跳转-----------------------
		"customSkip":{
			shortcuts:["ctrl+z|0","alt+z|0","ctrl+shift+z|1","alt+shift+z|1"]
		},
		//语音工具-----------------------
		"yuyin":{
			showHTML:"<ul><li><input type=\"button\" id=\"open\" value=\"开启\" onclick=\"ABT.API.plugin.call('yuyin')\" /></li></ul>",
			shortcuts:["alt+shift+y"],
			ms:"ms_yy"
		},
		//文本模式-----------------------
		"textMode":{
			showHTML:"<ul><li><input type=\"button\" id=\"textMode\" value=\"文本模式\" onclick=\"ABT.API.plugin.call('textMode')\" /></li></ul>",
			shortcuts:["alt+shift+j"],
			ms:"ms_tm"
		},
		//页面缩放-----------------------
		"pageZoom":{
			showHTML:"<ul><li><input type=\"button\" id=\"pageZoom1\" value=\"页面放大\" onclick=\"ABT.API.plugin.call('pageZoom',1)\" /></li><li><input type=\"button\" id=\"pageZoom2\" value=\"页面缩小\" onclick=\"ABT.API.plugin.call('pageZoom',0)\" /></li></ul>",
			ms:"ms_pz"
		},
		//文字缩放-----------------------
		"fontZoom":{
			showHTML:"<ul><li><input type=\"button\" id=\"fontZoom1\" value=\"文字放大\" onclick=\"ABT.API.plugin.call('fontZoom',1)\" /></li><li><input type=\"button\" id=\"fontZoom2\" value=\"文字缩小\" onclick=\"ABT.API.plugin.call('fontZoom',0)\" /></li></ul>",
			ms:"ms_fz"
		},
		//全屏正文-----------------------
		"showMainContent":{
			showHTML:"<ul><li><input type=\"button\" id=\"showMainContent\" value=\"全屏正文\" onclick=\"ABT.API.plugin.call('showMainContent')\" onfocus=\"ABT.API.cc('hightContrastList','hclistclose')\" /></li></ul>"
		},
		//高对比度-----------------------
		"hightContrast":{
			showHTML:"<ul><li onmouseover=\"ABT.API.cc('hightContrastList','hclistopen')\" onmouseout=\"ABT.API.cc('hightContrastList','hclistclose')\"><input type=\"button\" id=\"hightContrast\" value=\"高亮显示\" onfocus=\"ABT.API.cc('hightContrastList','hclistopen')\" /><ol id=\"hightContrastList\" class=\"hclistclose\"><li id=\"hcdefault\" class=\"hconchange\"><a href=\"javascript:ABT.API.plugin.call('hightContrast',0)\" title=\"默认样式\">默认样式</a></li><li id=\"hcbw\"><a href=\"javascript:ABT.API.plugin.call('hightContrast',1)\" title=\"黑白对比\">黑白对比</a></li><li id=\"hcwp\"><a href=\"javascript:ABT.API.plugin.call('hightContrast',2)\" title=\"白紫对比\">白紫对比</a></li><li id=\"hcbb\"><a href=\"javascript:ABT.API.plugin.call('hightContrast',3)\" title=\"黑蓝对比\">黑蓝对比</a></li><li id=\"hcrw\"><a href=\"javascript:ABT.API.plugin.call('hightContrast',4)\" title=\"红白对比\">红白对比</a></li></ol></li></ul>",
			ms:"ms_hc"
		},
		//辅助线-------------------------
		"guides":{
			showHTML:"<ul><li><input type=\"button\" id=\"guides\" value=\"辅助线\" onfocus=\"ABT.API.cc('hightContrastList','hclistclose')\" onclick=\"ABT.API.plugin.call('guides')\" /></li></ul>",
			ms:"ms_gl"
		},
		//文字提示-----------------------
		"textTips":{
			showHTML:"<ul><li><input type=\"button\" id=\"textTips1\" value=\"文字注音\" disabled=\"true\" onclick=\"ABT.API.plugin.call('textTips',1,'subMethod')\" /></li><li><input type=\"button\" id=\"textTips2\" value=\"局部文字放大\" onclick=\"ABT.API.plugin.call('textTips')\" /></li><li><input type=\"button\" id=\"textTips3\" value=\"焦点变色\" disabled=\"true\" onclick=\"ABT.API.plugin.call('textTips',2,'subMethod')\" /></li></ul>",
			ms:"ms_tt"
		},
		//重置工具条---------------------
		"reset":{
			showHTML:"<ul><li><input type=\"button\" id=\"ABTReset\" value=\"重置\" onclick=\"ABT.Rreset()\" /></li></ul>"
		},
		//关闭工具条---------------------
		"close":{
			showHTML:"<ul class=\"pluginlast\"><li><input type=\"button\" id=\"ABTClose\" value=\"关闭\" onclick=\"ABT.show()\" /></li></ul>"
		},
		//语音工具-----------------------
		"speech":{
			showHTML:"<ul><li><input type=\"button\" id=\"speech1\" value=\"点读\" onclick=\"ABT.API.plugin.call('speech',1)\" /></li><li><input type=\"button\" id=\"speech2\" value=\"连读\" onclick=\"ABT.API.plugin.call('speech',2)\" /></li><li><input type=\"button\" id=\"speech3\" value=\"停止\" onclick=\"ABT.API.speech.deaconStop()\" /></li></ul><ul class=\"pluginlast\"><li><input type=\"button\" id=\"speech4\" value=\"一键朗读正文\" onclick=\"ABT.API.plugin.call('speech',3)\" /></li></ul>"
		},
		//识别标签添加-------------------
		"addFlagTags":{}
	}
}