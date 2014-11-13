package com.marsman.fragment;

import com.marsman.R;
import com.marsman.adapter.GridImageAdapter;
import com.marsman.app.MarsManApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ImageFragment extends Fragment {

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
		
		initData();
	}
	
	private void initData() {
		imageFragment = new GridImageAdapter(getActivity(), iamgeData);
		imageGrid.setAdapter(imageFragment);

	}
}
