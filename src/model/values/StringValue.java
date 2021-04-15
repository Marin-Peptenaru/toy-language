package model.values;

public class StringValue implements Value<String> {

    String value;

    public StringValue(String value){
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.Str;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean equals(StringValue other) {
        return value.equals(other.getValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
