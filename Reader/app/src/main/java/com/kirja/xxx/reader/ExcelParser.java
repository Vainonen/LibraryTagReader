package com.kirja.xxx.reader;

import java.io.FileInputStream;
import java.util.List;

public class ExcelParser {
    /*
}
    try {
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        //Workbook wb = new XSSFWorkbook();
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        DataFormatter df;
        List<String> fieldnames = new List();
        List<List> database = new List<List>();

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        //int cols = sh.getRow(0).getLastCellNum();
        int tmp = 0;

        df = new DataFormatter();

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for(int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if(row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if(tmp > cols) cols = tmp;
            }
        }

        row = sheet.getRow(0);
        for(int c = 0; c < cols; c++) {
            cell = row.getCell((short)c);
            if(cell != null) {
                //df = new DataFormatter();
                fieldnames.add(df.formatCellValue(cell));
            }

            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                List<String> l = new List();
                database.add(l);
                if(row != null) {
                    for(int c = 0; c < cols; c++) {
                        cell = row.getCell((short)c);
                        if(cell != null) {
                            //df = new DataFormatter();
                            l.add(df.formatCellValue(cell));
                            //String value = df.formatCellValue(cell);
                            //String str = NumberToTextConverter.toText(cell.getNumericCellValue());
                        }
                    }
                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        */
}
