function logInForm(id) {
  var form = $("#renew-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result === "true") {
        updateStatusSuccess();
      } else {
        updateStatusFail();
        updateErrorMessage(
          "Unable to renew. Artifact is reserved for someone else. Artifact has been reserved for this user automatically."
        );
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function signUpForm(id) {}

function signUpFromLogIn() {
  console.log("clicked");
  var login = document.getElementById("logInDropdown");
  var signup = document.getElementById("signUpDropdown");

  signup.style.display = "none";
  login.style.display = "block";
}

function logInFromSignUp() {
  console.log("clicked");
  var login = document.getElementById("logInDropdown");
  var signup = document.getElementById("signUpDropdown");

  login.style.display = "none";
  signup.style.display = "block";
}

function signUpFromLogIn() {}

function triggerRefresh() {
  var form = $("#searchForm");
  form.submit();
}

function updateStatusSuccess() {
  var inputStatus = $("#updateStatus");
  inputStatus.val("success");
}

function updateStatusFail() {
  var inputStatus = $("#updateStatus");
  inputStatus.val("fail");
}

function updateErrorMessage(msg) {
  var inputErrorMsg = $("#errorMessage");
  inputErrorMsg.val(msg);
}
