package minCostMaxFlow_model;


//Min cost max flow algorithm using an adjacency matrix.  If you
//want just regular max flow, setting all edge costs to 1 gives
//running time O(|E|^2 |V|).
//
//Running time: O(min(|V|^2 * totflow, |V|^3 * totcost))
//
//INPUT: cap -- a matrix such that cap[i][j] is the capacity of
//          a directed edge from node i to node j
//
//   cost -- a matrix such that cost[i][j] is the (positive)
//           cost of sending one unit of flow along a 
//           directed edge from node i to node j
//
//   source -- starting node
//   sink -- ending node
//
//OUTPUT: max flow and min cost; the matrix flow will contain
//    the actual flow values (note that unlike in the MaxFlow
//    code, you don't need to ignore negative flow values -- there
//    shouldn't be any)
//
//To use this, create a MinCostMaxFlow object, and call it like this:
//
//MinCostMaxFlow nf;
//int maxflow = nf.getMaxFlow(cap,cost,source,sink);

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import evaluation.SimilarityCompare;
import student.faculty;
import student.student;


public class MinCostMaxFlow {
	private boolean found[],choose[],isRe[];
	private int N,cap[][],flow[][],dad[];
	private double cost[][];
	private double dist[],pi[];

	static final double INF = Integer.MAX_VALUE / 2 - 1;
	
	public boolean search(int source, int sink) { // check if is there any path from source to sink.
		Arrays.fill(found, false); // haven't found anypath from source.
		Arrays.fill(dist, INF);// at first cost from sourse to any node = max
		dist[source] = 0; // of course cost from source to source = 0;

		while (source != N) { //when haven't reach the last node, still go.
			int best = N;
			found[source] = true; // of course found source-the first node
			for (int k = 0; k < N; k++) {
				//if (!isRe[k]) continue;
				if (found[k]) continue; // if node k already found, find the next node
				if (flow[k][source] != 0) { // this is the current flow from source to node k
					double val = dist[source] + pi[source] - pi[k] - cost[k][source];
					if (dist[k] > val) {
						dist[k] = val;
						dad[k] = source;
					}
				}
				if (flow[source][k] < cap[source][k]) {
					double val = dist[source] + pi[source] - pi[k] + cost[source][k];
					if (dist[k] > val) {
						dist[k] = val;
						dad[k] = source;
					}
				}
		
				if (dist[k] < dist[best]) best = k;
			}
			source = best;
		}
		
		for (int k = 0; k < N; k++)
			pi[k] = Math.min(pi[k] + dist[k], INF);
		return found[sink];
	}

	
	

	public boolean[] getMaxFlow(student stu, String namefile) throws IOException {
		this.cap = stu.getCap();
		this.cost = stu.getCost();
		N = cap.length;
		int source = 0;
		int sink= N-1;
		this.cap = stu.getCap();
		this.cost = stu.getCost();
		String msv = stu.getMSV();
		//this.isRe = isRe;
		BufferedWriter bw = new BufferedWriter(new FileWriter("Data/"+namefile+"/"+stu.getNameFile()));
		
		found = new boolean[N];
		choose = new boolean[N];
		flow = new int[N][N];
		dist = new double[N+1];
		dad = new int[N];
		pi = new double[N];
	
//		
//		for (int i = 0; i <cap.length; i++)
//		{
//			for( int j = 0; j <cap.length; j++) System.out.print(cap[i][j] + ",");
//			System.out.println("");
//		}
//		for( int j = 0; j <cost.length; j++) System.out.print(cost[0][j] + ",");
////		//---------------------------------------------------------------------//
//		

		Arrays.fill(choose, false);
		
		int totflow = 0, totcost = 0;
		while (search(source, sink)) {
			double amt = INF;
			for (int x = sink; x != source; x = dad[x])
				amt = Math.min(amt, flow[x][dad[x]] != 0 ? flow[x][dad[x]] :
              cap[dad[x]][x] - flow[dad[x]][x]);
			for (int x = sink; x != source; x = dad[x]) {
				if (flow[x][dad[x]] != 0) {
					flow[x][dad[x]] -= amt;
					totcost -= amt * cost[x][dad[x]];
				} else {
					flow[dad[x]][x] += amt;
					totcost += amt * cost[dad[x]][x];
				}
			}
			totflow += amt;
			
			/*viet vao 1 file
			 * gom co MSV ma mon theo thu tu va bang 0 1
			*/
			choose[dad[dad[sink]]] = true;
		}
//		BufferedReader br = new BufferedReader(new FileReader("data/CNTT/GeneralData/MonBoTro_TuChon_CNTT.csv"));
//	String line;
//	
//	while( (line=br.readLine()) != null){
//		String[] values = line.split(",");
		int re1=0;
		int re2=0;
		List<String> StudiedCourses = stu.getStudiedCourses();
	
		for(int mamon = 1; mamon <= stu.getFal().getnumberOfCourse(); mamon++){
				//Integer.parseInt(values[0]); 
			String ID =stu.getFal().courses.get(mamon-1).getId();
		if(StudiedCourses.contains(ID)) continue;
		if ( choose[mamon] ){
			re1+=stu.getFal().courses.get(mamon-1).getCredit();
			bw.write(stu.getMSV()+","+stu.getFal().courses.get(mamon-1).getId()+","+1+"\n");
		}
		else {
			bw.write(stu.getMSV()+","+stu.getFal().courses.get(mamon-1).getId()+","+0+"\n");
		}
	}
		//--------------------------------------------------------------------------------------------------------------------------
		//System.out.println(stu.getNameFile()+ "/" +stu.getMSV()+ "/"+ re1+"______________"+stu.total_+"AVG = "+stu.getAVG());
		//for(int mamon = 0; mamon <N; mamon++){ System.out.println(mamon + "-" + choose[mamon]);}
		
	bw.close();
	
	//br.close();
	//return new double[]{ totflow, totcost };
	//getMaxFlow(stu.setNewCap(choose));
	return choose.clone();
}
	
	public void getMutipleResult(student stu) throws IOException, CloneNotSupportedException {
				boolean[] choose1 = getMaxFlow(stu,"Result");
				student stu2 = stu.setNewCap(choose1);
				getMaxFlow(stu2, "Result2");
			
	}
	
	public static void run(double a, double b, double c, faculty CNTT, faculty CNTT_CLC ) throws IOException, CloneNotSupportedException
	{
		Scanner avg = new Scanner(new File("data/Score/AVG.csv"));
		double[] AVG = new double[500];
		int index_avg = 0;
		while(avg.hasNext()) 
		{
			AVG[index_avg++]=Double.parseDouble(avg.nextLine().split(",")[1]);
		}
		
		Scanner inter = new Scanner(new File("data/student_info/so_thich_dinh_huong"));
		int studentNum = 20;
		for(int i = 1; i <= studentNum ; i++)
			{
			//if(i==19)continue;
			double [] interest = new double[4];
			String []inter_ = inter.nextLine().split(",");
			int target = Integer.parseInt(inter_[1]);
			for(int j = 0; j<4;j++)
			{
				interest[j] = Double.parseDouble(inter_[j+2]);
				
			}
			student stu ;
			if(i<12)   stu = new student(1,CNTT,"#"+i+".csv",AVG[i-1],target,interest);
			else stu = new student(1,CNTT_CLC,"#"+i+".csv",AVG[i-1],target,interest);
				//s1.get_score_CF(1);
			stu.get_cost_combine(a,b,c);
				MinCostMaxFlow mcmf = new MinCostMaxFlow();
				mcmf.getMutipleResult(stu);
				
			}
		double SUM1 = 0.0; 
			for(int i = 1; i <=studentNum ; i++)
			{
				if(i==19)continue;
				SimilarityCompare eva = new SimilarityCompare();
				SUM1+=eva.Caculation(i,"Result");
				//System.out.println("/ " + eva.Caculation(i) );
			}
			
		//System.out.println("CF / Predict Score / Target / Interest : " + a +" --- "+ b + " --- " + c + " --- "+ d + "    = "
		//	System.out.println("----------------------------------------------RESULT 1 : "+SUM1/19);
			
			double SUM2 = 0.0; 
			for(int i = 1; i <=studentNum ; i++)
			{
				if(i==19)continue;
				SimilarityCompare eva = new SimilarityCompare();
				SUM2+=eva.Caculation(i,"Result2");
				//System.out.println("/ " + eva.Caculation(i) );
			}
			//System.out.println("----------------------------------------------RESULT 2 : "+SUM2/19);
			
			
			System.out.println(SUM1/19);

    
	}
	
	public static void main(String[] args) throws IOException, CloneNotSupportedException {
	// TODO Auto-generated method stub
		faculty CNTT = new faculty("Cong Nghe Thong Tin", "data/faculty/CNTT_2012");
		faculty CNTT_CLC = new faculty("Cong Nghe Thong Tin", "data/faculty/CNTT_CLC_2012");
		faculty KHMT = new faculty("Khoa Hoc May Tinh","data/faculty/KHMT.csv");
/* 	
 * evaluation for 1 tudent 
 
 		student stu = new student(1,CNTT,"#"+10+".csv");
		//s1.get_score_CF(1);
		MinCostMaxFlow mcmf = new MinCostMaxFlow();
		mcmf.getMaxFlow(stu);
		SimilarityCompare eva = new SimilarityCompare();
		eva.Caculation(10);
		*/
		
		// read Diem TB 'AVG' cho tung sinh vien
		run(1,0,0,CNTT,CNTT_CLC);
		run(0,1,0,CNTT,CNTT_CLC);
		run(0,0,1,CNTT,CNTT_CLC);
		run(0.33,0.33,0.33,CNTT,CNTT_CLC);
		run(0.50,0.50,0.00,CNTT,CNTT_CLC);
		run(0.00,0.50,0.50,CNTT,CNTT_CLC);
		run(0.50,0.00,0.50,CNTT,CNTT_CLC);
		run(0.60,0.20,0.20,CNTT,CNTT_CLC);
		run(0.20,0.60,0.20,CNTT,CNTT_CLC);
		run(0.20,0.20,0.60,CNTT,CNTT_CLC);
		run(0.80,0.10,0.10,CNTT,CNTT_CLC);
		run(0.40,0.40,0.20,CNTT,CNTT_CLC);
		run(0.40,0.20,0.40,CNTT,CNTT_CLC);
		run(0.20,0.40,0.40,CNTT,CNTT_CLC);
		run(0.45,0.10,0.45,CNTT,CNTT_CLC);
		run(0.90,0.05,0.05,CNTT,CNTT_CLC);
		run(0.70,0.10,0.20,CNTT,CNTT_CLC);
		run(0.475,0.05,0.475,CNTT,CNTT_CLC);
		run(0.495,0.01,0.495,CNTT,CNTT_CLC);







		
	//	int i = 1;
/*	// -------------------------evaluation for  N = studentNum (sinh vien )---------------------------------------//
		int studentNum = 11;
			for(int i = 1; i <= studentNum ; i++)
				{
				double [] interest = new double[4];
				String []inter_ = inter.nextLine().split(",");
				int target = Integer.parseInt(inter_[1]);
				for(int j = 0; j<4;j++)
				{
					interest[j] = Double.parseDouble(inter_[j+2]);
				}student stu = new student(1,KHMT,"#"+i+".csv",AVG[i-1],target,interest);
					//s1.get_score_CF(1);
				stu.get_cost_combine(0.25, 0.25, 0.25, 0.25);
					MinCostMaxFlow mcmf = new MinCostMaxFlow();
					mcmf.getMaxFlow(stu);
				}
			double SUM = 0.0; 
				for(int i = 1; i <=studentNum ; i++)
				{
					SimilarityCompare eva = new SimilarityCompare();
					SUM+=eva.Caculation(i);
				}
			System.out.println(SUM/(double)studentNum);
	// -------------------------for eva---------------------------------------//
*/		
/*	 int i =2;
		student stu = new student(1,KHMT,"#"+i+".csv",AVG[i-1],target,interest);
		//s1.get_score_CF(1);
		MinCostMaxFlow mcmf = new MinCostMaxFlow();
		mcmf.getMaxFlow(stu);
		SimilarityCompare eva = new SimilarityCompare();
		System.out.println(eva.Caculation(i));*/
	}

}
