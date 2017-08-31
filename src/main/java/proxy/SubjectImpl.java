package proxy;

/**
 * Created by ZHUKE on 2017/8/18.
 */
public class SubjectImpl implements Subject {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
