$(function(){
	$.validator.addMethod('chinese', function (value, element) {
	    var identReg = /^[\u4e00-\u9fa5]+$/;
	    return this.optional(element) || (identReg.test(value));
	}, '请输入中文');
	$.validator.addMethod('english', function (value, element) {
	    var identReg = /^[A-Za-z]+$/;
	    return this.optional(element) || (identReg.test(value));
	}, '请输入英文');
	$.validator.addMethod('float', function (value, element) {
	    var identReg = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
	    return this.optional(element) || (identReg.test(value));
	}, '请输入合法的数字');
	$.validator.addMethod('enNum', function (value, element) {
	    var identReg = /^[A-Za-z0-9]+$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入英文和数字');
	$.validator.addMethod('number', function (value, element) {
	    var identReg = /^[0-9]\d*$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入整数');
	$.validator.addMethod('cnEnNum', function (value, element) {
	    var identReg = /^[\u4e00-\u9fa5|A-Za-z0-9]+$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入中文、英文和数字');
	$.validator.addMethod('email', function (value, element) {
	    var identReg = /^[a-zA-Z0-9][a-zA-Z0-9 . _-]+(@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)+)$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入邮箱');
	$.validator.addMethod('mobile', function (value, element) {
	    var identReg = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[2|6|7])|(18[0-9])|(17([0-1]|[3]|[5-8]))|(19[1|8|9]))\d{8}$/;
	    return this.optional(element) || (identReg.test(value));
	}, '请输入正确的手机号码');
	$.validator.addMethod('phone', function (value, element) {
	    var identReg = /^((\d{3,4}-\d{7,8})|(\d{7}-\d{1,12})|(\d{8}-\d{1,11})|(\d{11}-\d{1,8})|(\d{7,8})|(\d{11,20})|(\d{3}-\d{8}-\d{1,7})|(\d{3}-\d{7}-\d{1,8})|(\d{4}-\d{7}-\d{1,7})|(\d{4}-\d{8}-\d{1,6}))$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入座机号');
	$.validator.addMethod('phoneAll', function (value, element) {
	    var identReg = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[2|6|7])|(18[0-9])|(17([0-1]|[3]|[5-8]))|(19[1|8|9]))\d{8}$|^((\d{3,4}-\d{7,8})|(\d{7}-\d{1,12})|(\d{8}-\d{1,11})|(\d{11}-\d{1,8})|(\d{7,8})|(\d{11,20})|(\d{3}-\d{8}-\d{1,7})|(\d{3}-\d{7}-\d{1,8})|(\d{4}-\d{7}-\d{1,7})|(\d{4}-\d{8}-\d{1,6}))$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入手机或座机号');
	$.validator.addMethod('identity', function (value, element) {
	    var identReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入身份证号');
	$.validator.addMethod('postal', function (value, element) {
	    var identReg = /^\d{6}$/;
	    return this.optional(element) || (identReg.test(value));
	}, '只能输入邮政编码');
})