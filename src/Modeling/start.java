package Modeling;

import java.io.IOException;
import java.util.Random;

import CommitFeature.gitLogAnalysis;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;

public class start {
	private static String home;
	private String project;
	private double rate;
	public start(String homeUrl, String pro, double rate)
	{
		home = homeUrl;
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
		start st = new start("hbase");
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
		//String home = "/Users/WangQL/Documents/git/";
		readFile re = new readFile();		
		String inputCSV = home + project + "Output/AllCommits.csv";
		String outputArff = home + project + "Output/AllCommits.arff";
		re.CSVtoARFF(inputCSV, outputArff);		
		Instances data = re.getData(outputArff);
		System.out.println(inputCSV);
	
		RandomForest forest = new RandomForest();
		//forest.buildClassifier(data);		
		forest.setSeed(2);
		
		//data.trainCV(arg0, arg1);
		//data.instance(index)
	
		int numberOfAllInstances = data.numInstances();
		//int classNum = data.numInstances(8);

		
		int testIndex = 0;  // the latest instances serve as test dataset;
		//double testRate = 0.34; // the rate of testing dataset;
		int numberOfTest = (int) (numberOfAllInstances*rate);
		int trainIndex = numberOfTest;
		int numberOfTrain = numberOfAllInstances-numberOfTest;
		
		
		Instances trainData = new Instances(data,trainIndex,numberOfTrain);		
		Instances testData = new Instances(data,testIndex, numberOfTest);
		smote sm = new smote(trainData,100,10);
		Instances smoteTraindata = sm.getSmoteData();	
		
		AttributeStats trainStats = trainData.attributeStats(trainData.classIndex());
		AttributeStats testStats = testData.attributeStats(trainData.classIndex());
		AttributeStats smoteTrainStats = smoteTraindata.attributeStats(trainData.classIndex());
		
		int[] trainCount = trainStats.nominalCounts;
		int[] testCount = testStats.nominalCounts;
		int[] smoteTrainCount = smoteTrainStats.nominalCounts;
				
		int trainSize = trainData.numInstances();
		int testSize = testData.numInstances();
		int smoteTrainSize = smoteTraindata.numInstances();
		
		
		//Instance lastTest = testData.lastInstance();
		//Instance firstTrain = trainData.firstInstance();
		//eva.crossValidateModel(forest, data, 10, new Random());
		//Evaluation eva = evaluateTestData(forest, smoteTraindata, testData);
		Evaluation eva = evaluateTestData(forest, trainData, testData);
		System.out.println("This project:"+project);
		System.out.println("number of AllInstances:"+numberOfAllInstances);
		System.out.println("number of Training:"+trainSize);
		System.out.println("number of SmoteTraining:"+smoteTrainSize);
		System.out.println("number of Testing:"+testSize);
		System.out.println("Rate of Testing:"+rate);
		System.out.println("Training stats: normal:"+trainCount[0]+"  reverted:"+trainCount[1]);
		System.out.println("Training stats by smote: normal:"+smoteTrainCount[0]+"  reverted:"+smoteTrainCount[1]);
		System.out.println("Testing stats: normal:"+testCount[0]+"  reverted:"+testCount[1]);
		
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
