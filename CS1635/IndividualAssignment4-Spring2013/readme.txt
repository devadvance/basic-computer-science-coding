Copyright (c) 2013, Matt Joseph
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


University of Pittsburgh
CS1635
Assignment 4 - Twitter App
Spring Semester 2013


++++++++++++++++++++++++++++++++++++++++++++++++++
++++++++++++++++++++++++++++++++++++++++++++++++++


+-+-+-+-+-+
|A|b|o|u|t|
+-+-+-+-+-+

The project implements a basic Twitter client.

+-+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+
|I|n|s|t|a|l|l| |a|n|d| |U|s|a|g|e|
+-+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+

This is the eclipse project folder with all required resources.

It uses swiping for navigation, and required API v11+ (aka Android 3.0 and higher).

+-+-+-+-+-+-+-+
|L|i|c|e|n|s|e|
+-+-+-+-+-+-+-+

This source code is licensed under the New BSD license. The License text is located both at the top of this file, as well as in the LICENSE file.
Although it is not included in the header of every file, all related files in this "PostageCalcTabs" project are under this license.

+-+-+-+-+-+-+-+ +-+-+-+-+
|P|r|o|j|e|c|t| |I|n|f|o|
+-+-+-+-+-+-+-+ +-+-+-+-+

Program Requirements
Tested on a Galaxy Nexus with 4.2.2.
Also tested in the emulator with 4.0.3.
Note: This app uses fragments. While I attempted to use the support library for everything, I cannot guarantee compatibility with Android 2.3 and below. However, given that the majority of Android devices now run 4.0 and higher, I don't consider this an issue.
Android Platform Distribution: http://developer.android.com/about/dashboards/index.html

Basic Functions
View Home and Mentions timelines.
Post new tweet.
View specific tweet.
Press on the tweet author to get info about them. Note: To view author information, open a tweet by pressing on one, then press on the author's name!
Refresh either timeline.

Extra Credit
Shows user profile images.
Nice modern interface using Fragment Paging and context-aware Action Bar.