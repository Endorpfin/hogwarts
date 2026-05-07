-- 4.2.1 Ограничения для существующих таблиц Student и Faculty

-- Возраст по умолчанию: 20
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;

-- Возраст не меньше 16
ALTER TABLE student
    DROP CONSTRAINT IF EXISTS student_age_check,
    ADD CONSTRAINT student_age_check CHECK (age >= 16);

-- Имя студента: уникальное и not null
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE student
    DROP CONSTRAINT IF EXISTS student_name_unique,
    ADD CONSTRAINT student_name_unique UNIQUE (name);

-- Пара (название факультета, цвет) уникальна
ALTER TABLE faculty
    DROP CONSTRAINT IF EXISTS faculty_name_color_unique,
    ADD CONSTRAINT faculty_name_color_unique UNIQUE (name, color);
