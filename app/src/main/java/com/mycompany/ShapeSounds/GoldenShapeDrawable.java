package com.mycompany.ShapeSounds;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 class the extends ShapeDrawable and modified to be specifiec to goldenShapes
 */
public class GoldenShapeDrawable extends ShapeDrawable {
    GoldenShape gs;
    private static final RectF ZERO_BOUNDS_RECT = new RectF();
    private RectF mBounds = ZERO_BOUNDS_RECT;
    public <T extends Shape & GoldenShape> GoldenShapeDrawable(T shape) {
        super(shape);
        gs = shape;
    }

    public Formula[] GetGoldenPoints() {
        return gs.GetGoldenPoints();
    }

    public GoldenShape GetGoldenShape() {
        return gs;
    }

    public void setFBounds(float left, float top, float right, float bottom) {
        RectF oldBounds = mBounds;

        if (oldBounds == ZERO_BOUNDS_RECT) {
            oldBounds = mBounds = new RectF();
        }

        if (oldBounds.left != left || oldBounds.top != top ||
                oldBounds.right != right || oldBounds.bottom != bottom) {
            if (!oldBounds.isEmpty()) {
                // first invalidate the previous bounds
                invalidateSelf();
            }
            mBounds.set(left, top, right, bottom);
            onBoundsChange(mBounds);
        }
    }

    public final RectF getFBounds() {
        if (mBounds == ZERO_BOUNDS_RECT) {
            mBounds = new RectF();
        }

        return mBounds;
    }


    protected void onBoundsChange(RectF bounds) {
        super.onBoundsChange(new Rect((int)bounds.left,(int)bounds.top,(int)bounds.right,(int)bounds.bottom));
        updateShape();
    }


    private void updateShape() {
        if (getShape() != null) {
            final RectF r = getFBounds();
            final float w = r.width();
            final float h = r.height();

            getShape().resize(w, h);
            if (getShaderFactory() != null) {
                getPaint().setShader(getShaderFactory().resize((int)w, (int)h));
            }
        }
        invalidateSelf();
    }

    private static int modulateAlpha(int paintAlpha, int alpha) {
        int scale = alpha + (alpha >>> 7); // convert to 0..256
        return paintAlpha * scale >>> 8;
    }
    @Override
    public void draw(Canvas canvas) {
        final RectF r = getFBounds();
        //final ShapeState state = getConstantState();
        final Paint paint = getPaint();

        final int prevAlpha = paint.getAlpha();
        paint.setAlpha(modulateAlpha(prevAlpha, getAlpha()));


        // only draw shape if it may affect output
        if (paint.getAlpha() != 0 || paint.getXfermode() != null ) {


            if (getShape() != null) {
                // need the save both for the translate, and for the (unknown)
                // Shape
                final int count = canvas.save();
                canvas.translate(r.left, r.top);
                onDraw(getShape(), canvas, paint);
                canvas.restoreToCount(count);
            } else {
                canvas.drawRect(r, paint);
            }


        }

        // restore
        paint.setAlpha(prevAlpha);
    }


}










