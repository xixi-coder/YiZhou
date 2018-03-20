/**
 * Created by BOGONj on 2016/8/22.
 */
$(document).ready(function () {
    $('#resource_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'resource.name': 'required',
            'resource.show_name': 'required',
            'resource.code': 'required;remote[' + g.ctx + '/admin/sys/resource/validCode, resource.id, resource.code]',
            'resource.path': 'required;',
            'resource.sort': 'required;number'
        },
        valid: function (form) {
            if ($('#parent').val() == '') {
                layer.msg('上级菜单未选择');
                return;
            }
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/resource/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/resource';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
    $('#ico').on('click', function () {
        layer.open({
            area: ['100%', '100%'],
            content: tpl.bind($('#ico_tpl')),
            closeBtn: 0,
            shadeClose: true,
            success: function (layero, index) {
                $('.bs-glyphicons>li').on('click', function (a) {
                    var selectIco = $($(a.delegateTarget).children()[0]).attr('class');
                    layer.close(index);
                    $('#ico').val(selectIco);
                })
            }
        });
    });

    $.ajax({
        url: g.ctx + '/admin/sys/resource/parent',
        type: 'get',
        data: {id: $('#id').val()},
        success: function (data) {
            if (data.status == 'OK') {
                var zNodes = data.data;
                zNodes.unshift({id: 0, pId: 0, name: '顶级资源', checked: true});
                if ($('#parent').val() != '') {
                    $(zNodes).each(function (i, data) {
                        if ($('#parent').val() == data.id) {
                            data['checked'] = true;
                            zNodes[i] = data;
                            zNodes[0]['checked'] = false;
                        }
                    });
                }
                var setting = {
                    check: {
                        enable: true,
                        chkStyle: "radio",
                        radioType: "all"
                    },
                    data: {
                        simpleData: {
                            enable: true
                        }
                    },
                    callback: {
                        onCheck: function (e, treeId, treeNode) {
                            $("#parent").val(treeNode.id);
                        }
                    }
                };
                $.fn.zTree.init($("#ztreeparent"), setting, zNodes);
            }
        }
    });

});
