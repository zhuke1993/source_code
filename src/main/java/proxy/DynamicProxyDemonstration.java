package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 动态代理演示
 */
public class DynamicProxyDemonstration {
    public static void main(String[] args) {
        //代理的真实对象
        Subject realSubject = new SubjectImpl();

        /*
         * InvocationHandlerImpl 实现了 InvocationHandler 接口，并能实现方法调用从代理类到委托类的分派转发
         * 其内部通常包含指向委托类实例的引用，用于真正执行分派转发过来的方法调用.
         * 即：要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法
         */
        InvocationHandler handler = new SubjectInvokeHandler(realSubject);


        ClassLoader loader = realSubject.getClass().getClassLoader();
        Class[] interfaces = realSubject.getClass().getInterfaces();
        /*
         * 该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例
         */
        Subject proxySubject = (Subject) Proxy.newProxyInstance(loader, interfaces, handler);

        System.out.println("动态代理对象的类型：" + proxySubject.getClass().getName());

        for (int i = 0; i < 100; i++) {
            proxySubject.sayHello("zhuke");
            realSubject.sayHello("zhuke");
        }

        long millis = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            proxySubject.sayHello("zhuke");
        }
        long millis1 = System.currentTimeMillis();
        System.out.println("Proxy subject execute 1000000 consume time=" + (millis1 - millis) + " ms");

        for (int i = 0; i < 1000000000; i++) {
            realSubject.sayHello("zhuke");
        }
        long millis2 = System.currentTimeMillis();
        System.out.println("Real subject execute 1000000 consume time=" + (millis2 - millis1) + " ms");

    }

}