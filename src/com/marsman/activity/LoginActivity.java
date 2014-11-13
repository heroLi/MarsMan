package com.marsman.activity;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.marsman.R;
import com.marsman.logininfo.AccessTokenKeeper;
import com.marsman.logininfo.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/** 
 * 登陆界面
 * **/
public class LoginActivity extends Activity implements OnClickListener{
	//	
	private Button loginWB;
	
	    /** 授权认证所需要的信息 */
	    private AuthInfo mAuthInfo;
	    /** SSO 授权认证实例 */
	    private SsoHandler mSsoHandler;
	    /** 微博授权认证回调 */
	    private WeiboAuthListener mAuthListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initLogin();
	}
	
	private void initView() {
		loginWB = (Button)findViewById(R.id.login_weibo);
		
		loginWB.setOnClickListener(this);
	}
	
	 /**
     * 设置微博授权所需信息。
     * 
     * @param appKey       第三方应用的 APP_KEY
     * @param redirectUrl  第三方应用的回调页
     * @param scope        第三方应用申请的权限
     * @param authListener 微博授权认证回调接口
     */
	private void initLogin() {
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mAuthListener = new AuthListener();
	}

	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (mSsoHandler != null) {
	            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
	    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_weibo:
			 
			if (null == mSsoHandler && mAuthInfo != null) {
				WeiboAuth weiboAuth = new WeiboAuth(this, mAuthInfo);
				mSsoHandler = new SsoHandler(this, weiboAuth);
			}
			
	        if (mSsoHandler != null) {
	            mSsoHandler.authorize(mAuthListener);
	        } else {
	        	Toast.makeText(LoginActivity.this, 
	                    "授权认证实例 wei null", Toast.LENGTH_SHORT).show();
	        }
			break;

		default:
			break;
		}
		
	}
	 /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, 
                    "授权取消", Toast.LENGTH_SHORT).show();
        }
    }
}
