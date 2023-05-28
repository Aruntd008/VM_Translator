package vmTranslator;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;


public class removeblank 
{
    public static List<String> clean(RandomAccessFile file ) throws IOException
    {
        List<String> tempLines1 = new ArrayList<String>();
        String line;
        while ((line = file.readLine()) != null)              // While loop is reads one line at a time and store in the "String line" 
		{
			if(!line.isEmpty())                               // skips empty lines
			{
				line=line.trim();                             // removes leading and trailings blank spaces
				String[] split = line.split("//");         // removes in line comments
				if(!split[0].isEmpty())                       // skips full line comments
				{
					line=split[0];
					line=line.trim();
					tempLines1.add(line);                     // Stores the edited line in the "list allLines"
				}
			}
		}
        return tempLines1;
    }
    public static List<String> clean(List<String> file ) throws IOException
    {
        List<String> tempLines = new ArrayList<String>();
        
        for (int i = 0; i < file.size(); i++)          // While loop is reads one line at a time and store in the "String line" 
		{
        	String line1 = file.get(i);
			if(!line1.isEmpty())                             // skips empty lines
			{
				line1=line1.trim();                          // removes leading and trailings blank spaces
				String[] split = line1.split("//");          // removes in line comments
				if(!split[0].isEmpty())                      // skips full line comments
				{
					line1=split[0];
					line1=line1.trim();
					tempLines.add(line1);                    // Stores the edited line in the "list allLines"
				}
			}
		}
        return tempLines;
    }
}
