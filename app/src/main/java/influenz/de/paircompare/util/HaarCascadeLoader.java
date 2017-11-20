package influenz.de.paircompare.util;


import android.content.Context;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import influenz.de.paircompare.R;

public class CascadeClassifierLoader extends CascadeClassifier {

    private File cascadeFile;

    public CascadeClassifierLoader(Context context, String cascadeFilePath) throws FileNotFoundException {

        super(cascadeFilePath);

        InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_default);
        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
        cascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");

        try
        {
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            cascadeDir.delete();

        } catch (IOException e)
        {
            e.printStackTrace();
        }



    }
}
