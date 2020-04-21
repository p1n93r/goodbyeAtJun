<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="bp" uri="/setBasePath" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="p1n93r">
    <title>再见吧！@艾特君</title>
    <bp:basePath request="<%=request%>"/>
    <!-- Bootstrap core CSS -->
    <link href="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/css/bootstrap.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="static/css/cover.css" rel="stylesheet">
    <%--jq需要先加载，为了后面的子页面js能正常解析--%>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>

</head>
<body>
<div class="site-wrapper">
    <div class="site-wrapper-inner">
        <div class="cover-container">
            <!--右侧导航栏--start-->
            <div class="masthead clearfix">
                <div class="inner">
                    <h3 class="masthead-brand">P1n93r</h3>
                    <nav class="nav nav-masthead">
                        <a class="nav-link active" href="index">项目介绍</a>
                        <a class="nav-link" href="cancel">取消打卡</a>
                        <a class="nav-link" href="author">作者</a>
                    </nav>
                </div>
            </div>
            <!--右侧导航栏--end-->

            <div class="inner cover" id="main-content">
                <!--页面主内容区--start-->
                <jsp:include page="index.jsp"/>
                <!--页面主内容区--end-->
            </div>


            <!--底部链接--start-->
            <div class="mastfoot">
                <div class="inner">
                    <p>Powered by <a href="mailto:1725367974@qq.com">p1n93r.</a></p>
                </div>
            </div>
            <!--底部链接--end-->

        </div>
    </div>
</div>


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://cdn.bootcss.com/popper.js/1.16.1/umd/popper.min.js"></script>
<script src="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
<%--自定义js--start--%>
<script src="static/js/base.js"></script>
<%--自定义js--end--%>
</body>
</html>