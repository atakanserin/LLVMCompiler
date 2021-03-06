package Javalette.Absyn; // Java Package generated by the BNF Converter.

public abstract class Expr implements java.io.Serializable {
  public abstract <R,A> R accept(Expr.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(Javalette.Absyn.EVar p, A arg);
    public R visit(Javalette.Absyn.ELitInt p, A arg);
    public R visit(Javalette.Absyn.ELitDoub p, A arg);
    public R visit(Javalette.Absyn.ELitTrue p, A arg);
    public R visit(Javalette.Absyn.ELitFalse p, A arg);
    public R visit(Javalette.Absyn.EApp p, A arg);
    public R visit(Javalette.Absyn.EString p, A arg);
    public R visit(Javalette.Absyn.EArrayNew p, A arg);
    public R visit(Javalette.Absyn.EArrayLen p, A arg);
    public R visit(Javalette.Absyn.EArrayPtr p, A arg);
    public R visit(Javalette.Absyn.Neg p, A arg);
    public R visit(Javalette.Absyn.Not p, A arg);
    public R visit(Javalette.Absyn.EMul p, A arg);
    public R visit(Javalette.Absyn.EAdd p, A arg);
    public R visit(Javalette.Absyn.ERel p, A arg);
    public R visit(Javalette.Absyn.EAnd p, A arg);
    public R visit(Javalette.Absyn.EOr p, A arg);

  }

}
