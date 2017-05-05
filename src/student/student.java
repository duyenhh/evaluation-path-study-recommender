/**
 * 
 */
package student;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import evaluation.SimilarityCompare;
import minCostMaxFlow_model.MinCostMaxFlow;

public class student {

	/**
	 * 
	 */
	private String name_file;	// ten file chua thong tin cua sinh vien
	private String MSV; 		// Ma Sinh Vien
	private int yearSchool;		// nam hoc
	private faculty fal;		// chuyen nganh ( CNTT/KHMT/HTTT/M-TT)
	private int target;			// so thu tu cua target 
	private double[] interest;		// vector so thich -- Toan LapTrinh TiengAnh HocThuoc o muc do bao nhieu theo thang tu 1-5
	private ArrayList<String> StudiedCourses = new ArrayList<String>();	// cac mon da hoc 
	private double[][] cost ;	// mang cost de cho vao mo hinh MinCost-MaxFlow ---- value = [0,1]
	private int[][] cap ;		// mang cap de cho vao mo hinh MinCost-MaxFlow ---- value = [0,1]
	public int total_=0;		// tong so tin chi tich luy sau khi duoc  goi y ( chi ung de check ) 
	private double current_avg;	// diem trung binh cac mon da hoc
	private int NumberOfCourses;// tong so mon hoc tu chon trong khung chuong trinh
	
	
	/**
	 * khoi tao student tu cac thong tin
	 * @param yearSchool_ : sinh vien hoc het nam thu may ?
	 * @param fal_ : chuyen nganh ?
	 * @param name_file_ 
	 * @param current_avg_
	 * @throws IOException
	 * 
	 */
	public student(student stu) {
		this.cost = stu.cost;
		this.cap = stu.cap;
		this.name_file = stu.name_file;
		this.MSV = stu.MSV;
		this.yearSchool = stu.yearSchool;
		this.fal = stu.fal;
		this.target = stu.target;
		this.interest = stu.interest;
		this.StudiedCourses = stu.StudiedCourses;
		this.total_ = 0;
		this.current_avg = stu.current_avg;
		this.NumberOfCourses = stu.NumberOfCourses;
	}
	public student(int yearSchool_, faculty fal_, String name_file_,double current_avg_,int tar,double[] inter) throws IOException {
		name_file =  name_file_;
		yearSchool = yearSchool_;
		fal = fal_;
		target=tar;
		interest = inter;
		current_avg=current_avg_;
		NumberOfCourses=fal.numberOfCourse;
		cost = new double[fal.getN()][fal.getN()];
		//	fill mang cost bang gia tri 1
		for(int i = 0; i < cost.length;i++)
		Arrays.fill(cost[i], 1.0);
		//	fill mang cost[][]  = cost[][] tu khung cua chuyen nganh tuong ung 
		cap = fal.CapGenaral();
		//	dua vao du lieu cua tung sinh vien -> set cap tu node ( requirements ) den node ( sink) ...show more
		//set_cap_for_test_data() ;
		cap_learntCourse();
		//	combine type ?
		//get_cost_combine(0.0, 0.0,1.0,0.0);
		//get_cost_combine_mutil();
		/*-----------------------------for checking cap[][] & cost[][]-------------------------------*/
//				 for(int h = 0;h<cost.length;h++)
//					{
//						for(int k = 0; k < cost[0].length;k++) System.out.print(cost[h][k] + "     ");
//						System.out.println(" ");
//					}
//				 System.out.println(name_file);
//				 for(int h = 0;h<cap.length;h++)
//					{
//						for(int k = 0; k < cap[0].length;k++) System.out.print(cap[h][k] + "     ");
//						System.out.println(" ");
//					}
		/*--------------------------------------------------------------------------------------------*/
		
	}
	
	/**
	 * @method set_cap_for_test_data()
	 * @purpose dua vao du lieu that (real data cua tung sinh vien ) 
	 * -> set cap[][] tu node ( requirements ) ---->  node ( sink) = SUM ( so tin chi cac mon da hoc cua require nay)
	 * @throws FileNotFoundException
	 */
	public void set_cap_for_test_data() throws FileNotFoundException
	{
		Scanner input = new Scanner(new FileReader("data/student_info/real_data/"+name_file));

		 while ( input.hasNext())
		 {
			 //	doc tung dong theo dinh dang "MSV,courseId,0/1"
			 String[] line = input.nextLine().split(",");
			 String id_course = line[1];
			 int is_learnt = Integer.parseInt(line[2]);
			 //	bo qua neu mon nay khong duoc hoc
			 if(is_learnt == 0) continue;
			 // var index
			 // xac dinh index (STT) cua mon hoc co courseId tren trong mang cap[][] 
			 int index = (int) fal.Map_ID_STT.get(id_course);
			 // xác định course dựa trên index và arrayList "courses" của mỗi faculty
			 course c = fal.courses.get(index-1); 
			 int credit = c.getCredit(); //số tín chỉ
			 // xác định index ràng buộc của course này trong cap[][] và cost[][]
			 // require_index = Tong so courses + ID cua require do 
			 int require_index = fal.numberOfCourse+ c.getRequirement_ID();
			 //cap từ require_index ---> (sink) đc tang mot luong = số tc của các môn đã học.
			 cap[require_index][cap.length-1]+=credit; 
			
			 
		 }
		 //for checking total
		 for(int i = 1; i<=fal.numberOfRequire; i++)
		 {

			 System.out.println(total_);
		 }
	}
	
	/**
	 * @method cap_learntCourse()
	 * @purpose dua vao du lieu cac mon da hoc cua sinh vien ( learnt_course ) 
	 * -> set cap[][] tu node(source) ---> node ( course ) = 0 neu mon nay da duoc hoc.
	 * @throws FileNotFoundException
	 */
	public void cap_learntCourse() throws FileNotFoundException
	{
		Scanner input = new Scanner(new FileReader("data/student_info/learnt_course/"+name_file));
		 
		 while ( input.hasNext())
		 {
			// doc tung dong voi dinh dang "MSV,null/ma mon,ma chu cua mon"
			String[] line = input.nextLine().split(",");
			MSV = line[0];
			// neu ma mon ( Id ) = null -> mon nay khong nam trong khung chuong
			// trinh cua khoa fal --> bo qua
			if (line[1].equals("null")) continue;
			// xu ly ma mon de xac dinh vi tri (STT) cua mon trong mo hinh
			// MinCost_MaxFlow
			String id_course = line[2];
			StudiedCourses.add(id_course);
			try
				{
				// neu khong tim thay trong fal.(Map_ID_STT) -> day khong phai la mon tu chon ---> catch exception 
				int index = (int) fal.Map_ID_STT.get(id_course); // STT của course này trong cap[][] và cost[][]
				// System.out.println(id_course + "---> " + index + "--- OK");
				// xác định course dựa trên STT và arrayList "courses" của mỗi faculty 
				course c = fal.courses.get(index-1); 
				int credit = c.getCredit(); //số tín chỉ
				// xác định index ràng buộc của course này trong cap[][] và cost[][]
				int require_index = fal.numberOfCourse+ c.getRequirement_ID(); 
				//set cap từ nguồn ( source đến các môn đã học bằng 0)
				cap[0][index] = 0; 
				// caution !!! 
				cap[require_index][cap.length-1]-=credit; //cap từ require_index đến đích (sink) đc giảm một lượng = số tc của các môn đã học.
				}
			catch (Exception e) {
				// e.printStackTrace();
				// System.out.println(" khong tim thay trong fal.(Map_ID_STT) de
				// set tu nguon den = 0");
				continue;
			}
		 }
	}

	 /**
	 * @method get_score_CF(double heso)
	 * @purpose tra ve mang double[] la ket qua score ( lay tu file ) cua phuong phap CF : matrix factoryzation cho cac mon chua hoc cua tung sinh vien
	 * @param heso : he so ( de combine cac rang buoc)
	 * @return double [NumberOfCourses+1]
	 * @throws IOException
	 */
	public double[] get_score_CF() throws IOException
	{
		// khoi tao Array : 'CF_score[]' && len = [NumberOfCourses+1]
		double[] CF_score = new double[NumberOfCourses + 1];
		// Doc tung dong trong file
		Scanner input_CF = new Scanner(new FileReader("data/Score/CF/" + name_file));
		while (input_CF.hasNext()) {
			// each 'line' co dinh dang : (MSV, id_course, ket qua du doan)
			String[] line = input_CF.nextLine().split(",");
			String id_course = line[1];
			double CF = Double.parseDouble(line[2]);
			// Tim 'index' tuong ung cua tung mon , luc nay 'CF_score[index]' co
			// gia tri <-> cap[0][index]
			try {
				int index = (int) fal.Map_ID_STT.get(id_course);
				CF_score[index] = CF;

			} catch (Exception e) {
				// System.out.println("## "+ id_course);
			}

		}
		return CF_score;

	}
	 /**
	 * @method get_score_Predict(double heso) 
	 * @purpose return double[] as predict score results of 'not learn yet courses' ( take from file ) by model 'score prediction' for each student
	 * @param heso : he so ( de combine cac rang buoc)
	 * @return double [NumberOfCourses+1]
	 * @throws IOException
	 */
	public double[] get_score_Predict() throws IOException {
		Scanner input_Predict = new Scanner(new FileReader("data/Score/Predict_Score/" + name_file));
		double[] Pre_score = new double[NumberOfCourses + 1];
		// fill array 'Pre_score' = Diem trung binh
		Arrays.fill(Pre_score, (current_avg / 10));
		while (input_Predict.hasNext()) {
			String[] line = input_Predict.nextLine().split(",");
			String id_course = line[1];
			double Pre = Double.parseDouble(line[2]) / 10;
			try {
				int index = (int) fal.Map_ID_STT.get(id_course);
				Pre_score[index] = Pre;
			} catch (Exception e) {
				// System.out.println("predict : " + id_course);
				continue;
				// TODO: handle exception
			}
		}
		return Pre_score;
	}
	 
	 /**
	 * @purpose set score target base on student.'target'
	 * @return
	 * @throws FileNotFoundException
	 */
	public double[] get_score_target() throws FileNotFoundException
	 {
		 double[][] target_tem = fal.getTarget();
		 double[] Target_score=new double[NumberOfCourses+1];
		 for(int i = 1; i < NumberOfCourses+1;i++)
		 {
			 Target_score[i]=target_tem[target-1][i-1]*0.2;
		 }
		 return Target_score;
	 }
	
	public double[] get_score_interest() throws FileNotFoundException {
		double[][] inter = fal.get_interest();
		double[] interest_score = new double[NumberOfCourses+1];
		
			for(int i = 0; i < NumberOfCourses;i++)
			 {
				for(int contribute = 0; contribute <4; contribute++)
				 {
					interest_score[i+1]+=Math.abs(inter[contribute][i]-interest[contribute])/5;
				 }
				interest_score[i+1]=interest_score[i+1]/(double)4;
			 }
		return  interest_score;
	}
	 
	 /**
	 * @method get_cost_combine(double CF_heso, double Predict_heso)
	 * @purpose combine cost[][] tu ket qua cua 2 rang buoc CF va Du doan diem
	 * @param CF_heso : he so cua rang buoc CF
	 * @param Predict_heso : de so cua rnag buoc Du doan diem
	 * @throws IOException
	 */
	 public void get_cost_combine(double main_heso, double Target_heso, double Interest_heso)
			 throws IOException
	 {
		 double[] CF_score = get_score_CF();
		 double[] Pre_score = get_score_Predict();
		 double[] Target_score = get_score_target();
		 double[] Interest_score = get_score_interest();
		 // combine 2 rangs buoc tren = 1 - (CF_score[i]-Pre_score[i]); 
		 for(int i = 1; i<=NumberOfCourses; i++ )
		 {
			 this.cost[0][i] = 1.0-main_heso*CF_score[i]*Pre_score[i]- Target_heso*Target_score[i]- Interest_heso* Interest_score[i]; 
		 }
		 
//		 for(int h = 0;h<cost.length;h++)
//			{
//				for(int k = 0; k < cost[0].length;k++) System.out.print(cost[h][k] + "     ");
//				System.out.println(" ");
//			}

	 }
	 /**
	 * @name get_cost_combine_mutil()
	 * @param  double Target_heso, double Interest_heso
	 * @purpose ket hop 2 rang buoc CF va Score Predict = PP nhan + set cap[][] bang ket qua thu duoc
	 * @throws IOException
	 */
	public void get_cost_combine_mutil() throws IOException
			 {
				 double[] CF_score = this.get_score_CF();
				 double[] Pre_score = this.get_score_Predict();
//				 double[] Target_score = get_score_target();
//				 double[] Interest_score = get_score_interest();
//				 combine 2 rang buoc tren = 1 - (CF_score[i]*Pre_score[i]); 
				 for(int i = 1; i <= NumberOfCourses; i++)
				 {
					 cost[0][i]=cost[0][i]-CF_score[i]*Pre_score[i];
					 //- Target_heso*Target_score[i]- Interest_heso* Interest_score[i];
				 }
		 }
	 
/*	 public void get_score_target(double heso) throws FileNotFoundException
	 {
		 double[][] target = fal.getTarget();
		 
		 
	 }*/


	/**
	 * @purpose tim index ma tai do cost MAX
	 * @param choosen
	 * @return indexMax
	 */
	public int getIndexMax(boolean[] choosen){
		
//		int[] indexChoose = new int[NumberOfCourses];
//		int count= 0;
//		for(int i = 0; i < choosen.length ; i++)
//		{
//			if (choosen[i]) {
//				indexChoose[count++]=i;
//			}
//		}
		int indexMax = -1;
		double costMax = 0.0;
		System.out.println("???" + choosen.length);
		for(int i = 1; i < NumberOfCourses; i++){
			if(!choosen[i]  ) continue;
			
			if(this.cost[0][i] > costMax) {
				indexMax = i;
				costMax = this.cost[0][i] ;
			}
		}
		System.out.println("OK" + (indexMax) +"  "+ cost[0][indexMax]);
		return indexMax;
	}
	
	/**
	 * @purpose : lay S = mot tap n so co cost[][] lon nhat trong choosen 
	 * @param choosen
	 * @return S
	 */
	public int[] getIndexs(boolean[] choosen) {
		int[] newArr = new int[2];
		for(int i = 0; i<2;i++){
			newArr[i]=getIndexMax(choosen);
			choosen[newArr[i]]=false;
			}
		return newArr;
	}
	
	
	/**
	 * @purpose tao mot student moi co cap[][] dc set = 0 cho nhung mon co cost[][] cao nhat
	 * @param choosen
	 * @return new student with fixed cap
	 * @throws CloneNotSupportedException
	 */
	public student setNewCap(boolean[] choosen) throws CloneNotSupportedException {
		student newStudent= (student) this.clone();
		int[] indexMaxs = getIndexs(choosen);
		for(int i = 0 ; i<indexMaxs.length; i++)
		{
			int indexMax = indexMaxs[i];
			System.out.println("ok new student ! max cost at : " + (indexMax));
			//	showCap();
			newStudent.cap[0][indexMax]=0;
		}
		//System.out.println("ok new student ! max cost at : " + indexMax);
		//	showCap();
		//	newStudent.cap[0][indexMax+1]=0;
		//System.out.println("after set cap ");
		//showCap();
		
		
		return newStudent;
	}
	
	public void  showCap() {
		 for(int h = 0;h<cap.length;h++)
				{
					for(int k = 0; k < cap[0].length;k++) System.out.print(cap[h][k] + "     ");
					System.out.println(" ");
				}
	}
	
	
	public student clone() {
		return new student(this);
	}
	 
	public void sort(int arr[])
	{
	 int i, j, temp ;
		for ( i = 0 ; i <= 7 ; i++ )
		{
			for ( j = i + 1 ; j <= 8 ; j++ )
			{
				if ( arr[i] > arr[j] )
				{
					temp = arr[i] ;
					arr[i] = arr[j] ;
					arr[j] = temp ;
				}
			}
		}
	}



	/**
	 * @return the mSV
	 */
	public String getMSV() {
		return MSV;
	}

	/**
	 * @param mSV the mSV to set
	 */
	public void setMSV(String mSV) {
		MSV = mSV;
	}

	/**
	 * @return the yearShool
	 */
	public int getYearShool() {
		return yearSchool;
	}

	/**
	 * @param yearShool the yearShool to set
	 */
	public void setYearShool(int yearShool) {
		this.yearSchool = yearShool;
	}

	/**
	 * @return the fal
	 */
	public faculty getFal() {
		return fal;
	}

	/**
	 * @param fal the fal to set
	 */
	public void setFal(faculty fal) {
		this.fal = fal;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/**
	 * @return the interest
	 */
	public double[] getInterest() {
		return interest;
	}
	
	public double[][] getCost()
	{
		return cost;
	}
	public int[][] getCap()
	{
		return cap;
	}
	public String getNameFile()
	{
		return name_file;
	}
	public double getAVG () { 
		return current_avg;
		
	}

	/**
	 * @param interest the interest to set
	 */
	public void setInterest(double[] interest) {
		this.interest = interest;
	}



	/**
	 * @return the studiedCourses
	 */
	public ArrayList<String> getStudiedCourses() {
		return StudiedCourses;
	}

	/**
	 * @param studiedCourses the studiedCourses to set
	 */
	public void setStudiedCourses(ArrayList<String> studiedCourses) {
		StudiedCourses = studiedCourses;
	}

}
