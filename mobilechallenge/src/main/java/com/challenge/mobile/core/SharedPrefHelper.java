package com.challenge.mobile.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhangziy on 2017-07-31.
 */

public class SharedPrefHelper {


	private static final String NUDE_EXCLUDED = "nude_excluded";
	private SharedPreferences sharedPreferences;


	public SharedPrefHelper(Context context) {
		this.sharedPreferences = context.getSharedPreferences("core", Context.MODE_PRIVATE);
	}

	public boolean isNudeExcluded() {
		return sharedPreferences.getBoolean(NUDE_EXCLUDED, false);
	}

	public void setNudeExcluded(boolean excluded) {
		sharedPreferences.edit().putBoolean(NUDE_EXCLUDED, excluded).apply();
	}

}
