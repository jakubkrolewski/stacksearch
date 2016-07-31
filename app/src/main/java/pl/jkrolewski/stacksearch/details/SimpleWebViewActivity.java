package pl.jkrolewski.stacksearch.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleWebViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.web_view)
    WebView webView;

    public static Intent createIntent(@NonNull Context context, @NonNull String url) {
        return new Intent(context, SimpleWebViewActivity.class)
                .setData(Uri.parse(url));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_webview_activity);
        ButterKnife.bind(this);

        setUpToolbar();
        loadContent();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = checkNotNull(getSupportActionBar());
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadContent() {
        webView.loadUrl(getIntent().getDataString());
    }
}