if (window.console) {
    $(document).on("click", "#classify-button", function() {
        $.ajax({
            url : "/classify",
            type: "POST",
            data: JSON.stringify(
                {segment: $("#input").val()}
            ),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#author-result").text(data)
            }
        });
    });
}