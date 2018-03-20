/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/sys/role/list",
        resource: g.ctx + '/admin/sys/resource/parent',
        hasResource: g.ctx + '/admin/sys/role/resource',
        saveResource: g.ctx + '/admin/sys/role/saveResource',
        searchUser: g.ctx + '/admin/sys/user/searchZtree',
        saveUser: g.ctx + '/admin/sys/role/saveUser'
    };
    var columns = [
        {
            "data": "name",
            "render": function (a, b, c) {
                return a;
            }
        },
        {"data": "code"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];
    var userMap = new HashMap();
    var ztreeResource;
    var zNodes = [];
    var setting = {};
    var table = dt.build($('#role-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/role/del',
                    data: {id: id},
                    type: 'POST',
                    success: function (data) {
                        if (data.status == 'OK') {
                            layer.msg(data.msg);
                            window.location.reload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            });
        });
        $('.user').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.open({
                type: 0,
                content: tpl.bind($('#user_tpl'), {id: id}),
                yes: function (index, layero) {
                    var treeObj = $.fn.zTree.getZTreeObj("ztree_user");
                    $.ajax({
                        url: url.saveUser,
                        data: {roleId: id, userStrs: $('#selectUserHide').val()},
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
                                role.showUser();
                            }
                        }
                    };
                    $('#searchBtn').on('click', function (a) {
                        role.initZtree(id);
                    });
                    role.initZtree(id);
                }
            });
        });
    });
    $(".search").on('click',function(){
        table.ajax.reload();
    });
    var role = {
        showUser: function () {
            var values = userMap.values();
            console.log(values);
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
                url: url.searchUser,
                data: {name: $('#searchCondition').val(), roleId: id},
                type: 'get',
                success: function (data) {
                    zNodes = data.data;
                    $(zNodes).each(function (i, val) {
                        if (val.checked == 0) {
                            val.checked = true;
                            userMap.put(val.id, val);
                        } else {
                            val.checked = false;
                        }
                    });
                    role.showUser();
                    ztreeResource = $.fn.zTree.init($("#ztree_user"), setting, zNodes);
                }
            });
        }
    }
});