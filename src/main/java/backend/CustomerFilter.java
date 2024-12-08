package backend;

/**
 * A filter used by the administrator to find customers matching certain criteria.
 */
public class CustomerFilter 
{
	private String keywords;
	private double credit;
	private boolean orderByCredit;
	private boolean orderByName;
	private int vip;
	
	public CustomerFilter()
	{
		this.keywords = "";
		this.vip = 0;
		this.orderByCredit = false;
		this.orderByName = false;
		this.credit = 0;
	}
	
	public boolean getOrderByCredit() {
		return orderByCredit;
	}
	public void setOrderByCredit(boolean orderByCredit) {
		this.orderByCredit = orderByCredit;
	}
	public boolean getOrderByName() {
		return orderByName;
	}
	public void setOrderByName(boolean orderByName) {
		this.orderByName = orderByName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}
	
	
	
}
