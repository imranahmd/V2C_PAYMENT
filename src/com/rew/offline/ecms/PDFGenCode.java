//package com.rew.offline.ecms;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.MalformedURLException;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.CMYKColor;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.rew.payment.utility.PGUtils;
//
//public class PDFGenCode {
//
//	public static void main(String[] args)
//	{
//	    Document document = new Document();
//	    try
//	    {
//	    	Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE);
//	        Font blackFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new CMYKColor(255,255,255,255));
//	        Font blackFontBold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new CMYKColor(255,255,255,255));
//	        Font hiddenFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new CMYKColor(0,0,0,0));
//	        
//	                
//	    	String name="adita12";
//	    	String id="adita12";
//	    	String date="04-04-2020";
//	    	String depositNo="IE175511";
//	    	String amt="10.00";
//	    	
//	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:\\AddTableExample.pdf"));
//	        document.open();
//	        
//	        PdfPTable header = new PdfPTable(2);
//			header.setWidthPercentage(100.0f);								        
//			header.setWidths(new float[] {5.0f, 5.0f});
//			header.setSpacingBefore(10);       
//
//			// define font for table header row
//			Font headerfont = FontFactory.getFont(FontFactory.HELVETICA);
//			headerfont.setColor(BaseColor.BLACK);
//
//			// define table header cell
//			PdfPCell headercell = new PdfPCell();
//			headercell.setBackgroundColor(BaseColor.WHITE);
//			headercell.setPadding(2);
//
//			String filename = PGUtils.getPropertyValue("hdfclogo");//"/home/apache-tomcat-8.0.44/webapps/EcmsChallanGenerationAPI/images/hdfclogo.png";
//			System.out.println("filename = "+filename);
//			
//			Image image = null;
//			try {
//				image = Image.getInstance(filename);
//				//image.setAlignment(1); 
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			   
//			 
//			headercell.setPhrase(new Phrase("CORPORATE PRODUCTS AND SERVICES", headerfont));
//			header.addCell(image);
//			header.addCell(headercell);
//			
//			
//	 
//	        PdfPTable table = new PdfPTable(3); // 3 columns.
//	        table.setWidthPercentage(100); //Width 100%
//	        table.setSpacingBefore(10f); //Space before table
//	        table.setSpacingAfter(10f); //Space after table
//	 
//	        //Set Column widths
//	        float[] columnWidths = {1f, 1f, 1f};
//	        table.setWidths(columnWidths);
//	 
//	        PdfPCell cell1 = new PdfPCell(new Paragraph("",blackFontBold));
//	        cell1.setBorderColor(BaseColor.BLACK);
//	        cell1.setPaddingLeft(10);
//	        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	 
//	        PdfPCell cell2 = new PdfPCell(new Paragraph("Date: "+date,blackFontBold));
//	        cell2.setBorderColor(BaseColor.BLACK);
//	        cell2.setPaddingLeft(10);
//	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	 
//	        PdfPCell cell3 = new PdfPCell(new Paragraph("Deposit No: "+depositNo,blackFontBold));
//	        cell3.setBorderColor(BaseColor.BLACK);
//	        cell3.setPaddingLeft(10);
//	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell4 = new PdfPCell(new Paragraph("Client Code:  ies",blackFontBold));
//	        cell3.setBorderColor(BaseColor.BLACK);
//	        cell3.setPaddingLeft(10);
//	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell5 = new PdfPCell(new Paragraph("Pickup Point: ............................",blackFontBold));
//	        cell3.setBorderColor(BaseColor.BLACK);
//	        cell3.setPaddingLeft(10);
//	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell6 = new PdfPCell(new Paragraph("No. Of Insts.: ....................................",blackFontBold));
//	        cell3.setBorderColor(BaseColor.BLACK);
//	        cell3.setPaddingLeft(10);
//	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	 
//	        //To avoid having the cell border and the content overlap, if you are having thick cell borders
//	        cell1.setUseBorderPadding(true);
//	        cell2.setUseBorderPadding(true);
//	        cell3.setUseBorderPadding(true);
//	        cell4.setUseBorderPadding(true);
//	        cell5.setUseBorderPadding(true);
//	        cell6.setUseBorderPadding(true);
//	 
//	        table.addCell(cell1);
//	        table.addCell(cell2);
//	        table.addCell(cell3);
//	        table.addCell(cell4);
//	        table.addCell(cell5);
//	        table.addCell(cell6); 
//	        
//	        
//	        Paragraph para1 = new Paragraph("No convenience fee or service charge will be  levied on Debit Card Payments to the Remitter", blueFont);
//	        document.add(para1);
//	        
//	        Paragraph para2 = new Paragraph(" ",hiddenFont);
//	        para2.setAlignment(Element.ALIGN_CENTER);
//	        document.add(para2);
//	        
//	        Paragraph para3 = new Paragraph("Client Name: IES", blackFont);
//	        para3.setAlignment(Element.ALIGN_LEFT);
//	        document.add(para3);
//	        
//	        /*Paragraph para4 = new Paragraph("Student ID: "+ id);
//	        document.add(para4);
//	        
//	        Paragraph para5 = new Paragraph("Student Name lael: "+name);
//	        document.add(para5);	*/       
//	        	        
//	        
//	        //Second table start ---------------------------------------------------------------------------
//	        PdfPTable table2 = new PdfPTable(9); // 9 columns.
//	        table2.setWidthPercentage(100); //Width 100%
//	        table2.setSpacingBefore(10f); //Space before table
//	        table2.setSpacingAfter(10f); //Space after table
//	        table2.setHorizontalAlignment(2);
//	        
//	        //Set Column widths
//	        float[] columnWidths2 = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
//	        table2.setWidths(columnWidths2);
//	        
//	        PdfPCell cell21 = new PdfPCell(new Paragraph("Sr. No ",blackFont));
//	        cell21.setBorderColor(BaseColor.BLACK);
//	        cell21.setPaddingLeft(10);
//	        cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell21.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell22 = new PdfPCell(new Paragraph("Cheque/DD/PO No.",blackFont));
//	        cell22.setBorderColor(BaseColor.BLACK);
//	        cell22.setPaddingLeft(10);
//	        cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell22.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell23 = new PdfPCell(new Paragraph("Cheque Date ",blackFont));
//	        cell23.setBorderColor(BaseColor.BLACK);
//	        cell23.setPaddingLeft(10);
//	        cell23.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell23.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell24 = new PdfPCell(new Paragraph("Student Name label",blackFont));
//	        cell24.setBorderColor(BaseColor.BLACK);
//	        cell24.setPaddingLeft(10);
//	        cell24.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell24.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell25 = new PdfPCell(new Paragraph("Student Id ",blackFont));
//	        cell25.setBorderColor(BaseColor.BLACK);
//	        cell25.setPaddingLeft(10);
//	        cell25.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell25.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell26 = new PdfPCell(new Paragraph("Drawee Bank",blackFont));
//	        cell26.setBorderColor(BaseColor.BLACK);
//	        cell26.setPaddingLeft(10);
//	        cell26.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell26.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell27 = new PdfPCell(new Paragraph("Drawn on Location ",blackFont));
//	        cell27.setBorderColor(BaseColor.BLACK);
//	        cell27.setPaddingLeft(10);
//	        cell27.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell27.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell28 = new PdfPCell(new Paragraph("Amount (Rs.)",blackFont));
//	        cell28.setBorderColor(BaseColor.BLACK);
//	        cell28.setPaddingLeft(10);
//	        cell28.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell28.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell29 = new PdfPCell(new Paragraph("",blackFont));
//	        cell29.setBorderColor(BaseColor.BLACK);
//	        cell29.setPaddingLeft(10);
//	        cell29.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell29.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        //----------------------------------------------------------------------------------------------------ROW Change
//	        PdfPCell cell30 = new PdfPCell(new Paragraph("1",blackFont));
//	        cell30.setBorderColor(BaseColor.BLACK);
//	        cell30.setPaddingLeft(10);
//	        cell30.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell30.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell31 = new PdfPCell(new Paragraph(" ",blackFont));
//	        cell31.setBorderColor(BaseColor.BLACK);
//	        cell31.setPaddingLeft(10);
//	        cell31.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell31.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell32 = new PdfPCell(new Paragraph(" ",blackFont));
//	        cell32.setBorderColor(BaseColor.BLACK);
//	        cell32.setPaddingLeft(10);
//	        cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell32.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell33 = new PdfPCell(new Paragraph(name,blackFont));
//	        cell33.setBorderColor(BaseColor.BLACK);
//	        cell33.setPaddingLeft(10);
//	        cell33.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell33.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell34 = new PdfPCell(new Paragraph(id,blackFont));
//	        cell34.setBorderColor(BaseColor.BLACK);
//	        cell34.setPaddingLeft(10);
//	        cell34.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell34.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell35 = new PdfPCell(new Paragraph(" ",blackFont));
//	        cell35.setBorderColor(BaseColor.BLACK);
//	        cell35.setPaddingLeft(10);
//	        cell35.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell35.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell36 = new PdfPCell(new Paragraph(" ",blackFont));
//	        cell36.setBorderColor(BaseColor.BLACK);
//	        cell36.setPaddingLeft(10);
//	        cell36.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell36.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell37 = new PdfPCell(new Paragraph(" ",blackFont));
//	        cell37.setBorderColor(BaseColor.BLACK);
//	        cell37.setPaddingLeft(10);
//	        cell37.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell37.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell38 = new PdfPCell(new Paragraph(" ",blackFont));
//	        cell38.setBorderColor(BaseColor.BLACK);
//	        cell38.setPaddingLeft(10);
//	        cell38.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell38.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        //LOOP EMPTY ROWS------here
//	        
//	        //----------------------------START LAST ROW FOR TOTAL-----------------------------------------------ROW Change
//	        PdfPCell cell39 = new PdfPCell(new Paragraph("",blackFont));
//	        cell39.setBorderColor(BaseColor.BLACK);
//	        cell39.setPaddingLeft(10);
//	        cell39.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell39.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell40 = new PdfPCell(new Paragraph("",blackFont));
//	        cell40.setBorderColor(BaseColor.BLACK);
//	        cell40.setPaddingLeft(10);
//	        cell40.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell40.setVerticalAlignment(Element.ALIGN_MIDDLE);        
//        
//	        
//	        PdfPCell cell41 = new PdfPCell(new Paragraph("",blackFont));
//	        cell41.setBorderColor(BaseColor.BLACK);
//	        cell41.setPaddingLeft(10);
//	        cell41.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell41.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell42 = new PdfPCell(new Paragraph("",blackFont));
//	        cell42.setBorderColor(BaseColor.BLACK);
//	        cell42.setPaddingLeft(10);
//	        cell42.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell42.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell43 = new PdfPCell(new Paragraph("",blackFont));
//	        cell43.setBorderColor(BaseColor.BLACK);
//	        cell43.setPaddingLeft(10);
//	        cell43.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell43.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell44 = new PdfPCell(new Paragraph("",blackFont));
//	        cell44.setBorderColor(BaseColor.BLACK);
//	        cell44.setPaddingLeft(10);
//	        cell44.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell44.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell45 = new PdfPCell(new Paragraph("TOTAL",blackFontBold));
//	        cell45.setBorderColor(BaseColor.BLACK);
//	        cell45.setPaddingLeft(10);
//	        cell45.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell45.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell46 = new PdfPCell(new Paragraph(amt,blackFontBold));
//	        cell46.setBorderColor(BaseColor.BLACK);
//	        cell46.setPaddingLeft(10);
//	        cell46.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell46.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        PdfPCell cell47 = new PdfPCell(new Paragraph("",blackFont));
//	        cell47.setBorderColor(BaseColor.BLACK);
//	        cell47.setPaddingLeft(10);
//	        cell47.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell47.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        
//	        
//	        cell21.setUseBorderPadding(true);
//	        cell22.setUseBorderPadding(true);
//	        cell23.setUseBorderPadding(true);
//	        cell24.setUseBorderPadding(true);
//	        cell25.setUseBorderPadding(true);
//	        cell26.setUseBorderPadding(true);
//	        cell27.setUseBorderPadding(true);
//	        cell28.setUseBorderPadding(true);
//	        cell29.setUseBorderPadding(true);
//	        cell30.setUseBorderPadding(true);
//	        cell31.setUseBorderPadding(true);
//	        cell32.setUseBorderPadding(true);
//	        cell33.setUseBorderPadding(true);
//	        cell34.setUseBorderPadding(true);
//	        cell35.setUseBorderPadding(true);
//	        cell36.setUseBorderPadding(true);
//	        cell37.setUseBorderPadding(true);
//	        cell38.setUseBorderPadding(true);
//	        /*cell39.setUseBorderPadding(true);
//	        cell40.setUseBorderPadding(true);
//	        cell41.setUseBorderPadding(true);
//	        cell42.setUseBorderPadding(true);
//	        cell43.setUseBorderPadding(true);
//	        cell44.setUseBorderPadding(true);
//	        cell45.setUseBorderPadding(true);*/
//	        cell46.setUseBorderPadding(true);
//	        cell47.setUseBorderPadding(true);
//	        
//	        table2.addCell(cell21);
//	        table2.addCell(cell22);
//	        table2.addCell(cell23);
//	        table2.addCell(cell24);
//	        table2.addCell(cell25);
//	        table2.addCell(cell26);
//	        table2.addCell(cell27);
//	        table2.addCell(cell28);
//	        table2.addCell(cell29);
//	        table2.addCell(cell30);
//	        table2.addCell(cell31);
//	        table2.addCell(cell32);
//	        table2.addCell(cell33);
//	        table2.addCell(cell34);
//	        table2.addCell(cell35);
//	        table2.addCell(cell36);
//	        table2.addCell(cell37);
//	        table2.addCell(cell38);
//	        
//	        //START EMPTY ROWS ADDITION
//	        int srno=1;
//	        PdfPCell cellEmpty;	        
//	        for(int i=1;i<=36;i++) {
//	        	if(i==1 || i==10 || i==19 || i==28) {
//	        		srno++;	        		
//	        		//System.out.println("==>> srno = "+srno);
//	        		cellEmpty = new PdfPCell(new Paragraph(srno+"",blackFont));
//	        	}else {
//	        		//System.out.println("==>> i = "+i);
//	        		cellEmpty = new PdfPCell(new Paragraph(" ",hiddenFont));
//	        	}	
//	        	cellEmpty.setBorderColor(BaseColor.BLACK);
//	        	cellEmpty.setPaddingLeft(10);
//	        	cellEmpty.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        	cellEmpty.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        	cellEmpty.setUseBorderPadding(true);
//	        	table2.addCell(cellEmpty);
//	        	
//	        }
//	        //START EMPTY ROWS ADDITION
//	        table2.addCell(cell39);
//	        table2.addCell(cell40);	  
//	        table2.addCell(cell41);	  
//	        table2.addCell(cell42);
//	        table2.addCell(cell43);
//	        table2.addCell(cell44);
//	        table2.addCell(cell45);
//	        table2.addCell(cell46);
//	        table2.addCell(cell47);
//	        
//	        Rectangle rect= new Rectangle(577,825,10,15); // you can resize rectangle 
//	        rect.enableBorderSide(1);
//	        rect.enableBorderSide(2);
//	        rect.enableBorderSide(4);
//	        rect.enableBorderSide(8);
//	        rect.setBorderColor(BaseColor.BLACK);
//	        rect.setBorderWidth(1);
//	        document.add(rect);
//	        
//	        document.add(table);
//	        
//	        Paragraph para6 = new Paragraph("");
//	        document.add(para6);
//	        
//	        document.add(table2);
//	        
//	        Paragraph para7 = new Paragraph("Disclaimer: ",blackFontBold);
//	        document.add(para7);
//	        Paragraph para8 = new Paragraph("1. Remitter is required to generate challan for every payment as each challan is unique ",blackFontBold);
//	        document.add(para8);
//	        Paragraph para9 = new Paragraph("2. It is remitter's responsibility to ensure that the payment made are as per the exact details mentioned in the challan",blackFontBold);	        		
//	        document.add(para9);
//	        Paragraph para10 = new Paragraph("\n");	        		
//	        document.add(para10);
//	        Paragraph para11 = new Paragraph("TELLER'S Signature                               Depositor's Signature",blackFontBold);	        		
//	        document.add(para11);
//	        Paragraph para12 = new Paragraph("",blackFont);	        		
//	        document.add(para12);
//	        
//	        document.close();
//	        writer.close();
//	    } catch (Exception e)
//	    {
//	        e.printStackTrace();
//	    }
//	}
//
//}
