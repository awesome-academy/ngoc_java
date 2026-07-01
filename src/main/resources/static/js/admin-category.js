$(document).ready(function () {
    $(".btn-edit").each(function () {
        $(this).on("click", function () {
            let id = $(this).data("id");
            let name = $(this).data("name");
            let description = $(this).data("description");

            $("#editName").val(name);
            $("#editDescription").val(description);

            $("#editCategoryForm").attr(
                    "action",
                    "/admin/categories/" + id
                );
        });
    });

    let createModal = $("#createCategoryModal");

    if(createModal.length && window.openCreateModal){
        bootstrap.Modal
            .getOrCreateInstance(createModal[0])
            .show();

    }

    let editModal = $("#editCategoryModal");

    if(editModal.length && window.openEditModal){
        bootstrap.Modal
            .getOrCreateInstance(editModal[0])
            .show();
    }
});
