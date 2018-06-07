import java_cup.runtime.*;
import Javalette.*;
import Javalette.Absyn.*;
import java.io.*;

public class jlc
{

    public static String stripPath(String path){
	int lastIndex = path.lastIndexOf("/");
	return path.substring(lastIndex + 1, path.length());
    }

    public static String stripExtension(String file){
	int lastIndex = file.lastIndexOf(".");
	return file.substring(0, lastIndex);
    }    
    public static void main(String args[]) throws Exception
    {
	Yylex l = null;
	parser p;
	try
	    {
		if (args.length == 0)
		    l = new Yylex(new InputStreamReader(System.in));
		else
		    l = new Yylex(new FileReader(args[0]));
	    }
	catch(FileNotFoundException e)
	    {
		System.err.println("Error: File not found: " + args[0]);
		System.exit(1);
	    }

	String fileName = args[0];
	try
	    {
		p = new parser(l);
		Javalette.Absyn.Prog parseTree = p.pProg();

		new TypeChecker().typecheck(parseTree);
		new LLVMGenerator().generate(stripExtension(stripPath(fileName)), parseTree);
		System.err.println("OK");
		System.exit(0);
	    }
	catch(Throwable e)
	    {
		System.err.println("ERROR");
		System.err.println(e.getMessage());
		System.exit(1);
	    }
    }
}
