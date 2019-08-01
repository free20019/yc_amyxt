var MenuList = (function () {
	var prevCheckTabItem = null, menuItemActive = null;
	var initMenuTag = null;
	var displayType = {
		horizontal: function (tags) {/*水平*/
			return null;
		},
		vertical: function (tags, item) {/*垂直*/
			if (item && item.displayState) {
				tags.tagLi.addClass('active');
			}
			if (item && item.children) {
				tags.tagA.removeAttr('data-skip');
				tags.tagA.append(tags.arrow);
				tags.tagLi.append(tags.tagUl);
				tags.tagLi.addClass('subMenuPanel');
				if (item.displayState) tags.tagUl.show();
				var liList = setMenuHtml(item.children);
				$.each(liList, function (i, dataItem) {
					tags.tagUl.append(dataItem);
				});
			}
			return tags.tagLi;
		}
	};

	function MenuList(tag, options, methods) {
		initMenuTag = tag;
		$.extend(this.state, options);
		$.extend(this.methods, methods);

		$.each(setMenuHtml(this.state.menu), function (index, item) {
			$(item).appendTo(tag);
		});
		setEvent();
		if (menuItemActive) {
			$(tag + ' a[data-skip=' + menuItemActive + ']').trigger('click');
		}
	}

	MenuList.prototype = {
		state: {
			displayType: 'vertical',
			menu: [],
			tabPanel: '#tabsContent',
			tabContentPanels: '#tabsContentPanels'
		},
		methods: {}
	};

	function setMenuHtml(data) {
		var state = MenuList.prototype.state;
		var html = [];
		$.each(data, function (index, item) {
			var tagLi = $('<li></li>').data('database', item);
			var tagA = $('<a href="javascript:void(0);" data-skip="' + item.id + '" class="nav-title"></a>');
			var iconfont = $('<i class="iconfont ' + item.icon + '"></i>');
			var arrow = $('<i class="iconfont icon-arrow"></i>');
			var title = $('<span class="nav-label">' + item.name + '</span>');
			var tagUl = $('<ul class="childrenPanel"></ul>').hide();
			tagLi.append(tagA);
			tagA.append(iconfont);
			tagA.append(title);
			if (item && item.active) menuItemActive = item.id;
			html.push(displayType[state.displayType]({tagLi: tagLi, tagA: tagA, iconfont: iconfont, arrow: arrow, title: title, tagUl: tagUl}, item));
		});
		return html;
	}

	/*菜单栏点击事件*/
	function setEvent() {
		var state = MenuList.prototype.state;
		var selectedMenuBtn = null;
		$(initMenuTag).find('.nav-title').off('click').on('click', function () {
			prevCheckTabItem = $(this).attr('data-skip');
			var item = $(this).parent().data('database');
			var tabsPanels = $(state.tabPanel);
			var contentPanels = $(state.tabContentPanels);

			/*判断是否有二级菜单*/
			if ($(this).parent().hasClass('subMenuPanel')) {
				$(this).parent().addClass('active').siblings('.active').removeClass('active');
				$(this).parent().siblings('.subMenuPanel').find('ul.childrenPanel').hide();
				$(this).next().show();
			} else {
				/*取消当先选中的选项卡并隐藏iframe*/
				tabsPanels.find('.active').removeClass('active');
				contentPanels.find('iframe').hide();

				var childrenPanel = $(this).parent().parent();
				/*把记录的二级菜单 清除选中状态*/
				if (selectedMenuBtn) {
					$('[data-skip=' + selectedMenuBtn + ']').parent().removeClass('active');
					selectedMenuBtn = null;
				}
				/*判断是否是二级菜单 把二级菜单记录下来*/
				if (childrenPanel[0].className.indexOf('childrenPanel') >= 0) selectedMenuBtn = prevCheckTabItem;
				/*选中并清除同一级其他选中的标签*/
				$(this).parent().addClass('active').siblings('.active').removeClass('active').find('.childrenPanel').hide().find('.active').removeClass('active');
				/*判断是否打开连接*/
				if (contentPanels.find('#' + item.id).length > 0) {
					tabsPanels.find('.menuTab_item[data-remove=' + item.id + ']').addClass('active');
					contentPanels.find('#' + item.id).show();
				} else {
					$('<a class="menuTab_item active" data-remove="' + item.id + '"><span>' + item.name + '</span><i class="glyphicon glyphicon-remove"></i></a>').appendTo(state.tabPanel);
					$('<iframe name="iframe' + contentPanels.length + '" width="100%" src="' + item.href + '" frameborder="0" id="' + item.id + '" style="display: block; height: calc(100% - 33px);"></iframe>').appendTo(state.tabContentPanels);
				}
			}

			/*菜单栏-标签卡点击事件*/
			$('.menuTab_item').off('click').on('click', function () {
				$(this).addClass('active').siblings('.active').removeClass('active');
				$(state.tabContentPanels + ' iframe').hide();
				$(state.tabContentPanels + ' #' + $(this).attr('data-remove')).show();
				prevCheckTabItem = $(this).attr('data-remove');
			});
			/*菜单栏-标签卡的删除事件*/
			$('.menuTab_item i.glyphicon-remove').off('click').on('click', function () {
				event.stopPropagation();
				var id = $(this).parent().attr('data-remove');
				var content = $(state.tabContentPanels + ' #' + id);
				/*判断选中的选项卡是不是要删除的选项卡*/
				if (prevCheckTabItem && id === prevCheckTabItem) {
					/*判断选项卡的位置*/
					if (content.prev().length > 0) {
						prevCheckTabItem = $(this).parent().prev().addClass('active').attr('data-remove');
						content.prev().show();
					} else if (content.next().length > 0) {
						prevCheckTabItem = $(this).parent().next().addClass('active').attr('data-remove');
						content.next().show()
					}
				} else {
					$('.menuTab_item[data-remove=' + prevCheckTabItem + ']').addClass('active');
					$(state.tabContentPanels + ' #' + prevCheckTabItem).show();
				}
				$(this).parent().remove();
				content.remove();
			});
		});
	}

	return MenuList;
}());