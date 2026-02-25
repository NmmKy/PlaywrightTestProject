package helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class PropertiesHelper {
    private static final Properties PROPERTIES = new Properties();
    private static List<String> fileNames = new ArrayList<>();

    static {
        ArrayList<String> dataFileNames = getFileNames("resources.location");
        Path resourceDirectory = Paths.get("src","test","resources");
        try {
            setProperties(getFilePathsFromDirectory(resourceDirectory));
            loadProperties(fileNames);
            loadProperties(dataFileNames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<String> getFileNames(String fileName) {
        ArrayList<String> configFileNames = new ArrayList<>();
        for (String key: System.getProperties().stringPropertyNames()) {
            if (key.startsWith(fileName)) {
                String[] fileNames = System.getProperties().getProperty(key)
                        .split(";");
                configFileNames.addAll(Arrays.asList(fileNames));
            }
        }
        return configFileNames;
    }

    public static void setProperties(List<String> filePaths) {
        fileNames.addAll(filePaths);
    }

    public static List<String> getFilePathsFromDirectory(Path pathDirectory) throws IOException {
        List<String> filesPaths;
        filesPaths = Files.list(pathDirectory)
                .map(x -> {
                    try {
                        return x.toRealPath().toString();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return filesPaths;
    }

    public static void loadProperties(List<String> fileNames) throws IOException {
        for (String fileName: fileNames) {
            Path path = Paths.get(fileName);
            PROPERTIES.load(Files.newInputStream(path));
        }
    }

    public static String getProperty(String key){
        return PROPERTIES.getProperty(key);
    }
}
