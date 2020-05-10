import org.apache.commons.codec.binary.Base32;

public class Base32Plugin implements PlugInterface {

    private String name = "Base32";

    public String code(byte[] data){
        Base32 base32 = new Base32();
        return base32.encodeAsString(data);
    }

    public byte[] decode(String data){
        Base32 base32 = new Base32();
        return base32.decode(data);
    }

    @Override
    public String getName() {
        return this.name;
    }
}