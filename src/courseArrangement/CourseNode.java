/**
 * 
 */
package courseArrangement;

import java.util.ArrayList;

/**
 * @author Kin_meow
 *
 */
public class CourseNode {

	String TextId ;
	ArrayList<CourseNode> parents;
	ArrayList<CourseNode> children;
	int maxIn;
	int maxOut;
	int numOfPrerequisite;
	int position;
	String[] prqs;
	int totalUnderNode;
	
	public CourseNode(String textCode_ ,int position_,String[] prqs_) {
		parents = new ArrayList<CourseNode>();
		children = new ArrayList<CourseNode>();
		TextId = textCode_;
		position = position_;
		prqs = prqs_;
		maxIn = -1;
		maxOut = -1;
		totalUnderNode = 0;
		setNumOfPrequisite();
		
	}

	public void addParent(CourseNode parent){
		parents.add(parent);
	}
	
	public void addChild(CourseNode child) {
		children.add(child);
		
	}
	
	public void setPrqs(String[] p) {
		prqs = p;
	}
	
	public void setNumOfPrequisite()
	{
		if(prqs[0].equalsIgnoreCase("0")) numOfPrerequisite = 0;
		else numOfPrerequisite = prqs.length;
	}
	public void showProperty() {
		System.out.println("\n\nMM : " + TextId);
		for(String str : prqs) System.out.print( str + "---");
		System.out.print("parent : ");
		for (CourseNode course : parents) {
			System.out.print( course.TextId + " ");
		}
		System.out.print("\nchildren : ");
		for (CourseNode course : children) {
			System.out.print( course.TextId + " ");
		}
		System.out.println("\nNumOfPre : " + numOfPrerequisite);
		System.out.println("Position : "+position);
		System.out.println("total under node : " + totalUnderNode);
		System.out.println("MaxOut : " + maxOut);
		System.out.println("MaxIn : "+maxIn);
	}

}
