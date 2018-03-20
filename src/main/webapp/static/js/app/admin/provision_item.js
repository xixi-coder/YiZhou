/**
 * Created by Administrator on 2016/9/9.
 */
var url = {
    save: g.ctx + '/admin/sys/provision/save'
}
$(document).ready(function () {
    var editor;
    KindEditor.ready(function (K) {
        editor = K.create('#desc', {
            uploadJson: g.ctx + '/file/edit',
            allowFileManager: false
        });
    });

    $('#provision_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'desc': 'required'
        },
        valid: function (form) {
            var desc = editor.html();
            $("#descInput").val(desc);
            $.ajax({
                type: 'post',
                url: url.save,
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.history.back().reload();
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
});