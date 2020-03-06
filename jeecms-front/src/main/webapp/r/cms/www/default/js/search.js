var listData = []
var page = 1
var last = false
var oderBy = ''
var channelIds = ''
var releaseTimeBegin = ''
var releaseTimeEnd = ''
var assign = false
$('.noSearch').hide()
// 搜索
function search(){
    if(!last){
        if(assign){
            if($('#startTime').val() && $('#endTime').val()){
                releaseTimeBegin = $('#startTime').val()
                releaseTimeEnd = $('#endTime').val()
            }else{
                myMessage.add('请选择时间', 'warning');
                return 
            }
        }
        api.GET('/content/search',{
            "siteId": "",
            "channelIds": channelIds,
            "searchPos": "",
            "keyCondition": "",
            "keyword": $('#keyword').val(),
            "typeIds": "",
            "tagIds": "",
            "timeStage": "",
            "releaseTimeBegin": releaseTimeBegin,
            "releaseTimeEnd": releaseTimeEnd,
            "issueOrg": "",
            "issueYear": "",
            "issueNum": "",
            "isTop": "",
            "releaseApp": "",
            "releaseMiniprogram": "",
            "oderBy": oderBy,
            "page": page,
            "size": 20
        },function(data){
            if(data.content.length>0){
                $('.noSearch').hide()
                $('#totalElements').html(data.totalElements)
                $('#keywords').text($('#keyword').val())
                $('.search-res').show()
                listData = data.content
                page +=1
                last = data.last
                if(last){
                   $('.loading').text('已显示全部');
				   $('.loading').removeClass('loading-yes')
                   $('.loading').addClass('loading-no')
				   $('.loading').removeClass('pointer')
                }else{
					$('.loading').addClass('pointer')
                    $('.loading').text('加载更多')
					$('.loading').removeClass('loading-no')
					$('.loading').addClass('loading-yes')
                }
                $('.loading').show()
                for(var i=0;i<listData.length;i++){
					var html = '<div class="search-item overflow"><div class="item-left fl">'
					if(listData[i].contentFrontVo.modelId==912 && listData[i].contentFrontVo.imageJson.xinwenliebiaotu){//新闻
						html+='<img src="' +listData[i].contentFrontVo.imageJson.xinwenliebiaotu.url +'" alt="">'
					}else if(listData[i].contentFrontVo.modelId==913 && listData[i].contentFrontVo.imageJson.listImg){//图片
						html+='<img src="' +listData[i].contentFrontVo.imageJson.listImg.url +'" alt="">'
					}else if(listData[i].contentFrontVo.modelId==961 && listData[i].contentFrontVo.imageJson.wenkuliebiaotu){//文库
						html+='<img src="' +listData[i].contentFrontVo.imageJson.wenkuliebiaotu.url +'" alt="">'
					}else if(listData[i].contentFrontVo.modelId==963 && listData[i].contentFrontVo.imageJson.shipingliebiaotu){//视频
						html+='<img src="' +listData[i].contentFrontVo.imageJson.shipingliebiaotu.url +'" alt="">'
					}else if(listData[i].contentFrontVo.modelId==962 && listData[i].contentFrontVo.imageJson.xiazailiebiaotu){//下载
						html+='<img src="' +listData[i].contentFrontVo.imageJson.xiazailiebiaotu.url +'" alt="">'
					}else{
						html+='<img src="" alt="">'
					}
					html += '</div><div class="item-right fl"><a href="'+listData[i].url+'" class="Regular keywords">'+listData[i].title+'</a><div class="bottom Semilight hover"><ul><li>'
					if(listData[i].source && listData[i].source.sourceName){
						html += listData[i].source.sourceName
					}else{
						html += 'JeeCms演示站'
					}
					html += '</li><li class="time no-red">'+listData[i].releaseTime+'</li></ul><ul><li class="no-red"><i class="iconfont iconliulang"></i>'+listData[i].views+'</li><li><i class="iconfont iconthumb-up-line"></i>'+listData[i].ups+'</li><li><i class="iconfont iconmessage--line1"></i>'+listData[i].comments+'</li></ul></div><div class="addone none">+1</div></div></div>'
					var searchRow = $(html)
                    searchRow.appendTo($('#searchList'))
                }
				
            }else{
                $('.loading').hide();
                $('.noSearch').show();
                $('#keywords').text($('#keyword').val());
                $('#totalElements').text('0');
            }
            $('.keywords').highlight($('#keyword').val());
        })
    }else{
        myMessage.add('已经最后一页了', 'warning');
    }
}

// 搜索按钮
function toSearch(){
    page =1
    last = false
    $('#searchList').empty();
    search()
	getChannelList()
}
$("#keyword").keypress(function (e) {
    if (e.which == 13) {
        toSearch()
    }
});

function upHot(hot){
    $('#keyword').val(hot)
    toSearch()
}

$('.rank').click(function(){
    $('.rank').removeClass("active-red")
    this.classList.add("active-red")
    if($(this).index()==1){
        oderBy=''
    }else{
        oderBy=27
    }
    page =1
    last = false
    $('#searchList').empty();
    search()
	getChannelList()
})

$('.selectTime').click(function(){
    $('.selectTime').removeClass("active-red")
    $('.assignTime').removeClass("active-red")
    $('#date').val("")
    this.classList.add("active-red")
    if($(this).index()==1){
        releaseTimeBegin=''
        releaseTimeEnd=''
    }else if($(this).index()==2){
        releaseTimeBegin=getData(1)
        releaseTimeEnd=getData(0)
    }else if($(this).index()==3){
        releaseTimeBegin=getData(7)
        releaseTimeEnd=getData(0)
    }else if($(this).index()==4){
        releaseTimeBegin=getData(30)
        releaseTimeEnd=getData(0)
    }else if($(this).index()==5){
        releaseTimeBegin=getData(365)
        releaseTimeEnd=getData(0)
    }
    assign=false
    page =1
    last = false
    $('#searchList').empty();
    search()
	getChannelList()
})

$('.assignTime').click(function(){
    $('.selectTime').removeClass("active-red")
    this.classList.add("active-red")
})




function getChannelList(){
    api.GET('/content/searchSummary',{
        "siteId": "",
		"channelIds": channelIds,
		"searchPos": "",
		"keyCondition": "",
		"keyword": $('#keyword').val(),
		"typeIds": "",
		"tagIds": "",
		"timeStage": "",
		"releaseTimeBegin": releaseTimeBegin,
		"releaseTimeEnd": releaseTimeEnd,
		"issueOrg": "",
		"issueYear": "",
		"issueNum": "",
		"isTop": "",
		"releaseApp": "",
		"releaseMiniprogram": "",
		"oderBy": oderBy,
    },function(data){
        if(data.result.length>0){
			var html=''
            for(var i=0;i<data.result.length;i++){
                html += '<li class="tree-set"><a onclick="getChannelId('+data.result[i].channel.id+')">'+data.result[i].channel.name+" "+data.result[i].count+'</a></li>'		}
			$('.tree').html(html)
            $(".tree").treemenu({delay:300}).openActive();
			$('.search-left .menu').show();
        }else{
            $('.search-left .menu').hide();
        }
    })
}
$(document).ready(function(){ 
    $('.tree-set').click(function(){
        $('.tree-set').removeClass("active-red")
        this.classList.add("active-red")
    })
}); 


function getChannelId(id){
    channelIds=id
    page =1
    last = false
    $('#searchList').empty();
    search()
}

$(function(){
    if(getUrlArg('keyword')){
        $('#keyword').val(decodeURI(getUrlArg('keyword')))
        search()
		getChannelList()
    }else{
		search()
		getChannelList()
	}
    
    
    if( !('placeholder' in document.createElement('input')) ){
        // 匹配 除type=password以外所有input、textarea
        $('input[placeholder][type!=password],textarea[placeholder]').each(function(){   
            var self = $(this),   
            text= self.attr('placeholder');
            // 如果内容为空，则写入
            if(self.val()===""){ 
                self.val(text).addClass('placeholder');
            }
            // 控件激活，清空placeholder
            self.focus(function(){
                if(self.val()===text){
                    self.val("").removeClass('placeholder');
                }
            // 控件失去焦点，清空placeholder
            }).blur(function(){
                if(self.val()===""){
                    self.val(text).addClass('placeholder');
                }
            });            
        });   
    }
});


// 获取日期
function getData (dayNum) {
    var myDate = new Date()
    var lw = new Date(myDate - 1000 * 60 * 60 * 24 * dayNum)// 最后一个数字多少天前的意思
    var lastY = lw.getFullYear()
    var lastM = lw.getMonth() + 1
    var lastD = lw.getDate()
    var startdate = lastY + '-' + (lastM < 10 ? '0' + lastM : lastM) + '-' + (lastD < 10 ? '0' + lastD : lastD)
    return startdate
  }
// 树形点击
$(document).on('click', '.tree-empty a', function() {
	$('.tree-empty a').removeClass('ontree')
	this.classList.add("ontree");
})