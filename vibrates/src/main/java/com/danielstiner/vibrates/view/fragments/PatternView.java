package com.danielstiner.vibrates.view.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PatternView extends View {

	private List<Long> _pattern;
	private Long _pattern_length;

	private Paint pattern_border_paint;
	private Paint pattern_vibrate_paint;
	private Paint pattern_wait_paint;
	private Paint oversized_vibrate_paint;

	// private static final float DEFAULT_PATTERN_STRIPE_SIZE = (float) 0.12;
	private static final float DEFAULT_PATTERN_STRIPE_SIZE = (float) 0.15;

	public PatternView(Context context) {
		super(context);
		init();
	}

	public PatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PatternView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// Setup our paints
		pattern_border_paint = new Paint();
		pattern_vibrate_paint = new Paint();
		pattern_wait_paint = new Paint();
		oversized_vibrate_paint = new Paint();

		pattern_border_paint.setColor(Color.DKGRAY);
		pattern_border_paint.setStrokeWidth((float) 1.1);
		pattern_border_paint.setStyle(Paint.Style.STROKE);
		pattern_border_paint.setAlpha(50);

		pattern_vibrate_paint.setColor(Color.LTGRAY);
		oversized_vibrate_paint.setColor(Color.WHITE);

		pattern_wait_paint.setColor(Color.BLACK);
		pattern_wait_paint.setAlpha(0);
	}

	public void setPattern(List<Long> new_pattern) {
		// Null check
		if (new_pattern == null)
			return;

		_pattern_length = new Long(0);
		_pattern = new_pattern;
		for (Long l : _pattern) {
			_pattern_length += l;
		}
		invalidate();
	}

	public void setPattern(long[] new_pattern) {
		// Null check
		if (new_pattern == null)
			return;

		ArrayList<Long> tmp = new ArrayList<Long>(new_pattern.length);
		// copy pattern over
		for (int i = 0; i < new_pattern.length; i++) {
			tmp.add(new_pattern[i]);
		}
		// pass off work
		setPattern(tmp);
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

		// null check
		if (canvas == null)
			return;

		float w = this.getWidth();
		float h = this.getHeight();

		// Draw border
		canvas.drawRect(0, 0, w, h, pattern_border_paint);

		// another null check
		if (_pattern == null)
			return;

		if (isPatternOversized()) {
			// Compute needed stripe size to get everything to fit
			float size = (float) w / (float) sumPattern(_pattern);

			// oversized_stripe_paint
			drawStripes(canvas, size, oversized_vibrate_paint);
		} else {
			drawStripes(canvas, DEFAULT_PATTERN_STRIPE_SIZE,
					pattern_vibrate_paint);
		}

	}

	private static long sumPattern(List<Long> pattern) {
		long sum = 0;
		if (pattern == null)
			return sum;

		for (Long l : pattern)
			sum += l.longValue();

		return sum;
	}

	// private boolean isPatternOversized(long length_ms, float stripe_size, int
	// width) {
	private boolean isPatternOversized() {
		if (sumPattern(_pattern) * DEFAULT_PATTERN_STRIPE_SIZE > this
				.getWidth()) {
			return true;
		}
		return false;
	}

	private void drawStripes(Canvas canvas, float stripe_size,
			Paint vibrate_paint) {
		float w = this.getWidth();
		float h = this.getHeight();

		// Boolean to alternate between wait and vibrate times in the pattern
		// First number in pattern is a wait time
		boolean is_wait_period = true;

		float last_draw_pos = 0;

		float draw_width = 0;
		Paint draw_paint = null;

		for (Long l : _pattern) {

			// Draw part of the pattern
			draw_width = ((float) l) * (stripe_size);

			if (is_wait_period == true) {
				// Draw wait time box
				draw_paint = pattern_wait_paint;
				is_wait_period = false;
			} else {
				// Draw vibrate time box
				draw_paint = vibrate_paint;
				is_wait_period = true;
			}

			// Actually draw correctly colored box
			canvas.drawRect(last_draw_pos, 0,
					Math.min(last_draw_pos + draw_width, w), h, draw_paint);

			// Increment where we last drew on the screen
			last_draw_pos += draw_width;

			// Check we are still inside the width of this control
			if (last_draw_pos > w)
				break;
		}
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		// int result = 0;
		// int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		return specSize;
		/*
		 * if (specMode == MeasureSpec.EXACTLY) { // We were told how big to be
		 * result = specSize; } else { // Measure the text result = (int)
		 * mTextPaint.measureText(mText) + getPaddingLeft() + getPaddingRight();
		 * if (specMode == MeasureSpec.AT_MOST) { // Respect AT_MOST value if
		 * that was what is called for by measureSpec result = Math.min(result,
		 * specSize); } }
		 * 
		 * return result;
		 */
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		// int result = 0;
		// int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		return specSize;
		/*
		 * mAscent = (int) mTextPaint.ascent(); if (specMode ==
		 * MeasureSpec.EXACTLY) { // We were told how big to be result =
		 * specSize; } else { // Measure the text (beware: ascent is a negative
		 * number) result = (int) (-mAscent + mTextPaint.descent()) +
		 * getPaddingTop() + getPaddingBottom(); if (specMode ==
		 * MeasureSpec.AT_MOST) { // Respect AT_MOST value if that was what is
		 * called for by measureSpec result = Math.min(result, specSize); } }
		 * return result;
		 */
	}
}
