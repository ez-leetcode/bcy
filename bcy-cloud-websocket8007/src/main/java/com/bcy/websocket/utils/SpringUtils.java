package com.bcy.websocket.utils;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

//由于websocket是多对象，spring是单例模式，直接注入会失败，用这个工具类主动获取实例
@Component
public class SpringUtils implements BeanFactoryPostProcessor {


    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        if (getBeanFactory() == null) {
            System.out.println("无法获取到bean对象");
            return null;
        } else {
            T result = (T) getBeanFactory().getBean(name);
            return result;
        }
    }

    public static <T> T getBean(Class<T> name) throws BeansException {
        if (getBeanFactory() == null) {
            System.out.println("无法获取到bean对象");
            return null;
        } else {
            T result = (T) getBeanFactory().getBean(name);
            return result;
        }
    }

    //匹配判断
    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    //异常
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isSingleton(name);
    }

    //注册对象类型
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    //处理别名
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getAliases(name);
    }

}
