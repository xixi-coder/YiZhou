var username = "";
var i = 0;
var INum = 0;

$(function () {
    window.onresize = function(){
        $('#BgImg').height($(window).height());
    }
    $('#BgImg').height($(window).height());
    // setInterval(function () {
    //     $('#BgImg').height($(window).height() - 10);
    //     $('#BgImg1').height($(window).height() - 10);
    //     if (i == 0) {
    //         $('#BgImg').show();
    //         $('#BgImg1').hide();
    //         i = 1;
    //     } else {
    //         $('#BgImg1').show();
    //         $('#BgImg').hide();
    //         i = 0;
    //     }
    // }, 3000);
    login.portarait($("#username").val());
    $("#username").on("change", function (target) {
        var tmpUserName = $(target.target).val();
        login.portarait(tmpUserName);
    });
    $('#login').on('click', function () {
            var option = {
                success: login.success,
                beforeSubmit: login.beforeSubmit,
                url: g.ctx + '/admin/save',
                timeout: 10000
            };
            $('#loginForm').ajaxForm(option);


    });
});
var login = {

    success: function (data) {
       // debugger;
        //console.log($.cookie("INum"));

        if (data.status == 'OK') {
            layer.closeAll("loading");
            window.location.href = g.ctx + '/admin/dashboard'
        } else {
            layer.closeAll("loading");
            $("#captcha").attr("src", g.ctx + "/admin/captcha?" + Math.random());
            if(data.msg=="密码错误"){

                var expiresDate = new Date();
                expiresDate.setTime(expiresDate.getTime()+(60*60*1000));//只能这么写，10表示10秒钟
                if($.cookie("INum")!=undefined){
                    INum= $.cookie("INum");
                    INum++;
                    $.cookie("INum", INum,{expires:expiresDate});

                }else{
                    $.cookie("INum", INum++,{expires:expiresDate});
                }
            }
            if( $.cookie("INum")>=3){
                debugger;
                layer.msg("您已经三次输入错误，请等待一个小时后再尝试登陆！",function () {
                });
            }else{

                layer.msg(data.msg, function () {
                });
            }

        }
    }, beforeSubmit: function () {
        layer.load(2);
    },
    portarait: function (tmpUserName) {
        if (username != tmpUserName) {
            username = tmpUserName;
            $.ajax({
                type: "GET",
                url: g.ctx + '/admin/common/portrait',
                data: {username: username},
                success: function (data) {
                    if (data.status == 'OK') {
                        $("#portrait").attr("src", data.msg);
                    }else{
                        $("#portrait").attr("src", g.ctx + '/static/images/default-user.jpg');
                    }
                }
            });
        }
    }
}
