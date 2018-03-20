<!--基本引用 -->
<script src="${ctx}/static/js/jquery-1.9.1.min.js"></script>
<script src="${ctx}/static/js/bootstrap.js"></script>

<!--datepicker -->
<script src="${ctx}/static/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${ctx}/static/js/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<!--select2 -->
<script src="${ctx}/static/js/select2/js/select2.full.js"></script>
<script src="${ctx}/static/js/select2/js/i18n/zh-CN.js"></script>
<!--icheck -->
<script src="${ctx}/static/js/icheck/icheck.js"></script>
<!--datatable -->
<script src="${ctx}/static/js/datatable/jquery.dataTables.js"></script>
<script src="${ctx}/static/js/datatable/dataTables.bootstrap.js"></script>
<!--验证 -->
<script src="${ctx}/static/js/validator/jquery.validator.js"></script>
<script src="${ctx}/static/js/validator/local/zh-CN.js"></script>
<!--百度上传 -->
<script src="${ctx}/static/js/webuploader/webuploader.js"></script>
<!--AjaxForm -->
<script src="${ctx}/static/js/jquery.form.js"></script>
<!--layer -->
<script src="${ctx}/static/js/layer/layer.js"></script>
<!--ztree -->
<script src="${ctx}/static/js/ztree/jquery.ztree.all.js"></script>
<!--layerTMP模板引擎 -->
<script src="${ctx}/static/js/laytpl.js"></script>

<!--jsMap -->
<script src="${ctx}/static/js/hashmap.js"></script>
<!--初始化 -->
<script src="${ctx}/static/js/base.js"></script>
<script src="${ctx}/static/js/dict.js"></script>
<script type="text/javascript">
    var g = {
        ctx: '${ctx}',
        websocket: '${websocket}',
        user: '${_USER_.id}',
        company: '${_USER_.company}'
    }
    var menu;
</script>
<script>
    $(document).ready(function () {

        setInterval("getkcorder()", 10000);
        setInterval("getZxorder()", 10000);
    });
    function getkcorder() {
        $.ajax({
            type: "get",
            async: false, //同步请求
            url: g.ctx + "/admin/total/alarmCount",
            timeout: 1000,
            success: function (data) {
                if (data.data > 0) {
                    $("#Player").get(0).innerHTML = "<audio src='/Jfinal/static/music/4031.mp3' autoplay='false' ></audio>";
                } else {
                    $("#Player").get(0).innerHTML = "<audio src=''></audio>";
                }
            },
            error: function () {
            }
        });
    }
    function getZxorder() {
        $.ajax({
            type : "get",
            async : false, //同步请求
            url : g.ctx+"/admin/total/zxOrder",
            timeout:1000,
            success:function(data){
                if(data.data > 0){
                    $("#Player").get(0).innerHTML="<audio src='/Jfinal/static/music/neworder.mp3' autoplay='false' ></audio>";
                }else {
                    $("#Player").get(0).innerHTML="<audio src=''></audio>";
                }
                $("#number").html(data.data);
            },
            error: function() {
                // alert("失败，请稍后再试！");
            }
        });
    }
</script>