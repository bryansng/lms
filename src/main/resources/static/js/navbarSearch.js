let timeoutNavbarArtifactSearch = null;
function navbarSearchForm(url) {
  // handle delay.
  clearTimeout(timeoutNavbarArtifactSearch);

  timeoutNavbarArtifactSearch = setTimeout(function() {
    var searchQuery = document.querySelector("#navbarSearchQuery").value;
    var searchSpinner = document.querySelector(".search-spinner");
    var searchGlass = document.querySelector(".search-glass");

    clearChild(document.querySelector("#cardsContainer"));
    if (searchQuery !== "") {
      searchSpinner.style.display = "block";
      searchGlass.style.display = "none";

      fetch(url + searchQuery)
        .then(resp => resp.json())
        .then(artifactsPage => {
          for (artifact of artifactsPage.content) {
            document
              .querySelector("#cardsContainer")
              .appendChild(
                getSearchCard(
                  artifact.title,
                  artifact.authors,
                  `/artifacts/view?id=${artifact.id}`
                )
              );
          }

          document
            .querySelector("#cardsContainer")
            .appendChild(
              getSeeAllResults(
                searchQuery,
                `/search?searchQuery=${searchQuery}`
              )
            );

          searchSpinner.style.display = "none";
          searchGlass.style.display = "block";
        });
    }
  }, 250);
}

function clearChild(aNode) {
  while (aNode.firstChild) {
    aNode.removeChild(aNode.lastChild);
  }
}

/*
<a href="#" class="px-3 py-2 quick-search-dropdown-hover hover-only-bg border-bottom">
  <div class="d-flex justify-content-start align-items-center">
    <div class="bg-dark" style="width: 2.5rem; height: 2.5rem"></div>
    <div class="ml-2 d-flex flex-column justify-content-center align-items-start">
      <p class="font-weight-bolder m-0 text-truncate">Artifact Title</p>
      <p class="m-0 text-truncate">by Authors</p>
    </div>
  </div>
</a>
*/
function getSearchCard(titleText, authorsText, link) {
  var cardContainer = document.createElement("a");
  cardContainer.classList.add(
    "px-3",
    "py-2",
    "quick-search-dropdown-hover",
    "hover-only-bg",
    "border-bottom"
  );
  cardContainer.href = link;

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
  authors.classList.add("m-0", "text-truncate");
  authors.textContent = `by ${authorsText}`;

  cardContentDetailsContainer.appendChild(title);
  cardContentDetailsContainer.appendChild(authors);
  cardImgContainer.appendChild(cardImg);
  cardWrapper.appendChild(cardImgContainer);
  cardWrapper.appendChild(cardContentDetailsContainer);
  cardContainer.appendChild(cardWrapper);

  return cardContainer;
}

/*
<a href="#" class="px-3 py-2 quick-search-dropdown-seeAllResults-hover text-center">See all results for "searchQuery HERE"</a>
*/
function getSeeAllResults(searchQuery, link) {
  var seeAllResults = document.createElement("a");
  seeAllResults.classList.add(
    "px-3",
    "py-2",
    "quick-search-dropdown-seeAllResults-hover",
    "text-center"
  );
  seeAllResults.href = link;
  seeAllResults.textContent = `See all results for "${searchQuery}"`;
  return seeAllResults;
}
