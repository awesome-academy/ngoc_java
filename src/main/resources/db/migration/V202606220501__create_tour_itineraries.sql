CREATE TABLE tour_itineraries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tour_id BIGINT NOT NULL,
    day_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,

    CONSTRAINT fk_itinerary_tour
      FOREIGN KEY (tour_id)
          REFERENCES tours(id)
          ON DELETE CASCADE
);
