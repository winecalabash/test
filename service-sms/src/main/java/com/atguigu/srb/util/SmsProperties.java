package com.atguigu.srb.util;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
//注意prefix要写到最后一个 "." 符号之前
//调用setter为成员赋值
@ConfigurationProperties(prefix = "rckj")
public class SmsProperties implements InitializingBean {

    private String accountSid;
    private String accountToken;
    private String appId;
    private String serverIp;
    private String serverPort;

    public static String ACCOUNT_SID;
    public static String ACCOUNT_TOKEN;
    public static String APP_ID;
    public static String SERVER_IP;
    public static String SERVER_PORT;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCOUNT_SID = accountSid;
        ACCOUNT_TOKEN = accountToken;
        APP_ID = appId;
        SERVER_IP = serverIp;
        SERVER_PORT = serverPort;
    }
}
