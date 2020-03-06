GOV = {};
/**
 * 语音播放
 */
var apiurl="http://tsn.baidu.com/text2audio";
var audio = document.createElement("audio");
var base,enable;
GOV.yuyinInit = function(b,e){
	base=b;
	enable=e;
	$.cookie('yuyinOpen',e,{expires:30,path:'/'});
	$.cookie('base',b,{expires:30,path:'/'});
}
GOV.Openyuyin = function() {
		$("a").hover(
			  function () {
				  GOV.beginAudio($(this).text());
			  },
			  function () {
				  GOV.stopAudio();
			  }
		);
		$("p").hover(
			  function () {
				  GOV.beginAudio($(this).text());
			  },
			  function () {
				  GOV.stopAudio();
			  }
		);
}
GOV.StopYuyin = function() {
	GOV.stopAudio();
}
//text文本内容
//data token等相关参数
GOV.beginAudio=function filterTextAndStartAudio(text){
	var clientEnable=$.cookie('ms_yy');
	if(enable=="true"&&clientEnable==1){
		text=text.replace(/<.*>/,"").replace("&nbsp;","").replace(/(^\s*)|(\s*$)/g,"");
		if(text!=""){
			GOV.startAudio(base,text);
		}
	}
}
GOV.startAudio=function toAudio(base,text){
	$.post(base+"/yuyin/ajax.jspx", {}, function(data) {
		audio.src = apiurl+"?tex="+text+data;
		audio.load();
		audio.play();
	});
}
GOV.stopAudio=function stopAudio(){
	audio.pause();
}