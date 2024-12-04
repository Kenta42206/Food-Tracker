-- usersテーブルに初期データを挿入
INSERT INTO users (username, email, password)
VALUES 
    ('john', 'john@example.com', 'aaaaaa'),
    ('kaera', 'kaera@example.com', 'aaaaaa');

-- foodsテーブルに初期データを挿入
INSERT INTO foods (name, protein, fat, carbs, calories)
VALUES 
    ('Rice', 4.0, 1.0, 45.0, 200.0),
    ('Chicken Breast', 31.0, 3.6, 0.0, 165.0),
    ('Broccoli', 2.8, 0.4, 6.6, 55.0);

-- meal_historyテーブルに初期データを挿入
INSERT INTO meal_history (user_id, food_id, quantity)
VALUES 
    (1, 1, 200),  -- Johnが200gのRiceを食べた
    (2, 2, 150);  -- kaeraが150gのChicken Breastを食べた

-- reportsテーブルに初期データを挿入
INSERT INTO reports (user_id, report_date, total_calories, total_protein, total_carbs, total_fat)
VALUES 
    (1, '2024-12-01', 400.0, 8.0, 90.0, 4.0),
    (2, '2024-12-01', 330.0, 30.0, 0.0, 3.6);

