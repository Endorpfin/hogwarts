-- 4.2.3 JOIN-запросы

-- 1) Все студенты с названиями факультетов
SELECT s.name AS student_name,
       s.age  AS student_age,
       f.name AS faculty_name
FROM student s
         JOIN faculty f ON f.id = s.faculty_id;

-- 2) Только студенты, у которых есть аватарки
SELECT DISTINCT s.name AS student_name,
                s.age  AS student_age
FROM student s
         JOIN avatar a ON a.student_id = s.id;
