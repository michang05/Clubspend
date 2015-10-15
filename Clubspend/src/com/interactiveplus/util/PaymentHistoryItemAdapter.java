/**
 * 
 */
package com.interactiveplus.util;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.interactiveplus.R;
import com.interactiveplus.model.PaymentHistoryResponse;

/**
 * @author Michael Angelo
 *
 */
public class PaymentHistoryItemAdapter extends
		ArrayAdapter<PaymentHistoryResponse> {

private int resource;
	
	public PaymentHistoryItemAdapter(Context context, int resource,
			 List<PaymentHistoryResponse> items) {
		super(context, resource,items);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout transactionView;
		PaymentHistoryResponse item = getItem(position);
		
		String transaction = item.getTransaction();
		String postedDate = item.getPostedDate();
		
		String dateString = postedDate;
		
		if(convertView == null) {
			transactionView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi =(LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, transactionView,true);
		}else {
			transactionView = (LinearLayout)convertView;
		}
		
		TextView trans = (TextView)transactionView.findViewById(R.id.transaction);
		TextView date = (TextView)transactionView.findViewById(R.id.postingDate);
		
		date.setText(dateString);
		trans.setText(transaction);

		return transactionView;
	}

}
