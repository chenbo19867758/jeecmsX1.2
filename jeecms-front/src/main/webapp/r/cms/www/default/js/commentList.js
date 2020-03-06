$(function() {
	// 提交成功
	$('.comment-input a').on('click', function() {
		$('.comment-input .success').slideDown(200);
		$('.comment-input .success').show();
		setTimeout(function() {
			$('.comment-input .success').hide();
		}, 1000);
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

var contentId = ''
var allcomment = []
var sortStatus = 1
var page = 1
var size = 10
var last = false

$('#lookingAll').on('click', function() {
	if(last){
		myMessage.add('已经最后一页了', 'warning');
	}else{
		page+=1
		getAllcomment()
	}
	
});

function getCommentList(id) {
	contentId = id
	getAllcomment()
}

function getAllcomment() {
	api.GET('/usercomment', {
		"contentId": contentId,
		"sortStatus": sortStatus,
		"page": page,
		"size": size
	}, function(data) {
		if (data.code === 200) {
			allcomment = data.data.page.content
			last = data.data.page.last
			if(last){
				$('#lookingAll a').text('没有更多评论了')
				$('#lookingAll').removeClass('pointer')
				$('#lookingAll').removeClass('hover')
				$('#lookingAll').removeClass('lookingAll')
				$('#lookingAll').addClass('lookingNo')
			}else{
				$('#lookingAll a').text('查看更多评论')
				$('#lookingAll').addClass('pointer')
				$('#lookingAll').addClass('hover')
				$('#lookingAll').addClass('lookingAll')
				$('#lookingAll').removeClass('lookingNo')
			}
			produceList()
		} else {
			myMessage.add(data.message, 'warning');
		}
	})
}

$('#onNew').on('click', function() {
	$('#onHot').removeClass("onspan")
	$('#onNew').addClass("onspan")
	sortStatus = 2
	page = 1
	getAllcomment()
});

$('#onHot').on('click', function() {
	$('#onNew').removeClass("onspan")
	$('#onHot').addClass("onspan")
	sortStatus = 1
	page = 1
	getAllcomment()
});


function produceList() {
	var html = ''
	for (var j = 0; j < allcomment.length; j++) {
		html += '<div class="clearfix comment-list"><div class="user-icon pointer fl">'
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
						html += '<a class="list-title block overfow-text Semilight"><span class="Regular">' + allcomment[j].children[i].user.username +'</span> 回复 <span class="Regular">'+ allcomment[j].children[i].replyUser.username +'</span>：' + allcomment[j].children[i].commentText
					}
				} else {
					if(allcomment[j].children[i].replyParent){
						html += '<a class="list-title block overfow-text Semilight">' + '<span class="Regular">匿名</span>' + '：' + allcomment[j].children[i].commentText
					}else{
						html += '<a class="list-title block overfow-text Semilight">' + '<span class="Regular">匿名</span>' +' 回复 <span class="Regular">'+ allcomment[j].children[i].replyUser.username +'</span>：' + allcomment[j].children[i].commentText
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
			allcomment[j].id + '" class="Semilight reply block">回复</a></div></div></div></div>'
	}
	
	$('.hot-comment').append(html)
}

// 举报
function report(id) {
	api.POST('/usercomment/report', {
		commentId: id
	}, function(data) {
		if (data.code == 200) {
			myMessage.add('举报成功', 'success');
			getAllcomment()
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
				getAllcomment()
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
				getAllcomment()
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
				getAllcomment()
			} else {
				myMessage.add(data.message, 'error');
			}
		})
	} else {
		myMessage.add('请先输入评论', 'warning');
	}

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

// 点赞
function commentPraise(id) {
	api.POST('/usercomment/up', {
		"commentId": id
	}, function(data) {
		if (data.code == 200) {
			myMessage.add('点赞成功', 'success');
			getAllcomment()
		} else {
			myMessage.add(data.message, 'error');
		}
	})
}
