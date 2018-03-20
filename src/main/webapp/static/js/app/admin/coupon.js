/**
 * Created by Administrator on 2016/9/28.
 */
var memberMap = new HashMap();
var ztreeResource;
var zNodes = [];
var setting = {};
var table;
var url = {
    searchMember: g.ctx + '/admin/activity/coupon/memberList',
    list: g.ctx + "/admin/activity/coupon/list",
    award: g.ctx + "/admin/activity/coupon/award",
    del: g.ctx + '/admin/activity/coupon/del'
}
$(document).ready(function () {
    var columns = [
        {"data": "coupon_title"},
        {"data": "coupon_desc"},
        {"data": "coupon_type",
            "render": function (a) {
                if (a == 1){
                    return '<span class="label" style="background-color: #66CCFF">普通券</span>';
                }
                if (a == 2){
                    return '<span class="label" style="background-color: #699934">折扣券</span>';
                }else {
                    return '<span class="label" style="background-color: #66CCFF">普通券</span>';
                }

            }
        },
        {"data": "service_type",
            "render": function (a) {
                if (a == 0){
                    return '<span class="label" style="background-color: #66CCFF">通用优惠券</span>';
                } else if (a == 1){
                    return '<span class="label" style="background-color: #596dff">专车</span>';
                }else if (a == 2){
                    return '<span class="label" style="background-color: #7c50e5">代驾</span>';
                }else if (a == 3){
                    return '<span class="label" style="background-color: #4e23e5">出租车</span>';
                }else if (a == 4){
                    return '<span class="label" style="background-color: #0722e5">快车</span>';
                }else if (a == 5){
                    return '<span class="label" style="background-color: #8ae59e">顺风车</span>';
                }else if (a == 6){
                    return '<span class="label" style="background-color: #47f064">城际专线</span>';
                }else if (a == 7){
                    return '<span class="label" style="background-color: #8bf026">航空专线</span>';
                }else {
                    return '<span class="label" style="background-color: #66CCFF">通用优惠券</span>';
                }

            }
        },
        {"data": "name"},
        // {"data": "mini_price"},
        {"data": "base_amount"},
        {"data": "percent_amount"},
        {"data": "amount"},
        {"data": "percent"},
        {"data": "create_time"},
        {"data": "start_time"},
        {"data": "end_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    table = dt.build($('#coupon-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: url.del,
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
        $('.award').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.open({
                type: 0,
                area: ['100%', '100%'],
                content: tpl.bind($('#driver_tpl'), {id: id}),
                yes: function (index, layero) {
                    var treeObj = $.fn.zTree.getZTreeObj("ztree_user");
                    $.ajax({
                        url: url.award,
                        data: {couponId: id, memberIds: $('#selectUserHide').val()},
                        type: 'post',
                        success: function (data) {
                            layer.msg(data.msg);
                        },
                        beforeSend: function () {
                            $(".layui-layer-btn0").off();
                        }
                    });
                }, success: function (layero, index) {
                    memberMap.clear();
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
                                if (treeNode.id == 0) {
                                    if (treeNode.checked) {
                                        $(treeNode.children).each(function (index, val) {
                                            memberMap.put(val.id, val);
                                        });
                                    } else {
                                        $(treeNode.children).each(function (index, val) {
                                            memberMap.remove(val.id);
                                        });

                                    }
                                } else {
                                    if (treeNode.checked) {
                                        memberMap.put(treeNode.id, treeNode);
                                    } else {
                                        memberMap.remove(treeNode.id);
                                    }
                                }

                                coupon.showUser();
                            }
                        }
                    };
                    $('#searchBtn').on('click', function (a) {
                        coupon.initZtree(id);
                    });
                    coupon.initZtree(id);
                }
            });
        })
    });
    $('.search').on('click', function () {
        table.ajax.reload();
    })
});
var coupon = {
    showUser: function () {
        var values = memberMap.values();
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
            url: url.searchMember,
            data: {name: $('#searchCondition').val(), userId: id},
            type: 'get',
            success: function (data) {
                var parent = {pId: 0, id: 0, name: '全选'};
                data.push(parent);
                zNodes = data;
                $(zNodes).each(function (i, val) {
                    if (val.checked == 0) {
                        val.checked = true;
                        memberMap.put(val.id, val);
                    } else {
                        val.checked = false;
                        $(memberMap.values()).each(function (ii, vval) {
                            if (vval.id == val.id) {
                                val.checked = true;
                            }
                        });
                    }
                });
                coupon.showUser();
                ztreeResource = $.fn.zTree.init($("#ztree_user"), setting, zNodes);
            }
        });
    }
}