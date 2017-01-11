//package CommitFeature;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import org.eclipse.jgit.api.Git;
//
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.api.errors.NoHeadException;
//
///*
// * calculate the distance between two logs 
// */
//
//public class analysis
//{
//	public static List<String> logList;
//	public static List<String> diffList;
//	public static List<String> fileList;
//	public static List<Integer> ages;
//	public static Git git;
//
//	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
//	{
//
//	}
//	
//	public analysis(Git ogit, List<String> Logs)
//	{
//		logList = Logs;
//		ages = new ArrayList<Integer>();
//		git = ogit;
//	}
//	
//	public void getAllInfo()
//	{
//		JgitDiff dif = new JgitDiff(git);
//		dif.getInfo(logList.get(0), logList.get(1));
//		fileList = dif.getFileList();
//		
//		System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
//		for(int t = 0; t < fileList.size(); t ++)
//		{
//			List<String> cpFile = new ArrayList<String>();
//			cpFile.add(fileList.get(t));
//			System.out.println(cpFile.get(0));
//
//			for(int i = 1; i < logList.size() - 1; i ++)	
//			{
//				System.out.println(i);
//				List<String> localFiles = new ArrayList<String>();
//				dif.getInfo(logList.get(i), logList.get(i + 1));
//				localFiles = dif.getFileList();
//				System.out.print("\n");
//				if(localFiles.retainAll(cpFile))
//				{
//					System.out.println("have the same file log");
//					ages.add(i);
//					System.out.println("distance between two changes:  "+i);
//					System.out.println("@@@@@@@@@@@@@@@@@@@@@");
//
//					break;
//				}
//			}
//		}
//	}
//}