package Javalette;
import Javalette.Absyn.*;

public class PrettyPrinter
{
  //For certain applications increasing the initial size of the buffer may improve performance.
  private static final int INITIAL_BUFFER_SIZE = 128;
  private static final int INDENT_WIDTH = 2;
  //You may wish to change the parentheses used in precedence.
  private static final String _L_PAREN = new String("(");
  private static final String _R_PAREN = new String(")");
  //You may wish to change render
  private static void render(String s)
  {
    if (s.equals("{"))
    {
       buf_.append("\n");
       indent();
       buf_.append(s);
       _n_ = _n_ + INDENT_WIDTH;
       buf_.append("\n");
       indent();
    }
    else if (s.equals("(") || s.equals("["))
       buf_.append(s);
    else if (s.equals(")") || s.equals("]"))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals("}"))
    {
       int t;
       _n_ = _n_ - INDENT_WIDTH;
       for(t=0; t<INDENT_WIDTH; t++) {
         backup();
       }
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals(","))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals(";"))
    {
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals("")) return;
    else
    {
       buf_.append(s);
       buf_.append(" ");
    }
  }


  //  print and show methods are defined for each category.
  public static String print(Javalette.Absyn.Prog foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Prog foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.TopDef foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.TopDef foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.ListTopDef foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.ListTopDef foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.Arg foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Arg foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.ListArg foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.ListArg foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.Blk foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Blk foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.ListStmt foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.ListStmt foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.Stmt foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Stmt foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.Item foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Item foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.ListItem foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.ListItem foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.Type foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Type foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.ListType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.ListType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.Expr foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.Expr foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.ListExpr foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.ListExpr foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.AddOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.AddOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.MulOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.MulOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Javalette.Absyn.RelOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Javalette.Absyn.RelOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  /***   You shouldn't need to change anything beyond this point.   ***/

  private static void pp(Javalette.Absyn.Prog foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Program)
    {
       Javalette.Absyn.Program _program = (Javalette.Absyn.Program) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_program.listtopdef_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.TopDef foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.FnDef)
    {
       Javalette.Absyn.FnDef _fndef = (Javalette.Absyn.FnDef) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fndef.type_, 0);
       pp(_fndef.ident_, 0);
       render("(");
       pp(_fndef.listarg_, 0);
       render(")");
       pp(_fndef.blk_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.ListTopDef foo, int _i_)
  {
     for (java.util.Iterator<TopDef> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(Javalette.Absyn.Arg foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Argument)
    {
       Javalette.Absyn.Argument _argument = (Javalette.Absyn.Argument) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_argument.type_, 0);
       pp(_argument.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.ListArg foo, int _i_)
  {
     for (java.util.Iterator<Arg> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Javalette.Absyn.Blk foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Block)
    {
       Javalette.Absyn.Block _block = (Javalette.Absyn.Block) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_block.liststmt_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.ListStmt foo, int _i_)
  {
     for (java.util.Iterator<Stmt> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(Javalette.Absyn.Stmt foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Empty)
    {
       Javalette.Absyn.Empty _empty = (Javalette.Absyn.Empty) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.BStmt)
    {
       Javalette.Absyn.BStmt _bstmt = (Javalette.Absyn.BStmt) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_bstmt.blk_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Decl)
    {
       Javalette.Absyn.Decl _decl = (Javalette.Absyn.Decl) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_decl.type_, 0);
       pp(_decl.listitem_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Ass)
    {
       Javalette.Absyn.Ass _ass = (Javalette.Absyn.Ass) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_ass.ident_, 0);
       render("=");
       pp(_ass.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.AssArray)
    {
       Javalette.Absyn.AssArray _assarray = (Javalette.Absyn.AssArray) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_assarray.expr_1, 0);
       render("=");
       pp(_assarray.expr_2, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Incr)
    {
       Javalette.Absyn.Incr _incr = (Javalette.Absyn.Incr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_incr.ident_, 0);
       render("++");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Decr)
    {
       Javalette.Absyn.Decr _decr = (Javalette.Absyn.Decr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_decr.ident_, 0);
       render("--");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Ret)
    {
       Javalette.Absyn.Ret _ret = (Javalette.Absyn.Ret) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       pp(_ret.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.VRet)
    {
       Javalette.Absyn.VRet _vret = (Javalette.Absyn.VRet) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Cond)
    {
       Javalette.Absyn.Cond _cond = (Javalette.Absyn.Cond) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_cond.expr_, 0);
       render(")");
       pp(_cond.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.CondElse)
    {
       Javalette.Absyn.CondElse _condelse = (Javalette.Absyn.CondElse) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_condelse.expr_, 0);
       render(")");
       pp(_condelse.stmt_1, 0);
       render("else");
       pp(_condelse.stmt_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.While)
    {
       Javalette.Absyn.While _while = (Javalette.Absyn.While) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("while");
       render("(");
       pp(_while.expr_, 0);
       render(")");
       pp(_while.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.For)
    {
       Javalette.Absyn.For _for = (Javalette.Absyn.For) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("for");
       render("(");
       pp(_for.type_, 0);
       pp(_for.ident_, 0);
       render(":");
       pp(_for.expr_, 0);
       render(")");
       pp(_for.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.SExp)
    {
       Javalette.Absyn.SExp _sexp = (Javalette.Absyn.SExp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sexp.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.Item foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.NoInit)
    {
       Javalette.Absyn.NoInit _noinit = (Javalette.Absyn.NoInit) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_noinit.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Init)
    {
       Javalette.Absyn.Init _init = (Javalette.Absyn.Init) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_init.ident_, 0);
       render("=");
       pp(_init.expr_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.ListItem foo, int _i_)
  {
     for (java.util.Iterator<Item> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Javalette.Absyn.Type foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Type_int)
    {
       Javalette.Absyn.Type_int _type_int = (Javalette.Absyn.Type_int) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("int");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Type_double)
    {
       Javalette.Absyn.Type_double _type_double = (Javalette.Absyn.Type_double) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("double");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Type_bool)
    {
       Javalette.Absyn.Type_bool _type_bool = (Javalette.Absyn.Type_bool) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("boolean");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Type_void)
    {
       Javalette.Absyn.Type_void _type_void = (Javalette.Absyn.Type_void) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("void");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Type_array)
    {
       Javalette.Absyn.Type_array _type_array = (Javalette.Absyn.Type_array) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_type_array.type_, 0);
       render("[");
       render("]");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Fun)
    {
       Javalette.Absyn.Fun _fun = (Javalette.Absyn.Fun) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fun.type_, 0);
       render("(");
       pp(_fun.listtype_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.ListType foo, int _i_)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Javalette.Absyn.Expr foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.EVar)
    {
       Javalette.Absyn.EVar _evar = (Javalette.Absyn.EVar) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_evar.ident_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.ELitInt)
    {
       Javalette.Absyn.ELitInt _elitint = (Javalette.Absyn.ELitInt) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_elitint.integer_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.ELitDoub)
    {
       Javalette.Absyn.ELitDoub _elitdoub = (Javalette.Absyn.ELitDoub) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_elitdoub.double_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.ELitTrue)
    {
       Javalette.Absyn.ELitTrue _elittrue = (Javalette.Absyn.ELitTrue) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("true");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.ELitFalse)
    {
       Javalette.Absyn.ELitFalse _elitfalse = (Javalette.Absyn.ELitFalse) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("false");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EApp)
    {
       Javalette.Absyn.EApp _eapp = (Javalette.Absyn.EApp) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_eapp.ident_, 0);
       render("(");
       pp(_eapp.listexpr_, 0);
       render(")");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EString)
    {
       Javalette.Absyn.EString _estring = (Javalette.Absyn.EString) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_estring.string_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EArrayNew)
    {
       Javalette.Absyn.EArrayNew _earraynew = (Javalette.Absyn.EArrayNew) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("new");
       pp(_earraynew.type_, 0);
       render("[");
       pp(_earraynew.expr_, 0);
       render("]");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EArrayLen)
    {
       Javalette.Absyn.EArrayLen _earraylen = (Javalette.Absyn.EArrayLen) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_earraylen.ident_, 0);
       render(".");
       render("length");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EArrayPtr)
    {
       Javalette.Absyn.EArrayPtr _earrayptr = (Javalette.Absyn.EArrayPtr) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_earrayptr.ident_, 0);
       render("[");
       pp(_earrayptr.expr_, 0);
       render("]");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Neg)
    {
       Javalette.Absyn.Neg _neg = (Javalette.Absyn.Neg) foo;
       if (_i_ > 5) render(_L_PAREN);
       render("-");
       pp(_neg.expr_, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Not)
    {
       Javalette.Absyn.Not _not = (Javalette.Absyn.Not) foo;
       if (_i_ > 5) render(_L_PAREN);
       render("!");
       pp(_not.expr_, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EMul)
    {
       Javalette.Absyn.EMul _emul = (Javalette.Absyn.EMul) foo;
       if (_i_ > 4) render(_L_PAREN);
       pp(_emul.expr_1, 4);
       pp(_emul.mulop_, 0);
       pp(_emul.expr_2, 5);
       if (_i_ > 4) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EAdd)
    {
       Javalette.Absyn.EAdd _eadd = (Javalette.Absyn.EAdd) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_eadd.expr_1, 3);
       pp(_eadd.addop_, 0);
       pp(_eadd.expr_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.ERel)
    {
       Javalette.Absyn.ERel _erel = (Javalette.Absyn.ERel) foo;
       if (_i_ > 2) render(_L_PAREN);
       pp(_erel.expr_1, 2);
       pp(_erel.relop_, 0);
       pp(_erel.expr_2, 3);
       if (_i_ > 2) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EAnd)
    {
       Javalette.Absyn.EAnd _eand = (Javalette.Absyn.EAnd) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_eand.expr_1, 2);
       render("&&");
       pp(_eand.expr_2, 1);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EOr)
    {
       Javalette.Absyn.EOr _eor = (Javalette.Absyn.EOr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_eor.expr_1, 1);
       render("||");
       pp(_eor.expr_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.ListExpr foo, int _i_)
  {
     for (java.util.Iterator<Expr> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(Javalette.Absyn.AddOp foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Plus)
    {
       Javalette.Absyn.Plus _plus = (Javalette.Absyn.Plus) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("+");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Minus)
    {
       Javalette.Absyn.Minus _minus = (Javalette.Absyn.Minus) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("-");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.MulOp foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.Times)
    {
       Javalette.Absyn.Times _times = (Javalette.Absyn.Times) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("*");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Div)
    {
       Javalette.Absyn.Div _div = (Javalette.Absyn.Div) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("/");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.Mod)
    {
       Javalette.Absyn.Mod _mod = (Javalette.Absyn.Mod) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("%");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Javalette.Absyn.RelOp foo, int _i_)
  {
    if (foo instanceof Javalette.Absyn.LTH)
    {
       Javalette.Absyn.LTH _lth = (Javalette.Absyn.LTH) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("<");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.LE)
    {
       Javalette.Absyn.LE _le = (Javalette.Absyn.LE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("<=");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.GTH)
    {
       Javalette.Absyn.GTH _gth = (Javalette.Absyn.GTH) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(">");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.GE)
    {
       Javalette.Absyn.GE _ge = (Javalette.Absyn.GE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(">=");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.EQU)
    {
       Javalette.Absyn.EQU _equ = (Javalette.Absyn.EQU) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("==");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Javalette.Absyn.NE)
    {
       Javalette.Absyn.NE _ne = (Javalette.Absyn.NE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("!=");
       if (_i_ > 0) render(_R_PAREN);
    }
  }


  private static void sh(Javalette.Absyn.Prog foo)
  {
    if (foo instanceof Javalette.Absyn.Program)
    {
       Javalette.Absyn.Program _program = (Javalette.Absyn.Program) foo;
       render("(");
       render("Program");
       render("[");
       sh(_program.listtopdef_);
       render("]");
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.TopDef foo)
  {
    if (foo instanceof Javalette.Absyn.FnDef)
    {
       Javalette.Absyn.FnDef _fndef = (Javalette.Absyn.FnDef) foo;
       render("(");
       render("FnDef");
       sh(_fndef.type_);
       sh(_fndef.ident_);
       render("[");
       sh(_fndef.listarg_);
       render("]");
       sh(_fndef.blk_);
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.ListTopDef foo)
  {
     for (java.util.Iterator<TopDef> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Javalette.Absyn.Arg foo)
  {
    if (foo instanceof Javalette.Absyn.Argument)
    {
       Javalette.Absyn.Argument _argument = (Javalette.Absyn.Argument) foo;
       render("(");
       render("Argument");
       sh(_argument.type_);
       sh(_argument.ident_);
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.ListArg foo)
  {
     for (java.util.Iterator<Arg> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Javalette.Absyn.Blk foo)
  {
    if (foo instanceof Javalette.Absyn.Block)
    {
       Javalette.Absyn.Block _block = (Javalette.Absyn.Block) foo;
       render("(");
       render("Block");
       render("[");
       sh(_block.liststmt_);
       render("]");
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.ListStmt foo)
  {
     for (java.util.Iterator<Stmt> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Javalette.Absyn.Stmt foo)
  {
    if (foo instanceof Javalette.Absyn.Empty)
    {
       Javalette.Absyn.Empty _empty = (Javalette.Absyn.Empty) foo;
       render("Empty");
    }
    if (foo instanceof Javalette.Absyn.BStmt)
    {
       Javalette.Absyn.BStmt _bstmt = (Javalette.Absyn.BStmt) foo;
       render("(");
       render("BStmt");
       sh(_bstmt.blk_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Decl)
    {
       Javalette.Absyn.Decl _decl = (Javalette.Absyn.Decl) foo;
       render("(");
       render("Decl");
       sh(_decl.type_);
       render("[");
       sh(_decl.listitem_);
       render("]");
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Ass)
    {
       Javalette.Absyn.Ass _ass = (Javalette.Absyn.Ass) foo;
       render("(");
       render("Ass");
       sh(_ass.ident_);
       sh(_ass.expr_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.AssArray)
    {
       Javalette.Absyn.AssArray _assarray = (Javalette.Absyn.AssArray) foo;
       render("(");
       render("AssArray");
       sh(_assarray.expr_1);
       sh(_assarray.expr_2);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Incr)
    {
       Javalette.Absyn.Incr _incr = (Javalette.Absyn.Incr) foo;
       render("(");
       render("Incr");
       sh(_incr.ident_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Decr)
    {
       Javalette.Absyn.Decr _decr = (Javalette.Absyn.Decr) foo;
       render("(");
       render("Decr");
       sh(_decr.ident_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Ret)
    {
       Javalette.Absyn.Ret _ret = (Javalette.Absyn.Ret) foo;
       render("(");
       render("Ret");
       sh(_ret.expr_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.VRet)
    {
       Javalette.Absyn.VRet _vret = (Javalette.Absyn.VRet) foo;
       render("VRet");
    }
    if (foo instanceof Javalette.Absyn.Cond)
    {
       Javalette.Absyn.Cond _cond = (Javalette.Absyn.Cond) foo;
       render("(");
       render("Cond");
       sh(_cond.expr_);
       sh(_cond.stmt_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.CondElse)
    {
       Javalette.Absyn.CondElse _condelse = (Javalette.Absyn.CondElse) foo;
       render("(");
       render("CondElse");
       sh(_condelse.expr_);
       sh(_condelse.stmt_1);
       sh(_condelse.stmt_2);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.While)
    {
       Javalette.Absyn.While _while = (Javalette.Absyn.While) foo;
       render("(");
       render("While");
       sh(_while.expr_);
       sh(_while.stmt_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.For)
    {
       Javalette.Absyn.For _for = (Javalette.Absyn.For) foo;
       render("(");
       render("For");
       sh(_for.type_);
       sh(_for.ident_);
       sh(_for.expr_);
       sh(_for.stmt_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.SExp)
    {
       Javalette.Absyn.SExp _sexp = (Javalette.Absyn.SExp) foo;
       render("(");
       render("SExp");
       sh(_sexp.expr_);
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.Item foo)
  {
    if (foo instanceof Javalette.Absyn.NoInit)
    {
       Javalette.Absyn.NoInit _noinit = (Javalette.Absyn.NoInit) foo;
       render("(");
       render("NoInit");
       sh(_noinit.ident_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Init)
    {
       Javalette.Absyn.Init _init = (Javalette.Absyn.Init) foo;
       render("(");
       render("Init");
       sh(_init.ident_);
       sh(_init.expr_);
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.ListItem foo)
  {
     for (java.util.Iterator<Item> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Javalette.Absyn.Type foo)
  {
    if (foo instanceof Javalette.Absyn.Type_int)
    {
       Javalette.Absyn.Type_int _type_int = (Javalette.Absyn.Type_int) foo;
       render("Type_int");
    }
    if (foo instanceof Javalette.Absyn.Type_double)
    {
       Javalette.Absyn.Type_double _type_double = (Javalette.Absyn.Type_double) foo;
       render("Type_double");
    }
    if (foo instanceof Javalette.Absyn.Type_bool)
    {
       Javalette.Absyn.Type_bool _type_bool = (Javalette.Absyn.Type_bool) foo;
       render("Type_bool");
    }
    if (foo instanceof Javalette.Absyn.Type_void)
    {
       Javalette.Absyn.Type_void _type_void = (Javalette.Absyn.Type_void) foo;
       render("Type_void");
    }
    if (foo instanceof Javalette.Absyn.Type_array)
    {
       Javalette.Absyn.Type_array _type_array = (Javalette.Absyn.Type_array) foo;
       render("(");
       render("Type_array");
       sh(_type_array.type_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Fun)
    {
       Javalette.Absyn.Fun _fun = (Javalette.Absyn.Fun) foo;
       render("(");
       render("Fun");
       sh(_fun.type_);
       render("[");
       sh(_fun.listtype_);
       render("]");
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.ListType foo)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Javalette.Absyn.Expr foo)
  {
    if (foo instanceof Javalette.Absyn.EVar)
    {
       Javalette.Absyn.EVar _evar = (Javalette.Absyn.EVar) foo;
       render("(");
       render("EVar");
       sh(_evar.ident_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.ELitInt)
    {
       Javalette.Absyn.ELitInt _elitint = (Javalette.Absyn.ELitInt) foo;
       render("(");
       render("ELitInt");
       sh(_elitint.integer_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.ELitDoub)
    {
       Javalette.Absyn.ELitDoub _elitdoub = (Javalette.Absyn.ELitDoub) foo;
       render("(");
       render("ELitDoub");
       sh(_elitdoub.double_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.ELitTrue)
    {
       Javalette.Absyn.ELitTrue _elittrue = (Javalette.Absyn.ELitTrue) foo;
       render("ELitTrue");
    }
    if (foo instanceof Javalette.Absyn.ELitFalse)
    {
       Javalette.Absyn.ELitFalse _elitfalse = (Javalette.Absyn.ELitFalse) foo;
       render("ELitFalse");
    }
    if (foo instanceof Javalette.Absyn.EApp)
    {
       Javalette.Absyn.EApp _eapp = (Javalette.Absyn.EApp) foo;
       render("(");
       render("EApp");
       sh(_eapp.ident_);
       render("[");
       sh(_eapp.listexpr_);
       render("]");
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EString)
    {
       Javalette.Absyn.EString _estring = (Javalette.Absyn.EString) foo;
       render("(");
       render("EString");
       sh(_estring.string_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EArrayNew)
    {
       Javalette.Absyn.EArrayNew _earraynew = (Javalette.Absyn.EArrayNew) foo;
       render("(");
       render("EArrayNew");
       sh(_earraynew.type_);
       sh(_earraynew.expr_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EArrayLen)
    {
       Javalette.Absyn.EArrayLen _earraylen = (Javalette.Absyn.EArrayLen) foo;
       render("(");
       render("EArrayLen");
       sh(_earraylen.ident_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EArrayPtr)
    {
       Javalette.Absyn.EArrayPtr _earrayptr = (Javalette.Absyn.EArrayPtr) foo;
       render("(");
       render("EArrayPtr");
       sh(_earrayptr.ident_);
       sh(_earrayptr.expr_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Neg)
    {
       Javalette.Absyn.Neg _neg = (Javalette.Absyn.Neg) foo;
       render("(");
       render("Neg");
       sh(_neg.expr_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.Not)
    {
       Javalette.Absyn.Not _not = (Javalette.Absyn.Not) foo;
       render("(");
       render("Not");
       sh(_not.expr_);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EMul)
    {
       Javalette.Absyn.EMul _emul = (Javalette.Absyn.EMul) foo;
       render("(");
       render("EMul");
       sh(_emul.expr_1);
       sh(_emul.mulop_);
       sh(_emul.expr_2);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EAdd)
    {
       Javalette.Absyn.EAdd _eadd = (Javalette.Absyn.EAdd) foo;
       render("(");
       render("EAdd");
       sh(_eadd.expr_1);
       sh(_eadd.addop_);
       sh(_eadd.expr_2);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.ERel)
    {
       Javalette.Absyn.ERel _erel = (Javalette.Absyn.ERel) foo;
       render("(");
       render("ERel");
       sh(_erel.expr_1);
       sh(_erel.relop_);
       sh(_erel.expr_2);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EAnd)
    {
       Javalette.Absyn.EAnd _eand = (Javalette.Absyn.EAnd) foo;
       render("(");
       render("EAnd");
       sh(_eand.expr_1);
       sh(_eand.expr_2);
       render(")");
    }
    if (foo instanceof Javalette.Absyn.EOr)
    {
       Javalette.Absyn.EOr _eor = (Javalette.Absyn.EOr) foo;
       render("(");
       render("EOr");
       sh(_eor.expr_1);
       sh(_eor.expr_2);
       render(")");
    }
  }

  private static void sh(Javalette.Absyn.ListExpr foo)
  {
     for (java.util.Iterator<Expr> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Javalette.Absyn.AddOp foo)
  {
    if (foo instanceof Javalette.Absyn.Plus)
    {
       Javalette.Absyn.Plus _plus = (Javalette.Absyn.Plus) foo;
       render("Plus");
    }
    if (foo instanceof Javalette.Absyn.Minus)
    {
       Javalette.Absyn.Minus _minus = (Javalette.Absyn.Minus) foo;
       render("Minus");
    }
  }

  private static void sh(Javalette.Absyn.MulOp foo)
  {
    if (foo instanceof Javalette.Absyn.Times)
    {
       Javalette.Absyn.Times _times = (Javalette.Absyn.Times) foo;
       render("Times");
    }
    if (foo instanceof Javalette.Absyn.Div)
    {
       Javalette.Absyn.Div _div = (Javalette.Absyn.Div) foo;
       render("Div");
    }
    if (foo instanceof Javalette.Absyn.Mod)
    {
       Javalette.Absyn.Mod _mod = (Javalette.Absyn.Mod) foo;
       render("Mod");
    }
  }

  private static void sh(Javalette.Absyn.RelOp foo)
  {
    if (foo instanceof Javalette.Absyn.LTH)
    {
       Javalette.Absyn.LTH _lth = (Javalette.Absyn.LTH) foo;
       render("LTH");
    }
    if (foo instanceof Javalette.Absyn.LE)
    {
       Javalette.Absyn.LE _le = (Javalette.Absyn.LE) foo;
       render("LE");
    }
    if (foo instanceof Javalette.Absyn.GTH)
    {
       Javalette.Absyn.GTH _gth = (Javalette.Absyn.GTH) foo;
       render("GTH");
    }
    if (foo instanceof Javalette.Absyn.GE)
    {
       Javalette.Absyn.GE _ge = (Javalette.Absyn.GE) foo;
       render("GE");
    }
    if (foo instanceof Javalette.Absyn.EQU)
    {
       Javalette.Absyn.EQU _equ = (Javalette.Absyn.EQU) foo;
       render("EQU");
    }
    if (foo instanceof Javalette.Absyn.NE)
    {
       Javalette.Absyn.NE _ne = (Javalette.Absyn.NE) foo;
       render("NE");
    }
  }


  private static void pp(Integer n, int _i_) { buf_.append(n); buf_.append(" "); }
  private static void pp(Double d, int _i_) { buf_.append(d); buf_.append(" "); }
  private static void pp(String s, int _i_) { buf_.append(s); buf_.append(" "); }
  private static void pp(Character c, int _i_) { buf_.append("'" + c.toString() + "'"); buf_.append(" "); }
  private static void sh(Integer n) { render(n.toString()); }
  private static void sh(Double d) { render(d.toString()); }
  private static void sh(Character c) { render(c.toString()); }
  private static void sh(String s) { printQuoted(s); }
  private static void printQuoted(String s) { render("\"" + s + "\""); }
  private static void indent()
  {
    int n = _n_;
    while (n > 0)
    {
      buf_.append(" ");
      n--;
    }
  }
  private static void backup()
  {
     if (buf_.charAt(buf_.length() - 1) == ' ') {
      buf_.setLength(buf_.length() - 1);
    }
  }
  private static void trim()
  {
     while (buf_.length() > 0 && buf_.charAt(0) == ' ')
        buf_.deleteCharAt(0); 
    while (buf_.length() > 0 && buf_.charAt(buf_.length()-1) == ' ')
        buf_.deleteCharAt(buf_.length()-1);
  }
  private static int _n_ = 0;
  private static StringBuilder buf_ = new StringBuilder(INITIAL_BUFFER_SIZE);
}

