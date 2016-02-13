package net.inab_j.uecapp.model.System;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import net.inab_j.uecapp.R;

/**
  * 「このアプリについて」、「使用ライセンス」の情報の表示を行う。
 */

public class InfomationPreference extends DialogPreference {
    private final int ABOUT_APP = 0;
    private final int LICENSES = 1;

    public InfomationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNegativeButtonText(null);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.InformationPreference
        );

        // set message
        int type = typedArray.getInt(R.styleable.InformationPreference_informationType, 0);
        switch (type) {
            case ABOUT_APP:
                setDialogMessage(R.string.pref_about_message);
                break;

            case LICENSES:
                setDialogMessage(R.string.pref_license_message);
                break;
        }

    }
}
