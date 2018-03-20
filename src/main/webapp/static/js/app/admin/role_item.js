/**
 * Created by BOGONj on 2016/8/22.
 */
$(document).ready(function () {
    $('.children').on('ifChecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
        var parent = $($(event.target).parent().parent().parent().parent()).find(".parent");
        parent.iCheck('check')
    });
    $('.parent').on('ifClicked', function (event) {//ifCreated 事件应该在插件初始化之前绑定
        if ($(event.target).is(":checked")) {
            var children = $($(event.target).parent().parent().parent().next()).find(".children");
            $(children).each(function (index, val) {
                $(val).iCheck('uncheck');
            });
        } else {
            var children = $($(event.target).parent().parent().parent().next()).find(".children");
            $(children).each(function (index, val) {
                $(val).iCheck('check');
            });
        }
    });
    if (roleId != 0) {
        $.ajax(
            {
                url: g.ctx + '/admin/sys/role/resource/' + roleId,
                success: function (data) {
                    $(data.data).each(function (index, val) {
                        $("input[value='" + val.id + "']").iCheck('check');
                    });
                }
            }
        );
    }
    $('#role_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'role.name': 'required;'
        },
        valid: function (form) {
            var resourceStr = '';
            var checkBoxs = $('input:checkbox');
            $(checkBoxs).each(function (index, val) {
                if ($(val).is(':checked')) {
                    resourceStr += $(val).val() + ',';
                }
            });
            $('#resourcesIds').val(resourceStr);
            var dat = $(form).serialize()
            console.log();
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/role/save',
                data: dat,
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/role';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
});
