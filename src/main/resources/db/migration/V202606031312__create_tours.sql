CREATE TABLE tours (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   category_id BIGINT NULL,
   name VARCHAR(255) NOT NULL,
   description TEXT,
   departure_location VARCHAR(255),
   destination VARCHAR(255),
   duration_days INT,
   max_people INT,
   price DECIMAL(18,2),
   start_date DATE,
   end_date DATE,
   thumbnail VARCHAR(500),
   average_rating DECIMAL(3,2),
   total_rating INT DEFAULT 0,
   status VARCHAR(30),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);
