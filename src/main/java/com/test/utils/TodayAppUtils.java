package com.test.utils;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.*;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.test.domain.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import java.io.IOException;
import java.util.HashMap;
import static com.arronlong.httpclientutil.common.util.StringUtil.regex;



/**
 * @author P1n93r
 * 今日校园打卡工具
 * 157行改判断
 */
public final class TodayAppUtils {

    private TodayAppUtils(){}

    /**
     * @author p1n93r
     * 得到单例
     * @throws HttpProcessException http异常
     * @throws IOException IO异常
     */
    public static TodayAppUtils getInstance(User user) throws Exception {
        TODAY_APP_UTILS.user=user;
        TODAY_APP_UTILS .init(user);
        return TODAY_APP_UTILS ;
    }


    /**
     * @return 判断用户是否有效
     */
    public static Boolean isUserUseful(User user) throws HttpProcessException {
        String baseCookie = TODAY_APP_UTILS.getBaseCookie(user.getNum(), user.getPwd());
        if(baseCookie==null||"".equals(baseCookie)||!baseCookie.contains("CASTGC")){
            return false;
        }else{
            return true;
        }
    }



    /**
     * 单例模式设计(饿汉模式)
     */
    private static final TodayAppUtils TODAY_APP_UTILS =new TodayAppUtils();


    /**
     * 用于发送http包的client
     */
    private HttpClient client;

    /**
     * 待发送的数据（相当于提交今日校园的数据）
     */
    private JSONObject todayData;

    /**
     * 登录成功后的cookie
     */
    private String usefulCookie;


    /**
     * 今天是否已填写完毕，或者是否到点能写了
     */
    private Boolean isSubmit=false;

    /**
     * 保存个人信息
     */
    private User user;

    /**
     * 邮件发送的提示内容
     */
    private String emailTip="已完成今日校园签到！";



    /**
     * @author p1n93r
     * 数据初始化
     * @throws HttpProcessException http异常
     * @throws IOException IO异常
     */
    private void init(User user) throws Exception {
        addUsefulCookie(user.getNum(),user.getPwd());
        addTodayData(user.getLocation(),user.getIsAtTianJin(),user.getLastCollectorWid(),user.getTeacher());
    }


    /**
     * @author p1n93r
     * 设置今日校园需要提交的数据
     * @throws IOException IO异常
     * @param location 位置，格式如：xx省/xx市/xx县，你平常写的啥，这里就写啥，要一模一样！否则后果自负
     * @param isAtTJ 是否在天津
     */
    private void addTodayData(String location,Boolean isAtTJ,String lastWid,String teacher) throws Exception {

        //首先要找到本次调查，此处枚举collectWid，判断辅导员以及是否提交过的状态来找到调查问卷
        int i=0;
        //如果枚举了这么多次还找不到，就肯定不存在，不浪费时间了，肯定是你信息填错了
        while (i++<9999){
            HttpResult result=postByJson("https://tjut.cpdaily.com/wec-counselor-collector-apps/stu/collector/detailCollector","{\"collectorWid\":\""+lastWid+"\"}",usefulCookie);
            JSONObject resJson = JSONObject.fromObject(result.getResult());
            if(!"SUCCESS".equals(resJson.get("message"))){
                int temp = Integer.parseInt(lastWid);
                lastWid=String.valueOf(temp+1);
                continue;
            }
            JSONObject datas = (JSONObject) resJson.get("datas");
            JSONObject collector = (JSONObject) datas.get("collector");
            JSONObject form = (JSONObject) datas.get("form");
            //获取关键数据
            String senderUserName=(String) collector.get("senderUserName");
            Integer isConfirmed=(Integer) collector.get("isConfirmed");
            String formTitle=(String)form.get("formTitle");
            //如果找到了(2020届非毕业生)
            if(senderUserName.contains(teacher)&&isConfirmed==0&&!formTitle.contains("2020届毕业生")){
                //获取collectWid、formWid、schoolTaskWid
                String collectWid=(String) collector.get("wid");
                String formWid=(String) collector.get("formWid");
                String schoolTaskWid=(String) collector.get("schoolTaskWid");

                //更新lastWid（为了减少请求）
                user.setLastCollectorWid(collectWid);

                //构造发送的数据
                todayData= new JSONObject();
                //填充数据
                todayData.put("collectWid",collectWid);
                todayData.put("formWid",formWid);
                todayData.put("schoolTaskWid",schoolTaskWid);

                //在继续构造发送的数据之前，先检查一下今日校园是否改了题目，或者是否你今天已经提交过了
                HttpResult fromCheck=postByJson("https://tjut.cpdaily.com/wec-counselor-collector-apps/stu/collector/getFormFields",
                        "{\"pageSize\":10,\"pageNumber\":1,\"formWid\":\""+formWid+"\",\"collectorWid\":\""+collectWid+"\"}",
                        usefulCookie);
                JSONObject fromCheckJson=(JSONObject) JSONObject.fromObject(fromCheck.getResult()).get("datas");

                //这里检测题目的数量是不是对的，下面还会对内容进行核对
                if ((Integer) fromCheckJson.get("totalSize")!=7){
                    //发送管理员邮件提醒
                    EmailUtils.sendEmail(user.getEmail(),"再见吧！艾特君~","今日校园修改题目的数量啦~~赶紧追加~~");
                    emailTip="今日校园修改题目啦，请通知管理员及时更新程序~";
                    isSubmit=true;
                    return;
                }
                //通过基本的验证后，可以构造将发送的数据
                //构造填写的数据
                JSONArray rows = (JSONArray) fromCheckJson.get("rows");
                //遍历解析数据,得到需要提交的数据
                JSONArray resRow = new JSONArray();
                for(int it=0;it<rows.size();it++){
                   JSONObject current=(JSONObject)rows.get(it);
                   //设置需要提交的数据
                    String title=(String) current.get("title");
                    //设置选项
                    JSONArray items = (JSONArray) current.get("fieldItems");
                    JSONArray select = new JSONArray();
                    if("你的健康状况（可多选）".equals(title)){
                        select= setSelectItem(items, "健康");
                    }else if ("今天你的体温是多少".equals(title)){
                        select= setSelectItem(items, "正常");
                    }else if ("你目前所处状态".equals(title)){
                        select= setSelectItem(items, "普通居家");
                    }else if ("你目前所处地区".equals(title)){
                        current.put("value",location);
                    }else if ("尚在外地同学近2日是否因特殊原因需返回天津市；已在天津同学选择“已在津”。".equals(title)){
                        select= setSelectItem(items, isAtTJ? "已在津":"否");
                    }else if ("近两周，和你共同居住的亲属（人员）有无新冠肺炎医学留观或疑似病例或确诊病例？ (必填)".equals(title)){
                        select= setSelectItem(items,"无");
                    }else if ("本人是否承诺以上所填报的全部内容均属实、准确，不存在任何隐瞒与不实的情况，更无遗漏之处。".equals(title)){
                        select= setSelectItem(items,"是");
                    }else{
                        //进入到这里就是：题目数量没改，但是题目内容改了，需要发送邮件提醒我，停止自动打卡
                        EmailUtils.sendEmail(user.getEmail(),"再见吧！艾特君~","今日校园修改题目数量没改，但是修改了题目的内容~~请更新程序~~");
                        emailTip="今日校园修改题目啦，请通知管理员及时更新程序~";
                        isSubmit=true;
                        return;
                    }
                    //填入select
                    current.put("fieldItems",select);
                    resRow.add(current);
                }
                todayData.put("form",resRow);
                break;
            }
            int temp = Integer.parseInt(lastWid);
            lastWid=String.valueOf(temp+1);
        }
        if(i==9999){
            EmailUtils.sendEmail(user.getEmail(),"再见吧！艾特君~","辅导员是否填写正确？请核对后重新开始~");
            throw new Exception("辅导员信息填写错误！");
        }
    }


    /**
     * @author p1n93r
     * 捕获登录后的cookie
     * @throws HttpProcessException http发发送可能会失败
     */
    private void addUsefulCookie(String num,String pwd) throws HttpProcessException {
        String url = "http://authserver.tjut.edu.cn/authserver/login?service=https%3A%2F%2Ftjut.cpdaily.com%2Fwec-counselor-collector-apps%2Fstu%2Fmobile%2Findex.html%3FcollectorWid%3D3545";
        //待发送的数据
        HashMap<String, Object> params = new HashMap<>(7);


        //请求连接，并获得请求结果
        HttpResult httpResult = postByForm(url,null,null);
        //准备提取cookie、hidden令牌
        Header[] respHeaders = httpResult.getRespHeaders();

        //==================提取登录信息门户的令牌--staart===================

        //捕获cookie：登录信息门户需要的cookie-------start-----------
        String cookie="";
        for (Header v:respHeaders){
            //如果找到cookie了
            if("Set-Cookie".equals(v.getName())){
                //登录请求需要设置两个cookie
                if(v.getValue().startsWith("route")){
                    cookie+=v.getValue()+";";
                }else{
                    cookie+=v.getValue().split(";")[0]+";";
                }
            }
        }
        //捕获cookie：登录信息门户需要的cookie-------end-----------

        //捕获表单令牌：登录信息门户需要的令牌-------start-----------
        String respContent = httpResult.getResult();
        //定义hidden令牌特征
        String ltTag="lt";
        String dlltTage="dllt";
        String executionTage="execution";
        String _eventIdTag="_eventId";
        String rmShownTag="rmShown";
        //找到各个特征对应的目标值
        String lt = getHiddenVal(respContent, ltTag);
        String dllt=getHiddenVal(respContent,dlltTage);
        String execution=getHiddenVal(respContent,executionTage);
        String _eventId=getHiddenVal(respContent,_eventIdTag);
        String rmShown=getHiddenVal(respContent,rmShownTag);
        //捕获表单令牌：登录信息门户需要的令牌-------end-----------
        params.put("lt",lt);
        params.put("dllt",dllt);
        params.put("execution",execution);
        params.put("_eventId",_eventId);
        params.put("rmShown",rmShown);

        //=================提取登录信息门户的令牌--end==========================


        //构造发送的数据
        params.put("username",num);
        params.put("password",pwd);


        //请求连接，并获得请求结果
        HttpResult loginRes = postByForm(url, params, cookie);
        Header[] loginRespHeaders = loginRes.getRespHeaders();
        //捕获登录信息门户后得到的cookie
        String loginCookie="";
        for (Header v:loginRespHeaders){
            //如果找到cookie了
            if("set-cookie".equals(v.getName().toLowerCase())){
                String currentVal=v.getValue();
                if(currentVal.startsWith("CASTGC")||currentVal.startsWith("iPlanetDirectoryPro")){
                    loginCookie+=v.getValue().split(";")[0]+";";
                }
            }
        }

        HttpResult todayRes = postByForm(loginRes.getHeaders("Location").getValue(), null, null);
        Header[] todayRespHeaders = todayRes.getRespHeaders();
        for (Header v:todayRespHeaders){
            //开始设置登录今日校园的后cookie
            if("set-cookie".equals(v.getName().toLowerCase())){
                String currentVal=v.getValue();
                if(currentVal.startsWith("acw_tc")||currentVal.startsWith("MOD_AUTH_CAS")){
                    loginCookie+=v.getValue().split(";")[0]+";";
                }
            }
        }

        //此处得到的cookie是一个可登录信息门户以及今日校园的cookie
        loginCookie+=cookie;
        usefulCookie=loginCookie;
    }


    /**
     * @return 更新lastWid后的user数据，需要及时更新数据库的lastWid
     */
    public User getUser(){
        return this.user;
    }


    //======================================================本工具类的工具方法----start==========================================================================


    /**
     * 发送json数据，可带cookie发送
     * @param url 目标地址
     * @param json json数据
     * @param cookie 携带的cookie
     * @return HttpResult对象
     * @throws HttpProcessException http异常
     */
    private HttpResult postByJson(String url,String json,String cookie) throws HttpProcessException {

        HCB hcb = HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1_2).ssl().retry(5);
        //防止保存cookie，构造新的client
        client = hcb.build();

        //设置Header
        Header[] doHeader=HttpHeader.custom().
                connection("keep-alive").
                contentType("application/json;charset=utf-8").
                cookie(cookie)
                .build();
        //插件式配置请求参数（网址、请求参数、编码、client）
        HttpConfig initReqConfig = HttpConfig.custom()
                .url(url)
                .encoding("utf-8")
                .client(client)
                .method(HttpMethods.POST)
                .headers(doHeader)
                .json(json)
                ;
        //请求连接，并获得请求结果
        return HttpClientUtil.sendAndGetResp(initReqConfig);
    }

    /**
     * 发送表单形式的数据
     * @param url 目标地址
     * @param data 数据，Map格式
     * @param cookie 携带的cookie
     * @return HttpResult对象
     * @throws HttpProcessException http异常
     */
    private HttpResult postByForm(String url,HashMap<String,Object> data,String cookie) throws HttpProcessException {
        HCB hcb = HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1_2).ssl().retry(5);
        //防止保存cookie，构造新的client
        client = hcb.build();

        //设置cookie
        Header[] headers= HttpHeader.custom().
                connection("keep-alive").
                contentType(HttpHeader.Headers.APP_FORM_URLENCODED)
                .cookie(cookie)
                .build();
        //准备带数据请求
        HttpConfig config = HttpConfig.custom()
                .url(url)
                .encoding("utf-8")
                .client(client)
                .map(data)
                .method(HttpMethods.POST)
                .headers(headers)
                ;
        //请求连接，并获得请求结果
        return HttpClientUtil.sendAndGetResp(config);
    }


    /**
     * @author p1n93r
     * 抓取隐藏的input的value
     * @param content html
     * @param tag input的name
     * @return 找到的input的value
     */
    private String getHiddenVal(String content,String tag){
        return regex("\""+tag+"\" value=\"([^\"]*)\"", content)[0];
    }


    /**
     * 模拟选择选项
     * @param src 选项
     * @param tag 选择项标志
     * @return 最终选择的结果
     */
    private JSONArray setSelectItem(JSONArray src,String tag){
        JSONArray res = new JSONArray();
        for (Object o : src) {
            JSONObject currentItem = (JSONObject) o;
            if (((String)currentItem.get("content")).contains(tag)) {
                //设置已选择
                currentItem.put("isSelected", 1);
                res.add(currentItem);
            }
        }
        return res;
    }


    /**
     * 获取登陆信息门户后的cookie
     * @param num 学号
     * @param pwd 密码
     * @return cookie字串
     * @throws HttpProcessException http异常
     */
    private String getBaseCookie(String num,String pwd) throws HttpProcessException {
        String url = "http://authserver.tjut.edu.cn/authserver/login?service=https%3A%2F%2Ftjut.cpdaily.com%2Fwec-counselor-collector-apps%2Fstu%2Fmobile%2Findex.html%3FcollectorWid%3D3545";
        //待发送的数据
        HashMap<String, Object> params = new HashMap<>(7);


        //请求连接，并获得请求结果
        HttpResult httpResult = postByForm(url,null,null);
        //准备提取cookie、hidden令牌
        Header[] respHeaders = httpResult.getRespHeaders();

        //==================提取登录信息门户的令牌--staart===================

        //捕获cookie：登录信息门户需要的cookie-------start-----------
        String cookie="";
        for (Header v:respHeaders){
            //如果找到cookie了
            if("Set-Cookie".equals(v.getName())){
                //登录请求需要设置两个cookie
                if(v.getValue().startsWith("route")){
                    cookie+=v.getValue()+";";
                }else{
                    cookie+=v.getValue().split(";")[0]+";";
                }
            }
        }
        //捕获cookie：登录信息门户需要的cookie-------end-----------

        //捕获表单令牌：登录信息门户需要的令牌-------start-----------
        String respContent = httpResult.getResult();
        //定义hidden令牌特征
        String ltTag="lt";
        String dlltTage="dllt";
        String executionTage="execution";
        String _eventIdTag="_eventId";
        String rmShownTag="rmShown";
        //找到各个特征对应的目标值
        String lt = getHiddenVal(respContent, ltTag);
        String dllt=getHiddenVal(respContent,dlltTage);
        String execution=getHiddenVal(respContent,executionTage);
        String _eventId=getHiddenVal(respContent,_eventIdTag);
        String rmShown=getHiddenVal(respContent,rmShownTag);
        //捕获表单令牌：登录信息门户需要的令牌-------end-----------
        params.put("lt",lt);
        params.put("dllt",dllt);
        params.put("execution",execution);
        params.put("_eventId",_eventId);
        params.put("rmShown",rmShown);

        //=================提取登录信息门户的令牌--end==========================


        //构造发送的数据
        params.put("username",num);
        params.put("password",pwd);


        //请求连接，并获得请求结果
        HttpResult loginRes = postByForm(url, params, cookie);
        Header[] loginRespHeaders = loginRes.getRespHeaders();
        //捕获登录信息门户后得到的cookie
        String loginCookie="";
        for (Header v:loginRespHeaders){
            //如果找到cookie了
            if("set-cookie".equals(v.getName().toLowerCase())){
                String currentVal=v.getValue();
                if(currentVal.startsWith("CASTGC")||currentVal.startsWith("iPlanetDirectoryPro")){
                    loginCookie+=v.getValue().split(";")[0]+";";
                }
            }
        }
        loginCookie+=cookie;
        return loginCookie;
    }


    //======================================================本工具类的工具方法----end==========================================================================



    /**
     * 做最后的提交
     * @author p1n93r
     * @throws HttpProcessException http异常
     * @return 返回true代表签到成功，false则代表签到失败
     */
    public Boolean submit() throws HttpProcessException {
        //可以提交
        if(!isSubmit){
            HttpResult result=postByJson("https://tjut.cpdaily.com/wec-counselor-collector-apps/stu/collector/submitForm",todayData.toString(),usefulCookie);
            JSONObject resJson = JSONObject.fromObject(result.getResult());
            //如果提交失败
            if(!"SUCCESS".equals(resJson.get("message"))){
                emailTip="错误：自动签到失败！可能是网络阻塞~~"+resJson.get("message");
                isSubmit=true;
            }
        }
        EmailUtils.sendEmail(user.getEmail(),"再见吧！艾特君~",emailTip);
        return !isSubmit;
    }


    public static void main(String[] a) throws Exception {

        User user = new User();
        user.setNum("xx");
        user.setPwd("xxx");
        user.setIsAtTianJin(false);
        user.setLastCollectorWid("xxx");
        user.setLocation("xx省/xx市/xx县");
        user.setTeacher("周龙");
        user.setEmail("1725367974@qq.com");


        TodayAppUtils instance = TodayAppUtils.getInstance(user);
        JSONObject todayData = instance.todayData;
        System.out.println(todayData);
        instance.submit();

    }

}
