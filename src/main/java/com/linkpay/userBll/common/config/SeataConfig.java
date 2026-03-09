package com.linkpay.userBll.common.config;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeataConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
//        String txServiceGroup = this.seataProperties.getTxServiceGroup();
//        if (StringUtils.isEmpty(txServiceGroup)) {
//            txServiceGroup = applicationName + "-fescar-service-group";
//            this.seataProperties.setTxServiceGroup(txServiceGroup);
//        }
//
//        return new GlobalTransactionScanner(applicationName, txServiceGroup);
        return new GlobalTransactionScanner(applicationName, "my_test_tx_group");
    }
}
