ALTER TABLE tour_itineraries
    ADD CONSTRAINT uk_tour_itinerary_day
        UNIQUE (tour_id, day_number);