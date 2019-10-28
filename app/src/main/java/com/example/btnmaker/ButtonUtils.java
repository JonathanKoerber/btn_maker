package com.example.btnmaker;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;

//TODO add the DimenResource to the buttonShadow method

public class ButtonUtils {
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

