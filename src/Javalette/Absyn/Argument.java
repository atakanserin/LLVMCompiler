package Javalette.Absyn; // Java Package generated by the BNF Converter.

public class Argument extends Arg {
  public final Type type_;
  public final String ident_;
  public Argument(Type p1, String p2) { type_ = p1; ident_ = p2; }

  public <R,A> R accept(Javalette.Absyn.Arg.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Javalette.Absyn.Argument) {
      Javalette.Absyn.Argument x = (Javalette.Absyn.Argument)o;
      return this.type_.equals(x.type_) && this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.type_.hashCode())+this.ident_.hashCode();
  }


}
