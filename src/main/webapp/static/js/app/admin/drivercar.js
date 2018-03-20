/**
 * Created by Administrator on 2016/9/20.
 */
$(document).ready(function () {

    var url = g.ctx + "/admin/member/driver/carlist?id=" + id;
    var columns = [
        {"data": "bname"},
        {
            "data": "status",
            'render': function (a, b, c) {
                if (a == 1) {
                    return '<p class="text-success">审核通过</p>';
                } else if (a == 2) {
                    return '<p class="text-danger">审核不通过</p>';
                } else {
                    return '<p class="text-warning">未审核</p>';
                }
            }
        },
        {
            'data': 'sname'
        },
        {"data": "mname"},
        //{"data": "mdescription"},
        {"data": "plate_no"},
        {"data": "vin"},
        {"data": "distance"},
        {"data": "create_time"},
        {"data": "join_time"},
        //{"data":"province"},
        //{"data":"city"},

        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {car_id: row.car_id, status: row.status,plateNo: row.plate_no});
            }
        }
    ];

    var table = dt.build($('#drivercar-list'), url, columns, function () {
        $(".audit").on('click', function (a) {
            var status = $(a.target).attr("status");
            var car_id = $(a.target).data("id");
            var index = layer.open({
                type: 0,
                title: '司机审核',
                content: tpl.bind($('#audit_tpl')),
                btn: ['审核通过', '审核不通过'],
                yes: function (index, layero) {
                    $('#audit_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/driver/caraudit/1-' + car_id,
                                data: $(form).serialize(),
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        var id = data.data;
                                        layer.msg(data.msg);
                                        window.location.href = g.ctx + '/admin/member/driver/drivercar/' + id;
                                    } else {
                                        layer.alert(data.msg);
                                        //window.location.href=g.ctx+'/admin/member/driver';
                                    }
                                }
                            });
                        }
                    });
                    $('#audit_form').trigger('submit');
                },
                btn2: function (index, layero) {
                    $('#audit_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/driver/caraudit/0-' + car_id,
                                data: $(form).serialize(),
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        var id = data.data;
                                        layer.msg(data.msg);
                                        window.location.href = g.ctx + '/admin/member/driver/drivercar/' + id;
                                    } else {
                                        layer.alert(data.msg);
                                        //window.location.href=g.ctx+'/admin/member/driver';
                                    }
                                }
                            });

                        }
                    });
                    $('#audit_form').trigger('submit');
                }
            });
        });


        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/delete',
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
    });
})