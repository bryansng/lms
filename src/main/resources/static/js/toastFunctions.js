$(".toast").toast("show");

let timeout = null;
clearTimeout(timeout);
timeout = setTimeout(function () {
  $(".toast").toast("hide");
}, 10000)