package CommitFeature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class CSV_handler {

	public List<String[]> getContentFromFile(File file) throws IOException {
		List<String[]> content = new ArrayList<String[]>();
		;
		CsvListReader reader = new CsvListReader(new FileReader(file),
				CsvPreference.EXCEL_PREFERENCE);
		reader.getCSVHeader(true);
		List<String> line = new ArrayList<String>();
		while ((line = reader.read()) != null) {
			content.add(line.toArray(new String[] {}));
		}
		return content;
	}

	public List<String[]> getDetailFromFile(File file) throws IOException {
		List<String[]> content = new ArrayList<String[]>();
		;
		CsvListReader reader = new CsvListReader(new FileReader(file),
				CsvPreference.EXCEL_PREFERENCE);
		String[] header = reader.getCSVHeader(true);
		content.add(header);
		List<String> line = new ArrayList<String>();
		while ((line = reader.read()) != null) {
			content.add(line.toArray(new String[] {}));
		}
		return content;
	}

	public String[] getHeaderFromFile(File file) throws IOException {
		CsvListReader reader = new CsvListReader(new FileReader(file),
				CsvPreference.EXCEL_PREFERENCE);
		return reader.getCSVHeader(true);
	}

	public void writeToCsv(File file, String[] header, List<String[]> content)
			throws IOException {
		CsvListWriter writer = new CsvListWriter(new FileWriter(file),
				CsvPreference.EXCEL_PREFERENCE);
		writer.writeHeader(header);
		for (String[] str : content) {
			writer.write(str);
		}
		writer.close();
	}

	public void writeCommitsMsgsToCsv(File file, 
			List<Commit> commits) throws IOException {
		List<String[]> content = new ArrayList<String[]>();
		String[] header = 	{ "id", "commitId","committer", "label", "msg" };
		for (int i = 0; i < commits.size(); i++) {
			Commit thisCommit = commits.get(i);
			String[] thisContent = new String[5];
			thisContent[0] = "\"" + Integer.toString(thisCommit.getId()) + "\""; // index
			thisContent[1] = "\"" + thisCommit.getCommitid() + "\""; // commitId	
			thisContent[2] = "\"" + thisCommit.getCommitter() + "\""; // committer
			thisContent[3] = "\"" + thisCommit.getLabel() + "\""; // label, //
			thisContent[4] = "\""+thisCommit.getMsg()+"\"";
			content.add(thisContent);
		}
		writeToCsv(file, header, content);
	}
	
	public void writeCommitsMsgsToCsv2(File file, 
			List<Commit> reverted, List<Commit> reverting) throws IOException {
		if(reverted.size() != reverting.size())
		{
			
		}
		else 
		{
			List<String[]> content = new ArrayList<String[]>();
			String[] header = 	{ "id", "commitId","committer", "label", "msg",
								  "id", "commitId","committer", "label", "msg" };
			for (int i = 0; i < reverted.size(); i++) {
				Commit RevertedCommit = reverted.get(i);
				Commit RevertingCommit = reverting.get(i);
				String[] thisContent = new String[10];
				thisContent[0] = "\"" + Integer.toString(RevertedCommit.getId()) + "\""; // index
				thisContent[1] = "\"" + RevertedCommit.getCommitid() + "\""; // commitId	
				thisContent[2] = "\"" + RevertedCommit.getCommitter() + "\""; // committer
				thisContent[3] = "\"" + RevertedCommit.getLabel() + "\""; // label, //
				thisContent[4] = "\""+RevertedCommit.getMsg()+"\"";
				thisContent[5] = "\"" + Integer.toString(RevertingCommit.getId()) + "\""; // index
				thisContent[6] = "\"" + RevertingCommit.getCommitid() + "\""; // commitId	
				thisContent[7] = "\"" + RevertingCommit.getCommitter() + "\""; // committer
				thisContent[8] = "\"" + RevertingCommit.getLabel() + "\""; // label, //
				thisContent[9] = "\""+RevertingCommit.getMsg()+"\"";
				content.add(thisContent);
			}
			writeToCsv(file, header, content);
		}
	}
	
	public void writeCommitsMsgsToCsv3(File file, 
			List<String> reverted, List<String> reverting) throws IOException {
		if(reverted.size() != reverting.size())
		{
			
		}
		else 
		{
			List<String[]> content = new ArrayList<String[]>();
			String[] header = 	{ "id","id"};
			for (int i = 0; i < reverted.size(); i++) {
				String RevertedCommit = reverted.get(i);
				String RevertingCommit = reverting.get(i);
				String[] thisContent = new String[10];
				thisContent[0] = "\"" + RevertedCommit + "\""; // index
				thisContent[1] = "\"" + RevertingCommit + "\""; // commitId	

				content.add(thisContent);
			}
			writeToCsv(file, header, content);
		}
	}

	public void writeCommitsToCsvWithoutMsg(File file,
			List<Commit> commits) throws IOException {
		// TODO Auto-generated method stub
		List<String[]> content = new ArrayList<String[]>();
		String[] header = 	{ "AddFiles", "ModifyFiles","DeleteFiles", "RenameFiles", "CopyFiles","LowLines","MediumLines",
				"HighLines","CrucialLines","DelLines", "AddLines","SubSystem","EXP","NDEV",
				"NF", "ND","NUC", "Entropy", "Conf","msg_length", "has_bug", "has_feature","has_improve",
				"has_document", "has_refactor","language_num", "language_num", 
				"segs_added_num", "segs_deleted_num", "segs_update_num", "changes_files_modified",
				"recent_change_num", "time_before_reverted", "Label"};
		
		
		for (int i = 0; i < commits.size(); i++) {
			Commit thisCommit = commits.get(i);
			//notice that set the length of the array
			String[] thisContent = new String[34];
			
			thisContent[0] = Double.toString(logNormalization((thisCommit.getAddFiles()))); 
			thisContent[1] = Double.toString(logNormalization((thisCommit.getModifyFiles())));
			thisContent[2] = Double.toString(logNormalization((thisCommit.getDeleteFiles())));
			thisContent[3] = Double.toString(logNormalization((thisCommit.getRenameFiles()))); 
			thisContent[4] = Double.toString(logNormalization((thisCommit.getCopyFiles())));
			thisContent[5] = Double.toString(logNormalization((thisCommit.getNumberOfLow()))); 
			thisContent[6] = Double.toString(logNormalization((thisCommit.getNumberOfMedium()))); 
			thisContent[7] = Double.toString(logNormalization((thisCommit.getNumberOfHigh())));
			thisContent[8] = Double.toString(logNormalization((thisCommit.getNumberOfCrucial())));
			thisContent[9] = Double.toString(logNormalization((thisCommit.getDellines())));
			thisContent[10] = Double.toString(logNormalization((thisCommit.getAddlines())));
			thisContent[11] = Double.toString(logNormalization((thisCommit.getSubSystemNum())));
			thisContent[12] = Double.toString(logNormalization((thisCommit.getEXP())));
			thisContent[13] = Double.toString(logNormalization((thisCommit.getNDEV())));
			thisContent[14] = Double.toString(logNormalization((thisCommit.getNF())));
			thisContent[15] = Double.toString(logNormalization((thisCommit.getND())));
			thisContent[16] = Double.toString(logNormalization((thisCommit.getNUC())));
			thisContent[17] = Double.toString(logNormalization((thisCommit.getEntropy())));
			thisContent[18] = Double.toString(logNormalization((thisCommit.getConf())));
			thisContent[19] = Double.toString(logNormalization((thisCommit.getMsg_length())));
			thisContent[20] = Double.toString(((thisCommit.getTextHasBug())));
			thisContent[21] = Double.toString(((thisCommit.getTestHasFeature())));
			thisContent[22] = Double.toString(((thisCommit.getTextHasImprove())));
			thisContent[23] = Double.toString(((thisCommit.getTextHasDocument())));
			thisContent[24] = Double.toString(((thisCommit.getTextHasRefactor())));
			thisContent[25] = Double.toString(logNormalization((thisCommit.getLanguage_num())));
			thisContent[26] = Double.toString(logNormalization((thisCommit.getFile_type_num())));
			thisContent[27] = Double.toString(logNormalization((thisCommit.getSegs_added_num())));
			thisContent[28] = Double.toString(logNormalization((thisCommit.getSegs_deleted_num())));
			thisContent[29] = Double.toString(logNormalization((thisCommit.getSegs_update_num())));
			thisContent[30] = Double.toString(logNormalization((thisCommit.getChanges_files_modified())));
			thisContent[31] = Double.toString(logNormalization((thisCommit.getRecent_change_num())));
			thisContent[32] = Double.toString(((thisCommit.getTime_before_reverted())));
			thisContent[33] =  Integer.toString(thisCommit.getLabel());

			content.add(thisContent);
		}
		writeToCsv(file, header, content);
	}
	
	public double logNormalization (double metricValue)
	{
		return java.lang.Math.log(metricValue+1);
	}
	

	public void writeContentToCsv(File file, List<String[]> content)
			throws IOException {
		CsvListWriter writer = new CsvListWriter(new FileWriter(file),
				CsvPreference.EXCEL_PREFERENCE);
		for (String[] str : content) {
			writer.write(str);
		}
		writer.close();
	}


	public void writeHeaderToCsv(File file, String[] header) throws IOException {
		CsvListWriter writer = new CsvListWriter(new FileWriter(file),
				CsvPreference.EXCEL_PREFERENCE);
		writer.writeHeader(header);
		writer.close();
	}

	public String getTxtContent(String url) throws IOException {
		String outputText = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(url), "UTF-8")); // .............
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			outputText += line;
		}
		br.close();
		return outputText;
	}

//	public static void main(String[] args) throws IOException {
//		CSV_handler operateCsv = new CSV_handler();
//		File file = new File("D://firebird-input2.csv");
//		// List<String[]> content = operateCsv.getDetailFromFile(file);
//		// String[] header = operateCsv.getHeaderFromFile(file);
//		// for(String[] str : content) {
//		// for(int i = 0; i < str.length; i++) {
//		// System.out.println(str[i]);
//		// }
//		// }
//		// File file1 = new File("D:/����-����.csv");
//		// operateCsv.writeToCsv(file1, header, content);
//		// operateCsv.writeHeaderToCsv(file1, header);
//		// operateCsv.writeContentToCsv(file1, content);
//		String[] headers = { "project", "number", "content" };
//		List<String[]> contents = new ArrayList<String[]>();
//
//		String root = "D:";
//		String project = "firebird";
//
//		String[] urls = new String[10727]; //
//		for (int j = 0; j < 10727; j++) {
//			String[] messages = new String[3];
//			urls[j] = root + "\\input" + (j + 5) + ".txt";
//			messages[0] = project;
//			messages[1] = "" + (j + 1);
//			messages[2] = operateCsv.getTxtContent(urls[j]);
//			if (j == 6737)
//				System.out.println(messages[2]);
//			contents.add(messages);
//		}
//
//		operateCsv.writeToCsv(file, headers, contents);
//
//	}
//

}