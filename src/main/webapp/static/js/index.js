$(document).ready(function() {


    /**
     * 点击了最后的继续
     */
    $("#getStart").click(function (e) {
        //创建一个p节点(dom节点)
        var tipNode = document.createElement("p");
        tipNode.style.color="#333";
        tipNode.innerHTML = "接下来你填入的密码将加密保存到数据库<br/>" +
            "<strong>但此加密可被解密</strong>,否则程序不能自动登录今日校园<br/>" +
            "也就是你得足够信任我的编码能力(程序没有泄露数据的漏洞)<br/>" +
            "足够信任我的个人人品(不会去想着解密你的密码)<br/>" +
            "是否继续使用？";

        var form = document.getElementById("startForm");

        //声明待发送的数据
        var postData={};
        swal({
            title: "须知",
            content:tipNode,
            icon: "warning",
            buttons: ["不继续","继续"],
            dangerMode: true,
        })
        .then((ok) => {
            if (ok) {
                //==================填写关键数据--start==================
                //深克隆到临时变量，防止form被清除
                formTemp=form.cloneNode(true);
                formTemp.hidden=false;
                swal({
                    title: "请填写数据",
                    content:formTemp,
                    icon: "warning",
                    buttons: ["不继续","继续"],
                    dangerMode: true,
                })
                .then((ok) => {
                    if (ok) {
                        //获取数据
                        var d = {};
                        var t = $(formTemp).serializeArray();
                        $.each(t, function() {
                            d[this.name] = this.value;
                        });
                        //ajax发送数据
                        $.ajax({
                            url : 'addUser',
                            contentType: 'application/json;charset=utf-8',
                            data : JSON.stringify(d),
                            type : 'POST',
                            success : function(data) {
                                if(data.result){
                                    swal("成功！", data.msg, "success");
                                }else{
                                    swal("失败", data.msg, "error");
                                }
                            },
                            error: function (e) {
                                swal("失败", "意外失败，请检查网络！", "error");
                            }
                        });

                    } else {
                        swal("取消", "你已停止继续，有幸再会！", "info");
                    }
                });
                //==================填写关键数据--end==================
            } else {
                swal("取消", "你已停止继续，有幸再会！", "info");
            }
        });


    });







});