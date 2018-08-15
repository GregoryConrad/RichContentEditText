package com.gsconrad.richcontentedittextexample;

import android.content.ClipDescription;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Toast;

import com.gsconrad.richcontentedittext.RichContentEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRichContentEditText();
    }

    /**
     * Sets up the RichContentEditText and demos some of its features
     */
    private void setupRichContentEditText() {
        RichContentEditText editText = findViewById(R.id.rich_content_edit_text);

        // Not including the following line in production code increases performance
        // See the javadoc (control click runListenerInBackground) for more information
        editText.runListenerInBackground = false;

        // The following line sets the listener that is called when rich content is received
        editText.setOnRichContentListener(new RichContentEditText.OnRichContentListener() {
            @Override
            public void onRichContent(Uri contentUri, ClipDescription description) {
                if (description.getMimeTypeCount() > 0) {
                    final String fileExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(description.getMimeType(0));
                    final String filename = "filenameGoesHere." + fileExtension;
                    File richContentFile = new File(getFilesDir(), filename);
                    if (!writeToFileFromContentUri(richContentFile, contentUri)) {
                        Toast.makeText(MainActivity.this,
                                R.string.rich_content_copy_failure, Toast.LENGTH_LONG).show();
                    } else {
                        WebView displayView = findViewById(R.id.display_view);
                        displayView.loadUrl("file://" + richContentFile.getAbsolutePath());
                    }
                }
            }
        });
    }

    /**
     * A simple helper method that writes to a file from a content provider uri
     *
     * @param file the file to write to/overwrite
     * @param uri  the content provider uri
     * @return whether the operation was successful or not
     */
    public boolean writeToFileFromContentUri(File file, Uri uri) {
        if (file == null || uri == null) return false;
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
            OutputStream output = new FileOutputStream(file);
            if (stream == null) return false;
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = stream.read(buffer)) != -1) output.write(buffer, 0, read);
            output.flush();
            output.close();
            stream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Couldn't open stream: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException on stream: " + e.getMessage());
        }
        return false;
    }
}
