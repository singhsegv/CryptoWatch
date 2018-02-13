package io.github.rajdeep1008.cryptowatch.extras;

import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import io.github.rajdeep1008.cryptowatch.R;

/**
 * Created by rajdeep1008 on 13/02/18.
 */

public class ExtraPreferenceFragmentCompat extends PreferenceDialogFragmentCompat {

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        TextView github = view.findViewById(R.id.github_profile);
        if (github != null)
            github.setMovementMethod(LinkMovementMethod.getInstance());

        TextView twitter = view.findViewById(R.id.twitter_profile);
        if (twitter != null)
            twitter.setMovementMethod(LinkMovementMethod.getInstance());

        TextView repoTv = view.findViewById(R.id.code_tv);
        if (repoTv != null)
            repoTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {

    }

    public static ExtraPreferenceFragmentCompat newInstance(String key) {
        final ExtraPreferenceFragmentCompat fragment = new ExtraPreferenceFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }
}
