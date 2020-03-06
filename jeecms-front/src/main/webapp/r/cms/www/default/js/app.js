
var	desEncrypt = function(str){
		var cryptoKey = CryptoJS.enc.Utf8.parse('WfJTKO9S4eLkrPz2JKrAnzdb')
		var cryptoIv = CryptoJS.enc.Utf8.parse('D076D35C'.substr(0, 8))
		var encodeStr = CryptoJS.TripleDES.encrypt(str, cryptoKey, {
		iv: cryptoIv,
		mode: CryptoJS.mode.CBC,
		padding: CryptoJS.pad.Pkcs7
		})
		return encodeStr.toString()
	}

var myMessage = new MyMessage.message({
	    /*默认参数，下面为默认项*/
	    iconFontSize: "26px", //图标大小,默认为20px
	    messageFontSize: "20px", //信息字体大小,默认为12px
	    showTime: 2000, //消失时间,默认为3000
	    align: "center", //显示的位置类型center,right,left
	    positions: { //放置信息距离周边的距离,默认为10px
	        top: "10px",
	        bottom: "10px",
	        right: "10px",
	        left: "10px"
	    },
	    message: "这是一条消息", //消息内容,默认为"这是一条消息"
	    type: "normal", //消息的类型，还有success,error,warning等，默认为normal
	})

	var getUrlArg = function(name) {
		var url = window.location.search;
    // 正则筛选地址栏
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)")
    // 匹配目标参数
    var result = url.substr(1).match(reg);
    //返回参数值
    return result ? decodeURIComponent(result[2]) : null;
	}

	api = {
		POST:function(url,data,callback) {
			$.ajax({
				url: url,
				method: 'POST',
				data:JSON.stringify(data),
				headers:{'JEECMS-Auth-Token':localStorage.getItem('JEECMS-Auth-Token'),'Redirect-Header':false,'Content-Type':'application/json'},
				success: function (data) {
					callback(data)
				},
				error: function (xhr, textStatus, errorThrown) {
					myMessage.add(errorThrown, 'error');
				}
			})
		},
		GET:function(url,data,callback) {
			$.ajax({
				url: url,
				method: 'GET',
				data:data,
				headers:{'JEECMS-Auth-Token':localStorage.getItem('JEECMS-Auth-Token'),'Redirect-Header':false,'Content-Type':'application/json'},
				success: function (result) {
					callback(result)
				},
				error: function (xhr, textStatus, errorThrown) {
					myMessage.add(errorThrown, 'error');
				}
			})
		},
		PUT:function(url,data,callback) {
			$.ajax({
				url: url,
				method: 'PUT',
				data:JSON.stringify(data),
				headers:{'JEECMS-Auth-Token':localStorage.getItem('JEECMS-Auth-Token'),'Redirect-Header':false,'Content-Type':'application/json'},
				success: function (result) {
					callback(result)
				},
				error: function (xhr, textStatus, errorThrown) {
					myMessage.add(errorThrown, 'error');
				}
			})
		},
		DELETE:function(url,data,callback) {
			$.ajax({
				url: url,
				method: 'DELETE',
				data:JSON.stringify(data),
				headers:{'JEECMS-Auth-Token':localStorage.getItem('JEECMS-Auth-Token'),'Redirect-Header':false,'Content-Type':'application/json'},
				success: function (result) {
					callback(result)
				},
				error: function (xhr, textStatus, errorThrown) {
					myMessage.add(errorThrown, 'error');
				}
			})
		}
	}