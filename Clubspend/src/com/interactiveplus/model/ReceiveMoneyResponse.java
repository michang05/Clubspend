/**
 * 
 */
package com.interactiveplus.model;

/**
 * @author Michael Angelo
 *
 */
public class ReceiveMoneyResponse {

	private String success;
	private String amountReceived;
	private String dateReceived;
	private String balance;	
	private String paymentId;
	private String commissionFeeId;
	private String error;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(String amountReceived) {
		this.amountReceived = amountReceived;
	}
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getCommissionFeeId() {
		return commissionFeeId;
	}
	public void setCommissionFeeId(String commissionFeeId) {
		this.commissionFeeId = commissionFeeId;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}
