// Function types

import java.util.*;
import Javalette.Absyn.*;

public class FunType {
  final Type returnType;
  final ListArg args;
  public FunType (Type t, ListArg l) {
    returnType = t;
    args = l;
  }
  public String toLLVM () {
   String argTypes = "";
   for (Arg a: args) {
     //ADecl decl = (ADecl)a;
     //argTypes = argTypes + decl.type_.accept (new TypeVisitor (), null);
   }
   return "(" + argTypes + ")" + returnType.accept (new TypeVisitor(), null);
  }
}


class TypeVisitor implements Type.Visitor<String,Void>
{
  public String visit(Javalette.Absyn.Type_bool p, Void arg)
  {
    return "Z";
  }
  public String visit(Javalette.Absyn.Type_int p, Void arg)
  {
    return "I";
  }
  public String visit(Javalette.Absyn.Type_double p, Void arg)
  {
    return "D";
  }
   public String visit(Javalette.Absyn.Type_void p, Void arg)
  {
    return "V";
  }
   public String visit(Javalette.Absyn.Type_array p, Void arg)
  {
    return "A";
  }
  public String visit(Javalette.Absyn.Fun p, Void arg)  { return null;  }
}

