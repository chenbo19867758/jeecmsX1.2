//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 区域跳转插件 v1.0
//说明：为工具条提供"区域跳转"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["areaSkip"] = {
	mainMethod:function(skipModeString){
		if(!skipModeString){return ;}
		var areaId = skipModeString.split(":")[0];
		var elementText = skipModeString.split(":")[1];
		if(ABT.API.gi(areaId)){
			ABT.API.skipElement.build(ABT.API.gi(areaId),elementText);
		}
	}
}