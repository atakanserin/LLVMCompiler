package Javalette.Absyn; // Java Package generated by the BNF Converter.

public class EArrayPtr extends Expr {
  public final String ident_;
  public final Expr expr_;
  public EArrayPtr(String p1, Expr p2) { ident_ = p1; expr_ = p2; }

  public <R,A> R accept(Javalette.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Javalette.Absyn.EArrayPtr) {
      Javalette.Absyn.EArrayPtr x = (Javalette.Absyn.EArrayPtr)o;
      return this.ident_.equals(x.ident_) && this.expr_.equals(x.expr_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.ident_.hashCode())+this.expr_.hashCode();
  }


}
