package com.kudin.alex.common.parser.services;

import com.kudin.alex.common.parser.entities.Tire;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by KUDIN ALEKSANDR on 28.01.2018.
 */
public class DataParserTest extends Mockito {

    private final static String PATH = "C:\\Users\\homeuser.1-HP\\Desktop\\Прайс шины.xls";
    private final static String WRONG_FORMAT_TABLE_PATH = "C:\\Users\\homeuser.1-HP\\Desktop\\price_tires_wrong_cell_format.xls";
    private final static String STRING_TO_PARSE = "165/65 R15 Continental ContiWinterContact TS850 81T XL шип FR";
    private DataParser parser = new DataParser();

    @Before
    public void setUp() throws Exception {
        parser = new DataParser();
    }

    @Test
    public void parseFile() {
        assertFalse(parser.parseFile(PATH).isEmpty());
    }

    @Test
    public void openFile() {
        assertTrue(parser.openFile(PATH).getClass() == HSSFWorkbook.class);
    }

    @Test(expected = IllegalStateException.class)
    public void openFileWithWrongPath() {
        parser.openFile("C:\\Users\\homeuser.1-HP\\Desktop\\Прайс.xls");
    }

    @Test(expected = IllegalStateException.class)
    public void openFileWithWrongExtension() {
        parser.openFile("C:\\Users\\homeuser.1-HP\\Desktop\\Тестовое задание на позицию Java Developer.docx");
    }

    @Test
    public void parseData() {
        Workbook book = parser.openFile(PATH);
        assertFalse(parser.parseData(book).isEmpty());
    }

    @Test
    public void parseDataWrongFormat() {
        Workbook book = parser.openFile(WRONG_FORMAT_TABLE_PATH);
        parser.parseData(book);
    }

    @Test
    public void parseTireCipher() {
       Tire t = parser.parseTireCipher(STRING_TO_PARSE);
       assertTrue(t.getManufacturer() != null);
       assertTrue(t.getModel() != null);
       assertTrue(t.getLoadIndex() != null);
       assertTrue(t.getSpeedIndex() != null);
       assertTrue(t.getAdditionalParam() != null);
       assertTrue(t.getDiameter() != 0.0);
       assertTrue(t.getWidth() != 0.0);
       assertTrue(t.getHeight() != 0.0);
    }

    @Test
    public void isRowAHeaderTrue() {
        Workbook book = parser.openFile(PATH);
        Sheet sheet = book.getSheetAt(0);
        Row headerRow = sheet.getRow(1);
        assertTrue(parser.isRowAHeader(headerRow));
    }

    @Test
    public void isRowAHeaderFalse() {
        Workbook book = parser.openFile(PATH);
        Sheet sheet = book.getSheetAt(0);
        Row headerRow = sheet.getRow(2);
        assertFalse(parser.isRowAHeader(headerRow));
    }
}