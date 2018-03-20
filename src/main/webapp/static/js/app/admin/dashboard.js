/**
 * Created by admin on 2016/11/1.
 */
$(document).ready(function () {
    webSocket.init(order.add);
    // layer.load(2, {
    //     shade: [1,'#fff'] //0.1透明度的白色背景
    // });
    if ($($("#navbar > ul > li > a")[0]).attr("href") != "#") {
        window.location.href = $($("#navbar > ul > li > a")[0]).attr("href");
    } else {
        window.location.href = $($("#navbar > ul > li > ul > li > a")[0]).attr("href");
    }
});
var order = {
    add: function (data) {
        console.log(data);
        var order = eval('(' + data + ')').data;
        console.log(order);
        var typeTitle = (order.service_type + '').replace("1", '专车').replace("2", '代驾').replace("3", '出租车').replace("4", '快车')
        var html = tpl.bind($("#orderItem"), {id: order.id, name: order.real_name, type: typeTitle, time: order.time});
        $("#newOrder").append(html);
    }
}