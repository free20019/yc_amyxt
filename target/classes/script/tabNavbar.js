var TabNavbar = (function () {
	var initTag=null;
	function TabNavbar(tag, options, methods) {
		initTag=tag;
		$.extend(this.state, options);
		$.extend(this.methods, methods);
		/*初始化-重构tab*/
		reconstructDomStructure();
		/*添加点击事件*/
		addTabNavbarEventListener()
	}
	TabNavbar.prototype.state = {};
	TabNavbar.prototype.methods = {
		onTabButtonItem: function () {}
	};
	function setDatabase(item, value) {
		var database = $(item).data('database');
		if (!database) database = {};
		if (value.constructor === Object) {
			if(value.key && value.value) {
				database[value.key] = value.value;
				$(item).data('database', database);
			} else {
				for (var key in value) database[key] = value[key];
				$(item).data('database', database);
			}
		} else {
			throw '无法识别value'
		}
	}
	function reconstructDomStructure(){
		$(initTag).addClass('tabPanel');
		$('<ul />').addClass('tabNavbar').prependTo(initTag);
		$(initTag).find('[data-type=tabPanel]').each(function (index, item) {
			var href, title, type, id = $(item).attr('id');
			var database = $(item).data('database');
			if (href = $(item).attr('href')) {
				setDatabase(database, {key: 'href', value: href});
			}
			if (title = $(item).attr('title')) {
				setDatabase(database, {key: 'title', value: title});
				$(item).removeAttr('title')
			}
			if ((type = $(item).attr('data-type')) === 'tabPanel') {
				setDatabase(database, {key: 'type', value: type});
				$(item).addClass('tabContentPanel');
				$(item).removeAttr('data-type');
			}
			var liTag = $('<li />').addClass('tabNavItem').attr('data-skip', id).text(title);
			if (index===0) liTag.addClass('active');
			else $(item).css({display: 'none'});
			$(initTag).find('.tabNavbar').append(liTag);
		}).wrapAll('<div class="tabContentPanels"/>');
	}
	function addTabNavbarEventListener() {
		$(initTag).find('.tabNavItem').on('click', function () {
			var id = $(this).addClass('active').siblings('.active').removeClass('active').end().attr('data-skip');
			if (id) $('#' + id).show().siblings().hide();
			TabNavbar.prototype.methods.onTabButtonItem();
		});
	}
	return TabNavbar
}());