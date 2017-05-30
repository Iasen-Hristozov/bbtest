package com.discworld.booksbag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.FileUtils;
import com.discworld.booksbag.dto.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

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
   public final static int SHOW_EDIT_BOOK = 101;
   
   private static final String XPR_DIR = "BooksBag",
                               DB_PATH = "//data//com.discworld.booksbag//databases//";
   
   private DBAdapter oDbAdapter = null;
   
   private View recyclerView;
   
   private ActionMode mActionMode;
   
   private long sel_id;

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
      
      recyclerView = findViewById(R.id.book_list);
      assert recyclerView != null;
//      setupRecyclerView((RecyclerView) recyclerView);
      registerForContextMenu(recyclerView);

      if (findViewById(R.id.book_detail_container) != null)
      {
         // The detail container view will be present only in the
         // large-screen layouts (res/values-w900dp).
         // If this view is present, then the
         // activity should be in two-pane mode.
         mTwoPane = true;
      }
   }
   
   

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch(item.getItemId())
      {
         case R.id.action_settings:
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
            
         default:
            return true;
      }
   }

   private void setupRecyclerView(@NonNull RecyclerView recyclerView)
   {
//      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
//      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.BOOKS));
      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(oDbAdapter.getBooks(DBAdapter.ORD_TTL)));
   }

   public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
   {

//      private final List<DummyContent.DummyItem> mValues;
//      private final List<Book> mValues;
      private final List<Result> mValues;

//      public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items)
//      public SimpleItemRecyclerViewAdapter(List<Book> items)
      public SimpleItemRecyclerViewAdapter(List<Result> items)
      {
         mValues = items;
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
         holder.mItem = mValues.get(position);
         holder.mIdView.setText(String.valueOf(mValues.get(position)._id));
         holder.mContentView.setText(mValues.get(position).content);

         holder.mView.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               if (mTwoPane)
               {
                  Bundle arguments = new Bundle();
                  arguments.putLong(BookDetailFragment.ARG_ITEM_ID, holder.mItem._id);
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
                  intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem._id);

//                  context.startActivity  (intent);
                  startActivityForResult(intent, 0);
               }
            }
         });
         
         holder.mView.setOnLongClickListener(new View.OnLongClickListener() 
         {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) 
            {
               if (mActionMode != null) 
               {
                  return false;
               }

               // Start the CAB using the ActionMode.Callback defined above
               sel_id = holder.mItem._id;
               BookListActivity.this.startSupportActionMode(mActionModeCallback);
               
//               mActionMode = ((ActionBarActivity)view.getContext()).startSupportActionMode(mActionModeCallback);
//               mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callb
               view.setSelected(true);
               return true;
            }
         });
      }

      @Override
      public int getItemCount()
      {
         return mValues.size();
      }

      public class ViewHolder extends RecyclerView.ViewHolder
      {
         public final View mView;
         public final TextView mIdView;
         public final TextView mContentView;
         public Result mItem;

         public ViewHolder(View view)
         {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            
//            view.setOnLongClickListener(new View.OnLongClickListener() 
//            {
//               @Override
//               public boolean onLongClick(View v) 
//               {
////                   Toast.makeText(v.getContext(), "Position is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                   mActionMode = ((AppCompatActivity)v.getContext()).startSupportActionMode(mActionModeCallback);
//                   v.setSelected(true);
//                   return true;
//
////                   return false;
//               }
//           });
         }

         @Override
         public String toString()
         {
            return super.toString() + " '" + mContentView.getText() + "'";
         }
      }
   }

   @Override
   protected void onPause()
   {
      oDbAdapter.close();

      super.onPause();
   }

   @Override
   protected void onResume()
   {
      super.onResume();

      oDbAdapter.open();
      if(bUpdate)
         setupRecyclerView((RecyclerView) recyclerView);

//      oBook = oDbAdapter.getBook(oBook.iID);
   }

   private boolean bExportDb()
   {
      try
      {
//         File flRoot = android.os.Environment.getExternalStorageDirectory();

         // File flXprDir = new File (flRoot.getAbsolutePath() + "/" + XPR_DIR);
         File flXprDir = new File(Environment.getExternalStorageDirectory() + "/" + XPR_DIR);

         File flData = Environment.getDataDirectory();

         if(flXprDir.canWrite())
         {
            File flCurrentDB = new File(flData, DB_PATH + DBAdapter.DATABASE_NAME);
            File flBackupDB = new File(flXprDir, DBAdapter.DATABASE_NAME);

            if(flCurrentDB.exists())
            {
               FileChannel src = new FileInputStream(flCurrentDB).getChannel();
               FileChannel dst = new FileOutputStream(flBackupDB).getChannel();

               dst.transferFrom(src, 0, src.size());
               return true;
            }
         }
      } catch(Exception e)
      {
         System.out.println(e.getMessage());
         Log.e("BookListActivity", e.getMessage());
         return false;
      }
      // finally
      // {
      // try
      // {
      // src.close();
      // dst.close();
      // } catch(IOException e)
      // {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }
      // }
      return true;
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

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);
      
      if(resultCode == RESULT_OK)
         bUpdate = true;
      else
         bUpdate = false;
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
                oDbAdapter.deleteBook(sel_id);
                setupRecyclerView((RecyclerView) recyclerView);
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
}
