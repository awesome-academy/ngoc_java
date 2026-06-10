// jQuery handlers for Create Tour page
$(function() {
    // Image preview
    $('#images').on('change', function(e) {
        const files = e.target.files;
        const $preview = $('#imagePreview');
        $preview.empty();

        if (!files || files.length === 0) return;

        Array.from(files).slice(0, 6).forEach(file => {
            if (!file.type.startsWith('image/')) return;
            const reader = new FileReader();
            reader.onload = function(evt) {
                const $img = $('<img>').attr('src', evt.target.result);
                $preview.append($img);
            };
            reader.readAsDataURL(file);
        });
    });

    // Simple client-side validation on submit
    $('.create-tour-form').on('submit', function(e) {
        const title = $.trim($('#title').val());
        const price = parseFloat($('#price').val()) || 0;
        const seats = parseInt($('#seats').val()) || 0;

        let errors = [];
        if (!title) errors.push('Title is required');
        if (price <= 0) errors.push('Price must be greater than 0');
        if (seats <= 0) errors.push('Seats must be at least 1');

        if (errors.length) {
            e.preventDefault();
            alert(errors.join('\n'));
            return false;
        }

        // otherwise allow form submit
        return true;
    });
});
