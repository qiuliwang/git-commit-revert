package testGit;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

public class head {
	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
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
			switch(ans)
			{
				case 1:
					System.out.print("Project: ");
					project = in.next();
					CommitFeature.gitLogAnalysis analysis = new CommitFeature.gitLogAnalysis(project);
					break;
				case 2: System.out.println("2"); break;
				case 3: System.out.println("3"); break;
			}
			System.out.print("Enter: ");
			ans = in.nextInt();
		}
		in.close();
	}
}
