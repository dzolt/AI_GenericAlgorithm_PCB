package pl.damian.zoltowski.visualization;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonProcessBuilder {

    private final String pathToJson = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\data\\";
    private final String pathToResult = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\results\\";
    private final String python = "C:\\Users\\Legion\\AppData\\Local\\Microsoft\\WindowsApps\\python3.9";
    private final String script = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\python\\generator.py";
    public void generatePCBImageToFile(String pcbJsonFileName, String outputFileName) {
        try {
            String pcbJsonFile = pathToJson + pcbJsonFileName;
            String resultFile = pathToResult + outputFileName;
            Process process = Runtime.getRuntime().exec(
              new String[] {
                python,
                script,
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

}
