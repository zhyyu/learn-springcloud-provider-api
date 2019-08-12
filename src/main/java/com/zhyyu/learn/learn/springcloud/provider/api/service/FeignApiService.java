package com.zhyyu.learn.learn.springcloud.provider.api.service;

import com.zhyyu.learn.learn.springcloud.provider.api.config.FormFeignConfig;
import com.zhyyu.learn.learn.springcloud.provider.api.dto.MyDTO1;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
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
 * 测试feign api
 * <pre>
 *      1. 若已有同名@FeignClient (com.zhyyu.learn.springcloud.consumer.service.FeignService), 且注入SpringFormEncoder 配置, 则无下异常:
 *          - feign.codec.EncodeException: Could not write request: no suitable HttpMessageConverter found for request type [com.zhyyu.learn.learn.springcloud.provider.api.dto.MyDTO1] and content type [application/x-www-form-urlencoded]
 *      2. 若已有同名 feign, 如(com.zhyyu.learn.springcloud.consumer.service.FeignService),则注入 FeignApiService.Configuration.class 配置无效, 如(testRequestInterceptor 无效)
 *      3. 若无同名feign, 则需定义 feignFormEncoder, 否则如下异常:
 *          - feign.codec.EncodeException: Could not write request: no suitable HttpMessageConverter found for request type [com.zhyyu.learn.learn.springcloud.provider.api.dto.MyDTO1] and content type [application/x-www-form-urlencoded]
 *      4. 若无同名feign, 可定义如下 testRequestInterceptor 拦截器
 * </pre>
 * @author juror
 * @datatime 2019/7/23 0:08
 */
@FeignClient(name = "cloud-provider1", path = "/provider", configuration = FormFeignConfig.class)
//@FeignClient(name = "cloud-provider1", path = "/provider", configuration = FeignApiService.Configuration.class)
//@FeignClient(name = "cloud-provider1", path = "/provider")
public interface FeignApiService {

    @RequestMapping(value = "helloFromFeignApi", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String helloFromFeignApi(MyDTO1 myDTO1);


    /* =========================== 抽取至 com.zhyyu.learn.learn.springcloud.provider.api.config.FormFeignConfig =========================== */
    class Configuration {
        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
        }

        @Bean
        RequestInterceptor testRequestInterceptor() {
            return new RequestInterceptor() {
                @Override
                public void apply(RequestTemplate template) {

                    System.out.println(template.request());

                    Request.Body body = template.requestBody();

                    System.out.println(body.asString());
                    String newBodyStr = body.asString() + "&sign=mysign2";
                    template.body(newBodyStr);
                    System.out.println(template.request());

                    String sign = "mysignxxx";
                    template.query("sign", sign);
                    System.out.println(template.request());
                }
            };
        }

    }



}
