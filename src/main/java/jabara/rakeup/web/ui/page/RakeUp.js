var RakeUp = {};

RakeUp.focus = function(pId) {
	var tag = document.getElementById(pId);
	if (tag) {
		tag.focus();
	}
};