package com.example.btnmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;
// form stack overflow "https://stackoverflow.com/questions/16208365/how-to-create-a-circular-imageview-in-android"


public class RoundImageView extends  ImageView {
    public RoundImageView(Context context){
        super(context);
    }
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Drawable drawable = getDrawable();
        if(drawable ==null){
            return;
        }
        if(getWidth() == 0 || getHeight() == 0){
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        Bitmap roundedBitmap = getCroppedBitmap(bitmap, h);
        canvas.drawBitmap(roundedBitmap, (w-h)/2, 0, null);

    }
public static Bitmap getCroppedBitmap(Bitmap bmp, int radius){
        Bitmap sbmp;

        if(bmp.getWidth() != radius || bmp.getHeight() != radius){
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest/radius;
            sbmp = Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth()/factor), (int)(bmp.getHeight()/factor), false);
        }else{
            sbmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
    //todo painting a circle boarder around the image
        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius/ 2+0.7f, radius/2+0.7f, radius/2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
}
//todo refactor: instead of passin a view pass a the roundedRounded bitmap
    public static Drawable buttonShadow(View view, @ColorRes int backgroundColor,
                                        @DimenRes int cornerRadius,
                                        @ColorRes int shadowColor,
                                        @DimenRes int elevation,
                                        int shadowGravity){
        float cornerRadiusValue =  view.getContext().getResources().getDimension(cornerRadius);
        int elevationValue = (int) view.getContext().getResources().getDimension(elevation);
        int shadowColorValue = ContextCompat.getColor(view.getContext(), shadowColor);
        int backgroundColorValue = ContextCompat.getColor(view.getContext(), backgroundColor);
        float[] outerRadius = {
                cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue
        };

        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setShadowLayer(cornerRadiusValue, 0,0,0);

        Rect shapeDrawablePadding = new Rect();
        shapeDrawablePadding.left = elevationValue;
        shapeDrawablePadding.right = elevationValue;

        int DY;

        switch (shadowGravity){
            case Gravity.CENTER:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue;
                DY = 0;
                break;
            case Gravity.TOP:
                shapeDrawablePadding.top = elevationValue*2;
                shapeDrawablePadding.bottom = elevationValue;
                DY = 0;
                break;
            default:
            case Gravity.BOTTOM:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue*2;
                DY = 0;
                break;

        }
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);

        shapeDrawable.getPaint().setColor(backgroundColorValue);
        shapeDrawable.getPaint().setShadowLayer(cornerRadius/3, 0, DY, shadowColorValue);

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, elevationValue, elevationValue*2, elevationValue, elevationValue*2);

        return drawable;
    }

}
