package Modeling;

//import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import weka.core.Instance;
import weka.core.Instances;
import weka.core.SystemInfo;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class readFile {
	FileReader data_reader;
	Instances data;
	public static void main(String args[])
    {


    }



	public static List<String> traverseFolder1(String path) {
	    List<String> fileNames = new ArrayList<String>();
		int fileNum = 0, folderNum = 0;
		File file = new File(path);
		if (file.exists()) {
			LinkedList<File> list = new LinkedList<File>();
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isDirectory()) {
					System.out.println("鏂囦欢澶�:" + file2.getAbsolutePath());
					list.add(file2);
                    folderNum++;
				} else {
				    String filename = file2.getAbsolutePath();
				    if(filename.contains(".arff")) {
                        System.out.println("鏂囦欢:" + filename);
                        fileNum++;
                        fileNames.add(filename);
                        //Instances temp =
                    }
				}
			}
			File temp_file;
			while (!list.isEmpty()) {
				temp_file = list.removeFirst();
				files = temp_file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("鏂囦欢澶�:" + file2.getAbsolutePath());
						list.add(file2);
                        folderNum++;
					} else {
						System.out.println("鏂囦欢:" + file2.getAbsolutePath());
                        fileNum++;
					}
				}
			}
		} else {
			System.out.println("鏂囦欢涓嶅瓨鍦�!");
		}
		System.out.println("鏂囦欢澶瑰叡鏈�:" + folderNum + ",鏂囦欢鍏辨湁:" + fileNum);
        return fileNames;
	}
	
	public void CSVtoARFF(String csvfilename, String arffFileName) {
	    try {
	        // load CSV
	        CSVLoader loader = new CSVLoader();
	        loader.setSource(new File(csvfilename));
	        //loader.setNominalAttributes("0");
//	        loader.setNominalAttributes("1");
//	        loader.setNominalAttributes("2");
//	        loader.setNominalAttributes("3");
//	        loader.setNominalAttributes("4");
//	        loader.setNominalAttributes("5");
//	        loader.setNominalAttributes("6");
//	        loader.setNominalAttributes("7");
//	        loader.setNominalAttributes("8");
//	        loader.setNominalAttributes("9");
//	        loader.setNominalAttributes("10");
//	        loader.setNominalAttributes("11");
//	        loader.setNominalAttributes("12");
//	        loader.setNominalAttributes("13");
//	        loader.setNominalAttributes("14");
//	        loader.setNominalAttributes("15");
//	        loader.setNominalAttributes("16");
//	        loader.setNominalAttributes("17");
	        loader.setNominalAttributes("20");
	        loader.setNominalAttributes("21");
	        loader.setNominalAttributes("22");
	        loader.setNominalAttributes("23");
	        loader.setNominalAttributes("24");
        
	        Instances data = loader.getDataSet();
	        data.setClassIndex(19);
	        
	        
	        // save ARFF
	        ArffSaver saver = new ArffSaver();
	        saver.setInstances(data);
	        saver.setFile(new File(arffFileName));
	        //saver.setDestination(new File(arfffilename));
	        saver.writeBatch();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	public Instances getData(String arffFileName) throws IOException
	{		
			data_reader = new FileReader(arffFileName);
			data = new Instances(data_reader);
			//System.out.println(data);
			data.setClassIndex(data.numAttributes()-1);		
		return data;
	}
}
