/*静态变量*/
var ERROR_SELECT_MANY = '只支持单条数据操作';
var ERROR_SELECT_NOT = '必须选中一条数据才可操作';
var POINT_DELETE_CONFIRM = '确认是否删除';
var POINT_UPDATE_CONFIRM = '是否修改记录';

/**
 * 时间格式
 * @param fmt
 * @returns {*}
 * @constructor
 */
Date.prototype.Format = function (fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1,                 //月份
		"d+": this.getDate(),                    //日
		"h+": this.getHours(),                   //小时
		"m+": this.getMinutes(),                 //分
		"s+": this.getSeconds(),                 //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds()             //毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};
/**
 * 删除数组指定下标或指定对象
 * @param obj
 */
Array.prototype.remove=function(obj){
	for(var i =0;i <this.length;i++){
		var temp = this[i];
		if (!isNaN(obj)) temp = i;
		if (temp === obj) {
			for (var j = i; j < this.length; j++) this[j] = this[j + 1];
			this.length = this.length - 1;
		}
	}
};


//bootstrap-datetimepicker default option
var dateDefaultOption = {
  language:  'zh-CN',
  format: 'yyyy-mm-dd',
  autoclose: 1,
  startView: 2,
  minView: 2
};
var datetimeDefaultOption = {
  language:  'zh-CN',
  format: 'yyyy-mm-dd hh:ii:ss',
  autoclose: 1
};

/**
 * 配合bootstrap-datetimepicker使用
 * 显示时间控件
 * @inputTag：绑定的输入框
 * @idName：时间控件的id 格式：idName + 'DateTimePicker'
 * @datetimeOption: 自定义时间控件的属性
 */
function showDateTimePicker(inputTag, idName, option) {
	var dateTimePickerTag = $('<div class="ip-datetimepicker datetimepicker datetimepicker-top-right datetimepicker-menu" id="' + idName + 'DateTimePicker">');
	dateTimePickerTag.css(dateTimePickerPosition({inputTag: inputTag, dateTimePickerTag: dateTimePickerTag}, option));
	dateTimePickerTag.show();
	var modalDateTimePicker = $('<div class="modal-dateTimePicker">').on('click', function () {
		dateTimePickerTag.remove();
		modalDateTimePicker.remove();
	});
	$(inputTag).after(dateTimePickerTag);
	if (!dateTimePickerTag.prev().hasClass('modal-dateTimePicker')) dateTimePickerTag.before(modalDateTimePicker);
	dateTimePickerTag.datetimepicker(option.datetimeOption).on('changeDate', function (ev) {
		$(inputTag).data('date', ev).val(ev.date.Format($(inputTag).attr('data-format')));
		dateTimePickerTag.remove();
		modalDateTimePicker.remove();
	}).find('.icon-arrow-left').addClass('glyphicon-arrow-left').end().find('.icon-arrow-right').addClass('glyphicon-arrow-right');
}
function dateTimePickerPosition(tags, option) {
	var style = {
		left: $(tags.inputTag).get(0).offsetLeft
	};
	switch (option.position){
		case 'top':
		case 'top-left':
		case 'topLeft':
			style.left = $(tags.inputTag).get(0).offsetLeft;
			style.bottom = 'calc(100% + 3px)';
			break;
		case 'top-right':
		case 'topRight':
			break;
		case 'bottom':
			break;
		case 'bottom-left':
		case 'bottomLeft':
			break;
		case 'bottom-right':
		case 'bottomRight':
			break;
	}
	return style;
}

/**
 * 添加到多选框列表
 * @param item：要添加的数据
 * @param selectedItems：添加到要插入的多选框列表
 */
var selectItem = function(item, selectedItems) {
	selectedItems.push(item);
};
/**
 * 删除某一条多选框列表
 * @param item：要删除的数据
 * @param selectedItems：要从那个多选框列表中删除
 */
var unselectItem = function(item, selectedItems) {
	selectedItems.remove(item);
};