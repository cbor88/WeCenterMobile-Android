package cn.fanfan.homepage;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.Config;
import cn.fanfan.common.NetworkState;
import cn.fanfan.main.MainActivity;
import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageFragment extends Fragment {
	public static final String TAG = "HomePageFragment";
	private ListView listView;
	private TextView tvHomePageLoading;
	private List<HomePageItemModel> itemDataList = new ArrayList<HomePageItemModel>();
	private HomePageAdapter adapter;
	private Bundle bundle;
	private String uid;
	private int page = 0;
	private int totalRow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		bundle = getArguments();
		uid = bundle.getString("uid");
		View fragmentView;
		fragmentView = inflater.inflate(R.layout.fragment_homepage, container,
				false);
		tvHomePageLoading = (TextView) fragmentView
				.findViewById(R.id.tvHomePageLoading);
		listView = (ListView) fragmentView.findViewById(R.id.lvHomeListView);
		// tvHomePageLoading.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		// ��ȡ����
		getHomePageInfo(page);
		final MainActivity activity = (MainActivity) getActivity();
		adapter = new HomePageAdapter(activity, R.layout.listitem_homepage,
				itemDataList);
		listView = (ListView) fragmentView.findViewById(R.id.lvHomeListView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HomePageItemModel itemModel = itemDataList.get(position);
				Toast.makeText(activity, itemModel.getUserName(),
						Toast.LENGTH_SHORT).show();
				// itemDataList.clear();
				// adapter.notifyDataSetChanged();
				// listView.setSelection(0);
			}
		});
		return fragmentView;
	}

	private void getHomePageInfo(int page) {
		NetworkState networkState = new NetworkState();
		MainActivity activity = (MainActivity) getActivity();
		if (networkState.isNetworkConnected(activity)) {
			Log.i(TAG, "NetworkIsConnected");
			RequestParams params = new RequestParams();
			params.put("page", page);
			String url = Config.getValue("HomePageUrl");
			AsyncHttpClient client = new AsyncHttpClient();
			PersistentCookieStore mCookieStore = new PersistentCookieStore(
					activity);
			client.setCookieStore(mCookieStore);
			client.get(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					Toast.makeText((MainActivity) getActivity(),
							" �����е㲻��Ŷ�������ԣ���", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1,
						byte[] responseContent) {
					// TODO Auto-generated method stub
					int layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
					String string = new String(responseContent);
					Log.i(TAG, string);
					try {
						JSONObject all = new JSONObject(string);
						JSONObject rsm = all.getJSONObject("rsm");
						totalRow = (rsm.getInt("total_rows"));
						Log.i(TAG, Integer.toString(totalRow));
						// �����ҳ������������Ļ��չʾ��item�ĸ���������footview
						if (totalRow > 0) {
							tvHomePageLoading.setVisibility(View.GONE);
							listView.setVisibility(View.VISIBLE);
						}
						JSONArray rows = rsm.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject rowsObject = rows.getJSONObject(i);
							// int history_id = rowsObject.getInt("history_id");
							int userUid = rowsObject.getInt("uid");
							int allAction = rowsObject
									.getInt("associate_action");
							Log.i(TAG, Integer.toString(allAction));
							// int addTime = rowsObject.getInt("add_time");
							// ��ȡuserInfo����
							JSONObject userInfoObject = rowsObject
									.getJSONObject("user_info");
							String userName = userInfoObject
									.getString("user_name");
							Log.i(TAG, userName);
							String avatarUrl = Config
									.getValue("AvatarPrefixUrl")
									+ userInfoObject.getString("avatar_file");
							Log.i(TAG, userInfoObject.getString("avatar_file"));
							// ת��
							if (allAction == 101 || allAction == 105) {
								String action, itemTitle, bestAnswer = " ";
								int itemTitleUid, bestAnswerUid = 1, agreeCount = 1;
								JSONObject questionInfoObject = rowsObject
										.getJSONObject("question_info");
								itemTitle = questionInfoObject
										.getString("question_content").trim();
								Log.i(TAG, itemTitle);
								itemTitleUid = questionInfoObject
										.getInt("question_id");

								if (allAction == 101) {
									action = "����������";
									layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
								} else {
									action = "��ע������";
									layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
								}
								layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
								// ���ص�ListItemModel
								HomePageItemModel item = new HomePageItemModel(
										layoutType, avatarUrl, userName,
										userUid, action, itemTitle,
										itemTitleUid, bestAnswer,
										bestAnswerUid, agreeCount);
								itemDataList.add(item);
							}

							if (allAction == 201 || allAction == 204) {
								String action, itemTitle, bestAnswer = " ";
								int itemTitleUid, bestAnswerUid = 1, agreeCount = 1;
								JSONObject answerInfoObject = rowsObject
										.getJSONObject("answer_info");
								bestAnswerUid = answerInfoObject
										.getInt("answer_id");
								bestAnswer = answerInfoObject
										.getString("answer_content").trim();
								Log.i(TAG, bestAnswer);
								agreeCount = answerInfoObject
										.getInt("agree_count");
								// ��ȡquestion_info����
								JSONObject questionInfoObject = rowsObject
										.getJSONObject("question_info");
								itemTitle = questionInfoObject
										.getString("question_content").trim();
								Log.i(TAG, itemTitle);
								itemTitleUid = questionInfoObject
										.getInt("question_id");

								layoutType = HomePageItemModel.LAYOUT_TYPE_COMPLEX;

								if (allAction == 201) {
									action = "�ش������";
									layoutType = HomePageItemModel.LAYOUT_TYPE_COMPLEX;
								} else {
									action = "��ͬ�ûش�";
									layoutType = HomePageItemModel.LAYOUT_TYPE_COMPLEX;
								}
								// ���ص�ListItemModel
								HomePageItemModel item = new HomePageItemModel(
										layoutType, avatarUrl, userName,
										userUid, action, itemTitle,
										itemTitleUid, bestAnswer,
										bestAnswerUid, agreeCount);
								itemDataList.add(item);
							}

							if (allAction == 501 || allAction == 502) {
								String action, itemTitle, bestAnswer = " ";
								int itemTitleUid, bestAnswerUid = 1, agreeCount = 1;
								JSONObject articleInfoObject = rowsObject
										.getJSONObject("article_info");
								itemTitleUid = articleInfoObject.getInt("id");
								itemTitle = articleInfoObject
										.getString("title").trim();

								if (allAction == 501) {
									action = "����������";
									layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
								} else {
									action = "��ͬ������";
									layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
								}
								layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
								// ���ص�ListItemModel
								HomePageItemModel item = new HomePageItemModel(
										layoutType, avatarUrl, userName,
										userUid, action, itemTitle,
										itemTitleUid, bestAnswer,
										bestAnswerUid, agreeCount);
								itemDataList.add(item);
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.i(TAG, "Json�����쳣");
					}

				}
			});
		} else {
			Toast.makeText((MainActivity) getActivity(), "δ�������磡",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (bundle != null) {
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					"position"));
		}
	}
}
