$(function () {
    $('.left-share .share').on('mouseleave', function () {
        $('.left-share i').css('color', '#BBBBBB');
    });
    // 弹窗+遮挡层
    $('.hot-page').on('click', function () {
        $('.hide').show();
        $('.win').show();
    });
    $('.hide').on('click', function () {
        $('.win').hide();
        $('.hide').hide();
    });
    $('.remove').on('click', function () {
        $('.win').hide();
        $('.hide').hide();
    });
});

