<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1 class="cover-heading">再见吧！@艾特君</h1>
<br/>
<p class="lead">
    你是否对班委愤怒的@感到深深的恐惧？<br/>
    你是否厌烦了今日校园无止休、无反馈的强制打卡？<br/>
    你是否在想尽情放松的时刻面对可能随时出现的打卡感到深深的无力？<br/>
</p>
<br/>
<p class="lead"> <a href="javascript:void(0)" id="getStart" style="cursor: pointer" class="btn btn-lg btn-secondary">解脱</a></p>

<!--表单隐藏域---start-->
<form hidden id="startForm" style="color: #333;text-align: left">
    <div class="form-group">
        <label for="email">邮箱(用于接收通知):</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="用于接收打卡提示的邮箱">
    </div>
    <div class="form-group">
        <label for="num">学号:</label>
        <input type="number" class="form-control" id="num" name="num" placeholder="你的学号" >
    </div>
    <div class="form-group">
        <label for="pwd">密码:</label>
        <input type="password" class="form-control" id="pwd" name="pwd" placeholder="你的信息门户的密码" >
    </div>
    <div class="form-group">
        <label for="teacher">辅导员姓名(一定要填对吼！):</label>
        <input type="text" class="form-control" id="teacher" name="teacher" placeholder="请输入真实姓名" >
    </div>
    <div class="form-group">
        <label for="location">位置(格式：xx省/xx市/xx县,一定要写对吼！):</label>
        <input type="text" class="form-control" id="location" name="location" placeholder="xx省/xx市/xx县" >
    </div>
    <div class="form-group" id="isAtJradio">
        <label>是否在天津:</label>
        <label class="radio-inline"><input type="radio" name="isAtTianJin" value="true">是</label>
        <label class="radio-inline"><input type="radio" name="isAtTianJin" checked="checked" value="false">否</label>
    </div>
</form>
<!--表单隐藏域---end-->


<%--=============================引入js开始=============================--%>
<script src="https://cdn.bootcss.com/sweetalert/2.1.2/sweetalert.min.js"></script>
<script src="static/js/index.js"></script>
<%--=============================引入js结束=============================--%>