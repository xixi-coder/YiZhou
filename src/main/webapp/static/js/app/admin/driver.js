/**
 * Created by Administrator on 2016/9/14.
 */
var table;
$(document).ready(function () {
    var url = g.ctx + "/admin/member/driver/list";
    var columns = [
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status: row.status,
                    login_id: row.login_id,
                    realName: row.real_name,
                    amount: row.amount,
                    inst_phone: row.inst_phone,
                    level:row.level
                });
            }
        },
        {"data": "real_name"},
        {"data": "user_name"},

        {
            "data": "statusg",
            'render': function (a, b, c) {
                if (a == 1) {
                    return '<span class="label label-primary">已登录</span>';
                } else if (a == 0 || a == null) {
                    return '<span class="label label-default">未登录</span>';
                } else if (a == 3) {
                    return '<span class="label label-success">上线中</span>';
                } else {
                    return '<span class="label label-warning">忙碌中</span>';
                }
            }
        },
        {
            "data": "status",
            "render": function (a) {
                if (a == null) {
                    return '<span class="label label-warning">未审核</span>';
                } else if (a == 1) {
                    return '<span class="label label-success">审核通过</span>';
                } else if (a == 2) {
                    return '<span class="label label-default">审核未通过</span>';
                } else if (a == 3) {
                    return '<span class="label label-danger">冻结</span>';
                }
            }
        },
        {"data": "address"},
        {"data": "cname"},
        {"data": "phone"},
        {"data": "inst_phone"},
        {"data": "amount"},
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
        // {"data":"last_update_time"},
        {
            "data": "type",
            "render": function (a) {
                return a.replace('1', '专车')
                    .replace('2', '代驾')
                    .replace('3', '出租车')
                    .replace('4', '快车')
                    .replace('5','顺风车')
                    .replace('6','城际专线')
                    .replace('7','航空专线');
            }
        },
        {"data": "create_time"},
        {"data": "last_login_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status: row.status,
                    login_id: row.login_id,
                    realName: row.real_name,
                    amount: row.amount,
                    inst_phone: row.inst_phone,
                    level:row.level
                });
            }
        }
    ];

    table = dt.build($('#driver-list'), url, columns, function () {
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
                                url: g.ctx + '/admin/member/driver/updatepd',
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
            layer.confirm('是否将密码重置为“123123”?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/repw',
                    data: {'id': id},
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
        $('.delinst').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除绑定？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/delinst',
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

        $('.amount').on('click', function (a) {
            var id = $(a.target).data("id");
            var realName = $(a.target).data("realname");
            console.log($(a.target));
            var amount = $(a.target).data("amount");
            layer.open({
                type: 0,
                title: '司机充值-姓名:<span style="color: red;">' + realName + '</span>,余额:<span style="color: red;">' + amount + '</span>元',
                content: tpl.bind($('#amount_tpl'), {id: id}),
                yes: function (index, layero) {
                    $('#amount_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#amount_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/driver/recharge/' + id,
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

        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            //alert(id);
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/del',
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
                });
            });
        });
        $('.shunfengche').on('click',function (a) {
            var id = $(a.target).attr("data-id");
            layer.open({
                content:'是否通过该司机顺风车申请请求',
                title:'顺风车审核',
                btn: ['审核通过', '审核不通过'],
                yes: function (index, layero) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/driver/shenHe/1',
                                data: {id:id},
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        window.location.reload();
                                    } else {
                                        layer.alert(data.msg);
                                        //window.location.href=g.ctx+'/admin/member/driver';
                                    }
                                }
                            });

                },
                btn2: function (index, layero) {

                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/driver/shenHe/0',
                                data: {id:id},
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        window.location.reload();
                                    } else {
                                        layer.alert(data.msg);
                                        //window.location.href=g.ctx+'/admin/member/driver';
                                    }
                                }
                            });


                }
            });
        })

        $('.offline').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            //alert(id);
            layer.confirm('是否强制下线该司机？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/offline',
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
        $('.frozen').on('click', function (a) {
            var id = $(a.target).data("id");
            var status = $(a.target).data("status");
            var msg = '';
            if (status == 3) {
                msg = '是否解冻该司机？';
                layer.confirm(msg, {icon: 3, title: '提示'}, function (index) {
                    $.ajax({
                        url: g.ctx + '/admin/member/driver/frozen',
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
            } else if (status == 1) {
                msg = '是否冻结该司机？';
                layer.open({
                    type: 0,
                    title: '冻结司机',
                    content: tpl.bind($('#frozen_tpl'), {id:id}),
                    yes: function (index, layero) {
                        $('#frozen_form').trigger('submit');
                    }, success: function (layero, index) {
                        $('.frozenType').iCheck({
                            checkboxClass: 'icheckbox_square',
                            radioClass: 'iradio_square',
                            increaseArea: '20%' // optional
                        });
                        $('.frozenType').on('ifChecked', function (target) {
                            var frozenType = $(target.currentTarget).val();
                            if(frozenType==1){
                                $(".qixianxuanze").show();
                            }else{
                                $(".qixianxuanze").hide();
                            }
                        });
                        $('#frozen_form').validator({
                            valid: function (form) {
                                $.ajax({
                                    type: 'post',
                                    url: g.ctx + '/admin/member/driver/frozen',
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
            }

        });
        $('.resetdevice').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.confirm('是否重置该司机设备信息', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/resetDevice',
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
        $('.resetcompany').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.confirm('是否重置该服务人员公司信息', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/resetCompany/',
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
    });

    $(".search").on('click', function () {
        table.ajax.reload();
    });
    $("#noAduit").on('click', function () {
        $('select[name="s-info.status-EQ"]').val("ISNULL");
        table.ajax.reload();
    });
    $("#shunfengche").on('click',function () {
        $("#shunfen").val("1")
        table.ajax.reload();
    });
});