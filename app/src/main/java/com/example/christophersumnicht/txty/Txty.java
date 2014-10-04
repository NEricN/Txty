package com.example.christophersumnicht.txty;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jruby.compiler.ir.operands.Nil;
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
            System.out.println("Run code initiated.\nRunning:");
            System.out.println(code);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            // IMPORTANT: Save the old System.out!
            PrintStream old = rubyEnv.getOutput();
            // Tell Java to use your special stream
            rubyEnv.setOutput(ps);

            Object temp = rubyEnv.runScriptlet(code);
            ps.flush();
            String result = baos.toString();

            /*try {
                //System.out.println("here");
                ps.flush();
                result += "\n" + temp.toString();
                //System.out.println("Here2");
                //System.out.println(result);
            } catch(Exception e) {
                ps.flush();
                result = baos.toString();
            }

            if(result == "\n" && result == "") {
                ps.flush();
                result = baos.toString();
            }*/
            ps.flush();
            if(temp != null) {
                result = baos.toString() + "\n" + temp.toString();
            } else {
                result = baos.toString();
            }

            rubyEnv.setOutput(old);

            onNewData(result, webView);

            return (rubyEnv.runScriptlet(code).toString());
        }

        private void onNewData(final String str, final WebView webView) {

            runOnUiThread(new Runnable() {
                private String _str = str;
                private WebView _webView = webView;
                public void run() {
                    try {
                        _webView.loadUrl("javascript:addLine(\"" + _str + "\");");
                    } catch(Exception e) {
                        System.out.println("Error'd!");
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
    }
}
