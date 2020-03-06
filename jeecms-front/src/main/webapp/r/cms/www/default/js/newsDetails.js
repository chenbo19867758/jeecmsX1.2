var contentId = ''
var allcomment = []
var CollectId = ''
var likeId=''

function Collect(id){
	CollectId=id
	if($('#heart').attr("name")==1){
		noCollect()
	}else if($('#heart').attr("name")==2){
		toCollect()
	}
}

// 收藏
function toCollect() {
	api.POST('/usercollections', {
		contentId: CollectId
	}, function(data) {
		if (data.code == 200) {
			$("#heart").attr("name","1");
			$("#heart").removeClass('iconheart')
			$("#heart").addClass('iconheart-fill')
			$('.heart .plus').addClass('plus-animation')
			$('.heart .sub').removeClass('plus-animation')
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}

// 取消收藏
function noCollect(){
	api.GET('/member/usercollections/recall', {
		id: CollectId
	}, function(data) {
		if (data.code == 200) {
			$("#heart").attr("name","2");
			$("#heart").removeClass('iconheart-fill')
			$("#heart").addClass('iconheart')
			$('.heart .sub').addClass('plus-animation')
			$('.heart .plus').removeClass('plus-animation')
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}

/**
 * 检测内容是否点赞
 */
function getContentUp(contentId) {
	api.GET("/content/isUp", {
		"contentId" : contentId
	}, function(data) {
		if(data.data) {
			$("#isZan").attr("name","1");
			$("#isZan").removeClass('iconthumb-up-line')
			$("#isZan").addClass('iconthumb-up-fill')
		} else {
			$("#isZan").attr("name","2");
			$("#isZan").removeClass('iconthumb-up-fill')
			$("#isZan").addClass('iconthumb-up-line')
		}
	});
	return true;
}

function like(id){
	likeId = id
	if($('#isZan').attr("name")==1){
		noLike()
	}else if($('#isZan').attr("name")==2){
		toLike()
	}
}

function toLike() {
	api.POST('/content/up', {
		contentId: likeId
	}, function(data) {
		if (data.code == 200) {
			$("#isZan").attr("name","1");
			$("#isZan").removeClass('iconthumb-up-line')
			$("#isZan").addClass('iconthumb-up-fill')
			$('#contentUp').text((Number($('#contentUp').text()) + 1))
			$('.zan .plus').addClass('plus-animation')
			$('.zan .sub').removeClass('plus-animation')
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}
function noLike() {
	api.POST('/content/cancelUp', {
		contentId: likeId
	}, function(data) {
		if (data.code == 200) {
			$("#isZan").attr("name","2");
			$("#isZan").removeClass('iconthumb-up-fill')
			$("#isZan").addClass('iconthumb-up-line')
			$('#contentUp').text((Number($('#contentUp').text()) - 1))
			$('.zan .sub').addClass('plus-animation')
			$('.zan .plus').removeClass('plus-animation')
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}

// function getUsercommentSum(){
//     api.GET('/usercomment/count/'+contentId,{},function(data){
//         if(data.code==200){

//         }
//     })
// }

function getUsercomment(id) {
	if (id) {
		contentId = id
	}
	api.GET('/usercomment', {
		contentId: contentId,
		sortStatus: 1,
		page: "1",
		size: "3"
	}, function(data) {
		if (data.code == 200) {
			allcomment = data.data.page.content
			$('#totalElements').text(data.data.count + '条')
			startHotcomment()
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}



// 全部评论
function startHotcomment() {
	var length = 0
	var html = ''
	if (allcomment.length > 3) {
		length = 3
	} else if (allcomment.length === 0) {
		html += '<div class="noComment"></div>'
		$('.lookingAll').hide()
	} else {
		length = allcomment.length
	}

	for (var j = 0; j < length; j++) {
		html += '<div class="user-icon pointer fl">'
		if (allcomment[j].user) {
			html += '<img src="' + allcomment[j].user.userExt.userImgUrl + '"></div>'
			html += '<div class="hot-text fl">'
			html += '<div class="user-line overflow Semilight"><span class="username block fl">' + allcomment[j].user.username +
				'</span>'
		} else {
			html += '<i class="iconfont icontouxiang"></i></div>'
			html += '<div class="hot-text fl">'
			html += '<div class="user-line overflow Semilight"><span class="username block fl">' + '匿名' + '</span>'
		}
		
		html += '<span class="time block fl">' + allcomment[j].createTime + '</span>'
		// 判断是否已举报
		if(allcomment[j].report){
			html += '<div onclick="report(' + allcomment[j].id + ')" class="warning fr hover pointer"><i class="iconfont iconalarm-warning-fill"></i>举报</div></div>'
		}else{
			html += '<div class="nowarning fr hover pointer"><i class="iconfont iconalarm-warning-fill"></i>已举报</div></div>'
		}
		
		html += '<div class="user-text Regular">' + allcomment[j].commentText + '</div>'
		if (allcomment[j].children.length > 0) {
			html += '<div class="text-details"><div class="text-list pointer hover">'
			for (var i = 0; i < allcomment[j].children.length; i++) {
				// 判断是否匿名用户
				if (allcomment[j].children[i].user) {
					// 判断是否是回复评论
					if(allcomment[j].children[i].replyParent){
						html += '<a class="list-title block overfow-text Semilight"><span class="Regular">' + allcomment[j].children[i].user.username + '</span>：' +
							allcomment[j].children[i].commentText
					}else{
						if(allcomment[j].children[i].replyUser && allcomment[j].children[i].replyUser.username){
							html += '<a class="list-title block overfow-text Semilight"><span class="Regular">' + allcomment[j].children[i].user.username +'</span> 回复 <span class="Regular">'+ allcomment[j].children[i].replyUser.username +'</span>：' + allcomment[j].children[i].commentText
						}else{
							html += '<a class="list-title block overfow-text Semilight"><span class="Regular">' + allcomment[j].children[i].user.username +'</span> 回复 <span class="Regular">'+ '匿名' +'</span>：' + allcomment[j].children[i].commentText
						}
						
					}
				} else {
					if(allcomment[j].children[i].replyParent){
						html += '<a class="list-title block overfow-text Semilight">' + '<span class="Regular">匿名</span>' + '：' + allcomment[j].children[i].commentText
					}else{
						if(allcomment[j].children[i].replyUser && allcomment[j].children[i].replyUser.username){
							html += '<a class="list-title block overfow-text Semilight">' + '<span class="Regular">匿名</span>' +' 回复 <span class="Regular">'+ allcomment[j].children[i].replyUser.username +'</span>：' + allcomment[j].children[i].commentText
						}else{
							html += '<a class="list-title block overfow-text Semilight">' + '<span class="Regular">匿名</span>' +' 回复 <span class="Regular">'+ '匿名' +'</span>：' + allcomment[j].children[i].commentText
						}
						
					}
				}
				// 判断是否已点赞
				if(allcomment[j].children[i].isLike){
					html += '</a><div class="others overflow Semilight"><a class="block fl common-red hover" onclick="commentPraise(' +
					allcomment[j].children[i].id + ')">赞'
				}else{
					html += '</a><div class="others overflow Semilight"><a class="block fl sign-red hover" onclick="noCommentPraise(' +
					allcomment[j].children[i].id + ')">已赞'
				}
				html += allcomment[j].children[i].upCount + '</a><a class="sending block fl">·<span class="common-red hover">回复</span></a><a class="back fewer block fl">·<span class="common-red hover">收起</span></a><a class="block fl">·<span>' +allcomment[j].children[i].createTime +'</span></a><a class="hidden block fl">·'
				// 判断是否已举报
				if(allcomment[j].children[i].report){
					html += '<span class="common-red hover" onclick="report(' + allcomment[j].children[i].id + ')">举报</span>'
				}else{
					html += '<span class="sign-red hover">已举报</span>'
				}
				
				
				html +='</a></div><div class="send-input none"><textarea></textarea><a name="' + allcomment[j].children[i].id + '" value="' + allcomment[j].id + '" class="Semilight block grandson">回复</a></div>'
			}
			html += '</div></div>'
			// html+='<div class="send-number pointer Semilight">查看全部<span class="font-hover">'+allcomment[j].children.length+'</span>条回复 ></div>'
		}
		if(allcomment[j].isLike){
			html += '<div class="zan-send overflow Semilight"><div class="zan fl hover pointer" onclick="commentPraise(' +
				allcomment[j].id + ')"><i class="iconfont iconthumb-up-fill nolike"></i>赞' + allcomment[j].upCount 
		}else{
			html += '<div class="zan-send overflow Semilight"><div class="yeslike fl hover pointer" onclick="noCommentPraise(' +
				allcomment[j].id + ')"><i class="iconfont iconthumb-up-fill"></i>赞' + allcomment[j].upCount 
		}
		html +='</div><div class="send replys fl hover pointer" ><i class="iconfont iconmessage--fill"></i>回复</div><a class="packup back none pointer"><span class="common-red hover">收起</span></a><div class="send-input none"><textarea></textarea><a name="' +
			allcomment[j].id + '" class="Semilight reply block">回复</a></div></div></div>'
	}

	$('.hot-comment').html(html)
}

var checkStrLengths = function(str, maxLength) {
	var maxLength = maxLength;
	var result = 0;
	if (str && str.length > maxLength) {
		result = maxLength;
	} else {
		result = str.length;
	}
	return result;
}

//监听输入
$("#usercomments").on('input propertychange', function() {
	console.log($("#usercomments").val())
	//获取输入内容
	var userDesc = $(this).val();

	//判断字数
	var len;
	if (userDesc) {
		len = checkStrLengths(userDesc, 150);
	} else {
		len = 0
	}

	//显示字数
	$("#commentsLength").html(len + '/150');
});

// 举报
function report(id) {
	api.POST('/usercomment/report', {
		commentId: id
	}, function(data) {
		if (data.code == 200) {
			getUsercomment()
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}

// 孙评论回复
$(document).on('click', '.grandson', function() {
	if (this.previousSibling.value) {
		api.POST('/usercomment', {
			contentId: contentId,
			commentText: this.previousSibling.value,
			parentId: this.getAttribute("value"),
			userCommentId: this.getAttribute("name")
		}, function(data) {
			if (data.code == 200) {
				myMessage.add('评论成功', 'success');
				getUsercomment()
			} else {
				myMessage.add(data.message, 'error');
			}
		})
	} else {
		myMessage.add('请输入评论', 'error');
	}

})

// 子评论回复
$(document).on('click', '.reply', function() {
	if (this.previousSibling.value) {
		api.POST('/usercomment', {
			contentId: contentId,
			commentText: this.previousSibling.value,
			parentId: this.getAttribute("name"),
			userCommentId: this.getAttribute("name")
		}, function(data) {
			if (data.code == 200) {
				myMessage.add('评论成功', 'success');
				getUsercomment()
			} else {
				myMessage.add(data.message, 'error');
			}
		})
	} else {
		myMessage.add('请输入评论', 'error');
	}

})

// 内容评论回复
function toUsercomment(id) {
	if ($('#usercomments').val()) {
		api.POST('/usercomment', {
			contentId: id,
			commentText: $('#usercomments').val(),
			parentId: '',
			userCommentId: ''
		}, function(data) {
			if (data.code == 200) {
				myMessage.add('评论成功', 'success');
				getUsercomment()
			} else {
				myMessage.add(data.message, 'error');
			}
		})
	} else {
		myMessage.add('请先输入评论', 'warning');
	}

}

$(function() {

	// 分享
	$('.left-share .iconweixin').hover(function() {
		$('.left-share .iconweixin').css('color', '#4BB83F');
	});
	$('.left-share .iconQQ').hover(function() {
		$('.left-share .iconQQ').css('color', '#36B7F6');
	});
	$('.left-share .iconweibo1').hover(function() {
		$('.left-share .iconweibo1').css('color', '#E42324');
	});
	$('.left-share .iconQQkongjian').hover(function() {
		$('.left-share .iconQQkongjian').css('color', '#FDBF2D');
	});
	// 评论收起展开
	$(document).on('click', '.sending', function() {
		this.style.display = "none";
		this.nextSibling.style.display = "block";
		this.parentNode.nextSibling.style.display = "block";
	})
	$(document).on('click', '.fewer', function() {
		this.style.display = "none";
		this.parentNode.nextSibling.style.display = "none";
		this.previousSibling.style.display = "block";
	})

	$(document).on('click', '.replys', function() {
		this.style.display = "none";
		this.nextSibling.style.display = "block";
		this.nextSibling.nextSibling.style.display = "block";
	})
	$(document).on('click', '.packup', function() {
		this.previousSibling.style.display = "block";
		this.style.display = "none";
		this.nextSibling.style.display = "none";
	})
});

function commentPraise(id) {
	api.POST('/usercomment/up', {
		"commentId": id
	}, function(res) {
		if (res.code == 200) {
			getUsercomment()
		} else {
			myMessage.add(res.message, 'error');
		}
	})
}

function noCommentPraise(id){
	api.POST('/usercomment/cancel/up', {
		"commentId": id
	}, function(res) {
		if (res.code == 200) {
			getUsercomment()
		} else {
			myMessage.add(res.message, 'error');
		}
	})
}


var batchPage = 1
var batchLast = false
function inabatch(){
	if(batchPage<5 && !batchLast){
		batchPage += 1
	}else{
		batchPage = 1
	}
	api.GET('/content/page',{
		"contentId": "",
		"channelIds":"" ,
		"tagIds": "",
		"channelPaths": "news",
		"siteIds": "",
		"typeIds": "",
		"title": "",
		"isNew": "",
		"isTop": "",
		"timeBegin": "",
		"timeEnd": "",
		"excludeId": "",
		"orderBy": "",
		"page": batchPage,
		"size": 5,
		"releaseTarget": ""
	},function(res){
		if(res.code===200){
			var data = res.data.content
			batchLast = res.data.last
			var html = ""
			for(var i=0;i<data.length;i++){
				html += '<div class="main-news pointer overflow"><div class="left-img"><a href="'+data[i].url+'" target="_blank">'
				if(data[i].attr&&data[i].attr.xinwenliebiaotu&&data[i].attr.xinwenliebiaotu.resUrl){
					html +='<img src="'+data[i].attr.xinwenliebiaotu.resUrl+'">'
				}else{
					html += '<img src="">'
				}
				html += '</a></div><div class="right-text Semilight"> <a href="'+data[i].url+'" target="_blank">'+data[i].title+'</a><div><i class="iconfont iconliulang"></i>'+data[i].views+'</div></div></div>'
			}
			$('.right-main-news').html(html)
		}else{
			myMessage.add(result.message, 'error');
		}
	})
}