(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-register-register"],{"04d8":function(e,t,a){var i=a("3170");"string"===typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);var s=a("4f06").default;s("3469a849",i,!0,{sourceMap:!1,shadowMode:!1})},1039:function(e,t,a){"use strict";var i=a("04d8"),s=a.n(i);s.a},"120a":function(e,t,a){"use strict";var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("v-uni-view",{staticClass:"content register"},[a("v-uni-input",{staticStyle:{position:"absolute","z-index":"-1",width:"0",opacity:"0"},attrs:{type:"password"}}),a("v-uni-input",{staticStyle:{position:"absolute","z-index":"-1",width:"0",opacity:"0"},attrs:{type:"text"}}),a("page-head",{attrs:{title:"登录"},on:{click:function(t){t=e.$handleEvent(t),e.handleLogin(t)}}}),a("v-uni-view",{staticClass:"form-wrap"},[a("v-uni-view",{staticClass:"title Medium"},[e._v("注册")]),e._l(e.list,function(t,i){return a("v-uni-view",{key:i,staticClass:"box"},[a("v-uni-view",{staticClass:"wrap"},[a("v-uni-view",{staticClass:"top"},[t.value.isRequired?a("v-uni-view",{staticClass:"required"},[e._v("*")]):e._e(),"password"==t.value.name?a("v-uni-input",{staticClass:"inputbox",attrs:{type:"password",placeholder:t.value.label,id:t.value.name,maxlength:t.value.max>=0&&t.value.isLengthLimit?t.value.max:""},model:{value:e.form.passwords,callback:function(t){e.$set(e.form,"passwords",t)},expression:"form.passwords"}}):a("v-uni-input",{staticClass:"inputbox",attrs:{type:"text",placeholder:t.value.label,id:t.value.name,maxlength:t.value.max>=0&&t.value.isLengthLimit?t.value.max:""},model:{value:e.form[t.value.name],callback:function(a){e.$set(e.form,t.value.name,a)},expression:"form[a.value.name]"}})],1),t.value.tip?a("v-uni-view",{staticClass:"text Semilight",domProps:{textContent:e._s(t.value.tip)}}):e._e()],1),"password"===t.value.name?a("v-uni-view",{class:"password"===t.value.name?"wrap more":"wrap"},[a("v-uni-view",{staticClass:"wrap con"},[a("v-uni-view",{staticClass:"top"},[a("v-uni-view",{staticClass:"required"},[e._v("*")]),a("v-uni-input",{staticClass:"inputbox repassword",attrs:{type:"password",autocomplete:"off",placeholder:"重复密码",id:"repassword"},model:{value:e.form.repassword,callback:function(t){e.$set(e.form,"repassword",t)},expression:"form.repassword"}})],1),a("v-uni-view",{staticClass:"text Semilight"},[e._v("请再次输入密码")])],1)],1):"email"==t.value.name&&t.value.isSmsVerification?a("v-uni-view",{staticClass:"wrap",class:"email"===t.value.name?"wrap more":"wrap"},[a("v-uni-view",{staticClass:"wrap con"},[a("v-uni-view",{staticClass:"top"},[a("v-uni-view",{staticClass:"required"},[e._v("*")]),a("v-uni-input",{staticClass:"inputbox email",attrs:{type:"text",autocomplete:"off",placeholder:"邮箱验证码",id:"email",maxlength:"6"},model:{value:e.form.emailCode,callback:function(t){e.$set(e.form,"emailCode",t)},expression:"form.emailCode"}}),a("v-uni-view",{staticClass:"right emailCode Semilight code",on:{click:function(t){t=e.$handleEvent(t),e.getEmailCode(t)}}},[e._v(e._s(e.code))])],1),a("v-uni-view",{staticClass:"text Semilight"},[e._v("请输入邮箱验证码")])],1)],1):"telephone"==t.value.name&&t.value.isSmsVerification?a("v-uni-view",{staticClass:"wrap",class:"telephone"===t.value.name?"wrap more":"wrap"},[a("v-uni-view",{staticClass:"wrap con"},[a("v-uni-view",{staticClass:"top"},[a("v-uni-view",{staticClass:"required"},[e._v("*")]),a("v-uni-input",{staticClass:"inputbox telelphone",attrs:{type:"text",autocomplete:"off",placeholder:"请输入手机验证码",id:"telephone",maxlength:"6"},model:{value:e.form.telephoneCode,callback:function(t){e.$set(e.form,"telephoneCode",t)},expression:"form.telephoneCode"}}),a("v-uni-view",{staticClass:"right telCode Semilight code",on:{click:function(t){t=e.$handleEvent(t),e.getTelephoneCode(t)}}},[e._v(e._s(e.telcode))])],1),a("v-uni-view",{staticClass:"text Semilight"},[e._v("请输入手机验证码")])],1)],1):e._e()],1)}),e.needPic?a("v-uni-view",{staticClass:"box"},[a("v-uni-view",{staticClass:"wrap"},[a("v-uni-view",{staticClass:"top"},[a("v-uni-view",{staticClass:"required"},[e._v("*")]),a("v-uni-input",{staticClass:"inputbox pic",attrs:{type:"text",autocomplete:"off",placeholder:"请输入验证码",id:"captcha"},model:{value:e.form.captcha,callback:function(t){e.$set(e.form,"captcha",t)},expression:"form.captcha"}}),a("v-uni-view",{staticClass:"right telCode Semilight code",on:{click:function(t){t=e.$handleEvent(t),e.genCapatch(t)}}},[a("img",{staticClass:"image",attrs:{src:e.captchaSrc,mode:""}})])],1)],1),a("v-uni-view",{staticClass:"text Semilight"})],1):e._e(),a("v-uni-view",{staticClass:"agreement Regular"},[a("v-uni-radio-group",{on:{change:function(t){t=e.$handleEvent(t),e.radioChange(t)}}},[a("v-uni-label",[a("v-uni-radio",{staticClass:"radio",staticStyle:{transform:"scale(0.6)"},attrs:{color:"#e30b20",value:"1",checked:1==e.agreement}}),a("v-uni-text",[e._v("我已阅读并接受"),a("v-uni-text",{staticStyle:{color:"#2676B5"},on:{click:function(t){t=e.$handleEvent(t),e.toAgreement(t)}}},[e._v("《JEECMS注册协议》")])],1)],1)],1)],1),a("v-uni-view",{staticClass:"signIn",style:{"background-color":1==e.agreement?"#D20505":"#E6E6E6"},on:{click:function(t){t=e.$handleEvent(t),e.handleSubmit()}}},[e._v("注册")])],2)],1)},s=[];a.d(t,"a",function(){return i}),a.d(t,"b",function(){return s})},"1af3":function(e,t,a){"use strict";var i=a("288e");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0,a("7f7f"),a("3b2b");var s=i(a("bd86")),n=a("c9ca"),o={name:"register",data:function(){var e;return e={showPassword:!0,showRePassword:!0,captchaSrc:"",time1:60,timer1:null,time2:60,codeLoading:!1,checked:!0,isDisabled:!0,list:[],form:{username:"",passwords:"",password:"",repassword:"",email:"",emailCode:"",telephone:"",telephoneCode:"",captcha:"",sessionId:""},needPic:!1,code:"",isRead:!1,rules:{email:[this.$rules.email()],telephone:[this.$rules.mobile()],repassword:[this.$rules.required("请输入重复密码")]},messages:{}},(0,s.default)(e,"code","获取验证码"),(0,s.default)(e,"telcode","获取验证码"),(0,s.default)(e,"agreement",0),e},mounted:function(){this.getModel(),this.judgeCapatch()},computed:{},methods:{getModel:function(){var e=this;this.$request.getModel({}).then(function(t){e.list=t.data.enableJson.formListBase,e.putRulse()})},getEmailCode:function(){var e=this,t=new RegExp(/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/);if(t.test(this.form.email)){if(60==this.time1){var a={type:1,targetNumber:this.form.email};this.$request.getEmailCode(a).then(function(t){200==t.code?t.data&&(e.code=e.time1,e.timer1||(e.code=TIME_COUNT,e.timer1=setInterval(function(){e.code>0?(e.time1--,e.code="重新发送("+e.time1+")"):(e.time1=60,clearInterval(e.timer1),e.timer1=null,e.code="获取验证码")},1e3))):e.$message(t.message)})}}else this.$message("请输入正确的邮箱")},getTelephoneCode:function(){var e=this,t=new RegExp(/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[2|6|7])|(18[0-9])|(17([0-1]|[3]|[5-8]))|(19[1|8|9]))\d{8}$/);if(t.test(this.form.telephone)){if(60==this.time2){var a={type:1,targetNumber:this.form.telephone};this.$request.getTelephoneCode(a).then(function(t){if(200==t.code){if(t.data){e.telcode=e.time2;e.timer2||(e.timer2=setInterval(function(){e.telcode>0?(e.time2--,e.telcode="重新发送("+e.time2+")"):(e.time2=60,clearInterval(e.timer2),e.timer2=null,e.telcode="获取验证码")},1e3))}}else e.$message(t.message)})}}else this.$message("请输入正确的手机号")},judgeCapatch:function(){var e=this;this.$request.judgeCapatch({}).then(function(t){e.needPic=t.data.MemberRegisterCaptcha,e.needPic&&e.genCapatch()})},genCapatch:function(){var e=this;this.$request.genCapatch({}).then(function(t){200==t.code?(e.captchaSrc="data:image/png;base64,"+t.data.img,e.form.sessionId=t.data.sessionId):e.$message(t.message)})},handleSubmit:function(){var e=this;1==this.agreement?this.form.passwords===this.form.repassword?this.$rules.validator(this.form,this.rules).then(function(){e.form.password=(0,n.desEncrypt)(e.form.passwords),e.$request.fetchRegister(e.form).then(function(t){200==t.code?(e.$message("注册成功"),uni.navigateTo({url:"/pages/login/login"})):e.$message(t.message)})}).catch(this.$message):this.$message("密码和重复密码要相同"):this.$message("请先阅读并同意注册协议")},radioChange:function(e){this.agreement=e.target.value},putRulse:function(){for(var e=this.list,t=0;t<e.length;t++){var a=e[t].value;if(a.isRequired){var i=[this.$rules.required("请输入"+a.label)];"password"==a.name?this.rules.passwords=[this.$rules.required("请重复输入密码")]:this.rules[a.name]instanceof Array?this.rules[a.name]=this.rules[a.name].concat(i):this.rules[a.name]=i}if(a.inputLimit){var s=[this.$rules[a.inputLimit]()];this.rules[a.name]instanceof Array?this.rules[a.name]=this.rules[a.name].concat(s):this.rules[a.name]=s}"telephone"==a.name&&a.isSmsVerification&&(this.rules.telephoneCode=[this.$rules.required("请输入手机验证码")]),"email"==a.name&&a.isSmsVerification&&(this.rules.emailCode=[this.$rules.required("请输入邮箱验证码")])}},handleLogin:function(){uni.navigateTo({url:"/pages/login/login"})},toAgreement:function(){uni.navigateTo({url:"/pages/register/agreement"})}}};t.default=o},3170:function(e,t,a){t=e.exports=a("2350")(!1),t.push([e.i,'@charset "UTF-8";\n/**\r\n * 这里是uni-app内置的常用样式变量\r\n *\r\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\r\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\r\n *\r\n */\n/**\r\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\r\n *\r\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\r\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */.content.register .form-wrap[data-v-1da8ac89]{padding:0 %?115?%}.content.register .form-wrap .title[data-v-1da8ac89]{padding-top:%?60?%;font-size:%?50?%;font-family:PingFang SC;color:#333;padding-bottom:%?58?%}.content.register .form-wrap .wrap[data-v-1da8ac89]{position:relative}.content.register .form-wrap .wrap .top[data-v-1da8ac89]{border-bottom:%?1?% solid #f0f0f0}.content.register .form-wrap .wrap .top .code[data-v-1da8ac89]{position:absolute;right:0;top:%?12?%;width:%?184?%;height:%?60?%;background:#3b3b3b;border-radius:%?10?%;font-size:%?22?%;font-family:PingFang SC;color:#fff;text-align:center;line-height:%?60?%}.content.register .form-wrap .wrap .top .code .image[data-v-1da8ac89]{position:absolute;right:0}.content.register .form-wrap .wrap .top .required[data-v-1da8ac89]{top:%?30?%;position:absolute;left:0;font-size:%?28?%;font-family:PingFang SC;color:#e30b20}.content.register .form-wrap .wrap .top .inputbox[data-v-1da8ac89]{height:%?90?%;font-size:%?28?%;font-family:PingFang SC;color:#999;border:none;padding-left:%?20?%}.content.register .form-wrap .wrap .top .icon[data-v-1da8ac89]{position:absolute;top:%?63?%;right:0}.content.register .form-wrap .wrap .top .icon .icon-clear[data-v-1da8ac89]{width:%?32?%;height:%?32?%}.content.register .form-wrap .wrap .top.red[data-v-1da8ac89]{border-bottom:%?1?% solid #e30b20}.content.register .form-wrap .wrap .text[data-v-1da8ac89]{display:block;margin-top:%?19?%;font-size:%?22?%;font-family:PingFang SC;color:#b3b3b3}.content.register .form-wrap .wrap .text.red[data-v-1da8ac89]{color:#e30b20}.content.register .agreement[data-v-1da8ac89]{font-size:%?24?%;color:#999;line-height:%?140?%}.content.register .agreement .radio[data-v-1da8ac89]{position:relative;top:%?-2?%}.content.register .signIn[data-v-1da8ac89]{width:%?458?%;height:%?86?%;line-height:%?86?%;border-radius:%?43?%;text-align:center;color:#fff}',""])},"43aa":function(e,t,a){"use strict";a.r(t);var i=a("120a"),s=a("f247");for(var n in s)"default"!==n&&function(e){a.d(t,e,function(){return s[e]})}(n);a("1039");var o=a("2877"),r=Object(o["a"])(s["default"],i["a"],i["b"],!1,null,"1da8ac89",null);t["default"]=r.exports},f247:function(e,t,a){"use strict";a.r(t);var i=a("1af3"),s=a.n(i);for(var n in i)"default"!==n&&function(e){a.d(t,e,function(){return i[e]})}(n);t["default"]=s.a}}]);