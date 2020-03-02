// type, i.e. Artifact, Title, ISBN.

// introduce delay.
// https://schier.co/blog/wait-for-user-to-stop-typing-using-javascript
let timeoutArtifact = null;
function inputSearchArtifact(inputId, url) {
  // handle delay.
  clearTimeout(timeoutArtifact);

  timeoutArtifact = setTimeout(function() {
    var focusedInput = document.querySelector(`#${inputId}`);
    var searchQuery = focusedInput.value;
    var cappedInputType = capitalizeFirstLetter(inputId);
    var focusedCardsContainer = document.querySelector(
      `#cardsContainerArtifact${cappedInputType}`
    );
    var searchSpinner = document.querySelector(
      `#inputSpinnerArtifact${cappedInputType}`
    );

    clearChild(focusedCardsContainer);
    if (searchQuery !== "") {
      searchSpinner.style.display = "block";
      fetch(url + searchQuery)
        .then(resp => resp.json())
        .then(artifactsPage => {
          for (artifact of artifactsPage.content) {
            focusedCardsContainer.appendChild(
              getInputSearchArtifactCard(
                artifact.title,
                artifact.authors,
                artifact.isbn,
                artifact.itemPrice,
                artifact.id,
                focusedCardsContainer
              )
            );
          }
          searchSpinner.style.display = "none";
        });
    }
  }, 500);
}

function fillArtifactForm(isbn, title, artifactId) {
  document.querySelector(`#isbn`).value = isbn;
  document.querySelector(`#title`).value = title;
  document.querySelector(`#artifactID`).value = artifactId;
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
      <p class="font-weight-bolder m-0 text-truncate">Artifact Title</p>
      <p class="m-0 text-truncate">by Authors</p>
    </div>
  </div>
</div>
*/
function getInputSearchArtifactCard(
  titleText,
  authorsText,
  isbnText,
  fineText,
  artifactIdText,
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

  var cardImg = document.createElement("div");
  cardImg.style.width = "2.5rem";
  cardImg.style.height = "2.5rem";
  cardImg.classList.add("bg-dark");

  var cardContentDetailsContainer = document.createElement("div");
  cardContentDetailsContainer.classList.add(
    "w-100",
    "ml-2",
    "d-flex",
    "flex-column",
    "justify-content-center",
    "align-items-start"
  );

  var title = document.createElement("p");
  title.style.width = "90%";
  title.classList.add("m-0", "font-weight-bolder", "text-truncate");
  title.textContent = titleText;

  var authors = document.createElement("p");
  authors.style.width = "90%";
  authors.classList.add("m-0", "mb-2", "text-truncate");
  authors.textContent = `by ${authorsText}`;

  var isbn = document.createElement("p");
  isbn.style.width = "90%";
  isbn.classList.add("m-0", "font-weight-light", "text-truncate");
  isbn.textContent = `ISBN13: ${isbnText}`;

  var fine = document.createElement("p");
  fine.style.width = "90%";
  fine.classList.add("m-0", "font-weight-light", "text-truncate");
  fine.textContent = `Fine: €${fineText}`;

  cardContentDetailsContainer.appendChild(title);
  cardContentDetailsContainer.appendChild(authors);
  cardContentDetailsContainer.appendChild(isbn);
  cardContentDetailsContainer.appendChild(fine);
  cardImgContainer.appendChild(cardImg);
  cardWrapper.appendChild(cardImgContainer);
  cardWrapper.appendChild(cardContentDetailsContainer);
  cardContainer.appendChild(cardWrapper);

  cardContainer.onclick = () => {
    fillArtifactForm(isbnText, titleText, artifactIdText);
    clearChild(focusedCardsContainer);
  };

  return cardContainer;
}

function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}
