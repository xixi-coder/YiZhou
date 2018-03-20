/**
 * Created by admin on 2016/10/17.
 */
$(document).ready(function () {
    var select = $("select");
    $(select).each(function (index, val) {
        $(val).val($(val).data("value")).trigger("change");
    });
    var url = {
        celAmount: g.ctx + '/admin/special/celAmount',
        create: g.ctx + '/admin/special/updateline1'
    }
        $("#updateLine").on('click', function () {
            $.ajax({
                type: 'post',
                url: url.create,
                data: $("#createLine_form").serialize(),
                success: function (data) {
                    layer.msg(data.msg);
                    if (data.status == 'OK') {
                        //alert(data.msg);
                        console.log(data.serviceTypeItems);
                        window.location.href=g.ctx + "/admin/special?type=" + data.serviceTypeItems;
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        });
    });