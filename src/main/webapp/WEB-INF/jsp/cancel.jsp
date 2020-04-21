<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<h1 class="cover-heading">再见</h1>
<br/>
<p class="lead">
    你可以随时取消自动打卡<br/>
    我将删除你的数据，并且取消你的自动打卡~<br/>
</p>
<br/>
<p class="lead"> <a href="javascript:void(0)" id="endStart" style="cursor: pointer" class="btn btn-lg btn-secondary">再见</a></p>

<!--表单隐藏域---start-->
<form hidden id="endForm" style="color: #333;text-align: left">
    <div class="form-group">
        <label for="num">学号:</label>
        <input type="number" class="form-control" id="num" name="num" placeholder="你的学号" >
    </div>
    <div class="form-group">
        <label for="pwd">密码:</label>
        <input type="password" class="form-control" id="pwd" name="pwd" placeholder="你的信息门户的密码" >
    </div>
</form>
<!--表单隐藏域---end-->


<%--=============================引入js开始=============================--%>
<script src="https://cdn.bootcss.com/sweetalert/2.1.2/sweetalert.min.js"></script>
<script src="static/js/cancel.js"></script>
<%--=============================引入js结束=============================--%>