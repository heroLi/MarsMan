package com.marsman.adapter;

import com.marsman.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridImageAdapter extends BaseAdapter{

	private Context mContext;
	private int[] imageUrl ;
	public GridImageAdapter(Context co,int[] image) {
		this.mContext = co;
		if(image!=null){
			this.imageUrl = image;
		}else
			this.imageUrl= new int[0];
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrl.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrl[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_grid_image, parent, false);
			holder.image = (ImageView)convertView.findViewById(R.id.image);
			convertView.setTag(holder);		
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.image.setImageResource(imageUrl[position]);
		return convertView;
	}

	class ViewHolder{
		ImageView image;
	}
}
