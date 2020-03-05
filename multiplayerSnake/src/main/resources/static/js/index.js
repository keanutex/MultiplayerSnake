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
