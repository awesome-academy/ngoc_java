CREATE TABLE tour_reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,

  tour_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,

  rating INT NOT NULL,
  comment TEXT,

  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
      ON UPDATE CURRENT_TIMESTAMP,


  CONSTRAINT fk_review_tour
      FOREIGN KEY (tour_id)
          REFERENCES tours(id)
          ON DELETE CASCADE,

  CONSTRAINT fk_review_user
      FOREIGN KEY (user_id)
          REFERENCES users(id)
          ON DELETE CASCADE,

  CONSTRAINT uk_user_tour_review
      UNIQUE(tour_id, user_id)
);