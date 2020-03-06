var page = 1
var last = false
var toloading = false
function loading(id){
	id = id
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
				last = res.data.last
				var data = res.data.content
				for(var i=0;i<data.length;i++){
					var div = $('<div class="newslist-item overflow"></div>')
					div.html($('.newslist-item').html())
					div.children(".item-left").children("a").children("img").attr('src', data[i].iconUrl)
					div.children('.item-right').children(".big-title").children('a').attr('href',data[i].url)
					div.children('.item-right').children(".big-title").children('a').text(data[i].title)
					div.children('.item-right').children('.time').children('.timeTitle').text(data[i].channelName)
					div.children('.item-right').children('.time').children('.timeData').text(data[i].releaseTime)
					div.children('.item-right').children('.tag').children('div').eq(1).children('span').text(data[i].views)
					div.children('.item-right').children('.tag').children('div').eq(2).children('span').text(data[i].ups)
					div.children('.item-right').children('.tag').children('div').eq(3).children('span').text(data[i].comments)
					div.children('.item-right').children('p').text(data[i].description)
					$('.loading').before(div)
				}
				toloading = false
				if(last){
					$('#upLoading').text('没有更多图片新闻了')
					$('#upLoading').css('background-color','#fff');
					$('#upLoading').removeClass('loading').addClass('none-loading');
				}else{
					$('#upLoading').text('加载更多')
				}
			}else{
				myMessage.add(result.message, 'error');
			}
		})
	}else if(last){
		myMessage.add('没有更多图片新闻了', 'warning');
	}
}

function getContentPage(){
	
}