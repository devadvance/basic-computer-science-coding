Copyright (c) 2012, Matt Joseph, Arielle Garcia
All rights reserved.

See the LICENSE for the specific language governing permissions and limitations under the LICENSE.


University of Pittsburgh
CS1555
Term Project


++++++++++++++++++++++++++++++++++++++++++++++++++
++++++++++++++++++++++++++++++++++++++++++++++++++


This is the README file for the faces@Pitt project for CS1555, created by Matt Joseph and Arielle Garcia.

+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+
|I|n|s|t|a|l| |a|n|d| |U|s|a|g|e|
+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+

facesApp.java should compile with any version of Java 1.4 or newer. The project specified 1.5, and thus it was always compiled on the server using that version.


+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+
|R|e|q|u|i|r|e|d| |F|e|a|t|u|r|e|s|
+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+

All features specified in the project PDF are included and working properly.
There are no known bugs or issues.


+-+-+-+-+-+-+-+ +-+-+-+-+ +-+-+-+-+-+ +-+
|C|h|a|n|g|e|s| |f|r|o|m| |P|h|a|s|e| |1|
+-+-+-+-+-+-+-+ +-+-+-+-+ +-+-+-+-+-+ +-+

1) Changed the group message trigger from a FOR loop to an INSERT SELECT.
2) Added sequences for messages, users, and groups.
3) Fixed the attribute names.


+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+-+-+-+
|F|e|a|t|u|r|e| |E|x|p|l|a|n|a|t|i|o|n|s|
+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+-+-+-+

Quick list of additional features (explained further below):
1) Create a group
2) Send a message to more than one individual user
3) Two types of statistics

The Three Degrees of Separation feature uses a graph and Djikstra's algorithm to calculate shortest paths and determine the number of hops in between users. This was done for later extensibility and future-proofing, rather than as an efficiency concern. Doing it as a stored procedure in the database is theoretically fast, but doing Djikstra's algorithm in Oracle is somewhat outside of the scope of this course.

You should not be able to add a friend who is already your friend, nor should you be able to create duplicate friend requests. However, you can have two friend requests (from A - > B AND B - > A) but only one can be accepted. The other cannot be accepted and will simply be removed. This applies to both confirming specific requests as well as confirming all. In the case of confirming specific, it will tell the user to ignore it. For confirming all, the program will just ignore it in the background and remove it at the end.

You can send a message to one user, one group, OR multiple users. This was a feature carried over from the initial project description and was completed before the project guidelines were updated. It is an extra feature, and does not affect the proper working of the program.

Finding a user will automatically display the profiles of all of the users found. This was done since it was not specified otherwise in the project guidelines. Both displaying friends and finding users take advantage of the same helper method and are fairly efficient.

My Statistics offers two types of statistics because the project description was unspecific. The statistics show the top k friends who have sent or received the highest number of messages during for the past x months...
1) just with you (the current user)
2) in general (with anyone)
Both options are presented to the user.

When possible, data is kept in the Java interface after a query. For example, addFriends uses a query to grab a user's friends and then if the user tries to add a friend they already have, it tells them. This could also be done with another SELECT query, but it is faster, more efficient, and better JDBC practice to *not* re-run queries if possible.

Displaying friends may include friends of the user inside of Friends of Friends as well. This is because a user's friends might be friends with each other. This is not a bug, but rather just a fact of the implementation.

When joining groups, you cannot join more than 10. This is prevented by a combination of two things:
1) After every join, your list of groups is re-retrieved
2) If you are running two instances of the program, the database will prevent the second insert


+-+-+-+-+-+-+-+-+-+-+-+
|L|i|m|i|t|a|t|i|o|n|s|
+-+-+-+-+-+-+-+-+-+-+-+

Most text fields are not of an unlimited length. Messages and About Me are set to 1024 characters in length inside of the database, and any extra data will either error or cause truncation.

Input validation is not complete. It covers most cases, such as entering a letter when it asks for a number, as well as entering bad numbers or SQL injection, but there may be edge cases (such as an integer over MAX_INT) that is does not cover. Sometimes, if the user does not enter a valid option, it will just return them to the main menu rather than ask them to immediately try again.


+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+
|S|o|u|r|c|e|s| |O|u|t|s|i|d|e| |t|h|e| |C|l|a|s|s|
+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+

(Matt):

For the Djikstra method of computing the three degrees of separation, I used a combination of the basic structures located here:
http://en.literateprograms.org/Dijkstra's_algorithm_(Java)

as well as the Graph class that I wrote from the Algorithms class. Thus, I was able to create a "social graph" representation of the network.

Also useful was the Wikipedia page on the algorithm:
http://en.wikipedia.org/wiki/Dijkstra's_algorithm

For joining groups, I was unsure of how to do a CHECK CONSTRAINT with a SELECT and an aggregate function. This is unsupported by Oracle, and thus with a bit of help from:
http://tonyandrews.blogspot.com/2004/10/enforcing-complex-constraints-in.html

I was able to determine that I needed to use a VIEW in order to use a CHECK on top of an aggregate function.

(Arielle):

None.
