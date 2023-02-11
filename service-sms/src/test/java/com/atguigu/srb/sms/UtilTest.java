package com.atguigu.srb.sms;

import com.atguigu.srb.util.SmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilTest {

    @Test
    public void testProperties(){
        System.out.println(SmsProperties.ACCOUNT_SID);
        System.out.println(SmsProperties.APP_ID);
        System.out.println(SmsProperties.ACCOUNT_TOKEN);
        System.out.println(SmsProperties.SERVER_IP);
        System.out.println(SmsProperties.SERVER_PORT);
    }
}
