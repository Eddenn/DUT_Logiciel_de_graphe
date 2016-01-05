package model;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
	
	private static String FILE = "/root/Documents/test.pdf";
	private static Font catFont 	= new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont 	= new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont 	= new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold 	= new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Graph g;
	  
	public static void generer(Graph g, String name){
		 try {
			  PdfGenerator.g = g;
		      Document document = new Document();
		      PdfWriter.getInstance(document, new FileOutputStream(FILE));
		      document.open();
		      addMetaData(document);
		      addTitlePage(document, name);
		      addContent(document);
		      document.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
	
			int lol; //test commitaaaaa

		  // iText allows to add metadata to the PDF which can be viewed in your Adobe
		  // Reader
		  // under File -> Properties
		  private static void addMetaData(Document document) {
		    document.addTitle("My first PDF");
		    document.addSubject("Using iText");
		    document.addKeywords("Java, PDF, iText");
		    document.addAuthor("Groupe 3 Projet Tuteuré");
		    document.addCreator("Groupe 3 Projet Tuteuré");
		  }

		  private static void addTitlePage(Document document, String name)
		      throws DocumentException {
		    Paragraph preface = new Paragraph();
		    // We add one empty line
		    addEmptyLine(preface, 1);
		    // Lets write a big header
		    preface.add(new Paragraph("Résume du graphe : " + name, catFont));

		    addEmptyLine(preface, 1);
		    // Will create: Report generated by: _name, _date
		    preface.add(new Paragraph("Rapport généré par : " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		        smallBold));
		    addEmptyLine(preface, 3);
		    preface.add(new Paragraph("Ce document contient les elements suivants : \n\t 1 - Image du graphe \n\t 2 - La ou les structures ayant permis de générer le graphe \n\t 3 - L’inventaire des différents composants du graphe. ",
		        smallBold));

		    addEmptyLine(preface, 8);

		    document.add(preface);
		    // Start a new page
		    document.newPage();
		  }

		  private static void addContent(Document document) throws DocumentException {
		    Anchor anchor = new Anchor("1 - Image du graphe :", catFont);
		    anchor.setName("1 - Image du graphe :");

		    // Second parameter is the number of the chapter
		    Chapter catPart = new Chapter(new Paragraph(anchor), 1);

		    // now add all this to the document
		    document.add(catPart);

		    document.newPage();
		    
		    anchor = new Anchor("2 - La ou les structures qui ont permis de generer le graphe : ", catFont);
		    anchor.setName("2 - Les Structures :");

		    // Second parameter is the number of the chapter
		    catPart = new Chapter(new Paragraph(anchor), 1);
		    Paragraph liste  = new Paragraph(g.getFormattedList());
		    Paragraph matrix = new Paragraph(g.displayMatrix2());

		    // now add all this to the document
		    document.add(catPart);
		    document.add(new Paragraph(" \nListe d'adjacence : \n", smallBold));
		    document.add(liste);
		    document.add(new Paragraph(" \nMatrice : \n", smallBold));
		    document.add(matrix);

		  }

		  private static void createTable(Section subCatPart)
		      throws BadElementException {
		    PdfPTable table = new PdfPTable(3);

		    // t.setBorderColor(BaseColor.GRAY);
		    // t.setPadding(4);
		    // t.setSpacing(4);
		    // t.setBorderWidth(1);

		    PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase("Table Header 2"));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);

		    c1 = new PdfPCell(new Phrase("Table Header 3"));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		    table.setHeaderRows(1);

		    table.addCell("1.0");
		    table.addCell("1.1");
		    table.addCell("1.2");
		    table.addCell("2.1");
		    table.addCell("2.2");
		    table.addCell("2.3");

		    subCatPart.add(table);

		  }

		  private static void createList(Section subCatPart) {
		    List list = new List(true, false, 10);
		    list.add(new ListItem("First point"));
		    list.add(new ListItem("Second point"));
		    list.add(new ListItem("Third point"));
		    subCatPart.add(list);
		  }

		  private static void addEmptyLine(Paragraph paragraph, int number) {
		    for (int i = 0; i < number; i++) {
		      paragraph.add(new Paragraph(" "));
		    }
		  }
		} 
