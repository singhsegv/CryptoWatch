package io.github.rajdeep1008.cryptowatch.extras;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import io.github.rajdeep1008.cryptowatch.R;

/**
 * Created by rajdeep1008 on 13/02/18.
 */

public class ExtraPreference extends DialogPreference {

    private int layoutId = 0;

    public ExtraPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ExtraPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public ExtraPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
        String key = attrs.getAttributeValue(1);
        if (key.equals("privacy_policy")) {
            layoutId = R.layout.pref_privacy_policy;
        } else if (key.equals("eula")) {
            layoutId = R.layout.pref_eula;
        } else if (key.equals("about")) {
            layoutId = R.layout.pref_about;
        }
    }

    public ExtraPreference(Context context) {
        this(context, null);
    }

    @Override
    public int getDialogLayoutResource() {
        return layoutId;
    }
}
