package com.interactiveplus.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClubspendDBAdapter {

	public static final String KEY_DEBIT = "debit";
	public static final String KEY_CREDIT = "credit";
	public static final String KEY_BALANCE = "balance";
	public static final String KEY_STATUS = "status";
	public static final String KEY_POSTED_DATE = "posted_date";
	public static final String KEY_TRANSACTION = "transactionDetails";
	public static final String KEY_TRANSACTION_ID = "transaction_id";
	public static final String KEY_TRANSACTION_TYPE = "transaction_type";
	public static final String KEY_YEAR = "year";
	public static final String KEY_MONTH = "month";
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "Clubspend";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "Clubspend";
	private static final String DATABASE_TABLE = "transaction_details";
	private static final int DATABASE_VERSION = 2;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + "(" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_DEBIT + " text, "
			+ KEY_CREDIT + " text, " + KEY_BALANCE + " text," + KEY_STATUS
			+ " text, " + KEY_POSTED_DATE + " posted_date text , "
			+ KEY_TRANSACTION + " text," + KEY_TRANSACTION_ID + " text,"
			+ KEY_TRANSACTION_TYPE + " text ," + KEY_YEAR + " text,"
			+ KEY_MONTH + " text);";

	private final Context mCtx;
	private int numRows;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * Database creation sql statement
		 */
		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + "(" + KEY_ROWID
				+ " integer primary key autoincrement, " + KEY_DEBIT
				+ " text, " + KEY_CREDIT + " text, " + KEY_BALANCE + " text,"
				+ KEY_STATUS + " text, " + KEY_POSTED_DATE
				+ " posted_date text , " + KEY_TRANSACTION + " text,"
				+ KEY_TRANSACTION_ID + " text," + KEY_TRANSACTION_TYPE
				+ " text ," + KEY_YEAR + " text," + KEY_MONTH + " text);";

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public ClubspendDBAdapter(Context mCtx) {
		this.mCtx = mCtx;
		mDbHelper = new DatabaseHelper(this.mCtx);
	}

	public ClubspendDBAdapter open() throws SQLException {

		try {
			mDb = mDbHelper.getWritableDatabase();
		} catch (SQLException e) {
			mDb = mDbHelper.getReadableDatabase();
		}
		return this;
	}

	public int rowSize() {
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DEBIT, KEY_CREDIT, KEY_BALANCE, KEY_STATUS,
				KEY_POSTED_DATE, KEY_TRANSACTION, KEY_TRANSACTION_ID,
				KEY_TRANSACTION_TYPE, KEY_YEAR, KEY_MONTH }, null, null, null,
				null, null);

		numRows = c.getCount();
		return numRows;
	}

	public boolean isDbOpen() {
		return mDb.isOpen();
	}

	public void close() {
		mDb.close();
	}

	public long createDetails(String transId, String trans, String debit,
			String credit, String balance, String status, String postedDate,
			String transType, String year, String month) {

		if (balance == null) {
			balance = "0";
		}
		if (debit == null) {
			debit = "0";
		}
		if (credit == null) {
			credit = "0";
		}

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TRANSACTION_ID, transId);
		initialValues.put(KEY_TRANSACTION, trans);
		initialValues.put(KEY_DEBIT, debit);
		initialValues.put(KEY_CREDIT, credit);
		initialValues.put(KEY_BALANCE, balance);
		initialValues.put(KEY_STATUS, status);
		initialValues.put(KEY_POSTED_DATE, postedDate);
		initialValues.put(KEY_TRANSACTION_TYPE, transType);
		initialValues.put(KEY_YEAR, year);
		initialValues.put(KEY_MONTH, month);

		long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);

		return rowId;
	}

	public boolean deleteDetails(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public void dropTableDatabase() {
		mDb.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		mDb.execSQL(DATABASE_CREATE);
	}

	public void deleteAllDetails() {

		mDb.execSQL("DELETE FROM " + DATABASE_TABLE);
	}

	public Cursor fetchAllDetails() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_DEBIT,
				KEY_CREDIT, KEY_BALANCE, KEY_STATUS, KEY_POSTED_DATE,
				KEY_TRANSACTION, KEY_TRANSACTION_ID, KEY_TRANSACTION_TYPE,
				KEY_YEAR, KEY_MONTH }, null, null, null, null, null);
	}

	public Cursor fetchDetailByType(String type) throws SQLException {
		Cursor mCursor =

		mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " where "
				+ KEY_TRANSACTION_TYPE + "= ?", new String[] { type });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchDetailByType(String type, String status)
			throws SQLException {
		Cursor mCursor =

		mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " where ("
				+ KEY_STATUS + "= ? OR " + KEY_STATUS + " IS NULL) AND "
				+ KEY_TRANSACTION_TYPE + "= ?", new String[] { status, type });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor defaultSearch(int month, int year, String type, String status)
			throws SQLException {

		String monthStr = null;
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = String.valueOf(month);
		}
		Log.d(TAG, "month: " + monthStr + " year: " + year);
		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_YEAR + "= ? AND " + KEY_MONTH + "= ? AND ("
				+ KEY_STATUS + "= ? OR " + KEY_STATUS + " IS NULL ) AND "
				+ KEY_TRANSACTION_TYPE + "= ?", new String[] {
				String.valueOf(year), monthStr, status, type });
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor defaultSearch(int month, int year, String type)
			throws SQLException {
		Log.d(TAG, "Inside DefaultSearchCursor: " + month);
		String monthStr = null;
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = String.valueOf(month);
		}
		Log.d(TAG, "month: " + monthStr + " year: " + year);
		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_YEAR + "= ? AND " + KEY_MONTH + "= ? AND "
				+ KEY_TRANSACTION_TYPE + "= ?", new String[] {
				String.valueOf(year), monthStr, type });
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor searchDetailByMonthYear(String month, String year, String type)
			throws SQLException {

		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_YEAR + "= ? AND " + KEY_MONTH + "= ? AND "
				+ KEY_TRANSACTION_TYPE + "= ?", new String[] { year, month,
				type });
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor searchDetailByMonthYear(String month, String year,
			String type, String status) throws SQLException {

		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_YEAR + "= ? AND " + KEY_MONTH + "= ? AND "
				+ KEY_TRANSACTION_TYPE + "= ?AND (" + KEY_STATUS + "= ? OR "
				+ KEY_STATUS + " IS NULL )", new String[] { year, month, type,
				status });
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor searchDetailByTransactionId(String transId, String type)
			throws SQLException {

		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_TRANSACTION_ID + "= ? AND "
				+ KEY_TRANSACTION_TYPE + "= ?", new String[] { transId, type });
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor searchDetailByTransactionId(String transId, String type,
			String status) throws SQLException {

		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_TRANSACTION_ID + "= ? AND "
				+ KEY_TRANSACTION_TYPE + "= ?AND (" + KEY_STATUS + "= ? OR "
				+ KEY_STATUS + " IS NULL )", new String[] { transId, type,
				status });
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchDetail(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_TRANSACTION_ID, KEY_TRANSACTION, KEY_DEBIT, KEY_CREDIT,
				KEY_BALANCE, KEY_STATUS, KEY_POSTED_DATE, KEY_TRANSACTION_TYPE,
				KEY_TRANSACTION_TYPE, KEY_YEAR, KEY_MONTH }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateDetails(long rowId, String transId, String trans,
			String debit, String credit, String balance, String status,
			String postedDate, String transType, String year, String month) {

		ContentValues args = new ContentValues();
		args.put(KEY_TRANSACTION_ID, transId);
		args.put(KEY_TRANSACTION, trans);
		args.put(KEY_DEBIT, debit);
		args.put(KEY_CREDIT, credit);
		args.put(KEY_BALANCE, balance);
		args.put(KEY_STATUS, status);
		args.put(KEY_POSTED_DATE, postedDate);
		args.put(KEY_TRANSACTION_TYPE, transType);
		args.put(KEY_YEAR, year);
		args.put(KEY_MONTH, month);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

}
