package com.zhyyu.learn.learn.springcloud.provider.api.service;

import com.zhyyu.learn.learn.springcloud.provider.api.config.FormFeignConfig;
import com.zhyyu.learn.learn.springcloud.provider.api.dto.MyDTO1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "cloud-provider2", path = "/provider2", configuration = FormFeignConfig.class)
public interface FeignApi2Service {

    @RequestMapping(value = "helloFromFeignApi2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String helloFromFeignApi2(MyDTO1 myDTO1);

}
