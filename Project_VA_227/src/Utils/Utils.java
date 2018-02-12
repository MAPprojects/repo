package Utils;

public class Utils {
    public static Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String NullOrString(String value) {
        value = value.trim();
        if (value.equals(""))
            return null;
        return value;
    }
}
