import java.util.Base64;

public class Base64Plugin implements PlugInterface{

    private final String name = "Base64";

    public String code(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    @Override
    public String getName() {
        return this.name;
    }
}