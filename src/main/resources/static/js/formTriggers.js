function loanForm(id) {
  var form = $("#loan-form-" + id);
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
          "Unable to loan. Artifact not in stock. No change made to the database."
        );
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
      if (result === "true") {
        updateStatusSuccess();
      } else {
        updateStatusFail();
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

function lostForm(id) {
  var form = $("#lost-form-" + id);
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
      if (result === "true") {
        updateStatusSuccess();
      } else {
        updateStatusFail();
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
      if (result === "true") {
        updateStatusSuccess();
      } else {
        updateStatusFail();
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
