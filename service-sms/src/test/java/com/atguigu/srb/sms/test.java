package com.atguigu.srb.sms;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class test {

    @Test
    public void testWeb(@Autowired MockMvc mockMvc) throws Exception{
        MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders.get("/admin/core/integralGrade/list");
        mockMvc.perform(mockMvcRequestBuilders);
    }
}
