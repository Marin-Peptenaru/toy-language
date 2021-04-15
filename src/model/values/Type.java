package model.values;

import exception.TypeException;


public class Type {
    protected enum TypeLabel {
        INT,
        BOOL,
        STRING,
        REFERENCE,
        UNKNOWN,
        LOCK,
        RWLOCK,
        SEMAPHORE,
        COUNTDOWN,
        BARRIER,
        VOID;
    }

    private final TypeLabel label;
    public final static Type Int = new Type(TypeLabel.INT);
    public final static Type Bool = new Type(TypeLabel.BOOL);
    public final static Type Str = new Type(TypeLabel.STRING);
    public final static Type Void = new Type(TypeLabel.VOID);


    protected Type(TypeLabel type){
        label = type;
    }


    public boolean equals(Type other) {
        return label == other.label;
    }

    @Override
    public String toString() {
        return label.toString();
    }

    public static void  expectType(Type givenType, Type expectedType) throws TypeException {
        if(!givenType.equals(expectedType))
            throw new TypeException("Expected " + expectedType + " instead of " + givenType);
    }

    public static Value<?> getDefaultValue(Type type){
        return switch(type.label){
            case INT -> new IntValue(0);
            case BOOL -> new BoolValue(false);
            case STRING -> new StringValue("");
            case REFERENCE -> new ReferenceValue(0, ((ReferenceType)type).getInner());
            default -> null;
        };
    }
}
