$(function(){
    // 提交成功
    $('.comment-input a').on('click',function(){
        $('.comment-input .success').slideDown(200);
        $('.comment-input .success').show();
        setTimeout(function(){
            $('.comment-input .success').hide();
        },1000);
    });
    // 评论回复
    $('.others .sending').on('click',function(){
        $('.others .sending').hide();
        $('.others .back').show();
        $('.send-input').show();
        $('.text-list').css('border-bottom','0');
    });
    // 评论收起
    $('.others .back').on('click',function(){
        $('.others .back').hide();
        $('.others .sending').show();
        $('.send-input').hide();
    });
});