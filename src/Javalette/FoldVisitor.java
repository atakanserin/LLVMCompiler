package Javalette;

import Javalette.Absyn.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Prog */
    public R visit(Javalette.Absyn.Program p, A arg) {
      R r = leaf(arg);
      for (TopDef x : p.listtopdef_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* TopDef */
    public R visit(Javalette.Absyn.FnDef p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Arg x : p.listarg_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.blk_.accept(this, arg), r, arg);
      return r;
    }

/* Arg */
    public R visit(Javalette.Absyn.Argument p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* Blk */
    public R visit(Javalette.Absyn.Block p, A arg) {
      R r = leaf(arg);
      for (Stmt x : p.liststmt_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Stmt */
    public R visit(Javalette.Absyn.Empty p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.BStmt p, A arg) {
      R r = leaf(arg);
      r = combine(p.blk_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.Decl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Item x : p.listitem_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(Javalette.Absyn.Ass p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.AssArray p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.Incr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Decr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Ret p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.VRet p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Cond p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.CondElse p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_1.accept(this, arg), r, arg);
      r = combine(p.stmt_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.While p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.For p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.SExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Item */
    public R visit(Javalette.Absyn.NoInit p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Init p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Type */
    public R visit(Javalette.Absyn.Type_int p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Type_double p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Type_bool p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Type_void p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Type_array p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.Fun p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Type x : p.listtype_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Expr */
    public R visit(Javalette.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.ELitInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.ELitDoub p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.ELitTrue p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.ELitFalse p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.EApp p, A arg) {
      R r = leaf(arg);
      for (Expr x : p.listexpr_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(Javalette.Absyn.EString p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.EArrayNew p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.EArrayLen p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.EArrayPtr p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.Neg p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.Not p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.EMul p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.mulop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.EAdd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.addop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.ERel p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.relop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.EAnd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Javalette.Absyn.EOr p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }

/* AddOp */
    public R visit(Javalette.Absyn.Plus p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Minus p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* MulOp */
    public R visit(Javalette.Absyn.Times p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Div p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.Mod p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* RelOp */
    public R visit(Javalette.Absyn.LTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.LE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.GTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.GE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.EQU p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Javalette.Absyn.NE p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
