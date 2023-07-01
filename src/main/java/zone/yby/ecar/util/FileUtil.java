package zone.yby.ecar.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getFileNameWithoutSuffix(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }

    public static void write(String path, String content) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}
