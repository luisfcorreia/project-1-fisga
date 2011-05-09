package net.loide.games.fisga;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

	public static void setNumPlayers(Context ctx, int num) {
		SharedPreferences definicoes = ctx.getSharedPreferences("androidpt", 0);
		Editor edit = definicoes.edit();
		edit.putInt("numPlayers", num);
		edit.commit();
	}

}
