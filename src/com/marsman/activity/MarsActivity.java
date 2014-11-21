package com.marsman.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.marsman.R;
import com.marsman.app.MarsManApplication;
import com.marsman.fragment.ImageFragment;
import com.marsman.utils.MyLogger;

/**
 * 拼头像界面
 * */
public class MarsActivity extends FragmentActivity implements OnClickListener {

	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout mLinearLayout;
	private ViewPager pager;
	private int mScreenWidth;
	private int item_width;

	private int endPosition;
	private int beginPosition;
	private int currentFragmentIndex;
	private boolean isEnd;
	private int widthPixels;
	private int widthdp;
	private LinearLayout bitmapImageLayout;

	private String[] mDataString = { "发型", "脸", "眉毛", "眼睛", "嘴", "鼻子", "胡子",
			"眼镜", "衣服", "帽子", "配饰", "背景", "表情", "口头禅" };

	private ArrayList<Fragment> fragments;
	private MyLogger myLogger = MyLogger.getLogger("MarsManApplication");

	private Handler Handler = new Handler();

	private ImageView glass, bigImage;
	public List<ImageView> mImageList = new ArrayList<ImageView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars);
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		this.widthPixels = localDisplayMetrics.widthPixels;
		this.widthdp = px2dip(this, this.widthPixels);
		initView();
		// 初始化导航
		initNav();
		// 初始化viewPager
		initViewPager();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					saveMyBitmap(100);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}, 2000);
		
	}

	private void initView() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		mLinearLayout = (LinearLayout) findViewById(R.id.hsv_content);
		item_width = (int) ((mScreenWidth / 4.0 + 0.5f));
		// mImageView.getLayoutParams().width = item_width;
		bitmapImageLayout = (LinearLayout) findViewById(R.id.imageLayout);
		pager = (ViewPager) findViewById(R.id.pager);
		glass = (ImageView) findViewById(R.id.glass);
		bigImage = (ImageView) findViewById(R.id.bigImage);
		mImageList.add(glass);

		bigImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bigImage.setVisibility(View.GONE);

			}
		});
		bitmapImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bitmapImageLayout.setDrawingCacheEnabled(true);
				Bitmap bmp = bitmapImageLayout.getDrawingCache();
				bigImage.setVisibility(View.VISIBLE);
				bigImage.setImageBitmap(bmp);
			}
		});
	}

	private void initViewPager() {
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < 7; i++) {
			Bundle data = new Bundle();
			data.putString("text", (i + 1) + "");
			ImageFragment fragment = new ImageFragment(
					MarsManApplication.getMarsManApplication().Mars_Mouse);
			// fragment.setArguments(data);
			fragments.add(fragment);
		}
		MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		pager.setAdapter(fragmentPagerAdapter);
		fragmentPagerAdapter.setFragments(fragments);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		pager.setCurrentItem(0);
	}

	private void initNav() {
		for (int i = 0; i < 7; i++) {
			RelativeLayout layout = new RelativeLayout(this);
			TextView view = new TextView(this);
			view.setText(mDataString[i]);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			layout.addView(view, params);
			mLinearLayout.addView(layout, (int) (mScreenWidth / 4 + 0.5f), 50);
			layout.setOnClickListener(this);
			layout.setTag(i);
		}
	}

	public static int px2dip(Context paramContext, float paramFloat) {
		return (int) (0.5F + paramFloat
				/ paramContext.getResources().getDisplayMetrics().density);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragments;
		private FragmentManager fm;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void setFragments(ArrayList<Fragment> fragments) {
			if (this.fragments != null) {
				FragmentTransaction ft = fm.beginTransaction();
				for (Fragment f : this.fragments) {
					ft.remove(f);
				}
				ft.commit();
				ft = null;
				fm.executePendingTransactions();
			}
			this.fragments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Object obj = super.instantiateItem(container, position);
			return obj;
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(final int position) {

			beginPosition = position * item_width;

			currentFragmentIndex = position;
			mHorizontalScrollView.smoothScrollTo((currentFragmentIndex - 1)
					* item_width, 0);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (!isEnd) {
				if (currentFragmentIndex == position) {
					endPosition = item_width * currentFragmentIndex
							+ (int) (item_width * positionOffset);
				}
				if (currentFragmentIndex == position + 1) {
					endPosition = item_width * currentFragmentIndex
							- (int) (item_width * (1 - positionOffset));
				}

				mHorizontalScrollView.invalidate();
				beginPosition = endPosition;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_DRAGGING) {
				isEnd = false;
			} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
				isEnd = true;
				beginPosition = currentFragmentIndex * item_width;
				if (pager.getCurrentItem() == currentFragmentIndex) {
					mHorizontalScrollView.invalidate();
					endPosition = currentFragmentIndex * item_width;
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		pager.setCurrentItem((Integer) v.getTag());
	}

	public void saveMyBitmap(int percent) throws IOException {

		bitmapImageLayout.setDrawingCacheEnabled(true);
		Bitmap bmp = bitmapImageLayout.getDrawingCache();// 这里的drawable2Bitmap方法是我把ImageView中//

		myLogger.d("---Scren w----" + MarsManApplication.witch);
		Bitmap bitmap = Bitmap.createScaledBitmap(bmp,
				MarsManApplication.witch, MarsManApplication.witch, false);

		String file = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getAbsolutePath()
				+ File.separator + "mars";
		File f = new File(file + File.separator + System.currentTimeMillis()
				+ ".jpg");
		f.createNewFile();
		myLogger.d(f.getAbsolutePath());
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, percent, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MediaScannerConnection.scanFile(MarsActivity.this,
				new String[] { new File(file).toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {

					@Override
					public void onScanCompleted(String path, Uri uri) {

					}
				});
	}

	// 附加drawable2Bitmap方法
	static Bitmap drawable2Bitmap(Drawable d) {
		int width = d.getIntrinsicWidth();
		int height = d.getIntrinsicHeight();
		Bitmap.Config config = d.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, width, height);
		d.draw(canvas);
		return bitmap;
	}

	// public Bitmap loadBitmapFromView(View v) {
	// paramView.measure(View.MeasureSpec.makeMeasureSpec(this.widthPixels,
	// 1073741824), View.MeasureSpec.makeMeasureSpec(this.widthPixels,
	// 1073741824));
	// paramView.layout(0, 0, this.widthPixels, this.widthPixels);
	// int i = dip2px(this, 320.0F);
	// if (i > paramView.getMeasuredWidth())
	// i = paramView.getMeasuredWidth();
	// if (i > paramView.getMeasuredHeight())
	// paramView.getMeasuredHeight();
	// paramView.buildDrawingCache();
	// Bitmap localBitmap = paramView.getDrawingCache();
	// if (localBitmap == null)
	// Log.e(this.LOG_TAG, "convertViewToBitmap bitmap is null.");
	// return localBitmap;
	// }
	public static int dip2px(Context paramContext, float paramFloat) {
		return (int) (0.5F + paramFloat
				* paramContext.getResources().getDisplayMetrics().density);
	}

	public Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(this.widthPixels,
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.EXACTLY));
		view.layout(0, 0, this.widthPixels, view.getMeasuredHeight());
		// int i = dip2px(this, 320.0F);
		// if (i > view.getMeasuredWidth())
		// i = view.getMeasuredWidth();
		// if (i > view.getMeasuredHeight())
		// view.getMeasuredHeight();
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}
}
