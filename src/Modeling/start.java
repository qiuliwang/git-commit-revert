package Modeling;

import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;

public class start {
	
	private String project;
	private double rate = 0.2;
	public start(String pro, double rate)
	{
		project = pro;
		this.rate = rate;
	}
	public start(String pro)
	{
		project = pro;
		//this.rate = rate;
	}
	
	public static void main(String args[])
	{
		start st = new start("Java");
		try {
			st.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write() throws Exception
	{
		//CommitFeature.gitLogAnalysis ans = new CommitFeature.gitLogAnalysis("Java");
		String home = "/Users/WangQL/Documents/git/";
		readFile re = new readFile();		
		String inputCSV = home + project + "Output/AllLog.csv";
		String outputArff = home + project + "Output/AllLog.arff";
		re.CSVtoARFF(inputCSV, outputArff);		
		Instances data = re.getData(outputArff);
		System.out.println(inputCSV);
	
		RandomForest forest = new RandomForest();
		//forest.buildClassifier(data);		
		forest.setSeed(2);
		
		//data.trainCV(arg0, arg1);
		//data.instance(index)
		int numberOfAllInstances = data.numInstances();
		int testIndex = 0;  // the latest instances serve as test dataset;
		double testRate = rate; // the rate of testing dataset;
		int numberOfTest = (int) (numberOfAllInstances*testRate);
		int trainIndex = numberOfTest;
		int numberOfTrain = numberOfAllInstances-numberOfTest;
		
		
		Instances trainData = new Instances(data,trainIndex,numberOfTrain);
		Instances testData = new Instances(data,testIndex, numberOfTest);
		
		int trainSize = trainData.numInstances();
		int testSize = testData.numInstances();
		
		smote sm = new smote(trainData, 1000);
		Instances smoteTraindata = sm.localdata;	
		
		
		int smoteTrainSize = smoteTraindata.numInstances();
		//Instance lastTest = testData.lastInstance();
		//Instance firstTrain = trainData.firstInstance();
		//eva.crossValidateModel(forest, data, 10, new Random());
		//Evaluation eva = evaluateTestData(forest, smoteTraindata, testData);
		Evaluation eva = evaluateTestData(forest, trainData, testData);
		System.out.println("number of AllInstances:"+numberOfAllInstances);
		System.out.println("number of Training:"+trainSize);
		System.out.println("number of SmoteTraining:"+smoteTrainSize);
		System.out.println("number of Testing:"+testSize);
		
		System.out.println(eva.toClassDetailsString());
		System.out.println(eva.toSummaryString());
		System.out.println(eva.toMatrixString());
		
	}
	
	
	public static Evaluation evaluateTestData(Classifier classifier, Instances trainData, Instances testData ) throws Exception{
		
		//RandomForest forest = new RandomForest();
		classifier.buildClassifier(trainData);
		
		Evaluation eval = new Evaluation(trainData);
		eval.evaluateModel(classifier, testData);
		
		
		return eval;
	}
		
}
