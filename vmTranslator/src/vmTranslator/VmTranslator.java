
package vmTranslator;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class VmTranslator {

	public static void main(String[] args) throws IOException {
		String A = "D:\\nand2tetris\\projects\\08\\FunctionCalls\\StaticsTest\\Sys.vm";
		
		Path OutfileName = Path.of("D:\\nand2tetris\\projects\\08\\FunctionCalls\\StaticsTest\\StaticsTest.asm");

		List<String> allLines = new ArrayList<String>();
		List<String> finlLines = new ArrayList<String>();
		allLines = functionc.addSysInit(A);

		String[] split10 = A.split("\\\\");
		String funcName = split10[split10.length - 2];
		//String filename = "  ";
		int callcount = 0;
		int count=0;
		String line = "  ";
		
		if(funcName.equals("sys")){
			line = "@256\nD=A\n@SP\nM=D\n";
			finlLines.add(line);
		}

		for (int i = 0; i < allLines.size(); i++) {

			String updline = allLines.get(i);
			String[] split1 = updline.split(" ");
			String type = split1[0];
			String segment = (split1.length > 1) ? split1[1] : "";
			String nArgs = (split1.length > 2) ? split1[2] : "";
			
		

			if (type.equals("push")) {
				if (segment.equals("local")) {
					line = "\n@LCL\nD=M\n@" + nArgs + "\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("argument")) {
					line = "\n@ARG\nD=M\n@" + nArgs + "\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("static")) {
					line = "\n@" + funcName + "." + nArgs + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("constant")) {
					line = "\n@" + nArgs + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("this")) {
					line = "\n@THIS\nD=M\n@" + nArgs + "\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("that")) {
					line = "\n@THAT\nD=M\n@" + nArgs + "\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("temp")) {
					line = "\n@" + nArgs + "\nD=A\n@5\nA=A+D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (segment.equals("pointer")) {
					if (nArgs.equals("0")) {
						line = "\n@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";

					} else if (nArgs.equals("1")) {
						line = "\n@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";

					}
					finlLines.add(line);
				}

			} else if (type.equals("pop")) {
				if (segment.equals("local")) {
					line = "\n@LCL\nD=M\n@" + nArgs + "\nD=D+A\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D";
					finlLines.add(line);

				} else if (segment.equals("argument")) {
					line = "\n@ARG\nD=M\n@" + nArgs + "\nD=D+A\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D";
					finlLines.add(line);

				} else if (segment.equals("static")) {
					line = "\n@SP\nAM=M-1\nD=M\n @" + funcName + "." + nArgs + "\nM=D";
					finlLines.add(line);

				} else if (segment.equals("this")) {
					line = "\n@THIS\nD=M\n@" + nArgs + "\nD=D+A\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D";
					finlLines.add(line);

				} else if (segment.equals("that")) {
					line = "\n@THAT\nD=M\n@" + nArgs + "\nD=D+A\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D";
					finlLines.add(line);

				} else if (segment.equals("temp")) {
					line = "\n@5\nD=A\n@" + nArgs + "\nD=D+A\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D";
					finlLines.add(line);

				} else if (segment.equals("pointer")) {
					if (nArgs.equals("0")) {
						line = "\n@SP\nAM=M-1\nD=M\n@THIS\nM=D\n";
						finlLines.add(line);

					}
					if (nArgs.equals("1")) {
						line = "\n@SP\nAM=M-1\nD=M\n@THAT\nM=D\n";
						finlLines.add(line);

					}
				}
			} 
			else if (type.equals("label")) {
				line = "\n(" + segment + ")";
				finlLines.add(line);

			}
			else if (type.equals("if-goto")) {
				line = "\n@SP\nAM=M-1\nD=M\n@" + segment + "\nD;JNE";
				finlLines.add(line);

			} 
			else if (type.equals("goto")) {
				line = "\n@" + segment + "\n0;JMP";
				finlLines.add(line);

			}

			else if (type.equals("call")) {

				// SAVING CALLER FRAME
				line = "\n@" + segment + callcount + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"; // RETURN ADDR
				line = line + "\n@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";                  // SAVED LCL
				line = line + "\n@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";                  // SAVED ARG
				line = line + "\n@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";                 // SAVED THIS
				line = line + "\n@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";                 // SAVED THAT
				// SETTING ARG TO ARGUMENT 0 INDEX
				line = line + "\n@SP\nD=M\n@LCL\nM=D\n@5\nD=D-A\n@" + nArgs + "\nD=D-A\n@ARG\nM=D\n"; 
				line = line + "\n@" + segment + "\n0;JMP\n"; // JUMPS to execute function
				line = line + "\n(" + segment + callcount + ")\n";

				finlLines.add(line);
				callcount++;

			} else if (type.equals("function")) {

				int localvar = Integer.parseInt(nArgs);

				line = "(" + segment + ")\n";
				for (int j = 0; j < localvar; j++) {
					line = line + "\n@SP\nA=M\nM=0\n@SP\nM=M+1";
				}

				String[] filesplit = segment.split("\\.");
				funcName = filesplit[0];
				finlLines.add(line);

			} else if (type.equals("return")) {

				// restoring the frame
				line = "@LCL\nD=M\n@13\nM=D\n@5\nA=D-A\nD=M\n@14\nM=D\n";     // RETURN ADDR IS STORED IN RAM[1]
				line = line + "\n@SP\nA=M-1\nD=M\n@ARG\nA=M\nM=D\n";
				line = line + "\n@ARG\nD=M+1\n@SP\nM=D\n";
				line = line + "\n@1\nD=A\n@13\nD=M-D\nA=D\nD=M\n@THAT\nM=D\n";
				line = line + "\n@2\nD=A\n@13\nD=M-D\nA=D\nD=M\n@THIS\nM=D\n";
				line = line + "\n@3\nD=A\n@13\nD=M-D\nA=D\nD=M\n@ARG\nM=D\n";
				line = line + "\n@4\nD=A\n@13\nD=M-D\nA=D\nD=M\n@LCL\nM=D\n";
				line = line + "\n@14\nA=M\n0;JMP";

				finlLines.add(line);
			}

			else {
				if (type.equals("add")) {
					line ="@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M+D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (type.equals("sub")) {
					line = "@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M-D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (type.equals("neg")) {
					line = "@SP\nAM=M-1\nD=-M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (type.equals("and")) {
					line ="@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M&D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (type.equals("or")) {
					line = "@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M|D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (type.equals("not")) {
					line = "@SP\nAM=M-1\nD=!M\nM=D\n@SP\nM=M+1";
					finlLines.add(line);

				} else if (type.equals("gt")) {
					line ="@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nD=M-D\n@true"+ count + "\nD;JGT\n@SP\nA=M\nM=0\n@end" + count +"\n0;JMP\n(true" + count +")\n@SP\nA=M\nM=-1\n(end" + count +")\n@SP\nM=M+1" ;
					finlLines.add(line);
					count++;

				} else if (type.equals("lt")) {
					line = "@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nD=M-D\n@true"+ count + "\nD;JLT\n@SP\nA=M\nM=0\n@end" + count +"\n0;JMP\n(true" + count +")\n@SP\nA=M\nM=-1\n(end" + count +")\n@SP\nM=M+1";
					finlLines.add(line);
					count++;

				} else if (type.equals("eq")) {
					line ="@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nD=M-D\n@true"+ count + "\nD;JEQ\n@SP\nA=M\nM=0\n@end" + count +"\n0;JMP\n(true" + count +")\n@SP\nA=M\nM=-1\n(end" + count +")\n@SP\nM=M+1";
					finlLines.add(line);
					count++;

				}
			}
		}
		try {
			Files.write(OutfileName, finlLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
