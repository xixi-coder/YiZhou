/**
 * Created by Administrator on 2016/9/12.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/list/list";
    var columns = [
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status: row.status,
                    realName: row.real_name,
                    amount: row.amount,
                    inst_phone: row.inst_phone
                });
            }
        },
        {"data": "real_name"},
        {"data": "user_name"},
        {
            "data": "address",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        {
            "data": "cname",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        {"data": "phone"},
        {
            "data": "inst_phone",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        {"data": "amount"},
        {
            "data": "vipAmount",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        {
            "data": "vipHistoricalAmount",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        //{"data":"email"},
        {
            "data": "gender",
            'render': function (a, b, c) {
                if (a == 0) {
                    return '女';
                } else {
                    return '男';
                }
            }
        },
        //{"data":"type"},
        // {"data":"urgent_phone"},
        //{"data":"urgent_name"},
        //{"data":"post_code"},
        // {"data":"device_no"},
        //{"data":"device_type"},
        {
            "data": "create_time",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        {
            "data": "last_login_time",
            'render': function (a) {
                if (a == null) {
                    return '--';
                } else {
                    return a;
                }
            }
        },
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status: row.status,
                    realName: row.real_name,
                    amount: row.amount,
                    inst_phone: row.inst_phone,
                    vipAmount: row.vipAmount
                });
            }
        }
    ];

    var table = dt.build($('#mem-list'), url, columns, function () {
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
                                url: g.ctx + '/admin/member/list/updatepd',
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
        $('.delinst').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除绑定？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/list/delinst',
                    data: {'id': id},
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
        $('.rePw').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否将密码重置为“123123”?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/list/repw',
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
        $('.amount').on('click', function (a) {
            var id = $(a.target).data("id");
            var realName = $(a.target).data("realname");
            var amount = $(a.target).data("amount");
            layer.open({
                type: 0,
                title: '会员充值-姓名:<span style="color: red;">' + realName + '</span>,余额:<span style="color: red;">' + amount + '</span>元',
                content: tpl.bind($('#amount_tpl'), {id: id}),
                yes: function (index, layero) {
                    $('#amount_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#amount_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/list/recharge/' + id,
                                data: $(form).serialize(),
                                beforeSend: function () {
                                    layer.closeAll();
                                },
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        table.ajax.reload();
                                    } else {
                                        layer.tips(data.msg);
                                    }
                                }
                            });
                        }
                    });
                }
            })
        });
        $('.addinst').on('click', function (a) {
            var id = $(a.target).data("id");
            var realName = $(a.target).data("realname");
            var amount = $(a.target).data("amount");
            layer.open({
                type: 0,
                title: '会员-姓名:<span style="color: red;">' + realName + '</span>',
                content: tpl.bind($('#addinst_tpl'), {id: id}),
                yes: function (index, layero) {
                    $('#addinst_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#addinst_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/list/addinst?id=' + id,
                                data: $(form).serialize(),
                                beforeSend: function () {
                                    layer.closeAll();
                                },
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        table.ajax.reload();
                                    } else {
                                        layer.tips(data.msg);
                                    }
                                }
                            });
                        }
                    });
                }
            })
        });
        $('.resetdevice').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.confirm('是否重置该客户设备信息', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/list/resetDevice',
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

        $('.resetcompany').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.confirm('是否重置该客户公司信息', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/list/resetCompany/' + id,
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

        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            //alert(id);
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/list/del',
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

        $('.blackAdd').on('click', function (a) {
            var id = $(a.target).data("id");
            var status = $(a.target).data("status");
            var title = '';
            if (status == 1) {
                title = "是否将该用户设为异常用户";
            } else if (status == 2) {
                title = "是否将该用户设为正常用户";
            }
            layer.confirm(title, {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/list/black/' + id,
                    type: 'POST',
                    success: function (data) {
                        layer.msg(data.msg);
                        table.ajax.reload();
                    }
                })
            });
        });
    });


    $(".search").on('click', function () {
        table.ajax.reload();
    });
});