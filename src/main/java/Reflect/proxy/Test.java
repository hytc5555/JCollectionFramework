package Reflect.proxy;

import Annotation.MyAnnotationDemo;
import Annotation.SuperAnnotatonDemo;
import Reflect.proxy.handler.ProxyHandler;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles",
                "true");
        IProxyDemo proxyDemo = (IProxyDemo) Proxy.newProxyInstance(ProxyDemoImpl.class.getClassLoader(),ProxyDemoImpl.class.getInterfaces(),new ProxyHandler(new ProxyDemoImpl()));
            proxyDemo.test();



        System.out.println(new MyAnnotationDemo() instanceof SuperAnnotatonDemo);

    }
}
