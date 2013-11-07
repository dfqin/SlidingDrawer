SlidingDrawer
=============

A custom slide drawer widget support low api level 

Though there is a SlidingDrawer widget in android SDK which is  deprecated in API level 17, it can't meet the demands in 
many cases and it hard to extend.  

In my case I need a sliding drawer which support drag to top,drag to center or to bottom, so I write this custom 
SlidingDrawer widget, you can drag or click to operate the drawer.

It use "property animation"  do the animation, to support the low API level, I use the NineOldAndroids compatible 
package (download in http://nineoldandroids.com/).



SlidingDrawer 拖拉窗控件
========================

原来系统有一个SlidingDrawer控件(但在API 17中已不鼓励使用，移到开源项目中了)，但也只支持像android title bar一样拖下来拖上去
两种状态，很难满足实际项目中的很多需求，而且也不方便扩展。

我在自己项目中需要一个拖拉窗，可以打开，关闭，和半开三种状态，没有找到合适的控件使用，就自己写这个控件，窗口滑动动画使用
了属性动画，为了支持底版本，使用了 NineOldAndroids（http://nineoldandroids.com/） 兼容包，可以通过点击或拖动来改变窗口状态。

