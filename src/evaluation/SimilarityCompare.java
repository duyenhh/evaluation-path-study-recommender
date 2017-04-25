/**
 * 
 */
package evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import student.faculty;

/**
 * @author Thuy
 *
 */
public class SimilarityCompare {


		private List<String> realStudiedCourses = new ArrayList<String>();
		public double Caculation(int number) throws IOException{
			BufferedReader br_Real= new BufferedReader(new FileReader("Data/student_info/real_data/#"+number+".csv"));
			BufferedReader br_Result= new BufferedReader(new FileReader("Data/Result/#"+number+".csv"));
			
			
			String line_Real;
			String line_Result;
			// so sanh id_monhoc voi nhau; sau do so sanh tinh toan ket qua
			
			/*
			 * Cau truc file se la msv; mmh; result;
			 * RealData: Push all course studied into a list.
			 * Read PredictedData and count if recommended courses is in the list above.
			 */
			while ( (line_Real = br_Real.readLine()) != null ){ 
				String[] temp = line_Real.split(",");
				if ( temp[2].equals("0")) continue;
				realStudiedCourses.add(temp[1]);
			}
			br_Real.close();
			//test
			//for(int i = 0 ; i < realStudiedCourses.size(); i++)
			//System.out.println(realStudiedCourses.get(i));
			
			int m00=0,m01=0,m10=0,m11=0;
			while ( (line_Result = br_Result.readLine()) != null ){
				String[] temp = line_Result.split(",");
				if ( temp[2].equals("1")){
					if (realStudiedCourses.contains(temp[1])) 
					{//test
					//	System.out.println("OK 11 : "+temp[0]+"  "+ temp[1] );	
					m11++;}
					else m10++;
				}
				else{
					if (realStudiedCourses.contains(temp[1])) 
						{//test
						//System.out.println("BAD 01 : "+temp[0]+"  "+ temp[1] );	
						m01++;
						}
					else m00++;
					
				}
			}
			double smc = (double) (m00+m11)/(m00+m01+m10+m11);
			//test
			//System.out.println(smc);
			br_Result.close();
			return smc;// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		for(int i = 1; i <=250 ; i++)
//		{
//			SimilarityCompare eva = new SimilarityCompare();
//			eva.Caculation(i);
//		}
//		
		// TODO Auto-generated method stub

	}

}
