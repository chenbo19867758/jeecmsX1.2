//----------------------------------------------------------------------
//名称：ABT(Accessibility Browser Tool) - 辅助线插件 v1.0
//说明：为工具条提供"语音"功能的插件
//开发：tom
//日期：2015/07/07
// http://www.jeecms.com
//----------------------------------------------------------------------

ABT.plugins["yuyin"] = {
	memoryMethod:function(){
		ABT.plugins["yuyin"].mainMethod();
	},
	yuyinState:false,
	yuyinWidth:4,
	yuyinColor:"#F00",
	yuyinSkew:5,
	mainMethod:function(){
		var openMode=$.cookie(ABT.config.plugins["yuyin"].ms);
		var base=$.cookie('base');
		if(openMode==1){
			openMode=0;
			GOV.Openyuyin();
			$("#open").val("开启");
			var openPng=base+'/r/cms/yuyin/images/open.png';
			$("#open").css("background","rgba(0, 0, 0, 0) url("+openPng+") no-repeat scroll 15px top");
		}else{
			openMode=1;
			GOV.StopYuyin();
			$("#open").val("关闭");
			var closePng=base+'/r/cms/yuyin/images/close.png';
			$("#open").css("background","rgba(0, 0, 0, 0) url("+closePng+") no-repeat scroll 15px top");
		}
		ABT.API.cookie.set(ABT.config.plugins["yuyin"].ms,openMode);
	}
}