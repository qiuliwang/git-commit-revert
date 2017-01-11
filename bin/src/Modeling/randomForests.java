package Modeling;

import weka.classifiers.trees.RandomForest;

public class randomForests {
	RandomForest forest;
	public randomForests(Integer seed)
	{
		forest = new RandomForest();
		forest.setSeed(seed);
	}
	
	public RandomForest getForest()
	{
		return forest;
	}
}
