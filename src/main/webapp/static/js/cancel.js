$(document).ready(function() {


    /**
     * 点击了最后的继续
     */
    $("#endStart").click(function (e) {
        //创建一个p节点(dom节点)
        var tipNode = document.createElement("p");
        tipNode.style.color="#333";
        tipNode.innerHTML = "我需要确认是你本人在进行取消<br/>所以请你输入学号+密码进行验证<br/>验证完毕将关闭你的自动打卡，并删除你的个人数据";

        var form = document.getElementById("endForm");

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
                            url : 'delUser',
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
                        swal("取消", "很高兴继续使用！", "info");
                    }
                });
                //==================填写关键数据--end==================
            } else {
                swal("取消", "很高兴继续使用！", "info");
            }
        });


    });







});