$(document).ready(function () {
    $(".toggle-password").click(function () {
        const input = $(this).siblings("input");

        if (input.attr("type") === "password") {
            input.attr("type", "text");
            $(this).text("🙈");
        } else {
            input.attr("type", "password");
            $(this).text("👁️");
        }
    });
});
