(function ($) {
  var ms = {
    init: function (obj, args) { return (function () { ms.fillHtml(obj, args); ms.bindEvent(obj, args); })(); }, fillHtml: function (obj, args) {args.current = Number(args.current)
      return (function () {
        obj.empty();
        if (args.current > 1) { obj.append('<a href="javascript:;" class="prevPage">上一页</a>'); } else { obj.remove('.prevPage'); obj.append('<span class="disabled">上一页</span>'); }
        if (args.current != 1 && args.current >= 4 && args.pageCount != 4) { obj.append('<a href="javascript:;" class="tcdNumber">' + 1 + '</a>'); }
        if (args.current - 2 > 2 && args.current <= args.pageCount && args.pageCount > 5) { obj.append('<span>...</span>'); }
        var start = args.current - 2, end = args.current + 2; if ((start > 1 && args.current < 4) || args.current == 1) { end++; }
        if (args.current > args.pageCount - 4 && args.current >= args.pageCount) { start--; }
        for (; start <= end; start++) { if (start <= args.pageCount && start >= 1) { if (start != args.current) { obj.append('<a href="javascript:;" class="tcdNumber">' + start + '</a>'); } else { obj.append('<span class="current">' + start + '</span>'); } } }
        if (args.current + 2 < args.pageCount - 1 && args.current >= 1 && args.pageCount > 5) { obj.append('<span>...</span>'); }
        if (args.current != args.pageCount && args.current < args.pageCount - 2 && args.pageCount != 4) { obj.append('<a href="javascript:;" class="tcdNumber">' + args.pageCount + '</a>'); }
        if (args.current < args.pageCount) { obj.append('<a href="javascript:;" class="nextPage">下一页</a>'); } else { obj.remove('.nextPage'); obj.append('<span class="disabled">下一页</span>'); }
        obj.append('<span class="pagecount">共' + args.pageCount + '页</span>'); if (args.turndown == 'true') { obj.append('<span class="countYe">到第<input type="text" maxlength=' + args.pageCount.toString().length + '>页<a href="javascript:;" class="turndown">确定</a><span>'); }
      })();
    }, bindEvent: function (obj, args) {
      return (function () {
        obj.on("click", "a.tcdNumber", function () { var current = parseInt($(this).text()); ms.fillHtml(obj, { "current": current, "pageCount": args.pageCount, "turndown": args.turndown }); if (typeof (args.backFn) == "function") { args.backFn(current); } }); obj.on("click", "a.prevPage", function () { var current = parseInt(obj.children("span.current").text()); ms.fillHtml(obj, { "current": current - 1, "pageCount": args.pageCount, "turndown": args.turndown }); if (typeof (args.backFn) == "function") { args.backFn(current - 1); } }); obj.on("click", "a.nextPage", function () { var current = parseInt(obj.children("span.current").text()); ms.fillHtml(obj, { "current": current + 1, "pageCount": args.pageCount, "turndown": args.turndown }); if (typeof (args.backFn) == "function") { args.backFn(current + 1); } }); obj.on("click", "a.turndown", function () {
          var page = $("span.countYe input").val(); if (page > args.pageCount) { alert("您的输入有误，请重新输入！"); }
          ms.fillHtml(obj, { "current": page, "pageCount": args.pageCount, "turndown": args.turndown });
        });
      })();
    }
  }
  $.fn.createPage = function (options) { var args = $.extend({ pageCount: 10, current: 1, turndown: true, backFn: function () { } }, options); ms.init(this, args); }
})(jQuery);