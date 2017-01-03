package Modeling;

import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;

public class start {
	public static void main(String []args) throws Exception
	{
		readFile re = new readFile();		
		String inputCSV = CommitFeature.gitLogAnalysis.home +"/"+ CommitFeature.gitLogAnalysis.project + "Output/AllLog.csv";
		String outputArff = CommitFeature.gitLogAnalysis.home +"/"+ CommitFeature.gitLogAnalysis.project + "Output/AllLog.arff";
		re.CSVtoARFF(inputCSV, outputArff);		
		Instances data = re.getData(outputArff);
		
	
		RandomForest forest = new RandomForest();
		//forest.buildClassifier(data);		
		forest.setSeed(2);
		
		//data.trainCV(arg0, arg1);
		//data.instance(index)
		int numberOfAllInstances = data.numInstances();
		int testIndex = 0;  // the latest instances serve as test dataset;
		double testRate = 0.2; // the rate of testing dataset;
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
