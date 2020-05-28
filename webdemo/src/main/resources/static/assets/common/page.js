window.onload = function() {
	if (self != top) {
		if (window.location.pathname == '/login') {
			window.top.location = '/login';
		}
	}
}