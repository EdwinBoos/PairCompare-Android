package influenz.de.paircompare.util;


import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class HaarCascadeLoader {

    private File cascadeFile;
    private File cascadeDir;

    public HaarCascadeLoader(Context context, int cascadeFilePath, String  fileName) {


        InputStream is = context.getResources().openRawResource(cascadeFilePath);
        cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
        cascadeFile = new File(cascadeDir, fileName);

        try {
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAbsolutePath() {
        return cascadeFile.getAbsolutePath();
    }

    public HaarCascadeLoader deleteCascadeDir() {
        cascadeDir.delete();
        return this;
    }




}

