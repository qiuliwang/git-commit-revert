package CommitFeature;

import java.io.File;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.SignificanceLevel;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;

public class ChangeDistillerClass {
	
	
	public List<SourceCodeChange> getFineGrainedChange (String left, String right){

	
	File leftFile = new File(left);
	File rightFile = new File(right);

	FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);

	try {
	    distiller.extractClassifiedSourceCodeChanges(leftFile, rightFile);
	    
	} catch(Exception e) {
	    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
	       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
	       attach the full stack trace along with the two files that you tried to distill. */
	    System.err.println("Warning: error while change distilling. " + e.getMessage());
	}

	List<SourceCodeChange> changes = distiller.getSourceCodeChanges();	
		
	//System.out.println("Total changes:" + changes.size());
	//if(changes != null) {
		//int index=0;
	   // for(SourceCodeChange change : changes) {
	   // 	index=index+1;
	    	//System.out.println("change " +index+ " summary:");
	    //	String changeStr = change.toString();
	    //	System.out.println("1. changedCode:"+changeStr);
	    //	ChangeType thisType = change.getChangeType();
	    //	System.out.println("2. type:"+thisType.toString());
	    //	SourceCodeEntity thisEntity = change.getChangedEntity();
	    //	System.out.println("3. entityType:"+thisEntity.getType().toString());		    	
	    //	String thisLabel = change.getLabel();
	    //	System.out.println("4. label:"+thisLabel.toString());
	    //	SignificanceLevel thisLevel = change.getSignificanceLevel();
	    //	System.out.println("5. level:"+thisLevel.toString());
	    //	SourceCodeEntity parentEntity = change.getParentEntity();
	    			        // see Javadocs for more information
	    //}
	//}
	return changes;
}
}
