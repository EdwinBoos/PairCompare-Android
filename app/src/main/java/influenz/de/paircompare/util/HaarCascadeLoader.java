package influenz.de.paircompare.util;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public final class HaarCascadeLoader {

    private final File cascadeFile;
    private final File cascadeDir;
    private final int cascadeFilePath;
    private final Context context;

    public HaarCascadeLoader(final Context context, final int cascadeFilePath)
    {
        this.context = context;
        this.cascadeFilePath = cascadeFilePath;
        this.cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
        this.cascadeFile = new File(cascadeDir, String.valueOf(cascadeFilePath) + ".xml");
    }

    public File load()
    {

        final InputStream is = this.context.getResources().openRawResource(this.cascadeFilePath);
        try
        {

            final FileOutputStream os = new FileOutputStream(this.cascadeFile);
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

        return this.cascadeFile;

    }

    public HaarCascadeLoader deleteCascadeDir()
    {
        this.cascadeDir.delete();
        return this;
    }

}

