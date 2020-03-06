var getUrlArg = function(name) {
	var url = window.location.search;
	console.log('url:'+url)
	// 正则筛选地址栏
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)")
	console.log('reg:'+reg)
	// 匹配目标参数
	var result = url.substr(1).match(reg);
	console.log('result:'+result)
	//返回参数值
	return result ? decodeURIComponent(result[2]) : null;
}

var desEncrypt = function(str) {
	var cryptoKey = CryptoJS.enc.Utf8.parse('WfJTKO9S4eLkrPz2JKrAnzdb')
	var cryptoIv = CryptoJS.enc.Utf8.parse('D076D35C'.substr(0, 8))
	var encodeStr = CryptoJS.TripleDES.encrypt(str, cryptoKey, {
		iv: cryptoIv,
		mode: CryptoJS.mode.CBC,
		padding: CryptoJS.pad.Pkcs7
	})
	return encodeStr.toString()
}

// 成功的提示窗
function winTip(str, t, callBack) {
	t = t || 2000;
	var dom = document.createElement("p");
	dom.setAttribute('class', 'win-tip');
	document.body.appendChild(dom);
	var mytip = document.querySelector('.win-tip')

	mytip.style.display = "block";
	mytip.innerHTML = str;
	var tipHeight = mytip.offsetHeight;

	//文字两行或两行以上
	if ((tipHeight - 20) / 18 > 1) {
		mytip.style.width = "55%";
	}
	setTimeout(function() {
		mytip.style.display = "none";
		mytip.parentNode.removeChild(mytip);
		if (callBack) {
			callBack();
		}
	}, t);
}

// 失败的提示窗
function errorTip(str, t, callBack) {
	t = t || 2000;
	var dom = document.createElement("p");
	dom.setAttribute('class', 'error-tip');
	document.body.appendChild(dom);
	var mytip = document.querySelector('.error-tip')

	mytip.style.display = "block";
	mytip.innerHTML = str;
	var tipHeight = mytip.offsetHeight;

	//文字两行或两行以上
	if ((tipHeight - 20) / 18 > 1) {
		mytip.style.width = "55%";
	}
	setTimeout(function() {
		mytip.style.display = "none";
		mytip.parentNode.removeChild(mytip);
		if (callBack) {
			callBack();
		}
	}, t);
}
// winTip('成功的提示')	
// errorTip('失败的提示')

// 加载更多
function toloading(loading,callback) {
	var that = this
	var oScroll = new BScroll(loading, {
		probeType: 2,
		pullUpLoad: {
			threshold: 10
		},
		mouseWheel: { // pc端同样能滑动
			speed: 20,
			invert: false
		},
		useTransition: false // 防止iphone微信滑动卡顿
	});
	oScroll.on("pullingUp", function() {
		callback()
	});
	oScroll.refresh();
}
api = {
	POST: function(url, data, callback) {
		axios({
			method: 'post',
			url: url,
			data: JSON.stringify(data),
			headers: {
				'JEECMS-Auth-Token': localStorage.getItem('JEECMS-Auth-Token'),
				'Redirect-Header': false,
				'Content-Type': 'application/json'
			}
		}).then(function(response) {
			if (response.status == 200) {
				callback(response.data)
			} else {
				errorTip(response.data.message)
			}
		}).catch(function(error) {
			console.log(error)
		})
	},
	GET: function(url, data, callback) {
		axios({
			method: 'get',
			url: url,
			params: data,
			headers: {
				'JEECMS-Auth-Token': localStorage.getItem('JEECMS-Auth-Token'),
				'Redirect-Header': false,
				'Content-Type': 'application/json'
			}
		}).then(function(response) {
			if (response.status == 200) {
				callback(response.data)
			} else {
				errorTip(response.data.message)
			}
		}).catch(function(error) {
			console.log(error)
		})
	},
	PUT: function(url, data, callback) {
		axios({
			method: 'put',
			url: url,
			data: JSON.stringify(data),
			headers: {
				'JEECMS-Auth-Token': localStorage.getItem('JEECMS-Auth-Token'),
				'Redirect-Header': false,
				'Content-Type': 'application/json'
			}
		}).then(function(response) {
			if (response.status == 200) {
				callback(response.data)
			} else {
				errorTip(response.data.message)
			}
		}).catch(function(error) {
			console.log(error)
		})
	},
	DELETE: function(url, data, callback) {
		axios({
			method: 'delete',
			url: url,
			data: JSON.stringify(data),
			headers: {
				'JEECMS-Auth-Token': localStorage.getItem('JEECMS-Auth-Token'),
				'Redirect-Header': false,
				'Content-Type': 'application/json'
			}
		}).then(function(response) {
			if (response.status == 200) {
				callback(response.data)
			} else {
				errorTip(response.data.message)
			}
		}).catch(function(error) {
			console.log(error)
		})
	}
}




// 后台统计
Cms = {};
/**
 * 浏览次数
 */
Cms.viewCount = function(base, contentId, views, downs, ups, downloads, comments) {
	views = views || "views";
	downs = downs || "downs";
	ups = ups || "ups";
	downloads = downloads || "downloads";
	comments = comments || "comments";
	$.getJSON(base + "/content/view", {
		contentId : contentId
	}, function(data) {
		var json = data.data;
		if (json != null) {
			//总
			$("#" + views).find('span').text(json.views);
			$("#" + downs).text(json.downs);
			$("#" + ups).text(json.ups);
			$("#" + downloads).text(json.downloads);
			$("#" + comments).text(json.comments);
		}
	});
}