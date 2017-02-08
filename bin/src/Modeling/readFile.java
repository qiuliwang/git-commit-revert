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
        readFile fg = new readFile();
        List<String> fileName;

        fileName = traverseFolder1("/Users/WangQL/Documents/MATLAB/Experiment1-14/data/new");
        int plus = 0;
        int minus = 0;
        for(int i = 0; i < fileName.size(); i ++)
        {
            plus = 0;
            minus = 0;
            try {
                Instances temp = fg.getData(fileName.get(i));
                System.out.println(temp.numInstances());
                smote st = new smote(temp, 100);
                Instances temp2 = st.getSmoteData();
                System.out.println(temp2.numInstances());
//                int a = 0, b = 0;
//                for(int j = 0; j < temp.numInstances(); j ++)
//                {
//                    //System.out.println(temp.instance(j));
//                   //temp.instance(j).classValue());
//                    if(temp.instance(j).classValue() == 0.0)
//                    {
//                        minus ++;
//                    }
//                    else
//                    {
//                        plus ++;
//                    }
//                }
//                System.out.println("0.0:" + minus);
//                System.out.println("1.0:" + plus);
//                //System.out.println("per:" + (double)a / b);
//                if((double)a / b > 1)
//                {
//                    smote st = new smote(temp, 100);
//                    Instances inner = st.getSmoteData();
//                    inner.setClassIndex(inner.numAttributes() - 1);
//
//                }
//                else if((double)b / a > 1)
//                {
//
//                }
                System.out.println("~~~~~~~~~~~~~~");

            }
            catch(Exception e)
            {

            }
        }
        System.out.println("per:" + (double)minus / plus);

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
					System.out.println("文件夹:" + file2.getAbsolutePath());
					list.add(file2);
                    folderNum++;
				} else {
				    String filename = file2.getAbsolutePath();
				    if(filename.contains(".arff")) {
                        System.out.println("文件:" + filename);
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
						System.out.println("文件夹:" + file2.getAbsolutePath());
						list.add(file2);
                        folderNum++;
					} else {
						System.out.println("文件:" + file2.getAbsolutePath());
                        fileNum++;
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
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

	        
	        Instances data = loader.getDataSet();
	        data.setClassIndex(17);
	        
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
