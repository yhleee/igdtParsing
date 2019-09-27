package Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    public static List<String[]> readCSV(String path) {
        //반환용 리스트
        List<String[]> ret = new ArrayList<String[]>();
        BufferedReader br = null;

        try {
            File csv = new File(path);
            br = new BufferedReader(new FileReader(csv));
            br.readLine(); // skip first line
            String line = "";

            while ((line = br.readLine()) != null) {
                String array[] = line.split(",");

                    System.out.println(array[0] + " " + array[1]);
                    ret.add(array);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

}
