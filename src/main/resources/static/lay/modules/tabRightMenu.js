layui.define(['element'], function (exports) {
    var element = layui.element, $ = layui.jquery;
    var currentActiveTabID = undefined;
    var currentActiveTabTitle = undefined;

    var RIGHTMENUMOD = function () {
        this.v = '1.0';
        this.filter = '';//Tab选项卡的事件过滤
    };
    String.prototype.format = function () {
        if (arguments.length == 0) return this;
        var param = arguments[0];
        var s = this;
        if (typeof (param) == 'object') {
            for (var key in param) s = s.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
            return s;
        } else {
            for (var i = 0; i < arguments.length; i++) s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
            return s;
        }
    }

    function createStyle(ulClassName, width) {
        let style = '.{name} {position: absolute;width: {width_}px;z-index: 9999;display: none;background-color: #fff;padding: 2px;color: #333;border: 1px solid #eee;border-radius: 2px;cursor: pointer;}' +
            '.{name} li {text-align: center;display: block;height: 30px;line-height: 32px;}' +
            '.{name} li:hover {background-color: #666;color: #fff;}';
        return style.format({name: ulClassName, width_: width});
    }

    /**
     * 初始化
     */
    RIGHTMENUMOD.prototype.render = function (opt) {
        if (!opt.filter) {
            console.error("[ERROR]使用rightMenu组件需要制定'filter'属性！");
            return;
        }
        this.filter = opt.filter;
        this.width = opt.width ? opt.width : 110;// 右键弹出框的宽度，一般100~110即可
        this.isClickMidCloseTab = opt.isClickMidCloseTab || false;

        // pinTabTitles和pinTabIDs作用一样，只是便于开发使用考虑加入标题和ID形式进行两种方式的过滤
        this.pinTabTitles = opt.pinTabTitles;//固定标签的标题集合
        this.pintabIDs = opt.pintabIDs || ["main", "home"];//固定标签的ID集合
        this.navArr = opt.navArr || [
            {eventName: 'refresh', title: '刷新当前页'},
            {eventName: 'closeThis', title: '关闭标签'},
            {eventName: 'closeOthersButPin', title: '关闭其它'},
            {eventName: 'closeLeftButPin', title: '关闭左侧'},
            {eventName: 'closeRightButPin', title: '关闭右侧'}
        ];
        CreateRightMenu(this);
        regClickMidCloseTab(this);
    };


    /**
     * 注册点击鼠标中间关闭选项卡事件
     * @param rightMenuConfig
     */
    function regClickMidCloseTab(rightMenuConfig) {
        if (!rightMenuConfig.isClickMidCloseTab) {
            return;
        }

        $("#" + rightMenuConfig.filter).on('mousedown', 'li', function (e) {
            currentActiveTabID = $(e.target).attr('lay-id'); // 获取当前激活的选项卡ID,当前ID改为右键菜单弹出时就获取
            currentActiveTabTitle = $.trim($(e.target).text()); //获取当前激活选项卡的标题
            if (rightMenuConfig.pinTabTitles && rightMenuConfig.pinTabTitles.indexOf(currentActiveTabTitle) != -1 || currentActiveTabTitle == undefined) { //特殊ID，弹出默认的右键菜单
                return true;
            }
            if (rightMenuConfig.pintabIDs && rightMenuConfig.pintabIDs.indexOf(currentActiveTabID) != -1 || currentActiveTabID == undefined) { //特殊ID，弹出默认的右键菜单
                return true;
            }
            if (e.which != 2) {
                return true;
            }
            //鼠标中键关闭指定标签页
            element.tabDelete(rightMenuConfig.filter, currentActiveTabID);
            //隐藏菜单(如果有)
            $("." + rightMenuConfig.filter).hide();
            return false;
        });
    }

    /**
     * 创建右键菜单项目
     */
    function CreateRightMenu(opt) {
        // 使用"filter"属性作为css样式名称
        $("<style></style>").text(createStyle(opt.filter, opt.width)).appendTo($("head"));
        let li = '';
        $.each(opt.navArr, function (index, conf) {
            if (conf.eventName === 'line') {
                li += '<hr/>';
            } else {
                li += '<li data-type="{eventName}"><i class="layui-icon {icon}"></i>{title}</li>'
                    .format({eventName: conf.eventName, icon: conf.icon ? conf.icon : "", title: conf.title});
            }
        })
        let ul = '<ul class="{className}">{liStr} </ul>'.format({liStr: li, className: opt.filter})
        $(ul).appendTo("body");
        _CustomRightClick(opt);
    }

    /**
     * 绑定右键菜单
     */
    function _CustomRightClick(opt) {
        //事件委托，处理动态添加的li
        $("#" + opt.filter).on('contextmenu', 'li', function (e) {
            currentActiveTabID = $(e.target).attr('lay-id');// 获取当前激活的选项卡ID,当前ID改为右键菜单弹出时就获取
            currentActiveTabTitle = $.trim($(e.target).text());//获取当前激活选项卡的标题
            let popupMenu = $("." + opt.filter);
            let l = ($(document).width() - e.clientX) < popupMenu.width() ? (e.clientX - popupMenu.width()) : e.clientX;
            let t = ($(document).height() - e.clientY) < popupMenu.height() ? (e.clientY - popupMenu.height()) : e.clientY;
            popupMenu.css({left: l, top: t}).show();
            return false;
        });
        // 点击空白处隐藏弹出菜单
        $(document).click(function (event) {
            event.stopPropagation();
            $("." + opt.filter).hide();
        });

        /**
         * 注册tab右键菜单点击事件
         */
        $('.' + opt.filter + ' li').click(function (e) {
            var allTabIDArrButPin = [];//所有非固定选项卡集合
            var allTabIDArr = [];// 所有选项卡集合
            var idIndexButPin = 0;
            $.each($("#" + opt.filter + " li"), function (i) {
                let id = $(this).attr("lay-id");
                if (opt.pinTabTitles == undefined || opt.pintabIDs == undefined) {
                    allTabIDArrButPin[idIndexButPin] = id;
                    idIndexButPin++;
                } else if ((opt.pinTabTitles && opt.pinTabTitles.indexOf($(this).text()) == -1) &&
                    ((opt.pintabIDs && opt.pintabIDs.indexOf(id) == -1) || id == undefined)) {  //去除特殊ID
                    allTabIDArrButPin[idIndexButPin] = id;
                    idIndexButPin++;
                }
                allTabIDArr[i] = id;
            })
            // 事件处理
            switch ($(this).attr("data-type")) {
                case "closeThis"://关闭当前，如果开始了tab可关闭，实际意义不大
                    tabDelete(currentActiveTabID, opt.filter);
                    break;
                case "closeAllButPin"://关闭所有但固定
                    tabDeleteAll(allTabIDArrButPin, opt.filter);
                    break;
                case "closeOthersButPin"://关闭非当前但固定
                    allTabIDArrButPin.splice(allTabIDArrButPin.indexOf(currentActiveTabID), 1);
                    tabDeleteAll(allTabIDArrButPin, opt.filter);
                    break;
                case "closeLeft"://关闭左侧全部但固定
                case "closeLeftButPin":
                    tabDeleteAll(allTabIDArrButPin.slice(0, allTabIDArrButPin.indexOf(currentActiveTabID)), opt.filter);
                    break;
                case "closeRight"://关闭右侧全部但固定
                case "closeRightButPin":
                    tabDeleteAll(allTabIDArrButPin.slice(allTabIDArrButPin.indexOf(currentActiveTabID) + 1), opt.filter);
                    break;
                case "closeAll"://关闭所有
                    tabDeleteAll(allTabIDArr, opt.filter);
                    break;
                case "closeOthers"://关闭非当前
                    allTabIDArr.splice(allTabIDArr.indexOf(currentActiveTabID), 1);
                    tabDeleteAll(allTabIDArr, opt.filter);
                    break;
                case "refresh": //重新加载iFrame，实现更新
                    let $curFrame = $('iframe:visible');
                    $curFrame.attr("src", $curFrame.attr("src"));
                    break;
                default:
                    break;
            }
            $('.' + opt.filter).hide();
        })
    }

    function tabDelete(id, currentNav) {
        if (id != 0) {
            element.tabDelete(currentNav, id);//删除
        }
    }

    function tabDeleteAll(ids, currentNav) {
        $.each(ids, function (i, item) {
            tabDelete(item, currentNav);
        })
    }

    exports('tabRightMenu', new RIGHTMENUMOD());
})
