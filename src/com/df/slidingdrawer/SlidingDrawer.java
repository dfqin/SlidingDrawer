package com.df.slidingdrawer;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SlidingDrawer extends FrameLayout {

	private LinearLayout drawer;
	private View  content;
	DrawerState state; 
	
	int MAX_MOVE_VALUE; //启动推拉动画的阀值
	int DRAG_BAR_VALUE; //拖动条高度 
	int MAX_ANIMATION_DURATION = 600;
	int FAST_ANIMATION_DURATION = 100;
	
	int ANIMATION_BOUND_VALUE; //动画弹跳效果距离
	
	boolean isAniating; //是否正在运行动画
	
	public SlidingDrawer(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.sliding_drawer, this, true);
	}

	public SlidingDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.sliding_drawer, this, true);
	}

	public SlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.sliding_drawer, this, true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		drawer = (LinearLayout)findViewById(R.id.drawer_layout);
	
		content = findViewById(R.id.drawer_content);
		MAX_MOVE_VALUE = CommonUtil.dip2px(getContext(), 20);
		DRAG_BAR_VALUE = CommonUtil.dip2px(getContext(), 35);
		ANIMATION_BOUND_VALUE = CommonUtil.dip2px(getContext(), 15);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		drawer.setOnTouchListener(new LayoutClickListener(w,h));
		drawer.layout(0, h/2, w, h);
		state = DrawerState.Center;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int t = drawer.getTop();
		int b = drawer.getBottom();
		int l = drawer.getLeft();
		int r = drawer.getRight();
		super.onLayout(changed, left, top, right, bottom);
		if (t != b) {
			drawer.layout(l, t, r, b);
			content.layout(l, DRAG_BAR_VALUE, r, b-t);
		}
	}

	class LayoutClickListener implements OnTouchListener, AnimatorListener {

		int lastX, lastY;
		int orgY;// ACTION_DOWN时控件的Y值，用于计算用户拖拉过程中移动的距离，超过临界点时开启动画

		int containerWidth;
		int containerHeight;

		boolean isPressing;
		boolean isMoved;//主要用来判断tab事件的，如果移动过，将不产生tab事件。
		long pressTimeMillis;
		
		public LayoutClickListener(int screenW, int screenH) {
			containerWidth = screenW;
			containerHeight = screenH;
		}

		public boolean onTouch(View v, MotionEvent event) {

			if(isAniating){
				return true;
			}
			int ea = event.getAction();
			switch (ea) {

			case MotionEvent.ACTION_DOWN: // 按下

				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				orgY = lastY;
				//dragView.setImageBitmap(imgPress);
				isPressing = true;
				isMoved = false;
				
				pressTimeMillis = System.currentTimeMillis();
				
				v.layout(0, v.getTop(), v.getWidth(), v.getTop() + containerHeight);
				content.layout(0, content.getTop(),content.getWidth(), containerHeight);
				
				content.requestLayout();
				
				return true;

			case MotionEvent.ACTION_MOVE: // 移动
				if (!isPressing) {
					return false;
				}

				int dy = (int) event.getRawY() - lastY;
				
				if(dy > 10){
					isMoved = true;
				}
				
				int top = v.getTop() + dy;
				int bottom = containerHeight;
				
				if (top < 0) {
					top = 0;
				}
				
				if(top > bottom - DRAG_BAR_VALUE){
					top = bottom - DRAG_BAR_VALUE;
				}

				v.layout(0, top, v.getWidth(), bottom);
				content.layout(0, DRAG_BAR_VALUE, content.getWidth(), bottom - top);
				
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				return true;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				
				//dragView.setImageBitmap(imgNormal);
				if(System.currentTimeMillis() - pressTimeMillis < 200 && !isMoved){
					tapAnimation();
					return true;
				}
				int curY = (int) event.getRawY();
				 doAnimateMove(orgY, curY);
				isPressing = false;
				break;

			}
			return false;
		}

		private void move2Top(int duration, boolean isUp){
			if(isUp){
				
				drawer.layout(0, drawer.getTop(), drawer.getWidth(), drawer.getTop() + containerHeight);
				content.layout(0, content.getTop(), drawer.getWidth(), containerHeight);
				//routeSummary.setBottom(routeSummary.getTop() + containerHeight);
				//scrollView.setBottom(containerHeight);
				content.requestLayout();
			}
			if(duration < 100){
				duration = 100;
				Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
			}
			state = DrawerState.Top;
			ObjectAnimator ani;
			
			if(duration == FAST_ANIMATION_DURATION || duration < 200){
				ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, -drawer.getTop() ).setDuration(duration);
			} else {
				ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, -drawer.getTop(), -drawer.getTop()+ANIMATION_BOUND_VALUE, -drawer.getTop() ).setDuration(duration);
			}
			
			ani.addListener(this);
			ani.start();
		}
		
		private void move2Center(int duration, boolean isUp){
			if(isUp){
				
				drawer.layout(0, drawer.getTop(), drawer.getWidth(), drawer.getTop() + containerHeight);
				content.layout(0, content.getTop(), content.getWidth(), containerHeight);
				//routeSummary.setBottom(routeSummary.getTop() + containerHeight);
				//scrollView.setBottom(containerHeight);
				content.requestLayout();
			}
			if(duration < 100){
				duration = 100;
				Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
			}
			state = DrawerState.Center;
			ObjectAnimator ani;
			if(duration == FAST_ANIMATION_DURATION || duration < 200){
				ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, containerHeight/2 - drawer.getTop()  ).setDuration(duration);
			} else {
				int offset = containerHeight/2 - drawer.getTop();
				if(offset > 0){
					ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offset+ANIMATION_BOUND_VALUE, offset-ANIMATION_BOUND_VALUE, offset).setDuration(duration);
				} else {
					ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offset-ANIMATION_BOUND_VALUE, offset+ANIMATION_BOUND_VALUE, offset).setDuration(duration);
				}
			}
			ani.addListener(this);
			ani.start();
		}
		
		private void move2Bottom(int duration){
			if(duration < 100){
				duration = 100;
				Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
			}
			state = DrawerState.Bottom;
			ObjectAnimator ani;
			if(duration == FAST_ANIMATION_DURATION || duration < 200){
				ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, containerHeight - drawer.getTop() - DRAG_BAR_VALUE ).setDuration(duration);
			} else {
				int offTemp = containerHeight - drawer.getTop() - DRAG_BAR_VALUE;
				ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offTemp, offTemp - ANIMATION_BOUND_VALUE, offTemp ).setDuration(duration);
			}
			ani.addListener(this);
			ani.start();
		}
		
		
		/**
		 * when user tap the drag bar, change the state.
		 */
		private void tapAnimation(){
			if(state == DrawerState.Top){
				move2Bottom(MAX_ANIMATION_DURATION);
			} else if(state == DrawerState.Center){
				move2Top(MAX_ANIMATION_DURATION, true);
			} else {
				move2Center(MAX_ANIMATION_DURATION, true);
			}
		}
		
		/**
		 * do the animation by the down and up Y position
		 * @param orgY, the down Y value, this is absolute value in screen
		 * @param curY, the up Y value, this is absolute value in screen
		 */
		private void doAnimateMove(int orgY, int curY){
	  		float offset = curY - orgY;
	  		float maxDuration = MAX_ANIMATION_DURATION;
	  		float contHeight = containerHeight;
	  		
	  		switch(state){
		  		case Top:{// 当前拖动条在最上面
		  			
		  			if(offset > containerHeight>>1){ //move to bottom 
		  				float duration =  maxDuration *  (contHeight - offset) / (float)(containerHeight>>1) ;
		  				move2Bottom((int)duration);
		  			} else if(offset > MAX_MOVE_VALUE){// move to center	
		  				float duration = maxDuration * ((float)(containerHeight>>1) - offset) / (float)(containerHeight>>1);
		  				move2Center((int)duration, false);
		  			} else {// back to top
		  				move2Top(FAST_ANIMATION_DURATION, true);
		  			}
		  		}
		  		break;
		  		case Center:{// 当前拖动条在中间
		  			
		  			if(offset > 0){
		  				
		  				if(offset > MAX_MOVE_VALUE){
		  					float duration = maxDuration * ((float)(containerHeight>>1) - offset) / (float)(containerHeight>>1);
		  					move2Bottom((int)duration);
		  				} else {
		  					move2Center(FAST_ANIMATION_DURATION, true);
		  				}
		  				
		  			} else {
		  				
		  				if(offset < -MAX_MOVE_VALUE){
		  					float temp = - offset;
		  					float duration = maxDuration * ((float)(containerHeight>>1) - temp) / (float)(containerHeight>>1);
		  					move2Top((int)duration, true);
		  				} else {
		  					move2Center(FAST_ANIMATION_DURATION, false);
		  				}
		  				
		  			}
		  			
		  		}
		  		break;
		  		case Bottom:{// 当前拖动条在下面
		  			if(offset < -(containerHeight/2-DRAG_BAR_VALUE)){
		  				float temp = - offset;
	  					float duration = maxDuration * (contHeight - temp) / (float)(containerHeight>>1);
		  				move2Top((int)duration, true);
		  			} else if(offset < -MAX_MOVE_VALUE){
		  				float temp = - offset;
	  					float duration = maxDuration * ((float)(containerHeight>>1) - temp) / (float)(containerHeight>>1);
		  				move2Center((int)duration, true);
		  			} else {
		  				move2Bottom(FAST_ANIMATION_DURATION);
		  			}
		  		}
		  		break;
	  		}
	  	}

		@Override
		public void onAnimationCancel(Animator arg0) {
			isAniating = false;
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			
			isAniating = false;
			int bottom = containerHeight;
			switch(state){
				case Top:
					drawer.layout(0, 0, drawer.getWidth(), bottom);
					ViewHelper.setTranslationY(drawer, 0);
					content.layout(0, content.getTop(), content.getWidth(),  bottom - drawer.getTop());
					break;
				
				case Center:
					drawer.layout(0, containerHeight / 2, drawer.getWidth(), bottom);
					ViewHelper.setTranslationY(drawer, 0);
					content.layout(0, DRAG_BAR_VALUE, content.getWidth(),  bottom - containerHeight / 2);
					break;
				
				case Bottom:
					drawer.layout(0, bottom - DRAG_BAR_VALUE, drawer.getWidth(), bottom);
					ViewHelper.setTranslationY(drawer, 0);
					content.layout(0, DRAG_BAR_VALUE, content.getWidth(),  bottom - containerHeight / 2);
					break;
			}
			
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
			
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			isAniating = true;
		}
	}
}

/**
 * SlidingDrawer widget have several state, and 
 * support switch among these states. 
 * @author dfqin
 *
 */
enum DrawerState{
	Top,
	Center,
	Bottom
}