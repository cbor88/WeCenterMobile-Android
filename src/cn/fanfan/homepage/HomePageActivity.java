package cn.fanfan.homepage;

import java.util.ArrayList;
import java.util.List;

import cn.fanfan.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class HomePageActivity extends Activity {
	private ListView listView;
	private List<HomePageItemModel> itemDataList = new ArrayList<HomePageItemModel>();
	private HomePageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_homepage);
		listView = (ListView) findViewById(R.id.lvHomeListView);
		initItemData();
		adapter = new HomePageAdapter(HomePageActivity.this,
				R.layout.listitem_homepage, itemDataList);
		listView = (ListView) findViewById(R.id.lvHomeListView);
		listView.setAdapter(adapter);
	}

	private void initItemData() {
		// TODO Auto-generated method stub
		HomePageItemModel item1 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"��Ī",
				"��ͬ�ûش�",
				"��������Щ���ô������ʳ��",
				"����±��Ҳ��Ϊ³�棬�ø�ʢ����������˿����˿����˿���㹽�����㡢Ϻ�ɡ��ƻ��˵��������ȹ��ﳴ��󣬼���������󿪣�Ȼ�����������ζ�������ǡ����κͷ���۵ȣ�����±�ϡ�����ʱ���������Ϸ�Щ�²ˡ���ѿ������±�ϣ������Ϻ����ۡ���ը�ⶡ����ը����˿����˵����ϡ����ص㣺ɫ�����ޡ�����ˬ���������㴼�������ɿڡ�",
				"78");
		itemDataList.add(item1);
		HomePageItemModel item2 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_SIMPLE, "����", "��ע������",
				"�������м���ô����", "û������", "1");
		itemDataList.add(item2);
		HomePageItemModel item3 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"hcjcch",
				"��ͬ�ûش�",
				"��θ���ʳ����?",
				"������һЩ�̼��Ĺ���ʳ��浥����һЩСС���ĵã����ҷ���һ�¡����ȣ�˵һ���Լ���@�ż���Ƭ�ĸо�������˵�żѵĴ𰸲��ã�ֻ��˵һ���Լ���������Բۣ���һ���ڷ���@���ˣ��ý��ţ���һ�ΰ�",
				"100");
		itemDataList.add(item3);
		HomePageItemModel item4 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_SIMPLE, "��ΰ", "��ע�ûش�",
				"Ů�����ڲ��ܳ�ʲô��", " ", "0");
		itemDataList.add(item4);
		HomePageItemModel item5 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"��Hwei",
				"��ͬ�ûش�",
				"��������Щ���ô������ʳ��",
				"����±��Ҳ��Ϊ³�棬�ø�ʢ����������˿����˿����˿���㹽�����㡢Ϻ�ɡ��ƻ��˵��������ȹ��ﳴ��󣬼���������󿪣�Ȼ�����������ζ�������ǡ����κͷ���۵ȣ�����±�ϡ�����ʱ���������Ϸ�Щ�²ˡ���ѿ������±�ϣ������Ϻ����ۡ���ը�ⶡ����ը����˿����˵����ϡ����ص㣺ɫ�����ޡ�����ˬ���������㴼�������ɿڡ�",
				"1k");
		itemDataList.add(item5);
		HomePageItemModel item6 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"��Ī",
				"��ͬ�ûش�",
				"̨������Щ��ʳֵ��һ�ԣ�",
				"ֻ˵�Լ�ӡ����̵ģ�1. ������Ż������ǰ�ֻ���������Һųơ����Բ��п�������һö55NT����ֵ��һ���Ů����3-4���˷ֳԲ������⡣��Ȼ�Ե�������Щ��ΪʲôҪ����ô�༦�Ⱑ�����ĸо�����ֵ�ó��ԡ�",
				"256");
		itemDataList.add(item6);
	}
}
