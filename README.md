SlidingDrawer
=============

A custom slide drawer widget support low api level 

Though there is a SlidingDrawer widget in android SDK which is  deprecated in API level 17, it can't meet the demand in 
many case and it hard to extend.  

In my case I need a sliding drawer which support drag to top,drag to center or to bottom, so I write these custom 
SlidingDrawer widget, you can drag or click to operate the drawer.

It use "property animation"  do the animation, to support the low API level, I use the NineOldAndroids compatible 
pakage(download in http://nineoldandroids.com/).
