package cn.tonyn.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFile {
	public static void Write(String file,String str) {
		File textfile=new File(file);
        FileWriter writer = null;
	    try {
			writer = new FileWriter(textfile , true);
			BufferedWriter out = new BufferedWriter(writer);
			out.write(str);
			out.flush();
			out.close();
	    } catch (IOException e) {
			Logger.l("IOException:"+e.getMessage(),"IOException");
	    }
	}
	
	public static void Write(File file, String str) {
		FileWriter writer = null;
	    try {
			writer = new FileWriter(file , true);
			BufferedWriter out = new BufferedWriter(writer);
			out.write(str);
			out.flush();
			out.close();
	    } catch (IOException e) {
			Logger.l("IOException:"+e.getMessage(),"IOException");
			e.printStackTrace();
	    }
	}
	
	public static String Read(String file) {
        File textfile=new File(file);
	    FileReader reader = null;
	    try {
			reader = new FileReader(textfile);
			BufferedReader in = new BufferedReader(reader);
			String s=in.readLine();
			in.close();
			return s;

	    } catch (IOException e) {
			Logger.l("IOException:"+e.getMessage(),"IOException");
			e.printStackTrace();
			return "";
	    }

		
	}
	
	public static String Read(File file) {
	    FileReader reader = null;
	    try {
			reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String s=in.readLine();
			in.close();
			return s;

	    } catch (IOException e) {
			Logger.l("IOException:"+e.getMessage(),"IOException");
			e.printStackTrace();
			return "";
	    }

	}
	
	public static void Empty(String file) {
		if(new File(file).isFile()) {
			if(new File(file).delete()) {
				Write(file,"");
			}else {
				Logger.l("can't delete file: "+file, "Error");
			}
		}
	}
    public static void Empty(File file) {
    	if(file.isFile()) {
			if(file.delete()) {
				Write(file,"");
			}else {
				Logger.l("can't delete file: "+file, "Error");
			}
		}
	}

}
