package model;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import view.HCI;

public class PdfGenerator {

	private static String FILE = "/root/Documents/test.pdf";
	private static String strImagePath = "/root/Documents/test.png";

	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Graph g;
	private static HCI hci;

	public static void generer(Graph g, String name, String path, HCI hci) {
		try {
			if (path != null) {
				FILE = path;
				strImagePath = path;

				if (path.indexOf(".pdf") < 0) {
					FILE += ".pdf";
				} else {
					strImagePath = path.substring(0, path.indexOf(".pdf"));
				}
			}

			// simple test de commit

			PdfGenerator.g = g;
			PdfGenerator.hci = hci;
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

	// iText allows to add metadata to the PDF which can be viewed in your Adobe
	// Reader
	// under File -> Properties
	private static void addMetaData(Document document) {
		document.addTitle("My first PDF");
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("Groupe 3 Projet Tuteur�");
		document.addCreator("Groupe 3 Projet Tuteur�");
	}

	private static void addTitlePage(Document document, String name) throws DocumentException {
		Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("R�sume du graphe : " + name, catFont));

		// Connaitre le type de graphe

		String sType = "Le graphe est de type : ";
		if (g.isDirected())
			sType += "orient� ";
		else
			sType += "non-orient� ";

		sType += " et ";

		if (g.isValued())
			sType += " valu�";
		else
			sType += " non-valu�";

		preface.add(new Phrase(sType));

		// fin des types de graphes

		addEmptyLine(preface, 1);
		// Will create: Report generated by: _name, _date
		preface.add(new Paragraph("Rapport g�n�r� par : Logiciel Pedagogique de Graphes, " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$
																												// //$NON-NLS-3$
				smallBold));
		addEmptyLine(preface, 2);

		Paragraph middle = new Paragraph(
				"Ce document contient les elements suivants : \n\t 1 - Image du graphe \n\t 2 - La ou les structures ayant permis de g�n�rer le graphe \n\t 3 - L�inventaire des diff�rents composants du graphe. ",
				smallBold);
		middle.setAlignment(Element.ALIGN_CENTER);
		preface.add(middle);

		addEmptyLine(preface, 30);

		preface.add(new Paragraph("Auteur du document : " + System.getProperty("user.name")));

		document.add(preface);
		// Start a new page
		document.newPage();
	}

	private static void addContent(Document document) throws DocumentException, MalformedURLException, IOException {
		Anchor anchor = new Anchor("Image du graphe :", catFont);
		anchor.setName("1 - Image du graphe :");

		// Second parameter is the number of the chapter
		Chapter catPart = new Chapter(new Paragraph(anchor), 1);

		// now add all this to the document
		document.add(catPart);

		Dimension size = hci.getGraphPanel().getSize();
		BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		hci.getGraphPanel().paint(g2);

		int iMaxWidth = 520;
		int iMaxHeight = 446;

		int iNewWidth = 0;
		int iNewHeight = 0;

		if (size.width > iMaxWidth || size.height > iMaxHeight) {
			double dCoef1 = (double)size.width / (double)iMaxWidth;
			double dCoef2 = (double)size.height / (double)iMaxWidth;

			if (dCoef1 > dCoef2) {
				iNewWidth = (int) (size.width / dCoef1);
				iNewHeight = (int) (size.height / dCoef1);
			} else {
				iNewWidth = (int) (size.width / dCoef2);
				iNewHeight = (int) (size.height / dCoef2);
			}
		}
		
		new ResizeImage(bi, strImagePath,iNewWidth,iNewHeight);

		Image image = Image.getInstance(strImagePath + "_tn.png");
		document.add((Element) image);

		File file = new File(strImagePath + "_tn.png");

		file.delete();

		document.newPage();
		Anchor anchor2 = new Anchor("La ou les structures qui ont permis de generer le graphe : ", catFont);
		anchor2.setName("2 - Les Structures :");
		Chapter catPart2 = new Chapter(new Paragraph(anchor2), 2);

		// Second parameter is the number of the chapter
		// catPart = new Chapter(new Paragraph(anchor), 1);
		Paragraph liste = new Paragraph(g.getFormattedList());
		Paragraph matrix = new Paragraph(g.displayMatrix2());

		// now add all this to the document
		document.add(catPart2);
		document.add(new Paragraph(" \nListe d'adjacence : \n", smallBold));
		document.add(liste);
		document.add(new Paragraph(" \nMatrice : \n", smallBold));
		document.add(matrix);

		Anchor anchor3 = new Anchor("Elements qui composent le graphe : ", catFont);
		Chapter catPart3 = new Chapter(new Paragraph(anchor3), 3);

		document.add(catPart3);

		// Tableau des �l�ments qui composent le graphe :

		String sVertex = "Liste des sommets intervenant dans le graphe : \n";
		for (Vertex v : g.getAlVertex())
			sVertex += "\t- " + v.getName() + "\n";

		Paragraph listVextex = new Paragraph(sVertex);
		document.add(listVextex);

		// add a table
		Section subCatPart = catPart3.addSection(new Paragraph("\n\nListe des elements: \n\n", smallBold));
		createTable(subCatPart);

		document.add(subCatPart);

	}

	private static void createTable(Section subCatPart) throws BadElementException {
		PdfPTable table = new PdfPTable(3);

		PdfPCell c1 = new PdfPCell(new Phrase("Sommet"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Co�t"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Destination"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		table.setHeaderRows(1);

		for (Vertex v : g.getAlVertex()) {
			for (Arc a : v.getAlArcs()) {

				Vertex v2 = a.getVertex();

				c1 = new PdfPCell(new Phrase(v.getName()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c1);

				c1 = new PdfPCell(new Phrase(a.getIValue() + ""));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c1);

				c1 = new PdfPCell(new Phrase(v2.getName()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c1);

			}
		}

		subCatPart.add(table);

	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}