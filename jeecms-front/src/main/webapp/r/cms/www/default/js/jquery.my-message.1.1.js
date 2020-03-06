var MyMessage=(function(){function message(setting){this.messageContainer=null;this.opts=null;this._setting(setting);this.init();}
message.DEFAULTS={iconFontSize:"20px",messageFontSize:"20px",showTime:3000,align:"center",positions:{top:"10px",bottom:"10px",right:"10px",left:"10px"},message:"这是一条消息",type:"normal",}
message.prototype._setting=function(setting){this.opts=$.extend({},message.DEFAULTS,setting);}
message.prototype.setting=function(name,val){if("object"===typeof name){for(var k in name){this.opts[k]=name[k]}}else if("string"===typeof name){this.opts[name]=val;}}
message.prototype.init=function(){var domStr="<div class='m-message' style='top:"+
this.opts.positions.top+
";right:"+
this.opts.positions.right+
";left:"+
this.opts.positions.left+
";width:calc(100%-"+
this.opts.positions.right+
this.opts.positions.left+
");bottom:"+this.opts.positions.bottom+
"'></div>"
this.messageContainer=$(domStr);this.messageContainer.appendTo($('body'))}
message.prototype.add=function(message,type){var domStr="";type=type||this.opts.type;domStr+="<div class='c-message-notice' style='"+
"text-align:"+
this.opts.align+
";'><div class='m_content'><i class='";switch(type){case "normal":domStr+="icon-bubble";break;case "success":domStr+="icon-check-alt";break;case "error":domStr+="icon-notification";break;case "warning":domStr+="icon-cancel-circle";break;default:throw "传递的参数type错误，请传递normal/success/error/warning中的一种";break;}
domStr+="' style='font-size:"+
this.opts.iconFontSize+
";'></i><span style='font-size:"+
this.opts.messageFontSize+
";'>"+message+"</span></div></div>";var $domStr=$(domStr).appendTo(this.messageContainer);this._hide($domStr);}
message.prototype._hide=function($domStr){setTimeout(function(){$domStr.fadeOut(1000);},this.opts.showTime);}
return{message:message}})();