package com.marsman.activity;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.marsman.R;
import com.marsman.logininfo.AccessTokenKeeper;
import com.marsman.logininfo.AppConstants;
import com.marsman.logininfo.Constants;
import com.marsman.utils.Util;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 登陆界面
 * **/
public class LoginActivity extends Activity implements OnClickListener {
	//
	private Button loginWB, loginQQ, loginIn;

	/** 授权认证所需要的信息 */
	private AuthInfo mAuthInfo;
	/** SSO 授权认证实例 */
	private SsoHandler mSsoHandler;
	/** 微博授权认证回调 */
	private WeiboAuthListener mAuthListener;
	/**  qq登陆 */
	private Tencent mTencent;
	
	 public static QQAuth mQQAuth;
	 
	  private UserInfo mInfo;
	  
	  private TextView loginText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initLogin();
	}

	private void initView() {
		loginWB = (Button) findViewById(R.id.login_weibo);
		loginIn = (Button) findViewById(R.id.login_in);
		loginQQ = (Button) findViewById(R.id.login_qq);
		loginText = (TextView) findViewById(R.id.loginText);

		loginWB.setOnClickListener(this);
		loginIn.setOnClickListener(this);
		loginQQ.setOnClickListener(this);
	}

	/**
	 * 设置微博授权所需信息。
	 * 
	 * @param appKey
	 *            第三方应用的 APP_KEY
	 * @param redirectUrl
	 *            第三方应用的回调页
	 * @param scope
	 *            第三方应用申请的权限
	 * @param authListener
	 *            微博授权认证回调接口
	 */
	private void initLogin() {
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		mAuthListener = new AuthListener();

		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
		// 其中APP_ID是分配给第三方应用的appid，类型为String。
		mQQAuth = QQAuth.createInstance(AppConstants.APP_ID, this.getApplicationContext());
		mTencent = Tencent.createInstance(AppConstants.APP_ID, this.getApplicationContext());
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
				Toast.makeText(LoginActivity.this, "授权认证实例 wei null",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.login_in:
			startActivity(new Intent(this, MarsActivity.class));
			break;
		case R.id.login_qq:
		onClickLogin();
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
			Oauth2AccessToken accessToken = Oauth2AccessToken
					.parseAccessToken(values);
			if (accessToken != null && accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(accessToken.getExpiresTime()));
				AccessTokenKeeper.writeAccessToken(getApplicationContext(),
						accessToken);
				startActivity(new Intent(LoginActivity.this,
						WeiBoLoginActivity.class));
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(LoginActivity.this, e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT)
					.show();
		}
	}
	/**
	 * qq授权登陆
	 * */
	private void onClickLogin() {
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					updateUserInfo();
				}
			};
			//mQQAuth.login(this, "all", listener);
			mTencent.loginWithOEM(this, "all", listener,"10000144","10000144","xxxx");
		} else {
			mQQAuth.logout(this);
			updateUserInfo();
		}
	}
	
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject)response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {
		}
	}
	
	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				
				@Override
				public void onError(UiError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread(){

						@Override
						public void run() {
							JSONObject json = (JSONObject)response;
							if(json.has("figureurl")){
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
								} catch (JSONException e) {
									
								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}
						
					}.start();
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			};
//			  MainActivity.mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null,
//	                    Constants.HTTP_GET, requestListener, null);
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);
			
		} else {
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					
						loginText.setText("qq登錄信息"+response.toString());
					
				}
			}else if(msg.what == 1){
				Bitmap bitmap = (Bitmap)msg.obj;
			}
		}

	};
}
