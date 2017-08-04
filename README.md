# Pre-work - *Simple Todo App*

**Simple Todo App** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Kaamel Kermaani**

Time spent: **8 (4: v1, 4: v2)** hours spent in total ()

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/fLaY6CF.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Here is the second version after implementing storing in SQL, adding priority and due date, and add a custom adpater.

<img src='http://i.imgur.com/GXQsf2A.gifv' title='Video Walkthrough' width='' alt='Video Walkthrough' />



GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** In my opinion everything seemed natural and expected. The callbacks (onWhatever) make dealing with events fair simple and straightforward.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** The ArrayAdapter provides the "wiring" between the data array (the list) and what is displayed on the sreen. Clearly it is a key element that separtes and connects the data and the view. The getView method returns a view for the given position in the list which will then be displayed by Android in the list view in that position. The converstView allows the sytem to reuse an existing view (if there is one) that is no longer needed (because the user has scrolled it far enough off the screen). This will allow effective use the Android resources.

## Notes

Describe any challenges encountered while building the app.

Displaying the keyboard upon getting focus was interesting and I ended up posting a delayed event to make it to work. 

## License

    Copyright 2017 Kaamel Kermaani

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

