package Javalette;

import Javalette.Absyn.*;

/** BNFC-Generated All Visitor */
public interface AllVisitor<R,A> extends
  Javalette.Absyn.Prog.Visitor<R,A>,
  Javalette.Absyn.TopDef.Visitor<R,A>,
  Javalette.Absyn.Arg.Visitor<R,A>,
  Javalette.Absyn.Blk.Visitor<R,A>,
  Javalette.Absyn.Stmt.Visitor<R,A>,
  Javalette.Absyn.Item.Visitor<R,A>,
  Javalette.Absyn.Type.Visitor<R,A>,
  Javalette.Absyn.Expr.Visitor<R,A>,
  Javalette.Absyn.AddOp.Visitor<R,A>,
  Javalette.Absyn.MulOp.Visitor<R,A>,
  Javalette.Absyn.RelOp.Visitor<R,A>
{}
