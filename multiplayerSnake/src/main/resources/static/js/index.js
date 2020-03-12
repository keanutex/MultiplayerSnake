"use strict";

// ------------------------- styles -----------------------------------------
const setProp = prop => value => className => {
  document.querySelector(`.${className}`).style[prop] = value;
};

document.querySelector(".form-colorpicker").addEventListener("change", e => {
  const playerColor = e.target.value;
  const setToPlayerColor = setProp("color")(playerColor);

  setToPlayerColor("btn-submit");
  setToPlayerColor("form-input");
  setProp("backgroundColor")(playerColor)("snake-square");
  setProp("borderColor")(playerColor)("form-input");
});

// ------------------------- Storage -----------------------------------------
document.querySelector(".btn-submit").addEventListener("click", () => {
  window.localStorage.setItem(
    "user",
    JSON.stringify({
      username: document.querySelector(".form-input").value,
      color: document.querySelector(".form-colorpicker").value,
      playerId:("_" +Math.random().toString(36).substr(2, 9))

    })
  );
});
