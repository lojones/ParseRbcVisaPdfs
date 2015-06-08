package com.loginjones.examples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.sun.jndi.url.corbaname.corbanameURLContextFactory;


public class RbcVisaTxnRow {

	Date transactionDate;
	Date postingDate;
	String activityDescriptionMain;
	String notes;
	Double amount;
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Date getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}
	public String getActivityDescriptionMain() {
		return activityDescriptionMain;
	}
	public void setActivityDescriptionMain(String activityDescriptionMain) {
		this.activityDescriptionMain = activityDescriptionMain;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	private String getDate(Date d, SimpleDateFormat sdf)
	{
		try {
			return sdf.format(d);
		} catch (Exception e) {
			return "None";
		}
	}
	@Override
	public String toString() {

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return getDate(transactionDate, sdf)+","+getDate(postingDate, sdf)+","+activityDescriptionMain+","+amount;
	}
	
	public static String toString(List<RbcVisaTxnRow> rows)
	{
		String result="";
		
		for (RbcVisaTxnRow row:rows)
		{
			result += row.toString() +"\n";
		}
		
		return result;
	}
	
	
	
	
}
