use JavaUniversity;
delete Enrolled;
delete Courses;
delete Teachers;
delete Students;

DBCC CHECKIDENT ('Teachers', RESEED, 0);
DBCC CHECKIDENT ('Courses', RESEED, 0);

insert into Students(FirstName, LastName, ID)
values ('Vlad', 'Aro', 220), ('Ion', 'Dragomir', 910), ('Paul', 'Ion', 912), ('Andreea', 'Popescu', 911);

insert into Teachers(FirstName, LastName)
values ('Diana', 'Troanca'), ('Catalin', 'Rusu'), ('Anca', 'Andreica');

insert into Courses([Name],TeacherID,MaxEnrollment,Credits)
values ('Baze de date', 1, 1, 16), ('Metode avansate de programare', 2, 15, 14), 
('Arhitectura sistemelor de calcul', 3, 90, 7);

insert into Enrolled
values (220, 1), (220, 2), (910, 3), (911, 2), (911, 3), (912, 2);
