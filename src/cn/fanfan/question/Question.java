package cn.fanfan.question;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONException;
import org.json.JSONObject;






import cn.fanfan.common.MyProgressDialog;
import cn.fanfan.main.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Question extends FragmentActivity {
	private AsyncHttpClient client;
    private CookieStore myCookieStore; 
    private cn.fanfan.common.MyProgressDialog progressDialog;
    private int attach_id ;
	private Bitmap pic;
	private float offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private int itemcount = 3;
	private static Uri photoUri ;
	private ViewPager viewPager;
	private List<Fragment> listViews; // Tabҳ���б�
	private ImageView cursor;// ����ͼƬ
	private TextView t1, t2, t3;// ҳ��ͷ��
	Fragment text1;
	Fragment text2;
	Fragment text3;
	private Map<String, String> pathlink;
	private ActionBar actionBar;
	private static String attach_access_key ;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.quespagr);
		actionBar = getActionBar();
		actionBar.show();
		pathlink = new HashMap<String, String>();
		text1 = new Quesfrang();
		text2 = new Detilfrang();
		text3 = new Tagfrang();
		cursor = (ImageView) findViewById(R.id.cursor);
		attach_access_key = md5(getAttachKey());
        client = new AsyncHttpClient();
        myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		progressDialog = new MyProgressDialog(this, "正在上传", "稍等...", false);
		InitTextView();
		InitImageView();
		InitViewPager();
		
	}
     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	 MenuInflater inflater = getMenuInflater();
    	 inflater.inflate(R.menu.quespub, menu);
    	return super.onCreateOptionsMenu(menu);
    }
   
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
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
	 private void InitImageView() {
			Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.a);// ��ȡͼƬ���
			bmpW = pic.getWidth();
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			float screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ�
			offset = (screenW / itemcount - bmpW) / 2;// ����ƫ����
			Matrix matrix = new Matrix();;
			matrix.postTranslate(offset, 0);
			cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
		}
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		listViews = new ArrayList<Fragment>();
		listViews.add(text1);
		listViews.add(text2);
		listViews.add(text3);
		viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), listViews));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyPageAdapter extends FragmentPagerAdapter {
		public List<Fragment> mListViews;

		public MyPageAdapter(FragmentManager fm, List<Fragment> listViews) {
			// TODO Auto-generated constructor stub
			super(fm);
			this.mListViews = listViews;
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
			default:
				return null;
			}
			args.putString("mod", mod);
			args.putString("id", "0");
			fragment.setArguments(args);
			return fragment;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.postp:
			destoryBimap();
			//showDialog();
			break;
		case R.id.publish:
			String quesdetil =  ((Detilfrang) text2).getTextString().getText().toString();
            String question = ((Quesfrang) text1).getTextString().getText().toString();
            String questag = ((Tagfrang) text3).getTextString().getText().toString();
            RequestParams params = new RequestParams();
            params.put("question_content", question);
            params.put("question_detail", quesdetil);
			params.put("attach_access_key", attach_access_key);
			attach_access_key = md5(getAttachKey());
            params.put("topics", questag);
            quespost(params);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@SuppressLint("SimpleDateFormat") 
	private String getAttachKey() {  
        Date date = new Date(System.currentTimeMillis());  
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'yyyyMMddHHmmss");  
        return dateFormat.format(date) + Math.random()*100;  
    }  

	   @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    	// TODO Auto-generated method stub
		    	  super.onActivityResult(requestCode, resultCode, data);
		    	  System.out.println(1111);
		    	  System.out.println(requestCode+"   qqqqq   "+resultCode);
		    	  System.out.println(photoUri);
		    	  switch (requestCode) {
				case 0:
					if (resultCode!= RESULT_CANCELED && null!= data) {
						System.out.println(3333);
						Uri uri = data.getData();
						System.out.println(2222);
						System.out.println(uri.getPath() + ")))");

						String[] pojo = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver().query(uri, pojo, null, null,
								null);
						String picPath = null;
						if (cursor != null) {
							int columnIndex = cursor.getColumnIndex(pojo[0]);
							cursor.moveToFirst();
							picPath = cursor.getString(columnIndex);
							cursor.close();
						}
						File photopic = new File(picPath);
						photoUri = Uri.fromFile(photopic);
						picpost(photoUri);					
						
					} else {
						Toast.makeText(this,
			        			"选择图片失败", Toast.LENGTH_LONG).show();
					}
					break;
				case 1:
					if (resultCode!=RESULT_CANCELED ) {
						photoUri = ((Detilfrang) text2).getUri();
						//startPhotoZoom(photoUri);
						if (photoUri !=  null) {
							picpost(photoUri);	
						} else {
							Toast.makeText(this,
				        			"拍照失败", Toast.LENGTH_LONG).show();
						}
						
					} else {
						Toast.makeText(this,
			        			"����ʧ�ܣ�", Toast.LENGTH_LONG).show();
					}
					break;
				default:
					break;
				}
		    	  
		    }
	   private void picpost(Uri photoUri) {
		   File photo = null;
		try {
			photo = new File(Bimp.revitionImageSize(photoUri.getPath()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			RequestParams params = new RequestParams();
			
			try {
				params.put("qqfile", photo);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            postpic(params, "question", attach_access_key);			

	}
	   public void postpic(RequestParams params,String id,String attach) {
		      
	    	String url = "http://w.hihwei.com/?/api/publish/attach_upload/?id="+id+"&attach_access_key="+attach;
			client.post(url, params, new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					JSONObject jsonObject = new JSONObject() ;
					JSONObject rsm = new JSONObject();
					int errno = 0;
					String err = null;
					String result = new String(arg2);
					try {
					     jsonObject = new JSONObject(result);
						 errno = jsonObject.getInt("errno");
						 err = jsonObject.getString("err");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	  			    if (errno == 1) {
	                	Toast.makeText(Question.this, "上传成功", Toast.LENGTH_LONG).show();
					     try {
							rsm = jsonObject.getJSONObject("rsm");
							 attach_id = rsm.getInt("attach_id");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					     System.out.println(attach_id+" pppppp");
					     ((Detilfrang) text2).getTextString().append("[attach]"+attach_id+"[/attach]");
	        			((Detilfrang) text2).showpic(photoUri.getPath());
					}
	                if (errno == -1) {
	                	Toast.makeText(Question.this, err, Toast.LENGTH_LONG).show();         
	                	System.out.println(err);
					}
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
			});
			
			}
	   public void quespost(RequestParams params) {
	    	String url = "http://w.hihwei.com/?/api/publish/publish_question/";
			client.post(url, params, new AsyncHttpResponseHandler(){
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					progressDialog.show();
					super.onStart();
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					JSONObject jsonObject;
					String errno = null;
					String err = null;
					String result = new String(arg2);
					try {
					     jsonObject = new JSONObject(result);
						 errno = jsonObject.getString("errno");
						 err = jsonObject.getString("err");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progressDialog.hideAndCancle();
	                if (errno.equals("1")) {
	                	Toast.makeText(Question.this, "发布成功", Toast.LENGTH_LONG).show();
					}
	                if (errno.equals("-1")) {
	                	Toast.makeText(Question.this, err, Toast.LENGTH_LONG).show();
	                	System.out.println(err);
					}
	                
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	public static String md5(String string) {

		byte[] hash;

		try {

			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			throw new RuntimeException("Huh, MD5 should be supported?", e);

		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("Huh, UTF-8 should be supported?", e);

		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {

			if ((b & 0xFF) < 0x10)
				hex.append("0");

			hex.append(Integer.toHexString(b & 0xFF));

		}

		return hex.toString();

	}
		public class MyOnPageChangeListener implements OnPageChangeListener {

			float one = offset * 2 + bmpW;

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
		 private void destoryBimap() { 
		        if (pic != null && !pic.isRecycled()) { 
		            //pic.recycle(); 
		            pic = null; 
		        } 
		    } 
		
		 protected void onDestroy() {

				FileUtils.deleteDir(FileUtils.SDPATH);
				FileUtils.deleteDir(FileUtils.SDPATH1);
				// ����ͼƬ����
				super.onDestroy();
			}

}
@SuppressWarnings("serial")
class SerializableMap implements Serializable {
	private Map<String,String> map;
	public Map<String,String> getMap()
	{
		return map;
	}
	public void setMap(Map<String,String> map)
	{
		this.map=map;
	}
}
