README

REQUIRES MINET TO WORK!

Group Members:
Raymond Wang, Matt Joseph

In order to accomplish this project, each part was worked on by both team members.
Both of us understand and had a hand in implementing every part, give or take a 
little bit here and there.
This includes:
mux_handler
socket_handler
The helper functions (ForgePacket, SendData, etc).


Issues/Bugs/Notes:
-No good way to test client side close. Thus, it is not thoroughly tested.
-The getSize function from the Buffer class is broken. It returns only 6 or greater, 
thus causing issues with ACKing.
-Some debug code is left in. This does not affect performance.
-2 way data transfer is sort of working. It breaks sometimes, but this is most likely due to minet not responding properly to ARPs.