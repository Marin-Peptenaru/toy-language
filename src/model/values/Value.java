package model.values;

public interface Value<T extends Comparable<? super T>> extends Comparable<Value<T>>{
    Type getType();
    T getValue();
    String toString();

    @Override
    default int compareTo(Value<T> o){
        return getValue().compareTo(o.getValue());
    }


    default boolean equals(Value<T> o){
        return getValue().equals(o.getValue());
    }

}
