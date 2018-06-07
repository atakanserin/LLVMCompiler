# Makefile for Javalette



JAVAC 		= javac
JAVAC_FLAGS 	= -sourcepath .
JAVA 		= java

# Name of generated .cup file for bnfc 2.8.1
CUPFILE = Javalette/Javalette.cup

####################################################
makecompile:
	chmod a+x ../jlc
	javac *.java
	javac ./Javalette/*.java
	javac ./Javalette/Absyn/*.java
	llvm-as ../lib/runtime.ll
	
bnfc:
	bnfc -java Javalette.cf
	${JAVA} ${JAVA_FLAGS} JLex.Main Javalette/Yylex
	${JAVA} ${JAVA_FLAGS} java_cup.Main -nopositions -expect 100 $(CUPFILE)
	mv sym.java parser.java Javalette

.PHONY: clean

clean:
	rm *.class
	rm ./Javalette/*.class
	rm ./Javalette/Absyn/*.class
	rm ../lib/runtime.bc
