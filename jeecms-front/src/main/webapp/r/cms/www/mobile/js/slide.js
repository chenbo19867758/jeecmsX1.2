Vue.component('Slide', {
	template: '<div class="wh_slide" @click="clickSlide"><slot/></div>',
	methods: {
		clickSlide() {
			this.$emit('click')
		}
	}
});
