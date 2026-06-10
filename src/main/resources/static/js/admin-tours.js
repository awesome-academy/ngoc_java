$(function(){
    // Delete confirmation
    $('.btn-delete').on('click', function(e){
        e.preventDefault();
        const id = $(this).data('id');
        if (!id) return;
        if (confirm('Are you sure you want to delete this tour?')){
            // Example: send DELETE via AJAX (requires controller endpoint)
            $.ajax({
                url: '/admin/tours/' + id,
                method: 'DELETE',
                success: function(resp){
                    // simple reload on success
                    location.reload();
                },
                error: function(){
                    alert('Could not delete tour.');
                }
            });
        }
    });

    // Simple local filtering/search (client-side)
    $('#btnSearch').on('click', function(){
        const q = $('#searchInput').val().toLowerCase();
        const status = $('#filterStatus').val();

        $('tbody tr').each(function(){
            const $tr = $(this);
            if ($tr.find('td').length === 0) return; // skip empty/no-data row

            const title = $tr.find('td:nth-child(2)').text().toLowerCase();
            const cat = $tr.find('td:nth-child(3)').text().toLowerCase();
            const stat = $tr.find('td:nth-child(7)').text().toLowerCase();

            const matchesQ = !q || title.includes(q) || cat.includes(q);
            const matchesStatus = !status || stat.includes(status);

            if (matchesQ && matchesStatus) $tr.show(); else $tr.hide();
        });
    });
});
