package Javalette.Absyn; // Java Package generated by the BNF Converter.

public class EArrayNew extends Expr {
  public final Type type_;
  public final Expr expr_;
  public EArrayNew(Type p1, Expr p2) { type_ = p1; expr_ = p2; }

  public <R,A> R accept(Javalette.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Javalette.Absyn.EArrayNew) {
      Javalette.Absyn.EArrayNew x = (Javalette.Absyn.EArrayNew)o;
      return this.type_.equals(x.type_) && this.expr_.equals(x.expr_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.type_.hashCode())+this.expr_.hashCode();
  }


}
