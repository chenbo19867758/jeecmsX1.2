$(function(){
    //图片模块----左
    $('.images-box .zuo .zuo-img1').on('mouseenter',function(){
		$(this).children(".text").stop().slideDown(800);
        $(this).children(".text").removeClass('none');
    });
    $('.images-box .zuo .zuo-img1').on('mouseleave',function(){
        $(this).children(".text").stop().slideUp(800);
    });
    $('.images-box .zuo .zuo-img2').on('mouseenter',function(){
        $('.images-box .zuo .zuo-img2 .text').stop().slideDown(800);
        $('.images-box .zuo .zuo-img2 .text').removeClass('none');
    });
    $('.images-box .zuo .zuo-img2').on('mouseleave',function(){
        $('.images-box .zuo .zuo-img2 .text').stop().slideUp(800);
    });
    //图片模块----中
    $('.images-box .zhong .zhong-img').on('mouseenter',function(){
        $('.images-box .zhong .zhong-img .text').stop().slideDown(800);
        $('.images-box .zhong .zhong-img .text').removeClass('none');
    });
    $('.images-box .zhong .zhong-img').on('mouseleave',function(){
        $('.images-box .zhong .zhong-img .text').stop().slideUp(800);
    });
    //图片模块----右
    $('.images-box .you .you-img1').on('mouseenter',function(){
        $(this).children(".text").stop().slideDown(800);
        $(this).children(".text").removeClass('none');
    });
    $('.images-box .you .you-img1').on('mouseleave',function(){
        $(this).children(".text").stop().slideUp(800);
    });
    $('.images-box .you .you-img2').on('mouseenter',function(){
        $('.images-box .you .you-img2 .text').stop().slideDown(800);
        $('.images-box .you .you-img2 .text').removeClass('none');
    });
    $('.images-box .you .you-img2').on('mouseleave',function(){
        $('.images-box .you .you-img2 .text').stop().slideUp(800);
    });
    //为了ie8也有placeholder的效果（用JS模仿）
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

var pages = 1
$('.slide .right').addClass('hoverED');
function hotPrevious(){
	if(pages>1){
		pages-=1
        $('.slide .right i').css('color','#333');
        getContentPage()
		if(pages==1){
            $('.slide .left i').css('color','#999');
            $('.slide .left').removeClass('hoverED');
        }else{
            $('.slide .left i').css('color','#333');
            $('.slide .left').addClass('hoverED');
            $('.slide .right').addClass('hoverED');
        }
	}else{
        $('.slide .left i').css('color','#999');
        
    }
}
function hotNextPage(){
	if(pages<4){
		pages+=1
		$('.slide .left i').css('color','#333');
		getContentPage()
		if(pages==4){
            $('.slide .right i').css('color','#999');
            $('.slide .left').addClass('hoverED');
            $('.slide .right').removeClass('hoverED');
		}else{
            $('.slide .right i').css('color','#333');
            $('.slide .left').addClass('hoverED');
		}
	}else{
        $('.slide .right i').css('color','#999');
        $('.slide .right').css('background-color','#F5F5F5');
    }
    
	
}


function getContentPage(){
	var data={
		"contentId": "",
		  "channelIds": "",
		  "tagIds|": "",
		  "channelPaths": "news",
		  "siteIds": 1,
		  "typeIds": "",
		  "title": "",
		  "isNew": "",
		  "isTop": "",
		  "timeBegin": "",
		  "timeEnd": "",
		  "excludeId": "",
		  "orderBy": 3,
		  "page": pages,
		  "size": 3,
		  "releaseTarget": ""
		}
	api.GET('/content/page',data,function(res){
		if(res.code===200){
			var data = res.data.content
			var html = ''
			for(var i=0;i<data.length;i++){
				if(pages<=1){
                    html+='<li>'
                    html+='<span class="num Bold fl">'+((pages-1)*3+i+1)+'</span>'
                    html+='<a class="rank-img fl" href="'+data[i].url+'">'
					if(data[i].attr&&data[i].attr.xinwenliebiaotu&&data[i].attr.xinwenliebiaotu.resUrl){
						html+='<img class="img-scale" src="'+data[i].attr.xinwenliebiaotu.resUrl+'" alt="">'
					}else{
						html+='<img class="img-scale" src="'+data[i].iconUrl+'" alt="">'
					}
					html+='</a><a href="'+data[i].url+'" class="text Semilight fl">'+data[i].title+'</a>'
                    html+='<span class="read Semilight">'+data[i].views+'人阅读</span></li>'
                }
                else{
                    html+='<li>'
                    html+='<span class="num1 Bold fl">'+((pages-1)*3+i+1)+'</span>'
                    html+='<a class="rank-img fl" href="'+data[i].url+'">'
					if(data[i].attr&&data[i].attr.xinwenliebiaotu&&data[i].attr.xinwenliebiaotu.resUrl){
						html+='<img class="img-scale" src="'+data[i].attr.xinwenliebiaotu.resUrl+'" alt="">'
					}else{
						html+='<img class="img-scale" src="'+data[i].iconUrl+'" alt="">'
					}
                    html+='</a><a href="'+data[i].url+'" class="text Semilight fl">'+data[i].title+'</a>'
                    html+='<span class="read Semilight">'+data[i].views+'人阅读</span></li>'
                }
			}
			$('#hotList').html(html)
		}else{
			myMessage.add(res.message, 'error');
		}
	})	
}