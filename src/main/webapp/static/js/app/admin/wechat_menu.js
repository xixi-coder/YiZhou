/**
 * Created by admin on 2016/9/30.
 */
$(document).ready(function () {
    var url = {
        save: g.ctx + '/admin/wechat/menu/save',
        item: g.ctx + '/admin/wechat/menu/item'
    }
    $('.add').on('click', function (target) {
        var id = $(target.currentTarget).data("id");
        menu.html(false, id);
    });
    $('.edit').on('click', function (target) {
        var id = $(target.currentTarget).data("id");
        menu.html(true, id);
    });
    $('.del').on('click', function (target) {
    });
    var menu = {
        html: function (edit, id) {
            var content = {};
            if (edit) {
                $.ajax({
                    async: false,
                    url: url.item,
                    data: {id: id},
                    success: function (data) {
                        content['id'] = data.data.id;
                        content['menuUrl'] = data.data.url;
                        content['menuName'] = data.data.name;
                        content['parent'] = data.data.parent;
                    }
                })
            } else {
                content = {id: '', menuUrl: '', menuName: '', parent: id};
            }
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0, //不显示关闭按钮
                shift: 2,
                shadeClose: false, //开启遮罩关闭
                area: ['400px', '230px'], //宽高
                content: tpl.bind($('#menuContent'), content),
                btn: ['提交', '取消'],
                yes: function (index, layero) {
                    $.ajax({
                        url: url.save,
                        data: {
                            'menu.parent': $('#parent').val(),
                            'menu.name': $('#menuName').val(),
                            'menu.url': $('#menuUrl').val(),
                            'menu.id': $('#id').val()
                        },
                        success: function (data) {
                            layer.msg(data.msg);
                            window.location.reload();
                        }
                    });
                }
            });
        }
    }
});

