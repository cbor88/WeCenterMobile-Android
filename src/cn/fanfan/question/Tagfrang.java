package cn.fanfan.question;

import cn.fanfan.main.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Tagfrang extends Fragment {
	 private EditText editText;
  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	   View rootView ;
		rootView = inflater.inflate(R.layout.questag, container, false);
		editText = (EditText)rootView.findViewById(R.id.questag);
	return rootView;
  }
	public EditText getTextString() {
		return editText;
		
	}
}
