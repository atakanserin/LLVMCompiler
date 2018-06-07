public class TypeException extends RuntimeException {

    public TypeException(String msg) {
        super("ERROR\n"+msg);
    }

}
