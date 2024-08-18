import Javalette.*;
import Javalette.Absyn.*;
import java.util.*;
import java.io.PrintWriter;


public class LLVMGenerator{
    
    boolean DEBUG = true;
    
    ////////////////////////////////////////////////////////////////////////
    // The output of the compiler is a list of strings.
    LinkedList<String> output;
    
    ////////////////////////////////////////////////////////////////////////
    // The LLVM code holding global strings.
    LinkedList<String> globals;
    
    ////////////////////////////////////////////////////////////////////////
    // Mapping of global variable strings to IDs
    Map<Integer, GlobalEntry> globSig;
    
    ////////////////////////////////////////////////////////////////////////
    // Signature mapping function names to their LLVM name and type
    Map<String, FunEntry> funSig;
    
    ////////////////////////////////////////////////////////////////////////
    // Context mapping variable identifiers to their type.
    LinkedList<Map<String,CxtEntry>> cxt;
    
    ////////////////////////////////////////////////////////////////////////
    // Next free address for local variable;
    int registerID = 0;
    
    ////////////////////////////////////////////////////////////////////////
    // Next free address for global variable;
    int globalID = 0;
    
    ////////////////////////////////////////////////////////////////////////
    // Global counter to get next label;
    int labelID = 0;
    
    ////////////////////////////////////////////////////////////////////////
    // AssArray.  Stmt ::= Expr "=" Expr ";"  Checks if it is the leftside Expr.
    Boolean isLeftSideExpr = false;
    
    ////////////////////////////////////////////////////////////////////////
    String outputFile = "";
    
    ////////////////////////////////////////////////////////////////////////
    String ifThen = "if.then";
    String ifElse = "if.else";
    String ifEnd = "if.end";
    String whileLoop = "while";
    String whileIfThen = "while.ifThen";
    String whileEnd = "while.end";
    String forLoop = "for";
    String forIfThen = "for.ifThen";
    String forIfEnd = "for.end";
    
    ////////////////////////////////////////////////////////////////////////
    // Variable information
    public class CxtEntry {
        public TypeCode  type;
        public Integer addr;
        public int length;
        CxtEntry (TypeCode t, Integer a) {
            type   = t;
            addr   = a;
            length = 0;
            
        }
        
        CxtEntry (TypeCode type, Integer addr, int length) {
            this.type   = type;
            this.addr   = addr;
            this.length = length;
            
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    // Function signature
    public class FunEntry{
        final String name;
        final TypeCode type;
        public LinkedList <ArgEntry> argList;
        FunEntry (String name, TypeCode type){
            this.name = name;
            this.type = type;
            argList = new LinkedList<ArgEntry>();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    // Function argument information
    public class ArgEntry{
        final String ident;
        final TypeCode type;
        ArgEntry(String ident, TypeCode type){
            this.ident  = ident;
            this.type   = type;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    // Global variabl information
    public class GlobalEntry {
        final String  str;
        // Javalette string length, not the llvm generated length
        final Integer length;
        GlobalEntry (String s, Integer l) {
            str = s;
            length = l;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    // Share type constants
    public final Type BOOL   = new Type_bool  ();
    public final Type INT    = new Type_int   ();
    public final Type DOUBLE = new Type_double();
    public final Type VOID   = new Type_void();
    
    ////////////////////////////////////////////////////////////////////////
    int nextRegister(){
        return registerID++;
    }
    
    ////////////////////////////////////////////////////////////////////////
    int nextLabel(){
        return labelID++;
    }
    
    ////////////////////////////////////////////////////////////////////////
    int nextGlobal(){
        return globalID++;
    }
    
    ////////////////////////////////////////////////////////////////////////
    void newVar(String varID, TypeCode type, int length) {
        if( cxt != null )
            cxt.getLast().put(varID, new CxtEntry(type, nextRegister(), length));
        else
            throw new RuntimeException ("newVar(...): Context is not initiated (=null)");
    }
    
    ////////////////////////////////////////////////////////////////////////
    void updateVar (String x, CxtEntry ce) {
        for(int i=cxt.size()-1; i>=0; i-- ){
            CxtEntry varReg = cxt.get(i).get(x);
            if(  varReg != null ){
                cxt.get(i).put(x, new CxtEntry(ce.type, ce.addr, ce.length));
                return;
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    CxtEntry lookupVar (String x) {
        for(int i=cxt.size()-1; i>=0; i-- ){
            if( cxt.get(i).get(x) != null ){
                return cxt.get(i).get(x);
            }
        }
        throw new RuntimeException ("Impossible: unbound variable " + x);
    }
    
    ////////////////////////////////////////////////////////////////////////
    void emit (Code c) {
        String line = c.accept(new CodeToLLVM());
        if (!line.isEmpty()) output.add(line);
    }
    
    ////////////////////////////////////////////////////////////////////////
    TypeCode baseType( TypeCode arrayTC ){
        TypeCode tc = null;
        if( arrayTC == TypeCode.CArrayInt ){
            tc = TypeCode.CInt;
        }else if( arrayTC == TypeCode.CArrayDouble ){
            tc = TypeCode.CDouble;
        }else if( arrayTC == TypeCode.CArrayBool ){
            tc = TypeCode.CBool;
        }
        return tc;
    }
    
    ////////////////////////////////////////////////////////////////////////
    boolean isArrayType( TypeCode tc ){
        return    (tc == TypeCode.CArrayInt 
                || tc == TypeCode.CArrayDouble 
                || tc == TypeCode.CArrayBool );
    }
    
    ////////////////////////////////////////////////////////////////////////
    String typeCodeToString( TypeCode t ){
        String s="";
        if( t == TypeCode.CInt ){
            s = new String ("i32");
        }else if( t == TypeCode.CDouble ){
            s = new String("double");
        }else if( t == TypeCode.CBool ){
            s = new String("i1");
        }else if( t == TypeCode.CVoid ){
            s = new String("void");
        }else if(t == TypeCode.CArrayInt){
            s = new String("{i32, [0 x i32]}");
        }else if(t == TypeCode.CArrayDouble){
            s = new String("{i32, [0 x double]}");
        }else if(t == TypeCode.CArrayBool){
            s = new String("{i32, [0 x i1]}");
        }
        return s;
    }
    ////////////////////////////////////////////////////////////////////////
    public Type typeCodeToType(TypeCode tc){
        Type t;
        if( tc == TypeCode.CInt ){
            t = new Type_int();
        }else if( tc == TypeCode.CDouble ){
            t = new Type_double();
        }else if( tc == TypeCode.CBool ){
            t = new Type_bool();
        }else if( tc == TypeCode.CVoid ){
            t = new Type_void();
        }else{
            t = new Type_void();
        }
        return t;
    }
    
    ////////////////////////////////////////////////////////////////////////
    public void printToFile(String fileName){
        try{
            PrintWriter writer = new PrintWriter(fileName + ".ll", "UTF-8");
            
            for( int i = 0; i < globals.size(); i++ ){
                output.addFirst(globals.get(i));
            }
            for( int i = 0; i < output.size(); i++ ){
                writer.print(output.get(i));
            }
            
            writer.close();
        }
        catch(Exception e)
        {
            System.err.println("ERROR");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    public void loadBuildInFunctions(){
        output.add("\n\ndeclare void @printInt(i32 %n)\n");
        output.add("declare void @printString(i8* %s)\n");
        output.add("declare void @printDouble(double %x)\n");
        output.add("declare i32 @readInt()\n");
        output.add("declare double @readDouble()\n");
        output.add("declare i8* @calloc(i32, i32)\n\n\n");
    }
    
    ////////////////////////////////////////////////////////////////////////
    public void generate(String fileName, Javalette.Absyn.Prog p){
        // Initialize output
        globals = new LinkedList();
        output = new LinkedList();
        globSig = new HashMap<Integer, GlobalEntry>();
        
        loadBuildInFunctions();
        
        p.accept(new ProgVisitor(), null);
        
        printToFile(fileName);
        
    }
    
    ////////////////////////////////////////////////////////////////////////
    public class ProgVisitor implements Prog.Visitor<Void,Void>
    {
        public Void visit(Javalette.Absyn.Program p, Void arg)
        {
            // Build signature
            // User-defined functions
            funSig = new TreeMap();
            
            FunEntry printInt = new FunEntry("printInt", TypeCode.CVoid);
            printInt.argList.add(new ArgEntry("atakan1", TypeCode.CInt));
            funSig.put("printInt", printInt);
            
            
            FunEntry printDouble = new FunEntry("printDouble", TypeCode.CVoid);
            printDouble.argList.add(new ArgEntry("atakan2", TypeCode.CDouble));
            funSig.put("printDouble", printDouble);
            
            FunEntry printString = new FunEntry("printString", TypeCode.CVoid);
            printString.argList.add(new ArgEntry("atakan3", TypeCode.CString));
            funSig.put("printString", printString);
            
            funSig.put("readInt", new FunEntry("readInt", TypeCode.CInt));
            funSig.put("readDouble", new FunEntry("readDouble", TypeCode.CDouble));
            
            for (TopDef func: p.listtopdef_){
                TypeCode fTc = ((FnDef)func).type_.<TypeCode, Void> accept(new TypeVisitor(), null);
                String name = ((FnDef)func).ident_ ;
                FunEntry funEntry = new FunEntry(name, fTc );
                for (Arg a: ((FnDef)func).listarg_){
                    ArgEntry argEntry = a.<ArgEntry, Void> accept(new ArgVisitor(), null);
                    funEntry.argList.add(argEntry);
                }
                funSig.put( name, funEntry );
                
            }
            
            // Generate code for the functions
            for (TopDef func: p.listtopdef_){
                func.accept(new TopDefVisitor(), null);
            }
            
            return null;
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////
    public class TopDefVisitor implements TopDef.Visitor<Void,Void>
    {
        public Void visit(Javalette.Absyn.FnDef func, Void a)
        {
            cxt = new LinkedList();
            cxt.add(new TreeMap());
            
            String tmp = new String();
            
            // Compile function
            for (Arg arg: func.listarg_)
            {
                ArgEntry argEntry = arg.<ArgEntry, Void> accept(new ArgVisitor(), null);
                CxtEntry cxtE = cxt.getLast().get(((Argument)arg).ident_);
                if( isArrayType(cxtE.type) ){
                    tmp  += typeCodeToString(cxtE.type) + "** %t" + cxtE.addr + " , " ;
                }else{
                    tmp  += typeCodeToString(cxtE.type) + " %t" + cxtE.addr + " , " ;
                }
            }
            if(tmp.length() > 2 )
                tmp = tmp.substring(0,tmp.length()-3);
            FunEntry funEntry = funSig.get(func.ident_);
            String funcType = typeCodeToString(funEntry.type);
            if(isArrayType(funEntry.type))
                funcType+="**";
            String fnDef = "define " + funcType + " @" + func.ident_ + "( " + tmp + " ){\nentry:\n";
            output.add(fnDef);
            
            for (Arg arg: func.listarg_)
            {
                arg.<Void, Void> accept(new ArgVisitor2(), null);
            }
            
            func.blk_.<Void, Void> accept(new BlkVisitor(), null);
            
            if(funEntry.type == TypeCode.CVoid )
                emit (new Return ( TypeCode.CVoid, -1) );
            output.add("unreachable\n");
            output.add("}\n");
            return null;
        }
    }
    
    //////////////////////////////////////////////////////////////////////
    public class ArgVisitor2 implements Arg.Visitor<Void, Void>
    {
        public Void visit(Javalette.Absyn.Argument arg, Void v)
        {
            cxt.add(new TreeMap());
            
            TypeCode tc = arg.type_.<TypeCode, Void> accept(new TypeVisitor(), null );
            CxtEntry ce = lookupVar(arg.ident_);
            
            if( !isArrayType(ce.type) ){
                int tmpPtr = nextRegister();
                emit(new Alloca( tmpPtr, tc ) );
                emit(new Store( tc, ce.addr , tmpPtr ) );
                updateVar( arg.ident_, new CxtEntry( tc, tmpPtr ) );
            }
            cxt.removeLast();
            return null;
        }
        
    }
    
    //////////////////////////////////////////////////////////////////////
    public class ArgVisitor implements Arg.Visitor<ArgEntry, Void>
    {
        public ArgEntry visit(Javalette.Absyn.Argument arg, Void v)
        {
            
            TypeCode tc = arg.type_.<TypeCode, Void> accept(new TypeVisitor(), null );
            if( cxt != null )
                newVar ( arg.ident_, tc, 0);
            return new ArgEntry(arg.ident_, tc);
        }
        
    }
    
    //////////////////////////////////////////////////////////////////////
    // Block.     Blk ::= "{" [Stmt] "}" ;
    public class BlkVisitor implements Blk.Visitor<Void,Void>
    {
        public Void visit(Javalette.Absyn.Block blk, Void v)
        {
            cxt.add(new TreeMap());
            for (Stmt stmt: blk.liststmt_){
                stmt.<Void, Void> accept(new StmtVisitor(), null);
                
            }
            cxt.removeLast();
            return null;
        }
    }
    
    //////////////////////////////////////////////////////////////////////
    public class StmtVisitor implements Stmt.Visitor<Void, Void>
    {
        
        // Empty.     Stmt ::= ";" ;
        public Void visit(Javalette.Absyn.Empty eStmt, Void env)
        { return null; }
        
        // BStmt.     Stmt ::= Blk ;
        public Void visit(Javalette.Absyn.BStmt bStmt, Void env)
        {
            bStmt.blk_.<Void, Void> accept(new BlkVisitor(), env);
            return null;
        }
        
        // Decl.      Stmt ::= Type [Item] ";" ;
        public Void visit(Javalette.Absyn.Decl p, Void env)
        {
            TypeCode tc = p.type_.<TypeCode,Void> accept(new TypeVisitor(), null);
            for (Item item: p.listitem_){
                item.<Void, TypeCode> accept(new ItemVisitor(), tc);
            }
            return null;
        }
        
        //Ass.       Stmt ::= Ident "=" Expr  ";"
        public Void visit(Javalette.Absyn.Ass p, Void env)
        {
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept(new ExprVisitor(), env);
            CxtEntry identPtr = lookupVar(p.ident_);
            
            if( isArrayType(identPtr.type)){
                if( p.expr_ instanceof Javalette.Absyn.EArrayNew ){
                    
                    // Initiate the structure with the length
                    int totalSize = nextRegister();
                    int totalSizeInBytes = nextRegister();
                    int oneReg = nextRegister();
                    int heapPtr = nextRegister();
                    int structure = nextRegister();
                    int lengthPtr = nextRegister();
                    
                    if( identPtr.type == TypeCode.CArrayInt){
                        int fourReg = nextRegister();
                        emit (new AddZero(TypeCode.CInt, 4, fourReg ));
                        emit (new AddZero(TypeCode.CInt, 1, oneReg ));
                        emit (new Add(TypeCode.CInt, ce.addr, oneReg, totalSize));
                        emit (new Mul(TypeCode.CInt, totalSize, fourReg, totalSizeInBytes));
                    }
                    else if(identPtr.type == TypeCode.CArrayDouble){
                        int eightReg = nextRegister();
                        int fourReg = nextRegister();
                        int tmp = nextRegister();
                        emit (new AddZero(TypeCode.CInt, 8, eightReg ));
                        emit (new AddZero(TypeCode.CInt, 4, fourReg ));
                        emit (new Mul(TypeCode.CInt, ce.addr, eightReg, tmp));
                        emit (new Add(TypeCode.CInt, tmp, fourReg, totalSizeInBytes));
                    }
                    else if(identPtr.type == TypeCode.CArrayBool){
                        int fourReg = nextRegister();
                        emit (new AddZero(TypeCode.CInt, 4, fourReg ));
                        emit (new Add(TypeCode.CInt, ce.addr, fourReg, totalSizeInBytes));
                    }
                    
                    emit (new Calloc(heapPtr, totalSizeInBytes, identPtr.type));
                    emit (new BitCast( structure, heapPtr, identPtr.type) );
                    emit (new GetElementPtr( identPtr.type, lengthPtr, structure, 0 ) );
                    emit (new Store(TypeCode.CInt, ce.addr, lengthPtr));
                    emit (new Store(identPtr.type, structure, identPtr.addr ));
                }
                else if(p.expr_ instanceof Javalette.Absyn.EVar){
                    int tmpRegister = nextRegister();
                    emit (new Load(identPtr.type, tmpRegister, ce.addr));
                    emit (new Store(identPtr.type, tmpRegister, identPtr.addr ));
                }else if(p.expr_ instanceof Javalette.Absyn.EApp){
                    int tmpRegister = nextRegister();
                    emit (new Load(identPtr.type, tmpRegister, ce.addr));
                    emit (new Store(identPtr.type, tmpRegister, identPtr.addr ));
                }
            }else{
                emit (new Store(identPtr.type, ce.addr, identPtr.addr));
            }
            
            return null;
        }
        
        //AssArray.  Stmt ::= Expr "=" Expr ";" ;
        public Void visit(Javalette.Absyn.AssArray p, Void env)
        {
            isLeftSideExpr = true;
            CxtEntry exprLeft = p.expr_1.<CxtEntry, Void> accept(new ExprVisitor(), env);
            isLeftSideExpr = false;
            CxtEntry exprRight = p.expr_2.<CxtEntry, Void> accept(new ExprVisitor(), env);
            
            emit( new Store( exprRight.type, exprRight.addr, exprLeft.addr) ) ;
            return null;
        }
        
        // Incr.      Stmt ::= Ident "++"  ";"
        public Void visit(Javalette.Absyn.Incr p, Void env)
        {
            int tmpReg1 = nextRegister();
            CxtEntry ce = lookupVar(p.ident_);
            emit(new Load(ce.type, tmpReg1, ce.addr));
            
            int tmpReg2 = nextRegister();
            if( ce.type == TypeCode.CInt )
                emit(new AddZero(ce.type, 1, tmpReg2) );
            else
                emit(new AddZero(ce.type, 1.0, tmpReg2) );
            
            
            int sum = nextRegister();
            emit(new Add(ce.type, tmpReg1, tmpReg2, sum) );
            emit(new Store(ce.type, sum, ce.addr ) );
            return null;
        }
        
        // Decr.      Stmt ::= Ident "--"  ";"
        public Void visit(Javalette.Absyn.Decr p, Void env)
        {
            int tmpReg1 = nextRegister();
            CxtEntry ce = lookupVar(p.ident_);
            emit(new Load(ce.type, tmpReg1, ce.addr));
            
            int tmpReg2 = nextRegister();
            if( ce.type == TypeCode.CInt )
                emit(new AddZero(ce.type, 1, tmpReg2) );
            else
                emit(new AddZero(ce.type, 1.0, tmpReg2) );
            
            
            int sum = nextRegister();
            emit(new Sub(ce.type, tmpReg1, tmpReg2, sum) );
            emit(new Store(ce.type, sum, ce.addr ) );
            return null;
        }
        
        // Ret.       Stmt ::= "return" Expr ";"
        public Void visit(Javalette.Absyn.Ret p, Void env)
        {
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept (new ExprVisitor(), null);
            emit (new Return ( ce.type, ce.addr) );
            
            return null;
        }
        
        // VRet.      Stmt ::= "return" ";"
        public Void visit(Javalette.Absyn.VRet p, Void env)
        {
            emit (new Return ( TypeCode.CVoid, -1) );
            return null;
        }
        
        // Cond.      Stmt ::= "if" "(" Expr ")" Stmt
        public Void visit(Javalette.Absyn.Cond p, Void env)
        {
            
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept (new ExprVisitor(), null);
            
            int lblID = nextLabel();
            String labelIfThen = ifThen + lblID;
            String labelIfEnd = ifEnd + lblID;
            
            String branch = "\n\nbr i1 %t" + ce.addr + ", label %" + labelIfThen + ", label %" + labelIfEnd + "\n\n";
            output.add(branch);
            output.add(labelIfThen + ":\n");
            
            
            cxt.add(new TreeMap());
            p.stmt_.<Void, Void> accept(new StmtVisitor(), null);
            cxt.removeLast();
            
            
            output.add("br label %" + labelIfEnd + "\n");
            output.add(labelIfEnd + ":\n\n");
            return null;
            
        }
        // CondElse.  Stmt ::= "if" "(" Expr ")" Stmt "else" Stmt
        public Void visit(Javalette.Absyn.CondElse p, Void env)
        {
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept (new ExprVisitor(), null);
            
            int lblID = nextLabel();
            
            String labelIfThen = ifThen + lblID;
            String labelIfElse = ifElse + lblID;
            String labelIfEnd = ifEnd + lblID;
            String branch = "\n\nbr i1 %t" + ce.addr + ", label %" + labelIfThen + ", label %" + labelIfElse + "\n\n";
            output.add(branch);
            
            // If
            output.add(labelIfThen + ":\n");
            cxt.add(new TreeMap());
            p.stmt_1.<Void, Void> accept(new StmtVisitor(), null);
            cxt.removeLast();
            output.add("br label %" + labelIfEnd + "\n");
            
            // Else
            output.add(labelIfElse + ":\n");
            cxt.add(new TreeMap());
            p.stmt_2.<Void, Void> accept(new StmtVisitor(), null);
            cxt.removeLast();
            output.add("br label %" + labelIfEnd + "\n");
            
            
            output.add(labelIfEnd + ":\n\n");
            return null;
            
            
        }
        // While.     Stmt ::= "while" "(" Expr ")" Stmt
        public Void visit(Javalette.Absyn.While p, Void env)
        {
            int lblID = nextLabel();
            
            String whiLoop   = whileLoop + lblID;
            String whiIfThen = whileIfThen + lblID;
            String whiIfEnd  = whileEnd + lblID;
            
            output.add("\n\nbr label %" + whiLoop + "\n");
            output.add(whiLoop + ":\n\n");
            
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept (new ExprVisitor(), null);
            
            output.add( "\n\nbr i1 %t" + ce.addr + ", label %" + whiIfThen + ", label %" + whiIfEnd + "\n\n" );
            output.add(whiIfThen + ":\n");
            
            
            cxt.add(new TreeMap());
            p.stmt_.<Void, Void> accept(new StmtVisitor(), null);
            cxt.removeLast();
            output.add("br label %" + whiLoop + "\n");
            
            output.add(whiIfEnd + ":\n\n");
            return null;
            
        }
        
        // For.	   Stmt ::= "for" "(" Type Ident ":" Expr ")" Stmt
        public Void visit(Javalette.Absyn.For p, Void env)
        {
            int lblID = nextLabel();
            
            String forL   = forLoop + lblID;
            String forThen = forIfThen + lblID;
            String forEnd  = forIfEnd + lblID;
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept (new ExprVisitor(), null);
            if(ce != null){
                
                TypeCode tc = baseType(ce.type);
                int structure = nextRegister();
                emit (new Load(ce.type, structure, ce.addr));
                
                // Create temporary register holding zero and one
                int zeroReg = nextRegister();
                emit(new AddZero(TypeCode.CInt, 0, zeroReg));
                int oneReg = nextRegister();
                emit(new AddZero(TypeCode.CInt, 1, oneReg));
                
                // Create a new LLVM name for index variable
                newVar (p.ident_, TypeCode.CInt, 0);
                int iPtr = (lookupVar(p.ident_)).addr;
                emit (new Alloca(iPtr, TypeCode.CInt));
                emit (new Store( TypeCode.CInt, zeroReg, iPtr ) );
                
                // Register holding the length of the array
                int lengthPtr = nextRegister();
                int lengthReg = nextRegister();
                emit (new GetElementPtr( ce.type, lengthPtr, structure, 0 ) );
                emit (new Load(TypeCode.CInt, lengthReg, lengthPtr) );
                
                // Start of the loop
                output.add("\n\nbr label %" + forL + "\n");
                output.add(forL + ":\n\n");
                
                int iReg = nextRegister();
                emit (new Load( TypeCode.CInt, iReg, iPtr ) );
                
                int resultReg = nextRegister();
                emit(new CmpLT(TypeCode.CInt, iReg, lengthReg, resultReg));
                
                // If i < length jump to forThen label or else exit for loop by jumping to label forEnd
                output.add( "\n\nbr i1 %t" + resultReg + ", label %" + forThen + ", label %" + forEnd + "\n\n" );
                
                
                // Start of block
                output.add(forThen + ":\n\n");
                cxt.add(new TreeMap());
                newVar (p.ident_, tc, 0);
                CxtEntry ident = lookupVar(p.ident_);
                emit (new Alloca(ident.addr, ident.type));
                
                int elementPtr = nextRegister();
                int element = nextRegister();
                emit (new GetElementFromArray( ce.type, elementPtr, structure, iReg ) );
                emit (new Load(ident.type, element, elementPtr));
                emit (new Store(ident.type, element, ident.addr));
                
                p.stmt_.<Void, Void> accept(new StmtVisitor(), null);
                cxt.removeLast();
                
                int iRegNext = nextRegister();
                emit ( new Add ( TypeCode.CInt, iReg, oneReg, iRegNext ) );
                emit (new Store( TypeCode.CInt, iRegNext, iPtr ) );
                
                output.add("br label %" + forL + "\n");
                output.add(forEnd + ":\n\n");
            }
            return null;
        }
        
        // SExp.      Stmt ::= Expr  ";"
        public Void visit(Javalette.Absyn.SExp p, Void env)
        {
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept(new ExprVisitor(), env);
            return null;
        }
        
    }
    
    public class ExprVisitor implements Expr.Visitor<CxtEntry, Void>
    {
        public CxtEntry visit(Javalette.Absyn.EVar p, Void env)
        {
            CxtEntry ce = lookupVar(p.ident_);
            int tmpReg = nextRegister();
            if( isArrayType(ce.type) )
                return ce;
            else{
                emit( new Load(ce.type, tmpReg, ce.addr ) );
                return new CxtEntry( ce.type, tmpReg);
            }
            
        }
        public CxtEntry visit(Javalette.Absyn.ELitInt p, Void env)
        {
            int valReg = nextRegister();
            emit(new AddZero(TypeCode.CInt, p.integer_, valReg));
            return new CxtEntry( TypeCode.CInt, valReg, p.integer_);
        }
        public CxtEntry visit(Javalette.Absyn.ELitDoub p, Void env)
        {
            int valReg = nextRegister();
            emit(new AddZero(TypeCode.CDouble, p.double_, valReg));
            return new CxtEntry( TypeCode.CDouble, valReg);
            
        }
        public CxtEntry visit(Javalette.Absyn.ELitTrue p, Void env)
        {
            int valReg = nextRegister();
            emit(new AddZero(TypeCode.CBool, 1, valReg));
            return new CxtEntry( TypeCode.CBool, valReg);
        }
        public CxtEntry visit(Javalette.Absyn.ELitFalse p, Void env)
        {
            int valReg = nextRegister();
            emit(new AddZero(TypeCode.CBool, 0, valReg));
            return new CxtEntry( TypeCode.CBool, valReg);
        }
        
        //EApp.      Expr6 ::= Ident "(" [Expr] ")" ;
        public CxtEntry visit(Javalette.Absyn.EApp p, Void env)
        {
            FunEntry funEntry = funSig.get(p.ident_);
            TypeCode fTc = funEntry.type;
            String res = "";
            int returnReg = -1;
            
            // If the return type of the function is "void", we omitt a result register
            if( fTc == TypeCode.CVoid ){
                
                // Special case if the function we are calling "is printString"
                if( p.ident_.equals("printString") ){
                    CxtEntry ce = p.listexpr_.get(0).<CxtEntry, Void> accept(new ExprVisitor(), env);
                    GlobalEntry ge = globSig.get(ce.addr);
                    int strReg = nextRegister();
                    String getElementPtr = "%s" + strReg + " = getelementptr [" + ge.length + " x i8], [" + ge.length + " x i8]* @g" + ce.addr + ", i32 0, i32 0\n";
                    output.add( getElementPtr );
                    String fCall = "call void @printString( i8* %s" + strReg + ")\n";
                    output.add(fCall);
                    return null;
                }
                
            }else{
                returnReg = nextRegister();
                res += "%t"+returnReg+" = ";
            }
            String retType = typeCodeToString(fTc);
            if(isArrayType(fTc))
                retType+="**";
            String fCall = res + "call " + retType + " @" + p.ident_ + "( ";
            String arguments = "";
            String comma = " , ";
            int listSize = p.listexpr_.size();
            
            
            for (int i = 0 ; i < listSize; i++ )
            {
                CxtEntry tmpPtr = p.listexpr_.get(i).<CxtEntry, Void> accept(new ExprVisitor(), env);
                
                if(isArrayType(tmpPtr.type)){
                    arguments += typeCodeToString(tmpPtr.type) + "**" + " %t" + tmpPtr.addr;
                }else{
                    arguments += typeCodeToString(tmpPtr.type) + " %t" + tmpPtr.addr;
                }
                
                if( (listSize - i )> 1 )
                    arguments += comma;
            }
            
            fCall += arguments;
            output.add(fCall + " )\n");
            
            if( returnReg != -1 )
                return new CxtEntry( fTc, returnReg);
            else
                return null;
        }
        
        // EString.   Expr6 ::= String ;
        public CxtEntry visit(Javalette.Absyn.EString p, Void env)
        {
            
            int globID = nextGlobal();
            int length = p.string_.length() + 1;
            
            String tmpString = "@g" + globID + " = global [" + length + " x i8] c\"" + p.string_ + "\\00\"\n";
            globals.add(tmpString);
            globSig.put(globID, new GlobalEntry(tmpString, length));
            return  (new CxtEntry( TypeCode.CInt, globID ) );
        }
        // Neg.       Expr5 ::= "-" Expr6
        public CxtEntry visit(Javalette.Absyn.Neg p, Void env)
        {
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept(new ExprVisitor(), env);
            int tmpReg = nextRegister();
            if( ce.type == TypeCode.CInt )
                emit(new AddZero(ce.type, 0, tmpReg) );
            else
                emit(new AddZero(ce.type, 0.0, tmpReg) );
            
            int sub = nextRegister();
            
            emit(new Sub(ce.type, tmpReg, ce.addr, sub) );
            
            return new CxtEntry( ce.type, sub );
            
        }
        
        // Not.       Expr5 ::= "!" Expr6 ;
        public CxtEntry visit(Javalette.Absyn.Not p, Void env)
        {
            CxtEntry ce = p.expr_.<CxtEntry, Void> accept(new ExprVisitor(), env);
            int tmpReg = nextRegister();
            emit(new AddZero(ce.type, 1, tmpReg) );
            int sub = nextRegister();
            emit(new Add(ce.type, tmpReg, ce.addr, sub) );
            return new CxtEntry( ce.type, sub );
        }
        
        // EAdd.      Expr3 ::= Expr3 AddOp Expr4 ;
        public CxtEntry visit(Javalette.Absyn.EAdd p, Void env)
        {
            
            CxtEntry ce1 = p.expr_1.accept(new ExprVisitor(), null);
            CxtEntry ce2 = p.expr_2.accept(new ExprVisitor(), null);
            String addOp = p.addop_.accept(new AddOpVisitor(), null);
            int resultRegister = nextRegister();
            
            if( addOp.equals("+") ){ // Check if plus
                emit (new Add((ce1.type), ce1.addr, ce2.addr, resultRegister));
            }else if(addOp.equals("-")){
                emit (new Sub((ce1.type), ce1.addr, ce2.addr, resultRegister));
            }
            
            return new CxtEntry(ce1.type, resultRegister);
        }
        
        // EMul.      Expr4 ::= Expr4 MulOp Expr5 ;
        public CxtEntry visit(Javalette.Absyn.EMul p, Void env)
        {
            CxtEntry ce1 = p.expr_1.accept(new ExprVisitor(), null);
            CxtEntry ce2 = p.expr_2.accept(new ExprVisitor(), null);
            String mulOp = p.mulop_.accept(new MulOpVisitor(), null);
            int resultRegister = nextRegister();
            
            if( mulOp.equals("*") ){
                emit (new Mul((ce1.type), ce1.addr, ce2.addr, resultRegister));
            }else if ( mulOp.equals("/") ) {
                emit (new Div((ce1.type), ce1.addr, ce2.addr, resultRegister));
            } else if ( mulOp.equals("%") ) {
                emit (new Mod((ce1.type),ce1.addr, ce2.addr, resultRegister));
            }
            return new CxtEntry(ce1.type, resultRegister);
        }
        
        // ERel.      Expr2 ::= Expr2 RelOp Expr3 ;
        public CxtEntry visit(Javalette.Absyn.ERel p, Void env)
        {
            CxtEntry ce1 = p.expr_1.accept(new ExprVisitor(), null);
            CxtEntry ce2 = p.expr_2.accept(new ExprVisitor(), null);
            String relOp = p.relop_.accept(new RelOpVisitor(), null);
            int resultRegister = nextRegister();
            
            if( relOp.equals("<") ){
                emit (new CmpLT(ce1.type, ce1.addr, ce2.addr, resultRegister));
            }else if(relOp.equals("<=")){
                emit (new CmpLE(ce1.type, ce1.addr, ce2.addr, resultRegister));
            }else if(relOp.equals(">")){
                emit (new CmpGT(ce1.type, ce1.addr, ce2.addr, resultRegister));
            }else if(relOp.equals(">=")){
                emit (new CmpGE(ce1.type, ce1.addr, ce2.addr, resultRegister));
            }else if(relOp.equals("==")){
                emit (new CmpEQ(ce1.type, ce1.addr, ce2.addr, resultRegister));
            }else if(relOp.equals("!=")){
                emit (new CmpNE(ce1.type, ce1.addr, ce2.addr, resultRegister));
            }
            return new CxtEntry(TypeCode.CBool, resultRegister);
            
        }
        
        // EAnd.      Expr1 ::= Expr2 "&&" Expr1 ;
        public CxtEntry visit(Javalette.Absyn.EAnd p, Void env)
        {
            int lblID = nextLabel();
            String labelSecond        = "secondAnd" + lblID;
            String labelEnd           = "endAnd" + lblID;
            
            int tmpRegister1 = nextRegister();
            int tmpRegister2 = nextRegister();
            
            CxtEntry ce1 = p.expr_1.accept(new ExprVisitor(), null);
            int tmpVar = nextRegister();
            
            int falseRegister = nextRegister();
            
            // Create a temporary variable, initiated to false
            emit (new Alloca( tmpVar, TypeCode.CBool ));
            emit (new AddZero( TypeCode.CBool, 0, falseRegister ));
            emit (new Store( TypeCode.CBool, falseRegister, tmpVar ));
            
            String branch = "\n\nbr i1 %t" + ce1.addr + ", label %" + labelSecond + ", label %" + labelEnd + "\n\n";
            output.add(branch);
            
            
            output.add(labelSecond + ":\n");
            CxtEntry ce2 = p.expr_2.accept(new ExprVisitor(), null);
            emit (new And( TypeCode.CBool, ce1.addr, ce2.addr, tmpRegister1));
            // Update tmpVar with value of the AND operation
            emit (new Store( TypeCode.CBool, tmpRegister1, tmpVar ));
            output.add("br label %" + labelEnd + "\n");
            
            output.add(labelEnd + ":\n");
            
            emit (new Load( TypeCode.CBool, tmpRegister2, tmpVar ));
            
            int finalResult = nextRegister();
            
            emit (new Or( TypeCode.CBool, tmpRegister2, falseRegister, finalResult ) );
            
            return new CxtEntry( TypeCode.CBool, finalResult);
        }
        
        // EOr.       Expr ::= Expr1 "||" Expr ;
        public CxtEntry visit(Javalette.Absyn.EOr p, Void env)
        {
            int lblID = nextLabel();
            String labelSecond        = "secondAnd" + lblID;
            String labelEnd           = "endAnd" + lblID;
            
            int tmpRegister1 = nextRegister();
            int tmpRegister2 = nextRegister();
            
            CxtEntry ce1 = p.expr_1.accept(new ExprVisitor(), null);
            int tmpVar = nextRegister();
            
            int trueRegister = nextRegister();
            
            // Create a temporary variable, initiated to false
            emit (new Alloca( tmpVar, TypeCode.CBool));
            emit (new AddZero( TypeCode.CBool, 1, trueRegister ));
            emit (new Store( TypeCode.CBool, trueRegister, tmpVar ));
            
            String branch = "\n\nbr i1 %t" + ce1.addr + ", label %" + labelEnd + ", label %" +labelSecond  + "\n\n";
            output.add(branch);
            
            
            output.add(labelSecond + ":\n");
            CxtEntry ce2 = p.expr_2.accept(new ExprVisitor(), null);
            emit (new Or( TypeCode.CBool, ce1.addr, ce2.addr, tmpRegister1));
            // Update tmpVar with value of the Or operation
            emit (new Store( TypeCode.CBool, tmpRegister1, tmpVar ));
            output.add("br label %" + labelEnd + "\n");
            
            output.add(labelEnd + ":\n");
            
            emit (new Load( TypeCode.CBool, tmpRegister2, tmpVar ));
            
            int finalResult = nextRegister();
            
            emit (new And( TypeCode.CBool, tmpRegister2, trueRegister, finalResult ) );
            
            return new CxtEntry( TypeCode.CBool, finalResult);
        }
        
        //EArrayNew. Expr6 ::= "new" Type "[" Expr "]"
        public CxtEntry visit(Javalette.Absyn.EArrayNew p, Void env)
        {
            CxtEntry ce = p.expr_.accept(new ExprVisitor(), null);
            if( ce.type == TypeCode.CInt ){
                return ce;
            }
            else
                throw new TypeException( "Array index can only be of type Integer\n");
        }
        
        //EArrayLen. Expr6 ::= Ident "." "length" ;
        public CxtEntry visit(Javalette.Absyn.EArrayLen p, Void env)
        {
            CxtEntry var = lookupVar(p.ident_);
            int structure = nextRegister();
            int lengthPtr = nextRegister();
            int lengthReg = nextRegister();
            
            emit (new Load(var.type, structure, var.addr));
            emit (new GetElementPtr( var.type, lengthPtr, structure, 0 ) );
            emit (new Load(TypeCode.CInt, lengthReg, lengthPtr) );
            
            return new CxtEntry( TypeCode.CInt, lengthReg);
            
        }
        
        //EArrayPtr. Expr6 ::= Ident "[" Expr "]" ;
        public CxtEntry visit(Javalette.Absyn.EArrayPtr p, Void env)
        {
            CxtEntry identPtr = lookupVar( p.ident_ );
            CxtEntry ce = p.expr_.accept(new ExprVisitor(), null);
            
            int elementPtr = nextRegister();
            int structure = nextRegister();
            emit (new Load(identPtr.type, structure, identPtr.addr));
            emit (new GetElementFromArray( identPtr.type, elementPtr, structure, ce.addr ) );
            
            if(isLeftSideExpr){
                return new CxtEntry( baseType(identPtr.type), elementPtr);
            }
            else{
                int tmpReg = nextRegister();
                emit (new Load(baseType(identPtr.type), tmpReg, elementPtr));
                return new CxtEntry( baseType(identPtr.type), tmpReg);
            }
        }
    }
    
    //////////////////////////////////////////////////////////////////////
    public class ItemVisitor implements Item.Visitor<Void, TypeCode>
    {
        public Void visit(Javalette.Absyn.NoInit p, TypeCode tc)
        {
            // Create a new LLVM name for variable
            newVar (p.ident_, tc, 0);
            
            // Allocate memory for variable
            int var = (lookupVar(p.ident_)).addr;
            emit (new Alloca(var, tc));
            int zeroReg;
            
            if( tc == TypeCode.CInt ){
                zeroReg = nextRegister();
                emit (new AddZero( TypeCode.CInt, 0, zeroReg ));
                emit (new Store( TypeCode.CInt, zeroReg, var ) );
            }else if( tc == TypeCode.CDouble ){
                zeroReg = nextRegister();
                emit (new AddZero( TypeCode.CDouble, 0.0, zeroReg ));
                emit (new Store( TypeCode.CDouble, zeroReg, var ) );
            }else if( tc == TypeCode.CBool ){
                zeroReg = nextRegister();
                emit (new AddZero( TypeCode.CBool, 0, zeroReg ));
                emit (new Store( TypeCode.CBool, zeroReg, var ) );
            }
            
            return null;
        }
        //Init.      Item ::= Ident "=" Expr ;
        public Void visit(Javalette.Absyn.Init p, TypeCode tc)
        {
            CxtEntry ce = p.expr_.accept(new ExprVisitor(), null);
            
            // Create a new LLVM name for variable
            newVar (p.ident_, tc, 0);
            CxtEntry ident = lookupVar(p.ident_);
            
            // Check if variable is an array
            if( isArrayType(tc) ){
                
                // Allocate the array structure {i32, [0 x type]}*
                emit (new Alloca(ident.addr, tc) );
                
                if(isArrayType(ce.type)){
                    int tmpRegister = nextRegister();
                    emit (new Load(ce.type, tmpRegister, ce.addr));
                    emit (new Store(ce.type, tmpRegister, ident.addr ));
                }else{
                    
                    // Initiate the structure with the length
                    int totalSize = nextRegister();
                    int totalSizeInBytes = nextRegister();
                    int oneReg = nextRegister();
                    int heapPtr = nextRegister();
                    int structure = nextRegister();
                    int lengthPtr = nextRegister();
                    
                    if( tc == TypeCode.CArrayInt){
                        int fourReg = nextRegister();
                        emit (new AddZero(TypeCode.CInt, 4, fourReg ));
                        emit (new AddZero(TypeCode.CInt, 1, oneReg ));
                        emit (new Add(TypeCode.CInt, ce.addr, oneReg, totalSize));
                        emit (new Mul(TypeCode.CInt, totalSize, fourReg, totalSizeInBytes));
                    }
                    else if(tc == TypeCode.CArrayDouble){
                        int eightReg = nextRegister();
                        int fourReg = nextRegister();
                        int tmp = nextRegister();
                        emit (new AddZero(TypeCode.CInt, 8, eightReg ));
                        emit (new AddZero(TypeCode.CInt, 4, fourReg ));
                        emit (new Mul(TypeCode.CInt, ce.addr, eightReg, tmp));
                        emit (new Add(TypeCode.CInt, tmp, fourReg, totalSizeInBytes));
                    }
                    else if(tc == TypeCode.CArrayBool){
                        int fourReg = nextRegister();
                        emit (new AddZero(TypeCode.CInt, 4, fourReg ));
                        emit (new Add(TypeCode.CInt, ce.addr, fourReg, totalSizeInBytes));
                    }
                    
                    emit (new Calloc(heapPtr, totalSizeInBytes, ident.type));
                    emit (new BitCast( structure, heapPtr, tc) );
                    emit (new GetElementPtr( tc, lengthPtr, structure, 0 ) );
                    emit (new Store(TypeCode.CInt, ce.addr, lengthPtr));
                    emit (new Store( tc, structure, ident.addr ));
                }
                
            }else{
                // Allocate memory for the new variable
                int newVar = (lookupVar(p.ident_)).addr;
                emit (new Alloca( newVar,tc, 0 ));
                emit (new Store( tc, ce.addr, newVar));
            }
            
            return null;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    public class AddOpVisitor implements AddOp.Visitor<String, Void>
    {
        public String visit(Javalette.Absyn.Plus p, Void v) { return "+"; }
        public String visit(Javalette.Absyn.Minus p, Void v){ return "-"; }
    }
    
    ////////////////////////////////////////////////////////////////////////
    public class MulOpVisitor implements MulOp.Visitor<String, Void>
    {
        public String visit(Javalette.Absyn.Times p, Void v) { return "*"; }
        public String visit(Javalette.Absyn.Div p, Void v)   { return "/"; }
        public String visit(Javalette.Absyn.Mod p, Void v)   { return "%"; }
    }
    
    ////////////////////////////////////////////////////////////////////////
    public class RelOpVisitor implements RelOp.Visitor<String,Void>
    {
        
        public String visit(Javalette.Absyn.LTH p, Void env) { return "<"; }
        public String visit(Javalette.Absyn.LE p, Void env)  { return "<="; }
        public String visit(Javalette.Absyn.GTH p, Void env) { return ">"; }
        public String visit(Javalette.Absyn.GE p, Void env)  { return ">="; }
        public String visit(Javalette.Absyn.EQU p, Void env) { return "=="; }
        public String visit(Javalette.Absyn.NE p, Void env)  { return "!="; }
    }
    ////////////////////////////////////////////////////////////////////////
    public class TypeVisitor implements Type.Visitor<TypeCode, Void>
    {
        public TypeCode visit(Javalette.Absyn.Type_bool p, Void env)  { return TypeCode.CBool;  }
        public TypeCode visit(Javalette.Absyn.Type_int p, Void env)   { return TypeCode.CInt;   }
        public TypeCode visit(Javalette.Absyn.Type_double p, Void env){ return TypeCode.CDouble;}
        public TypeCode visit(Javalette.Absyn.Type_void p, Void env)  { return TypeCode.CVoid;  }
        public TypeCode visit(Javalette.Absyn.Type_array p, Void env)  {
            
            TypeCode t = p.type_.<TypeCode,Void> accept(new TypeVisitor(), env);
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
        
        
        
        public TypeCode visit(Javalette.Absyn.Fun p, Void env)  { return TypeCode.CVoid;  }
    }
}
