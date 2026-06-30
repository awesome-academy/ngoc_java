$(document).ready(function () {
    function updateTotalPrice() {
        let price = Number($("#totalPrice").data("price"));
        let people = parseInt($("#adults").val()) || 1;

        if (people < 1) {
            people = 1;
            $("#adults").val(1);
        }

        let total = price * people;

        $("#totalPrice").text(total.toLocaleString('en-US', { style: 'currency', currency: 'USD' }));
    }

    // Hiển thị khi load trang
    updateTotalPrice();

    // Cập nhật khi thay đổi số người
    $("#adults").on("input", function () {
        updateTotalPrice();
    });
});