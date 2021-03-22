package pl.damian.zoltowski.visualization;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonProcessBuilder {

    private final String PYTHON = "C:\\Users\\Legion\\AppData\\Local\\Microsoft\\WindowsApps\\python3.9";

    private final String PATH_TO_JSON_BOARD = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\data\\board\\";
    private final String PATH_TO_RESULT_BOARD = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\results\\board\\";

    private final String PATH_TO_JSON_CHART = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\data\\chart\\";
    private final String PATH_TO_RESULT_CHART = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\results\\chart\\";

    private final String BOARD_SCRIPT = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\python\\generator.py";
    private final String CHART_SCRIPT = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\python\\chartDraw.py";

    public void generatePCBImageToFile(String pcbJsonBoardFileName, String outputBoardFileName) {
        try {
            String pcbJsonFile = PATH_TO_JSON_BOARD + pcbJsonBoardFileName;
            String resultFile = PATH_TO_RESULT_BOARD + outputBoardFileName;
            Process process = Runtime.getRuntime().exec(
              new String[] {
                PYTHON,
                BOARD_SCRIPT,
                pcbJsonFile,
                resultFile
              }
            );
            //for debugging
//            String cmdO = null;
//            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            BufferedReader brEr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            while((cmdO = br.readLine()) != null) {
//                System.out.println(cmdO);
//            }
//
//            while((cmdO = brEr.readLine()) != null) {
//                System.out.println(cmdO);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generatePCBChartImageToFile(String pcbJsonChartFileName, String outputChartFileName) {
        try {
            String pcbJsonFile = PATH_TO_JSON_CHART + pcbJsonChartFileName;
            String resultFile = PATH_TO_RESULT_CHART + outputChartFileName;
            Process process = Runtime.getRuntime().exec(
              new String[] {
                PYTHON,
                CHART_SCRIPT,
                pcbJsonFile,
                resultFile
              }
            );
            //for debugging
            String cmdO = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader brEr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while((cmdO = br.readLine()) != null) {
                System.out.println(cmdO);
            }

            while((cmdO = brEr.readLine()) != null) {
                System.out.println(cmdO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
