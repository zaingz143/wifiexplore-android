package com.example.dell.wifiexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.WalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;

import java.text.NumberFormat;

/**
 * This activity is shown after the user has selected a payment instrument. It is the "confirmation page".
 * This activity is launched from MainActivity.launchConfirmationPage().
 *
 * Created by rwaldura on 12/30/14.
 */
public class PlaceOrderActivity extends ActionBarActivity {

    private WalletFragment mWalletFragment;
    private ImpulseItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm);

        MaskedWallet maskedWallet = getIntent().getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
        Log.i(ImpulseStore.TAG, "got wallet " + maskedWallet);

        mItem = (ImpulseItem) getIntent().getSerializableExtra(ImpulseStore.EXTRA_ITEM);
        Log.i(ImpulseStore.TAG, "item to buyZ: " + mItem);

        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWallet(maskedWallet)
                .setMaskedWalletRequestCode(WalletSupport.REQUEST_CODE_CHANGE_MASKED_WALLET);
        WalletFragmentInitParams params = startParamsBuilder.build();

        populateOrderDetails();

        // locate and load the fragment that was defined in XML
        mWalletFragment = (WalletFragment) getFragmentManager().findFragmentById(R.id.wallet_confirmation_fragment);
        mWalletFragment.initialize(params);
        Log.i(ImpulseStore.TAG, "initialized wallet fragment with params " + params);
    }

    private void populateOrderDetails() {
        TextView itemDescription = (TextView) findViewById(R.id.order_item_descr);
        itemDescription.setText(mItem.description);

        TextView itemPrice = (TextView) findViewById(R.id.order_item_price);
        itemPrice.setText(mItem.displayPrice());

        TextView shipping = (TextView) findViewById(R.id.order_shipping);
        shipping.setText(NumberFormat.getCurrencyInstance().format(ImpulseStore.getShipping(mItem) / 100.0));

        TextView tax = (TextView) findViewById(R.id.order_tax);
        tax.setText(NumberFormat.getCurrencyInstance().format(ImpulseStore.getTax(mItem) / 100.0));

        TextView total = (TextView) findViewById(R.id.order_total_price);
        total.setText(NumberFormat.getCurrencyInstance().format(ImpulseStore.computeTotalPrice(mItem) / 100.0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w(ImpulseStore.TAG, "onActivityResult request=" + requestCode + " result=" + resultCode);

        FullWalletConfirmationButtonFragment fragment = (FullWalletConfirmationButtonFragment) getSupportFragmentManager().findFragmentById(R.id.full_wallet_confirmation_button_fragment);

        // we have to explicitly forward the item(s) to buy
        data.putExtra(ImpulseStore.EXTRA_ITEM, mItem);

        switch (requestCode) {
            case WalletSupport.REQUEST_CODE_CHANGE_MASKED_WALLET:
                if (resultCode == Activity.RESULT_OK && data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {
                    MaskedWallet mw = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                    fragment.updateMaskedWallet(mw);
                }
                // you may also want to use the new masked wallet data here, say to recalculate
                // shipping or taxes if shipping address changed
                break;
            case WalletConstants.RESULT_ERROR:
                Log.e(ImpulseStore.TAG, "activity result error");
                int errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, 0);
                WalletSupport.handleError(this, errorCode);
                break;
            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET:
            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_ERR:
//            case PromoAddressLookupFragment.REQUEST_CODE_RESOLVE_ADDRESS_LOOKUP:
//            case PromoAddressLookupFragment.REQUEST_CODE_RESOLVE_ERR:
//            case LoginFragment.REQUEST_CODE_RESOLVE_ERR:
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
//            case REQUEST_USER_LOGIN:
//                if (resultCode == RESULT_OK) {
//                    ActivityCompat.invalidateOptionsMenu(this);
//                }
//                break;
            default:
                Log.w(ImpulseStore.TAG, "fell through: activity result request code = " + requestCode);
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
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
