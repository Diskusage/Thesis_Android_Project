package com.app.greenpass.activities.locale

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    public override fun attachBaseContext(newBase: Context) {
    // get chosen language from shared preference
        val pref =  newBase.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val localeToSwitchTo : String? = pref.getString("lang_code", "en")
        val localeUpdatedContext: ContextWrapper = ChangeLocaleTool.wrap(newBase, localeToSwitchTo!!)
        super.attachBaseContext(localeUpdatedContext)
    }

}