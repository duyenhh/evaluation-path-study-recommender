/**
 * @description : tung loai rang buoc 
 */
package student;


public class require {
	
	private int ID;				// ID cua rang buoc  [ 1 ---> n ]
	private String name;		// Ten rang buoc VD : 'Khoi mon hoc bo tro'
	private int total_credit;	// total credits need for each require

	
	public require(int ID_, int total)
	{
		name = "auto";
		ID = ID_;
		total_credit = total;
	}
	
	public require() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the total_credit
	 */
	public int getTotal_credit() {
		return total_credit;
	}
	/**
	 * @param total_credit the total_credit to set
	 */
	public void setTotal_credit(int total_credit) {
		this.total_credit = total_credit;
	}

}
