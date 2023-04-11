USE algoclub;

INSERT INTO admin
values(220208153, "Blake Becker", "Mr", "1077 Duck Creek Road", 4152250739, "Blake.Becker@algomau.ca");

INSERT INTO Manager
values(227591869, "Jane Moreno", "Ms", "2654 Joseph Street", 4148108903, "Jane.Moreno@algomau.ca"),
(225214771, "Alban Anderson", "Mr", "4356 Clarence Court", 9102125023, "Alban.Anderson@algomau.ca"),
(228074889, "Robert Savage", "Mr", "38 Perry Street", 2483208164, "Robert.Savage@algomau.ca");

INSERT INTO User(Admin_ID, Manager_ID, User_Name, Password, Role)
VALUES(220208153, null, 'admin', 'admin', 'Admin'),
(null, 227591869, 'manager1', '1234', 'Manager'),
(null, 225214771,'manager2', '123456', 'Manager'),
(null, 228074889,'manager3', 'manager', 'Manager');

INSERT INTO Event(Manager_ID, Event_Name, Event_Location, Event_Date, Event_Description)
VALUES(227591869, "Basic programming languages workshop", "Google Meet", "2022-07-24 08:00:00", "Basic programming languages workshop Workshop of (C, C++, Java)"),
(225214771, "Getting Started on TensorFlow", "Google Meet", "2022-07-17 08:00:00", "Introduction to TensorFlow with Expert Umberto Michelucci"),
(228074889, "New Mobile Applications Club Event", "Google Meet", "2022-07-30 08:00:00", "Develpoing an IOS/Android Mobile App");

INSERT INTO financial(Event_ID, Transaction_Description, Cost, Transaction_Date)
VALUES(1, "Event startup fee", 100, "2022-06-24"),
(1, "Software license fee", -50, "2022-06-26"),
(1, "Printed materials", -20, "2022-06-28");

INSERT INTO student(Student_ID, Student_Name, Pronouns, Student_Address, Student_Phone, Student_Email, Guardian_Name, Guardian_Phone, Relationship)
VALUES(229526419, "John Smith", "He/Him", "4525 Hartway Street", 6057945397, "John.Smith@algomau.ca", "Elizabeth Smith", 3282555516, "Mother"),
(229816002, "Jane Doe", "She/Her", "602-46 Route Roger Bureau", 7045291013, "Jane.Doe@algomau.ca", "Alphonso Doe", 7048070014, "Mother"),
(229987535, "Johny Brown","They/Them", "4011 Charack Road", 8122913310, "Jony.Brown@algomau.ca", "Caleb Brown", 8122167893, "Father"),
(229516144, "Richard Smith", "He/Him", "519 Winifred Way", 7656740060, "Richard.Smith@algomau.ca", "Timothy Smith", 7806242234, "Father"),
(229510223, "Jane Smithuser", "She/Her", "2530 Jarvisville Road", 5168813766, "Jane.Smithuser@algomau.ca", "Robert Smithuser", 4038267999, "Grandfather"),
(229759283, "Everly Miller", "She/Her", "887-43 Avenue Paulette", 6313279267, "Everly.Miller@algomau.ca", "Carlotta Miller", 6049396306, "Mother");

INSERT INTO participant(Event_ID, Student_ID)
VALUES(1, 229114005),
(1, 229424006),
(1, 229510223);