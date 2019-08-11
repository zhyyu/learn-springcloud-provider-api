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
    /**
     * <pre>
     *     https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html
     *
     *     FooConfiguration does not need to be annotated with @Configuration.
     *     However, if it is, then take care to exclude it from any @ComponentScan that would otherwise include this configuration as it will become the default source for feign.Decoder,
     *     feign.Encoder, feign.Contract, etc., when specified. This can be avoided by putting it in a separate, non-overlapping package from any @ComponentScan or @SpringBootApplication,
     *     or it can be explicitly excluded in @ComponentScan.
     *
     *     如果设置 @Configuration 则对所有feign 生效(线上不可, 可能仅想差异外某一feign), 待验证存在 @Configuration 情况; todo
     *     如无 @Configuration, 配置依然生效, 但是否对其他feign 生效待验证 todo
     * </pre>
     */
//    @Configuration
    /*class Configuration {
        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
        }

        *//**
         * 测试使用拦截器添加请求表单参数
         * <pre>
         *     1. 使用 body
         *          - 手动修改body, 如添加 "&sign=mysign2" 尾缀
         *          - 该参数方法优先级高于 query, 如query sign=mysign 则被body 覆盖
         *     2. 使用 template.query
         *          - 添加为url? 参数, 如 POST /helloFromFeignApi?sign=mysign HTTP/1.1
         * </pre>
         *//*
        @Bean
        RequestInterceptor testRequestInterceptor() {
            return new RequestInterceptor() {
                @Override
                public void apply(RequestTemplate template) {
                    *//**
                     * POST /helloFromFeignApi HTTP/1.1
                     * Content-Length: 23
                     * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
                     *
                     * key1=value1&key2=value2
                     *//*
                    System.out.println(template.request());

                    Request.Body body = template.requestBody();
                    *//**
                     * key1=value1&key2=value2
                     *//*
                    System.out.println(body.asString());
                    String newBodyStr = body.asString() + "&sign=mysign2";
                    template.body(newBodyStr);
                    System.out.println(template.request());

                    // 为增加到url 后, POST /provider/helloFromFeignApi?sign=mysign HTTP/1.1
                    *//**
                     * POST /helloFromFeignApi?sign=mysign HTTP/1.1
                     * Content-Length: 36
                     * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
                     *
                     * key1=value1&key2=value2&sign=mysign2
                     *
                     * body 和 url 中参数可以合并解析到provider dto1 中(当body key1=value1&key2=value2)
                     *//*
                    String sign = "mysign";
                    template.query("sign", sign);
                    System.out.println(template.request());
                }
            };
        }

    }*/



}
