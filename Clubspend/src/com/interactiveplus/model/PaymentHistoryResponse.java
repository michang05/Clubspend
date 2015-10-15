package com.interactiveplus.model;

public class PaymentHistoryResponse {

	private String type;
	private String transId;
	private String postedDate;
	private String transaction;
	private String credit;
	private String debit;
	private String balance;
	private String status;
	private String totalBalance;

	private String p_id;
	private String p_transaction;
	private String p_postedDate;
	private String p_debit;
	private String p_credit;
	private String p_balance;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getP_id() {
		return p_id;
	}

	public void setP_id(String pId) {
		p_id = pId;
	}

	public String getP_transaction() {
		return p_transaction;
	}

	public void setP_transaction(String pTransaction) {
		p_transaction = pTransaction;
	}

	public String getP_postedDate() {
		return p_postedDate;
	}

	public void setP_postedDate(String pPostedDate) {
		p_postedDate = pPostedDate;
	}

	public String getP_debit() {
		return p_debit;
	}

	public void setP_debit(String pDebit) {
		p_debit = pDebit;
	}

	public String getP_credit() {
		return p_credit;
	}

	public void setP_credit(String pCredit) {
		p_credit = pCredit;
	}

	public String getP_balance() {
		return p_balance;
	}

	public void setP_balance(String pBalance) {
		p_balance = pBalance;
	}

	public PaymentHistoryResponse copy() {
		PaymentHistoryResponse copy = new PaymentHistoryResponse();
		copy.type = type;
		copy.transaction = transaction;
		copy.transId = transId;
		copy.postedDate = postedDate;
		copy.credit = credit;
		copy.debit=debit;
		copy.balance=balance;
		copy.status=status;
		copy.totalBalance=totalBalance;
		
		copy.p_id=p_id;
		copy.p_transaction=p_transaction;
		copy.p_postedDate=p_postedDate;
		copy.p_debit=p_debit;
		copy.p_credit=p_credit;
		copy.p_balance=p_balance;
		
		return copy;
	}

}
