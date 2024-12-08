-- usersテーブルに初期データを挿入
INSERT INTO users (username, email, password)
VALUES 
    ('testUser', 'test@test.com', '$2a$10$QzFh3ZLzQoMYJHXoFE0QX.fJZ0kdASehQ.o2PzODHEc/ZcXj6uOWK'); -- password : testPassword


-- foodsテーブルに初期データを挿入
INSERT INTO foods (name, protein, fat, carbs, calories)
VALUES 
    ('Rice', 4.0, 1.0, 45.0, 200.0),
    ('Chicken Breast', 31.0, 3.6, 0.0, 165.0),
    ('Broccoli', 2.8, 0.4, 6.6, 55.0);

-- meal_historyテーブルに初期データを挿入
INSERT INTO meal_history (user_id, food_id, quantity, meal_number, consumed_at)
VALUES 
    (1, 1, 200, 1, TO_TIMESTAMP('2000/01/01 20:15:00', 'YYYY/MM/DD HH24:MI:SS')),  
    (1, 2, 200, 1, TO_TIMESTAMP('2000/01/01 20:15:00', 'YYYY/MM/DD HH24:MI:SS')),  
    (1, 3, 10, 2, TO_TIMESTAMP('2000/01/01 20:15:00', 'YYYY/MM/DD HH24:MI:SS'));

-- reportsテーブルに初期データを挿入
INSERT INTO reports (user_id, report_date, total_calories, total_protein, total_carbs, total_fat)
VALUES 
    (1, '2000-01-01', 400.0, 8.0, 90.0, 4.0);


