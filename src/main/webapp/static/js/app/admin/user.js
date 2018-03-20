/**
 * Created by BOGONm on 16/8/11.
 */
var url = {
    list: g.ctx + "/admin/sys/user/list",
    searchDriver: g.ctx + "/admin/sys/user/driverList",
    saveDriver: g.ctx + "/admin/sys/user/saveOwnDriver"
};
var userMap = new HashMap();
var ztreeResource;
var zNodes = [];
var setting = {};
$(document).ready(function () {
    var columns = [
        {
            "data": "name",
            "render": function (a, b, c) {
                return a;
            }
        },
        {"data": "username"},
        {
            "data": "status",
            'render': function (a) {
                if (a == 1) {
                    return '正常';
                } else {
                    return '禁用';
                }
            }
        },
        {"data": "phone"},
        {"data": "birthday"},
        {"data": "last_login_time"},
        {"data": "last_login_ip"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id, type: row.type, status: row.status});
            }
        }
    ];

    var table = dt.build($('#user-list'), url.list, columns, function () {
        $(".updatePw").on('click', function (a) {
            var id = $(a.target).attr("data-id");
            var index = layer.open({
                type: 0,
                content: tpl.bind($('#updatePw_tpl'), {id: id}),
                yes: function (index, layero) {
                    $('#password_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#password_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/sys/user/updatepd',
                                data: $(form).serialize(),
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                    } else {
                                        layer.tips(data.msg, '#oldpassword');
                                    }
                                }
                            });
                        }
                    });
                }
            });
        });
        $('.rePw').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否将密码充值为“123@123？”', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/user/repw',
                    data: {id: id},
                    type: 'POST',
                    success: function (data) {
                        if (data.status == 'OK') {
                            layer.msg(data.msg);
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            });
        });
        $('.delete').on('click', function (a) {
            console.log(123)
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/user/del',
                    data: {id: id},
                    type: 'POST',
                    success: function (data) {
                        if (data.status == 'OK') {
                            layer.msg(data.msg);
                            table.ajax.reload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            });
        });
        $('.disable').on('click', function (a) {
            var id = $(a.target).data("id");
            var status =$(a.target).data("status");
            var title = '禁用';
            if(status==2){
                title = "启用"
            }

            layer.confirm('是否' + title + '该用户？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/user/disable/' + id,
                    type: 'POST',
                    success: function (data) {
                        if (data.status == 'OK') {
                            layer.msg(data.msg);
                            table.ajax.reload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            });
        });
        $('.drivers').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.open({
                type: 0,
                area: ['100%', '100%'],
                content: tpl.bind($('#driver_tpl'), {id: id}),
                yes: function (index, layero) {
                    var treeObj = $.fn.zTree.getZTreeObj("ztree_user");
                    $.ajax({
                        url: url.saveDriver,
                        data: {userId: id, driverIds: $('#selectUserHide').val()},
                        type: 'post',
                        success: function (data) {
                            layer.msg(data.msg);
                        }
                    });
                }, success: function (layero, index) {
                    userMap.clear();
                    setting = {
                        check: {
                            enable: true
                        },
                        data: {
                            simpleData: {
                                enable: true
                            }
                        },
                        callback: {
                            onCheck: function (e, treeId, treeNode) {
                                if (treeNode.checked) {
                                    userMap.put(treeNode.id, treeNode);
                                } else {
                                    userMap.remove(treeNode.id);
                                }
                                user.showUser();
                            }
                        }
                    };
                    $('#searchBtn').on('click', function (a) {
                        user.initZtree(id);
                    });
                    user.initZtree(id);
                }
            });
        });
    });
    $('.search').on('click', function () {
        table.ajax.reload();
    })
});

var user = {
    showUser: function () {
        console.log(userMap.values());
        var values = userMap.values();
        if (values.length == 0) {
            $('#selectUser').val('');
            $('#selectUserHide').val('');
        }
        $(values).each(function (i, val) {
            if (i == 0) {
                $('#selectUser').val(val.name);
                $('#selectUserHide').val(val.id);
            } else {
                $('#selectUser').val($('#selectUser').val() + ";" + val.name);
                $('#selectUserHide').val($('#selectUserHide').val() + "," + val.id);
            }
        });
    },
    initZtree: function (id) {
        $.ajax({
            url: url.searchDriver,
            data: {name: $('#searchCondition').val(), userId: id},
            type: 'get',
            success: function (data) {
                console.log(data);
                zNodes = data;
                $(zNodes).each(function (i, val) {
                    if (val.checked == 1) {
                        val.checked = true;
                        userMap.put(val.id, val);
                    } else {
                        val.checked = false;
                        $(userMap.values()).each(function (ii, vval) {
                            if (vval.id == val.id) {
                                val.checked = true;
                                return;
                            }
                        });
                    }
                });
                user.showUser();
                ztreeResource = $.fn.zTree.init($("#ztree_user"), setting, zNodes);
            }
        });
    }
}