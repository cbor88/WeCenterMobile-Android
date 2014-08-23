package cn.fanfan.detilques;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import cn.fanfan.common.Config;
import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.TextShow;
import cn.fanfan.main.R;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


import com.loopj.android.http.PersistentCookieStore;

import cn.fanfan.common.Config;
import cn.fanfan.common.TextShow;
import cn.fanfan.main.R;
import android.app.ActionBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.Menu;


import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Detilques extends Activity {
    private CookieStore myCookieStore;
	private ListView comlist;
	private List<AnswerItem> comlists;
	private ComAdapter adapter;
	private TextView  questiontitle, questiondetil, focus,
			answercount;
	private LinearLayout addanswer;
	private AsyncHttpClient client;
	private TextShow textShow;
	private String question_content;// ����
	private String question_id;// ����id
	private Button focusques;
	private int focustag = 1;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detilques);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		client = new AsyncHttpClient();
		layout = (LinearLayout)findViewById(R.id.linear);
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		focusques = (Button)findViewById(R.id.focusques);
		focusques.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Focusorno();
			}
		});
		comlist = (ListView) findViewById(R.id.comlist);
		comlists = new ArrayList<AnswerItem>();
		Intent intent = getIntent();
		question_id = intent.getStringExtra("questionid");
		comlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("answerid", comlists.get(arg2).getAnswer_id());
				intent.setClass(Detilques.this, Answer.class);
				startActivity(intent);
			}
		});
		addanswer = (LinearLayout) findViewById(R.id.addanswer);
		questiontitle = (TextView) findViewById(R.id.question_contents);
		questiondetil = (TextView) findViewById(R.id.question_detail);
		focus = (TextView) findViewById(R.id.focus_count);
		answercount = (TextView) findViewById(R.id.answer_count);
		addanswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("questionid", question_id);
				intent.setClass(Detilques.this, WriteAnswer.class);
				startActivityForResult(intent, 1);
                
			}
		});
		GetQuestion(question_id);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			flash();
		}
			
	}

	private void GetQuestion(String questionId) {
		String url = "http://w.hihwei.com/?/api/question/question/?id="
				+ questionId;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(Detilques.this, "��������ʧ�ܣ�", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String information = new String(arg2);
				JSONObject rsm = null;
				String answer_count = null;
				int errno = 0;
				JSONObject jsonObject = null;
				try {
 	                jsonObject = new JSONObject(information);
 	                System.out.println(jsonObject);
					errno = jsonObject.getInt("errno");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (errno == 1) {
					try {
						
						rsm = jsonObject.getJSONObject("rsm");
						JSONObject question_info = rsm
								.getJSONObject("question_info");
					    answer_count = rsm.getString("answer_count");
						
						answercount.setText(answer_count);
						final String question_topics = rsm
								.getString("question_topics");
			
						question_content = question_info
								.getString("question_content");
						focustag = question_info.getInt("has_focus");
						        setFollow();
						layout.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
	                            intent.putExtra("topic", question_topics);
	                            intent.setClass(Detilques.this, TopicAbout.class);
	                            startActivity(intent);
							}
						});

						String question_detail = question_info
								.getString("question_detail");
						String focus_count = question_info.getString("focus_count");
						questiontitle.setText(question_content);
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						float screenW = dm.widthPixels;
						textShow = new TextShow(JSONTokener(question_detail), questiondetil,Detilques.this,screenW);
						textShow.execute();
						focus.setText(focus_count);
						if (!answer_count.equals("0")) {
							JSONArray answers = rsm.getJSONArray("answers");
							for (int i = 0; i < answers.length(); i++) {
								JSONObject answer = answers.getJSONObject(i);
								AnswerItem answerItem = new AnswerItem();
								answerItem.setAnswer_id(answer.getString("answer_id"));
								answerItem.setAnswer_content(JSONTokener(answer
										.getString("answer_content")));
								answerItem.setAgree_count(answer
										.getString("agree_count"));
								answerItem.setUid(answer.getString("uid"));
								answerItem.setName(answer.getString("user_name"));
			
								comlists.add(answerItem);
							}

						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						try {
							JSONObject answers = rsm.getJSONObject("answers");
							for (int i = 0; i < Integer.valueOf(answer_count); i++) {
								JSONObject answer = answers.getJSONObject(String.valueOf(i+1));
								AnswerItem answerItem = new AnswerItem();
								answerItem.setAnswer_id(answer.getString("answer_id"));
								answerItem.setAnswer_content(JSONTokener(answer
										.getString("answer_content")));
								answerItem.setAgree_count(answer
										.getString("agree_count"));
								answerItem.setUid(answer.getString("uid"));
								answerItem.setName(answer.getString("user_name"));
								//answerItem.setAvatar_file(answer.getString("avatar_file"));
								comlists.add(answerItem);
							}
						} catch (Exception e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}
						
					}

					adapter = new ComAdapter(comlists, Detilques.this);
					comlist.setAdapter(adapter);
				} else {
                   try {
					String err = jsonObject.getString("err");
					Toast.makeText(Detilques.this, err, Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                   
				}

			}

		});
	}

	private void Focusorno(){
		String url = "http://w.hihwei.com/?/question/ajax/focus/?question_id="+question_id;
		client.get(url, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(Detilques.this, "��������ʧ�ܣ�", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String info = new String(arg2);
				
				try {
					JSONObject jsonObject = new JSONObject(info);
					int errno = jsonObject.getInt("errno");
					if (errno == 1) {
						JSONObject rsm = jsonObject.getJSONObject("rsm");
						String type = rsm.getString("type");
						if (type.equals("add")) {
							focustag = 1;
						} else {
	                        focustag = 0;
						}
						setFollow();
					} else {
                       String err = jsonObject.getString("err");
                   	Toast.makeText(Detilques.this, err, Toast.LENGTH_LONG).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
	}

	public static String JSONTokener(String in){
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}


     
	private void setFollow(){
		if (focustag == 1) {
			System.out.println(123);
			focusques.setBackgroundResource(R.drawable.btn_silver_normal);
			focusques.setText("ȡ����ע");
			focusques.setTextColor(Detilques.this.getResources().getColor(R.color.text_color_gray));
		}else {
			focusques.setBackgroundResource(R.drawable.btn_green_normal);
			focusques.setText("��ע");
			focusques.setTextColor(Detilques.this.getResources().getColor(R.color.text_color_white));
		}
	}
	 public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case  R.id.share:
				showShare();
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
    private void flash(){
    	comlists.clear();
		GetQuestion(question_id);
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.share, menu);
		return super.onCreateOptionsMenu(menu);
	}



	// ����
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ�������
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		oks.setDialogMode();// ���óɴ���ģʽ
		oks.setImageUrl(Config.getValue("IconUrl"));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(question_content);
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl(Config.getValue("ShareQuestionUrl") + question_id);
		/** url��΢�ţ��������ѡ�����Ȧ�ղأ������ţ��������Ѻ�����Ȧ����ʹ�ã�������Բ��ṩ */
		oks.setUrl(Config.getValue("ShareQuestionUrl") + question_id);
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText(question_content + Config.getValue("ShareQuestionUrl")
				+ question_id + "�������Է�����׿�ˣ�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���

		// oks.setImagePath("/sdcard/test.jpg");// ����ͼƬ��ʱ�����
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl(Config.getValue("ShareQuestionUrl") + question_id);
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("  ");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl(Config.getValue("ShareQuestionUrl") + question_id);
		// ��������GUI
		oks.show(this);
	}
}
