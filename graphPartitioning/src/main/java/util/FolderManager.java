package util;

import java.io.File;
import java.io.IOException;

public class FolderManager {

	public static boolean ensureDirExist(String path) throws IOException {
		
		File dir = new File(path);
		if (!dir.isDirectory())
			dir = dir.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		return true;
	}

}
