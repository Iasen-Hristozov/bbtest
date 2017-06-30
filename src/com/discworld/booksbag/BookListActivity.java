package com.discworld.booksbag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.discworld.booksbag.dto.BooksAdapter;
import com.discworld.booksbag.dto.DividerItemDecoration;
//import com.discworld.booksbag.dto.ParrentAdapter;
import com.discworld.booksbag.dto.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity
{
   public final static int SHOW_EDIT_BOOK = 101,
                           SHOW_EDIT_BOOK_COPY = 102;
   private static final String XPR_DIR = "BooksBag",
                               DB_PATH = "//data//com.discworld.booksbag//databases//",
                               PREF_ORDER_ID = "order_id",
                               PREF_EXPAND_ALL = "pref_expand_all";
   
   private int iOrderID = DBAdapter.ORD_TTL,
               iClickedItemNdx = -1;
   
   private boolean bExpandAll = false;
   
   private long sel_id;
   
//   private EditText etFilter;
   
   private TextView tvBooksCount;
   
   private DBAdapter oDbAdapter = null;
   
   private ArrayList<OrderItem> alOrderItems = new ArrayList<>();
   
//   private ParrentAdapter oSimpleItemRecyclerViewAdapter;
   private BooksAdapter oBooksAdapter;
   
   private SharedPreferences oPreferences;
   
   private View.OnClickListener onRecyclerViewClickListener = new View.OnClickListener()
   {
      @Override
      public void onClick(View v)
      {
         iClickedItemNdx = recyclerView.indexOfChild(v);
         sel_id = oBooksAdapter.getItemId(iClickedItemNdx);
         
         if(mTwoPane)
         {
            Bundle arguments = new Bundle();
            arguments.putLong(BookDetailFragment.ARG_ITEM_ID, sel_id);
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.book_detail_container, fragment)
                                       .commit();
         } 
         else
         {
            Context context = v.getContext();
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.ARG_ITEM_ID, sel_id);

            // context.startActivity (intent);
            startActivityForResult(intent, 0);
         }         
      }
   };

   private View.OnLongClickListener onRecyclerViewLongClickListener = new View.OnLongClickListener()
   {
      @Override
      public boolean onLongClick(View v)
      {
         iClickedItemNdx = recyclerView.indexOfChild(v);
         sel_id = oBooksAdapter.getItemId(iClickedItemNdx);
         if(mActionMode != null)
         {
            return false;
         }

         // Start the CAB using the ActionMode.Callback defined above
         startSupportActionMode(mActionModeCallback);

         v.setSelected(true);
         return true;      
      }
   };
   
   private RecyclerView recyclerView;
   
   private ActionMode mActionMode;

   /**
    * Whether or not the activity is in two-pane mode, i.e. running on a tablet
    * device.
    */
   private boolean mTwoPane,
                   bUpdate = true;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_book_list);
  
      oPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      loadPreferences();
      SharedPreferences.OnSharedPreferenceChangeListener listener  = new SharedPreferences.OnSharedPreferenceChangeListener() 
      {
         public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
         {
            if(key.equalsIgnoreCase(PREF_EXPAND_ALL))
               bExpandAll = oPreferences.getBoolean(PREF_EXPAND_ALL, false); 
         }
      };

      oPreferences.registerOnSharedPreferenceChangeListener(listener);
      
      tvBooksCount = (TextView) findViewById(R.id.tv_books_count);
      
      oDbAdapter = new DBAdapter(this);
//      oDbAdapter.open();
      
      verifyStoragePermissions(this);
      
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      toolbar.setTitle(getTitle());

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View view)
         {
            Intent intent = new Intent(getApplicationContext(), EditBookActivity.class);
            intent.putExtra(EditBookActivity.BOOK_ID, 0);
//            intent.setClass(getApplicationContext(), EditBookActivity.class);
            startActivityForResult(intent, SHOW_EDIT_BOOK);
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
//                    .show();
         }
      });
      
//      RecyclerView.ItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
      recyclerView = (RecyclerView) findViewById(R.id.book_list);
      assert recyclerView != null;
      recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
      recyclerView.setItemAnimator(new DefaultItemAnimator());
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      

//      oDbAdapter.open();
//      oSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(oDbAdapter.getBooks(DBAdapter.ORD_TTL));
//      recyclerView.setAdapter(oSimpleItemRecyclerViewAdapter);

//      DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//               layoutManager.getOrientation());
//      RecyclerView.ItemDecoration itemDecoration =
//               new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);      
//           recyclerView.addItemDecoration(dividerItemDecoration);      
//      setupRecyclerView((RecyclerView) recyclerView);
//      oSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(oDbAdapter.getBooks(DBAdapter.ORD_TTL));
      
//      etFilter = (EditText) findViewById(R.id.et_filter);
// 
//      // Capture Text in EditText
//      etFilter.addTextChangedListener(new TextWatcher() 
//      {
// 
//         @Override
//         public void afterTextChanged(Editable arg0) 
//         {
//            // TODO Auto-generated method stub
//            String text = etFilter.getText().toString().toLowerCase(Locale.getDefault());
//            oSimpleItemRecyclerViewAdapter.filter(text);
//         }
// 
//         @Override
//         public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
//         {
//            // TODO Auto-generated method stub
//         }
// 
//         @Override
//         public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
//         {
//            // TODO Auto-generated method stub
//         }
//      });

      if (findViewById(R.id.book_detail_container) != null)
      {
         // The detail container view will be present only in the
         // large-screen layouts (res/values-w900dp).
         // If this view is present, then the
         // activity should be in two-pane mode.
         mTwoPane = true;
      }
      
      alOrderItems.add(new OrderItem(DBAdapter.ORD_TTL, getString(R.string.ord_title)));
      alOrderItems.add(new OrderItem(DBAdapter.ORD_AUT, getString(R.string.ord_author)));
   }

   @Override
   protected void onResume()
   {
      super.onResume();

      oDbAdapter.open();
      if(bUpdate)
      {
//         setupRecyclerView((RecyclerView) recyclerView);
//          oSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(oDbAdapter.getBooks(iOrderID));
//          oSimpleItemRecyclerViewAdapter.setClickListener(onRecyclerViewClickListener);
//          oSimpleItemRecyclerViewAdapter.setLongClickListener(onRecyclerViewLongClickListener);
//          recyclerView.swapAdapter(oSimpleItemRecyclerViewAdapter, true);
          setupRecyclerView(recyclerView, iOrderID);
//         etFilter.setText("");
      }

      // oBook = oDbAdapter.getBook(oBook.iID);
   }

   @Override
   protected void onPause()
   {
      oDbAdapter.close();
   
      super.onPause();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.menu_main, menu);
      
      final MenuItem searchItem = menu.findItem(R.id.action_search);
      final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
      
      searchView.setOnQueryTextListener(new OnQueryTextListener()
      {
         
         @Override
         public boolean onQueryTextSubmit(String arg0)
         {
            // TODO Auto-generated method stub
            return false;
         }
         
         @Override
         public boolean onQueryTextChange(String arg0)
         {
            oBooksAdapter.expandAll();
            oBooksAdapter.filter(arg0);
            tvBooksCount.setText(String.valueOf(oBooksAdapter.getAllChildrenCount()) + " " + getString(R.string.lbl_books));
            return true;
         }
      });
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch(item.getItemId())
      {
         case R.id.action_settings:
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
//            intent.setClass(getApplicationContext(), EditBookActivity.class);
            startActivity(intent);
            return true;

         case R.id.action_exp_db:
            oDbAdapter.close();
            if(oDbAdapter.exportDatabase(Environment.getExternalStorageDirectory() + "/" + XPR_DIR + "/" + DBAdapter.DATABASE_NAME))
               Toast.makeText(getApplicationContext(), R.string.prf_xpr_db_scs, Toast.LENGTH_SHORT).show();
            oDbAdapter.open();            
            return true;
         
         case R.id.action_imp_db:
            oDbAdapter.close();
            if(oDbAdapter.importDatabase(Environment.getExternalStorageDirectory() + "/" + XPR_DIR + "/" + DBAdapter.DATABASE_NAME))
               Toast.makeText(getApplicationContext(), R.string.prf_imp_db_scs, Toast.LENGTH_SHORT).show();
            oDbAdapter.open();            
            
            return true;
            
         case R.id.action_exp_all:
            oBooksAdapter.expandAll();
            return true;
         
         case R.id.action_clp_all:
            oBooksAdapter.collapseAll();
            return true;
            
         case R.id.action_sort:
            View menuItemView = findViewById(R.id.action_sort); // SAME ID AS MENU ID
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            for(OrderItem oItem: alOrderItems)
               popupMenu.getMenu().add(1, oItem.iID, 0, oItem.sTitle).setCheckable(true).setChecked(oItem.iID == iOrderID);
            popupMenu.getMenu().setGroupCheckable(1, true, true);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
               
               @Override
               public boolean onMenuItemClick(MenuItem menuItem)
               {
                  iOrderID = menuItem.getItemId();
                  saveOrderID(iOrderID);
                  setupRecyclerView(recyclerView, iOrderID);
                  return true;
               }
            });
//            popupMenu.inflate(R.menu.menu_main);
            popupMenu.show();
            return true;
            
         default:
            return true;
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);
      
      if(resultCode == RESULT_OK)
         bUpdate = true;
      else
         bUpdate = false;
   }



   /**
       * Copies the database file at the specified location over the current
       * internal application database.
       * */
   //   public boolean importDatabase(String dbPath) throws IOException 
   //   {
   //
   //       // Close the SQLiteOpenHelper so it will commit the created empty
   //       // database to internal storage.
   //       oDbAdapter.close();
   //       File newDb = new File(dbPath);
   //       File oldDb = new File(DB_PATH + DBAdapter.DATABASE_NAME);
   //       if (newDb.exists()) 
   //       {
   //           FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
   //           // Access the copied database so SQLiteHelper will cache it and mark
   //           // it as created.
   ////           oDbAdapter.getWritableDatabase().close();
   //           
   //           return true;
   //       }
   //       return false;
   //   }   
      
      public static void verifyStoragePermissions(Activity activity) 
      {
         // Check if we have write permission
         int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
   
         if (permission != PackageManager.PERMISSION_GRANTED) 
         {
             // We don't have permission so prompt the user
             ActivityCompat.requestPermissions(
                     activity,
                     new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                     1);
         }
      }

   private void setupRecyclerView(@NonNull RecyclerView recyclerView, int iOrderID)
   {
      oBooksAdapter = new BooksAdapter(this, oDbAdapter.getBooks1(iOrderID));
//      oSimpleItemRecyclerViewAdapter = new ParrentAdapter(this, oDbAdapter.getBooks1(iOrderID));
      oBooksAdapter.setClickListener(onRecyclerViewClickListener);
      oBooksAdapter.setLongClickListener(onRecyclerViewLongClickListener);
//      oBooksAdapter.setAllExpanded(true);
      if(bExpandAll)
         oBooksAdapter.expandAll();
//      recyclerView.swapAdapter(oSimpleItemRecyclerViewAdapter, true);
      recyclerView.setAdapter(oBooksAdapter);
      tvBooksCount.setText(String.valueOf(oBooksAdapter.getAllChildrenCount()) + " " + getString(R.string.lbl_books));
   }
   
   private void loadPreferences()
   {
      iOrderID = oPreferences.getInt(PREF_ORDER_ID, DBAdapter.ORD_TTL);
      bExpandAll = oPreferences.getBoolean(PREF_EXPAND_ALL, false); 
   }
   
   private void saveOrderID(int iOrderID)
   {
      Editor editor = oPreferences.edit();
      
      editor.putInt(PREF_ORDER_ID, iOrderID);
      
      editor.commit();      
   }

   public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
   {
      private String sFilter;

      OnClickListener onClickListener = null;

      OnLongClickListener onLongClickListener = null;
      
      private final List<Result> alItemsAll, 
                                 alItems;
      
      public void setClickListener(OnClickListener callback) 
      {
         this.onClickListener = callback;
      }      

      public void setLongClickListener(OnLongClickListener callback) 
      {
         this.onLongClickListener = callback;
      }      
      
      public SimpleItemRecyclerViewAdapter(List<Result> items)
      {
         alItemsAll = items;
         alItems = new ArrayList<Result>();
         alItems.addAll(alItemsAll);
         sFilter = "";
      }

      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
      {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_content, parent, false);
         return new ViewHolder(view);
      }

      @Override
      public void onBindViewHolder(final ViewHolder holder, int position)
      {
         holder.mItem = alItems.get(position);
         holder.mIdView.setText(String.valueOf(alItems.get(position).id));
         Spannable spContent = new SpannableString(alItems.get(position).content);
         int iFilteredStart = alItems.get(position).content.indexOf(sFilter);
         int iFilterEnd;
         if(iFilteredStart < 0)
         {
            iFilteredStart = 0;
            iFilterEnd = 0;
         } 
         else
            iFilterEnd = iFilteredStart + sFilter.length();
         spContent.setSpan(new ForegroundColorSpan(ContextCompat.getColor(BookListActivity.this, R.color.accent)),
                                                                          iFilteredStart, iFilterEnd,
                                                                          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
         holder.mContentView.setText(spContent);

         holder.mView.setOnClickListener(onClickListener);
         holder.mView.setOnLongClickListener(onLongClickListener);
      }

      @Override
      public int getItemCount()
      {
         return alItems.size();
      }

      public class ViewHolder extends RecyclerView.ViewHolder
      {
         public final View     mView;
         public final TextView mIdView;
         public final TextView mContentView;
         public Result         mItem;

         public ViewHolder(View view)
         {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
         }

         @Override
         public String toString()
         {
            return super.toString() + " '" + mContentView.getText() + "'";
         }
      }
      
      public void removeAt(int position)
      {
//         boolean b = alItemsAll.remove(alItems.get(position));
         alItems.remove(position);
         notifyItemRemoved(position);
      }

      // Filter Class
      public void filter(String charText)
      {
         charText = charText.toLowerCase(Locale.getDefault());
         sFilter = charText;
         alItems.clear();
         if(charText.length() == 0)
         {
            alItems.addAll(alItemsAll);
         } else
         {
            for(Result result : alItemsAll)
            {
               if(result.content.toLowerCase(Locale.getDefault()).contains(charText))
               {
                  alItems.add(result);
               }
            }
         }
         notifyDataSetChanged();
      }

      @Override
      public long getItemId(int position)
      {
         return alItems.get(position).id;
      }
   }

   private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() 
   {
      // Called when the action mode is created; startActionMode() was called
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) 
      {
          // Inflate a menu resource providing context menu items
         MenuInflater inflater = mode.getMenuInflater();
         inflater.inflate(R.menu.menu_context, menu);
         return true;
      }

      // Called each time the action mode is shown. Always called after onCreateActionMode, but
      // may be called multiple times if the mode is invalidated.
      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) 
      {
         return false; // Return false if nothing is done
      }

      // Called when the user selects a contextual menu item
      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
      {
         switch (item.getItemId()) 
         {
             case R.id.action_delete:
//                    shareCurrentItem();
//                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//                long i = info.id;
                assert oDbAdapter != null;
//                oDbAdapter.deleteBook(sel_id);
//                setupRecyclerView((RecyclerView) recyclerView);
//                oSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(oDbAdapter.getBooks(iOrderID)); 
//                recyclerView.swapAdapter(oSimpleItemRecyclerViewAdapter, false);
//                oSimpleItemRecyclerViewAdapter.notifyItemRemoved(iClickedItemNdx);
//                oSimpleItemRecyclerViewAdapter.notifyItemRemoved(2);
                oBooksAdapter.removeAt(iClickedItemNdx);

                mode.finish(); // Action picked, so close the CAB
                return true;
             default:
                return false;
         }
      }

      // Called when the user exits the action mode
      @Override
      public void onDestroyActionMode(ActionMode mode) 
      {
         mActionMode = null;
      }
   };
   
   private class OrderItem 
   {
      public int iID;
      public String sTitle;
      
      public OrderItem(int iID, String sTitle)
      {
         this.iID = iID;
         this.sTitle = sTitle;
      }
      
   }
}
