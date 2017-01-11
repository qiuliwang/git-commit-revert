package testGit;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

public class head {
	public static void main(String args[])
	{
		System.out.println("1. analysis the project");
		System.out.println("2. model");
		System.out.println("3. analysis project and model");
		System.out.println("4. exit");
		System.out.print("Enter: ");
		String project;
		double percent;
		Scanner in=new Scanner(System.in);
		int ans = in.nextInt();
		
		while(ans != 4)
		{
			try{
				switch(ans)
				{
				case 1:
					System.out.println("Project: ");
					project = in.next();
					CommitFeature.gitLogAnalysis analysis = new CommitFeature.gitLogAnalysis(project);
					analysis.analysis();
					break;
				case 2:
					System.out.println("Project: ");
					project = in.next();
					System.out.println("rate: ");
					double rate = in.nextDouble();
					Modeling.start st = new Modeling.start(project, rate);
					st.write();
					break;
				case 3: 
					System.out.println("Project: ");
					project = in.next();
					System.out.println("rate: ");
					double rate2 = in.nextDouble();
					CommitFeature.gitLogAnalysis analysis2 = new CommitFeature.gitLogAnalysis(project);
					analysis2.analysis();
					Modeling.start st2 = new Modeling.start(project, rate2);
					st2.write();
					break;
				}
			}
			catch(Exception e)
			{
				
			}
			try {
				Runtime.getRuntime().exec("cls");
				Runtime.getRuntime().exec("clear");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Enter: ");
			ans = in.nextInt();
		}
		in.close();
	}
}
