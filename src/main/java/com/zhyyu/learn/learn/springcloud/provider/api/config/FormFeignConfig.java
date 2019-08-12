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
 * 测试 FormFeignConfig 有无 @Configuration
 * <pre>
 *     未添加 @Configuration
 *     -----------------------------
 *     1. @FeignClient(name = "cloud-provider1", path = "/provider", configuration = FormFeignConfig.class) 可生效配置, 但不是对其他feign 生效
 *
 *     添加 @Configuration
 *     1.若 feign 未添加自定义配置, 则所有feign 均使用 @Configuration 对应配置
 *     2.若 feign 使用自定义配置, 如@FeignClient(name = "cloud-provider1", path = "/provider", configuration = FeignApiService.Configuration.class), 则覆盖@Configuration 默认配置, 其他无自定义配置feign 继续使用默认配置
 *
 * </pre>
 * @author juror
 * @datatime 2019/8/11 17:32
 */
//@Configuration
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
     *     2. 使用 template.query
     *          - 添加为url? 参数, 如 POST /helloFromFeignApi?sign=mysign HTTP/1.1
     *
     *     3. 若body url 均增加sign 字段, 则结果为两者合并, 如"mysign,mysign2"
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


                String sign = "mysign";
                template.query("sign", sign);
                System.out.println(template.request());

                /**
                 * POST /helloFromFeignApi?sign=mysign HTTP/1.1
                 * Content-Length: 36
                 * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
                 *
                 * key1=value1&key2=value2&sign=mysign2
                 */
            }
        };
    }

}
