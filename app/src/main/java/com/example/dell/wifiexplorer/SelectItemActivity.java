package com.example.dell.wifiexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.WalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentState;

/**
 * This activity is shown first, and displays the "buy with google" button.
 *
 */
public class SelectItemActivity extends ActionBarActivity {

    private WalletFragment mWalletFragment;
    private ImpulseItem mItem;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item);

        // locate the fragment that was defined in XML
        mWalletFragment = (WalletFragment) getFragmentManager().findFragmentById(R.id.wallet_buy_fragment);

        // for debugging purposes only
        mWalletFragment.setOnStateChangedListener(new WalletFragment.OnStateChangedListener() {
            @Override
            public void onStateChanged(WalletFragment walletFragment, int oldState, int newState, Bundle bundle) {
                Log.i("SHAN", "wallet fragment state changed: " + oldState + " -> " + newState);
            }
        });

        ImageButton randomButton = (ImageButton) findViewById(R.id.button_random);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem = ImpulseStore.pickRandomItem();
                Log.i("SHAN", "item to buy: " + mItem);

                TextView itemToBuyLabel = (TextView) findViewById(R.id.item_to_buy);
                itemToBuyLabel.setText(mItem.description + " - " + mItem.displayPrice());

                MaskedWalletRequest maskedWalletRequest = WalletSupport.createMaskedWalletRequest(mItem);
                Log.i("SHAN", "created wallet request " + maskedWalletRequest);

                WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                        .setMaskedWalletRequest(maskedWalletRequest)
                        .setMaskedWalletRequestCode(WalletSupport.REQUEST_CODE_MASKED_WALLET);
                WalletFragmentInitParams params = startParamsBuilder.build();

                if (mWalletFragment.getState() == WalletFragmentState.UNINITIALIZED)
                    mWalletFragment.initialize(params);
                else
                    mWalletFragment.updateMaskedWalletRequest(maskedWalletRequest);

                Log.i("SHAN", "initialized wallet fragment with params " + params);
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("SHAN", "onActivityResult request=" + requestCode + " result=" + resultCode);

        // retrieve the error code, if available
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        }

        switch (requestCode) {
            case WalletSupport.REQUEST_CODE_MASKED_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("SHAN", "activity result OK");

                        MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        Log.i("SHAN", "got wallet: " + maskedWallet);

                        launchConfirmationPage(maskedWallet);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("SHAN", "activity result cancelled");
                        break;
                    default:
                        Log.e("SHAN", "activity result code not handled!");
                        WalletSupport.handleError(this, errorCode);
                        break;
                }
                break;
            case WalletConstants.RESULT_ERROR:
                Log.e("SHAN", "activity result error");
                WalletSupport.handleError(this, errorCode);
                break;
            default:
                Log.w("SHAN", "activity result default");
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * @param maskedWallet
     */
    private void launchConfirmationPage(MaskedWallet maskedWallet) {
        Intent intent = new Intent(this, PlaceOrderActivity.class);
        intent.putExtra(ImpulseStore.EXTRA_ITEM, mItem);
        intent.putExtra(WalletConstants.EXTRA_MASKED_WALLET, maskedWallet);
        startActivity(intent);
        Log.i("SHAN", "launched confirmation page"+maskedWallet);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
