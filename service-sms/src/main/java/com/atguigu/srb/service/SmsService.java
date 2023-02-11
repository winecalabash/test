package com.atguigu.srb.service;

import java.util.Map;

/**
 * @author hp
 */
public interface SmsService {
    void send(String mobile , Map<String,Object> param);
}
