package com.cyber.kinoost.fragments;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.Constants;
import com.cyber.kinoost.api.vk.sources.Auth;
import com.cyber.kinoost.db.repositories.UserRepository;

public class LoginFragment extends Fragment {
	
	private static final String TAG = "Kate.LoginActivity";
	
	WebView webview;
	Account account;
	UserRepository userRepo;


	public LoginFragment() {
		// Empty constructor required for fragment subclasses
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.login,
				container, false);		
		webview = (WebView) layout.findViewById(R.id.vkontakteview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.clearCache(true);

		// Чтобы получать уведомления об окончании загрузки страницы
		webview.setWebViewClient(new VkontakteWebViewClient());
		
		CookieSyncManager.createInstance(getActivity());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		String url = Auth.getUrl(Constants.API_ID, Auth.getSettings());
		webview.loadUrl(url);

		return webview;
	}
	
	private void parseUrl(String url) {
		try {
			if (url == null)
				return;
			Log.i(TAG, "url=" + url);
			if (url.startsWith(Auth.redirect_url)) {
				if (!url.contains("error=")) {
					String[] auth = Auth.parseRedirectUrl(url);
					
					account = new Account(getActivity(), auth[0], Long.parseLong(auth[1]));
					account.save();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class VkontakteWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			parseUrl(url);
		}
	}


}
