/**
 * 
 */
package courseArrangement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import javax.activation.UnsupportedDataTypeException;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;
import javax.swing.undo.UndoableEditSupport;

/**
 * @author Kin_meow
 *
 */
public class MyList {

	ArrayList<CourseNode> myList;
	Map <String, Integer > mapTextIdToIndex;
	int[] currentMaxIn;
	public MyList(String nameFile) throws FileNotFoundException {
		myList = new ArrayList<CourseNode>();
		mapTextIdToIndex = new HashMap<String,Integer>();
		Scanner input = new Scanner(new File("Data/Result_to_arrangement/#"+nameFile+".txt"));
		int count = 0;
		while(input.hasNext()){
			String[] eachLine = input.nextLine().split(",");
			String textId = eachLine[0];
			int position = Integer.parseInt(eachLine[1]);
			//System.out.println(textId + "/"+ eachLine[2]);
			String[] prqs = eachLine[2].split(";");
			CourseNode courseNode = new CourseNode(textId, position,prqs);
			
			myList.add(courseNode);
			mapTextIdToIndex.put(textId,count++);
			
			
		}
		
		currentMaxIn = new int[myList.size()];
		setConnect();
		setTotalUnderNode();
		setMaxOut();
		setMaxIn();
		
		
	}
	
	public void setConnect() {
		for(CourseNode cNode : myList){
			if (cNode.numOfPrerequisite == 0 ) continue;
			for(String pre : cNode.prqs){
				//System.out.println(cNode.TextId + "OK");
				CourseNode parent = myList.get(mapTextIdToIndex.get(pre));
				cNode.addParent(parent);
				parent.addChild(cNode);						
			}

			//System.out.println(cNode.TextId + " OK ");
		}
	}
	
	public void setTotalUnderNode() {
		for(CourseNode cNode : myList){
			cNode.totalUnderNode = countTotalUnderNode(cNode);
		}
	}
	
	public int countTotalUnderNode(CourseNode courseNode) {
		if(courseNode.children.size()==0) return 0;
		int sum = courseNode.children.size();
		for(CourseNode cNode : courseNode.children){
			sum += countTotalUnderNode(cNode);
		}
		return sum;
	}
	
	public int countMaxOut(CourseNode courseNode){
		if(courseNode.children.size()==0) return 0;
		
		int max = 0;
		for(CourseNode cNode : courseNode.children){
			int count = countMaxOut(cNode)+1;
			if(count > max) max = count;
		}
		return max;
	}
	
	public void setMaxOut(){
		for(CourseNode courseNode : myList){
			courseNode.maxOut = countMaxOut(courseNode);
		}
	}
	
	public int countMaxIn(CourseNode courseNode){
		if(courseNode.parents.size()==0) return 0;
		
		int max = 0;
		for(CourseNode cNode : courseNode.parents){
			int count = countMaxIn(cNode)+1;
			if(count > max) max = count;
		}
		return max;
	}
	
	public void setMaxIn(){
		for(int i = 0; i < myList.size(); i++){
			CourseNode courseNode = myList.get(i);
			currentMaxIn[i] = countMaxIn(courseNode);
			courseNode.maxIn= currentMaxIn[i];
			
		}
	}
	
	public void showList() {
		for(CourseNode courseNode : myList){
			courseNode.showProperty();
		}
	}

	
	public void remove(int index){
		CourseNode currentNode = myList.get(index);
		discountMaxIn(currentNode);
		updateCurrentMaxIn();
	}
	
	public void discountMaxIn(CourseNode courseNode) {
		if(courseNode == null ) return;
		courseNode.maxIn--;
		for(CourseNode cNode : courseNode.children){
			if(cNode.maxIn == courseNode.maxIn+2) discountMaxIn(cNode);
		}
		
	}
	
	public void updateCurrentMaxIn(){
		for(int i = 0 ; i < currentMaxIn.length; i++){
			currentMaxIn[i] = myList.get(i).maxIn;
			//System.out.println(myList.get(i).TextId + "--> " + currentMaxIn[i]);
		}
	}
	
	public int[] getAvailableNodes() {
		int[] avList = new int[100];
		int count = 0;
		for(int i = 0 ; i < currentMaxIn.length ; i ++)
		{
			if(currentMaxIn[i]==0) {
				avList[count++] = i;
			}
		}
		return Arrays.copyOfRange(avList,0,count);
	}
	
	public int[] sortAvailbleNodes(int [] avList) {
	//-----------------------------------------------------------------------------------------------------------------			
		for(int i = 0 ; i < avList.length-1; i++){
			CourseNode node1 = myList.get(avList[i]);
	//-------------------------------------------------------------------------		
			for(int j = i+1; j < avList.length; j++){
				CourseNode node2 = myList.get(avList[j]);
				if(node2.maxIn > node1.maxIn)
				{
					int tem = avList[i];
					avList[i] = avList[j];
					avList[j] = tem;
				}
				
				else if(node2.maxIn == node1.maxOut)
				{
					if(node2.totalUnderNode > node1.totalUnderNode)
					{
						int tem = avList[i];
						avList[i] = avList[j];
						avList[j] = tem;
					}
					else if(node2.position < node1.position)
					{
						int tem = avList[i];
						avList[i] = avList[j];
						avList[j] = tem;
					}
				}
			}
	//------------------------------------------------------------------------------------			
		}
	//-----------------------------------------------------------------------------------------------------------------		
	return avList;
	}
	
	CourseNode[] getNextSemeter(int numOfCourse)
	{
		CourseNode[] myNextSem = new CourseNode[numOfCourse];
		int[] avList = sortAvailbleNodes(getAvailableNodes());
		for(int i = 0 ; i < numOfCourse; i++)
		{
			myNextSem[i]= myList.get(avList[i]);
			remove(avList[i]);
			
		}
		
		return myNextSem;
	}
	
	public static void showElement(CourseNode[] list)
	{
		for(CourseNode courseNode : list)
		{
			//courseNode.showProperty();
			System.out.println(courseNode.TextId);;
		}
	}
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		MyList mList = new MyList("1");
		mList.showList();
		//mList.remove(4);
//		System.out.println("NEXT SEMETER : _____________________________________________________________");
//		showElement(mList.getNextSemeter(5));
//		
		//System.out.println("AFTER : --------------------------------------------------------------");
		//mList.showList();
		
		int numOfSemeter = 7;
		for(int i = 1 ; i <= numOfSemeter ; i ++)
		{
			System.out.println("SEMETER " + i + " : _____________________________________________________________");
			showElement(mList.getNextSemeter(5));	
		}

	}

}
