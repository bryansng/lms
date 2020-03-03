function inputAvatarMember(inputId, avatarInitialsSpanId) {
  var focusedInput = document.querySelector(`#${inputId}`);
  var fullName = focusedInput.value.trim();

  if (fullName !== "") {
    console.log(getInitials(fullName));
    document.querySelector(
      `#${avatarInitialsSpanId}`
    ).textContent = limitInitialsDisplayed(getInitials(fullName));
  }
}

function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

function getInitials(name) {
  initials = "";
  for (word of name.split(" ")) {
    initials += word.charAt(0).toUpperCase();
  }
  return initials;
}

function limitInitialsDisplayed(initials) {
  // allow up to only 5 characters.
  return initials.slice(0, 5);
}
