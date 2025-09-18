INSERT INTO Members (FirstName, LastName) VALUES
 ('John', 'Smith'),
 ('Ethan', 'Calder'),
 ('Sophia', 'Raines'),
 ('Daniel', 'Voss'),
 ('Miranda', 'Keene'),
 ('Julian', 'Harlow'),
 ('Nina', 'Whitmore'),
 ('Gavin', 'Ashford'),
 ('Leah', 'Sutton'),
 ('Maxwell', 'Draper'),
 ('Camilla', 'Hartwell'),
 ('Felix', 'Durant'),
 ('Tessa', 'Holloway'),
 ('Victor', 'Langley'),
 ('Travis', 'Leone'),
 ('Serena', 'March');
 
 INSERT INTO Books (Title, Author, PageCount, Genre) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 180, 'Classic'),
('To Kill a Mockingbird', 'Harper Lee', 281, 'Classic'),
('1984', 'George Orwell', 328, 'Dystopian'),
('Moby-Dick', 'Herman Melville', 635, 'Adventure'),
('Pride and Prejudice', 'Jane Austen', 432, 'Romance'),
('The Catcher in the Rye', 'J.D. Salinger', 277, 'Coming-of-Age'),
('Brave New World', 'Aldous Huxley', 311, 'Dystopian'),
('Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', 309, 'Fantasy'),
('The Hobbit', 'J.R.R. Tolkien', 310, 'Fantasy'),
('The Lord of the Rings', 'J.R.R. Tolkien', 1178, 'Fantasy'),
('The Alchemist', 'Paulo Coelho', 197, 'Fiction'),
('The Da Vinci Code', 'Dan Brown', 454, 'Thriller'),
('War and Peace', 'Leo Tolstoy', 1225, 'Historical'),
('The Road', 'Cormac McCarthy', 287, 'Post-Apocalyptic'),
('Ready Player One', 'Ernest Cline', 384, 'Science Fiction'),
('The Shining', 'Stephen King', 659, 'Horror');


INSERT INTO BorrowRecords (Book_ID, Member_ID, DueDate) VALUES
(1, 1, '2025-04-06'),
(2, 2, '2025-05-06'),
(3, 3, '2025-05-15'),
(4, 4, '2025-05-04'),
(5, 5, '2025-05-01'),
(6, 6, '2025-05-30'),
(7, 7, '2025-05-02'),
(8, 8, '2025-05-03'),
(9, 9, '2025-05-04'),
(10, 9, '2025-05-20'),
(11, 5, '2025-05-05'),
(12, 12, '2025-05-21'),
(13, 2, '2025-05-01'),
(14, 1, '2025-05-03'),
(15, 3, '2025-05-19');

UPDATE Fines SET Amount = 0, LastFineDate = '2025-04-07' WHERE Member_ID = 1;
UPDATE Fines SET Amount = 0, LastFineDate = '2025-05-07' WHERE Member_ID = 2;
UPDATE Fines SET Amount = 7, LastFineDate = '2025-05-16' WHERE Member_ID = 3;
UPDATE Fines SET Amount = 10, LastFineDate = '2025-05-05' WHERE Member_ID = 4;
UPDATE Fines SET Amount = 2, LastFineDate = '2025-05-02' WHERE Member_ID = 5;
UPDATE Fines SET Amount = 3, LastFineDate = '2025-05-31' WHERE Member_ID = 6;
UPDATE Fines SET Amount = 5.5, LastFineDate = '2025-05-03' WHERE Member_ID = 7;
UPDATE Fines SET Amount = 9.5, LastFineDate = '2025-05-04' WHERE Member_ID = 8;
UPDATE Fines SET Amount = 6.25, LastFineDate = '2025-05-05' WHERE Member_ID = 9;
UPDATE Fines SET Amount = 8.75, LastFineDate = '2025-05-21' WHERE Member_ID = 10;
UPDATE Fines SET Amount = 11.75, LastFineDate = '2025-05-06' WHERE Member_ID = 11;
UPDATE Fines SET Amount = 17.38, LastFineDate = '2025-05-22' WHERE Member_ID = 12;
UPDATE Fines SET Amount = 2.5, LastFineDate = '2025-05-02' WHERE Member_ID = 13;
UPDATE Fines SET Amount = 1.50, LastFineDate = '2025-05-04' WHERE Member_ID = 14;
UPDATE Fines SET Amount = 4.75, LastFineDate = '2025-05-20' WHERE Member_ID = 15;
UPDATE Fines SET Amount = 0 WHERE Member_ID = 16;