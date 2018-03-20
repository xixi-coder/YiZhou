/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    $('#ad_form').validator({
        // debug: true,
        stopOnError: true,
        rules: {
            number: [/^[0-9]*$/, "请输入数字"],
            // url: [/^((https?|ftp|news):\/\/)?([a-z]([a-z0-9\-]*[\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\/[a-z0-9_\-\.~]+)*(\/([a-z0-9_\-\.]*)(\?[a-z0-9+_\-\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$/, "请输入正确的网址"]
        },
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            // 'ad.url': 'url',
            'ad.default_flag': 'required',
            'ad.sort': 'required;number',
            'ad.type': 'required'
        },
        valid: function (form) {
            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            if ($('#app_type').val() == 0) {
                layer.msg("请选择所客户端类型");
                return;
            }
            if ($('#url').val() == 0) {
                layer.msg("请选择广告网址");
                return;
            }
            var paths = $('.image-path');
            var pathStr = '';
            $(paths).each(function (index, val) {
                if (index == (paths.length - 1)) {
                    pathStr += $(val).val();
                } else {
                    pathStr += $(val).val() + ';';
                }
            });
            $('#pic').val(pathStr);
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/ad/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/ad';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
    uploadPlug.create("#picker", $('#portrait_show'), $('#pic'));
});
function del(target) {
    $("#file").attr("disabled", false);
    $($(target).parent().parent()).remove();
}
var uploadPlug = {
    create: function (picker) {
        var uploader = WebUploader.create({
            pick: picker,//图片选择琪
            auto: true,//自动上传
            server: g.ctx + '/file',//服务器地址
            fileSingleSizeLimit: 2 * 1024 * 1024,
            duplicate: true,
            accept: {
                title: '图片文件',
                extensions: 'gif,jpg,jpeg,bmp,png'
            }
        });
        uploader.on('uploadSuccess', function (file, data) {
            var html = tpl.bind($('#imageTmp'), {path: data.data.path});
            $('#imageList').append(html);
            $("#file").attr("disabled", "disabled");
            layer.closeAll('page');
        });
        uploader.on('error', function (errorCode, limitSize, file) {
            if (errorCode == 'F_EXCEED_SIZE') {
                layer.closeAll();
                layer.alert('请上传2M以内头像',
                    {ico: 2}
                )
            } else if (errorCode == 'Q_TYPE_DENIED') {
                layer.closeAll();
                layer.alert('请上传图片文件',
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

