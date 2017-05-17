package testGit;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

public class head {
	public static void main(String args[]) throws Exception {
		//String home = "D:/CommitData/";
		//String home = "C:/Users/WangQL/Documents/Code/";
		String home = "";
		String osName = System.getProperty("os.name");
		if(osName.contains("Mac"))
		{
			home = "/Users/WangQL/Documents/git/";
		}
		else if(osName.contains("Windows"))
		{
			//path = "C:\\Users\\WangQL\\Desktop\\ArrayBackedTag.java";
			home="C:/Users/WangQL/Documents/Code/";
		}
		String[] projects = new String[] { "Java", "hadoop", "hbase", "hibernate-search", "jenkins", "karaf" };
		int projectIndex =  0;
		String project = projects[projectIndex];
		double testRate = 0.2;

		boolean preprocess = true;
		boolean modeling = false;

		if (preprocess) {
			CommitFeature.gitLogAnalysis analysis = new CommitFeature.gitLogAnalysis(home, project);
			analysis.analysis();
		}

		if (modeling) {
			Modeling.start st = new Modeling.start(home, project, testRate);
			st.write();
		}

	}
}
