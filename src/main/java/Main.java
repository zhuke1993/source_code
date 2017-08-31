import java.util.Base64;

/**
 * Created by ZHUKE on 2017/8/18.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(getShortName("zhuke"));
    }
    private static String getShortName(String key) {
        final String[] keyParts = key.split("\\.");
        return keyParts[keyParts.length - 1];
    }
}
