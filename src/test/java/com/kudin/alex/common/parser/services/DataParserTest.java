package com.kudin.alex.common.parser.services;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.junit.Assert.*;

/**
 * Created by KUDIN ALEKSANDR on 28.01.2018.
 */
public class DataParserTest extends Mockito {

    private final static String PATH = "C:\\Users\\homeuser.1-HP\\Desktop\\Прайс шины.xls";
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
        Workbook b = parser.openFile(PATH);
        assertFalse(parser.parseData(b).isEmpty());
    }

    @Test
    public void parseDataWrongFormat() {
        Workbook book = mock(Workbook.class);
        Sheet sheet = mock(Sheet.class);
        Row row = mock(Row.class);
        Cell cell = mock(Cell.class);

        when(book.getSheetAt(0)).thenReturn(sheet);
        when(sheet.getLastRowNum()).thenReturn(7);
        when(sheet.getFirstRowNum()).thenReturn(0);

        when(sheet.getRow(anyInt())).thenReturn(row);
        when(row.getPhysicalNumberOfCells()).thenReturn(7);

        when(isRowAHeader(sheet.getRow(anyInt())).thenReturn(false);
    }

    @Test
    public void parseTireCipher() {
    }

    @Test
    public void isRowAHeader() {
    }
}