CREATE INDEX idx_tour_itineraries_tour_id
    ON tour_itineraries(tour_id);
ALTER TABLE tour_itineraries DROP CONSTRAINT uk_tour_itinerary_day;