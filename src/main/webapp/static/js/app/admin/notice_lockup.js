/**
 * Created by admin on 2016/12/9.
 */
$(document).ready(function () {
    var check = $("#reciverType").val();
    $('input[name="reciverType"][value="' + check + '"]').iCheck('check');
    $("select").each(function (index, val) {
        $(val).val($(val).data("value"));
        $(val).trigger('change.select2');
    });
    $("#type").val($("#type").data("value")).trigger("change");
    if ($("#type").data("value") == 2) {
        $('.notice').show();
        $('.pulish').hide();
    } else {
        $('.pulish').show();
        $('.notice').hide();
    }
    if (check == -4 || check == -5) {
        $("#memberList").show();
    }
});