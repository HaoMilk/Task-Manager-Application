-- Tạo cơ sở dữ liệu
CREATE DATABASE taskmanager;

-- Sử dụng cơ sở dữ liệu mới tạo
USE taskmanager;

-- Tạo bảng users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

-- Tạo bảng tasks
CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_datetime DATETIME,  -- Thêm thời gian bắt đầu (bao gồm ngày và giờ)
    end_datetime DATETIME,    -- Thêm thời gian kết thúc (bao gồm ngày và giờ)
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'OVERDUE') DEFAULT 'PENDING',
    user_id INT,
    category_id INT,  -- Thêm khóa ngoại liên kết với bảng task_categories
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES task_categories(id)  -- Khóa ngoại với bảng task_categories
);


-- Tạo bảng task_categories
CREATE TABLE task_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


ALTER TABLE taskmanager.task_categories
ADD COLUMN image_url VARCHAR(255);

ALTER TABLE taskmanager.users
ADD COLUMN image_url VARCHAR(255);

