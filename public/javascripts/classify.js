if (window.console) {
    $(document).on("click", "#classify-button", function() {
        $.ajax({
            url : "/classify_tm",
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

if (window.console) {
    $(document).on("click", "#classify-button2", function() {
        $.ajax({
            url : "/classify_rc",
            type: "POST",
            data: JSON.stringify(
                {segment: $("#input").val()}
            ),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#author-result2").text(data)
            }
        });
    });
}