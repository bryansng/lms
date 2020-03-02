// type, i.e. Artifact, Title, ISBN.

// introduce delay.
// https://schier.co/blog/wait-for-user-to-stop-typing-using-javascript
let timeout = null;
function inputSearchArtifactViaAPI(inputId) {
  // handle delay.
  clearTimeout(timeout);

  timeout = setTimeout(function() {
    var focusedInput = document.querySelector(`#${inputId}`);
    var searchQuery = focusedInput.value;
    var cappedInputType = capitalizeFirstLetter(inputId);
    var focusedCardsContainer = document.querySelector(
      `#cardsContainerArtifact${cappedInputType}`
    );
    var searchSpinner = document.querySelector(
      `#inputSpinnerArtifact${cappedInputType}`
    );

    const MAX_RESULTS = 5;
    // var url = `https://www.googleapis.com/books/v1/volumes?q=${searchQuery}&maxResults=${MAX_RESULTS}`;
    var url = `https://www.googleapis.com/books/v1/volumes?q=${searchQuery}&maxResults=${MAX_RESULTS}&printType=all&projection=full`;

    clearChild(focusedCardsContainer);
    if (searchQuery !== "") {
      searchSpinner.style.display = "block";
      fetch(url)
        .then(resp => resp.json())
        .then(artifacts => {
          for (artifact of artifacts.items) {
            var artifactInfo = artifact.volumeInfo;
            var title =
              typeof artifactInfo.title !== "undefined"
                ? artifactInfo.title
                : "";
            var authors =
              typeof artifactInfo.authors !== "undefined"
                ? artifactInfo.authors.join(", ")
                : "";
            var isbn = getISBN13(artifactInfo.industryIdentifiers);
            var publisher =
              typeof artifactInfo.publisher !== "undefined"
                ? artifactInfo.publisher
                : "";
            var publishedOn =
              typeof artifactInfo.publishedDate !== "undefined"
                ? artifactInfo.publishedDate
                : "";
            var category =
              typeof artifactInfo.categories !== "undefined"
                ? artifactInfo.categories[0]
                : "";
            focusedCardsContainer.appendChild(
              getInputSearchArtifactCard(
                title,
                authors,
                isbn,
                publisher,
                publishedOn,
                category,
                artifactInfo,
                artifact.saleInfo,
                focusedCardsContainer
              )
            );
          }
          searchSpinner.style.display = "none";
        });
    }
  }, 500);
}

function fillArtifactForm(
  isbn,
  title,
  authors,
  description,
  publishers,
  publishedOn,
  artifactType,
  genreOrCategory,
  price,
  quantity,
  totalQuantity
) {
  document.querySelector(`#isbn`).value = isbn;
  document.querySelector(`#title`).value = title;
  document.querySelector(`#authors`).value = authors;
  document.querySelector(`#description`).value =
    typeof description !== "undefined" ? description : "";
  document.querySelector(`#publishers`).value = publishers;
  document.querySelector(`#publishedOn`).value = publishedOn;
  document.querySelector(`#type`).value = artifactType === "BOOK" ? "book" : "";
  document.querySelector(`#genre`).value = genreOrCategory;
  document.querySelector(`#itemPrice`).value = price;
  document.querySelector(`#quantity`).value = quantity;
  document.querySelector(`#totalQuantity`).value = totalQuantity;
}

function clearChild(aNode) {
  while (aNode.firstChild) {
    aNode.removeChild(aNode.lastChild);
  }
}

function getISBN13(industryIdentifiers) {
  for (industryIdentifier of industryIdentifiers) {
    if (industryIdentifier.type === "ISBN_13") {
      return industryIdentifier.identifier;
    }
  }
  return "";
}

function getRetailPrice(saleInfo) {
  if (typeof saleInfo.retailPrice !== "undefined") {
    return saleInfo.retailPrice.amount;
  }
  return "10.00";
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
  publisherText,
  publishedDateText,
  categoryText,
  artifactInfo,
  saleInfo,
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

  var category = document.createElement("p");
  category.style.width = "90%";
  category.classList.add("m-0", "font-weight-light", "text-truncate");
  category.textContent = `Category: ${categoryText}`;

  var publisher = document.createElement("p");
  publisher.style.width = "90%";
  publisher.classList.add("m-0", "font-weight-light", "text-truncate");
  publisher.textContent = `Published by ${publisherText}`;

  var publishedDate = document.createElement("p");
  publishedDate.style.width = "90%";
  publishedDate.classList.add("m-0", "font-weight-light", "text-truncate");
  publishedDate.textContent = `Published on ${publishedDateText}`;

  cardContentDetailsContainer.appendChild(title);
  cardContentDetailsContainer.appendChild(authors);
  cardContentDetailsContainer.appendChild(isbn);
  cardContentDetailsContainer.appendChild(category);
  cardContentDetailsContainer.appendChild(publisher);
  cardContentDetailsContainer.appendChild(publishedDate);
  cardImgContainer.appendChild(cardImg);
  cardWrapper.appendChild(cardImgContainer);
  cardWrapper.appendChild(cardContentDetailsContainer);
  cardContainer.appendChild(cardWrapper);

  cardContainer.onclick = () => {
    fillArtifactForm(
      isbnText,
      titleText,
      authorsText,
      artifactInfo.description,
      publisherText,
      publishedDateText,
      artifactInfo.printType,
      categoryText,
      getRetailPrice(saleInfo),
      1,
      1
    );
    clearChild(focusedCardsContainer);
  };

  return cardContainer;
}

function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}
