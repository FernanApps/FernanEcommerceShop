package pe.fernanapps.shop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundFrameLayout extends FrameLayout {
    private Path path;
    private final float cornerRadius = 35f;
    private final double percentage = 0.925;

    public RoundFrameLayout(Context context) {
        super(context);
        init();
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = canvas.save();

        path.reset();
        //path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
        path = get(cornerRadius, getWidth(), getHeight());
        canvas.clipPath(path);
        super.dispatchDraw(canvas);

        canvas.restoreToCount(count);
    }

    private Path get(float radius, int width, int height){
        Path path = new Path();

        path.moveTo(radius, 0f);
        path.lineTo(width - radius, 0f);
        path.arcTo(new RectF(width - 2 * radius, 0f, width, 2 * radius), -90f, 90f, false);

        float height90 = (float) (height * percentage);
        path.lineTo(width, height90 - radius);

        path.arcTo(new RectF(width - 2 * radius, height90 - 2 * radius, width, height90), 0f, 90f, false);

        path.lineTo(radius, height);
        path.arcTo(new RectF(0f, height - 2 * radius, 2 * radius, height), 90f, 90f, false);
        path.lineTo(0f, radius);
        path.arcTo(new RectF(0f, 0f, 2 * radius, 2 * radius), 180f, 90f, false);
        path.close();

        return path;
    }
}
