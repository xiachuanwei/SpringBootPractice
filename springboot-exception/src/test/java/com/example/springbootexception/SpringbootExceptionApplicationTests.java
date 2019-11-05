package com.example.springbootexception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
class SpringbootExceptionApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void Test() throws Exception {

        System.out.println("请求URL : /getUser");
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/getUser")).andReturn();
        System.out.println(result1.getResponse().getContentAsString(StandardCharsets.UTF_8));

        System.out.println("请求URL : /getUserArgs");
        MockHttpServletRequestBuilder mock = MockMvcRequestBuilders.get("/getUserArgs").param("userName", "zhangsan");
        MvcResult result2 = mockMvc.perform(mock).andReturn();
        System.out.println(result2.getResponse().getContentAsString(StandardCharsets.UTF_8));

        System.out.println("请求URL : /test");
        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.get("/test")).andReturn();
        System.out.println(result3.getResponse().getContentAsString(StandardCharsets.UTF_8));

        System.out.println("请求URL : /testABC");
        MvcResult result4 = mockMvc.perform(MockMvcRequestBuilders.get("/testABC")).andReturn();
        System.out.println(result4.getResponse().getContentAsString(StandardCharsets.UTF_8));

    }
}
