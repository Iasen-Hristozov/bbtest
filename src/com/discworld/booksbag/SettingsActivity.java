package com.discworld.booksbag;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity
{
   @Override
   protected void onCreate(final Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
   }

   public static class MyPreferenceFragment extends PreferenceFragmentCompat
   {
      @Override
      public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
      {
//         super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.preference_screen);
      }
   }
}
