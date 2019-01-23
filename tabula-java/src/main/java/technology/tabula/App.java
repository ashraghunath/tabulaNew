package technology.tabula;

import com.google.common.collect.Range;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.text.PDFTextStripper;
import technology.tabula.trap.Table;
import technology.tabula.trap.TableCell;
import technology.tabula.trap.TableRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class App {

    static File pdf = new File("C:/Users/araghunath/Desktop/TRP/Legacy.pdf");
    boolean added = false;

    public static void main(String[] args) throws Exception {

        PDDocument document = PDDocument.load(pdf);
        PDPage pdPage = document.getPage(0);

        App app = new App();

        //print no of columns
//        System.out.println("columns are " + app.noOfColumns());

        //printing tables using coordinates of rectangle
        app.printTableUsingCoOrdinates();

        //getting page number of phrase
//        System.out.println("pageNumber "+ app.getPhrasePageNumber("Beneficiary Information", document));

//        app.getPageFonts(pdPage);


    }

    public void getPageFonts(PDPage page) throws IOException {
        PDResources resources = page.getResources();
        if (resources != null) {
            Iterable<COSName> fontMap = resources.getFontNames();
            Iterator it = fontMap.iterator();

            while (it.hasNext()) {
                System.out.println(it.next().toString());
            }
        }
    }

    public void printTableUsingCoOrdinates() {

        //trap
        List<Table> tables = new ArrayList<>();
        Table table1 = new Table(getColumnRanges().size());
        //trap
        int idrow = 0;
        int idcell = 0;
        TableRow row = new TableRow(idrow);
        TableCell tableCell = null;
        //trap

        //trap


        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Rectangle r1 = new Rectangle(258.953f, 53.933f, 257.04f, 137.7f);
        Rectangle r2 = new Rectangle(78.413f, 29.453f, 537.795f, 250.7f);
        Rectangle PortFolioPage1 = new Rectangle(426.488f, 53.933f, 721.358f, 273.87f);
        Rectangle activity_summary = new Rectangle(242.888f, 53.168f, 255.51f, 200.418f);
        Rectangle money_Market_Activity = new Rectangle(363.758f, 30.983f, 740.135f, 194.31f); //problem
        Rectangle brokerage_holdings = new Rectangle(79.178f,52.403f,537.03f,498.015f);
        ObjectExtractor extractor = new ObjectExtractor(pdfDocument);
        Page area = extractor.extract(1).getArea(PortFolioPage1);

        double prevY = area.getText().get(0).getY();
        double prevX = area.getText().get(0).getX();
        double prevW = area.getText().get(0).getWidth();
        StringBuilder cellContentBuilder = new StringBuilder();
        List<Range<Double>> columnRange = getColumnRanges();
        System.out.println("Columns range is : " + columnRange);
        TextElement firstText = area.getText().get(0);


        for (TextElement textElement : area.getText()) {
            double yOfElement = textElement.getY();
            if ((textElement.getX() - (prevX + prevW)) > 1) {
                if ((textElement.getX() - (prevX + prevW)) > 3) {
                    System.out.print(cellContentBuilder.toString());


                    double wordEndIndex = prevX;

                    //trap
                    int index = 0;
                    for (Range range : columnRange) {
                        if (range.contains((double) firstText.x) || range.contains(wordEndIndex)) {
                            idcell = index;
                            break;
                        }
                        index++;
                    }
                    firstText = textElement;

                    tableCell = new TableCell(idcell, cellContentBuilder.toString());
                    List<TableCell> tableCellList = row.getCells();
                    tableCellList.add(tableCell);
                    idcell++;
//                    uncomment above when removing above foreach
                    added = true;
                    //trap

                    cellContentBuilder.delete(0, cellContentBuilder.length());
                    System.out.print(";");

                } else {
                    cellContentBuilder.append(" ");
//                    System.out.print(" ");
                    added = false;
                }
            } else {
                added = false;
            }
            if (yOfElement - prevY > 4) {

                if (!added) {
                    //trap


                    double wordEndIndex = prevX;

                    int index = 0;
                    for (Range range : columnRange) {
                        if (range.contains((double) firstText.x) || range.contains(wordEndIndex)) {
                            idcell = index;
                        }
                        index++;
                    }
                    firstText = textElement;

                    tableCell = new TableCell(idcell, cellContentBuilder.toString());
                    row.getCells().add(tableCell);
                    idcell++;
                }
                idrow++;


                List<TableRow> rowList = table1.getRows();
                rowList.add(row);
                row = new TableRow(idrow);
                //trap

                System.out.print(cellContentBuilder.toString());
                cellContentBuilder.delete(0, cellContentBuilder.length());
                System.out.println();
                prevY = yOfElement;
                //remove this to add semicolon
                idcell = 0;
            } else {
                prevY = yOfElement;
            }
            prevX = textElement.getX();
            prevW = textElement.getWidth();

            cellContentBuilder.append(textElement.getText());
//            System.out.print(textElement.getText());
        }

        List<TableRow> finalRow = table1.getRows();
        for (TableRow currentRow : finalRow) {
            List<TableCell> cell = currentRow.getCells();

            for (int i = 0; i < columnRange.size(); i++)
                if (getTableCellById(currentRow, i) == -1) {
                    tableCell = new TableCell(i, "");
                    currentRow.getCells().add(i, tableCell);
                }
        }

        System.out.println();
        System.out.println("hereeeeeeeeeeee");
        System.out.println(table1.getRows());
        String csv = table1.toHtml();
        System.out.println(csv);
//
//        System.out.println(csv);
    }

    int getTableCellById(TableRow row, int id) {

        List<TableCell> cells = row.getCells();
        for (TableCell cell : cells) {
            if (cell.getIdx() == id)
                return id;
        }

        return -1;
    }

    public List<Range<Double>> getColumnRanges() {
        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double topr1 = 53.933f;
        Rectangle r1 = new Rectangle(258.953f, 53.933f, 257.04f, 137.7f);
        Rectangle r2 = new Rectangle(78.413f, 29.453f, 537.795f, 250.7f);
        ObjectExtractor extractor = new ObjectExtractor(pdfDocument);
        Rectangle PortFolioPage1 = new Rectangle(426.488f, 53.933f, 721.358f, 273.87f);
        Rectangle activity_summary = new Rectangle(242.888f, 53.168f, 255.51f, 200.418f);
        Rectangle money_Market_Activity = new Rectangle(363.758f, 30.983f, 740.135f, 194.31f); //problem
        Rectangle brokerage_holdings = new Rectangle(79.178f,52.403f,537.03f,498.015f); //problem
        Page area = extractor.extract(1).getArea(PortFolioPage1);
        int i = 1;
        double prevY = area.getText().get(0).getY();
        double prevX = area.getText().get(0).getX();
        double prevW = area.getText().get(0).getWidth();
        StringBuilder cellContentBuilder = new StringBuilder();
        List<Range<Double>> rangeList = new ArrayList<>();
        double startX = area.getText().get(0).getX();

        for (TextElement textElement : area.getText()) {
            double yOfElement = textElement.getY();
            if ((textElement.getX() - (prevX + prevW)) > 1) {
                if ((textElement.getX() - (prevX + prevW)) > 3) {
//                    System.out.print(cellContentBuilder.toString());
                    i++;
                    Range<Double> range = Range.closed((Double) startX, (Double) (prevX + prevW));
                    rangeList.add(range);
                    startX = textElement.getX();
                    added = true;

                    cellContentBuilder.delete(0, cellContentBuilder.length());
//                    System.out.print(";");

                } else {
                    cellContentBuilder.append(" ");
                    added = false;
                }
            } else {
                added = false;
            }
            if (yOfElement - prevY > 4) {
                i++;
//                System.out.print(cellContentBuilder.toString());

                if (added == false) {
                    Range<Double> range = Range.closed((Double) startX, (Double) (prevX + prevW));
                    rangeList.add(range);
                    startX = textElement.getX();
                    return rangeList;
                }

                cellContentBuilder.delete(0, cellContentBuilder.length());
                System.out.println();
                prevY = yOfElement;

            } else {
                prevY = yOfElement;
            }
            prevX = textElement.getX();
            prevW = textElement.getWidth();


            cellContentBuilder.append(textElement.getText());
        }

        return rangeList;
    }


    int getPhrasePageNumber(String phrase, PDDocument document) {
        int pageNumber = -1;
        int totalPages = document.getNumberOfPages();
        PDPage page;
        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            for (int i = 1; i <= totalPages; i++) {
                page = document.getPage(i);
                pdfStripper.setStartPage(i);
                pdfStripper.setEndPage(i);
                if (page.hasContents()) {
                    String temp = pdfStripper.getText(document);
                    if (temp.contains(phrase.trim())) {
                        pageNumber = i;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pageNumber;
    }

}
