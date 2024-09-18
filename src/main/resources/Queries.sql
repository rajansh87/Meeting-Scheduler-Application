-- to run on mysql workbench to create db

create database project2;
use project2;

CREATE TABLE Employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL
);

INSERT INTO Employee (name,email,contact) VALUES ('emp1','email1','contact1');
INSERT INTO Employee (name,email,contact) VALUES ('emp2','email2','contact2');
INSERT INTO Employee (name,email,contact) VALUES ('emp3','email3','contact3');
INSERT INTO Employee (name,email,contact) VALUES ('emp4','email4','contact4');

CREATE TABLE Room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO Room (name) VALUES ('Meeting Room 1');
INSERT INTO Room (name) VALUES ('Meeting Room 2');
INSERT INTO Room (name) VALUES ('Meeting Room 3');
INSERT INTO Room (name) VALUES ('Meeting Room 4');

CREATE TABLE Meetings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (room_id) REFERENCES Room(id)
);

CREATE TABLE Meeting_Invitees (
    meeting_id BIGINT NOT NULL,
    emp_id BIGINT NOT NULL,
    PRIMARY KEY (meeting_id, emp_id),
    FOREIGN KEY (meeting_id) REFERENCES Meetings(id),
    FOREIGN KEY (emp_id) REFERENCES Employee(id)
);

CREATE TABLE MeetingHistory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    FOREIGN KEY (meeting_id) REFERENCES Meetings(id)
);


select * from meetings;