package vmTranslator;

import java.util.*;

public class Table {

    static Hashtable<String, String> c_Arithmetic_table = new Hashtable<String, String>();
    
    static int count;
    static void c_Arithmetic_table() 
    {
        c_Arithmetic_table.put("add","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M+D\n@SP\nM=M+1");
        c_Arithmetic_table.put("sub","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M-D\n@SP\nM=M+1");
        c_Arithmetic_table.put("neg","@SP\nAM=M-1\nD=-M\nM=D\n@SP\nM=M+1");
        c_Arithmetic_table.put("and","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M&D\n@SP\nM=M+1");
        c_Arithmetic_table.put("or","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nM=M|D\n@SP\nM=M+1");
        c_Arithmetic_table.put("not","@SP\nAM=M-1\nD=!M\nM=D\n@SP\nM=M+1");
        c_Arithmetic_table.put("gt","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nD=M-D\n@true"+ count + "\nD;JGT\n@SP\nA=M\nM=0\n@end" + count +"\n0;JMP\n(true" + count +")\n@SP\nA=M\nM=-1\n(end" + count +")\n@SP\nM=M+1");
        c_Arithmetic_table.put("lt","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nD=M-D\n@true"+ count + "\nD;JLT\n@SP\nA=M\nM=0\n@end" + count +"\n0;JMP\n(true" + count +")\n@SP\nA=M\nM=-1\n(end" + count +")\n@SP\nM=M+1");
        c_Arithmetic_table.put("eq","@SP\nAM=M-1\nD=M\n@SP\nAM=M-1\nD=M-D\n@true"+ count + "\nD;JEQ\n@SP\nA=M\nM=0\n@end" + count +"\n0;JMP\n(true" + count +")\n@SP\nA=M\nM=-1\n(end" + count +")\n@SP\nM=M+1");
        
    }

    public static String get_c_arth_code(final String key) // to get the corresponding binary value to the key
    { 
		c_Arithmetic_table();
        if(key.equals("gt")||key.equals("lt")||key.equals("eq"))
        {
            count++;
        }
		return c_Arithmetic_table.get(key);
	}
    
}
