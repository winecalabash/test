package com.atguigu.srb.controller.api;

/**
 * @ClassName ApiSmsController
 * @Description TODO
 * @Author hp
 * @DateTime 2021/9/13 10:24
 **/

import com.atguigu.srb.client.CoreUserInfoClient;
import com.atguigu.srb.exception.Assert;
import com.atguigu.srb.result.R;
import com.atguigu.srb.result.ResponseEnum;
import com.atguigu.srb.service.SmsService;
import com.atguigu.common.util.RandomUtils;
import com.atguigu.common.util.RegexValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hp
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin //跨域
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private  RedisTemplate redisTemplate;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;


    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号码",required = true)
            @PathVariable("mobile") String mobile){

        //MOBILE_NULL_ERROR(-202, "手机号不能为空"),
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //MOBILE_ERROR(-203, "手机号不正确"),
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

//        //判断手机号是否已经注册
//        boolean checkMobile = coreUserInfoClient.checkMobile(mobile);
//        log.info("checkMobile",checkMobile);
//        Assert.isTrue(checkMobile == false,ResponseEnum.MOBILE_EXIST_ERROR);

        //生成验证码
        String code = RandomUtils.getFourBitRandom();
        //组装短信模板参数
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        //发送短信
        smsService.send(mobile,param);

        //将验证码存入redis
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, code, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}