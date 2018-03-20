/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    $("select").each(function (index, val) {
        $(val).val($(val).data("value"));
        $(val).trigger('change.select2');
    });
    if ($('#osType').data("value") == 2) {
        $(".iOS").show();
        $(".android").hide();
    } else {
        $(".android").show();
        $(".iOS").hide();
    }
    $('#osType').on('select2:select', function (evt) {
        // Do something
        if ($('#osType').val() == 2) {
            $(".iOS").show();
            $(".android").hide();
        } else {
            $(".android").show();
            $(".iOS").hide();
        }
    });
    $('#version_form').validator({
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
            'version.version_no': 'required;',
            'version.version': 'required;number'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/version/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/version';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });
    uploadPlugq.create("#picker", $('#portrait_show'), $('#file_path'));

});
var uploadPlugq = {
    create: function (picker, $picturesho, $pictureval) {
        var uploader = WebUploader.create({
            pick: picker,
            auto: true,
            server: g.ctx + '/file',
            fileSingleSizeLimit: 100 * 1024 * 1024,
            accept: {
                title: '文件',
                extensions: 'apk'
            }
        });
        uploader.on('uploadSuccess', function (file, data) {
            $picturesho.attr("src", g.ctx + '/' + data.data.path);
            $pictureval.val(data.data.path);
            layer.closeAll('page');
        });
        uploader.on('error', function (errorCode, limitSize, file) {
            if (errorCode == 'F_EXCEED_SIZE') {
                layer.closeAll();
                layer.alert('请上传2M以内文件',
                    {ico: 2}
                )
            } else if (errorCode == 'Q_TYPE_DENIED') {
                layer.closeAll();
                layer.alert('请上传apk文件',
                    {ico: 2}
                )
            }
        });
        uploader.on('uploadProgress', function (a, b, c) {
            $('#uploadProgress').css("width", (b * 100) + '%');
        });
        uploader.on('uploadBeforeSend', function (a, b, c) {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0, //不显示关闭按钮
                shift: 2,
                shadeClose: false, //开启遮罩关闭
                area: ['100px', '20px'], //宽高
                content: '<div style="height: 20px; width: 100%;">' +
                '<div style="margin-bottom: 0px; border-radius:2px; " class="progress no-rounded progress-striped"><div id="uploadProgress" class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="1" aria-valuemin="0" aria-valuemax="100" style="width: 0%">' +
                '<span class="sr-only">40% Complete (success)</span>' +
                '</div></div></div>'
            });
        });
    }
};

