package com.kochubei.example;

import com.kochubei.service.product.PriceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("start program");
        ApplicationContext context = new ClassPathXmlApplicationContext("services.xml");

        PriceService priceService = (PriceService) context.getBean("productService");
        //todo System.out.println(priceService.mergePrices("123"));
    }

}
