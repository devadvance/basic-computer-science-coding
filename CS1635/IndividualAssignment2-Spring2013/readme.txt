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
Assignment 2 - Handwrite
Spring Semester 2013


++++++++++++++++++++++++++++++++++++++++++++++++++
++++++++++++++++++++++++++++++++++++++++++++++++++


+-+-+-+-+-+
|A|b|o|u|t|
+-+-+-+-+-+

The project implements a handwriting recognition engine via web API.

+-+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+
|I|n|s|t|a|l|l| |a|n|d| |U|s|a|g|e|
+-+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+

This is the eclipse project folder with all required resources.

+-+-+-+-+-+-+-+
|L|i|c|e|n|s|e|
+-+-+-+-+-+-+-+

This source code is licensed under the New BSD license. The License text is located both at the top of this file, as well as in the LICENSE file.
Although it is not included in the header of every file, all related files in this "PostageCalcTabs" project are under this license.

+-+-+-+-+-+-+-+ +-+-+-+-+
|P|r|o|j|e|c|t| |I|n|f|o|
+-+-+-+-+-+-+-+ +-+-+-+-+

Program Requirements
This app REQUIRES Android version 3.0+. It uses the Action Bar, thus does not work on versions below 3.0.
This is a design decision to minimize code, maximize space in the interface, and to eliminate the use of third party libraries (such as ActionBarSherlock).

Basic Functions
Accepts and responds to touch events.
Tracks and displays single finger movements on a canvas.
Ability to clear the input area.
Select color for ink traces.
Implements two activities (MainActivity and PreferencesActivity).
Only supports English characters. Unless we were eventually making this into a translation app or a keyboard, there is no reason to support anything more...

Extra Credit
Rotation enabled (though it might not look great since the aspect ratio changes!).
Save and load ink traces.
Custom view/widget (DrawingPanel).
Color picker using sliders.
Holding down on the Action Bar buttons provides a popup hint of what they are. In landscape, they have text next to them.