var csrf = $("meta[name=_csrf]").attr("value");

function logout() {
    $.ajax({
        method: "POST",
        url: "/logout",
        headers: {"X-CSRF-TOKEN":csrf},
        success: function() {
            location.replace("/");
        }
    })
}