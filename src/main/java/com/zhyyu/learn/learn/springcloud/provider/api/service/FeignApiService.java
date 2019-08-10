package com.zhyyu.learn.learn.springcloud.provider.api.service;

import com.zhyyu.learn.learn.springcloud.provider.api.dto.MyDTO1;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author juror
 * @datatime 2019/7/23 0:08
 */
@FeignClient(name = "cloud-provider1", path = "/provider", configuration = FeignApiService.Configuration.class)
public interface FeignApiService {

    @RequestMapping(value = "helloFromFeignApi", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String helloFromFeignApi(MyDTO1 myDTO1);

    class Configuration {
        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
        }
    }


}
