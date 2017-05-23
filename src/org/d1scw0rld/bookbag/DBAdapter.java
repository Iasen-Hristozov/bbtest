package com.discworld.booksbag;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.InputType;
import android.util.Log;

import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.Field;
import com.discworld.booksbag.dto.FieldType;
import com.discworld.booksbag.dummy.DummyContent;

public class DBAdapter
{
   private static final String TAG = "DB";
	public static final String DATABASE_NAME = "books_bag.db";
   private static final int DATABASE_VERSION = 1;

   private static final String TABLE_BOOKS = "books";
   private static final String TABLE_FIELDS = "fields";
   private static final String TABLE_BOOK_FIELDS = "book_fields";
	private static final String TABLE_PRESENCES = "presences";

   // Common column names
   private static final String KEY_ID = "_id";

   // BOOKS column names
   private static final String KEY_TTL = "title";
   private static final String KEY_DSCR = "description";
   private static final String KEY_VLM = "volume";
   private static final String KEY_PBL_DT = "publication_date";
   private static final String KEY_PGS = "pages";
   private static final String KEY_PRC = "price";
   private static final String KEY_VL = "value";
   private static final String KEY_CND_ID = "condition_id";
   private static final String KEY_DUE_DT = "due_date";
   private static final String KEY_RD_DT = "read_date";
   private static final String KEY_EDN = "edition";
   private static final String KEY_ISBN = "isbn";
   private static final String KEY_WEB = "web";

   // FIELDS column names
   private static final String KEY_NM = "name";
   private static final String KEY_TP_ID = "type_id";

   // BOOK_FIELDS column names
   private static final String KEY_BK_ID = "book_id";
   private static final String KEY_FLD_ID = "field_id";

   // Table Create Statements
   // BOOKS table create statement
   private static final String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " ("
         + KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
         + KEY_TTL + " TEXT, "
         + KEY_DSCR + " TEXT, "
         + KEY_VLM + " INTEGER, "
         + KEY_PBL_DT + " INTEGER, "
         + KEY_PGS + " INTEGER, "
         + KEY_PRC + " TEXT, "
         + KEY_VL + " TEXT, "
         + KEY_DUE_DT + " INTEGER, "
         + KEY_RD_DT + " INTEGER, "
         + KEY_EDN + " INTEGER, "
         + KEY_ISBN + " TEXT, "
         + KEY_WEB + " TEXT)";

   private static final int ID_KEY_ID = 0,
                            ID_KEY_TTL = 1,
                            ID_KEY_DSCR = 2,
                            ID_KEY_VLM = 3,
                            ID_KEY_PBL_DT = 4,
                            ID_KEY_PGS = 5,
                            ID_KEY_PRC = 6,
                            ID_KEY_VL = 7,
                            ID_KEY_DUE_DT = 8,
                            ID_KEY_RD_DT = 9,
                            ID_KEY_EDN = 10,
                            ID_KEY_ISBN = 11,
                            ID_KEY_WEB = 12,
                            ID_KEY_TP_ID = 1,
                            ID_KEY_NM = 2;

   // FIELDS table create statement
   private static final String CREATE_TABLE_FIELDS = "CREATE TABLE " + TABLE_FIELDS + " ("
            + KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + KEY_TP_ID + " INTEGER, "
            + KEY_NM + " TEXT)";

   // BOOK_FIELDS table create statement
   private static final String CREATE_TABLE_BOOK_FIELDS = "CREATE TABLE " + TABLE_BOOK_FIELDS + " ("
         + KEY_BK_ID + " INTEGER, "
         + KEY_FLD_ID + " INTEGER, "
         + KEY_TP_ID + " INTEGER)";
   
	private SQLiteDatabase db;
	private final Context context;
	private DBOpenHelper dbHelper;
	
	public final static char separator = DecimalFormatSymbols.getInstance().getDecimalSeparator();

   public final static int FLD_AUTHOR = 1,
                           FLD_SERIE = 2,
                           FLD_CATEGORY = 3,
                           FLD_LANGUAGE = 4,
                           FLD_PUBLISHER = 5,
                           FLD_PUBLICATION_LOCATION = 6,
                           FLD_STATUS = 7,
                           FLD_RATING = 8,
                           FLD_FORMAT = 9,
                           FLD_LOCATION = 10,
                           FLD_CONDITION = 11,
                           FLD_CURRENCY = 13,
                           FLD_TITLE = 99,
                           FLD_DESCRIPTION = 100,
                           FLD_VOLUME = 101,
                           FLD_PUBLICATION_DATE = 102,
                           FLD_PAGES = 103,
                           FLD_PRICE = 104,
                           FLD_VALUE = 105,
                           FLD_DUE_DATE = 106,
                           FLD_READ_DATE = 107,
                           FLD_EDITION = 108,
                           FLD_ISBN = 109,
                           FLD_WEB = 110,
                           FLD_PRICE_CURRENCY = 111,
                           FLD_VALUE_CURRENCY = 112;                           

   public final static int ORD_TTL = 1,
                           ORD_AUT = 2;

   public final static ArrayList<FieldType> FIELD_TYPES = new ArrayList<FieldType>();
//   {/**
//       * 
//       */
//      private static final long serialVersionUID = 1397960005890445623L;
//
//   {
//      add(new FieldType(FLD_TITLE, "Title", true, FieldType.TYPE_TEXT));
//      add(new FieldType(FLD_AUTHOR, "Authors"));
//      add(new FieldType(FLD_DESCRIPTION, "Description", true, FieldType.TYPE_TEXT_MULTILINE));
//      add(new FieldType(FLD_SERIE, "Serie"));
//      add(new FieldType(FLD_VOLUME, "Volume"));
//      add(new FieldType(FLD_CATEGORY, "Category"));
//      add(new FieldType(FLD_LANGUAGE, "Language"));
//      add(new FieldType(FLD_PAGES, "Pages"));
//      add(new FieldType(FLD_PUBLISHER, "Publisher"));
//      add(new FieldType(FLD_PUBLICATION_DATE, "Publication Date"));
//      add(new FieldType(FLD_PUBLICATION_LOCATION, "Publication Location"));
//      add(new FieldType(FLD_EDITION, "Edition"));
//      add(new FieldType(FLD_PRICE, "Price"));
//      add(new FieldType(FLD_STATUS, "Status"));
//      add(new FieldType(FLD_VALUE, "Value"));
//      add(new FieldType(FLD_READ_DATE, "Read Date"));
//      add(new FieldType(FLD_RATING, "Rating"));
//      add(new FieldType(FLD_FORMAT, "Format"));
//      add(new FieldType(FLD_LOCATION, "Location"));
//      add(new FieldType(FLD_CONDITION, "Condition"));
//      add(new FieldType(FLD_DUE_DATE, "Due Date"));
//      add(new FieldType(FLD_ISBN, "ISBN"));
//      add(new FieldType(FLD_WEB, "Web"));
//   }};
   
	public DBAdapter(Context _context)
	{
		this.context = _context;
		dbHelper = new DBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		Resources r = context.getResources();
		FIELD_TYPES.clear();
		FIELD_TYPES.add(new FieldType(FLD_TITLE, r.getString(R.string.fld_title), FieldType.TYPE_TEXT).setVisibility(true));
		FIELD_TYPES.add(new FieldType(FLD_AUTHOR, r.getString(R.string.fld_author), FieldType.TYPE_MULTIFIELD).setVisibility(true));
//		FIELD_TYPES.add(new FieldType(FLD_DESCRIPTION, r.getString(R.string.fld_descrition), true, FieldType.TYPE_TEXT_MULTILINE).setMultiline(false));
		FIELD_TYPES.add(new FieldType(FLD_DESCRIPTION, r.getString(R.string.fld_descrition), FieldType.TYPE_TEXT).setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE).setVisibility(false));
		FIELD_TYPES.add(new FieldType(FLD_SERIE, r.getString(R.string.fld_serie), FieldType.TYPE_TEXT_AUTOCOMPLETE));
		FIELD_TYPES.add(new FieldType(FLD_VOLUME, r.getString(R.string.fld_volume), FieldType.TYPE_TEXT).setInputType(InputType.TYPE_CLASS_NUMBER));
		FIELD_TYPES.add(new FieldType(FLD_CATEGORY, r.getString(R.string.fld_category), FieldType.TYPE_MULTI_SPINNER));
		FIELD_TYPES.add(new FieldType(FLD_LANGUAGE, r.getString(R.string.fld_language), FieldType.TYPE_SPINNER));
		FIELD_TYPES.add(new FieldType(FLD_PAGES, r.getString(R.string.fld_pages), FieldType.TYPE_TEXT).setInputType(InputType.TYPE_CLASS_NUMBER));
		FIELD_TYPES.add(new FieldType(FLD_PUBLISHER, r.getString(R.string.fld_publisher), FieldType.TYPE_TEXT_AUTOCOMPLETE));
		FIELD_TYPES.add(new FieldType(FLD_PUBLICATION_DATE, r.getString(R.string.fld_publication_date), FieldType.TYPE_TEXT).setInputType(InputType.TYPE_CLASS_NUMBER));
		FIELD_TYPES.add(new FieldType(FLD_PUBLICATION_LOCATION, r.getString(R.string.fld_publication_location), FieldType.TYPE_TEXT_AUTOCOMPLETE));
		FIELD_TYPES.add(new FieldType(FLD_EDITION, r.getString(R.string.fld_edition), FieldType.TYPE_TEXT).setInputType(InputType.TYPE_CLASS_NUMBER));
		FIELD_TYPES.add(new FieldType(FLD_PRICE, r.getString(R.string.fld_price), FieldType.TYPE_MONEY).setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL));
		FIELD_TYPES.add(new FieldType(FLD_STATUS, r.getString(R.string.fld_status), FieldType.TYPE_SPINNER));
		FIELD_TYPES.add(new FieldType(FLD_VALUE, r.getString(R.string.fld_value), FieldType.TYPE_MONEY).setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL));
		FIELD_TYPES.add(new FieldType(FLD_READ_DATE, r.getString(R.string.fld_read_date), FieldType.TYPE_DATE));
		FIELD_TYPES.add(new FieldType(FLD_RATING, r.getString(R.string.fld_rating), FieldType.TYPE_SPINNER));
		FIELD_TYPES.add(new FieldType(FLD_FORMAT, r.getString(R.string.fld_format), FieldType.TYPE_SPINNER));
		FIELD_TYPES.add(new FieldType(FLD_LOCATION, r.getString(R.string.fld_location), FieldType.TYPE_TEXT_AUTOCOMPLETE));
		FIELD_TYPES.add(new FieldType(FLD_CONDITION, r.getString(R.string.fld_condition), FieldType.TYPE_SPINNER));
		FIELD_TYPES.add(new FieldType(FLD_DUE_DATE, r.getString(R.string.fld_due_date), FieldType.TYPE_DATE));
		FIELD_TYPES.add(new FieldType(FLD_ISBN, r.getString(R.string.fld_isbn), FieldType.TYPE_TEXT).setInputType(InputType.TYPE_CLASS_NUMBER));
		FIELD_TYPES.add(new FieldType(FLD_WEB, r.getString(R.string.fld_web), FieldType.TYPE_TEXT));
	}

	public void open() throws SQLiteException
	{
		try
		{
			db = dbHelper.getWritableDatabase();
		}
		catch(SQLiteException ex)
		{
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close()
	{
		db.close();
	}

   public ArrayList<Book> getBooksOrderedBy(String query)
   {
      if(Debug.ON)
      {
         return DummyContent.BOOKS;
      }
      else
      {
         ArrayList<Book> alBooks = new ArrayList<Book>();
   
   //      String query = "b." + KEY_ID + ", b." + KEY_TTL + ", GROUP_CONCAT(f." + KEY_NM + ") AS authors "
   //            + "FROM " + TABLE_BOOKS + " AS b LEFT JOIN " + TABLE_BOOK_FIELDS + " AS bf ON bf." + KEY_BK_ID + " = " + "b." + KEY_ID
   //            + " LEFT JOIN " + TABLE_FIELDS + " AS f ON f." + KEY_ID + " = bf." + KEY_FLD_ID
   //            + " WHERE bf." + KEY_TP_ID + " = " + FLD_AUT
   //            + " GROUP BY b." + KEY_ID
   //            + " ORDER BY b." + KEY_TTL;
   
         Cursor cursor = db.rawQuery(query, null);
         Book oBook;
   
         if(cursor.moveToFirst())
         {
            do
            {
   //            oBook = new Field(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
               oBook = new Book(Integer.parseInt(cursor.getString(ID_KEY_ID)),
                        cursor.getString(ID_KEY_TTL),
                        cursor.getString(ID_KEY_DSCR),
                        Integer.parseInt(cursor.getString(ID_KEY_VLM)),
                        Integer.parseInt(cursor.getString(ID_KEY_PBL_DT)),
                        Integer.parseInt(cursor.getString(ID_KEY_PGS)),
//                        Integer.parseInt(cursor.getString(ID_KEY_PRC)),
//                        Integer.parseInt(cursor.getString(ID_KEY_VL)),
                        cursor.getString(ID_KEY_PRC),
                        cursor.getString(ID_KEY_VL),
                        Integer.parseInt(cursor.getString(ID_KEY_DUE_DT)),
                        Integer.parseInt(cursor.getString(ID_KEY_RD_DT)),
                        Integer.parseInt(cursor.getString(ID_KEY_EDN)),
                        cursor.getString(ID_KEY_ISBN),
                        cursor.getString(ID_KEY_WEB));
               
               alBooks.add(oBook);
            } while (cursor.moveToNext());
         }
         cursor.close();
   
         return alBooks;
      }      
   }

   public ArrayList<Book> getBooks(int iOrder)
   {
      String query = "";
      switch(iOrder)
      {
         case ORD_TTL:
            query = "b." + KEY_ID + ", b." + KEY_TTL + ", GROUP_CONCAT(f." + KEY_NM + ") AS authors "
                  + "FROM " + TABLE_BOOKS + " AS b LEFT JOIN " + TABLE_BOOK_FIELDS + " AS bf ON bf." + KEY_BK_ID + " = " + "b." + KEY_ID
                  + " LEFT JOIN " + TABLE_FIELDS + " AS f ON f." + KEY_ID + " = bf." + KEY_FLD_ID
                  + " WHERE bf." + KEY_TP_ID + " = " + FLD_AUTHOR
                  + " GROUP BY b." + KEY_ID
                  + " ORDER BY b." + KEY_TTL;
         break;

         case ORD_AUT:
            query = "b." + KEY_ID + ", b." + KEY_TTL + ", GROUP_CONCAT(f." + KEY_NM + ") AS authors "
                  + "FROM " + TABLE_BOOKS + " AS b LEFT JOIN " + TABLE_BOOK_FIELDS + " AS bf ON bf." + KEY_BK_ID + " = " + "b." + KEY_ID
                  + " LEFT JOIN " + TABLE_FIELDS + " AS f ON f." + KEY_ID + " = bf." + KEY_FLD_ID
                  + " WHERE bf." + KEY_TP_ID + " = " + FLD_AUTHOR
                  + " GROUP BY b." + KEY_ID
                  + " ORDER BY authors";
         break;

         default:
            return null;
      }

      return getBooksOrderedBy(query);
   }

   public void insertBook(Book oBook)
   {
//      String sql = "INSERT INTO "+ TABLE_BOOKS +" VALUES (?,?,?,?,?,?,?,?,?,?);";
//      SQLiteStatement statement = db.compileStatement(sql);
      db.beginTransaction();
      try
      {

//         statement.clearBindings();

         ContentValues values = new ContentValues();
         values.put(KEY_TTL, oBook.csTitle.value);
         values.put(KEY_DSCR, oBook.csDescription.value);
         values.put(KEY_VLM, oBook.ciVolume.value);
         values.put(KEY_PBL_DT, oBook.ciPublicationDate.value);
         values.put(KEY_PGS, oBook.ciPages.value);
         values.put(KEY_PRC, oBook.csPrice.value);
         values.put(KEY_VL, oBook.csValue.value);
         values.put(KEY_DUE_DT, oBook.ciDueDate.value);
         values.put(KEY_RD_DT, oBook.ciReadDate.value);
         values.put(KEY_EDN, oBook.ciEdition.value);
         values.put(KEY_ISBN, oBook.csISBN.value);
         values.put(KEY_WEB, oBook.csWeb.value);

         long iBookID = db.insert(TABLE_BOOKS, null, values);
         
         for(int i = 0; i < oBook.alFields.size(); i++)
         {
            if (oBook.alFields.get(i).iID == 0)
            {
               values = new ContentValues();
               values.put(KEY_TP_ID, oBook.alFields.get(i).iTypeID);
               values.put(KEY_NM, oBook.alFields.get(i).sValue);
               oBook.alFields.get(i).iID = db.insert(TABLE_FIELDS, null, values);
            }
         }

         for (Field oField : oBook.alFields)
         {
            values = new ContentValues();
            values.put(KEY_FLD_ID, oField.iID);
            values.put(KEY_TP_ID, oField.iTypeID);
            values.put(KEY_BK_ID, iBookID);
            long res = db.insert(TABLE_BOOK_FIELDS, null, values);
         }

//      statement.bindString(KEY_TTL_ID, oBook.sTitle);
//      statement.bindString(KEY_DSCR_ID, oBook.sDescription);
//      statement.bindLong(KEY_VLM_ID, oBook.iVolume);
//      statement.bindLong(KEY_PBL_DT_ID, oBook.iPublicationDate);
//      statement.bindLong(KEY_PGS_ID, oBook.iPages);
//      statement.bindLong(KEY_PRC_ID, oBook.iPrice);
//      statement.bindLong(KEY_VL_ID, oBook.iValue);
//      statement.bindLong(KEY_RD_DT_ID, oBook.iReadDate);
//      statement.bindString(KEY_ISBN_ID, oBook.sISBN);
//      statement.bindString(KEY_WEB_ID, oBook.sWeb);
//
//      statement.execute();
         db.setTransactionSuccessful();
      }
      finally
      {
         db.endTransaction();
      }
      shrink();
   }

   public ArrayList<Field> getFieldValues(int iTypeID)
   {
      if(Debug.ON)
      {
         switch(iTypeID)
         {
            case FLD_AUTHOR:
               return DummyContent.AUTHORS;
            case FLD_CATEGORY:
               return DummyContent.CATEGORIES;
            case FLD_CONDITION:
               return DummyContent.CONDITIONS;
            case FLD_CURRENCY:
               return DummyContent.CURRENCIES;
            case FLD_FORMAT:
               return DummyContent.FORMATS;
            case FLD_LANGUAGE:
               return DummyContent.LANGUAGES;
            case FLD_LOCATION:
               return DummyContent.LOCATIONS;
            case FLD_PUBLICATION_LOCATION:
               return DummyContent.PUBLISHING_LOCATIONS;
            case FLD_PUBLISHER:
               return DummyContent.PUBLISHERS;
            case FLD_RATING:
               return DummyContent.RATINGS;
            case FLD_SERIE:
               return DummyContent.SERIES;
            case FLD_STATUS:
               return DummyContent.STATUS;
            default:
               return null;
         }
      }
      else
      {
         ArrayList<Field> alFieldValues = new ArrayList<>();
   
   //      SELECT f.id, bf.type_id, f.name
   //      FROM book_fields as bf LEFT JOIN fields AS f ON bf.field_id = f.id
   //      WHERE bf.type_id =1
   //      ORDER BY f.name
   
         String sql = "SELECT f." + KEY_ID + ", bf." + KEY_TP_ID + ", f." + KEY_NM
               + " FROM " + TABLE_BOOK_FIELDS + " as bf LEFT JOIN " + TABLE_FIELDS + " AS f ON bf." + KEY_FLD_ID + " = f." + KEY_ID
               + " WHERE bf." + KEY_TP_ID + " = " + iTypeID
               + "ORDER BY f." + KEY_NM;
   
         Cursor cursor = db.rawQuery(sql, null);
   
         Field oField;
   //      ArrayList<Field> alFields = new ArrayList<>();
         if(cursor.moveToFirst())
         {
            do
            {
               oField = new Field(Integer.parseInt(cursor.getString(ID_KEY_ID)),
                                  Integer.parseInt(cursor.getString(ID_KEY_TP_ID)),
                                  cursor.getString(ID_KEY_NM));
   
               alFieldValues.add(oField);
            } while (cursor.moveToNext());
         }
   
         return alFieldValues;
      }
   }

   public Book getBook(long iBookID)
   {
      if(Debug.ON)
      {
         return DummyContent.BOOKS_MAP.get(iBookID);
      }
      else
      {
         Book oBook = null;
   
         Cursor cursor = db.query(TABLE_BOOKS,
                                  null,
                                  KEY_ID + " = " + iBookID,
                                  null,
                                  null,
                                  null,
                                  null);
   
         if(cursor.moveToFirst())
         {
            oBook = new Book(Integer.parseInt(cursor.getString(ID_KEY_ID)),
                             cursor.getString(ID_KEY_TTL),
                             cursor.getString(ID_KEY_DSCR),
                             Integer.parseInt(cursor.getString(ID_KEY_VLM)),
                             Integer.parseInt(cursor.getString(ID_KEY_PBL_DT)),
                             Integer.parseInt(cursor.getString(ID_KEY_PGS)),
//                             Integer.parseInt(cursor.getString(ID_KEY_PRC)),
//                             Integer.parseInt(cursor.getString(ID_KEY_VL)),
                             cursor.getString(ID_KEY_PRC),
                             cursor.getString(ID_KEY_VL),
                             Integer.parseInt(cursor.getString(ID_KEY_DUE_DT)),
                             Integer.parseInt(cursor.getString(ID_KEY_RD_DT)),
                             Integer.parseInt(cursor.getString(ID_KEY_EDN)),
                             cursor.getString(ID_KEY_ISBN),
                             cursor.getString(ID_KEY_WEB));
         }
   
         String sql = "SELECT f." + KEY_ID + ", bf." + KEY_TP_ID + ", f." + KEY_NM
                     +" FROM " + TABLE_BOOK_FIELDS + " as bf LEFT JOIN " + TABLE_FIELDS + " AS f ON bf." + KEY_FLD_ID + " = f." + KEY_ID
                     +" WHERE bf." + KEY_ID + " = " + iBookID;
   
         cursor = db.rawQuery(sql, null);
   
         Field oField;
         ArrayList<Field> alFields = new ArrayList<>();
         if(cursor.moveToFirst())
         {
            do
            {
               oField = new Field(Integer.parseInt(cursor.getString(ID_KEY_ID)),
                                  Integer.parseInt(cursor.getString(ID_KEY_TP_ID)),
                                  cursor.getString(ID_KEY_NM));
   
               alFields.add(oField);
            } while (cursor.moveToNext());
         }
         oBook.alFields = alFields;
   
         return oBook;
      }
      
   }

   public boolean deleteBook(long iBookID)
   {
      boolean result = true;

      if(Debug.ON)
      {
         DummyContent.BOOKS_MAP.remove(iBookID);         
      }
      else
      {
         db.beginTransaction();
         try
         {
            db.delete(TABLE_BOOK_FIELDS, KEY_BK_ID + " = " + iBookID, null);
            db.delete(TABLE_BOOKS, KEY_ID + " = " + iBookID, null);
         }
         catch (Exception e)
         {
            Log.e(TAG, e.getMessage());
            result = false;
         }
         finally
         {
            db.endTransaction();
            shrink();
         }
      }

      return result;
   }

   public boolean updateBook(Book oBook)
   {
      
      if(Debug.ON)
      {
         DummyContent.BOOKS_MAP.remove(oBook.iID);
         DummyContent.BOOKS_MAP.put(oBook.iID, oBook);
         DummyContent.BOOKS.remove(oBook);
         DummyContent.BOOKS.add(oBook);
         return true;
      }
      else
      {
         ContentValues oValues;
         boolean result = true;

         db.beginTransaction();
         try
         {
            for (Field oField : oBook.alFields)
            {
               if (oField.iID == 0)
               {
                  oValues = new ContentValues();
                  oValues.put(KEY_NM, oField.sValue);
                  oField.iID = db.insert(TABLE_FIELDS, null, oValues);
               }
            }
   
            db.delete(TABLE_BOOK_FIELDS, KEY_BK_ID + " = " + oBook.iID, null);
   
            for (Field oField : oBook.alFields)
            {
               oValues = new ContentValues();
               oValues.put(KEY_FLD_ID, oField.iID);
               oValues.put(KEY_TP_ID, oField.iTypeID);
               oValues.put(KEY_BK_ID, oBook.iID);
               db.insert(TABLE_BOOK_FIELDS, null, oValues);
            }
   
            oValues = new ContentValues();
            oValues.put(KEY_TTL, oBook.sTitle);
            oValues.put(KEY_DSCR, oBook.sDescription);
            oValues.put(KEY_VLM, oBook.iVolume);
            oValues.put(KEY_PBL_DT, oBook.iPublicationDate);
            oValues.put(KEY_PGS, oBook.iPages);
//            oValues.put(KEY_PRC, oBook.iPrice);
//            oValues.put(KEY_VL, oBook.iValue);
            oValues.put(KEY_PRC, oBook.sPrice);
            oValues.put(KEY_VL, oBook.sValue);
            oValues.put(KEY_DUE_DT, oBook.iDueDate);
            oValues.put(KEY_RD_DT, oBook.iReadDate);
            oValues.put(KEY_EDN, oBook.iEdition);
            oValues.put(KEY_ISBN, oBook.sISBN);
            oValues.put(KEY_WEB, oBook.sWeb);
            db.update(TABLE_PRESENCES,
                      oValues,
                      KEY_ID + " = ?" + oBook.iID,
                      null);
   
            db.setTransactionSuccessful();
         }
         catch (Exception e)
         {
            Log.e(TAG, e.getMessage());
            result = false;
         }
         finally
         {
            db.endTransaction();
            shrink();
         }
         return result;
      }
   }

   public void shrink()
   {
      db.execSQL("VACUUM");
   }

   private static class DBOpenHelper extends SQLiteOpenHelper
	{
      private final static String msg_ftm = "Error inserting in %1$s TypeID:%2$d Value:%3$s";
		
      public DBOpenHelper(Context context, String name, CursorFactory factory, int version)
		{
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db)
		{
			_db.execSQL(CREATE_TABLE_BOOKS);
			_db.execSQL(CREATE_TABLE_FIELDS);
			_db.execSQL(CREATE_TABLE_BOOK_FIELDS);

			_db.beginTransaction();
	      try
	      {
	         ContentValues values;
	         String msg;
	         
	         for(Field f: DummyContent.LANGUAGES)
	         {
	            values = new ContentValues();
	            values.put(KEY_TP_ID, f.iTypeID);
	            values.put(KEY_NM, f.sValue);
               if(_db.insert(TABLE_FIELDS, null, values) < 0)
               {
                  msg = String.format(Locale.getDefault(),msg_ftm, "LANGUAGES", f.iTypeID, f.sValue);
                  throw new RuntimeException(msg);
               }
	         }
	         
            for(Field f: DummyContent.STATUS)
            {
               values = new ContentValues();
               values.put(KEY_TP_ID, f.iTypeID);
               values.put(KEY_NM, f.sValue);
               if(_db.insert(TABLE_FIELDS, null, values) < 0)
               {
                  msg = String.format(Locale.getDefault(), msg_ftm, "STATUS", f.iTypeID, f.sValue);
                  throw new RuntimeException(msg);
               }
            }

            for(Field f: DummyContent.RATINGS)
            {
               values = new ContentValues();
               values.put(KEY_TP_ID, f.iTypeID);
               values.put(KEY_NM, f.sValue);
               if(_db.insert(TABLE_FIELDS, null, values) < 0)
               {
                  msg = String.format(Locale.getDefault(), msg_ftm, "RATINGS", f.iTypeID, f.sValue);
                  throw new RuntimeException(msg);
               }
            }

            for(Field f: DummyContent.FORMATS)
            {
               values = new ContentValues();
               values.put(KEY_TP_ID, f.iTypeID);
               values.put(KEY_NM, f.sValue);
               if(_db.insert(TABLE_FIELDS, null, values) < 0)
               {
                  msg = String.format(Locale.getDefault(), msg_ftm, "FORMATS", f.iTypeID, f.sValue);
                  throw new RuntimeException(msg);
               }
            }
            
            for(Field f: DummyContent.CONDITIONS)
            {
               values = new ContentValues();
               values.put(KEY_TP_ID, f.iTypeID);
               values.put(KEY_NM, f.sValue);
               if(_db.insert(TABLE_FIELDS, null, values) < 0)
               {
                  msg = String.format(Locale.getDefault(), msg_ftm, "CONDITIONS", f.iTypeID, f.sValue);
                  throw new RuntimeException(msg);
               }
            }
            
            for(Field f: DummyContent.CURRENCIES)
            {
               values = new ContentValues();
               values.put(KEY_TP_ID, f.iTypeID);
               values.put(KEY_NM, f.sValue);
               if(_db.insert(TABLE_FIELDS, null, values) < 0)
               {
                  msg = String.format(Locale.getDefault(), msg_ftm, "CURRENCIES", f.iTypeID, f.sValue);
                  throw new RuntimeException(msg);
               }
            }
            _db.setTransactionSuccessful();
	      }
	      catch(RuntimeException e)
	      {
	         Log.e("TaskDBAdapter", e.getMessage());
	      }
            
	      finally
	      {
	         _db.endTransaction();
         }
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
		{
			Log.w("TaskDBAdapter", "Upgrade from version " + _oldVersion + " to " + _newVersion + ", which will destroy all old data");
			// on upgrade drop older tables
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK_FIELDS);

			// create new tables
			onCreate(_db);
		}
	}
}
