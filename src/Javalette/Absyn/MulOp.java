package Javalette.Absyn; // Java Package generated by the BNF Converter.

public abstract class MulOp implements java.io.Serializable {
  public abstract <R,A> R accept(MulOp.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(Javalette.Absyn.Times p, A arg);
    public R visit(Javalette.Absyn.Div p, A arg);
    public R visit(Javalette.Absyn.Mod p, A arg);

  }

}
