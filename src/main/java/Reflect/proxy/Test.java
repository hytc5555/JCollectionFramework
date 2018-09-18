package Reflect.proxy;

import Reflect.proxy.handler.ProxyHandler;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles",
                "true");
        IProxyDemo proxyDemo = (IProxyDemo) Proxy.newProxyInstance(ProxyDemoImpl.class.getClassLoader(),ProxyDemoImpl.class.getInterfaces(),new ProxyHandler(new ProxyDemoImpl()));
            proxyDemo.test();

    }
}
