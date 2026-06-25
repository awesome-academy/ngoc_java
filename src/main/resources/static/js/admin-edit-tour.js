document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("itinerariesContainer");
    const addBtn = document.getElementById("addItineraryBtn");
    const template = document.getElementById("itineraryTemplate");

    if (!container || !addBtn || !template) {
        return;
    }

    function renumberDays() {
        const items = container.querySelectorAll(".itinerary-item");

        items.forEach((item, index) => {

            const dayNumber = index + 1;

            const heading = item.querySelector(".itinerary-heading");

            const dayInput = item.querySelector(".day-number");

            const idInput = item.querySelector(".itinerary-id");

            const titleInput = item.querySelector(".itinerary-title");

            const descriptionInput = item.querySelector(".itinerary-description");

            if (heading) {
                heading.textContent = `Day ${dayNumber}`;
            }

            if (dayInput) {
                dayInput.value = dayNumber;
                dayInput.name = `itineraries[${index}].dayNumber`;
            }

            if (idInput) {
                idInput.name = `itineraries[${index}].id`;
            }

            if (titleInput) {
                titleInput.name = `itineraries[${index}].title`;
            }

            if (descriptionInput) {
                descriptionInput.name = `itineraries[${index}].description`;
            }
        });
    }

    addBtn.addEventListener("click", () => {

        const clone = template.content.cloneNode(true);
        container.appendChild(clone);
        renumberDays();
    });

    container.addEventListener("click", (e) => {

        const removeBtn = e.target.closest(".remove-itinerary");
        if (!removeBtn) {
            return;
        }

        const item = removeBtn.closest(".itinerary-item");

        if (item) {
            item.remove();
            renumberDays();
        }
    });

    /*
     * CREATE:
     *   container rỗng -> tạo Day 1 mặc định
     *
     * EDIT:
     *   đã có dữ liệu -> chỉ renumber lại
     */
    const existingItems =
        container.querySelectorAll(".itinerary-item");

    if (existingItems.length === 0) {
        addBtn.click();
    } else {
        renumberDays();
    }
});
