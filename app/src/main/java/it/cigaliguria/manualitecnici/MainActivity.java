package it.cigaliguria.manualitecnici;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.*;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.*;
import java.util.Base64;

public class MainActivity extends Activity {
    private WebView web;
    private ValueCallback<Uri[]> fileCallback;
    private static final int PICK_PDF = 41;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(android.graphics.Color.rgb(3,26,49));
        web = new WebView(this);
        web.setBackgroundColor(android.graphics.Color.rgb(3,23,43));
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setSupportMultipleWindows(false);
        s.setJavaScriptCanOpenWindowsAutomatically(false);
        web.addJavascriptInterface(new PdfBridge(), "AndroidBridge");
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("application/pdf");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                startActivityForResult(i, PICK_PDF);
                return true;
            }
        });
        web.setWebViewClient(new WebViewClient());
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    private class PdfBridge {
        @JavascriptInterface public void openPdf(String base64, String name) {
            runOnUiThread(() -> {
                try {
                    byte[] data = Base64.getDecoder().decode(base64);
                    File dir = new File(getCacheDir(), "pdf");
                    if (!dir.exists()) dir.mkdirs();
                    String safe = (name == null ? "manuale.pdf" : name).replaceAll("[^a-zA-Z0-9._-]", "_");
                    if (!safe.toLowerCase().endsWith(".pdf")) safe += ".pdf";
                    File file = new File(dir, safe);
                    try (FileOutputStream out = new FileOutputStream(file)) { out.write(data); }
                    Uri uri = FileProvider.getUriForFile(MainActivity.this, getPackageName()+".provider", file);
                    Intent view = new Intent(Intent.ACTION_VIEW);
                    view.setDataAndType(uri, "application/pdf");
                    view.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(view, "Apri manuale PDF"));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Impossibile aprire il PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF && fileCallback != null) {
            Uri[] result = (resultCode == RESULT_OK && data != null && data.getData() != null) ? new Uri[]{data.getData()} : null;
            fileCallback.onReceiveValue(result);
            fileCallback = null;
        }
    }
    @Override public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
}
