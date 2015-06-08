package com.loginjones.examples;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.lucene.LucenePDFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class RbcVisaPdfStatementParser {

	SimpleDateFormat sdf= new SimpleDateFormat("MMM dd");
	Calendar cal = Calendar.getInstance();
	
	public void parse() throws IOException, ParseException
	{
		File file=new File("/Users/login/Documents/Financial/4514093604656914-2015Mar19-2015Apr20.pdf");
		Document doc=LucenePDFDocument.getDocument(file);
		

		if (doc != null)
		{
			System.out.println("Created document");
			Reader contentReader=doc.getField("contents").readerValue();
			String contentStringAll=IOUtils.toString(contentReader);
//			System.out.println(contentStringAll);
			
			int idxStmtDateStart=contentStringAll.indexOf("STATEMENT FROM ")+"STATEMENT FROM ".length();
			int idxStmtDateEnd=contentStringAll.indexOf("\n", idxStmtDateStart);
			String stmtDateString=contentStringAll.substring(idxStmtDateStart, idxStmtDateEnd);
			String[] stmtDateArray=stmtDateString.split(" TO ");
			String[] stmtEndDateArray=stmtDateArray[1].split(", ");
			String[] stmtEndDateArray2=stmtEndDateArray[1].split(" ");
			int stmtYear=Integer.parseInt(stmtEndDateArray2[0]);
			Date statementEndDate=sdf.parse(stmtEndDateArray[0]);
			cal.setTime(statementEndDate);
			cal.set(Calendar.YEAR, stmtYear);
			statementEndDate = cal.getTime();
			String[] stmtStartDateArray=stmtDateArray[0].split(", ");
			Date statementStartDate=sdf.parse(stmtStartDateArray[0]);
			cal.setTime(statementStartDate);
			int stmtStartYear=stmtYear;
			if (stmtStartDateArray.length>1)
			{
				stmtStartYear=Integer.parseInt(stmtStartDateArray[1]);
			}
			cal.set(Calendar.YEAR, stmtStartYear);
			statementStartDate=cal.getTime();
			
			System.out.println("Statement Date Range "+statementStartDate+" to "+statementEndDate);
			
			int idxStart=contentStringAll.indexOf("DATE DATE") + "DATE DATE".length() + 1;
			int idxEnd=contentStringAll.indexOf("Time to Pay");
			String contentStringCore= contentStringAll.substring(idxStart,idxEnd);
			System.out.println(contentStringCore);
			
			String[] contentArrayCore=contentStringCore.split("\n");
			System.out.println(contentArrayCore.length);
			
			List<RbcVisaTxnRow> rows=new ArrayList<RbcVisaTxnRow>();
			RbcVisaTxnRow lastRow=null;
			
			boolean isAmountColumn=false;
			boolean isNewLine=true;
			
			
			for (String line:contentArrayCore)
			{
				try {
					if (isNewLine)
					{
						String dateTxnString=line.substring(0, 6);
						String datePostedString=line.substring(7,13);
						String activityDesc=line.substring(14);
						System.out.println("Txn date "+dateTxnString+" Posted date "+datePostedString);
						Date dateTxn = adjust(sdf.parse(dateTxnString),stmtStartYear,stmtYear);
						Date datePosted = adjust(sdf.parse(datePostedString),stmtStartYear,stmtYear); //sdf.parse(datePostedString);
						RbcVisaTxnRow row=new RbcVisaTxnRow();
						row.setTransactionDate(dateTxn);
						row.setPostingDate(datePosted);
						row.setActivityDescriptionMain(activityDesc);
						lastRow=row;
						rows.add(row);
						isNewLine=false;					
					}
					if (line.startsWith("$"))
					{
						String amountString=line.substring(1);
						Double amount=Double.parseDouble(amountString);
						lastRow.setAmount(amount);
						isNewLine=true;
					}
				} catch (Exception e) {
					System.out.println("Error on this line, skipping - "+line);
				}
				
			}
		
			String data=RbcVisaTxnRow.toString(rows);
			System.out.println(data);
		}

	}
	
	private Date adjust(Date date,int statementStartYear, int statementEndYear)
	{
		cal.setTime(date);
		
		if (date.getMonth()==11 && statementStartYear != statementEndYear)
		{
			cal.set(Calendar.YEAR, statementStartYear);
			return cal.getTime();
		}
		else if (date.getMonth()==0 && statementStartYear != statementEndYear) {
			cal.set(Calendar.YEAR, statementEndYear);
			return cal.getTime();
		}
		else {
			cal.set(Calendar.YEAR, statementStartYear);
			return cal.getTime();
		}
	}
	
	
	
}
