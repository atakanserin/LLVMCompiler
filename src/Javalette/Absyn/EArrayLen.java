package Javalette.Absyn; // Java Package generated by the BNF Converter.

public class EArrayLen extends Expr {
  public final String ident_;
  public EArrayLen(String p1) { ident_ = p1; }

  public <R,A> R accept(Javalette.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Javalette.Absyn.EArrayLen) {
      Javalette.Absyn.EArrayLen x = (Javalette.Absyn.EArrayLen)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}