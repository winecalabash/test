package com.atguigu.srb.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.common.exception.Assert;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.service.SmsService;
import com.atguigu.srb.util.SmsProperties;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hp
 * 短信接口
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

//    @Value(value = "${rckj.accountSid}")
//    private String ACCOUNTS_ID;
//    @Value(value = "${rckj.accountToken}")
//    private String ACCOUNTTOKEN;
//    @Value(value = "${rckj.appId}")
//    private String APPID;
//    @Value(value = "${rckj.serverIp}")
//    private String serverIp;
//    @Value(value = "${rckj.serverPort}")
//    private String serverPort;

    @Override
    public void send(String mobile,Map<String,Object> params) {

        CCPRestSmsSDK ccpRestSmsSDK=new CCPRestSmsSDK();
        ccpRestSmsSDK.setAccount(SmsProperties.ACCOUNT_SID,SmsProperties.ACCOUNT_TOKEN);
        ccpRestSmsSDK.setAppId(SmsProperties.APP_ID);
        ccpRestSmsSDK.setBodyType(BodyType.Type_JSON);
        ccpRestSmsSDK.init(SmsProperties.SERVER_IP,SmsProperties.SERVER_PORT);
        String[] datas={(String) params.get("code"),"2"};
        HashMap<String, Object> result = ccpRestSmsSDK.sendTemplateSMS(mobile, "1", datas);
        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }

//    @Override
//    public void send(String mobile, String templateCode, Map<String,Object> param) {
//
//        //创建远程连接客户端对象
//        DefaultProfile profile = DefaultProfile.getProfile(
//                SmsProperties.REGION_Id,
//                SmsProperties.KEY_ID,
//                SmsProperties.KEY_SECRET);
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        //创建远程连接的请求参数
//        CommonRequest request = new CommonRequest();
//        request.setSysMethod(MethodType.POST);
//        request.setSysDomain("dysmsapi.aliyuncs.com");
//        request.setSysVersion("2017-05-25");
//        request.setSysAction("SendSms");
//        request.putQueryParameter("RegionId", SmsProperties.REGION_Id);
//        request.putQueryParameter("PhoneNumbers", mobile);
//        request.putQueryParameter("SignName", SmsProperties.SIGN_NAME);
//        request.putQueryParameter("TemplateCode", templateCode);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(param);
//        request.putQueryParameter("TemplateParam", json);
//
//        try {
//            //使用客户端对象携带请求对象发送请求并得到响应结果
//            CommonResponse response = client.getCommonResponse(request);
//            boolean success = response.getHttpResponse().isSuccess();
//            //ALIYUN_RESPONSE_FAIL(-501, "阿里云响应失败"),
//            Assert.isTrue(success, ResponseEnum.ALIYUN_RESPONSE_FAIL);
//
//            String data = response.getData();
//            HashMap<String, String> resultMap = gson.fromJson(data, HashMap.class);
//            String code = resultMap.get("Code");
//            String message = resultMap.get("Message");
//            log.info("阿里云短信发送响应结果：");
//            log.info("code：" + code);
//            log.info("message：" + message);
//
//            //ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "短信发送过于频繁"),//业务限流
//            Assert.notEquals("isv.BUSINESS_LIMIT_CONTROL", code, ResponseEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);
//            //ALIYUN_SMS_ERROR(-503, "短信发送失败"),//其他失败
//            Assert.equals("OK", code, ResponseEnum.ALIYUN_SMS_ERROR);
//
//        } catch (ServerException e) {
//            log.error("阿里云短信发送SDK调用失败：");
//            log.error("ErrorCode=" + e.getErrCode());
//            log.error("ErrorMessage=" + e.getErrMsg());
//            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
//        } catch (ClientException e) {
//            log.error("阿里云短信发送SDK调用失败：");
//            log.error("ErrorCode=" + e.getErrCode());
//            log.error("ErrorMessage=" + e.getErrMsg());
//            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
//        }
//    }
}