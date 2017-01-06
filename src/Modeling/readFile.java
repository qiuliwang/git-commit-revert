package Modeling;

//import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class readFile {
	FileReader data_reader;
	Instances data;
	
	public readFile()
	{
		
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
	        loader.setNominalAttributes("19");

	        
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
