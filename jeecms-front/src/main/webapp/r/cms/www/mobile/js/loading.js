Vue.component('my-loading', {
	template: '<div class="my-loading"><i class="my-loading-i"></i><span>{{load}}</span></div>',
	data() {
		return {}
	},
	props: {
		load: {
			type: String,
			required: true,
			default: '加载更多'
		},
		isload: {
			type: Boolean,
			default: false
    },
    bindScroll:{
      type:String,
      default: ''
    }
	},
	mounted() {
    var that = this
    this.$nextTick(()=>{
        if(!this.bindScroll){
          document.body.onscroll = function() {
          //变量scrollTop是滚动条滚动时，距离顶部的距离
          var scrollTop =
            document.documentElement.scrollTop || document.body.scrollTop; //变量windowHeight是可视区的高度
          var windowHeight =
            document.documentElement.clientHeight || document.body.clientHeight; //变量scrollHeight是滚动条的总高度
          var scrollHeight =
            document.documentElement.scrollHeight || document.body.scrollHeight; //滚动条到底部的条件
          // console.log(scrollTop,windowHeight,scrollHeight)
          if ((scrollTop + windowHeight+100) >= scrollHeight) {
            //已滚动底部
            that.$parent.loading();
          }
        };
      }else{
          document.getElementById(this.bindScroll).onscroll = function() {
          var scrollTop1 =
          document.getElementById(that.bindScroll).scrollTop || document.getElementById(that.bindScroll).scrollTop; //变量windowHeight是可视区的高度
          var windowHeight1 =
          document.getElementById(that.bindScroll).clientHeight || document.getElementById(that.bindScroll).clientHeight; //变量scrollHeight是滚动条的总高度
          var scrollHeight1 =
          document.getElementById(that.bindScroll).scrollHeight || document.getElementById(that.bindScroll).scrollHeight; //滚动条到底部的条件
          if ((scrollTop1 + windowHeight1+100) >= scrollHeight1) {
            //已滚动底部
            that.$parent.loading();
          }
        }
      }
    })
    
		
    
	},
	methods: {
		getData() {
			//此次用来加载数据（对应加载不同page下数据）
			this.page = this.page + 1;
			/*发送请求*/
		},
		initScroll() {
			this.scroll = new BScroll(this.$refs.bscroll, {
				click: true,
				scrollbar: true,
				//上拉
				pullUpLoad: {
					threshold: 50
				}
			});
			//上拉
			this.scroll.on('pullingUp', () => {
				this.getData();
			})
		},
	}
});
