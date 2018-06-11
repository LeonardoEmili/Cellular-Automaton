package inputWindow;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.io.*;

public class InputBox {

    // Default values, the user inputs have be passed to handleInput
    final static String folderName = "Configurations";
    final static String fileName ="config";
    private static HashMap<String, Integer> map;
    private static Type type = new TypeToken<HashMap<String, Integer>>() {}.getType();

    /*public static void open(Type type) {
        try {
            JsonReader reader = new JsonReader(new FileReader("Configurations/"+ fileName + ".stb"));
            HashMap<String, Integer> map_file = new Gson().fromJson(reader, type);
            System.out.println(map_file.get("States"));
            } catch (FileNotFoundException ex) {
            ColorPickerBox.show("File does not exist", "Error");
        }
    }

    public static void save(Slider s1, Slider s2, Slider s3, String customFileName) {
        if (customFileName.length() == 0)
            customFileName = fileName;
        if (fileAlreadyExists(customFileName)) {
            ColorPickerBox.show("This profile name already exists, choose another one.", "Profile Name already taken");
            return;
        }
        map = new HashMap<>();
        map.put("Height", Integer.parseInt(s1.valueProperty().asString("%.0f").getValue()));
        map.put("Width", Integer.parseInt(s2.valueProperty().asString("%.0f").getValue()));
        map.put("States", Integer.parseInt(s3.valueProperty().asString("%.0f").getValue()));
        jsonDump(map, type, customFileName);
    } */

    public static void jsonDump(HashMap<String, Integer> map, Type type, String customFileName) {

        Gson gson = new Gson();
        String parsedDict = gson.toJson(map, type);
        System.out.println(parsedDict);
        try {
            FileWriter f = new FileWriter(folderName +"/"+ customFileName + ".stb");
            f.write(parsedDict);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileAlreadyExists(String customFileName){
        File folder = new File("./" + folderName);
        if (!folder.exists())
            folder.mkdir();
        String cwd = System.getProperty("user.dir") + "/" + folderName;
        File dir = new File(cwd);
        for (File f: dir.listFiles()) {
            if (f.isDirectory())
                continue;
            if (f.getName().equals(customFileName + ".stb"))
                return true;
        }
        return false;
    }

}