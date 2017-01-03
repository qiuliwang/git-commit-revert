package Modeling;

import weka.core.Instances;
import weka.filters.supervised.instance.SMOTE;
public class smote {
	Instances localdata;
	public smote(Instances data, double percent)
	{		
		SMOTE localsmote = new SMOTE();
		try {
			localsmote.setInputFormat(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localsmote.setPercentage(percent);
		try {
			localdata = weka.filters.Filter.useFilter(data, localsmote);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Instances getSmoteData()
	{
		return localdata;
	}
}
