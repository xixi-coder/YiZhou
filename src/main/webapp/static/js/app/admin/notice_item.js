/**
 * Created by Administrator on 2016/9/9.
 */
var url = {
    memberList: g.ctx + '/admin/sys/notice/memberList',
    save: g.ctx + '/admin/sys/notice/save'
}
var userMap = new HashMap();
var ztreeResource;
var zNodes = [];
var setting = {};
var type=0;
$(document).ready(function () {
    $('.sendTime').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        format: 'yyyy-mm-dd hh:ii',
        startDate: new Date(),
        minuteStep: 1
    });
    uploadPlug.create("#picker", $('#portrait_show'), $('#head_portrait'));
    $("#type").val($("#type").data("val")).trigger("change");
    if ($("#type").data("val") == 2) {
        $('.notice').show();
        $('.pulish').hide();
    } else {
        $('.pulish').show();
        $('.notice').hide();
    }
    $("#type").on('change', function (target) {
        if ($(target.currentTarget).val() == 2) {
            $('.notice').show();
            $('.pulish').hide();
        } else {
            $('.pulish').show();
            $('.notice').hide();
        }
    });
    $(".reciverType").on('ifChecked', function (event) {
        var selectType = $(event.target).val();
        var table;
        if (selectType == -5) {//指定司机推送
            userMap = new HashMap();
            notic.showUser();
            type = 1;
            notic.loadMemberList();
        } else if (selectType == -4) {//指定用户
            type = 2;
            userMap = new HashMap();
            notic.showUser();
            notic.loadMemberList();
        } else {
            userMap = new HashMap();
            notic.showUser();
            $("#memberList").hide();
        }
    });

    $('#notice_form').validator({
        // debug: true,
        stopOnError: true,
        rules: {
            number: [/^[0-9]*$/, "请输入数字"]
        },
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'notice.title': 'required',
            'notice.content': 'required'
        },
        valid: function (form) {
            var times = $(".datetimepicker-input");
            $(times).each(function (index, val) {
                if ($(val).val() != '') {
                    $(val).val($(val).val() + ':00');
                }
            });
            var sendTimes = $(".sendTime");
            $(sendTimes).each(function (index, val) {
                if ($(val).val() != '') {
                    $(val).val($(val).val() + ':00');
                }
            });
            if ($('#company') != undefined && $('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            var typeInfo = {};
            typeInfo["type"]=$(".reciverType:checked").val();
            var memberIds = '';
            $(userMap.values()).each(function (index,val) {
               memberIds +=val.id+";";
            });
            typeInfo["ids"]=memberIds;
            $("#memberInfo").val(JSON.stringify(typeInfo));
            $.ajax({
                type: 'post',
                url: url.save,
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/notice';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
});

var notic = {
    loadMemberList: function () {
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
                    notic.showUser();
                }
            }
        };
        $.ajax({
            url: url.memberList + '/' + type + '-' + $('#company').val(),
            data:{'condition':$("#searchCondition").val()},
            success: function (data) {
                var zNodes = data;
                $(zNodes).each(function(index,val){
                    if(userMap.get(val.id)){
                        val.checked = true;
                    }
                });
                $.fn.zTree.init($("#ztree"), setting, zNodes);
                $("#memberList").show();
            }
        });
        $('#searchBtn').on('click', function (a) {
            notic.loadMemberList();
        });
    },
    showUser: function () {
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
                $('#selectUser').val($('#selectUser').val() + "；" + val.name);
                $('#selectUserHide').val($('#selectUserHide').val() + "," + val.id);
            }
        });
    }
}