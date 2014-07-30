package cn.fanfan.found;

import java.util.ArrayList;
import java.util.List;

import cn.fanfan.main.R;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class FoundFrg extends Fragment {
	 private int offset = 0;// ����ͼƬƫ����
	 private int currIndex = 0;// ��ǰҳ�����
	 private int bmpW;// ����ͼƬ���
	 private int itemcount = 4;
	 private ViewPager viewPager;
	 private List<Fragment> listViews; // Tabҳ���б�
	 private ImageView cursor;// ����ͼƬ
	 private TextView t1, t2, t3,t4;// ҳ��ͷ��
	 public FoundFrg() {
		// TODO Auto-generated constructor stub
	}
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
	
         View rootView = inflater.inflate(R.layout.foundfrg, container, false);
         cursor = (ImageView) rootView.findViewById(R.id.cursor);
         t1 = (TextView) rootView.findViewById(R.id.text1);
	     t2 = (TextView) rootView.findViewById(R.id.text2);
	     t3 = (TextView) rootView.findViewById(R.id.text3);
	     t4 = (TextView) rootView.findViewById(R.id.text4);
	     InitTextView();
	     InitImageView();
	     viewPager = (ViewPager) rootView.findViewById(R.id.foundpager);
	     InitViewPager();
         return rootView;
     }
	 private void InitTextView() {
	     t1.setOnClickListener(new MyOnClickListener(0));
	     t2.setOnClickListener(new MyOnClickListener(1));
	     t3.setOnClickListener(new MyOnClickListener(2));
	     t4.setOnClickListener(new MyOnClickListener(3));
	    }
	 private void InitViewPager() {
	        listViews = new ArrayList<Fragment>();
	        Fragment text1 = new FoundPager();
	        Fragment text2 = new FoundPager();
	        Fragment text3 = new FoundPager();
	        Fragment text4 = new FoundPager();
	        listViews.add(text1);
	        listViews.add(text2);
	        listViews.add(text3);
	        listViews.add(text4);
	        viewPager.setAdapter(new FoundPageAdapter(getActivity().getSupportFragmentManager(),listViews));
	        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	    }
	 private void InitImageView() {
			bmpW = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.a).getWidth();// ��ȡͼƬ���
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
			offset = (screenW / itemcount - bmpW) / 2;// ����ƫ����
			Matrix matrix = new Matrix();
			matrix.postTranslate(offset, 0);
			cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
		}

	 public class MyOnClickListener implements View.OnClickListener {
	        private int index = 0;

	        public MyOnClickListener(int i) {
	            index = i;
	        }

	        @Override
	        public void onClick(View v) {
	            viewPager.setCurrentItem(index);
	        }
	    };
	    public class FoundPageAdapter extends FragmentStatePagerAdapter{
			public List<Fragment> mListViews;
	        
	        public FoundPageAdapter(FragmentManager fm,List<Fragment> mListViews) {
				// TODO Auto-generated constructor stub
	        	super(fm);
	        	this.mListViews = mListViews;
			}
	     
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mListViews.size();
			}
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				Fragment fragment = mListViews.get(arg0);
				Bundle args = new Bundle();
				String mod;
				switch (arg0) {
				case 0:
					mod = "0";
					break;
				case 1:
					mod = "1";
					break;
				case 2:
					mod = "2";
					break;
				case 3:
					mod = "2";
					break;
				default:
					return null;
				}
				args.putString("mod", mod);
				args.putString("id", "0");
				fragment.setArguments(args);
				return fragment;
			}
		}
	    public class MyOnPageChangeListener implements OnPageChangeListener {

			int one = offset * 2 + bmpW;

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Animation animation = null;
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one * arg0, 0, 0);
				} else {
					animation = new TranslateAnimation(one * currIndex, one * arg0,
							0, 0);
				}
				currIndex = arg0;
				animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
				animation.setDuration(300);
				cursor.startAnimation(animation);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

		}
}
