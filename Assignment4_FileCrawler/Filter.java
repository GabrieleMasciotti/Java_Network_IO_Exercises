//package filecrawler;

import java.io.File;
import java.io.FileFilter;

public class Filter implements FileFilter{

    @Override
    public boolean accept(File arg0) {
        return arg0.isDirectory();
    }
    
}
