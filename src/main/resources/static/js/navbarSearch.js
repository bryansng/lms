function searchBarForm(url) {
  var searchQuery = document.querySelector("#searchQuery").value;
  var searchSpinner = document.querySelector(".search-spinner");
  var searchGlass = document.querySelector(".search-glass");

  if (searchQuery !== "") {
    searchSpinner.style.display = "block";
    searchGlass.style.display = "none";

    clearChild(document.querySelector("#cardsContainer"));

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
            getSeeAllResults(searchQuery, `/search?searchQuery=${searchQuery}`)
          );

        searchSpinner.style.display = "none";
        searchGlass.style.display = "block";
      });
  } else {
    clearChild(document.querySelector("#cardsContainer"));
  }
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
      <p class="font-weight-bolder m-0">Artifact Title</p>
      <p class="m-0">by Authors</p>
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

  var cardImg = document.createElement("div");
  cardImg.style.width = "2.5rem";
  cardImg.style.height = "2.5rem";
  cardImg.classList.add("bg-dark");

  var titleAuthorsContainer = document.createElement("div");
  titleAuthorsContainer.classList.add(
    "ml-2",
    "d-flex",
    "flex-column",
    "justify-content-center",
    "align-items-start"
  );

  var title = document.createElement("p");
  title.classList.add("font-weight-bolder", "m-0");
  title.textContent = titleText;

  var authors = document.createElement("p");
  authors.classList.add("m-0");
  authors.textContent = `by ${authorsText}`;

  titleAuthorsContainer.appendChild(title);
  titleAuthorsContainer.appendChild(authors);
  cardWrapper.appendChild(cardImg);
  cardWrapper.appendChild(titleAuthorsContainer);
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

function searchBarForm1(id) {
  var form = $("#searchBarForm");
  $.ajax({
    url: form.attr("action"),
    data: form.serialize(),
    type: "get",
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
