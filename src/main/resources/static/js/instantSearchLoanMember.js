// type, i.e. Member, Title, ISBN.

// introduce delay.
// https://schier.co/blog/wait-for-user-to-stop-typing-using-javascript
let timeoutMember = null;
function inputSearchMember(inputId, url) {
  // handle delay.
  clearTimeout(timeoutMember);

  timeoutMember = setTimeout(function() {
    var focusedInput = document.querySelector(`#${inputId}`);
    var searchQuery = focusedInput.value;
    var cappedInputType = capitalizeFirstLetter(inputId);
    var focusedCardsContainer = document.querySelector(
      `#cardsContainerMember${cappedInputType}`
    );
    var searchSpinner = document.querySelector(
      `#inputSpinnerMember${cappedInputType}`
    );

    clearChild(focusedCardsContainer);
    if (searchQuery !== "") {
      searchSpinner.style.display = "block";
      fetch(url + searchQuery)
        .then(resp => resp.json())
        .then(membersPage => {
          for (member of membersPage.content) {
            focusedCardsContainer.appendChild(
              getInputSearchMemberCard(
                member.id,
                member.email,
                member.fullName,
                member.mobileNumber,
                member.type,
                focusedCardsContainer
              )
            );
          }
          searchSpinner.style.display = "none";
        });
    }
  }, 500);
}

function fillMemberForm(memberId) {
  document.querySelector(`#memberID`).value = memberId;
}

function clearChild(aNode) {
  while (aNode.firstChild) {
    aNode.removeChild(aNode.lastChild);
  }
}

/*
<div href="#" class="px-3 py-2 quick-search-dropdown-hover hover-only-bg border-bottom">
  <div class="d-flex justify-content-start align-items-center">
    <div class="bg-dark" style="width: 2.5rem; height: 2.5rem"></div>
    <div class="ml-2 d-flex flex-column justify-content-center align-items-start">
      <p class="font-weight-bolder m-0 text-truncate">Member Title</p>
      <p class="m-0 text-truncate">by Authors</p>
    </div>
  </div>
</div>
*/
function getInputSearchMemberCard(
  memberIdText,
  emailText,
  fullNameText,
  mobileNumberText,
  typeText,
  focusedCardsContainer
) {
  var cardContainer = document.createElement("div");
  cardContainer.classList.add(
    "px-3",
    "py-2",
    "quick-search-dropdown-hover",
    "hover-only-bg",
    "border-bottom"
  );

  var cardWrapper = document.createElement("div");
  cardWrapper.classList.add(
    "d-flex",
    "justify-content-start",
    "align-items-center"
  );

  var cardImgContainer = document.createElement("div");
  cardImgContainer.classList.add("h-100");

  var cardImgWrapper = document.createElement("div");
  cardImgWrapper.classList.add(
    "d-flex",
    "justify-content-center",
    "align-items-center",
    "member-search-avatar-container"
  );

  var cardImgSpan = document.createElement("span");
  cardImgSpan.classList.add("font-weight-bold");
  cardImgSpan.textContent = limitInitialsDisplayed(getInitials(fullNameText));

  var cardContentDetailsContainer = document.createElement("div");
  cardContentDetailsContainer.classList.add(
    "w-100",
    "ml-2",
    "d-flex",
    "flex-column",
    "justify-content-center",
    "align-items-start"
  );

  var fullName = document.createElement("p");
  fullName.style.width = "90%";
  fullName.classList.add("m-0", "font-weight-bolder", "text-truncate");
  fullName.textContent = fullNameText;

  var email = document.createElement("p");
  email.style.width = "90%";
  email.classList.add("m-0", "mb-2", "text-truncate");
  email.textContent = `Email: ${emailText}`;

  var id = document.createElement("p");
  id.style.width = "90%";
  id.classList.add("m-0", "font-weight-light", "text-truncate");
  id.textContent = `Member ID: ${memberIdText}`;

  var mobileNumber = document.createElement("p");
  mobileNumber.style.width = "90%";
  mobileNumber.classList.add("m-0", "font-weight-light", "text-truncate");
  mobileNumber.textContent = `Mobile Number: ${mobileNumberText}`;

  var type = document.createElement("p");
  type.style.width = "90%";
  type.classList.add("m-0", "font-weight-light", "text-truncate");
  type.textContent = `Type: ${capitalizeFirstLetter(typeText)}`;

  cardContentDetailsContainer.appendChild(fullName);
  cardContentDetailsContainer.appendChild(email);
  cardContentDetailsContainer.appendChild(id);
  cardContentDetailsContainer.appendChild(mobileNumber);
  cardContentDetailsContainer.appendChild(type);
  cardImgContainer.appendChild(cardImgWrapper);
  cardImgWrapper.appendChild(cardImgSpan);
  cardWrapper.appendChild(cardImgContainer);
  cardWrapper.appendChild(cardContentDetailsContainer);
  cardContainer.appendChild(cardWrapper);

  cardContainer.onclick = () => {
    fillMemberForm(memberIdText);
    clearChild(focusedCardsContainer);
  };

  return cardContainer;
}

function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}
