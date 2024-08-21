package org.haiyang.springunderlying;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

public class BeanFactoryUnderlying {
    @Test
    public void test() {
        //bean定义
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //构建者模式
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(Config.class)
                .setScope("singleton")
                .getBeanDefinition();

        //注册bean
        beanFactory.registerBeanDefinition("config", beanDefinition);
        //给BeanFactory添加一些常用的后处理器(原始的BeanFactory并没有解析注解的能力,后处理器可以扩展解析注解功能)
        //这一步仅仅是将后处理器加入BeanFactory,但是并没有执行
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //执行BeanFactory后处理器
        beanFactory
                .getBeansOfType(BeanFactoryPostProcessor.class)
                .values()
                .stream()
                .forEach(
                        beanFactoryPostProcessor ->
                                beanFactoryPostProcessor
                                        .postProcessBeanFactory(beanFactory));
        Arrays.stream(beanFactory
                .getBeanDefinitionNames())
                .toList()
                .forEach(e -> System.out.println(e));
    }


}

@Configuration
 class Config {
    @Bean
    public Bean1 bean1() {
        return new Bean1();
    }
}

class Bean1 {
    public Bean1() {
        System.out.println("Bean1");
    }
}
