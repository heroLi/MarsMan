package com.marsman.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marsman.R;
import com.marsman.logininfo.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 微博登陆
 * */
public class WeiBoLoginActivity extends Activity {
	/** UI 元素：ListView */
	private ListView mFuncListView;
	/** 当前 Token 信息 */
	private Oauth2AccessToken mAccessToken;
	/** 用户信息接口 */
	private UsersAPI mUsersAPI;

	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_text);

		text = (TextView) findViewById(R.id.wbText);

		// 获取当前已保存过的 Token
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		// 获取用户信息接口
		mUsersAPI = new UsersAPI(mAccessToken);

		if (mAccessToken != null && mAccessToken.isSessionValid()) {
			// String uid = mAccessToken.getUid();
			long uid = Long.parseLong(mAccessToken.getUid());
			mUsersAPI.show(uid, mListener);
		}

	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					Toast.makeText(WeiBoLoginActivity.this,
							"获取User信息成功，用户昵称：" + user.screen_name,
							Toast.LENGTH_LONG).show();
					text.setText(user.toString());
				} else {
					Toast.makeText(WeiBoLoginActivity.this, response,
							Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
		}
	};
}
