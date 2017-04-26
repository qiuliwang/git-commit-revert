package Modeling;

import weka.core.Instances;
import weka.filters.supervised.instance.SMOTE;
public class smote {
	Instances localdata;
	public static void main(String args[])
	{

	}

	
	public smote(Instances data, double percent, int neighbors)
	{		
		SMOTE localsmote = new SMOTE();
		try {
			localsmote.setInputFormat(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localsmote.setPercentage(percent);
		localsmote.setNearestNeighbors(neighbors);
		try {
			localdata = weka.filters.Filter.useFilter(data, localsmote);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double cal(Instances data)
	{
		data.setClassIndex(data.numAttributes() - 1);
		int cout0 = 0, cout1 = 0;
		for(int i = 0; i < data.numInstances(); i ++)
		{
			if(data.instance(i).classValue() == 0.0)
			{
				cout0 ++;
			}
			else if(data.instance(i).classValue() == 1.0)
			{
				cout1 ++;
			}
		}
		//System.out.println("0.0:" + cout0);
		//System.out.println("1.0:" + cout1);
		double ans =  (double)cout0 / cout1;
		//System.out.println("Per:" + ans);

		return ans;
	}
	public Instances getSmoteData()
	{
		return localdata;
	}
}
