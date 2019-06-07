import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ExcelExport {



    public void excelExport_unmatched (ArrayList<String[]> unmatchedList, ArrayList<String[]> matchedList) throws IOException {

        Map<String, String> map = null;

        XSSFWorkbook workbook = new XSSFWorkbook();

        //unmachedList create
        XSSFSheet sheet2 = workbook.createSheet("matchedList");
        XSSFSheet sheet1 = workbook.createSheet("unmatchedList");

        XSSFRow row1 = sheet1.createRow(0);
        XSSFRow row2 = sheet2.createRow(0);

        XSSFCell cell1 ;
        XSSFCell cell2 ;

        //헤더 정보 구성 - unmatched
        cell1 = row1.createCell(0);
        cell1.setCellValue("gds_id");

        cell1 = row1.createCell(1);
        cell1.setCellValue("gds_nm");

        cell1 = row1.createCell(2);
        cell1.setCellValue("igdt_nm");

        //헤더 정보 구성 - matched
        cell2 = row2.createCell(0);
        cell2.setCellValue("gds_id");

        cell2 = row2.createCell(1);
        cell2.setCellValue("gds_nm");

        cell2 = row2.createCell(2);
        cell2.setCellValue("igdt_cd");

        cell2 = row2.createCell(3);
        cell2.setCellValue("igdt_nm");

        cell2 = row2.createCell(4);
        cell2.setCellValue("igdt_nm_en");

        for(int rowlistcount = 0 ; rowlistcount < unmatchedList.size() ; rowlistcount++){
            String[] strArr = unmatchedList.get(rowlistcount);
            //행 생성
            row1 = sheet1.createRow(rowlistcount+1);

            cell1 = row1.createCell(0);
            cell1.setCellValue(strArr[0]);

            cell1 = row1.createCell(1);
            cell1.setCellValue(strArr[1]);

            cell1 = row1.createCell(2);
            cell1.setCellValue(strArr[2]);

        }

        for(int rowlistcount = 0 ; rowlistcount < matchedList.size() ; rowlistcount++){
            String[] strArr = matchedList.get(rowlistcount);
            //행 생성
            row2 = sheet2.createRow(rowlistcount+1);

            cell2 = row2.createCell(0);
            cell2.setCellValue(strArr[0]);

            cell2 = row2.createCell(1);
            cell2.setCellValue(strArr[2]);

            cell2 = row2.createCell(2);
            cell2.setCellValue(strArr[1]);

            cell2 = row2.createCell(3);
            cell2.setCellValue(strArr[3]);

            cell2 = row2.createCell(4);
            cell2.setCellValue(strArr[4]);

        }


        File file = new File("C:\\Dev\\result.xlsx");
        FileOutputStream fos = null;

        try{
            fos = new FileOutputStream(file);
            workbook.write(fos);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(workbook != null) workbook.close();
                if(fos != null) fos.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }





    }


}
