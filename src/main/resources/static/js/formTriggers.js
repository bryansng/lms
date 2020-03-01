function loanForm(id) {
  var form = $("#loan-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result.isSuccess === true) {
        setIsSuccess();
        updateSuccessMessage(result.message);
      } else {
        setIsFailure();
        updateFailureMessage(result.message);
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function returnForm(id) {
  var form = $("#return-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result.isSuccess === true) {
        setIsSuccess();
        updateSuccessMessage(result.message);
      } else {
        setIsFailure();
        updateFailureMessage(result.message);
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function renewForm(id) {
  var form = $("#renew-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result.isSuccess === true) {
        setIsSuccess();
        updateSuccessMessage(result.message);
      } else {
        setIsFailure();
        updateFailureMessage(result.message);
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function lostForm(id) {
  var form = $("#lost-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result.isSuccess === true) {
        setIsSuccess();
        updateSuccessMessage(result.message);
      } else {
        setIsFailure();
        updateFailureMessage(result.message);
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function restockForm(id) {
  var form = $("#restock-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result.isSuccess === true) {
        setIsSuccess();
        updateSuccessMessage(result.message);
      } else {
        setIsFailure();
        updateFailureMessage(result.message);
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function deleteForm(id) {
  var form = $("#delete-form-" + id);
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "post",
    success: function(result) {
      // Do something with the response.
      // Might want to check for errors here.
      if (result.isSuccess === true) {
        setIsSuccess();
        updateSuccessMessage(result.message);
      } else {
        setIsFailure();
        updateFailureMessage(result.message);
      }
      triggerRefresh();
    },
    error: function(error) {
      // Here you can handle exceptions thrown by the server or your controller.
    }
  });
}

function triggerRefresh() {
  var form = $("#searchForm");
  form.submit();
}

function setIsSuccess() {
  var inputStatus = $("#isSuccess");
  inputStatus.val("true");
}

function setIsFailure() {
  var inputStatus = $("#isSuccess");
  inputStatus.val("false");
}

function updateSuccessMessage(msg) {
  var inputSuccessMsg = $("#successMessage");
  inputSuccessMsg.val(msg);
}

function updateFailureMessage(msg) {
  var inputFailureMsg = $("#failureMessage");
  inputFailureMsg.val(msg);
}
