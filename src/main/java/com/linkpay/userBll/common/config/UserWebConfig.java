package com.linkpay.userBll.common.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.linkpay.userBase.api.AuthApi;
import com.linkpay.userBase.api.UserApi;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class UserWebConfig {

  @Autowired
  public WebClient.Builder webClientBuilder;

  @Resource
  private NacosDiscoveryClient nacosDiscoveryClient;

  @SneakyThrows
  @Bean
  UserApi userApi() {
    var instance = nacosDiscoveryClient.getInstances("linkpay-user-base");

    var count = 0;
    while (instance.size() == 0 && count < 120) {
      log.info("wait for service user-base ready...");
      Thread.sleep(1000);
      instance = nacosDiscoveryClient.getInstances("linkpay-user-base");
    }

    if (instance.size() == 0) {
      log.error("fail to start service due to user-base not ready");
      return null;
    }

    HttpServiceProxyFactory httpServiceProxyFactory =
        HttpServiceProxyFactory.builder(
                WebClientAdapter.forClient(webClientBuilder.baseUrl(
                    "http://" + instance.get(0).getHost() + ":" + instance.get(0).getPort()).build()))
            .build();
    //        HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClientBuilder.baseUrl("http://127.0.0.1:8086").build())).build();
    return httpServiceProxyFactory.createClient(UserApi.class);
  }

  @SneakyThrows
  @Bean
  AuthApi authApi() {
    var instance = nacosDiscoveryClient.getInstances("linkpay-user-base");

    var count = 0;
    while (instance.size() == 0 && count < 120) {
      log.info("wait for service user-base ready...");
      Thread.sleep(1000);
      instance = nacosDiscoveryClient.getInstances("linkpay-user-base");
    }

    if (instance.size() == 0) {
      log.error("fail to start service due to user-base not ready");
      return null;
    }

    HttpServiceProxyFactory httpServiceProxyFactory =
        HttpServiceProxyFactory.builder(
                WebClientAdapter.forClient(webClientBuilder.baseUrl(
                    "http://" + instance.get(0).getHost() + ":" + instance.get(0).getPort()).build()))
            .build();
    //        HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClientBuilder.baseUrl("http://127.0.0.1:8086").build())).build();
    return httpServiceProxyFactory.createClient(AuthApi.class);
  }
}
