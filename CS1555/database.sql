--Matt Joseph
--Arielle Garcia
-- See README.txt for license information.

DROP TABLE profile CASCADE CONSTRAINTS;
DROP TABLE friends CASCADE CONSTRAINTS;
DROP TABLE pendingfriends CASCADE CONSTRAINTS;
DROP TABLE messages CASCADE CONSTRAINTS;
DROP TABLE messageRecipients CASCADE CONSTRAINTS;
DROP TABLE groups CASCADE CONSTRAINTS;
DROP TABLE groupMembership CASCADE CONSTRAINTS;
DROP SEQUENCE seq_userID;
DROP SEQUENCE seq_msgID;
DROP SEQUENCE seq_gID;
DROP MATERIALIZED VIEW groupCheck_MV;

purge recyclebin;

CREATE TABLE profile
(
	userID NUMBER(10) NOT NULL,
	name VARCHAR2(64) NOT NULL,
	email VARCHAR2(32) NOT NULL,
	password VARCHAR2(32) NOT NULL,
	date_of_birth DATE,
	picture_URL VARCHAR2(128),
	aboutme VARCHAR2(1024),
	lastlogin DATE,
	CONSTRAINT profile_pk PRIMARY KEY (userID),
	CONSTRAINT profile_email_unique UNIQUE (email)
);

CREATE SEQUENCE seq_userID START WITH 8 INCREMENT BY 1;

CREATE TABLE friends
(
	userID1 NUMBER(10) NOT NULL,
	userID2 NUMBER(10) NOT NULL,
	JDate DATE NOT NULL,
	message VARCHAR2(1024),
	CONSTRAINT friends_pk PRIMARY KEY (userID1, userID2),
	CONSTRAINT friends_fk1 FOREIGN KEY (userID1) REFERENCES profile(userID),
	CONSTRAINT friends_fk2 FOREIGN KEY (userID2) REFERENCES profile(userID)
);

CREATE TABLE pendingfriends
(
	fromID NUMBER(10) NOT NULL,
	toID NUMBER(10) NOT NULL,
	message VARCHAR2(1024),
	CONSTRAINT pendingfriends_pk PRIMARY KEY (fromID, toID),
	CONSTRAINT pendingfriends_fk1 FOREIGN KEY (fromID) REFERENCES profile(userID),
	CONSTRAINT pendingfriends_fk2 FOREIGN KEY (toID) REFERENCES profile(userID)
);

CREATE TABLE messages
(
	msgID NUMBER(10) NOT NULL,
	fromID NUMBER(10),
	message VARCHAR2(1024),
	toID NUMBER(10) DEFAULT NULL,
	toGroupID NUMBER(10) DEFAULT NULL,
	dateSent DATE NOT NULL,
	CONSTRAINT messages_pk PRIMARY KEY (msgID),
	CONSTRAINT messages_fk1 FOREIGN KEY (fromID) REFERENCES profile(userID)
);

CREATE SEQUENCE seq_msgID START WITH 5 INCREMENT BY 1;

CREATE TABLE messageRecipients
(
	msgID NUMBER(10) NOT NULL,
	userID NUMBER(10) NOT NULL,
	CONSTRAINT messageRecipients_pk PRIMARY KEY (msgID, userID),
	CONSTRAINT messageRecipients_fk1 FOREIGN KEY (msgID) REFERENCES messages(msgID) DEFERRABLE,
	CONSTRAINT messageRecipients_fk2 FOREIGN KEY (userID) REFERENCES profile(userID)
);
-- messageRecipients_fk1 is set to DEFERRABLE in case the recipients get added in a transaction before the message.

CREATE TABLE groups
(
	gID NUMBER(10) NOT NULL,
	name VARCHAR2(64) NOT NULL,
	description VARCHAR2(1024),
	CONSTRAINT groups_pk PRIMARY KEY (gID),
	CONSTRAINT groups_email_unique UNIQUE (name)
);

CREATE SEQUENCE seq_gID START WITH 3 INCREMENT BY 1;

CREATE TABLE groupMembership
(
	gID NUMBER(10) NOT NULL,
	userID NUMBER(10) NOT NULL,
	CONSTRAINT groupMembership_pk PRIMARY KEY (gID, userID),
	CONSTRAINT groupMembership_fk1 FOREIGN KEY (gID) REFERENCES groups(gID) DEFERRABLE,
	CONSTRAINT groupMembership_fk2 FOREIGN KEY (userID) REFERENCES profile(userID)
	);
-- groupMembership_fk1 is set to DEFERRABLE in case the members get added in a transaction before the group.

CREATE OR REPLACE TRIGGER drop_trigger
AFTER DELETE
ON profile
FOR EACH ROW
BEGIN
-- Delete member from group WORKS!
DELETE FROM groupMembership WHERE userID= :old.userID;
-- Delete friendships WORKS!
DELETE FROM friends WHERE userID1= :old.userID or userID2= :old.userID;
-- Delete pending friendships WORKS???
DELETE FROM pendingfriends WHERE fromID= :old.userID or toID= :old.userID;

-- Delete from messageRecipients WORKS!
DELETE FROM messageRecipients WHERE userID= :old.userID;

-- Update messages where you are the recipient
UPDATE messages
SET toID= NULL
WHERE toID= :old.userID;

-- Update messages where you are the sender
UPDATE messages
SET fromID= NULL
WHERE fromID= :old.userID;

-- Delete from messages if everything is null and it wasn't to a group
DELETE FROM messages WHERE fromID IS NULL AND toID IS NULL AND toGroupID IS NULL;

-- Delete from messages if the sender is gone, it was sent to a group, and the group is empty
DELETE FROM messages WHERE fromID IS NULL AND toID IS NULL AND toGroupID IS NOT NULL AND msgID NOT IN (SELECT msgID from messageRecipients);

END;
/



SET TRANSACTION READ WRITE;
INSERT INTO profile VALUES(1, 'Shenoda', 'shg@pitt.edu', 'shpwd', '13-OCT-1977', '/afs/pitt.edu/home/s/g/shg18/public/photo.jpg', 'CS 1555 TA', '11-NOV-2012');
INSERT INTO profile VALUES(2, 'Lory', 'lra@pitt.edu', 'lpwd', '08-MAR-1986', NULL, 'Member of ADMT Lab', '10-NOV-2012');
INSERT INTO profile VALUES(3, 'Peter', 'pdj@pitt.edu', 'ppwd', '09-JAN-1984', 'http://www.cs.pitt.edu/~peter', 'Graduate Student in CS dept.', '10-NOV-2012');
INSERT INTO profile VALUES(4, 'Alexandrie', 'alx@pitt.edu', 'apwd', '21-AUG-1975', NULL, 'Architecture researcher', '11-NOV-2012');
INSERT INTO profile VALUES(5, 'Panickos', 'pnk@pitt.edu', 'kpwd', '08-SEP-1989', NULL, 'ADMT Lab researcher', '08-NOV-2012');
INSERT INTO profile VALUES(6, 'Socratis', 'soc@pitt.edu', 'spwd', '17-MAY-1981', NULL, 'TA in CS dept', '09-NOV-2012');
INSERT INTO profile VALUES(7, 'Yaw', 'yaw@pitt.edu', 'ypwd', '27-FEB-1987', NULL, 'Staff at CS dept', '07-NOV-2012');

INSERT INTO friends VALUES(1,2, '06-JAN-2008', 'Hey, it is me  Shenoda!' );
INSERT INTO friends VALUES(1,5, '15-JAN-2011', 'Hey, it is me  Shenoda!');
INSERT INTO friends VALUES(2,3, '23-AUG-2007', 'Hey, it is me  Lory!');
INSERT INTO friends VALUES(2,4, '17-FEB-2008', 'Hey, it is me  Lory!');
INSERT INTO friends VALUES(3,4, '16-SEP-2010', 'Hey, it is me  Peter!');
INSERT INTO friends VALUES(4,6, '06-OCT-2010', 'Hey, it is me  Alexandrie!');
INSERT INTO friends VALUES(6,7, '13-SEP-2012', 'Hey, it is me  Socratis!');

INSERT INTO pendingfriends VALUES(7,4, 'Hey, it is me Yaw');
INSERT INTO pendingfriends VALUES(5,2, 'Hey, it is me Panickos');
INSERT INTO pendingfriends VALUES(2,6, 'Hey, it is me Lory');

INSERT INTO messages VALUES(1,1, 'are we meeting tomorrow for the project?', 2, NULL, '09-NOV-2012');
INSERT INTO messages VALUES(2,1, 'Peter''s pub tomorrow?', 5, NULL, '07-NOV-2012');
INSERT INTO messages VALUES(3,2, 'Please join our DB Group forum tomorrow', NULL, 1, '06-NOV-2012');
INSERT INTO messages VALUES(4,5, 'Here is the paper I will present tomorrow', NULL, 2, '04-NOV-2012');

INSERT INTO messageRecipients VALUES(3,1);
INSERT INTO messageRecipients VALUES(3,2);
INSERT INTO messageRecipients VALUES(3,3);
INSERT INTO messageRecipients VALUES(3,4);
INSERT INTO messageRecipients VALUES(3,5);
INSERT INTO messageRecipients VALUES(3,6);
INSERT INTO messageRecipients VALUES(3,7);
INSERT INTO messageRecipients VALUES(4,1);
INSERT INTO messageRecipients VALUES(4,2);
INSERT INTO messageRecipients VALUES(4,5);

INSERT INTO groups VALUES(1, 'Grads at CS', 'list of all graduate students');
INSERT INTO groups VALUES(2, 'DB Group', 'member of the ADMT Lab.');

INSERT INTO groupMembership VALUES(1,1);
INSERT INTO groupMembership VALUES(1,2);
INSERT INTO groupMembership VALUES(1,3);
INSERT INTO groupMembership VALUES(1,4);
INSERT INTO groupMembership VALUES(1,5);
INSERT INTO groupMembership VALUES(1,6);
INSERT INTO groupMembership VALUES(1,7);
INSERT INTO groupMembership VALUES(2,1);
INSERT INTO groupMembership VALUES(2,2);
INSERT INTO groupMembership VALUES(2,5);
COMMIT;

CREATE OR REPLACE TRIGGER sendGroupMessage_trigger
AFTER INSERT ON messages
FOR EACH ROW
WHEN (new.toGroupID IS NOT NULL)
BEGIN
INSERT INTO messageRecipients
	(SELECT :new.msgID, userID FROM groupMembership WHERE gID = :new.toGroupID);
END;
/

CREATE MATERIALIZED VIEW groupCheck_MV
BUILD IMMEDIATE
REFRESH COMPLETE ON COMMIT AS
SELECT userID, COUNT(gID) currCount
FROM groupMembership
GROUP BY userID;

ALTER TABLE groupCheck_MV
ADD CONSTRAINT maxGroupCheck
CHECK (currCount <= 10)
DEFERRABLE;