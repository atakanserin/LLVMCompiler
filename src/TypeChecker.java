import Javalette.*;
import Javalette.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

public class TypeChecker
{
    
    //////////////////////////////////////////////////////////////////////
    public enum TypeCode {CInt, CDouble, CBool, CVoid, CString, CArray, CArrayInt, CArrayDouble, CArrayBool} ;
    
    //////////////////////////////////////////////////////////////////////
    // Help class holding functions and their corresponding types
    //////////////////////////////////////////////////////////////////////
    public class FunType{
	
	public LinkedList<TypeCode> args ;
	public TypeCode val ;
	public FunType(){
	    args = new LinkedList<TypeCode>();
	}
	public void addArg(TypeCode t){
	    args.add(t);
	}
	public void setVal(TypeCode t){
	    val = t;
	}
	public void emptyArgs(){
	    args = new LinkedList<TypeCode>();
	}
    }

    //////////////////////////////////////////////////////////////////////
    public void extendBuiltInFunctions(Env env){

	FunType ft = new FunType();
	ft.setVal( TypeCode.CVoid );
	ft.addArg( TypeCode.CInt );
	env.extendFun("printInt", ft);

	FunType ft2 = new FunType();
	ft2.setVal( TypeCode.CVoid );
	ft2.addArg( TypeCode.CDouble );
	env.extendFun("printDouble", ft2);

	FunType ft3 = new FunType();
	ft3.setVal( TypeCode.CInt );
	env.extendFun("readInt", ft3);

	FunType ft4 = new FunType();
	ft4.setVal( TypeCode.CDouble );
	env.extendFun("readDouble", ft4);


	FunType ft5 = new FunType();
	ft5.setVal( TypeCode.CVoid );
	ft5.addArg( TypeCode.CString );
	env.extendFun("printString", ft5);
	
    }

    
    //////////////////////////////////////////////////////////////////////
    // Entry function for typechecking
    //////////////////////////////////////////////////////////////////////

    public void typecheck(Prog p) {

	BuildSignatureVisit buildSV = new BuildSignatureVisit();
	BuildSignatureVisit.ProgVisitor pVis = buildSV.new ProgVisitor();
	Env env = new Env();

	extendBuiltInFunctions(env);
	p.<Void, Env> accept( pVis, env);
	//env.printSignature();
	
	
	Check check = new Check();
	Check.ProgVisitor checkP = check.new ProgVisitor();
	p.<Void, Env> accept( checkP, env);
	
    }

    //////////////////////////////////////////////////////////////////////
    //
    //////////////////////////////////////////////////////////////////////
    public class Check {
	//////////////////////////////////////////////////////////////////////
	//  Program. Prog ::= [TopDef];
    	public class ProgVisitor implements Prog.Visitor<Void, Env>
    	{
	    
	    public Void visit(Javalette.Absyn.Program p, Env env)
	    { 
		for (TopDef x: p.listtopdef_){
		    x.<Void, Env> accept(new Check.TopDefVisitor(), env); 
		}   
		return null;
	    }
    	}
	//////////////////////////////////////////////////////////////////////
	// FnDef. TopDef ::= Type Ident "(" [Arg] ")" Blk ;
  	public class TopDefVisitor implements TopDef.Visitor<Void,Env>
  	{
	    public Void visit(Javalette.Absyn.FnDef p, Env env)
	    {
		env.newBlock();
		env.lastFuncType = p.type_.<TypeCode, Env> accept(new Check.TypeVisitor(), env);
		env.lastFuncId = p.ident_;
		
		for (Arg arg: p.listarg_){
		    arg.<Void, Env> accept(new Check.ArgVisitor(), env);
		}

		env.returnExists = p.blk_.<Boolean, Env> accept(new BlkVisitor(), env);

		if(env.lastFuncType == TypeCode.CVoid){}
		else if( env.returnExists == false ){
		    throw new TypeException("Return does not exist!");
		}
		env.returnExists = false;

		env.removeLastBlock();
		return null;
	    }
  	}


	//////////////////////////////////////////////////////////////////////
	// Argument.  Arg ::= Type Ident;
	public class ArgVisitor implements Arg.Visitor<Void, Env>
	{
	    public Void visit(Javalette.Absyn.Argument arg, Env env)
	    {
		
		
		TypeCode tc = arg.type_.<TypeCode, Env> accept(new Check.TypeVisitor(), env);
		env.extendVar( arg.ident_, tc);
		return null;
	    }
	}
	
	//////////////////////////////////////////////////////////////////////
	// Block.     Blk ::= "{" [Stmt] "}" ;
	public class BlkVisitor implements Blk.Visitor<Boolean,Env>
	{
	    public Boolean visit(Javalette.Absyn.Block p, Env env)
	    { 
		env.newBlock();
		for (Stmt stmt: p.liststmt_){
		    if( stmt.<Boolean, Env> accept(new Check.StmtVisitor(), env) == true){
			env.removeLastBlock();
			return true;
		    }
		   
		}
		env.removeLastBlock();
		return false;
	    }
	}

	
	//////////////////////////////////////////////////////////////////////
	public class StmtVisitor implements Stmt.Visitor<Boolean, Env>
	{

	    // Empty.     Stmt ::= ";" ;
	    public Boolean visit(Javalette.Absyn.Empty eStmt, Env env)
	    { return false; }    

	    // BStmt.     Stmt ::= Blk ;
	    public Boolean visit(Javalette.Absyn.BStmt bStmt, Env env)
	    {
		
		return bStmt.blk_.<Boolean, Env> accept(new BlkVisitor(), env);
		
		
	    }    

	    // Decl.      Stmt ::= Type [Item] ";" ;
	    public Boolean visit(Javalette.Absyn.Decl p, Env env)
	    {
		TypeCode ty = p.type_.<TypeCode, Env> accept(new Check.TypeVisitor(), env);
		env.lastDeclarationType = ty;
		for (Item item: p.listitem_){
		    item.<Void, Env> accept(new ItemVisitor(), env);
		}
		return false;
	    } 

   	    //Ass.       Stmt ::= Ident "=" Expr  ";" 
	    public Boolean visit(Javalette.Absyn.Ass p, Env env)
	    {
		TypeCode tIdent = env.lookupVar(p.ident_);
		TypeCode tExpr = p.expr_.<TypeCode, Env> accept(new ExprVisitor(), env);
		if(tIdent != tExpr){
		    throw new TypeException(p.ident_ + " of type " + tIdent + " does not match the expression of type " + tExpr);
		}
		return false;
	    }   

	    //AssArray.  Stmt ::= Expr "=" Expr ";" ;
	    public Boolean visit(Javalette.Absyn.AssArray p, Env env)
	    {
		if(p.expr_1 instanceof Javalette.Absyn.EArrayPtr){
		    TypeCode tExpr1 = p.expr_1.<TypeCode, Env> accept(new ExprVisitor(), env);
		    TypeCode tExpr2 = p.expr_2.<TypeCode, Env> accept(new ExprVisitor(), env);
		    if(tExpr1 != tExpr2){
			throw new TypeException("Statement: \"x[] = expression;\" , x and the expression have different types");
		    }
		    return false;
		}else{
		    throw new TypeException("Statement: \"x = expression ;\" ,  x must be an array");
		}
		
	    }   
	

	    // Incr.      Stmt ::= Ident "++"  ";" 
	    public Boolean visit(Javalette.Absyn.Incr p, Env env)
	    { 
		
		TypeCode tIdent = env.lookupVar(p.ident_);
		if(tIdent != TypeCode.CInt && tIdent != TypeCode.CDouble){
		    throw new TypeException(p.ident_ + " of type " + tIdent + " does not match operator ++ of Int or Double");
		}
		return false;
	    }   
 
	    // Decr.      Stmt ::= Ident "--"  ";"
	    public Boolean visit(Javalette.Absyn.Decr p, Env env)
	    { 
		TypeCode tIdent = env.lookupVar(p.ident_);
		if(tIdent != TypeCode.CInt && tIdent != TypeCode.CDouble){
		    throw new TypeException(p.ident_ + " of type " + tIdent + " does not match operator -- of Int or Double");
		}
		return false;

	    }    
	    
	    // Ret.       Stmt ::= "return" Expr ";"
	    public Boolean visit(Javalette.Absyn.Ret p, Env env)
	    { 
		TypeCode tExpr = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if( tExpr == TypeCode.CVoid ){
		    throw new TypeException("Return stm: Expression cant have type Void");	
	    
		}if( env.lastFuncType != tExpr ){
		    throw new TypeException("Return stm: "+env.lastFuncId+" of type "+ env.lastFuncType +" doesnt match return type " + tExpr);   
		} 
		return true;
		
	    }   
	    
	    // VRet.      Stmt ::= "return" ";"
	    public Boolean visit(Javalette.Absyn.VRet p, Env env)
	    {   
		if( env.lastFuncType != TypeCode.CVoid){
		    throw new TypeException(env.lastFuncId +" must return a value.");
		}
		return false;
	    }   
	   
  	    // Cond.      Stmt ::= "if" "(" Expr ")" Stmt 
	    public Boolean visit(Javalette.Absyn.Cond p, Env env)
	    { 
		Boolean a;
		TypeCode tExpr = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if(tExpr != TypeCode.CBool){
		    throw new TypeException( "Expression inside of if statements must be of type Boolean");
		}
		env.newBlock();
		a = p.stmt_.<Boolean,Env> accept(new StmtVisitor(), env);
		env.removeLastBlock();
		
		if(p.expr_ instanceof Javalette.Absyn.ELitTrue)
		    return a;
		return false;
	    }   
	    // CondElse.  Stmt ::= "if" "(" Expr ")" Stmt "else" Stmt 
            public Boolean visit(Javalette.Absyn.CondElse p, Env env)
	    { 
		Boolean a;
		Boolean b;
		TypeCode tExpr = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if(tExpr != TypeCode.CBool){
		    throw new TypeException( "Expression inside of if else statements must be of type Boolean");
		}
		env.newBlock();
		a = p.stmt_1.<Boolean,Env> accept(new StmtVisitor(), env);
		env.removeLastBlock();

		env.newBlock();
		b = p.stmt_2.<Boolean,Env> accept(new StmtVisitor(), env);
		env.removeLastBlock();

		if(p.expr_ instanceof Javalette.Absyn.ELitTrue)
		    return a;
		if(p.expr_ instanceof Javalette.Absyn.ELitFalse)
		    return b;
		return a && b;
	    }    
	    // While.     Stmt ::= "while" "(" Expr ")" Stmt 
	    public Boolean visit(Javalette.Absyn.While p, Env env)
	    { 	Boolean a;
 		TypeCode tExpr = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if(tExpr != TypeCode.CBool){
		    throw new TypeException( "Expression inside of while statements must be of type Boolean");
		}
		env.newBlock();
		a = p.stmt_.<Boolean,Env> accept(new StmtVisitor(), env);
		env.removeLastBlock();
		if(p.expr_ instanceof Javalette.Absyn.ELitTrue)
		    return a;
		return false;
	    } 
	    // For.	   Stmt ::= "for" "(" Type Ident ":" Expr ")" Stmt
	    public Boolean visit(Javalette.Absyn.For p, Env env)
	    { 	
		Boolean a;

		TypeCode tExpr = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if(!(tExpr == TypeCode.CArrayInt || tExpr == TypeCode.CArrayDouble || tExpr == TypeCode.CArrayBool)){
		    throw new TypeException( "Expression inside for loop must be an array.");
		}
		TypeCode t = p.type_.<TypeCode,Env> accept(new TypeVisitor(), env);
		if( (tExpr == TypeCode.CArrayInt && t == TypeCode.CInt) || 
		    (tExpr == TypeCode.CArrayDouble && t == TypeCode.CDouble) || 
		    (tExpr == TypeCode.CArrayBool && t == TypeCode.CBool)){
		    env.newBlock();
		    env.extendVar(p.ident_, t);
		    a = p.stmt_.<Boolean,Env> accept(new StmtVisitor(), env);
		    env.removeLastBlock();
		    return a;
		}else {
		    throw new TypeException( "Iterator type does not match with array in the For loop.");
		}
		
	    }   

	    // SExp.      Stmt ::= Expr  ";" 
	    public Boolean visit(Javalette.Absyn.SExp p, Env env)
	    { 
		TypeCode tc = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		
		if( tc != TypeCode.CVoid ){
		    throw new TypeException("Expression as statement need to be a void function!");
		}
		return false;
	    }
	}

	//////////////////////////////////////////////////////////////////////
	public class ItemVisitor implements Item.Visitor<Void,Env>
	{
	    public Void visit(Javalette.Absyn.NoInit p, Env env)
	    { 
      		env.extendVar( p.ident_, env.lastDeclarationType);
      		return null;
	    }    
	    public Void visit(Javalette.Absyn.Init p, Env env)
	    { 
		env.extendVar( p.ident_, env.lastDeclarationType);
		TypeCode tExpr = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		
		// check
		if(env.lastDeclarationType != tExpr) {
		    throw new TypeException( "The variable "+p.ident_+" of type "+ env.lastDeclarationType + "does not match with type of the expression" + tExpr);
		}
      		return null;
	    }
	}


	////////////////////////////////////////////////////////////////////////
	public class TypeVisitor implements Type.Visitor<TypeCode, Env>
	{
	    public TypeCode visit(Javalette.Absyn.Type_bool p, Env env)  { return TypeCode.CBool;  }
	    public TypeCode visit(Javalette.Absyn.Type_int p, Env env)   { return TypeCode.CInt;   }
	    public TypeCode visit(Javalette.Absyn.Type_double p, Env env){ return TypeCode.CDouble;}
	    public TypeCode visit(Javalette.Absyn.Type_void p, Env env)  { return TypeCode.CVoid;  }

	    public TypeCode visit(Javalette.Absyn.Type_array p, Env env)  { 

		TypeCode t = p.type_.<TypeCode,Env> accept(new TypeVisitor(), env);
		if(t == TypeCode.CInt ){
		    return TypeCode.CArrayInt;  
		}else if( t == TypeCode.CDouble ){
		    return TypeCode.CArrayDouble;  
		}else if( t == TypeCode.CBool ){
		    return TypeCode.CArrayBool;  
		}else{
		    throw new TypeException( "Arrays can be declared with types: int, double or boolean!");
		}
	    }
		
	   	    
	    public TypeCode visit(Javalette.Absyn.Fun p, Env env)  { return TypeCode.CVoid;  }
	}




	//////////////////////////////////////////////////////////////////////
	public class ExprVisitor implements Expr.Visitor<TypeCode, Env>
	{
	    public TypeCode visit(Javalette.Absyn.EVar p, Env env)
	    { 
		return env.lookupVar(p.ident_);
	    }   
	    public TypeCode visit(Javalette.Absyn.ELitInt p, Env env)
	    { 
		return TypeCode.CInt;
	    }    
	    public TypeCode visit(Javalette.Absyn.ELitDoub p, Env env)
	    {
		return TypeCode.CDouble;
	    }   
	    public TypeCode visit(Javalette.Absyn.ELitTrue p, Env env)
	    { 
		return TypeCode.CBool;
	    }    
	    public TypeCode visit(Javalette.Absyn.ELitFalse p, Env env)
	    { 
		return TypeCode.CBool;
	    }   
	    
	    /*EApp.      Expr6 ::= Ident "(" [Expr] ")" ; */
	    public TypeCode visit(Javalette.Absyn.EApp p, Env env)
	    { 
		FunType func = env.signature.get(p.ident_);
		env.lastSexpIdent = p.ident_;
		if( func.args.size() != p.listexpr_.size() ){
		    throw new TypeException("Function call: " + p.ident_ + " nr of args " + p.listexpr_.size() + " does not match " + func.args.size());
		}
		for (int i = 0 ; i < p.listexpr_.size(); i++ )
		    {
			TypeCode exprType = p.listexpr_.get(i).<TypeCode, Env> accept(new ExprVisitor(), env);
			if(exprType != func.args.get(i)) {
			    throw new TypeException("Function call: arg type "+ exprType + " is not equal to expected arg type of "+ func.args.get(i));
			}
		    }

		return func.val;
	    }   
	  
	    public TypeCode visit(Javalette.Absyn.EString p, Env env)
	    { 
		return  TypeCode.CString;
	    }   
	    // Neg.       Expr5 ::= "-" Expr6      
	    public TypeCode visit(Javalette.Absyn.Neg p, Env env)
	    { 
		TypeCode tc = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if(tc == TypeCode.CInt || tc == TypeCode.CDouble){
		    return tc;
		}else {
		    throw new TypeException(" \"-\" can only operate on Integer and Double");
		}
	    }    


	    // Not.       Expr5 ::= "!" Expr6
	    public TypeCode visit(Javalette.Absyn.Not p, Env env)
	    { 
		TypeCode tc = p.expr_.<TypeCode,Env> accept(new ExprVisitor(), env);
		if(tc == TypeCode.CBool){
		    return tc;
		}else {
		    throw new TypeException(" \"!\" can only operate on Boolean");
		}
	    }    

    
	    public TypeCode visit(Javalette.Absyn.EMul p, Env env)
	    { 
		TypeCode t1 = p.expr_1.<TypeCode, Env> accept(new ExprVisitor(), env);
		TypeCode t2 = p.expr_2.<TypeCode, Env> accept(new ExprVisitor(), env);
		Boolean isModOperator = p.mulop_.<Boolean, Env> accept(new MulOpVisitor(), env);  

		if (t1 == TypeCode.CInt && t2 == TypeCode.CInt)
		    return TypeCode.CInt;
		if (t1 == TypeCode.CDouble && t2 == TypeCode.CDouble){
		    if( !isModOperator )
			return TypeCode.CDouble ;
		    else  
			throw new TypeException("Expression e1%e2: Operands  must be CInt");
		}else
		    throw new TypeException("Expression e1 e2: Operands  must be of same type");

		
	    }        
	    public TypeCode visit(Javalette.Absyn.EAdd p, Env env)
	    { 
		TypeCode t1 = p.expr_1.<TypeCode, Env> accept(new ExprVisitor(), env);
		TypeCode t2 = p.expr_2.<TypeCode, Env> accept(new ExprVisitor(), env);

    		if (t1 == TypeCode.CInt && t2 == TypeCode.CInt)
		    return TypeCode.CInt;
		if (t1 == TypeCode.CDouble && t2 == TypeCode.CDouble)
		    return TypeCode.CDouble;
		else  
		    throw new TypeException("Expression e1 + e2: Operands must be CInt or CDouble");
	    }
	    public TypeCode visit(Javalette.Absyn.ERel p, Env env)
	    { 

		TypeCode t1 = p.expr_1.<TypeCode, Env> accept(new ExprVisitor(), env);
		Boolean worksForAllTypes = p.relop_.<Boolean, Env> accept(new RelOpVisitor(), env);
		TypeCode t2 = p.expr_2.<TypeCode, Env> accept(new ExprVisitor(), env);

		if (t1 == TypeCode.CInt && t2 == TypeCode.CInt)
		    return TypeCode.CBool;
		if (t1 == TypeCode.CDouble && t2 == TypeCode.CDouble)
		    return TypeCode.CBool ;
		
		if (t1 == TypeCode.CBool && t2 == TypeCode.CBool ){
		    if( worksForAllTypes )
			return TypeCode.CBool;
		    else
			throw new TypeException("Relation operation is not allowed on for variables of type CBool");

		}else  
		    throw new TypeException("Expression e1*e2: Operands  must be of the same type");
	    }        
	    public TypeCode visit(Javalette.Absyn.EAnd p, Env env)
	    { 
		TypeCode t1 = p.expr_1.<TypeCode, Env> accept(new ExprVisitor(), env);
		TypeCode t2 = p.expr_2.<TypeCode, Env> accept(new ExprVisitor(), env);

		if (t1 == TypeCode.CBool && t2 == TypeCode.CBool)
		    return TypeCode.CBool;
		else  
		    throw new TypeException("Expression e1 && e2: Operands must be CBool");
	    }        
	    public TypeCode visit(Javalette.Absyn.EOr p, Env env)
	    { 
		TypeCode t1 = p.expr_1.<TypeCode, Env> accept(new ExprVisitor(), env);
		TypeCode t2 = p.expr_2.<TypeCode, Env> accept(new ExprVisitor(), env);

		if (t1 == TypeCode.CBool && t2 == TypeCode.CBool)
		    return TypeCode.CBool;
		else  
		    throw new TypeException("Expression e1 || e2: Operands must be CBool");
		
	    } 

	    //EArrayNew. Expr6 ::= "new" Type "[" Expr "]"    
	    public TypeCode visit(Javalette.Absyn.EArrayNew p, Env env)
	    { 
		TypeCode t 	= p.type_.<TypeCode, Env> accept(new TypeVisitor(), env);
		TypeCode tExpr  = p.expr_.<TypeCode, Env> accept(new ExprVisitor(), env);

		if (tExpr != TypeCode.CInt)
		    throw new TypeException("Expression: \"new Type [x] ;\" , x must be of type int" );
	
		if(t == TypeCode.CInt ){
		    return TypeCode.CArrayInt;  
		}else if( t == TypeCode.CDouble ){
		    return TypeCode.CArrayDouble;  
		}else if( t == TypeCode.CBool ){
		    return TypeCode.CArrayBool;  
		}else{
		    throw new TypeException( "Arrays can be declared with types: int, double or boolean!");
		}		
	    } 

	    //EArrayLen. Expr6 ::= Ident ".length" ;
	    public TypeCode visit(Javalette.Absyn.EArrayLen p, Env env)
	    { 
		TypeCode tc = env.lookupVar(p.ident_);
		if(tc == TypeCode.CArrayInt || tc == TypeCode.CArrayDouble || tc == TypeCode.CArrayBool ){
		    return TypeCode.CInt;
		}else {
		    throw new TypeException( p.ident_ + " must be an array.");
		}
		 
	    }
	    //EArrayPtr. Expr6 ::= Ident "[" Expr "]" ;  
	    public TypeCode visit(Javalette.Absyn.EArrayPtr p, Env env)
	    { 
		TypeCode tExpr  = p.expr_.<TypeCode, Env> accept(new ExprVisitor(), env);
		if(tExpr != TypeCode.CInt){
		    throw new TypeException( "Expression: \""+ p.ident_ +"[x] \" , x must be of type int");			
		}
		TypeCode t = env.lookupVar(p.ident_);
		if(t == TypeCode.CArrayInt ){
		    return TypeCode.CInt;  
		}else if( t == TypeCode.CArrayDouble ){
		    return TypeCode.CDouble;  
		}else if( t == TypeCode.CArrayBool ){
		    return TypeCode.CBool;  
		}else{
		    throw new TypeException( "Expression: \""+ p.ident_ +"[x] \", " + p.ident_ +" is not an array.");
		}	
	    }   
     
	}
	//////////////////////////////////////////////////////////////////////
	public class AddOpVisitor implements AddOp.Visitor<Void,Env>
	{
	    public Void visit(Javalette.Absyn.Plus p, Env env)
	    { 
		return null;
	    }    public Void visit(Javalette.Absyn.Minus p, Env env)
	    { 
		return null;
	    }
	}
	//////////////////////////////////////////////////////////////////////
	public class MulOpVisitor implements MulOp.Visitor<Boolean,Env>
	{
	    // returns false when operator is not mod
	    // return true when operator is mod
	    public Boolean visit(Javalette.Absyn.Times p, Env env)
	    { 
		return false;
	    }    public Boolean visit(Javalette.Absyn.Div p, Env env)
	    { 
		return false;
	    }    public Boolean visit(Javalette.Absyn.Mod p, Env env)
	    { 
		return true;
	    }
	}
	//////////////////////////////////////////////////////////////////////
	public class RelOpVisitor implements RelOp.Visitor<Boolean,Env>
	{
	    // returns false when operator only works for integers and double
	    // return true when operator works for integers, doubles and boolean
	    public Boolean visit(Javalette.Absyn.LTH p, Env env)
	    { 
		return false;
	    }    public Boolean visit(Javalette.Absyn.LE p, Env env)
	    { 
		return false;
	    }    public Boolean visit(Javalette.Absyn.GTH p, Env env)
	    { 
		return false;
	    }    public Boolean visit(Javalette.Absyn.GE p, Env env)
	    { 
		return false;
	    }    public Boolean visit(Javalette.Absyn.EQU p, Env env)
	    { 
		return true;
	    }    public Boolean visit(Javalette.Absyn.NE p, Env env)
	    { 
		return true;
	    }
	}
    }
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    // The environment class.
    public static class Env {
	public static HashMap<String, FunType> signature ;
	public static LinkedList<HashMap<String, TypeCode>> contexts ;
	public static TypeCode lastFuncType;
	public static TypeCode lastDeclarationType;
	public static String lastFuncId;
	public static String lastSexpIdent;
	public static Boolean returnExists;

	//////////////////////////////////////////////////////////////////////
	public Env(){
	    signature = new HashMap<String, FunType>();
	    contexts = new LinkedList<HashMap<String, TypeCode>>();
	    lastFuncId = "";

	    
	}

	//////////////////////////////////////////////////////////////////////
	public static void extendVar (String id, TypeCode ty) {

	    if(ty == TypeCode.CVoid){
		throw new TypeException("Variable: "+id+" has type CVoid, not allowed!");
	    }
	    // Check if variable already exists
	    else if(contexts.getLast().containsKey(id)){
		throw new TypeException("Variable: "+id+" of already exists!");
	    }else{
		contexts.getLast().put(id, ty);
	    }
	}
    
	//////////////////////////////////////////////////////////////////////
	public static TypeCode lookupVar(String id) {
	    for(int i= contexts.size() - 1; i >= 0 ; i--) {
		// Check if variable has a type
		if(contexts.get(i).get(id) != null) {
		    return contexts.get(i).get(id);
		}
	    }
	    throw new TypeException("Variable: "+id+" has no type, not declared!");
	} 
    
	//////////////////////////////////////////////////////////////////////
	public static void extendFun (String id, FunType fty) {
	    if(signature.containsKey(id)){
		throw new TypeException("Function: "+id+" already exists!");
	    }else{
		signature.put(id, fty);
	    }
	}

	//////////////////////////////////////////////////////////////////////
	public static FunType lookupFun(String id) { return signature.get(id); }
    
	//////////////////////////////////////////////////////////////////////
	public static void newBlock() { contexts.add(new HashMap<String, TypeCode>()); }
    
	//////////////////////////////////////////////////////////////////////
	public static void removeLastBlock() { contexts.removeLast(); }

	//////////////////////////////////////////////////////////////////////
	public static void printSignature(){
	    System.out.println("--------Printing env.signature--------");
	    for (String key : signature.keySet()) {
		System.out.println(signature.get(key).val + " " + key + " " + signature.get(key).args);
	    }
	}

	//////////////////////////////////////////////////////////////////////
	public static void printContexts(){

	    System.out.println("--------Printing env.contexts----------");
	    System.out.println(contexts);
	}
    }


    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    // Help class for building the environment signature
    public class BuildSignatureVisit
    {
  	////////////////////////////////////////////////////////////////////////
	public class TypeVisitor implements Type.Visitor<TypeCode, Env>
	{
	    public TypeCode visit(Javalette.Absyn.Type_bool p, Env arg) { return TypeCode.CBool;  }
	    public TypeCode visit(Javalette.Absyn.Type_int p, Env arg)  { return TypeCode.CInt;   }
	    public TypeCode visit(Javalette.Absyn.Type_double p, Env arg){return TypeCode.CDouble;}
	    public TypeCode visit(Javalette.Absyn.Type_void p, Env arg) { return TypeCode.CVoid;  }
	    public TypeCode visit(Javalette.Absyn.Type_array p, Env env)  { 

		TypeCode t = p.type_.<TypeCode,Env> accept(new TypeVisitor(), env);
		if(t == TypeCode.CInt ){
		    return TypeCode.CArrayInt;  
		}else if( t == TypeCode.CDouble ){
		    return TypeCode.CArrayDouble;  
		}else if( t == TypeCode.CBool ){
		    return TypeCode.CArrayBool;  
		}else{
		    throw new TypeException( "Arrays can be declared with types: int, double or boolean!");
		}
	    }
		
	  	    
	    public TypeCode visit(Javalette.Absyn.Fun p, Env env)  { return TypeCode.CVoid;  }
	}
	
	////////////////////////////////////////////////////////////////////////
	public class ProgVisitor implements Prog.Visitor<Void,Env>
	{
	    public Void visit(Javalette.Absyn.Program p, Env env)
	    {
		for (TopDef x: p.listtopdef_){ 
		    x.<Void, Env> accept(new TopDefVisitor(), env);
		}
		return null;
	    }
	}

	////////////////////////////////////////////////////////////////////////
	public class TopDefVisitor implements TopDef.Visitor<Void,Env>
	{
	    public Void visit(Javalette.Absyn.FnDef func, Env env)
	    { 
		FunType funtype = new FunType();
		TypeCode tc = func.type_.<TypeCode, Env> accept(new TypeVisitor(), env);
		funtype.setVal ( tc );
		for (Arg arg: func.listarg_)
		    {
			funtype.addArg( arg.<TypeCode, Env> accept(new ArgVisitor(), env) );
		    }
	    
		env.extendFun ( func.ident_, funtype );
		return null;
	    }
	}

	////////////////////////////////////////////////////////////////////////
	public class ArgVisitor implements Arg.Visitor<TypeCode, Env>
	{
	    public TypeCode visit(Javalette.Absyn.Argument p, Env env)
	    {
		return p.type_.<TypeCode, Env> accept(new TypeVisitor(), env);
	    }
	}
	////////////////////////////////////////////////////////////////////////
    }
}
