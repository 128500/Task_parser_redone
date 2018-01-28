package com.kudin.alex.common.parser.services;

import com.kudin.alex.common.parser.entities.Tire;
import lombok.extern.log4j.Log4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;


/**
 * Created by KUDIN ALEKSANDR on 26.01.2018.
 *
 * This class contains methods to parse the given Excel file
 * Parsing data transfer to the Tire entity ({@see Tire.class})
 * and the entities at the same time fill the List
 *
 * Note that the class methods relies on the specific format of
 * the table (tires). If the table is changing or changing the
 * format of its cells the class methods must be adapted to new
 * conditions.
 */

@Service
@Log4j
public class DataParser {

    /*A map to manage double word names of manufacturers (nothing else comes to mind)*/
    private final static Map<String, String> MANUFACTURERS = new HashMap<>();

    /*Static initializer of MANUFACTURERS*/
    static {
        MANUFACTURERS.put("Viking", "Tyres");
    }

    public List<Tire> parseFile(final String filePath){
        return parseData(openFile(filePath));
    }

    /**
     * Opens the given Excel file with FileInputStream
     * Note! Public access is given only for testing purposes.
     * In production it must be changed to 'private'.
     * @param filePath path of the file to open
     * @return Workbook entity to work with
     */
    public Workbook openFile(final String filePath){
        try(FileInputStream fis = new FileInputStream(new File(filePath))){
            log.info("Trying to access the file " + filePath);
            return new HSSFWorkbook(fis);
        } catch (FileNotFoundException e1){
            e1.printStackTrace();
            log.warn("Couldn't find the file with path: " + filePath);
        }catch(org.apache.poi.poifs.filesystem.OfficeXmlFileException e2){
            e2.printStackTrace();
            log.warn("CANNOT OPEN THE FILE WITH WRONG EXTENSION " + filePath + ". EXTENSION MUST BE 'xls'.");
        }
        catch(IOException e3){
            e3.printStackTrace();
            log.error(e3);
        }
        throw new IllegalStateException("CANNOT FIND OR OPEN FILE!");
    }

    /**
     * Parses data of the given Workbook entity (Excel file)
     * Note! Public access is given only for testing purposes.
     * In production it must be changed to 'private'.
     * @param workbook - workbook to parse
     * @return parsed data as a list of Tire entities
     */
    public List<Tire> parseData(final Workbook workbook){
        List<Tire> tires = new ArrayList<>();
        try{
            Sheet sheet = workbook.getSheetAt(0); // If there is more then one sheet to parse must be changed
            int rowNumber = sheet.getLastRowNum()+1;
            int i = sheet.getFirstRowNum()+1;

            /*While we are not at the beginning of the table skip rows*/
            while(sheet.getRow(i).getPhysicalNumberOfCells() < 7) i++;

            /*
              While row is a header skip it
              @see isRowAHeader() method below
             */
            while(isRowAHeader(sheet.getRow(i))) i++;

            /*For each row of the table do decipher*/
            for( ; i<= rowNumber; i++){
                Row r = sheet.getRow(i);
                Tire t = null;
                if(r != null){
                    String tireName = r.getCell(2).getStringCellValue();
                    if(tireName != null && !tireName.isEmpty()){
                        t = parseTireCipher(tireName);
                    }
                    else{
                        log.error("THE CELL AT ROW " + (i+1) + " AND COLUMN 'C' IS EMPTY. INFO OF THIS ROW WON'T BE SAVED");
                        break;
                    }
                    t.setType(r.getCell(0).getStringCellValue().replaceAll("\u0000", ""));
                    t.setSeason(r.getCell(1).getStringCellValue().replaceAll("\u0000", ""));
                    t.setBalance((int)r.getCell(3).getNumericCellValue());
                    t.setPrice(r.getCell(4).getNumericCellValue());
                    if(t.getPrice() == 0.0){
                        log.warn("The cell at ROW N" + (i+1) + " and COLUMN 'E' has value of 0.0 or empty.");
                    }
                    t.setCountry(r.getCell(5).getStringCellValue().replaceAll("\u0000", ""));

                    if(r.getCell(6).getCellTypeEnum().equals(CellType.NUMERIC)) {
                        int year = (int)r.getCell(6).getNumericCellValue();
                        String yearr = String.valueOf(year);
                        if (yearr.length() > 2) {
                            if (yearr.length() == 3) {
                                year = Integer.valueOf(yearr.substring(1, 3)) + 2000;
                            } else {
                                year = Integer.valueOf(yearr.substring(2, 4)) + 2000;
                            }
                        }
                        else year += 2000;
                        if(year > 2090) year = year - 100;
                        if(year > 2018 && year <= 2090){
                            log.warn("Incorrect year format at ROW N" + (i+1) + " and COLUMN 'G'." +
                                    " Value of " + year + " is more then the current year." );
                            year = 0;
                        }
                        t.setYear(year);
                    }

                    if(r.getCell(6).getCellTypeEnum().equals(CellType.STRING)){
                        log.warn("Incorrect year format at ROW N" + (i+1) + " and COLUMN 'G'");
                        t.setYear(0);
                    }

                    tires.add(t);
                    System.out.println(t.toString());
                }
            }
        }catch (Exception e){
            log.error(e);
        }
        return tires;
    }

    /**
     * Decipher the value of the cell of tires table with the name of 'Название'
     * Note! Public access is given only for testing purposes.
     * In production it must be changed to 'private'.
     * @param info the string to decipher
     * @return Tire entity with set deciphered fields
     */
    public Tire parseTireCipher(final String info){
        Tire t = new Tire();
        String[] parts = info.split(" ");
        final boolean[] indexMark = {false};

        /*This Consumer interface parse each part of the splitting string of the column 'Название' of
        * the table (tires) according to multiple 'if' statements and Regex expressions
        * containing in them
        */
        Consumer<String> decipher = x -> {                              //Java 8 Functional interfaces
            if(x.matches("\\d{3}/\\d{2}")){
                t.setWidth(Integer.valueOf(x.substring(0, 3)));
                t.setHeight(Integer.valueOf(x.substring(4)));
                return;
            }

            if(x.matches("R\\d{2},?\\d?C?")){
                t.setDiameter(Double.valueOf(x.replaceAll("[RC]", "").replaceAll(",", ".")));

                if(x.contains("C")){
                    t.setStrengthen(true);
                }
                return;
            }

            if(x.matches("\\d{2}[TH]")){
                t.setLoadIndex(x.substring(0,2));
                t.setSpeedIndex(x.substring(2));
                indexMark[0] = true;
                return;
            }

            if(x.matches("\\d{3}[RL]?/\\d{3}[RL]?")){
                t.setLoadIndex(x.replaceAll("[RL]", ""));
                String speedIndex = x.replaceAll("\\d{3}", "");
                if(speedIndex.length() == 2) speedIndex = speedIndex.replaceAll("/", "");
                t.setSpeedIndex(speedIndex);
                indexMark[0] = true;
                return;
            }

            if(x.matches("XL") && indexMark[0]){
                t.setStrengthen(true);
            }
            if(x.matches("шип") && indexMark[0]){
                t.setStudded(true);
            }
            if(x.matches("[^XL]{1,2}") && indexMark[0]){
                t.setAdditionalParam(x);
            }

            if(x.matches("\\w*") && !indexMark[0] && t.getManufacturer() == null){
                t.setManufacturer(x);
                return;
            }

            if(x.matches("\\w*") && !indexMark[0] && t.getManufacturer() != null){
                if(MANUFACTURERS.containsKey(t.getManufacturer())){
                    t.setManufacturer(t.getManufacturer().concat(" ").concat(x));
                    return;
                }
                if(t.getModel() == null) t.setModel(x);
                else t.setModel(t.getModel().concat(" ").concat(x));
            }
        };

        Arrays.stream(parts).forEachOrdered(p -> decipher.accept(p));           //Java 8 Lambdas and Functional interfaces

        /*If there is NULL value of the field additionalParam, set it to "", to avoid NullPointerException*/
        if(t.getAdditionalParam() == null) t.setAdditionalParam("");
        return t;
    }

    /**
     * Determines if the given row is a header of a table
     * Note! Public access is given only for testing purposes.
     * In production it must be changed to 'private'.
     * @param row - a row to analyze
     * @return true if all the cells at the given row have String type, false if not
     */
    public boolean isRowAHeader(final Row row){
        return row.getCell(0).getCellTypeEnum() == CellType.STRING &&
                row.getCell(1).getCellTypeEnum() == CellType.STRING &&
                row.getCell(2).getCellTypeEnum() == CellType.STRING &&
                row.getCell(3).getCellTypeEnum() == CellType.STRING &&
                row.getCell(4).getCellTypeEnum() == CellType.STRING &&
                row.getCell(5).getCellTypeEnum() == CellType.STRING &&
                row.getCell(6).getCellTypeEnum() == CellType.STRING;
    }

}
