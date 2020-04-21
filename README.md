## 项目简介
欢迎使用本项目（再见吧！@艾特君），本项目的主要功能是今日校园自动自动打卡。

限于本人编程水平低，以及开发本项目时间短，可能项目有诸多不合理的设计，所以欢迎大家提交Pull Request，共同参与进来！

本人联系方式如下：

Email：<a href="mailto:1725367974@qq.com">1725367974@qq.com</a>

## 须知
1. 如果你需要在自己服务器运行本项目，需要修改resources下的：api/key.properties、email/email.properties、mysql/db.properties。（里面需要修改的部分我已用“xxx”打码）。
2. 自动打卡的核心代码在utils.TodayAppUtils工具类内，如果发生了问卷调查题目变化，也是从中修改。（设计的不好，应该通过加载属性文件的方式来适配问卷题目变化）。
3. 定时任务的设置在resources/spring/applicationContextTask内，对应的Task类在包task下。（防止同一时间Task执行多次，此处避免用注解）。