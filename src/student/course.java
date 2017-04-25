/**
 * 
 */
package student;

public class course {
	private String id;					// MA mon hoc cua course trong file khung chuong trinh
	private int STT;				// STT <--> index [0][STT] trong cap[][] va cost[][] 
	//private String courseId;		// Ma Mon Hoc - bang chu - VD: INT3110
	private String courseName;		// Ten Mon Hoc
	private int credit;				// So tin chi
	private int requirement_ID;		// ID cua 'require' tuong ung
	private double score;			// Diem ghi nhan cua mon hoc nay
	
	private String preRequisite;
	
	
	public course(int STT_,String id_, String courseName_ , int credit_ , int require_ )
	{
		setSTT(STT_);
		id = id_;
		courseName = courseName_;
		credit = credit_;
		requirement_ID = require_;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getRequirement_ID() {
		return requirement_ID;
	}
	public void setRequirement_ID(int requirement) {
		this.requirement_ID = requirement;
	}
	public String getPreRequisite() {
		return preRequisite;
	}
	public void setPreRequisite(String preRequisite) {
		this.preRequisite = preRequisite;
	}



	/**
	 * @return the sTT
	 */
	public int getSTT() {
		return STT;
	}

	/**
	 * @param sTT the sTT to set
	 */
	public void setSTT(int sTT) {
		STT = sTT;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
}
