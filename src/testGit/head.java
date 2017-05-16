package testGit;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

public class head {
	public static void main(String args[]) throws Exception {
		String home = "D:/CommitData/";
		String[] projects = new String[] { "testgit", "hadoop", "hbase", "hibernate-search", "jenkins", "karaf" };
		int projectIndex =  2;
		String project = projects[projectIndex];
		double testRate = 0.2;

		boolean preprocess = false;
		boolean modeling = true;

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
