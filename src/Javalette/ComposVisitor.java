package Javalette;
import Javalette.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  Javalette.Absyn.Prog.Visitor<Javalette.Absyn.Prog,A>,
  Javalette.Absyn.TopDef.Visitor<Javalette.Absyn.TopDef,A>,
  Javalette.Absyn.Arg.Visitor<Javalette.Absyn.Arg,A>,
  Javalette.Absyn.Blk.Visitor<Javalette.Absyn.Blk,A>,
  Javalette.Absyn.Stmt.Visitor<Javalette.Absyn.Stmt,A>,
  Javalette.Absyn.Item.Visitor<Javalette.Absyn.Item,A>,
  Javalette.Absyn.Type.Visitor<Javalette.Absyn.Type,A>,
  Javalette.Absyn.Expr.Visitor<Javalette.Absyn.Expr,A>,
  Javalette.Absyn.AddOp.Visitor<Javalette.Absyn.AddOp,A>,
  Javalette.Absyn.MulOp.Visitor<Javalette.Absyn.MulOp,A>,
  Javalette.Absyn.RelOp.Visitor<Javalette.Absyn.RelOp,A>
{
/* Prog */
    public Prog visit(Javalette.Absyn.Program p, A arg)
    {
      ListTopDef listtopdef_ = new ListTopDef();
      for (TopDef x : p.listtopdef_)
      {
        listtopdef_.add(x.accept(this,arg));
      }
      return new Javalette.Absyn.Program(listtopdef_);
    }
/* TopDef */
    public TopDef visit(Javalette.Absyn.FnDef p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      ListArg listarg_ = new ListArg();
      for (Arg x : p.listarg_)
      {
        listarg_.add(x.accept(this,arg));
      }
      Blk blk_ = p.blk_.accept(this, arg);
      return new Javalette.Absyn.FnDef(type_, ident_, listarg_, blk_);
    }
/* Arg */
    public Arg visit(Javalette.Absyn.Argument p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      return new Javalette.Absyn.Argument(type_, ident_);
    }
/* Blk */
    public Blk visit(Javalette.Absyn.Block p, A arg)
    {
      ListStmt liststmt_ = new ListStmt();
      for (Stmt x : p.liststmt_)
      {
        liststmt_.add(x.accept(this,arg));
      }
      return new Javalette.Absyn.Block(liststmt_);
    }
/* Stmt */
    public Stmt visit(Javalette.Absyn.Empty p, A arg)
    {
      return new Javalette.Absyn.Empty();
    }    public Stmt visit(Javalette.Absyn.BStmt p, A arg)
    {
      Blk blk_ = p.blk_.accept(this, arg);
      return new Javalette.Absyn.BStmt(blk_);
    }    public Stmt visit(Javalette.Absyn.Decl p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      ListItem listitem_ = new ListItem();
      for (Item x : p.listitem_)
      {
        listitem_.add(x.accept(this,arg));
      }
      return new Javalette.Absyn.Decl(type_, listitem_);
    }    public Stmt visit(Javalette.Absyn.Ass p, A arg)
    {
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.Ass(ident_, expr_);
    }    public Stmt visit(Javalette.Absyn.AssArray p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Javalette.Absyn.AssArray(expr_1, expr_2);
    }    public Stmt visit(Javalette.Absyn.Incr p, A arg)
    {
      String ident_ = p.ident_;
      return new Javalette.Absyn.Incr(ident_);
    }    public Stmt visit(Javalette.Absyn.Decr p, A arg)
    {
      String ident_ = p.ident_;
      return new Javalette.Absyn.Decr(ident_);
    }    public Stmt visit(Javalette.Absyn.Ret p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.Ret(expr_);
    }    public Stmt visit(Javalette.Absyn.VRet p, A arg)
    {
      return new Javalette.Absyn.VRet();
    }    public Stmt visit(Javalette.Absyn.Cond p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Javalette.Absyn.Cond(expr_, stmt_);
    }    public Stmt visit(Javalette.Absyn.CondElse p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_1 = p.stmt_1.accept(this, arg);
      Stmt stmt_2 = p.stmt_2.accept(this, arg);
      return new Javalette.Absyn.CondElse(expr_, stmt_1, stmt_2);
    }    public Stmt visit(Javalette.Absyn.While p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Javalette.Absyn.While(expr_, stmt_);
    }    public Stmt visit(Javalette.Absyn.For p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Javalette.Absyn.For(type_, ident_, expr_, stmt_);
    }    public Stmt visit(Javalette.Absyn.SExp p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.SExp(expr_);
    }
/* Item */
    public Item visit(Javalette.Absyn.NoInit p, A arg)
    {
      String ident_ = p.ident_;
      return new Javalette.Absyn.NoInit(ident_);
    }    public Item visit(Javalette.Absyn.Init p, A arg)
    {
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.Init(ident_, expr_);
    }
/* Type */
    public Type visit(Javalette.Absyn.Type_int p, A arg)
    {
      return new Javalette.Absyn.Type_int();
    }    public Type visit(Javalette.Absyn.Type_double p, A arg)
    {
      return new Javalette.Absyn.Type_double();
    }    public Type visit(Javalette.Absyn.Type_bool p, A arg)
    {
      return new Javalette.Absyn.Type_bool();
    }    public Type visit(Javalette.Absyn.Type_void p, A arg)
    {
      return new Javalette.Absyn.Type_void();
    }    public Type visit(Javalette.Absyn.Type_array p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      return new Javalette.Absyn.Type_array(type_);
    }    public Type visit(Javalette.Absyn.Fun p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      ListType listtype_ = new ListType();
      for (Type x : p.listtype_)
      {
        listtype_.add(x.accept(this,arg));
      }
      return new Javalette.Absyn.Fun(type_, listtype_);
    }
/* Expr */
    public Expr visit(Javalette.Absyn.EVar p, A arg)
    {
      String ident_ = p.ident_;
      return new Javalette.Absyn.EVar(ident_);
    }    public Expr visit(Javalette.Absyn.ELitInt p, A arg)
    {
      Integer integer_ = p.integer_;
      return new Javalette.Absyn.ELitInt(integer_);
    }    public Expr visit(Javalette.Absyn.ELitDoub p, A arg)
    {
      Double double_ = p.double_;
      return new Javalette.Absyn.ELitDoub(double_);
    }    public Expr visit(Javalette.Absyn.ELitTrue p, A arg)
    {
      return new Javalette.Absyn.ELitTrue();
    }    public Expr visit(Javalette.Absyn.ELitFalse p, A arg)
    {
      return new Javalette.Absyn.ELitFalse();
    }    public Expr visit(Javalette.Absyn.EApp p, A arg)
    {
      String ident_ = p.ident_;
      ListExpr listexpr_ = new ListExpr();
      for (Expr x : p.listexpr_)
      {
        listexpr_.add(x.accept(this,arg));
      }
      return new Javalette.Absyn.EApp(ident_, listexpr_);
    }    public Expr visit(Javalette.Absyn.EString p, A arg)
    {
      String string_ = p.string_;
      return new Javalette.Absyn.EString(string_);
    }    public Expr visit(Javalette.Absyn.EArrayNew p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.EArrayNew(type_, expr_);
    }    public Expr visit(Javalette.Absyn.EArrayLen p, A arg)
    {
      String ident_ = p.ident_;
      return new Javalette.Absyn.EArrayLen(ident_);
    }    public Expr visit(Javalette.Absyn.EArrayPtr p, A arg)
    {
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.EArrayPtr(ident_, expr_);
    }    public Expr visit(Javalette.Absyn.Neg p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.Neg(expr_);
    }    public Expr visit(Javalette.Absyn.Not p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Javalette.Absyn.Not(expr_);
    }    public Expr visit(Javalette.Absyn.EMul p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      MulOp mulop_ = p.mulop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Javalette.Absyn.EMul(expr_1, mulop_, expr_2);
    }    public Expr visit(Javalette.Absyn.EAdd p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      AddOp addop_ = p.addop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Javalette.Absyn.EAdd(expr_1, addop_, expr_2);
    }    public Expr visit(Javalette.Absyn.ERel p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      RelOp relop_ = p.relop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Javalette.Absyn.ERel(expr_1, relop_, expr_2);
    }    public Expr visit(Javalette.Absyn.EAnd p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Javalette.Absyn.EAnd(expr_1, expr_2);
    }    public Expr visit(Javalette.Absyn.EOr p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Javalette.Absyn.EOr(expr_1, expr_2);
    }
/* AddOp */
    public AddOp visit(Javalette.Absyn.Plus p, A arg)
    {
      return new Javalette.Absyn.Plus();
    }    public AddOp visit(Javalette.Absyn.Minus p, A arg)
    {
      return new Javalette.Absyn.Minus();
    }
/* MulOp */
    public MulOp visit(Javalette.Absyn.Times p, A arg)
    {
      return new Javalette.Absyn.Times();
    }    public MulOp visit(Javalette.Absyn.Div p, A arg)
    {
      return new Javalette.Absyn.Div();
    }    public MulOp visit(Javalette.Absyn.Mod p, A arg)
    {
      return new Javalette.Absyn.Mod();
    }
/* RelOp */
    public RelOp visit(Javalette.Absyn.LTH p, A arg)
    {
      return new Javalette.Absyn.LTH();
    }    public RelOp visit(Javalette.Absyn.LE p, A arg)
    {
      return new Javalette.Absyn.LE();
    }    public RelOp visit(Javalette.Absyn.GTH p, A arg)
    {
      return new Javalette.Absyn.GTH();
    }    public RelOp visit(Javalette.Absyn.GE p, A arg)
    {
      return new Javalette.Absyn.GE();
    }    public RelOp visit(Javalette.Absyn.EQU p, A arg)
    {
      return new Javalette.Absyn.EQU();
    }    public RelOp visit(Javalette.Absyn.NE p, A arg)
    {
      return new Javalette.Absyn.NE();
    }
}