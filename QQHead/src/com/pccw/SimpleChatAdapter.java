package com.pccw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SimpleChatAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private final ArrayList<MessageInfo> msgs;
	private Context context;

	public SimpleChatAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(this.context);
		msgs=new ArrayList<MessageInfo>();
	}
	
	public void addMessage(MessageInfo msg){
		msgs.add(msg);
		notifyDataSetChanged();
	}
	
	public void addMessages(List<MessageInfo> msgList){
		msgs.addAll(msgList);
		notifyDataSetChanged();
	}
	
	public void clear(){
		msgs.clear();
		notifyDataSetChanged();
	}
	
	public void addUser(UserInfo user){
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return msgs.size();
	}

	@Override
	public MessageInfo getItem(int position) {
		return msgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		MessageInfo msgInfo = getItem(position);
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.team_layout_main_singlechat_item, null);
			holder=new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.setData(msgInfo);
		return convertView;
	}
	
	private class ViewHolder{
		RelativeLayout chat_layout;
		ImageView image;
		TextView text;
		public ViewHolder(View convertView){
			chat_layout=(RelativeLayout) convertView.findViewById(R.id.team_singlechat_id_listiteam);
			image=(ImageView) convertView.findViewById(R.id.team_singlechat_id_listiteam_headicon);
			text=(TextView) convertView.findViewById(R.id.team_singlechat_id_listiteam_message);
		}
		public void setData(MessageInfo msg){
			RelativeLayout.LayoutParams rl_chat_left=((RelativeLayout.LayoutParams)chat_layout.getLayoutParams());
			RelativeLayout.LayoutParams rl_tv_msg_left=((RelativeLayout.LayoutParams)text.getLayoutParams());
			RelativeLayout.LayoutParams rl_iv_headicon_left=((RelativeLayout.LayoutParams)image.getLayoutParams());
			if(!DicqConstant.DEFAULTMAC.equalsIgnoreCase(msg.getUsermac())){	//根据本地的mac地址来判断该条信息是属于本人所说还是对方所说
																				//如果是自己说的，则显示在右边；如果是对方所说，则显示在左边
				rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,-1);
				rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
				rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,-1);
				rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
				rl_tv_msg_left.addRule(RelativeLayout.RIGHT_OF,R.id.team_singlechat_id_listiteam_headicon);
				rl_tv_msg_left.addRule(RelativeLayout.LEFT_OF,0);
				text.setBackgroundResource(R.drawable.balloon_l_selector);
			}else{
				rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
				rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1);
				rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
				rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1);
				rl_tv_msg_left.addRule(RelativeLayout.RIGHT_OF,0);
				rl_tv_msg_left.addRule(RelativeLayout.LEFT_OF,R.id.team_singlechat_id_listiteam_headicon);
				text.setBackgroundResource(R.drawable.balloon_r_selector);
			}
			image.setImageResource(PrortaitUtils.conversionIdToRes(msg.getProtrait()));		//设置头像
			String str = msg.getMsg();														//消息具体内容
			String zhengze = "f0[0-9]{2}|f10[0-7]";											//正则表达式，用来判断消息内是否有表情
			try {
				SpannableString spannableString = ExpressionUtil.getExpressionString(context, str, zhengze);
				text.setText(spannableString);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}
}
