package com.marsman.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.marsman.R;
import com.marsman.activity.MarsActivity;
import com.marsman.adapter.GridImageAdapter;

public class ImageFragment extends Fragment implements OnItemClickListener {

	private GridView imageGrid;
	
	
	private int[] iamgeData;
	private GridImageAdapter imageFragment;
	public ImageFragment(int[] imageurl) {
		if(imageurl==null){
			this.iamgeData = new int[0];
		}else
			this.iamgeData =imageurl ;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_grad, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		imageGrid = (GridView) view.findViewById(R.id.gridImage);
		imageGrid.setOnItemClickListener(this);
		initData();
	}
	
	private void initData() {
		imageFragment = new GridImageAdapter(getActivity(), iamgeData);
		imageGrid.setAdapter(imageFragment);

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position==5){
			((MarsActivity)getActivity()).mImageList.get(0).setBackgroundResource(R.drawable.glass);
		}
	}
}
