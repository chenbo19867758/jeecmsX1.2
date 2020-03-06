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
				last=res.data.last
				var data = res.data.content
				for(var i=0;i<data.length;i++){
					var div = $('<div class="video-item"></div>')
					div.html($('.video-item').html())
					div.children("a").attr('href',data[i].url)
					div.children("a").children("img").attr('src', data[i].iconUrl)
					div.children('.video-title').children('a').attr('href',data[i].url)
					div.children('.video-title').children('a').text(data[i].title)
					div.children('.small').children('.small-left').text(data[i].releaseTime)
					div.children('.small').children('.small-right').children('.icon-item').eq(1).children('span').text(data[i].views)
					div.children('.small').children('.small-right').children('.icon-item').eq(1).children('span').text(data[i].ups)
					div.children('.small').children('.small-right').children('.icon-item').eq(1).children('span').text(data[i].comments)
					div.children('.player-time').children('span').text(data[i].resDuration)
					$('.loading').before(div)
				}
				toloading = false
				if(last){
					$('#upLoading').text('没有更多视频了')
					$('#upLoading').css('background-color','#fff')
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

function getContentPage(){
	
}