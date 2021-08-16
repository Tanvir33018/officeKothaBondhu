package net.islbd.kothabondhu.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.databinding.FragmentWebViewBinding;

public class WebViewFragment extends Fragment {


    private FragmentWebViewBinding mBinding;
    private String orderId;
    private String amount;
    private String url;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        orderId = getArguments().getString("mOrderId","N/A");
        amount = getArguments().getString("amount","N/A");
        url = "http://kothabondhu.com/nagadapi/index_payment.php?orderid="+orderId+"&amount="+amount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        mBinding = FragmentWebViewBinding.inflate(getLayoutInflater());
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.setWebViewClient(new WebViewClient());
        mBinding.webView.loadUrl(url);

        return mBinding.getRoot();
    }
}