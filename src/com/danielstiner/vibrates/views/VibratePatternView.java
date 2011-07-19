package com.danielstiner.vibrates.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VibratePatternView extends View {

	private List<Long> _pattern;
	private Long _pattern_length;
	
	Paint pattern_border_paint;
	Paint pattern_vibrate_paint;
	Paint pattern_wait_paint;
	
	float pattern_drawlength_multiplier = (float) 0.02;

	public VibratePatternView(Context context) {
		super(context);
		init();
	}

	public VibratePatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VibratePatternView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		// Setup our paints
		pattern_border_paint = new Paint();
		pattern_vibrate_paint = new Paint();
		pattern_wait_paint = new Paint();
		
		pattern_border_paint.setColor(Color.GRAY);
		pattern_border_paint.setStrokeWidth((float) 1.1);
		pattern_border_paint.setStyle(Paint.Style.STROKE);
		pattern_vibrate_paint.setColor(Color.LTGRAY);
		pattern_wait_paint.setColor(Color.DKGRAY);
	}
	
	public void setPattern(String new_pattern) {
		// Null check
		if(new_pattern == null) return;
		// Do some fancy parsing
		String[] string_pattern = new_pattern.split("[,.]", 100);
		_pattern_length = new Long(0);
		_pattern = new ArrayList<Long>(string_pattern.length);
		for(int i=0; i<string_pattern.length; i++) {
			Long part = Long.parseLong(string_pattern[i]);
			_pattern.set(i, part);
			_pattern_length += part;
		}
		invalidate();
	}
	public void setPattern(List<Long> new_pattern) {
		// Null check
		if(new_pattern == null) return;
		
		_pattern_length = new Long(0);
		_pattern = new_pattern;
		for(Long l : _pattern) {
			_pattern_length += l;
		}
		invalidate();
	}
	public void setPattern(long[] new_pattern) {
		// Null check
		if(new_pattern == null) return;
		
		_pattern_length = new Long(0);
		// copy pattern over
		for(int i=0; i<new_pattern.length; i++) {
			_pattern.add(new_pattern[i]);
			_pattern_length += new_pattern[i];
		}
		invalidate();
	}
	
	public List<Long> getPattern() {
		return _pattern;
	}
	
	@Override
	public String toString() {
		return _pattern.toString();
	}
	
	// TODO Set paint
	

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//null check
		if(canvas == null) return;
		
		float w = this.getWidth();
		float h = this.getHeight();
		
		// Draw border
		canvas.drawRect(0, 0, w, h, pattern_border_paint);
		
		// another null check
		if(_pattern == null) return;
		
		// Boolean to alternate between wait and vibrate times in the pattern
		// First number in pattern is a wait time
		boolean is_wait_period = true;
		
		float last_draw_pos = 0;
		
		
		float draw_width = 0;
		Paint draw_paint = null;
		
		for(Long l : _pattern) {
			
			// Draw part of the pattern
			draw_width = ((float) l) * (pattern_drawlength_multiplier);
			
			if(is_wait_period == true) {
				// Draw wait time box
				draw_paint = pattern_wait_paint;
			} else {
				// Draw vibrate time box
				draw_paint = pattern_vibrate_paint;
			}
			
			// Actually draw correctly colored box
			canvas.drawRect(last_draw_pos, 0, last_draw_pos+draw_width, h, draw_paint);
			
			// Increment where we last drew on the screen
			last_draw_pos += last_draw_pos+draw_width;
			
			// Check we are still inside the width of this control
			if(last_draw_pos > w) break;
		}
		
	}
	
	
	/**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        return specSize;
        /*
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;*/
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        
        return specSize;
/*
        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;*/
    }
}
