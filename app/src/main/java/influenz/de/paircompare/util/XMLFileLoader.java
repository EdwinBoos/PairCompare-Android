package influenz.de.paircompare.util;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public final class XMLFileLoader {

    private final File rawFile;
    private final File rawDir;
    private final int rawFilePath;
    private final Context context;
    private final String fileExtension = ".xml";

    public XMLFileLoader(final Context context, final int rawFilePath)
    {
        this.context = context;
        this.rawFilePath = rawFilePath;
        this.rawDir = context.getDir("cascade", Context.MODE_PRIVATE);
        this.rawFile = new File(rawDir, String.valueOf(rawFilePath) + fileExtension);
    }

    public File load()
    {

        final InputStream is = this.context.getResources().openRawResource(this.rawFilePath);
        try
        {

            final FileOutputStream os = new FileOutputStream(this.rawFile);
            int bytesRead;
            final byte[] buffer = new byte[4096];
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return this.rawFile;

    }

    public XMLFileLoader deleteCascadeDir()
    {
        this.rawDir.delete();
        return this;
    }

}

