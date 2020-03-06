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
				last = res.data.last
				var data = res.data.content
				for(var i=0;i<data.length;i++){
					var div = $('<li class="docList"></li>')
					div.html($('.docList').html())
					div.children(".image").children("a").attr('href',data[i].url)
					if(data[i].attr&&data[i].attr.wenkuliebiaotu&&data[i].attr.wenkuliebiaotu.resUrl){
						div.children(".image").children("a").children("img").attr('src', data[i].attr.wenkuliebiaotu.resUrl)
					}else{
						div.children(".image").children("a").children("img").attr('src', '')
					}
					
					div.children('.rest-box').children('.rest-title').children('a').attr('href',data[i].url)
					div.children('.rest-box').children('.rest-title').children('a').text(data[i].title)
					div.children('.rest-box').children('.rest-text').text(data[i].description)
					div.children('.rest-box').children('.rest-bottom').children('.rest-time').children('span').text(data[i].releaseTime)
					div.children('.rest-box').children('.rest-bottom').children('.rest-icon').children('.browse').children('.browseText').text(data[i].views)
					div.children('.rest-box').children('.rest-bottom').children('.rest-icon').children('.download').children('.downloadText').text(data[i].downloads)
					$('#docList').append(div)
				}
				toloading = false
				if(last){
					$('#upLoading').text('没有更多文库了')
					$('#upLoading').css('background-color','#fff');
					$('#upLoading').removeClass('loadings').addClass('none-loading');
				}else{
					$('#upLoading').text('加载更多')
				}
			}else{
				myMessage.add(result.message, 'error');
			}
		})
	}else if(last){
		myMessage.add('没有更多文库了', 'warning');
	}
}
