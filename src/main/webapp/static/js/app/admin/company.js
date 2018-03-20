/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/sys/company/list"
    };
    var columns = [
        {"data": "name"},
        {"data": "amount"},
        {"data": "smsCount"},
        {"data": "activityAmount"},
        {"data": "insuranceAmount"},
        {"data": "p_name"},
        {"data": "c_name"},
        {"data": "cc_name"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    pk_id: row.pk_id,
                    id:row.id,
                    count: row.smsCount,
                    activityamount: row.activityAmount,
                    insuranceamount: row.insuranceAmount
                });
            }
        }
    ];
    var table = dt.build($('#company-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/company/del',
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
        $('.disablealluser').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否禁用该公司所有的后台用户？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/company/disable/'+id,
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
        $('.ablealluser').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否启用该公司所有的后台用户？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/company/able/'+id,
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
        $('.nofrozenalldriver').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否解冻该公司所有的司机？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/company/nofrozen/'+id,
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
        $('.frozenalldriver').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否冻结该公司所有的司机？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/company/frozen/'+id,
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
        $('.rechangeSms').on('click', function (a) {
            var id = $(a.target).data("id");
            var smsCount = $(a.target).data("count");
            if (smsCount == null) {
                smsCount = 0;
            }
            var html = tpl.bind($("#rechangeSms"), {count: smsCount});
            layer.open({
                title: '短信充值'
                , content: html,
                btn: ['确定', '取消'] //可以无限个按钮
                , btn1: function (index, layero) {
                    var money = $("#money").val();
                    $.ajax({
                        url: g.ctx + '/admin/sys/company/rechangeSms',
                        data: {id: id, money: money},
                        success: function (data) {
                            layer.close(index);
                            layer.msg(data.msg);
                            table.ajax.reload();
                        }
                    })
                }
            });
        });
        $('.rechangeActivityAmount').on('click', function (a) {
            var id = $(a.target).data("id");
            var activityAmount = $(a.target).data("activityamount");
            if (activityAmount == null) {
                activityAmount = 0;
            }
            var html = tpl.bind($("#rechangeActivityAmount"), {activityAmount: activityAmount});
            layer.open({
                title: '活动金额充值'
                , content: html,
                btn: ['确定', '取消'] //可以无限个按钮
                , btn1: function (index, layero) {
                    var money = $("#money").val();
                    $.ajax({
                        url: g.ctx + '/admin/sys/company/rechangeActivityAmount',
                        data: {id: id, money: money},
                        beforeSend: function () {
                            layer.close(index);
                        },
                        success: function (data) {
                            layer.msg(data.msg);
                            table.ajax.reload();
                        }
                    })
                }
            });
        });

        $('.rechangeInsuranceAmount').on('click', function (a) {
            var id = $(a.target).data("id");
            var insuranceAmount = $(a.target).data("insuranceamount");
            console.log(insuranceAmount)
            if (insuranceAmount == null) {
                insuranceAmount = 0;
            }
            var html = tpl.bind($("#rechangeInsuranceAmount"), {insuranceAmount: insuranceAmount});
            layer.open({
                title: '保险金额充值'
                , content: html,
                btn: ['确定', '取消'] //可以无限个按钮
                , btn1: function (index, layero) {
                    var money = $("#money").val();
                    $.ajax({
                        url: g.ctx + '/admin/sys/company/rechangeInsuranceAmount',
                        data: {id: id, money: money},
                        beforeSend: function () {
                            layer.close(index);
                        },
                        success: function (data) {
                            layer.msg(data.msg);
                            table.ajax.reload();
                        }
                    })
                }
            });
        });
    });
    $('.search').on('click', function () {
        table.ajax.reload();
    })
});