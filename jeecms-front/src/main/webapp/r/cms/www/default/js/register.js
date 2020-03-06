$(function(){
    $('form input').on('focus',function(){
        $(this).css('border-color','#666666');
    });
    $('form input').on('blur',function(){
        $(this).css('border-color','#E5E5E5');
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