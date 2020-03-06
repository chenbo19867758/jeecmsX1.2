$(function(){
	// 头部悬浮定位
	var sTop = 0;
	$(window).scroll(function(){
	    sTop = $(this).scrollTop();
		if(sTop > 0){
	        $(".header").css({"position":"fixed","top":"0"});
		}else{
			$(".header").css({"position":"relative"});
	    }
	});
	// 展开搜索栏
	$('.search').on('click',function(){
	    $('.search-show').slideDown(500);
	    $('.barrierBed').removeClass("none");
	});
	// 收起搜索栏
	$('.iconguanbi').on('click',function(){
	    $('.search-show').slideUp(500);
	    $('.barrierBed').addClass("none");
	});
	// 展开移动端二维码
	$('.mobile').on('mouseenter',function(){
	    $('.mobile-show').stop().slideDown(500);
	    $('.mobile .icondaohanjiantousvg1').addClass('none');
	    $('.mobile .icondaohanjiantousvg').removeClass('none');
	});
	// 收起移动端二维码
	$('.mobile').on('mouseleave',function(){
	    $('.mobile-show').stop().slideUp(500);
	    $('.icondaohanjiantousvg').addClass('none');
	    $('.icondaohanjiantousvg1').removeClass('none');
	});
	// 展开更换简体繁体
	$('.typeface').on('mouseenter',function(){
	    $('.font-show').stop().slideDown(500);
	    $('.typeface .icondaohanjiantousvg1').addClass('none');
	    $('.typeface .icondaohanjiantousvg').removeClass('none');
	});
	// 收起更换简体繁体
	$('.typeface').on('mouseleave',function(){
	    $('.font-show').stop().slideUp(500);
	    $('.typeface .icondaohanjiantousvg').addClass('none');
	    $('.typeface .icondaohanjiantousvg1').removeClass('none');
	});
	
	

	var fontClass = null
	// 字体移入变Bold
	$('.hover-Bold').on('mouseenter',function(){
		if(($(this).hasClass("Regular"))){
			$(this).removeClass('Regular');
			fontClass = 'Regular'
		}
		if(($(this).hasClass("Light"))){
			$(this).removeClass('Light');
			fontClass = 'Light'
		}
		if(($(this).hasClass("Semilight"))){
			$(this).removeClass('Semilight');
			fontClass = 'Semilight'
		}
	    $(this).addClass('Bold');
	});
	$('.hover-Bold').on('mouseleave',function(){
		$(this).addClass(fontClass);
		$(this).removeClass('Bold')
	})
	// 字体移入变Light
	$('.hover-Light').on('mouseenter',function(){
		if(($(this).hasClass("Regular"))){
			$(this).removeClass('Regular');
			fontClass = 'Regular'
		}
		if(($(this).hasClass("Bold"))){
			$(this).removeClass('Bold');
			fontClass = 'Bold'
		}
		if(($(this).hasClass("Semilight"))){
			$(this).removeClass('Semilight');
			fontClass = 'Semilight'
		}
	    $(this).addClass('Light');
	});
	$('.hover-Light').on('mouseleave',function(){
		$(this).addClass(fontClass);
		$(this).removeClass('Light')
	})
	// 字体移入变Regular
	$('.hover-Regular').on('mouseenter',function(){
		if(($(this).hasClass("Light"))){
			$(this).removeClass('Light');
			fontClass = 'Light'
		}
		if(($(this).hasClass("Bold"))){
			$(this).removeClass('Bold');
			fontClass = 'Bold'
		}
		if(($(this).hasClass("Semilight"))){
			$(this).removeClass('Semilight');
			fontClass = 'Semilight'
		}
	    $(this).addClass('Regular');
	});
	$('.hover-Regular').on('mouseleave',function(){
		$(this).addClass(fontClass);
		$(this).removeClass('Regular')
	})
	// 字体移入变Semilight
	$('.hover-Semilight').on('mouseenter',function(){
		if(($(this).hasClass("Light"))){
			$(this).removeClass('Light');
			fontClass = 'Light'
		}
		if(($(this).hasClass("Bold"))){
			$(this).removeClass('Bold');
			fontClass = 'Bold'
		}
		if(($(this).hasClass("Regular"))){
			$(this).removeClass('Regular');
			fontClass = 'Regular'
		}
	    $(this).addClass('Semilight');
	});
	$('.hover-Semilight').on('mouseleave',function(){
		$(this).addClass(fontClass);
		$(this).removeClass('Semilight')
	})
})