package com.interactiveplus.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.interactiveplus.model.PaymentHistoryResponse;
import com.interactiveplus.parserbase.BaseXmlParser;

public class PaymentHistoryWSParser extends BaseXmlParser {

	private PaymentHistoryResponse paymentHistoryResponse;
	
	public PaymentHistoryWSParser() {
		super();
	}

	@Override
	public List<PaymentHistoryResponse> parse(InputStream inputStream) {

		final PaymentHistoryResponse currentPaymentHistoryResponse = new PaymentHistoryResponse();

		RootElement ninedot = new RootElement(NINEDOT);

		final List<PaymentHistoryResponse> paymentHistoryList = new ArrayList<PaymentHistoryResponse>();

		Element pendingPayments = ninedot.getChild(PENDING_PAYMENTS);
		Element pending = pendingPayments.getChild(PENDING_PAYMENT);
		Element id = pending.getChild(PAYMENT_HISTORY_ID);
		id.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String transId) {
				currentPaymentHistoryResponse.setP_id(transId);
			}
		});
		Element postedDate = pending.getChild(POSTED_DATE);
		postedDate.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String postedDate) {
				currentPaymentHistoryResponse.setP_postedDate(postedDate);
			}
		});
		Element transaction = pending.getChild(TRANSACTION);
		transaction.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String transaction) {
				currentPaymentHistoryResponse.setP_transaction(transaction);
			}
		});
		Element debit = pending.getChild(DEBIT);
		debit.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String debit) {
				currentPaymentHistoryResponse.setP_debit(debit);
			}
		});
		Element credit = pending.getChild(CREDIT);
		credit.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String credit) {
				currentPaymentHistoryResponse.setP_credit(credit);
			}
		});
		pending.setEndElementListener(new EndElementListener() {

			@Override
			public void end() {
				currentPaymentHistoryResponse.setType("PENDING");
				paymentHistoryList.add(currentPaymentHistoryResponse.copy());			}
		});
		

		// POSTED
		Element postedPayments = ninedot.getChild(POSTED_PAYMENTS);
		Element posted = postedPayments.getChild(POSTED_PAYMENT);
		Element status = posted.getChild(STATUS);
		status.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String status) {
				currentPaymentHistoryResponse.setStatus(status);
			}
		});
		Element postedId = posted.getChild(PAYMENT_HISTORY_ID);
		postedId.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String transId) {
				currentPaymentHistoryResponse.setTransId(transId);
			}
		});
		Element post_postedDate = posted.getChild(POSTED_DATE);
		post_postedDate.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String postedDate) {
				currentPaymentHistoryResponse.setPostedDate(postedDate);
			}
		});
		Element post_transaction = posted.getChild(TRANSACTION);
		post_transaction.setEndTextElementListener(new EndTextElementListener() {

					@Override
					public void end(String transaction) {
						currentPaymentHistoryResponse
								.setTransaction(transaction);
					}
				});
		Element post_debit = posted.getChild(DEBIT);
		post_debit.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String debit) {
				currentPaymentHistoryResponse.setDebit(debit);
			}
		});
		Element post_credit = posted.getChild(CREDIT);
		post_credit.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String credit) {
				currentPaymentHistoryResponse.setCredit(credit);
			}
		});
		Element balance = posted.getChild(BALANCE);
		balance.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String balance) {
				currentPaymentHistoryResponse.setBalance(balance);
			}
		});
		
		posted.setEndElementListener(new EndElementListener() {

			@Override
			public void end() {
				currentPaymentHistoryResponse.setType("POSTED");
				paymentHistoryList.add(currentPaymentHistoryResponse.copy());
			}
		});

		Element totalBalance = postedPayments.getChild(RUNNING_BALANCE);
		totalBalance.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String totalBalance) {
				currentPaymentHistoryResponse.setTotalBalance(totalBalance);
				paymentHistoryList.add(currentPaymentHistoryResponse.copy());
			}
		});
	
			try {
				
				Xml.parse(inputStream, Xml.Encoding.UTF_8, ninedot
				.getContentHandler());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	
	
		System.out.println("TOTAL BALANCE IS: "+currentPaymentHistoryResponse.getTotalBalance());
		return paymentHistoryList;
	}

	public PaymentHistoryResponse getPaymentHistoryResponse() {
		return paymentHistoryResponse;
	}

	public void setPaymentHistoryResponse(
			PaymentHistoryResponse paymentHistoryResponse) {
		this.paymentHistoryResponse = paymentHistoryResponse;
	}

	/* (non-Javadoc)
	 * @see com.interactiveplus.parserbase.ConnectionXmlParser#parse(java.io.InputStream)
	 */
	@Override
	public List<?> parse() {
		// TODO Auto-generated method stub
		return null;
	}

}
