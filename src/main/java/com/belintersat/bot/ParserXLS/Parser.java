package com.belintersat.bot.ParserXLS;
import com.belintersat.bot.Parser.Lists.BelintersatList;
import com.belintersat.bot.Parser.Lists.HappyBirthdayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class Parser {
    public static BelintersatList parseAbonentList(InputStream in){
        String keyResult = "";
        HSSFWorkbook wb = null;
        BelintersatList list = new BelintersatList();


        try {
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while(it.hasNext()){
            String result = " ";
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while(cells.hasNext()){
                Cell cell = cells.next();
                if(cell.getColumnIndex() == 0){
                    keyResult = cell.getStringCellValue();
                    continue;
                }
                int cellType = cell.getCellType();
                switch (cellType){
                    case Cell.CELL_TYPE_STRING:
                        if(cell.getColumnIndex() == 6){
                            result += cell.getStringCellValue() + ".";
                        } else {
                            result += cell.getStringCellValue() + ", ";
                        }
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        result += "[" +  dateFormat.format(cell.getDateCellValue()) + "], ";
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        result += "[" + (int)cell.getNumericCellValue() + "] ";
                        break;

                    default:
                        result += "|";
                        break;
                }
            }
            list.addMap(keyResult, result);
        }

        return list;
    }



    public static HappyBirthdayList parserBirthday(InputStream in){
        HSSFWorkbook wb = null;
        HappyBirthdayList list = new HappyBirthdayList();

        try {
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while(it.hasNext()){
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while(cells.hasNext()){
                Cell cell = cells.next();
                list.addUser(cell.getStringCellValue());
            }
        }
        return list;
    }

    public static BelintersatList parseSipAbonents(InputStream in){
        HSSFWorkbook wb = null;
        BelintersatList list = new BelintersatList();
        String keyResult = "";
        String result = "";

        try {
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while(it.hasNext()){
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while(cells.hasNext()){
                Cell cell = cells.next();
                if(cell.getColumnIndex() == 0){
                    keyResult = cell.getStringCellValue();
                    continue;
                }
                int cellType = cell.getCellType();
                switch (cellType){
                    case Cell.CELL_TYPE_NUMERIC :
                        result = "[" + (int)cell.getNumericCellValue() + "]";
                        break;
                    case Cell.CELL_TYPE_STRING :
                        result = "[" + cell.getStringCellValue() + "]";
                        break;
                }
            }
            list.addMap(keyResult, result);
        }
        return list;
    }


}



