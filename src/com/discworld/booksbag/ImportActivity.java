package com.discworld.booksbag;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ImportActivity extends AppCompatActivity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
//      setContentView(R.layout.activity_main);

      Intent intent = new Intent().setType("*/*")
                                  .setAction(Intent.ACTION_GET_CONTENT);

      startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == 123 && resultCode == RESULT_OK)
      {
         Uri selectedfile = data.getData(); // The uri with the location of the
                                            // file
      }
   }

}
