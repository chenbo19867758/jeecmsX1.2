$('.persondata').on('click', function () {
    $('.one').removeClass('none');
    $('.two').addClass('none');
    $('.three').addClass('none');
    $('.four').addClass('none');
    $('.five').addClass('none');
    $('.person').removeClass('none');
    $('.repairpassword').addClass('none');
    $('.repairEmail').addClass('none');
});
//修改密码切换
$('.repairpassword a').on('click', function () {
    $('.person').removeClass('none');
    $('.repairpassword').addClass('none');
});
//修改注册邮箱切换
$('.second .repair .youx').on('click', function () {
    $('.person').addClass('none');
    $('.repairEmail .before').removeClass('none');
    $('.repairEmail').removeClass('none');
    $('.repairEmail .next').addClass('none');
});
$('.repairEmail .before a').on('click', function () {
    $('.person').removeClass('none');
    $('.repairEmail').addClass('none');
});
$('.repairEmail .next a').on('click', function () {
    $('.repairEmail .before').removeClass('none');
    $('.repairEmail .next').addClass('none');
    $('.person').addClass('none');
});
//我要投稿切换
$('.gocontribute .callback').on('click', function () {
    $('.contri').removeClass('none');
    $('.gocontribute').addClass('none');
});
$('.inter').on('click', function () {
	$('.one').addClass('none');
	$('.two').removeClass('none');
	$('.three').addClass('none');
	$('.four').addClass('none');
	$('.five').addClass('none');
	interactPage=1
	interaction()
});
$('.coll').on('click', function () {
    $('.one').addClass('none');
    $('.two').addClass('none');
    $('.three').removeClass('none');
    $('.four').addClass('none');
    $('.five').addClass('none');
	collectPage = 1
	collection()
});
$('.support').on('click', function () {
    $('.one').addClass('none');
    $('.two').addClass('none');
    $('.three').addClass('none');
    $('.four').removeClass('none');
    $('.five').addClass('none');
	likePage=1
	supports()
});
$('.introc').on('click', function () {
    $('.one').addClass('none');
    $('.two').addClass('none');
    $('.three').addClass('none');
    $('.four').addClass('none');
    $('.five').removeClass('none');
    $('.contri').removeClass('none');
    $('.gocontribute').addClass('none');
	draftPage = 1
	contribution()
});
var $a = $('.data-left>a');
$('.data-left>a').click(function () {
    var ind = $(this).index();
    $a.removeClass('active-red');
    $a[ind].classList.add('active-red');
});
//鼠标放在头像上的效果
$('.upload').on('mouseenter', function () {
    $('.camera').removeClass('none');
    $('.upload-black').removeClass('none');
});
$('.upload').on('mouseleave', function () {
    $('.camera').addClass('none');
    $('.upload-black').addClass('none');
});
//点击图标实现密码显示隐藏
$('.one .repairpassword form .before input').on('focus', function () {
    $('.one .repairpassword form .before .close').removeClass('none');
    $('.one .repairpassword form .before .ifHide .hid').removeClass('none');
});
$('.one .repairpassword form .before input').on('blur', function () {
    $('.one .repairpassword form .before .close').addClass('none');
});
$('.one .repairpassword form .before .ifHide .hid').on('click', function () {
    $('.one .repairpassword form .before .ifHide .look').removeClass('none');
    $('.one .repairpassword form .before .ifHide .hid').addClass('none');
    $('#oldPStr').attr('type', 'text');
});
$('.one .repairpassword form .before .ifHide .look').on('click', function () {
    $('.one .repairpassword form .before .ifHide .hid').removeClass('none');
    $('.one .repairpassword form .before .ifHide .look').addClass('none');
    $('#oldPStr').attr('type', 'password');
});

$('form input').on('focus', function () {
    $(this).css('border-color', '#666666');
});
$('form input').on('blur', function () {
    $(this).css('border-color', '#E5E5E5');
});
$('textarea').on('focus', function () {
    $(this).css('border-color', '#666666');
});
$('textarea').on('blur', function () {
    $(this).css('border-color', '#E5E5E5');
});
//修改个人资料-性别
$('#radio_s1JAwa').on('click',function(){
    $('#radio_s1JAwa').prop('checked',false);
    $(this).prop('checked',true);
});

//为了ie8也有placeholder的效果（用JS模仿）
if (!('placeholder' in document.createElement('input'))) {
    // 匹配 除type=password以外所有input、textarea
    $('input[placeholder][type!=password],textarea[placeholder]').each(function () {
        var self = $(this),
            text = self.attr('placeholder');
        // 如果内容为空，则写入
        if (self.val() === "") {
            self.val(text).addClass('placeholder');
        }
        // 控件激活，清空placeholder
        self.focus(function () {
            if (self.val() === text) {
                self.val("").removeClass('placeholder');
            }
            // 控件失去焦点，清空placeholder
        }).blur(function () {
            if (self.val() === "") {
                self.val(text).addClass('placeholder');
            }
        });
    });
}