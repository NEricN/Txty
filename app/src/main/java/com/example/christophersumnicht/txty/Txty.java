package com.example.christophersumnicht.txty;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;

import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.LocalVariableBehavior;

public class Txty extends Activity {

    public void onStart() {
        super.onStart();

        WebView webView =  (WebView)findViewById(R.id.mainView);
        //enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(webView, this), "jsinterface");
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txty);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.txty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // this bro does the code execution
    final class JavaScriptInterface {
        private ScriptingContainer rubyEnv;
        private WebView webView;
        private Activity activity;
        JavaScriptInterface (WebView webView, Activity activity) {
            this.webView = webView;
            this.activity = activity;
            rubyEnv = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
        }

        @JavascriptInterface
        public String runCode(String code) {
            System.out.println("Running:");
            System.out.println(code);
            try {
                System.out.println((rubyEnv.runScriptlet(code)).toString());
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            onNewData(code, webView);

            return (rubyEnv.runScriptlet(code).toString());
        }

        private void onNewData(final String str, final WebView webView) {

            runOnUiThread(new Runnable() {
                private String _str = str;
                private WebView _webView = webView;
                public void run() {
                    _webView.loadUrl("javascript:addLine(\"" + (rubyEnv.runScriptlet(_str)).toString() + "\");");
                }
            });
        }
    }
}
