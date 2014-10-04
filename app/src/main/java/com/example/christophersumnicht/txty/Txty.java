package com.example.christophersumnicht.txty;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.LocalVariableBehavior;

public class Txty extends Activity {

    private ScriptingContainer rubyEnv;

    public void onStart() {
        super.onStart();

        rubyEnv = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
        WebView webView =  (WebView)findViewById(R.id.mainView);
        //enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
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

    private void addCode(String addendum) {
        rubyEnv.runScriptlet(addendum);
    }
}
