package Utils;

import java.io.File;

public class DeleteFile {
    public void deleteFile(String filename) {
        File toDelete = new File(filename);
        if (toDelete.exists())
            toDelete.delete();
    }
}
