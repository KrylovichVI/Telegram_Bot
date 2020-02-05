package com.belintersat.bot.ParserXLS;

import com.belintersat.bot.Parser.Lists.BelintersatList;
import com.belintersat.bot.Parser.Lists.GusBelintersatMap;
import com.belintersat.bot.Parser.Lists.HappyBirthdayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parser {

    public static final int DATA_OF_EMPLOYEES = 3;

    public static BelintersatList parseAbonentList(String path){
        String keyResult = "";
        HSSFWorkbook wb = null;
        BelintersatList list = new BelintersatList();


        try {
            wb = new HSSFWorkbook(new FileInputStream(path));
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

                switch (cell.getCellType()){
                    case STRING:
                        if(cell.getColumnIndex() == 6){
                            result += cell.getStringCellValue() + ".";
                        } else {
                            result += cell.getStringCellValue() + ", ";
                        }
                        break;
                    case NUMERIC:
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        result += "[" +  dateFormat.format(cell.getDateCellValue()) + "], ";
                        break;
                    case FORMULA:
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



    public static HappyBirthdayList parserBirthday(String path){
        HSSFWorkbook wb = null;
        HappyBirthdayList list = new HappyBirthdayList();

        try {
            wb = new HSSFWorkbook(new FileInputStream(path));
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

    public static BelintersatList parseSipAbonents(String path){
        HSSFWorkbook wb = null;
        BelintersatList list = new BelintersatList();
        String keyResult = "";
        String result = "";

        try {
            wb = new HSSFWorkbook(new FileInputStream(path));
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

                switch (cell.getCellType()){
                    case NUMERIC:
                        result = "[" + (int)cell.getNumericCellValue() + "]";
                        break;
                    case STRING:
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
        String path = "./src/main/resources/files/Orbit_Plan.ods";
        File file = new File(path);
        if(file.exists() && !file.isDirectory()){
            file.delete();
        }

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
                if(values.get(cell).contains(":")){
                    firstSheet.setValueAt(values.get(cell) + " UTC", cell, row);
                }else{
                    firstSheet.setValueAt(values.get(cell), cell, row);
                }
            }
            row ++;
        }

        return ooSShet.saveAs(new File(path));
    }

    public static String changeOfEmployees(String path, Calendar calendar){
        if(!new File(path).exists()) {
            return null;
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String strDay = "Дневная смена:\n";
        String strNight = "Ночная смена:\n";

        int length = (strDay + strNight).length();

        XSSFWorkbook wb = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
            wb = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(wb != null){
            int indexCol = -1;
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();


            while(it.hasNext()){
                Row row = it.next();

                if(indexCol > -1){
                    Iterator<Cell> cellIterator = row.iterator();
                    while(cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        if(cell.getColumnIndex() == indexCol  && (cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() == 12)){
                            Color fillBackgroundColor = cell.getCellStyle().getFillBackgroundColorColor();
                            short fontIndex = cell.getCellStyle().getFontIndex();
                            if(fillBackgroundColor == null){
                                //дневная смена
                                strDay += getString(strDay, row, fontIndex);
                            } else {
                                //ночная смена
                                strNight += getString(strNight, row, fontIndex);
                            }
                        }

                    }
                }

                if( row.getRowNum() == DATA_OF_EMPLOYEES){
                    Iterator<Cell> cellIterator = row.iterator();
                    while(cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        if(cell.getCellType() == CellType.NUMERIC &&  (int) cell.getNumericCellValue() == day){
                            indexCol = cell.getColumnIndex();
                            break;
                        }
                    }
                }
            }

        }

        try {
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String res = (length + 1) != (strDay + "\n" + strNight).length() ? (strDay + "\n" + strNight) : null;
        return  res;
    }

    private static String getString(String strNight, Row row, short fontIndex) {
        String department = getDepartment(fontIndex);
        if(department != null){
            strNight = department + row.getCell(1).getStringCellValue() + "\n";
        }
        return strNight;
    }

    private static String getDepartment(int colorIndex){
        if(colorIndex == 5 || colorIndex == 11){
            return "НДС: ";
        } else if(colorIndex == 9 || colorIndex == 8){
            return "Аналитик: ";
        } else if(colorIndex == 15){
            return "Оператор: ";
        } else if(colorIndex == 17){
            return "ДИ МПН: ";
        } else if(colorIndex == 20){
            return "ДИ СНП: ";
        }
        return null;
    }
}



