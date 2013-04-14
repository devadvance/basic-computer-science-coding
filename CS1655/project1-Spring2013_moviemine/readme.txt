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
CS1655
Assignment 1 - moviemine
Spring Semester 2013


++++++++++++++++++++++++++++++++++++++++++++++++++
++++++++++++++++++++++++++++++++++++++++++++++++++


+-+-+-+-+-+
|A|b|o|u|t|
+-+-+-+-+-+

This project is an implementation of the apriori algorithm. It takes movie preferences from here: (http://www.grouplens.org/node/73) and allows correlation of positive/negative/combined preferences to suggest association rules.

+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+
|I|n|s|t|a|l| |a|n|d| |U|s|a|g|e|
+-+-+-+-+-+-+ +-+-+-+ +-+-+-+-+-+

MovieMine should compile with any version of Java 1.4 or newer. It was compiled/tested using SE6 and SE7.

To compile:
javac *.java

To run:
java MovieMine -minsup XXX -minconf YYY -maxmovies ZZZ -pos/neg/comb
Arguments:
XXX - the minSup as a decimal between 0 and 1
YYY - the minConf as a decimal between 0 and 1
ZZZ - max movies per rule
-pos/-neg/-comb - which table to use

+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+
|R|e|q|u|i|r|e|d| |F|e|a|t|u|r|e|s|
+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+

All features specified in the project PDF are included and working properly.

There are no known bugs or issues.

+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+-+-+-+
|F|e|a|t|u|r|e| |E|x|p|l|a|n|a|t|i|o|n|s|
+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+-+-+-+

This takes full advantage of HashMaps and HashSets. HashSets have O(1) time for insert, contains, and delete.
HashMaps have O(1) lookup for managing an itemset -> count mapping.
HashSets also allow candidate set generation and rule generation to be simpler to implement.
