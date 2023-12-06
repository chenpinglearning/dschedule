package org.enterprise.infrastructure.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring bean util
 * <p>
 *
 * @author Percy.Chuang
 * @date 2019-07-25 13:48
 **/
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * get application context
     *
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     * get Object from AplicationConext with bean name;
     *
     * @param name bean name
     * @return Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * get Object from applicationContext with clazz
     *
     * @param clazz bean class
     * @param <T>   bean obect
     * @return class's object
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * get Object from applicationContext with bean name and convert to param's classes's object
     *
     * @param name  bean name
     * @param clazz bean clazz
     * @param <T>   Object
     * @return object
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
