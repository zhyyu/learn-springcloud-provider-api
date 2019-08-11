package com.zhyyu.learn.learn.springcloud.provider.api.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

/**
 * @author juror
 * @datatime 2019/8/11 17:32
 */
public class FormFeignConfig {

    @Bean
    Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

    /**
     * 测试使用拦截器添加请求表单参数
     * <pre>
     *     1. 使用 body
     *          - 手动修改body, 如添加 "&sign=mysign2" 尾缀
     *          - 该参数方法优先级高于 query, 如query sign=mysign 则被body 覆盖
     *     2. 使用 template.query
     *          - 添加为url? 参数, 如 POST /helloFromFeignApi?sign=mysign HTTP/1.1
     * </pre>
     */
    @Bean
    RequestInterceptor testRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                /**
                 * POST /helloFromFeignApi HTTP/1.1
                 * Content-Length: 23
                 * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
                 *
                 * key1=value1&key2=value2
                 */
                System.out.println(template.request());

                Request.Body body = template.requestBody();
                /**
                 * key1=value1&key2=value2
                 */
                System.out.println(body.asString());
                String newBodyStr = body.asString() + "&sign=mysign2";
                template.body(newBodyStr);
                System.out.println(template.request());

                // 为增加到url 后, POST /provider/helloFromFeignApi?sign=mysign HTTP/1.1
                /**
                 * POST /helloFromFeignApi?sign=mysign HTTP/1.1
                 * Content-Length: 36
                 * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
                 *
                 * key1=value1&key2=value2&sign=mysign2
                 *
                 * body 和 url 中参数可以合并解析到provider dto1 中(当body key1=value1&key2=value2)
                 */
                String sign = "mysign";
                template.query("sign", sign);
                System.out.println(template.request());
            }
        };
    }

}
