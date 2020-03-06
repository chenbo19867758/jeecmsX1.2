$(function () {
    //验证码
    var code;
    function createCode() {
        code = '';
        var codeLength = 4;
        var codeV = $(".letter");
        var arr = new Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
        for (var i = 0; i < codeLength; i++) {
            var index = Math.floor(Math.random() * 26);
            code += arr[index];
        }
        codeV.text(code);
    }
    // createCode();
    // $('.letter').on('click', function () {
    //     createCode();
    // });
    $('.formtable input').on('focus', function () {
        $(this).css('border-color', '#666666');
        $('.username p').css('display', 'none');
        $('.psword p').css('display', 'none');
    });
    $('.formtable input').on('blur', function () {
        $(this).css('border-color', '#E5E5E5');
    });
    $('.formtable .psword .close').on('click', function () {
        $('.formtable .psword .look').removeClass('none');
        $('.formtable .psword .close').addClass('none');
        $('.formtable .psword input').attr('type', 'text');
    });
    $('.formtable .psword .look').on('click', function () {
        $('.formtable .psword .close').removeClass('none');
        $('.formtable .psword .look').addClass('none');
        $('.formtable .psword input').attr('type', 'password');
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
});