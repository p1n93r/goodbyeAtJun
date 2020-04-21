$(document).ready(function() {
    /**
     * 点击选项菜单，显示内容局部刷新
     * @author p1n93r
     */
    $(".nav-masthead a").click(function (e) {
        //ajax请求页面,并且填充右侧主内容
        $.post($(this).attr('href'), function (data) {
            $("#main-content").html(data);
        });
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
        //阻止超链接的跳转
        e.preventDefault();
    });



});