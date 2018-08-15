# RichContentEditText
A simple wrapper around AppCompatEditText that makes it easier to handle rich content

# Obligatory screenshots
<img height="400" alt="Screenshots" src="https://raw.githubusercontent.com/GregoryConrad/RichContentEditText/master/screenshots.png">

# Adding to your project
Add this to your `Module: app`'s build.gradle file under `dependencies`:

`implementation 'com.gsconrad:richcontentedittext:1.0.0'`

If that does not work, ensure `jcenter()` is in your project build.gradle's `repositories` section—it should be by default.

And if for some reason, you still can't get it to work, try using [Jitpack](https://jitpack.io/#gregoryconrad/richcontentedittext/v1.0.0).

# Usage
For a full working example (where the screenshots came from), see the example app. You can clone this repository and open it in Android Studio to see the example app. But, here are some easy code snippets:

### XML
```
<?xml version="1.0" encoding="utf-8"?>
<!-- Root layout can be anything. Just make sure to include xmlns:app line. -->
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- The RichContentEditText -->
    <!-- Notice app:allowedMimeTypes="images"; it is what accepts certain mime types
             (you can do this programmatically too) -->
    <com.gsconrad.richcontentedittext.RichContentEditText
        android:id="@+id/rich_content_edit_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/rich_content_edit_text_hint"
        android:inputType="text"
        app:allowedMimeTypes="images" />
</android.support.constraint.ConstraintLayout>
```
### Java
```
// MainActivity.java
private void setupRichContentEditText() {
    RichContentEditText editText = findViewById(R.id.rich_content_edit_text);
    // The following line sets the listener that is called when rich content is received
    editText.setOnRichContentListener(new RichContentEditText.OnRichContentListener() {
        // Called when a keyboard sends rich content
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
```

# More information
There isn't much else you need to know, as this library is very simple. If you find any issues, please file an issue. Otherwise, here are some mock code snippets of features you can use:
```
/**
 * Determines whether the listener is run on a background thread or on the main thread
 * True (recommended): runs the listener on a background thread to improve performance
 * False (not advised): runs the listener on the main thread
 * NOTE: if this is true, ensure you update any necessary views on the main thread
 */
public boolean runListenerInBackground

/**
 * Sets the listener that is called whenever rich content is received
 * NOTE: the listener is run on a background thread by default; see runListenerInBackground
 *
 * @param onRichContentListener the listener
 */
public void setOnRichContentListener(OnRichContentListener onRichContentListener)

/**
 * Sets the mime types to accept from a keyboard
 *
 * @param mimeTypes a string array of mime types
 */
public void setContentMimeTypes(String[] mimeTypes)

/**
 * A convenience method for allowing image insertion from a keyboard
 * Sets the appropriate mime types to accept images
 */
public void allowImageInsertion()

/**
 * A convenience method for disallowing rich content from a keyboard
 */
public void disallowRichContent()

/**
 * @return the currently allowed mime types
 */
public String[] getContentMimeTypes()
```
