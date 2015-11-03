# Project 4 - Tweeter

Tweeter is an android app that allows a user to view home and mentions timelines, view user profiles with user timelines, as well as compose and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: 8 hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] The app includes **all required user stories** from Week 3 Twitter Client
* [X] User can **switch between Timeline and Mention views using tabs**
  * [X] User can view their home timeline tweets.
  * [X] User can view the recent mentions of their username.
* [X] User can navigate to **view their own profile**
  * [X] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [X] User can **click on the profile image** in any tweet to see **another user's** profile.
 * [X] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * [X] Profile view includes that user's timeline
* [X] User can [infinitely paginate](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews) any of these timelines (home, mentions, user) by scrolling to the bottom

The following **optional** features are implemented:

* [X] User can view following / followers list through the profile
* [X] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [X] When a network request is sent, user sees an [indeterminate progress indicator](http://guides.codepath.com/android/Handling-ProgressBars#progress-within-actionbar)
* [X] User can **"reply" to any tweet on their home timeline**
  * [X] The user that wrote the original tweet is automatically "@" replied in compose
* [X] User can click on a tweet to be **taken to a "detail view"** of that tweet
 * [X] User can retweet actions on a tweet
* [X] Improve the user interface and theme the app to feel twitter branded
* [X] User can **search for tweets matching a particular query** and see results

The following **additional** features are implemented:

* [X] Added favourites list to profile
* [X] Swipe to refresh

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/1vRx2g5.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Pager Sliding] (https://github.com/astuetz/PagerSlidingTabStrip)
- [Persistance] (https://github.com/pardom/ActiveAndroid)
