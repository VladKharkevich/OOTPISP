package pluginUtils;

public interface PlugInterface {
    String code(byte[] data);

    byte[] decode(String data);

    String getName();

}
