package com.belintersat.bot.ParserXLS;

import com.belintersat.bot.Parser.Lists.BelintersatList;
import com.belintersat.bot.Parser.Lists.GusBelintersatMap;
import com.belintersat.bot.Parser.Lists.HappyBirthdayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.util.stream.Collectors;

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


    public static GusBelintersatMap readODS(File file, Calendar calendar) {
        GusBelintersatMap list = new GusBelintersatMap();

        Sheet sheet = null;

        try {
            sheet = SpreadSheet.createFromFile(file).getSheet(calendar.get(Calendar.MONTH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(sheet == null) return null;
        int columnCount = sheet.getUsedRange().getEndPoint().x;
        int rowCount = sheet.getUsedRange().getEndPoint().y;

        for(int rowIndex = 3; rowIndex <= rowCount; rowIndex++){
            MutableCell cell;
            String key = null;
            ArrayList<String> value = new  ArrayList<>();
            for(int cellIndex = 0; cellIndex <= columnCount; cellIndex++){
                cell = sheet.getCellAt(cellIndex, rowIndex);
                value.add(cell.getTextValue());
                if(cellIndex == 0 ){
                    key = cell.getTextValue();
                }
            }
            list.addMap(key, value);
        }
        return list;
    }

    public static File writeODS(LinkedHashMap<String, ArrayList<String>> map) throws IOException {
        SpreadSheet ooSShet = SpreadSheet.create(1,1,1);
        Sheet firstSheet = ooSShet.getFirstSheet();
        int sizeCol = map.entrySet()
                .stream()
                .findFirst()
                .get()
                .getValue()
                .size();

        firstSheet.setRowCount(map.size());
        firstSheet.setColumnCount(sizeCol);

        Set<String> keySet = map.keySet();
        int row = 0;

        for(String key : keySet){
            ArrayList<String> values = map.get(key);
            for(int cell = 0; cell < values.size(); cell++){
                firstSheet.setValueAt(values.get(cell), cell, row);
            }
            row ++;
        }

        return ooSShet.saveAs(new File("./src/main/resources/files/Test.ods"));
    }



}



