$(function(){
    //点赞加1
    $('.iconthumb-up-line').on('click',function(){
        $('.item-right addone').removeClass('none');
    });
});
var page = 1
var last = false
var toloading = false
function loading(id){
	if(!last && !toloading){
		page+=1
		toloading = true
		$('#upLoading').text('加载中...')
		api.GET('/content/page',{
			"contentId": "",
			"channelIds": id,
			"tagIds": "",
			"channelPaths": "",
			"siteIds": "",
			"typeIds": "",
			"title": "",
			"isNew": "",
			"isTop": "",
			"timeBegin": "",
			"timeEnd": "",
			"excludeId": "",
			"orderBy": "",
			"page": page,
			"size": 20,
			"releaseTarget": ""
		},function(res){
			if(res.code===200){
				var data = res.data.content
				last=res.data.last
				for(var i=0;i<data.length;i++){
					var div = $('<div class="item"></div>')
					div.html($('.item').html())
					if(data[i].attr&&data[i].attr.xinwenliebiaotu&&data[i].attr.xinwenliebiaotu.resUrl){
						div.children(".item-left").children("img").attr('src', data[i].attr.xinwenliebiaotu.resUrl)
					}else{
						div.children(".item-left").children("img").attr('src', '')
					}
					
					div.children('.item-right').children('a').attr('href',data[i].url)
					div.children('.item-right').children('a').text(data[i].title)
					div.children('.item-right').children('div').children('ul').children('li').eq(0).html(data[i].sourceName)
					div.children('.item-right').children('div').children('ul').children('li').eq(1).html(data[i].releaseTimeString)
					div.children('.item-right').children('div').children('ul').children('li').eq(2).children('span').html(data[i].comments)
					div.children('.item-right').children('div').children('ul').children('li').eq(3).children('span').html(data[i].ups)
					div.children('.item-right').children('div').children('ul').children('li').eq(4).children('span').html(data[i].views)
					$('.loading').before(div)
				}
				toloading = false
				if(last){
					$('#upLoading').text('没有更多新闻了');
					$('#upLoading').css({'background-color':'#fff','margin-top':'-9px'});
					$('#upLoading').removeClass('loading').addClass('none-loading');
				}else{
					$('#upLoading').text('加载更多')
				}
			}else{
				myMessage.add(result.message, 'error');
			}
		})
	}else if(last){
		myMessage.add('没有更多新闻了', 'warning');
	}
}

var batchPage = 1
var batchLast = false
function inabatch(id){
	if(batchPage<5 && !batchLast){
		batchPage += 1
	}else{
		batchPage = 1
	}
	api.GET('/content/page',{
		"contentId": "",
		"channelIds": id,
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
			batchLast = res.data.last
			var data = res.data.content
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
