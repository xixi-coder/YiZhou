//城市三级联动
$(document).ready(function () {
    uploadPlug.create('#picker', $('#id_Card'), $('#idCard'));
    uploadPlug.create('#picker1', $('#driver_license'), $('#driverlicense'));
    uploadPlug.create('#picker2', $('#driving_license'), $('#drivinglicense'));
    //uploadPlug.create('#picker3', $('#qualification_certificate'), $('#qualification'));

    /* $("#picker").click(function(){
     uploadPlug.create('#picker', $('#drivered_license'), $('#driveredlicense'));
     });
     $("#picker1").click(function(){
     uploadPlug.create('#picker1', $('#driver_license'), $('#driverlicense'));
     });
     $("#picker2").click(function(){
     uploadPlug.create('#picker2', $('#driving_license'), $('#drivinglicense'));
     });*/

    $('#province').change(function (e) {
        area.change($('#city'), 'city', $(e.currentTarget).val());
        $('#county').empty();
        $('#county').append('<option value="0">请选择</option>');
    });
    $('#city').change(function (e) {
        area.change($('#county'), 'district', $(e.currentTarget).val());
    });

    var area = {
        change: function ($select, level, parent) {
            $select.empty();
            $.ajax({
                url: g.ctx + '/driver/enroll/area',
                data: {level: level, parent: parent},
                success: function (data) {
                    $select.append('<option value="0">请选择</option>');
                    $(data.data).each(function (index, val) {
                        $select.append('<option value=' + val.adcode + '>' + val.name + '</option>');
                    });
                }
            })
        }
    }
});

//点击获取验证码
$('#sendSms').on('click', function () {
    var timer = '';
    var $phone = $('#phone');
    var reg_phone = /^1[3-9]\d{9}$/;
    if (typeof($phone.val()) == "undefined" || $.trim($phone.val()) == '') {
        layer.tips('手机号不能为空!', '#phone');
        $phone.focus();
        return;
    }
    if (!reg_phone.test($phone.val())) {
        layer.tips('手机号格式不正确!', '#phone');
        $phone.focus();
        return;
    }
    $.ajax({
        url: g.ctx + '/api/smscode',
        data: {phone: $phone.val(), type: 1},
        headers: {
            'appType': 1,
            'devicetype': 3
        },
        success: function (data) {
            if (data.status == 'OK') {
                layer.msg("短信验证码已发送！");
                $("#sendSms").attr("disabled", "disabled");
                timer = setInterval(timerEvent, 1000)
            } else {
                layer.msg(data.msg);
            }
        }
    });

    //60秒倒计时定时器
    var countdown = 60;
    var timerEvent = function () {
        var $target = $('#sendSms');
        if (countdown == 0) {
            $("#sendSms").attr("disabled", false);
            $target.val("免费获取");
            countdown = 60;
            clearInterval(timer);
        } else {
            $target.val("重新发送(" + countdown + ")");
            countdown--;
        }
    }
});

//提交
$('#tijiao').on('click', function () {
    save.formSubmit();
});
var save = {
    formSubmit: function () {
        var $name = $('#name');
        var $phone = $('#phone');
       /* var $tphone = $('#tphone');*/
        var $gender = $('input[name="sex"]:checked');
        var reg_phone = /^1[3-9]\d{9}$/;
        var $type = $('input[type="checkbox"]:checked');
        var $idCard = $('#id_Card');
        var $driverLicense = $('#driver_license');
        var $drivingLicense = $('#driving_license');
        var $qualification = $('#qualification_certificate');

        var $smsCode = $('#smsCode');
        if (typeof($name.val()) == "undefined" || $.trim($name.val()) == '') {
            layer.tips('姓名不能为空!', '#name');
            $name.focus();
            return;
        }
        if (typeof($gender.val()) == 'undefined' || $.trim($gender.val()) == '') {
            layer.tips('请选择性别!', '#sex');
            return
        }

        if (typeof($phone.val()) == "undefined" || $.trim($phone.val()) == '') {
            layer.tips('手机号不能为空!', '#phone');
            $phone.focus();
            return;
        }
        if (!reg_phone.test($phone.val())) {
            layer.tips('手机号格式不正确!', '#phone');
            $phone.focus();
            return;
        }
        if (typeof($smsCode.val()) == "undefined" || $.trim($smsCode.val()) == '') {
            layer.tips('短信验证码不能为空!', '#smsCode');
            $phone.focus();
            return;
        }
        if ($type.length <= 0) {
            layer.tips('请选择类型!', '#types');
            return
        }
        /*if ($tphone.val() != '' && !reg_phone.test($tphone.val())) {
            layer.tips('手机号不能为空!', '#tphone');
            $phone.focus();
            return;
        }*/
        if (typeof($idCard.val()) == 'undefined' || $.trim($idCard.val()) == '') {
            layer.tips('请上传身份证照片!', '#idCard');
            return
        }
        if ($type.length != 1 || $type.val() != 2) {
            if (typeof($drivingLicense.val()) == 'undefined' || $.trim($drivingLicense.val()) == '') {
                layer.tips('请上传行驶证照片!', '#drivingLicense');
                return
            }
        }
        if (typeof($driverLicense.val()) == 'undefined' || $.trim($driverLicense.val()) == '') {
            layer.tips('请上传驾驶证照片!', '#driverLicense');
            return
        }
        /*if (typeof($qualification.val()) == 'undefined' || $.trim($qualification.val()) == '') {
            layer.tips('请上传从业资格证照片!', '#qualification');
            return
        }*/
        var typeStr = '';
        $type.each(function (index, val) {
            if (index == $type.length - 1) {
                typeStr = typeStr + $(val).val();
            } else {
                typeStr = typeStr + $(val).val() + ";";
            }
        });
        var province = $("#province").val();
        var city = $("#city").val();
        var county = $("#county").val();
        var cityCode = '';
        if (province == 0) {
            layer.tips('请选择区域!', '#province5');
            $phone.focus();
            return;
        }
        if (county != 0) {
            cityCode = county;
        } else if (city != 0) {
            cityCode = city;
        } else if (province != 0) {
            cityCode = province;
        }

        var registerIp = $("#registerIp").val();
        var registerIMSI = $("#registerIMSI").val();
        var registerIMEI = $("#registerIMEI").val();
        var macAddress = $("#macAddress").val();
        var port = $("#port").val();

        $.ajax({
            url: g.ctx + '/driver/enroll/save',
            data: {
                name: $name.val(),
                gender: $gender.val(),
                /*tphone: $tphone.val(),*/
                phone: $phone.val(),
                type: typeStr,
                smsCode: $smsCode.val(),
                idCard: $idCard.val(),
                driverLicense: $driverLicense.val(),
                drivingLicense: $drivingLicense.val(),
                qualification: $qualification.val(),
                areaCode: cityCode,
                registerIp: registerIp,
                registerIMSI: registerIMSI,
                registerIMEI: registerIMEI,
                macAddress: macAddress,
                port:port
            },
            headers: {
                'appType': 1,
                'devicetype': 3
            }, beforeSend: function () {
                $("#tijiao").prop("disabled", "true");
            },
            success: function (data) {
                if (data.status == 'OK') {
                    layer.msg(data.msg);
                    var u = navigator.userAgent;
                    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
                    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
                    if (isAndroid) {
                        javascript:JavaScriptInterface.closeWindow();
                    }
                    if (isiOS) {
                        webViewClose();
                    }
                } else {
                    layer.msg(data.msg);
                    $("#tijiao").prop("disabled", "");
                }
            }
        });
    }
}

//上传图片
var uploadPlug = {
    create: function (picker, $pictureval, $opacity) {
        var uploader = WebUploader.create({
            pick: picker,//图片选择琪
            auto: true,//自动上传
            server: g.ctx + '/file',//服务器地址
            fileSingleSizeLimit: 8 * 1024 * 1024,
            accept: {
                title: '图片文件',
                extensions: 'gif,jpg,jpeg,bmp,png'
            }
        });
        uploader.on('uploadSuccess', function (file, data) {
            $pictureval.val(data.data.path);
            if ($pictureval.val()) {
                $opacity.css("opacity", "1");
            }
            layer.closeAll();
        });
        uploader.on('error', function (errorCode, limitSize, file) {
            if (errorCode == 'F_EXCEED_SIZE') {
                layer.closeAll();
                layer.msg('请上传8M以内的图片文件')
            } else if (errorCode == 'Q_TYPE_DENIED') {
                layer.closeAll();
                layer.msg('请上传图片文件')
            }/*else if(errorCode == 'F_DUPLICATE'){
             layer.closeAll();
             layer.msg('文件重复上传');
             }*/
            else {
                layer.closeAll();
                layer.msg('上传图片文件发生错误' + errorCode)
            }
        });
        uploader.on('uploadBeforeSend', function (a, b, c) {
            layer.load(1, {shade: [0.8, '#393D49']})
        });
    }
}