package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication  // 扫描自己工程下面的bean
// 除要扫描自己工程下面的bean外，还需扫描api & model 接口下面的bean
@EntityScan("com.xuecheng.framework.domain.cms") //扫描model下的cms
@ComponentScan(basePackages={"com.xuecheng.api"})   //扫描api接口
@ComponentScan(basePackages={"com.xuecheng.manage_cms"})//扫描本项目下的所有类 (此注解可以不加)
public class ManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class,args);
    }
}
