package mad9132.rgb_a;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import model.RGBAModel;

/**
 * The Controller for RGBAModel.
 *
 * As the Controller:
 *   a) event handler for the View
 *   b) observer of the Model (RGBAModel)
 *
 * Features the Update / React Strategy.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1.0
 */
public class MainActivity extends Activity implements Observer, SeekBar.OnSeekBarChangeListener {
    // CLASS VARIABLES
    private static final String ABOUT_DIALOG_TAG = "About";
    private static final String LOG_TAG          = "RGBA";

    // INSTANCE VARIABLES
    // Pro-tip: different naming style; the 'm' means 'member'
    private AboutDialogFragment mAboutDialog;
    private SeekBar mBlueSB;
    private TextView mColorSwatch;
    private SeekBar mGreenSB;
    private RGBAModel mModel;
    private SeekBar mRedSB;
    private SeekBar mAlphaSB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a new AboutDialogFragment()
        // but do not show it (yet)
        mAboutDialog = new AboutDialogFragment();

        // instantiate a new RGB-A model
        // and observer it
        mModel = new RGBAModel( RGBAModel.MAX_RGB, RGBAModel.MAX_RGB, RGBAModel.MAX_RGB, RGBAModel.MAX_ALPHA );
        mModel.addObserver( this );

        // reference each View component
        mRedSB = (SeekBar) findViewById( R.id.sbRed );
        mGreenSB = (SeekBar) findViewById( R.id.sbGreen );
        mBlueSB = (SeekBar) findViewById( R.id.sbBlue );
        mAlphaSB = (SeekBar) findViewById( R.id.sbAlpha );
        mColorSwatch = (TextView) findViewById( R.id.imgColorSwatch );

        // Set the domains
        mRedSB.setMax(RGBAModel.MAX_RGB);
        mGreenSB.setMax(RGBAModel.MAX_RGB);
        mBlueSB.setMax(RGBAModel.MAX_RGB);
        mAlphaSB.setMax(RGBAModel.MAX_ALPHA);

        // Set Seekbar Listeners
        mRedSB.setOnSeekBarChangeListener( this );
        mGreenSB.setOnSeekBarChangeListener( this );
        mBlueSB.setOnSeekBarChangeListener( this );
        mAlphaSB.setOnSeekBarChangeListener( this );


        // initialize the View to the values of the Model
        this.updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            mAboutDialog.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            return true;
        }

        // Handle presses on the action bar items
        switch ( id ) {
            case R.id.action_black:
                mModel.asBlack();
                return true;
            case R.id.action_blue:
                mModel.asBlue();
                return true;
            case R.id.action_cyan:
                mModel.asCyan();
                return true;
            case R.id.action_green:
                mModel.asGreen();
                return true;
            case R.id.action_magenta:
                mModel.asMagenta();
                return true;
            case R.id.action_red:
                mModel.asRed();
                return true;
            case R.id.action_white:
                mModel.asWhite();
                return true;
            case R.id.action_yellow:
                mModel.asYellow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Event handler for the <SeekBar>s: red, green, blue, and alpha.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // Did the user cause this event?
        // YES > continue
        // NO  > leave this method
        if ( fromUser == false ) {
            return;
        }

        // GET the SeekBar's progress, and SET the model to it's new value
        int id = seekBar.getId();

        switch( id ) {
            case R.id.sbBlue:
                mModel.setBlue( seekBar.getProgress() );
                break;
            case R.id.sbGreen:
                mModel.setGreen( seekBar.getProgress() );
                break;
            case R.id.sbRed:
                mModel.setRed( seekBar.getProgress() );
                break;
            case R.id.sbAlpha:
                mModel.setAlpha( seekBar.getProgress() );
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // No-Operation
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // No-Operation
    }

    // The Model has changed state!
    // Refresh the View to display the current values of the Model.
    @Override
    public void update(Observable observable, Object data) {
        this.updateView();
    }

    // GET the blue value from the model, and
    // SET the blue <SeekBar> to the model's blue value
    private void updateBlueSB() {
        mBlueSB.setProgress( mModel.getBlue() );
    }

    // GET the green value from the model, and
    // SET the green <SeekBar> to the model's green value
    private void updateGreenSB() {
        mGreenSB.setProgress( mModel.getGreen() );
    }

    // GET the red value from the model, and
    // SET the red <SeekBar> to the model's red value
    private void updateRedSB() {
        mRedSB.setProgress( mModel.getRed() );
    }

    // GET the alpha value from the model, and
    // SET the alpha <SeekBar> to the model's alpha value
    private void updateAlphaSB() {
        mAlphaSB.setProgress( mModel.getAlpha() );
    }

    private void updateColorSwatch() {
        //GET the model's r,g,b,a values, and SET the background colour of the swatch <TextView>
        mColorSwatch.setBackgroundColor(Color.argb(mModel.getAlpha()
                , mModel.getRed()
                , mModel.getGreen()
                , mModel.getBlue()));
    }

    private void updateColorText() {
        int r = 255;
        int g = 255;
        int b = 255;
        int backgroundColorValue = mModel.getRed() + mModel.getGreen() + mModel.getBlue();

        // Got this answer from:
        // https://stackoverflow.com/questions/8741479/automatically-determine-optimal-fontcolor-by-backgroundcolor
        if (backgroundColorValue > 383) {
            r = 0;
            g = 0;
            b = 0;
        }

        mColorSwatch.setTextColor(Color.rgb(r, g, b));
        mColorSwatch.setText("RGBA(" + mModel.getRed() + ", " + mModel.getGreen() + ", " + mModel.getBlue() + ", " + mModel.getAlpha() + ")");
    }


    // synchronize each View component with the Model
    public void updateView() {
        this.updateColorSwatch();
        this.updateRedSB();
        this.updateGreenSB();
        this.updateBlueSB();
        this.updateAlphaSB();
        this.updateColorText();
    }
}   // end of class
